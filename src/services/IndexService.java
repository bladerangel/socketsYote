package services;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.CasaLayout;
import models.ComunicacaoTCP;
import models.Tabuleiro;

import java.io.IOException;
import java.util.ArrayList;

public class IndexService {

    private VBox linhasTabuleiro;
    private HBox colunasTabuleiro;
    private ArrayList<CasaLayout> casas;
    private Tabuleiro tabuleiroJogo;
    private ComunicacaoTCP comunicacao;
    private int jogador;
    private String mensagemRecebida;
    private Pane tabuleiro;
    private Text numeroJogador;
    private TextArea chat;
    private TextField escrever;
    private Text turno;
    private Text numeroPecasAdversarias;
    private int posicaoInicial;

    public IndexService(Pane tabuleiro, Text numeroJogador, TextArea chat, TextField escrever, Text turno, Text numeroPecasAdversarias) {
        this.tabuleiro = tabuleiro;
        this.numeroJogador = numeroJogador;
        this.chat = chat;
        this.escrever = escrever;
        this.turno = turno;
        this.numeroPecasAdversarias = numeroPecasAdversarias;
    }

    public void criarTabuleiro() {
        tabuleiroJogo = new Tabuleiro();
        casas = new ArrayList<CasaLayout>();
        linhasTabuleiro = new VBox();
        tabuleiro.getChildren().add(linhasTabuleiro);
        for (int i = 0; i < 5; i++) {
            colunasTabuleiro = new HBox();
            tabuleiro.getChildren().add(colunasTabuleiro);
            linhasTabuleiro.getChildren().add(colunasTabuleiro);
            for (int j = 0; j < 6; j++) {
                CasaLayout casa = new CasaLayout();
                casas.add(casa);
                casa.setOnMouseClicked(event -> colocarPeca((CasaLayout) event.getSource()));
                colunasTabuleiro.getChildren().add(casa);
                casa.getCasa().setPosicao(casas.size() - 1);
            }
        }
        numeroJogador.setText("Você: Jogador " + jogador);
    }

    public void iniciarComunicacao() {
        try {
            comunicacao = new ComunicacaoTCP();
            comunicacao.iniciarServidor(9999);
            comunicacao.esperandoConexao();
            jogador = 1;
        } catch (IOException e) {
            try {
                comunicacao.iniciarCliente(9999);
                jogador = 2;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    public void chat() {
        comunicacao.enviarPacote(escrever.getText());
        chat.appendText(escrever.getText() + "\n");
        escrever.clear();
    }

    public void passarTurno() {
        tabuleiroJogo.setTurnoJogador();
        chat.appendText("O turno é do jogador" + tabuleiroJogo.getTurnoJogador());
        comunicacao.enviarPacote("turno:" + tabuleiroJogo.getTurnoJogador());
        turno.setText("Turno Atual: Jogador" + tabuleiroJogo.getTurnoJogador());
    }

    public void iniciarThreadRecebePacotes() {
        new Thread(() -> {
            while (true) {
                mensagemRecebida = comunicacao.recebePacote();
                if (mensagemRecebida.matches("^andar:\\d:\\d+:\\d+$")) {
                    String[] mensagem = mensagemRecebida.split(":");
                    casas.get(Integer.parseInt(mensagem[2])).removerPeca();
                    casas.get(Integer.parseInt(mensagem[3])).colocarPeca(Integer.parseInt(mensagem[1]));

                } else if (mensagemRecebida.matches("^colocar:\\d:\\d+$")) {
                    String[] mensagem = mensagemRecebida.split(":");
                    System.out.println(mensagem[2]);
                    casas.get(Integer.parseInt(mensagem[2])).colocarPeca(Integer.parseInt(mensagem[1]));

                } else if (mensagemRecebida.matches("^turno:\\d$")) {
                    String[] mensagem = mensagemRecebida.split(":");
                    chat.appendText("O turno é do jogador " + Integer.parseInt(mensagem[1]) + "\n");
                    tabuleiroJogo.setTurnoJogador();
                    turno.setText("Turno Atual: Jogador" + tabuleiroJogo.getTurnoJogador());

                } else if (mensagemRecebida.matches("^pecasAdversarias:\\d:\\d+$")) {
                    tabuleiroJogo.setPecas();
                    numeroPecasAdversarias.setText(tabuleiroJogo.getPecas() + " peças adversarias restantes");

                } else if (mensagemRecebida.matches("^reiniciar$")) {

                } else if (mensagemRecebida.matches("^desistir$")) {

                } else {
                    //System.out.println("mensagem= " + mensagemRecebida);
                    chat.appendText(mensagemRecebida + "\n");
                }

            }
        }).start();

    }

    public void colocarPeca(CasaLayout casa) {
        if (tabuleiroJogo.getTurnoJogador() == jogador) {

            /*if (casa.getCasa().getPeca().getJogador() == 0 && tabuleiroJogo.isEscolherCasa() && casa.verificarMovimento(posicaoInicial, casa.getCasa().getPosicao())) {
                if (posicaoInicial != -1) {
                    casas.get(posicaoInicial).removerPeca(tabuleiroJogo.getTurnoJogador());
                }
                casa.colocarPeca(tabuleiroJogo.getTurnoJogador());
                tabuleiroJogo.setEscolherCasa();
                comunicacao.enviarPacote("colocar:" + jogador + ":" + casa.getCasa().getPosicao());
                comunicacao.enviarPacote("pecasAdversarias:" + jogador + ":" + tabuleiroJogo.getPecas());
            } else if (casa.getCasa().getPeca().getJogador() == tabuleiroJogo.getTurnoJogador() && casa.getCasa().getPeca().getJogador() != 0 && !tabuleiroJogo.isTirouPeca()) {
                tabuleiroJogo.setEscolherCasa();
                tabuleiroJogo.setTirouPeca();
                posicaoInicial = casa.getCasa().getPosicao();
                //casa.removerPeca(tabuleiroJogo.getTurnoJogador());
            }*/
            //System.out.println("clicou");
            verificarMovimento(posicaoInicial, casa.getCasa().getPosicao());
        }
    }


    public void removerPeca(Text numeroPecas) {
        if (tabuleiroJogo.getTurnoJogador() == jogador && !tabuleiroJogo.isEscolherCasa() && !tabuleiroJogo.isJaJogou()) {
            tabuleiroJogo.setPecas();
            posicaoInicial = -1;
            tabuleiroJogo.setEscolherCasa();
            numeroPecas.setText(tabuleiroJogo.getPecas() + " peças restantes");
        }
    }

    public void verificarMovimento(int posicaoInicial, int posicaoFinal) {
        if (posicaoInicial == -1 && tabuleiroJogo.isEscolherCasa() && !tabuleiroJogo.isJaJogou()) {
            tabuleiroJogo.setJaJogou();
            tabuleiroJogo.setEscolherCasa();
            casas.get(posicaoFinal).colocarPeca(tabuleiroJogo.getTurnoJogador());
            comunicacao.enviarPacote("colocar:" + jogador + ":" + posicaoFinal);
            comunicacao.enviarPacote("pecasAdversarias:" + jogador + ":" + tabuleiroJogo.getPecas());
        } else if (casas.get(posicaoFinal).getCasa().getPeca().getJogador() == tabuleiroJogo.getTurnoJogador() && !tabuleiroJogo.isJaJogou()) {
            this.posicaoInicial = posicaoFinal;
            tabuleiroJogo.setEscolherCasa(true);
            System.out.println("quero andar");
        } else if (casas.get(posicaoFinal).getCasa().getPeca().getJogador() == 0 && tabuleiroJogo.isEscolherCasa() && !tabuleiroJogo.isJaJogou()) {
            verificarMovimentoAndar(posicaoInicial, posicaoFinal);
            System.out.println("andou");
        }
    }

    public void verificarMovimentoAndar(int posicaoInicial, int posicaoFinal) {

        switch (posicaoFinal - posicaoInicial) {
            case 1:
                mover(posicaoInicial, posicaoFinal);
                break;
            case -1:
                mover(posicaoInicial, posicaoFinal);
                break;
            case 6:
                mover(posicaoInicial, posicaoFinal);
                break;
            case -6:
                mover(posicaoInicial, posicaoFinal);
                break;
            default:
                tabuleiroJogo.setEscolherCasa();
                break;
        }

    }

    public void mover(int posicaoInicial, int posicaoFinal) {
        casas.get(posicaoInicial).removerPeca();
        casas.get(posicaoFinal).colocarPeca(tabuleiroJogo.getTurnoJogador());
        comunicacao.enviarPacote("andar:" + jogador + ":" + posicaoInicial + ":" + posicaoFinal);
        //tabuleiroJogo.setEscolherCasa();
        tabuleiroJogo.setJaJogou();
    }

    public Tabuleiro getTabuleiroJogo() {
        return tabuleiroJogo;
    }


    public ComunicacaoTCP getComunicacao() {
        return comunicacao;
    }

    public int getJogador() {
        return jogador;
    }
}

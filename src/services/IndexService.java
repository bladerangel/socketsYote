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
    private Text turnoAtual;
    private Text numeroPecasAdversarias;
    private int posicaoInicial;
    private Button removerPeca;
    private Text numeroPecas;
    private Button passarTurno;

    public IndexService(Pane tabuleiro, Text numeroJogador, TextArea chat, TextField escrever, Text turnoAtual, Text numeroPecasAdversarias, Button removerPeca, Text numeroPecas, Button passarTurno) {
        this.tabuleiro = tabuleiro;
        this.numeroJogador = numeroJogador;
        this.chat = chat;
        this.escrever = escrever;
        this.turnoAtual = turnoAtual;
        this.numeroPecasAdversarias = numeroPecasAdversarias;
        this.removerPeca = removerPeca;
        this.numeroPecas = numeroPecas;
        this.passarTurno = passarTurno;
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
                casa.setOnMouseClicked(event -> movimentarPecaTabuleiro((CasaLayout) event.getSource()));
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

    public void adicionarMensagemChat(String mensagem) {
        chat.appendText(mensagem + "\n");
    }

    public void atualizarTurnoAtual() {
        turnoAtual.setText("Turno Atual: Jogador " + tabuleiroJogo.getTurnoJogador());
    }

    public void atualizarNumeroPecas(boolean pecasAdversarias) {
        tabuleiroJogo.setPecas();
        adicionarMensagemChat("O jogador " + tabuleiroJogo.getTurnoJogador() + " tirou 1 peça");
        if (!pecasAdversarias)
            numeroPecas.setText(tabuleiroJogo.getPecas() + " peças restantes");
        else
            numeroPecasAdversarias.setText(tabuleiroJogo.getPecas() + " peças adversarias restantes");
    }


    public void enviarMensagemChat() {
        comunicacao.enviarPacote(escrever.getText());
        adicionarMensagemChat(escrever.getText());
        escrever.clear();
    }

    public void removerPeca() {
        removerPeca.setDisable(true);
        atualizarNumeroPecas(false);
        comunicacao.enviarPacote("atualizarNumeroPecasAdversarias");
    }

    public void adicionarPecaTabuleiro(int posicao, boolean enviarPacote) {
        casas.get(posicao).colocarPeca(tabuleiroJogo.getTurnoJogador());
        if (enviarPacote) {
            comunicacao.enviarPacote("adicionarPecaTabuleiro:" + posicao);
            passarTurno(true);
        }
    }

    public void selecionarPecaTabuleiro(int posicaoFinal) {
        this.posicaoInicial = posicaoFinal;
    }

    public void passarTurno(boolean enviarPacote) {
        tabuleiroJogo.setTurnoJogador();
        adicionarMensagemChat("O turno é do jogador " + tabuleiroJogo.getTurnoJogador());
        atualizarTurnoAtual();
        if (enviarPacote) {
            comunicacao.enviarPacote("passarTurno");
            removerPeca.setDisable(true);
            passarTurno.setDisable(true);
        } else {
            removerPeca.setDisable(false);
        }
    }

    public void movimentarPecaTabuleiro(CasaLayout casa) {
        if (tabuleiroJogo.getTurnoJogador() == jogador) {
            verificarMovimento(posicaoInicial, casa.getCasa().getPosicao());
        }
    }

    public void verificarMovimento(int posicaoInicial, int posicaoFinal) {
        if (removerPeca.isDisable() && casas.get(posicaoFinal).getCasa().getPeca().getJogador() == 0 && passarTurno.isDisable()) { //adicionar Peça tabuleiro
            adicionarPecaTabuleiro(posicaoFinal, true);
        } else if (casas.get(posicaoFinal).getCasa().getPeca().getJogador() == tabuleiroJogo.getTurnoJogador()) { //escolher peça
            selecionarPecaTabuleiro(posicaoFinal);
        } else if (!removerPeca.isDisable() && casas.get(posicaoFinal).getCasa().getPeca().getJogador() == 0 && posicaoInicial != posicaoFinal) {//andar ou capturar uma peça
            verificarMovimentoAndar(posicaoInicial, posicaoFinal);
            verificaCaptura(posicaoInicial, posicaoFinal);
        } else if (casas.get(posicaoFinal).getCasa().getPeca().getJogador() == 0 && posicaoInicial != posicaoFinal && !tabuleiroJogo.isRemoverOutraPeca()) {//capturar multipla peças
            verificaCaptura(posicaoInicial, posicaoFinal);
        } else if (tabuleiroJogo.isRemoverOutraPeca()) {//remover peça ao realizar a captura multipla
            removerOutraPeca(posicaoFinal, true);
        }
    }


    public void verificarMovimentoAndar(int posicaoInicial, int posicaoFinal) {

        switch (posicaoFinal - posicaoInicial) {
            case 1:
                andarPeca(posicaoInicial, posicaoFinal, true);
                break;
            case -1:
                andarPeca(posicaoInicial, posicaoFinal, true);
                break;
            case 6:
                andarPeca(posicaoInicial, posicaoFinal, true);
                break;
            case -6:
                andarPeca(posicaoInicial, posicaoFinal, true);
                break;
            default:
                break;
        }

    }

    public void verificaCaptura(int posicaoInicial, int posicaoFinal) {
        switch (posicaoFinal - posicaoInicial) {
            case 2:
                capturarPeca(posicaoInicial, posicaoFinal, true);
                break;
            default:
                break;
        }
    }

    public void andarPeca(int posicaoInicial, int posicaoFinal, boolean enviarPacote) {
        casas.get(posicaoInicial).removerPeca(tabuleiroJogo.getTurnoJogador());
        casas.get(posicaoFinal).colocarPeca(tabuleiroJogo.getTurnoJogador());
        adicionarMensagemChat("O jogador " + tabuleiroJogo.getTurnoJogador() + " moveu a peça da casa " + posicaoInicial + " para " + posicaoFinal);
        if (enviarPacote) {
            comunicacao.enviarPacote("andarPeca:" + posicaoInicial + ":" + posicaoFinal);
            passarTurno(true);
        }

    }

    public void capturarPeca(int posicaoInicial, int posicaoFinal, boolean enviarPacote) {
        int peca = casas.get(posicaoFinal - 1).getCasa().getPeca().getJogador();
        if (peca != tabuleiroJogo.getTurnoJogador() && peca != 0) {
            casas.get(posicaoInicial).removerPeca(tabuleiroJogo.getTurnoJogador());
            casas.get(posicaoFinal - 1).removerPeca(peca);
            casas.get(posicaoFinal).colocarPeca(tabuleiroJogo.getTurnoJogador());
            tabuleiroJogo.setRemoverOutraPeca(true);
            adicionarMensagemChat("O jogador " + tabuleiroJogo.getTurnoJogador() + " capturou a peça da casa " + (posicaoFinal - 1));
            if (enviarPacote) {
                removerPeca.setDisable(true);
                passarTurno.setDisable(false);
                comunicacao.enviarPacote("capturarPeca:" + posicaoInicial + ":" + posicaoFinal);
            }
        }
    }

    public void removerOutraPeca(int posicao, boolean enviarPacote) {
        int peca = casas.get(posicao).getCasa().getPeca().getJogador();
        if (peca != tabuleiroJogo.getTurnoJogador() && peca != 0) {
            casas.get(posicao).removerPeca(peca);
            tabuleiroJogo.setRemoverOutraPeca(false);
            adicionarMensagemChat("O jogador " + tabuleiroJogo.getTurnoJogador() + " removeu a peça da casa " + posicao);
            if (enviarPacote)
                comunicacao.enviarPacote("removerOutraPeca:" + posicao);
        }
    }

    public void iniciarThreadRecebePacotes() {
        new Thread(() -> {
            while (true) {
                mensagemRecebida = comunicacao.recebePacote();
                if (mensagemRecebida.matches("^andarPeca:\\d+:\\d+$")) {
                    String[] mensagem = mensagemRecebida.split(":");
                    andarPeca(Integer.parseInt(mensagem[1]), Integer.parseInt(mensagem[2]), false);
                } else if (mensagemRecebida.matches("^adicionarPecaTabuleiro:\\d+$")) {
                    adicionarPecaTabuleiro(Integer.parseInt(mensagemRecebida.split(":")[1]), false);
                } else if (mensagemRecebida.matches("^capturarPeca:\\d+:\\d+$")) {
                    String[] mensagem = mensagemRecebida.split(":");
                    capturarPeca(Integer.parseInt(mensagem[1]), Integer.parseInt(mensagem[2]), false);
                } else if (mensagemRecebida.matches("^removerOutraPeca:\\d+$")) {
                    removerOutraPeca(Integer.parseInt(mensagemRecebida.split(":")[1]), false);
                } else if (mensagemRecebida.matches("^passarTurno$")) {
                    passarTurno(false);
                } else if (mensagemRecebida.matches("^atualizarNumeroPecasAdversarias$")) {
                    atualizarNumeroPecas(true);
                } else if (mensagemRecebida.matches("^reiniciar$")) {

                } else if (mensagemRecebida.matches("^desistir$")) {

                } else {
                    chat.appendText(mensagemRecebida + "\n");
                }

            }
        }).start();
    }
}

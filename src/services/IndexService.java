package services;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.CasaLayout;
import models.ComunicacaoTCP;
import models.Jogador;
import models.Tabuleiro;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class IndexService {

    private VBox linhasTabuleiro;
    private HBox colunasTabuleiro;
    private ArrayList<CasaLayout> casas;
    private Pane tabuleiro;
    private Text numeroJogador;
    private TextArea chat;
    private TextField escrever;
    private Text turnoAtual;
    private Text numeroPecasAdversarias;
    private Button removerPeca;
    private Text numeroPecas;
    private Button passarTurno;

    private Tabuleiro tabuleiroJogo;
    private ComunicacaoTCP comunicacao;
    private String mensagemRecebida;

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

    public void criarTabuleiro(Jogador jogador, Jogador jogadorAdversario, Jogador turnoJogador) {
        System.out.println("entrou aki");
        tabuleiroJogo = new Tabuleiro(jogador, jogadorAdversario, turnoJogador);
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

        if (tabuleiroJogo.getTurnoJogador() == tabuleiroJogo.getJogador())
            removerPeca.setDisable(false);
        else
            removerPeca.setDisable(true);
        passarTurno.setDisable(true);
        numeroJogador.setText("Você: Jogador " + tabuleiroJogo.getJogador().getTipo());
        numeroPecas.setText(tabuleiroJogo.getJogador().getQuantidadePecasForaTabuleiro() + " peças restantes - " + tabuleiroJogo.getJogador().totalPecas() + " no total");
        numeroPecasAdversarias.setText(tabuleiroJogo.getJogadorAdversario().getQuantidadePecasForaTabuleiro() + " peças adversarias restantes - " + tabuleiroJogo.getJogadorAdversario().totalPecas() + " no total");
    }

    public void iniciarNovoJogo(boolean servidor) {
        Jogador jogador = new Jogador(1, 2, 0);
        Jogador jogadorAdversario = new Jogador(2, 1, 0);
        if (servidor)
            Platform.runLater(() -> {
                criarTabuleiro(jogador, jogadorAdversario, jogador);
            });

        else
            Platform.runLater(() -> {
                criarTabuleiro(jogadorAdversario, jogador, jogador);
            });
    }

    public void iniciarComunicacao() {
        try {

            comunicacao = new ComunicacaoTCP();
            comunicacao.iniciarServidor(9999);
            comunicacao.esperandoConexao();
            iniciarNovoJogo(true);
        } catch (IOException e) {
            try {
                iniciarNovoJogo(false);
                comunicacao.iniciarCliente(9999);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    public void adicionarMensagemChat(String mensagem) {
        chat.appendText(mensagem + "\n");
    }

    public void atualizarTurnoAtual() {
        turnoAtual.setText("Turno Atual: Jogador " + tabuleiroJogo.getTurnoJogador().getTipo());
    }

    public void enviarMensagemChat() {
        comunicacao.enviarPacote(escrever.getText());
        adicionarMensagemChat(escrever.getText());
        escrever.clear();
    }

    public void removerPeca(boolean enviarPacote) {
        adicionarMensagemChat("O jogador " + tabuleiroJogo.getTurnoJogador().getTipo() + " tirou 1 peça");
        if (enviarPacote) {
            removerPeca.setDisable(true);
            comunicacao.enviarPacote("removerPeca");
        }
    }

    public void adicionarPecaTabuleiro(int posicao, boolean enviarPacote) {
        tabuleiroJogo.getTurnoJogador().removerPecasForaTabuleiro();
        casas.get(posicao).colocarPeca(tabuleiroJogo.getTurnoJogador());
        if (enviarPacote) {
            numeroPecas.setText(tabuleiroJogo.getTurnoJogador().getQuantidadePecasForaTabuleiro() + " peças restantes - " + tabuleiroJogo.getTurnoJogador().totalPecas() + " no total");
            comunicacao.enviarPacote("adicionarPecaTabuleiro:" + posicao);
            passarTurno(true);
        } else {
            numeroPecasAdversarias.setText(tabuleiroJogo.getJogadorAdversario().getQuantidadePecasForaTabuleiro() + " peças adversarias restantes - " + tabuleiroJogo.getJogadorAdversario().totalPecas() + " no total");
        }
    }

    public void selecionarPecaTabuleiro(int posicaoFinal) {
        tabuleiroJogo.setPosicaoInicial(posicaoFinal);
    }

    public void passarTurno(boolean enviarPacote) {
        tabuleiroJogo.mudarTurnoJogador();
        adicionarMensagemChat("O turno é do jogador " + tabuleiroJogo.getTurnoJogador().getTipo());
        atualizarTurnoAtual();
        if (enviarPacote) {
            comunicacao.enviarPacote("passarTurno");
            removerPeca.setDisable(true);
            passarTurno.setDisable(true);
        } else {
            if (tabuleiroJogo.getTurnoJogador().getQuantidadePecasForaTabuleiro() > 0)
                removerPeca.setDisable(false);
        }
    }

    public void movimentarPecaTabuleiro(CasaLayout casa) {
        if (tabuleiroJogo.getTurnoJogador() == tabuleiroJogo.getJogador()) {
            verificarMovimento(tabuleiroJogo.getPosicaoInicial(), casa.getCasa().getPosicao());
        }
    }

    public void janelaAlerta(String titulo, String cabecalho, String conteudo) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(titulo);
            alert.setHeaderText(cabecalho);
            alert.setContentText(conteudo);
            alert.showAndWait();
        });
    }

    public void reiniciarJogo() {
        adicionarMensagemChat("O jogo foi reiniciado!");
        if (tabuleiroJogo.getJogador().getTipo() == 1) {
            iniciarNovoJogo(true);
        } else {
            iniciarNovoJogo(false);
        }
    }

    public void desistirPartida() {
        adicionarMensagemChat("O jogador " + tabuleiroJogo.getJogador().getTipo() + " desistiu da partida!");
        comunicacao.enviarPacote("desistirJogo:" + tabuleiroJogo.getJogador().getTipo());
        reiniciarJogo();
        comunicacao.enviarPacote("reiniciarJogo");
    }

    public void recebePacoteDesistirPartida(String jogador) {
        adicionarMensagemChat("O jogador " + jogador + " desistiu da partida!");
    }


    public void verificarVencedor(boolean enviarPacote) {
        if (enviarPacote) {
            if (tabuleiroJogo.getJogadorAdversario().totalPecas() == 0) {
                janelaAlerta("Resultado da partida", null, "Você ganhou a partida!");
                adicionarMensagemChat("O jogador " + tabuleiroJogo.getTurnoJogador().getTipo() + " ganhou a partida!");
                comunicacao.enviarPacote("ganhouJogo");
                reiniciarJogo();
                comunicacao.enviarPacote("reiniciarJogo");
            }
        } else {
            adicionarMensagemChat("O jogador " + tabuleiroJogo.getTurnoJogador().getTipo() + " ganhou a partida!");
            janelaAlerta("Resultado da partida", null, "Você perdeu a partida!");
        }

    }

    public void verificarEmpate(boolean enviarPacote) {
        if (enviarPacote) {
            if (tabuleiroJogo.getTurnoJogador().totalPecas() <= 3 && tabuleiroJogo.getJogadorAdversario().totalPecas() <= 3) {
                janelaAlerta("Resultado da partida", null, "Deu empate!");
                adicionarMensagemChat("O partida terminou em empate");
                comunicacao.enviarPacote("empateJogo");
                reiniciarJogo();
                comunicacao.enviarPacote("reiniciarJogo");
            }
        } else {
            adicionarMensagemChat("O partida terminou em empate");
            janelaAlerta("Resultado da partida", null, "Deu empate!");
        }
    }

    public void verificarMovimento(int posicaoInicial, int posicaoFinal) {
        if (removerPeca.isDisable() && passarTurno.isDisable() && casas.get(posicaoFinal).getCasa().getPeca().getTipo() == 0 && tabuleiroJogo.getTurnoJogador().getQuantidadePecasForaTabuleiro() > 0) { //adicionar Peça tabuleiro
            adicionarPecaTabuleiro(posicaoFinal, true);
        } else if (passarTurno.isDisable() && casas.get(posicaoFinal).getCasa().getPeca().getTipo() == tabuleiroJogo.getTurnoJogador().getTipo()) { //escolher peça
            selecionarPecaTabuleiro(posicaoFinal);
        } else if (passarTurno.isDisable() && casas.get(posicaoFinal).getCasa().getPeca().getTipo() == 0 && posicaoInicial != -1 && posicaoInicial != posicaoFinal) {//andar ou capturar uma peça
            verificarMovimentoAndar(posicaoInicial, posicaoFinal);
            verificaCaptura(posicaoInicial, posicaoFinal);
        } else if (!passarTurno.isDisable() && casas.get(posicaoFinal).getCasa().getPeca().getTipo() == 0 && posicaoInicial != -1 && posicaoInicial != posicaoFinal && !tabuleiroJogo.getTurnoJogador().isRemoverOutraPeca()) {//capturar multipla peças
            verificaCaptura(posicaoInicial, posicaoFinal);
        } else if (!passarTurno.isDisable() && tabuleiroJogo.getTurnoJogador().isRemoverOutraPeca()) {//remover peça ao realizar a captura multipla
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
                capturarPeca(posicaoInicial, posicaoFinal, posicaoInicial + 1, true);
                break;
            case -2:
                capturarPeca(posicaoInicial, posicaoFinal, posicaoInicial - 1, true);
                break;
            case 12:
                capturarPeca(posicaoInicial, posicaoFinal, posicaoInicial + 6, true);
                break;
            case -12:
                capturarPeca(posicaoInicial, posicaoFinal, posicaoInicial - 6, true);
                break;
            default:
                break;
        }
    }

    public void andarPeca(int posicaoInicial, int posicaoFinal, boolean enviarPacote) {
        casas.get(posicaoInicial).removerPeca(tabuleiroJogo.getTurnoJogador());
        casas.get(posicaoFinal).colocarPeca(tabuleiroJogo.getTurnoJogador());
        adicionarMensagemChat("O jogador " + tabuleiroJogo.getTurnoJogador().getTipo() + " moveu a peça da casa " + posicaoInicial + " para " + posicaoFinal);
        if (enviarPacote) {
            comunicacao.enviarPacote("andarPeca:" + posicaoInicial + ":" + posicaoFinal);
            passarTurno(true);
        }

    }

    public void capturarPeca(int posicaoInicial, int posicaoFinal, int posicaoVerificar, boolean enviarPacote) {
        int peca = casas.get(posicaoVerificar).getCasa().getPeca().getTipo();
        System.out.println(tabuleiroJogo.getJogadorAdversario().getTipo());
        if (peca != tabuleiroJogo.getTurnoJogador().getTipo() && peca != 0) {
            casas.get(posicaoInicial).removerPeca(tabuleiroJogo.getTurnoJogador());
            casas.get(posicaoFinal).colocarPeca(tabuleiroJogo.getTurnoJogador());
            tabuleiroJogo.getTurnoJogador().setRemoverOutraPeca(true);
            adicionarMensagemChat("O jogador " + tabuleiroJogo.getTurnoJogador().getTipo() + " capturou a peça da casa " + posicaoVerificar);
            selecionarPecaTabuleiro(posicaoFinal);
            if (enviarPacote) {
                tabuleiroJogo.getJogadorAdversario().removerPecasDentroTabuleiro();
                numeroPecasAdversarias.setText(tabuleiroJogo.getJogadorAdversario().getQuantidadePecasForaTabuleiro() + " peças adversarias restantes - " + tabuleiroJogo.getJogadorAdversario().totalPecas() + " no total");
                casas.get(posicaoVerificar).removerPeca(tabuleiroJogo.getJogadorAdversario());
                removerPeca.setDisable(true);
                passarTurno.setDisable(false);
                verificarEmpate(true);
                verificarVencedor(true);
                comunicacao.enviarPacote("capturarPeca:" + posicaoInicial + ":" + posicaoFinal + ":" + posicaoVerificar);

            } else {
                tabuleiroJogo.getJogador().removerPecasDentroTabuleiro();
                casas.get(posicaoVerificar).removerPeca(tabuleiroJogo.getJogador());
                numeroPecas.setText(tabuleiroJogo.getJogador().getQuantidadePecasForaTabuleiro() + " peças restantes - " + tabuleiroJogo.getJogador().totalPecas() + " no total");
            }
        }
    }

    public void removerOutraPeca(int posicao, boolean enviarPacote) {
        int peca = casas.get(posicao).getCasa().getPeca().getTipo();
        if (peca != tabuleiroJogo.getTurnoJogador().getTipo() && peca != 0) {
            tabuleiroJogo.getTurnoJogador().setRemoverOutraPeca(false);
            adicionarMensagemChat("O jogador " + tabuleiroJogo.getTurnoJogador().getTipo() + " removeu a peça da casa " + posicao);
            if (enviarPacote) {
                casas.get(posicao).removerPeca(tabuleiroJogo.getJogadorAdversario());
                tabuleiroJogo.getJogadorAdversario().removerPecasDentroTabuleiro();
                numeroPecasAdversarias.setText(tabuleiroJogo.getJogadorAdversario().getQuantidadePecasForaTabuleiro() + " peças adversarias restantes - " + tabuleiroJogo.getJogadorAdversario().totalPecas() + " no total");
                verificarEmpate(true);
                verificarVencedor(true);
                comunicacao.enviarPacote("removerOutraPeca:" + posicao);
            } else {
                tabuleiroJogo.getJogador().removerPecasDentroTabuleiro();
                casas.get(posicao).removerPeca(tabuleiroJogo.getJogador());
                numeroPecas.setText(tabuleiroJogo.getJogador().getQuantidadePecasForaTabuleiro() + " peças restantes - " + tabuleiroJogo.getJogador().totalPecas() + " no total");
            }

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
                } else if (mensagemRecebida.matches("^capturarPeca:\\d+:\\d+:\\d+$")) {
                    String[] mensagem = mensagemRecebida.split(":");
                    capturarPeca(Integer.parseInt(mensagem[1]), Integer.parseInt(mensagem[2]), Integer.parseInt(mensagem[3]), false);
                } else if (mensagemRecebida.matches("^removerOutraPeca:\\d+$")) {
                    removerOutraPeca(Integer.parseInt(mensagemRecebida.split(":")[1]), false);
                } else if (mensagemRecebida.matches("^passarTurno$")) {
                    passarTurno(false);
                } else if (mensagemRecebida.matches("^removerPeca$")) {
                    removerPeca(false);
                } else if (mensagemRecebida.matches("^ganhouJogo")) {
                    verificarVencedor(false);
                } else if (mensagemRecebida.matches("^empateJogo$")) {
                    verificarEmpate(false);
                } else if (mensagemRecebida.matches("^reiniciarJogo$")) {
                    reiniciarJogo();
                } else if (mensagemRecebida.matches("^desistirJogo:\\d$")) {
                    recebePacoteDesistirPartida(mensagemRecebida.split(":")[1]);
                } else {
                    chat.appendText(mensagemRecebida + "\n");
                }

            }
        }).start();
    }
}

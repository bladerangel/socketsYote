package services;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

import models.CasaBotao;
import models.ComunicacaoTCP;
import models.Jogador;
import models.Tabuleiro;

public class IndexService {

    private Pane tabuleiroPane;
    private Text numeroPecas;
    private Text numeroPecasAdversarias;
    private Text tipoJogador;
    private Text turnoAtual;
    private TextField escreverMensagem;
    private TextArea chat;
    private Button pegarPeca;
    private Button passarTurno;

    private ArrayList<CasaBotao> casasTabuleiro;
    private Tabuleiro tabuleiro;
    private ComunicacaoTCP comunicacao;

    public IndexService(Pane tabuleiroPane, Text numeroPecas, Text numeroPecasAdversarias, Text tipoJogador, Text turnoAtual, TextField escreverMensagem, TextArea chat, Button pegarPeca, Button passarTurno) {
        this.tabuleiroPane = tabuleiroPane;
        this.numeroPecas = numeroPecas;
        this.numeroPecasAdversarias = numeroPecasAdversarias;
        this.tipoJogador = tipoJogador;
        this.turnoAtual = turnoAtual;
        this.escreverMensagem = escreverMensagem;
        this.chat = chat;
        this.pegarPeca = pegarPeca;
        this.passarTurno = passarTurno;
    }

    public void iniciarComunicacao() {
        try {
            comunicacao = new ComunicacaoTCP();
            comunicacao.iniciarServidor(9999);
            alerta("Iniciar Partida", null, "Aguarde o jogador 2 conectar-se ....");
            comunicacao.esperandoConexao();
            iniciarNovoJogo(true);
        } catch (IOException e) {
            try {
                janelaAlerta("Iniciar Partida", null, "O jogador 2 se conectou!");
                iniciarNovoJogo(false);
                comunicacao.iniciarCliente(9999);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void iniciarNovoJogo(boolean servidor) {
        Jogador jogador = new Jogador(1, 2, 0);
        Jogador jogadorAdversario = new Jogador(2, 1, 0);
        if (servidor) {
            Platform.runLater(() -> criarTabuleiro(jogador, jogadorAdversario, jogador));
        } else {
            Platform.runLater(() -> criarTabuleiro(jogadorAdversario, jogador, jogador));
        }
    }


    public void criarTabuleiro(Jogador jogador, Jogador jogadorAdversario, Jogador turnoJogador) {
        VBox linhasTabuleiro = new VBox();
        tabuleiroPane.getChildren().add(linhasTabuleiro);
        tabuleiro = new Tabuleiro(jogador, jogadorAdversario, turnoJogador);
        casasTabuleiro = new ArrayList<CasaBotao>();
        for (int i = 0; i < Tabuleiro.QUANTIDADE_LINHAS; i++) {
            HBox colunasTabuleiro = new HBox();
            tabuleiroPane.getChildren().add(colunasTabuleiro);
            linhasTabuleiro.getChildren().add(colunasTabuleiro);
            for (int j = 0; j < Tabuleiro.QUANTIDADE_COLUNAS; j++) {
                CasaBotao casa = new CasaBotao();
                casasTabuleiro.add(casa);
                casa.setOnMouseClicked(event -> movimentarPeca((CasaBotao) event.getSource()));
                colunasTabuleiro.getChildren().add(casa);
                casa.getCasa().setPosicao(casasTabuleiro.size() - 1);
            }
        }

        if (tabuleiro.getTurnoJogador() == tabuleiro.getJogador()) {
            desabilitarBotaoPegarPeca(false);
        } else {
            desabilitarBotaoPegarPeca(true);
        }

        desabilitarBotaoPassarTurno(true);
        setTextTipoJogador();
        setTextNumeroPecas();
        setTextNumeroPecasAdversarias();
    }

    public void setTextTipoJogador() {
        tipoJogador.setText("Você: Jogador " + tabuleiro.getJogador().getTipo());
    }

    public void setTextNumeroPecas() {
        numeroPecas.setText(tabuleiro.getJogador().getQuantidadePecasForaTabuleiro() + " peças restantes - " + tabuleiro.getJogador().totalPecas() + " no total");
    }

    public void setTextNumeroPecasAdversarias() {
        numeroPecasAdversarias.setText(tabuleiro.getJogadorAdversario().getQuantidadePecasForaTabuleiro() + " peças adversarias restantes - " + tabuleiro.getJogadorAdversario().totalPecas() + " no total");
    }

    public void setTextTurnoAtual() {
        turnoAtual.setText("Turno Atual: Jogador " + tabuleiro.getTurnoJogador().getTipo());
    }

    public void desabilitarBotaoPegarPeca(boolean desabilitar) {
        pegarPeca.setDisable(desabilitar);
    }

    public void desabilitarBotaoPassarTurno(boolean desabilitar) {
        passarTurno.setDisable(desabilitar);
    }

    public void adicionarMensagemChat(String mensagem) {
        chat.appendText(mensagem + "\n");
    }

    public void enviarPacoteMensagemChat() {
        comunicacao.enviarPacote(escreverMensagem.getText());
        adicionarMensagemChat(escreverMensagem.getText());
        limparMensagem();
    }

    public void limparMensagem() {
        escreverMensagem.clear();
    }

    public void pegarPeca() {
        adicionarMensagemChat("O jogador " + tabuleiro.getTurnoJogador().getTipo() + " tirou 1 peça");
    }

    public void enviarPacotePegarPeca() {
        pegarPeca();
        desabilitarBotaoPegarPeca(true);
        comunicacao.enviarPacote("removerPeca");
    }

    public void recebePacotePegarPeca() {
        pegarPeca();
    }

    public void passarTurno() {
        tabuleiro.mudarTurnoJogador();
        adicionarMensagemChat("O turno é do jogador " + tabuleiro.getTurnoJogador().getTipo());
        setTextTurnoAtual();
    }

    public void enviarPacotePassarTurno() {
        passarTurno();
        comunicacao.enviarPacote("passarTurno");
        desabilitarBotaoPegarPeca(true);
        desabilitarBotaoPassarTurno(true);
    }

    public void receberPacotePassarTurno() {
        passarTurno();
        if (tabuleiro.getTurnoJogador().getQuantidadePecasForaTabuleiro() > 0)
            desabilitarBotaoPegarPeca(false);
    }

    public void adicionarPeca(int posicao) {
        tabuleiro.getTurnoJogador().removerPecasForaTabuleiro();
        casasTabuleiro.get(posicao).colocarPeca(tabuleiro.getTurnoJogador());
    }

    public void enviarPacoteAdicionarPeca(int posicao) {
        adicionarPeca(posicao);
        setTextNumeroPecas();
        comunicacao.enviarPacote("adicionarPeca:" + posicao);
        enviarPacotePassarTurno();
    }

    public void receberPacoteAdicionarPeca(int posicao) {
        adicionarPeca(posicao);
        setTextNumeroPecasAdversarias();
    }

    public void selecionarPeca(int posicaoFinal) {
        tabuleiro.setPosicaoInicial(posicaoFinal);
    }

    public void movimentarPeca(CasaBotao casa) {
        if (tabuleiro.getTurnoJogador() == tabuleiro.getJogador()) {
            verificarMovimento(tabuleiro.getPosicaoInicial(), casa.getCasa().getPosicao());
        }
    }

    public void verificarMovimento(int posicaoInicial, int posicaoFinal) {
        if (pegarPeca.isDisable() && passarTurno.isDisable() && tabuleiro.getTurnoJogador().getQuantidadePecasForaTabuleiro() > 0 && casasTabuleiro.get(posicaoFinal).getCasa().getPeca().getTipo() == 0) { //adicionar Peça tabuleiro
            enviarPacoteAdicionarPeca(posicaoFinal);
        } else if (passarTurno.isDisable() && casasTabuleiro.get(posicaoFinal).getCasa().getPeca().getTipo() == tabuleiro.getTurnoJogador().getTipo()) { //escolher peça
            selecionarPeca(posicaoFinal);
        } else if (passarTurno.isDisable() && casasTabuleiro.get(posicaoFinal).getCasa().getPeca().getTipo() == 0 && posicaoInicial != -1 && posicaoInicial != posicaoFinal) {//andar ou capturar uma peça
            verificarMovimentoAndar(posicaoInicial, posicaoFinal);
            verificaCaptura(posicaoInicial, posicaoFinal);
        } else if (!passarTurno.isDisable() && !tabuleiro.getTurnoJogador().isRemoverOutraPeca() && casasTabuleiro.get(posicaoFinal).getCasa().getPeca().getTipo() == 0 && posicaoInicial != -1 && posicaoInicial != posicaoFinal) {//capturar multipla peças
            verificaCaptura(posicaoInicial, posicaoFinal);
        } else if (!passarTurno.isDisable() && tabuleiro.getTurnoJogador().isRemoverOutraPeca()) {//remover peça ao realizar a captura multipla
            enviarPacoteRemoverOutraPeca(posicaoFinal);
        }
    }

    public void verificarMovimentoAndar(int posicaoInicial, int posicaoFinal) {
        switch (posicaoFinal - posicaoInicial) {
            case 1:
                enviarPacoteAndarPeca(posicaoInicial, posicaoFinal);
                break;
            case -1:
                enviarPacoteAndarPeca(posicaoInicial, posicaoFinal);
                break;
            case 6:
                enviarPacoteAndarPeca(posicaoInicial, posicaoFinal);
                break;
            case -6:
                enviarPacoteAndarPeca(posicaoInicial, posicaoFinal);
                break;
            default:
                break;
        }
    }

    public void andarPecar(int posicaoInicial, int posicaoFinal) {
        casasTabuleiro.get(posicaoInicial).removerPeca(tabuleiro.getTurnoJogador());
        casasTabuleiro.get(posicaoFinal).colocarPeca(tabuleiro.getTurnoJogador());
        adicionarMensagemChat("O jogador " + tabuleiro.getTurnoJogador().getTipo() + " moveu a peça da casa " + posicaoInicial + " para " + posicaoFinal);
    }

    public void enviarPacoteAndarPeca(int posicaoInicial, int posicaoFinal) {
        andarPecar(posicaoInicial, posicaoFinal);
        comunicacao.enviarPacote("andarPeca:" + posicaoInicial + ":" + posicaoFinal);
        enviarPacotePassarTurno();
    }

    public void receberPacoteAndarPeca(int posicaoInicial, int posicaoFinal) {
        andarPecar(posicaoInicial, posicaoFinal);
    }

    public void verificaCaptura(int posicaoInicial, int posicaoFinal) {
        switch (posicaoFinal - posicaoInicial) {
            case 2:
                enviarPacoteCapturarPeca(posicaoInicial, posicaoFinal, posicaoInicial + 1);
                break;
            case -2:
                enviarPacoteCapturarPeca(posicaoInicial, posicaoFinal, posicaoInicial - 1);
                break;
            case 12:
                enviarPacoteCapturarPeca(posicaoInicial, posicaoFinal, posicaoInicial + 6);
                break;
            case -12:
                enviarPacoteCapturarPeca(posicaoInicial, posicaoFinal, posicaoInicial - 6);
                break;
            default:
                break;
        }
    }

    public void capturarPeca(int posicaoInicial, int posicaoFinal, int posicaoVerificar) {
        casasTabuleiro.get(posicaoInicial).removerPeca(tabuleiro.getTurnoJogador());
        casasTabuleiro.get(posicaoFinal).colocarPeca(tabuleiro.getTurnoJogador());
        adicionarMensagemChat("O jogador " + tabuleiro.getTurnoJogador().getTipo() + " capturou a peça da casa " + posicaoVerificar);
    }

    public void enviarPacoteCapturarPeca(int posicaoInicial, int posicaoFinal, int posicaoVerificar) {
        if (casasTabuleiro.get(posicaoVerificar).getCasa().getPeca().getTipo() == tabuleiro.getJogadorAdversario().getTipo()) {
            capturarPeca(posicaoInicial, posicaoFinal, posicaoVerificar);
            tabuleiro.getJogadorAdversario().removerPecasDentroTabuleiro();
            casasTabuleiro.get(posicaoVerificar).removerPeca(tabuleiro.getJogadorAdversario());
            tabuleiro.getTurnoJogador().setRemoverOutraPeca(true);
            selecionarPeca(posicaoFinal);
            setTextNumeroPecasAdversarias();
            desabilitarBotaoPegarPeca(true);
            desabilitarBotaoPassarTurno(false);
            comunicacao.enviarPacote("capturarPeca:" + posicaoInicial + ":" + posicaoFinal + ":" + posicaoVerificar);
            verificarVitoria();
            verificarEmpate();
        }
    }

    public void receberPacoteCapturarPeca(int posicaoInicial, int posicaoFinal, int posicaoVerificar) {
        capturarPeca(posicaoInicial, posicaoFinal, posicaoVerificar);
        tabuleiro.getJogador().removerPecasDentroTabuleiro();
        casasTabuleiro.get(posicaoVerificar).removerPeca(tabuleiro.getJogador());
        setTextNumeroPecas();
    }

    public void removerOutraPeca(int posicao) {
        adicionarMensagemChat("O jogador " + tabuleiro.getTurnoJogador().getTipo() + " removeu a peça da casa " + posicao);
    }

    public void enviarPacoteRemoverOutraPeca(int posicao) {
        if (casasTabuleiro.get(posicao).getCasa().getPeca().getTipo() == tabuleiro.getJogadorAdversario().getTipo()) {
            tabuleiro.getJogadorAdversario().removerPecasDentroTabuleiro();
            casasTabuleiro.get(posicao).removerPeca(tabuleiro.getJogadorAdversario());
            removerOutraPeca(posicao);
            tabuleiro.getTurnoJogador().setRemoverOutraPeca(false);
            setTextNumeroPecasAdversarias();
            comunicacao.enviarPacote("removerOutraPeca:" + posicao);
            verificarVitoria();
            verificarEmpate();

        }
    }

    public void receberPacoteRemoverOutraPeca(int posicao) {
        tabuleiro.getJogador().removerPecasDentroTabuleiro();
        casasTabuleiro.get(posicao).removerPeca(tabuleiro.getJogador());
        removerOutraPeca(posicao);
        setTextNumeroPecas();
    }

    public void vitoria() {
        adicionarMensagemChat("O jogador " + tabuleiro.getTurnoJogador().getTipo() + " ganhou a partida!");
    }

    public void verificarVitoria() {
        if (tabuleiro.getJogadorAdversario().totalPecas() == 0) {
            janelaAlerta("Resultado da partida", null, "Você ganhou a partida!");
            comunicacao.enviarPacote("vitoriaPartida");
            enviarPacoteReiniciarPartida();
        }
    }

    public void receberPacoteVitoria() {
        vitoria();
        janelaAlerta("Resultado da partida", null, "Você perdeu a partida!");
    }

    public void empate() {
        adicionarMensagemChat("A partida terminou em empate");
        janelaAlerta("Resultado da partida", null, "Deu empate!");
    }

    public void verificarEmpate() {
        if (tabuleiro.getTurnoJogador().totalPecas() <= 3 && tabuleiro.getJogadorAdversario().totalPecas() <= 3) {
            empate();
            comunicacao.enviarPacote("empatePartida");
            enviarPacoteReiniciarPartida();
        }
    }

    public void receberEmpatePartida() {
        empate();
    }

    public void reiniciarPartida() {
        adicionarMensagemChat("O jogo foi reiniciado!");
        if (tabuleiro.getJogador().getTipo() == 1) {
            iniciarNovoJogo(true);
        } else {
            iniciarNovoJogo(false);
        }
    }

    public void enviarPacoteReiniciarPartida() {
        reiniciarPartida();
        comunicacao.enviarPacote("reiniciarPartida");
    }

    public void receberPacoteReiniciarPartida() {
        reiniciarPartida();
    }

    public void enviarPacoteDesistirPartida() {
        adicionarMensagemChat("O jogador " + tabuleiro.getJogador().getTipo() + " desistiu da partida!");
        comunicacao.enviarPacote("desistirPartida:" + tabuleiro.getJogador().getTipo());
        enviarPacoteReiniciarPartida();
    }

    public void recebePacoteDesistirPartida(int jogador) {
        adicionarMensagemChat("O jogador " + jogador + " desistiu da partida!");
        janelaAlerta("Resultado partida", null, "O jogador " + jogador + " desistiu da partida!");
    }

    public void alerta(String titulo, String cabecalho, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }

    public void janelaAlerta(String titulo, String cabecalho, String conteudo) {
        Platform.runLater(() -> alerta(titulo, cabecalho, conteudo));
    }

    public void sairPartida() {
        comunicacao.fecharConexao();
    }

    public void iniciarThreadRecebePacotes() {

        new Thread(() -> {
            String mensagemRecebida;
            while (true) {
                mensagemRecebida = comunicacao.recebePacote();
                if (mensagemRecebida.matches("^pegarPeca$")) {
                    recebePacotePegarPeca();
                } else if (mensagemRecebida.matches("^passarTurno$")) {
                    receberPacotePassarTurno();
                } else if (mensagemRecebida.matches("^adicionarPeca:\\d+$")) {
                    receberPacoteAdicionarPeca(Integer.parseInt(mensagemRecebida.split(":")[1]));
                } else if (mensagemRecebida.matches("^andarPeca:\\d+:\\d+$")) {
                    String[] mensagem = mensagemRecebida.split(":");
                    receberPacoteAndarPeca(Integer.parseInt(mensagem[1]), Integer.parseInt(mensagem[2]));
                } else if (mensagemRecebida.matches("^capturarPeca:\\d+:\\d+:\\d+$")) {
                    String[] mensagem = mensagemRecebida.split(":");
                    receberPacoteCapturarPeca(Integer.parseInt(mensagem[1]), Integer.parseInt(mensagem[2]), Integer.parseInt(mensagem[3]));
                } else if (mensagemRecebida.matches("^removerOutraPeca:\\d+$")) {
                    receberPacoteRemoverOutraPeca(Integer.parseInt(mensagemRecebida.split(":")[1]));
                } else if (mensagemRecebida.matches("^vitoriaPartida$")) {
                    receberPacoteVitoria();
                } else if (mensagemRecebida.matches("^empatePartida$")) {
                    receberEmpatePartida();
                } else if (mensagemRecebida.matches("^reiniciarPartida$")) {
                    receberPacoteReiniciarPartida();
                } else if (mensagemRecebida.matches("^desistirPartida:\\d$")) {
                    recebePacoteDesistirPartida(Integer.parseInt(mensagemRecebida.split(":")[1]));
                } else {
                    adicionarMensagemChat(mensagemRecebida);
                }
            }
        }).start();
    }
}

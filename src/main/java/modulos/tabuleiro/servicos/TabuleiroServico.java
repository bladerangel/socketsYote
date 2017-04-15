package modulos.tabuleiro.servicos;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import modulos.casa.componentes.CasaBotao;

import modulos.jogador.modelos.Jogador;
import modulos.tabuleiro.modelos.Tabuleiro;
import modulos.chat.servicos.ChatServico;
import modulos.comunicacao.servicos.ComunicacaoServico;
import utils.JanelaAlerta;

import java.util.ArrayList;

public class TabuleiroServico {

    private Pane tabuleiroPane;
    private Text numeroPecas;
    private Text numeroPecasAdversarias;
    private Text tipoJogador;
    private Text turnoAtual;
    private Button pegarPeca;
    private Button passarTurno;

    private JanelaAlerta janelaAlerta;
    private Tabuleiro tabuleiro;
    private ArrayList<CasaBotao> casasTabuleiro;
    private ChatServico chatServico;
    private ComunicacaoServico comunicacaoServico;

    private Jogador jogadorPadrao;
    private Jogador jogadorAdversarioPadrao;

    public TabuleiroServico(Pane tabuleiroPane, Text numeroPecas, Text numeroPecasAdversarias, Text tipoJogador, Text turnoAtual, Button pegarPeca, Button passarTurno, JanelaAlerta janelaAlerta, ChatServico chatServico, ComunicacaoServico comunicacaoServico) {
        this.tabuleiroPane = tabuleiroPane;
        this.numeroPecas = numeroPecas;
        this.numeroPecasAdversarias = numeroPecasAdversarias;
        this.tipoJogador = tipoJogador;
        this.turnoAtual = turnoAtual;
        this.pegarPeca = pegarPeca;
        this.passarTurno = passarTurno;
        this.chatServico = chatServico;
        this.comunicacaoServico = comunicacaoServico;
        this.janelaAlerta = janelaAlerta;
        casasTabuleiro = new ArrayList<CasaBotao>();
    }

    public void iniciarJogadorers() {
        jogadorPadrao = new Jogador(1, 12, 0);
        jogadorAdversarioPadrao = new Jogador(2, 12, 0);
    }

    public void iniciarPartida(boolean servidor) {
        iniciarJogadorers();
        if (servidor) {
            criarTabuleiro(jogadorPadrao, jogadorAdversarioPadrao, jogadorPadrao);
        } else {
            criarTabuleiro(jogadorAdversarioPadrao, jogadorPadrao, jogadorPadrao);
        }
    }

    public void iniciarTabuleiro(Jogador jogador, Jogador jogadorAdversario, Jogador turnoJogador) {
        casasTabuleiro.forEach(CasaBotao::resetarCasa);
        tabuleiro = new Tabuleiro(jogador, jogadorAdversario, turnoJogador);
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

    public void criarTabuleiro(Jogador jogador, Jogador jogadorAdversario, Jogador turnoJogador) {
        VBox linhasTabuleiro = new VBox();
        tabuleiroPane.getChildren().add(linhasTabuleiro);
        for (int i = 0; i < Tabuleiro.QUANTIDADE_LINHAS; i++) {
            HBox colunasTabuleiro = new HBox();
            tabuleiroPane.getChildren().add(colunasTabuleiro);
            linhasTabuleiro.getChildren().add(colunasTabuleiro);
            for (int j = 0; j < Tabuleiro.QUANTIDADE_COLUNAS; j++) {
                CasaBotao casa = new CasaBotao();
                casasTabuleiro.add(casa);
                colunasTabuleiro.getChildren().add(casa);
                casa.getCasa().setPosicao(casasTabuleiro.size() - 1);
            }
        }
        iniciarTabuleiro(jogador, jogadorAdversario, turnoJogador);
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

    public void adicionarMensagemChat(int jogador, String mensagem) {
        chatServico.adicionarMensagemChat("O jogador " + jogador + " digitou: " + mensagem);
    }

    public void pegarPeca() {
        chatServico.adicionarMensagemChat("O jogador " + tabuleiro.getTurnoJogador().getTipo() + " tirou 1 peça");
    }

    public void passarTurno() {
        tabuleiro.mudarTurnoJogador();
        chatServico.adicionarMensagemChat("O turno é do jogador " + tabuleiro.getTurnoJogador().getTipo());
        setTextTurnoAtual();
    }

    public void adicionarPeca(int posicao) {
        tabuleiro.getTurnoJogador().removerPecasForaTabuleiro();
        casasTabuleiro.get(posicao).colocarPeca(tabuleiro.getTurnoJogador());
    }


    public void selecionarPeca(int posicaoFinal) {
        tabuleiro.setPosicaoInicial(posicaoFinal);
    }


    public void andarPecar(int posicaoInicial, int posicaoFinal) {
        casasTabuleiro.get(posicaoInicial).removerPeca(tabuleiro.getTurnoJogador());
        casasTabuleiro.get(posicaoFinal).colocarPeca(tabuleiro.getTurnoJogador());
        chatServico.adicionarMensagemChat("O jogador " + tabuleiro.getTurnoJogador().getTipo() + " moveu a peça da casa " + posicaoInicial + " para " + posicaoFinal);
    }

    public void capturarPeca(int posicaoInicial, int posicaoFinal, int posicaoVerificar) {
        casasTabuleiro.get(posicaoInicial).removerPeca(tabuleiro.getTurnoJogador());
        casasTabuleiro.get(posicaoFinal).colocarPeca(tabuleiro.getTurnoJogador());
        chatServico.adicionarMensagemChat("O jogador " + tabuleiro.getTurnoJogador().getTipo() + " capturou a peça da casa " + posicaoVerificar);
    }

    public void removerOutraPeca(int posicao) {
        chatServico.adicionarMensagemChat("O jogador " + tabuleiro.getTurnoJogador().getTipo() + " removeu a peça da casa " + posicao);
    }


    public void vitoria() {
        chatServico.adicionarMensagemChat("O jogador " + tabuleiro.getTurnoJogador().getTipo() + " ganhou a partida!");
    }


    public void empate() {
        chatServico.adicionarMensagemChat("A partida terminou em empate");
        janelaAlerta.janelaAlertaRunLater("Resultado da partida", null, "Deu empate!");
    }


    public void reiniciarPartida() {
        chatServico.adicionarMensagemChat("O jogo foi reiniciado!");
        iniciarJogadorers();
        if (tabuleiro.getJogador().getTipo() == 1) {
            iniciarTabuleiro(jogadorPadrao, jogadorAdversarioPadrao, jogadorPadrao);
        } else {
            iniciarTabuleiro(jogadorAdversarioPadrao, jogadorPadrao, jogadorPadrao);
        }
    }

    public void sairPartida() {
        comunicacaoServico.getComunicacao().fecharConexao();
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public Button getPegarPeca() {
        return pegarPeca;
    }

    public Button getPassarTurno() {
        return passarTurno;
    }

    public ArrayList<CasaBotao> getCasasTabuleiro() {
        return casasTabuleiro;
    }

    public ChatServico getChatServico() {
        return chatServico;
    }

    public ComunicacaoServico getComunicacaoServico() {
        return comunicacaoServico;
    }

    public JanelaAlerta getJanelaAlerta() {
        return janelaAlerta;
    }

    public Jogador getJogadorPadrao() {
        return jogadorPadrao;
    }

    public Jogador getJogadorAdversarioPadrao() {
        return jogadorAdversarioPadrao;
    }
}

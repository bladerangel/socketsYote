package modulos.tabuleiro.servicos;

import modulos.casa.componentes.CasaBotao;
import modulos.casa.modelos.Casa;
import modulos.chat.servicos.ChatServico;
import modulos.comunicacao.servicos.ComunicacaoServico;
import modulos.peca.modelos.Peca;
import modulos.tabuleiro.modelos.Tabuleiro;
import utils.JanelaAlerta;

public class TabuleiroEnviarPacoteServico {

    private JanelaAlerta janelaAlerta;
    private TabuleiroServico tabuleiroServico;
    private ComunicacaoServico comunicacaoServico;
    private ChatServico chatServico;


    public TabuleiroEnviarPacoteServico(JanelaAlerta janelaAlerta, TabuleiroServico tabuleiroServico, ComunicacaoServico comunicacaoServico, ChatServico chatServico) {
        this.janelaAlerta = janelaAlerta;
        this.tabuleiroServico = tabuleiroServico;
        this.comunicacaoServico = comunicacaoServico;
        this.chatServico = chatServico;
        tabuleiroServico.getCasasTabuleiro().forEach(casa -> casa.setOnMouseClicked(event -> movimentarPeca((CasaBotao) event.getSource())));
    }

    public void enviarPacoteMensagemChat() {
        if (!chatServico.getEscreverMensagem().getText().equals("")) {
            comunicacaoServico.getComunicacao().enviarPacote("jogadorDigitou:" + tabuleiroServico.getTabuleiro().getJogador().getTipo() + ":" + chatServico.getEscreverMensagem().getText());
            tabuleiroServico.adicionarMensagemChat(tabuleiroServico.getTabuleiro().getJogador().getTipo(), chatServico.getEscreverMensagem().getText());
            chatServico.limparMensagem();
        }
    }

    public void enviarPacotePegarPeca() {
        tabuleiroServico.pegarPeca();
        tabuleiroServico.desabilitarBotaoPegarPeca(true);
        comunicacaoServico.getComunicacao().enviarPacote("pegarPeca");
    }

    public void enviarPacotePassarTurno() {
        tabuleiroServico.passarTurno();
        comunicacaoServico.getComunicacao().enviarPacote("passarTurno");
        tabuleiroServico.desabilitarBotaoPegarPeca(true);
        tabuleiroServico.desabilitarBotaoPassarTurno(true);
    }

    public void enviarPacoteAdicionarPeca(int posicao) {
        tabuleiroServico.adicionarPeca(posicao);
        tabuleiroServico.setTextNumeroPecas();
        comunicacaoServico.getComunicacao().enviarPacote("adicionarPeca:" + posicao);
        enviarPacotePassarTurno();
    }


    public void enviarPacoteAndarPeca(int posicaoInicial, int posicaoFinal) {
        tabuleiroServico.andarPecar(posicaoInicial, posicaoFinal);
        comunicacaoServico.getComunicacao().enviarPacote("andarPeca:" + posicaoInicial + ":" + posicaoFinal);
        enviarPacotePassarTurno();
    }


    public void movimentarPeca(CasaBotao casa) {
        if (tabuleiroServico.getTabuleiro().getTurnoJogador() == tabuleiroServico.getTabuleiro().getJogador()) {
            verificarMovimento(tabuleiroServico.getTabuleiro().getPosicaoInicial(), casa.getCasa().getPosicao());
        }
    }

    public void verificarMovimento(int posicaoInicial, int posicaoFinal) {
        if (tabuleiroServico.getPegarPeca().isDisable() && tabuleiroServico.getPassarTurno().isDisable() && tabuleiroServico.getTabuleiro().getTurnoJogador().getQuantidadePecasForaTabuleiro() > 0 && tabuleiroServico.getCasasTabuleiro().get(posicaoFinal).getCasa().getPeca().getTipo() == Casa.CASA_VAZIA) { //adicionar Peça tabuleiro
            enviarPacoteAdicionarPeca(posicaoFinal);
        } else if (tabuleiroServico.getPassarTurno().isDisable() && tabuleiroServico.getCasasTabuleiro().get(posicaoFinal).getCasa().getPeca().getTipo() == tabuleiroServico.getTabuleiro().getTurnoJogador().getTipo()) { //escolher peça
            tabuleiroServico.selecionarPeca(posicaoFinal);
        } else if (tabuleiroServico.getPassarTurno().isDisable() && tabuleiroServico.getCasasTabuleiro().get(posicaoFinal).getCasa().getPeca().getTipo() == Casa.CASA_VAZIA && posicaoInicial != Tabuleiro.POSICAO_INICIAL_VAZIA && posicaoInicial != posicaoFinal) {//andar ou capturar uma peça
            verificarMovimentoAndar(posicaoInicial, posicaoFinal);
            verificaCaptura(posicaoInicial, posicaoFinal);
        } else if (!tabuleiroServico.getPassarTurno().isDisable() && !tabuleiroServico.getTabuleiro().getTurnoJogador().isRemoverOutraPeca() && tabuleiroServico.getCasasTabuleiro().get(posicaoFinal).getCasa().getPeca().getTipo() == Casa.CASA_VAZIA && posicaoInicial != Tabuleiro.POSICAO_INICIAL_VAZIA && posicaoInicial != posicaoFinal) {//capturar multipla peças
            verificaCaptura(posicaoInicial, posicaoFinal);
        } else if (!tabuleiroServico.getPassarTurno().isDisable() && tabuleiroServico.getTabuleiro().getTurnoJogador().isRemoverOutraPeca()) {//remover peça ao realizar a captura multipla
            enviarPacoteRemoverOutraPeca(posicaoFinal);
        }
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

    public void enviarPacoteCapturarPeca(int posicaoInicial, int posicaoFinal, int posicaoVerificar) {
        if (tabuleiroServico.getCasasTabuleiro().get(posicaoVerificar).getCasa().getPeca().getTipo() == tabuleiroServico.getTabuleiro().getJogadorAdversario().getTipo()) {
            tabuleiroServico.capturarPeca(posicaoInicial, posicaoFinal, posicaoVerificar);
            tabuleiroServico.getTabuleiro().getJogadorAdversario().removerPecasDentroTabuleiro();
            tabuleiroServico.getCasasTabuleiro().get(posicaoVerificar).removerPeca(tabuleiroServico.getTabuleiro().getJogadorAdversario());
            tabuleiroServico.getTabuleiro().getTurnoJogador().setRemoverOutraPeca(true);
            tabuleiroServico.selecionarPeca(posicaoFinal);
            tabuleiroServico.setTextNumeroPecasAdversarias();
            tabuleiroServico.desabilitarBotaoPegarPeca(true);
            tabuleiroServico.desabilitarBotaoPassarTurno(false);
            comunicacaoServico.getComunicacao().enviarPacote("capturarPeca:" + posicaoInicial + ":" + posicaoFinal + ":" + posicaoVerificar);
            verificarVitoria();
            verificarEmpate();
        }
    }

    public void enviarPacoteRemoverOutraPeca(int posicao) {
        if (tabuleiroServico.getCasasTabuleiro().get(posicao).getCasa().getPeca().getTipo() == tabuleiroServico.getTabuleiro().getJogadorAdversario().getTipo()) {
            tabuleiroServico.getTabuleiro().getJogadorAdversario().removerPecasDentroTabuleiro();
            tabuleiroServico.getCasasTabuleiro().get(posicao).removerPeca(tabuleiroServico.getTabuleiro().getJogadorAdversario());
            tabuleiroServico.removerOutraPeca(posicao);
            tabuleiroServico.getTabuleiro().getTurnoJogador().setRemoverOutraPeca(false);
            tabuleiroServico.setTextNumeroPecasAdversarias();
            comunicacaoServico.getComunicacao().enviarPacote("removerOutraPeca:" + posicao);
            verificarVitoria();
            verificarEmpate();

        }
    }

    public void verificarVitoria() {
        if (tabuleiroServico.getTabuleiro().getJogadorAdversario().totalPecas() == 0) {
            janelaAlerta.janelaAlertaRunLater("Resultado da partida", null, "Você ganhou a partida!");
            tabuleiroServico.vitoria();
            comunicacaoServico.getComunicacao().enviarPacote("vitoriaPartida");
            enviarPacoteReiniciarPartida();
        }
    }

    public void verificarEmpate() {
        if (tabuleiroServico.getTabuleiro().getTurnoJogador().totalPecas() <= 3 && tabuleiroServico.getTabuleiro().getJogadorAdversario().totalPecas() <= 3) {
            tabuleiroServico.empate();
            comunicacaoServico.getComunicacao().enviarPacote("empatePartida");
            enviarPacoteReiniciarPartida();
        }
    }

    public void enviarPacoteReiniciarPartida() {
        tabuleiroServico.reiniciarPartida();
        comunicacaoServico.getComunicacao().enviarPacote("reiniciarPartida");
    }

    public void enviarPacoteDesistirPartida() {
        chatServico.adicionarMensagemChat("O jogador " + tabuleiroServico.getTabuleiro().getJogador().getTipo() + " desistiu da partida!");
        comunicacaoServico.getComunicacao().enviarPacote("desistirPartida:" + tabuleiroServico.getTabuleiro().getJogador().getTipo());
        enviarPacoteReiniciarPartida();
    }

    public void enviarPacoteSairPartida() {
        comunicacaoServico.getComunicacao().enviarPacote("sairPartida:" + tabuleiroServico.getTabuleiro().getJogador().getTipo());
        tabuleiroServico.sairPartida();
    }


}

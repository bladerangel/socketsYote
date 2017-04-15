package services;

import models.CasaBotao;

public class TabuleiroEnviarPacoteService {

    private TabuleiroService tabuleiroService;
    private ComunicacaoService comunicacaoService;
    private ChatService chatService;

    public TabuleiroEnviarPacoteService(TabuleiroService tabuleiroService, ComunicacaoService comunicacaoService, ChatService chatService) {
        this.tabuleiroService = tabuleiroService;
        this.comunicacaoService = comunicacaoService;
        this.chatService = chatService;
    }

    public void iniciarAcaoCasas() {
        tabuleiroService.getCasasTabuleiro().forEach(casa -> casa.setOnMouseClicked(event -> movimentarPeca((CasaBotao) event.getSource())));
    }

    public void enviarPacoteIniciarPartida(boolean servidor) {
        tabuleiroService.iniciarJogadorers();
        if (servidor) {
            tabuleiroService.criarTabuleiro(tabuleiroService.getJogadorPadrao(), tabuleiroService.getJogadorAdversarioPadrao(), tabuleiroService.getJogadorPadrao());
            iniciarAcaoCasas();
        } else {
            tabuleiroService.getComunicacaoService().getComunicacao().enviarPacote("iniciarPartida");
            tabuleiroService.criarTabuleiro(tabuleiroService.getJogadorAdversarioPadrao(), tabuleiroService.getJogadorPadrao(), tabuleiroService.getJogadorPadrao());
            iniciarAcaoCasas();
        }
    }

    public void enviarPacoteMensagemChat() {
        if (!chatService.getEscreverMensagem().getText().equals("")) {
            comunicacaoService.getComunicacao().enviarPacote("jogadorDigitou:" + tabuleiroService.getTabuleiro().getJogador().getTipo() + ":" + chatService.getEscreverMensagem().getText());
            tabuleiroService.adicionarMensagemChat(tabuleiroService.getTabuleiro().getJogador().getTipo(), chatService.getEscreverMensagem().getText());
            chatService.limparMensagem();
        }
    }

    public void enviarPacotePegarPeca() {
        tabuleiroService.pegarPeca();
        tabuleiroService.desabilitarBotaoPegarPeca(true);
        tabuleiroService.getComunicacaoService().getComunicacao().enviarPacote("pegarPeca");
    }

    public void enviarPacotePassarTurno() {
        tabuleiroService.passarTurno();
        tabuleiroService.getComunicacaoService().getComunicacao().enviarPacote("passarTurno");
        tabuleiroService.desabilitarBotaoPegarPeca(true);
        tabuleiroService.desabilitarBotaoPassarTurno(true);
    }

    public void enviarPacoteAdicionarPeca(int posicao) {
        tabuleiroService.adicionarPeca(posicao);
        tabuleiroService.setTextNumeroPecas();
        tabuleiroService.getComunicacaoService().getComunicacao().enviarPacote("adicionarPeca:" + posicao);
        enviarPacotePassarTurno();
    }


    public void enviarPacoteAndarPeca(int posicaoInicial, int posicaoFinal) {
        tabuleiroService.andarPecar(posicaoInicial, posicaoFinal);
        tabuleiroService.getComunicacaoService().getComunicacao().enviarPacote("andarPeca:" + posicaoInicial + ":" + posicaoFinal);
        enviarPacotePassarTurno();
    }


    public void movimentarPeca(CasaBotao casa) {
        if (tabuleiroService.getTabuleiro().getTurnoJogador() == tabuleiroService.getTabuleiro().getJogador()) {
            verificarMovimento(tabuleiroService.getTabuleiro().getPosicaoInicial(), casa.getCasa().getPosicao());
        }
    }

    public void verificarMovimento(int posicaoInicial, int posicaoFinal) {
        if (tabuleiroService.getPegarPeca().isDisable() && tabuleiroService.getPassarTurno().isDisable() && tabuleiroService.getTabuleiro().getTurnoJogador().getQuantidadePecasForaTabuleiro() > 0 && tabuleiroService.getCasasTabuleiro().get(posicaoFinal).getCasa().getPeca().getTipo() == 0) { //adicionar Peça tabuleiro
            enviarPacoteAdicionarPeca(posicaoFinal);
        } else if (tabuleiroService.getPassarTurno().isDisable() && tabuleiroService.getCasasTabuleiro().get(posicaoFinal).getCasa().getPeca().getTipo() == tabuleiroService.getTabuleiro().getTurnoJogador().getTipo()) { //escolher peça
            tabuleiroService.selecionarPeca(posicaoFinal);
        } else if (tabuleiroService.getPassarTurno().isDisable() && tabuleiroService.getCasasTabuleiro().get(posicaoFinal).getCasa().getPeca().getTipo() == 0 && posicaoInicial != -1 && posicaoInicial != posicaoFinal) {//andar ou capturar uma peça
            verificarMovimentoAndar(posicaoInicial, posicaoFinal);
            verificaCaptura(posicaoInicial, posicaoFinal);
        } else if (!tabuleiroService.getPassarTurno().isDisable() && !tabuleiroService.getTabuleiro().getTurnoJogador().isRemoverOutraPeca() && tabuleiroService.getCasasTabuleiro().get(posicaoFinal).getCasa().getPeca().getTipo() == 0 && posicaoInicial != -1 && posicaoInicial != posicaoFinal) {//capturar multipla peças
            verificaCaptura(posicaoInicial, posicaoFinal);
        } else if (!tabuleiroService.getPassarTurno().isDisable() && tabuleiroService.getTabuleiro().getTurnoJogador().isRemoverOutraPeca()) {//remover peça ao realizar a captura multipla
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
        if (tabuleiroService.getCasasTabuleiro().get(posicaoVerificar).getCasa().getPeca().getTipo() == tabuleiroService.getTabuleiro().getJogadorAdversario().getTipo()) {
            tabuleiroService.capturarPeca(posicaoInicial, posicaoFinal, posicaoVerificar);
            tabuleiroService.getTabuleiro().getJogadorAdversario().removerPecasDentroTabuleiro();
            tabuleiroService.getCasasTabuleiro().get(posicaoVerificar).removerPeca(tabuleiroService.getTabuleiro().getJogadorAdversario());
            tabuleiroService.getTabuleiro().getTurnoJogador().setRemoverOutraPeca(true);
            tabuleiroService.selecionarPeca(posicaoFinal);
            tabuleiroService.setTextNumeroPecasAdversarias();
            tabuleiroService.desabilitarBotaoPegarPeca(true);
            tabuleiroService.desabilitarBotaoPassarTurno(false);
            tabuleiroService.getComunicacaoService().getComunicacao().enviarPacote("capturarPeca:" + posicaoInicial + ":" + posicaoFinal + ":" + posicaoVerificar);
            verificarVitoria();
            verificarEmpate();
        }
    }

    public void enviarPacoteRemoverOutraPeca(int posicao) {
        if (tabuleiroService.getCasasTabuleiro().get(posicao).getCasa().getPeca().getTipo() == tabuleiroService.getTabuleiro().getJogadorAdversario().getTipo()) {
            tabuleiroService.getTabuleiro().getJogadorAdversario().removerPecasDentroTabuleiro();
            tabuleiroService.getCasasTabuleiro().get(posicao).removerPeca(tabuleiroService.getTabuleiro().getJogadorAdversario());
            tabuleiroService.removerOutraPeca(posicao);
            tabuleiroService.getTabuleiro().getTurnoJogador().setRemoverOutraPeca(false);
            tabuleiroService.setTextNumeroPecasAdversarias();
            tabuleiroService.getComunicacaoService().getComunicacao().enviarPacote("removerOutraPeca:" + posicao);
            verificarVitoria();
            verificarEmpate();

        }
    }

    public void verificarVitoria() {
        if (tabuleiroService.getTabuleiro().getJogadorAdversario().totalPecas() == 0) {
            tabuleiroService.getJanelaAlerta().janelaAlertaRunLater("Resultado da partida", null, "Você ganhou a partida!");
            tabuleiroService.getComunicacaoService().getComunicacao().enviarPacote("vitoriaPartida");
            enviarPacoteReiniciarPartida();
        }
    }

    public void verificarEmpate() {
        if (tabuleiroService.getTabuleiro().getTurnoJogador().totalPecas() <= 3 && tabuleiroService.getTabuleiro().getJogadorAdversario().totalPecas() <= 3) {
            tabuleiroService.empate();
            tabuleiroService.getComunicacaoService().getComunicacao().enviarPacote("empatePartida");
            enviarPacoteReiniciarPartida();
        }
    }

    public void enviarPacoteReiniciarPartida() {
        tabuleiroService.reiniciarPartida();
        tabuleiroService.getComunicacaoService().getComunicacao().enviarPacote("reiniciarPartida");
    }

    public void enviarPacoteDesistirPartida() {
        tabuleiroService.getChatService().adicionarMensagemChat("O jogador " + tabuleiroService.getTabuleiro().getJogador().getTipo() + " desistiu da partida!");
        tabuleiroService.getComunicacaoService().getComunicacao().enviarPacote("desistirPartida:" + tabuleiroService.getTabuleiro().getJogador().getTipo());
        enviarPacoteReiniciarPartida();
    }

    public void enviarPacoteSairPartida() {
        tabuleiroService.getComunicacaoService().getComunicacao().enviarPacote("sairPartida:" + tabuleiroService.getTabuleiro().getJogador().getTipo());
        tabuleiroService.sairPartida();
    }


}

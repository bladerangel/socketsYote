package services;

import utils.JanelaAlerta;

public class TabuleiroReceberPacoteService {


    private TabuleiroService tabuleiroService;
    private JanelaAlerta janelaAlerta;

    public TabuleiroReceberPacoteService(JanelaAlerta janelaAlerta, TabuleiroService tabuleiroService) {
        this.janelaAlerta = janelaAlerta;
        this.tabuleiroService = tabuleiroService;

    }

    public void receberPacoteIniciarPartida() {
        janelaAlerta.janelaAlertaRunLater("Iniciar Partida", null, "O jogador 2 se conectou!");
    }

    public void recebePacotePegarPeca() {
        tabuleiroService.pegarPeca();
    }

    public void receberPacotePassarTurno() {
        tabuleiroService.passarTurno();
        if (tabuleiroService.getTabuleiro().getTurnoJogador().getQuantidadePecasForaTabuleiro() > 0)
            tabuleiroService.desabilitarBotaoPegarPeca(false);
    }


    public void receberPacoteAdicionarPeca(int posicao) {
        tabuleiroService.adicionarPeca(posicao);
        tabuleiroService.setTextNumeroPecasAdversarias();
    }


    public void receberPacoteAndarPeca(int posicaoInicial, int posicaoFinal) {
        tabuleiroService.andarPecar(posicaoInicial, posicaoFinal);
    }

    public void receberPacoteCapturarPeca(int posicaoInicial, int posicaoFinal, int posicaoVerificar) {
        tabuleiroService.capturarPeca(posicaoInicial, posicaoFinal, posicaoVerificar);
        tabuleiroService.getTabuleiro().getJogador().removerPecasDentroTabuleiro();
        tabuleiroService.getCasasTabuleiro().get(posicaoVerificar).removerPeca(tabuleiroService.getTabuleiro().getJogador());
        tabuleiroService.setTextNumeroPecas();
    }

    public void receberPacoteRemoverOutraPeca(int posicao) {
        tabuleiroService.getTabuleiro().getJogador().removerPecasDentroTabuleiro();
        tabuleiroService.getCasasTabuleiro().get(posicao).removerPeca(tabuleiroService.getTabuleiro().getJogador());
        tabuleiroService.removerOutraPeca(posicao);
        tabuleiroService.setTextNumeroPecas();
    }

    public void receberPacoteVitoria() {
        tabuleiroService.vitoria();
        tabuleiroService.getJanelaAlerta().janelaAlertaRunLater("Resultado da partida", null, "Você perdeu a partida!");
    }

    public void receberEmpatePartida() {
        tabuleiroService.empate();
    }

    public void receberPacoteReiniciarPartida() {
        tabuleiroService.reiniciarPartida();
    }


    public void recebePacoteDesistirPartida(int jogador) {
        tabuleiroService.getChatService().adicionarMensagemChat("O jogador " + jogador + " desistiu da partida!");
        tabuleiroService.getJanelaAlerta().janelaAlertaRunLater("Resultado partida", null, "O jogador " + jogador + " desistiu da partida!");
    }

    public void receberPacoteSairPartida(int jogador) {
        janelaAlerta.janelaAlertaRunLater("Sair partida", null, "O jogador " + jogador + " saiu da partida!");
        tabuleiroService.getChatService().adicionarMensagemChat("O jogador " + jogador + " saiu da partida!");
        tabuleiroService.sairPartida();
    }

    public void iniciarThreadRecebePacotes() {
        new Thread(() -> {
            String mensagemRecebida;
            while (tabuleiroService.getComunicacaoService().getComunicacao().isConectado()) {
                mensagemRecebida = tabuleiroService.getComunicacaoService().getComunicacao().receberPacote();
                if (mensagemRecebida.matches("^iniciarPartida$")) {
                    receberPacoteIniciarPartida();
                } else if (mensagemRecebida.matches("^pegarPeca$")) {
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
                } else if (mensagemRecebida.matches("^sairPartida:\\d$")) {
                    receberPacoteSairPartida(Integer.parseInt(mensagemRecebida.split(":")[1]));
                } else {
                    tabuleiroService.getChatService().adicionarMensagemChat(mensagemRecebida);
                }
            }
        }).start();
    }

}

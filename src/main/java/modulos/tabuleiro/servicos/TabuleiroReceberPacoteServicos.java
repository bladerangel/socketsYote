package modulos.tabuleiro.servicos;

import modulos.chat.servicos.ChatServico;
import modulos.comunicacao.servicos.ComunicacaoServico;
import utils.JanelaAlerta;

public class TabuleiroReceberPacoteServicos {


    private JanelaAlerta janelaAlerta;
    private TabuleiroServico tabuleiroServico;
    private ComunicacaoServico comunicacaoServico;
    private ChatServico chatServico;

    public TabuleiroReceberPacoteServicos(JanelaAlerta janelaAlerta, TabuleiroServico tabuleiroServico, ComunicacaoServico comunicacaoServico, ChatServico chatServico) {
        this.janelaAlerta = janelaAlerta;
        this.tabuleiroServico = tabuleiroServico;
        this.comunicacaoServico = comunicacaoServico;
        this.chatServico = chatServico;
    }

    public void receberPacoteMensagemChat(int jogador, String mensagem) {
        tabuleiroServico.adicionarMensagemChat(jogador, mensagem);
    }

    public void recebePacotePegarPeca() {
        tabuleiroServico.pegarPeca();
    }

    public void receberPacotePassarTurno() {
        tabuleiroServico.passarTurno();
        if (tabuleiroServico.getTabuleiro().getTurnoJogador().getQuantidadePecasForaTabuleiro() > 0)
            tabuleiroServico.desabilitarBotaoPegarPeca(false);
    }


    public void receberPacoteAdicionarPeca(int posicao) {
        tabuleiroServico.adicionarPeca(posicao);
        tabuleiroServico.setTextNumeroPecasAdversarias();
    }


    public void receberPacoteAndarPeca(int posicaoInicial, int posicaoFinal) {
        tabuleiroServico.andarPecar(posicaoInicial, posicaoFinal);
    }

    public void receberPacoteCapturarPeca(int posicaoInicial, int posicaoFinal, int posicaoVerificar) {
        tabuleiroServico.capturarPeca(posicaoInicial, posicaoFinal, posicaoVerificar);
        tabuleiroServico.getTabuleiro().getJogador().removerPecasDentroTabuleiro();
        tabuleiroServico.getCasasTabuleiro().get(posicaoVerificar).removerPeca(tabuleiroServico.getTabuleiro().getJogador());
        tabuleiroServico.setTextNumeroPecas();
    }

    public void receberPacoteRemoverOutraPeca(int posicao) {
        tabuleiroServico.getTabuleiro().getJogador().removerPecasDentroTabuleiro();
        tabuleiroServico.getCasasTabuleiro().get(posicao).removerPeca(tabuleiroServico.getTabuleiro().getJogador());
        tabuleiroServico.removerOutraPeca(posicao);
        tabuleiroServico.setTextNumeroPecas();
    }

    public void receberPacoteVitoria() {
        tabuleiroServico.vitoria();
        janelaAlerta.janelaAlertaRunLater("Resultado da partida", null, "Você perdeu a partida!");
    }

    public void receberEmpatePartida() {
        tabuleiroServico.empate();
    }

    public void receberPacoteReiniciarPartida() {
        tabuleiroServico.reiniciarPartida();
    }


    public void recebePacoteDesistirPartida(int jogador) {
        chatServico.adicionarMensagemChat("O jogador " + jogador + " desistiu da partida!");
        janelaAlerta.janelaAlertaRunLater("Resultado partida", null, "O jogador " + jogador + " desistiu da partida!");
    }

    public void receberPacoteSairPartida(int jogador) {
        janelaAlerta.janelaAlertaRunLater("Sair partida", null, "O jogador " + jogador + " saiu da partida!");
        chatServico.adicionarMensagemChat("O jogador " + jogador + " saiu da partida!");
        tabuleiroServico.sairPartida();
    }

    public void iniciarThreadRecebePacotes() {
        new Thread(() -> {
            String mensagemRecebida;
            while (comunicacaoServico.getComunicacao().isConectado()) {
                mensagemRecebida = comunicacaoServico.getComunicacao().receberPacote();
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
                } else if (mensagemRecebida.matches("^sairPartida:\\d$")) {
                    receberPacoteSairPartida(Integer.parseInt(mensagemRecebida.split(":")[1]));
                } else if (mensagemRecebida.matches("^jogadorDigitou:\\d:\\w+$")) {
                    String[] mensagem = mensagemRecebida.split(":");
                    receberPacoteMensagemChat(Integer.parseInt(mensagem[1]), mensagem[2]);
                }
            }
        }).start();
    }

}

package modulos.tabuleiro.modelos;

import modulos.jogador.modelos.Jogador;

public class Tabuleiro {

    private Jogador jogador;
    private Jogador jogadorAdversario;
    private Jogador turnoJogador;
    public static final int QUANTIDADE_LINHAS = 5;
    public static final int QUANTIDADE_COLUNAS = 6;
    private int posicaoInicial;

    public Tabuleiro(boolean servidor) {
        if (servidor) {
            jogador = new Jogador(Jogador.TIPO_JOGADOR_SERVIDOR, Jogador.QUANTIDADE_PECAS, 0);
            jogadorAdversario = new Jogador(Jogador.TIPO_JOGADOR_CLIENTE, Jogador.QUANTIDADE_PECAS, 0);
            turnoJogador = jogador;
        } else {
            jogador = new Jogador(Jogador.TIPO_JOGADOR_CLIENTE, Jogador.QUANTIDADE_PECAS, 0);
            jogadorAdversario = new Jogador(Jogador.TIPO_JOGADOR_SERVIDOR, Jogador.QUANTIDADE_PECAS, 0);
            turnoJogador = jogadorAdversario;
        }
        posicaoInicial = -1;
    }

    public void mudarTurnoJogador() {
        if (turnoJogador == jogador)
            turnoJogador = jogadorAdversario;
        else
            turnoJogador = jogador;
        turnoJogador.setRemoverOutraPeca(false);
        posicaoInicial = -1;
    }

    public Jogador getTurnoJogador() {
        return turnoJogador;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public Jogador getJogadorAdversario() {
        return jogadorAdversario;
    }

    public void setPosicaoInicial(int posicaoInicial) {
        this.posicaoInicial = posicaoInicial;
    }

    public int getPosicaoInicial() {
        return posicaoInicial;
    }

}

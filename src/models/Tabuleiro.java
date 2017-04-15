package models;

public class Tabuleiro {

    private Jogador jogador;
    private Jogador jogadorAdversario;
    private Jogador turnoJogador;
    public static int QUANTIDADE_LINHAS = 5;
    public static int QUANTIDADE_COLUNAS = 6;
    private int posicaoInicial;

    public Tabuleiro(Jogador jogador, Jogador jogadorAdversario, Jogador turnoJogador) {
        this.jogador = jogador;
        this.jogadorAdversario = jogadorAdversario;
        this.turnoJogador = turnoJogador;
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

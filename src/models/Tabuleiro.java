package models;

public class Tabuleiro {

    private Jogador jogador;
    private Jogador jogadorAdversario;
    private Jogador turnoJogador;

    public Tabuleiro(Jogador jogador, Jogador jogadorAdversario, Jogador turnoJogador) {
        this.jogador = jogador;
        this.jogadorAdversario = jogadorAdversario;
        this.turnoJogador = turnoJogador;
    }

    public void mudarTurnoJogador() {
        if (turnoJogador == jogador)
            turnoJogador = jogadorAdversario;
        else
            turnoJogador = jogador;
        turnoJogador.setRemoverOutraPeca(false);
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
}

package models;

public class Tabuleiro {

    private int pecas;
    private int pecasAdversarias;
    private int turnoJogador;
    private boolean removerOutraPeca;

    public Tabuleiro() {
        removerOutraPeca = false;
        turnoJogador = 1;
        pecas = 12;
        pecasAdversarias = 12;
    }

    public void setTurnoJogador() {
        if (turnoJogador == 1)
            turnoJogador = 2;
        else
            turnoJogador = 1;
        removerOutraPeca = false;
    }

    public int getTurnoJogador() {
        return turnoJogador;
    }

    public void setPecas() {
        if (turnoJogador == 1)
            pecas--;
        else
            pecasAdversarias--;
    }

    public int getPecas() {
        if (turnoJogador == 1)
            return pecas;
        else
            return pecasAdversarias;
    }

    public void setRemoverOutraPeca(boolean removerOutraPeca) {
        this.removerOutraPeca = removerOutraPeca;
    }

    public boolean isRemoverOutraPeca() {
        return removerOutraPeca;
    }

}

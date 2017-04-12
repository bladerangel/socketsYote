package models;

public class Tabuleiro {

    private int pecas;
    private int pecasAdversarias;
    private boolean escolherCasa;
    private int turnoJogador;
    private boolean tirouPeca;

    public Tabuleiro() {
        escolherCasa = false;
        tirouPeca = false;
        turnoJogador = 1;
        pecas = 12;
        pecasAdversarias = 12;
    }

    public void setTurnoJogador() {
        if (turnoJogador == 1)
            turnoJogador = 2;
        else
            turnoJogador = 1;
        tirouPeca = false;
        escolherCasa = false;
    }

    public int getTurnoJogador() {
        return turnoJogador;
    }

    public void setEscolherCasa() {
        escolherCasa = !escolherCasa;
    }

    public boolean isEscolherCasa() {
        return escolherCasa;
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

    public void setTirouPeca() {
        tirouPeca = !tirouPeca;
    }

    public boolean isTirouPeca() {
        return tirouPeca;
    }
}

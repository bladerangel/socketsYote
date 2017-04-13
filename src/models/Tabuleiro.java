package models;

public class Tabuleiro {

    private int pecas;
    private int pecasAdversarias;
    private boolean escolherCasa;
    private int turnoJogador;
    private boolean jaJogou;
    private boolean removerOutraPeca;
    private boolean jaAndou;

    public Tabuleiro() {
        escolherCasa = false;
        jaJogou = false;
        removerOutraPeca = false;
        jaAndou = false;
        turnoJogador = 1;
        pecas = 12;
        pecasAdversarias = 12;
    }

    public void setTurnoJogador() {
        if (turnoJogador == 1)
            turnoJogador = 2;
        else
            turnoJogador = 1;
        jaAndou = false;
        jaJogou = false;
        escolherCasa = false;
        removerOutraPeca = false;
    }

    public int getTurnoJogador() {
        return turnoJogador;
    }

    public void setEscolherCasa(boolean escolherCasa) {
        this.escolherCasa = escolherCasa;
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

    public void setJaJogou(boolean jaJogou) {
        this.jaJogou = jaJogou;
    }

    public boolean isJaJogou() {
        return jaJogou;
    }

    public void setRemoverOutraPeca(boolean removerOutraPeca) {
        this.removerOutraPeca = removerOutraPeca;
    }

    public boolean isRemoverOutraPeca() {
        return removerOutraPeca;
    }

    public void setJaAndou(boolean jaAndou) {
        this.jaAndou = jaAndou;
    }

    public boolean isJaAndou() {
        return jaAndou;
    }
}

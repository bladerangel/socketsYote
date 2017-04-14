package models;


public class Jogador {

    private int tipo;
    private int quantidadePecas;

    public Jogador(int tipo, int quantidadePecas) {
        this.tipo = tipo;
        this.quantidadePecas = quantidadePecas;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setQuantidadePecas(int quantidadePecas) {
        this.quantidadePecas = quantidadePecas;
    }

    public int getQuantidadePecas() {
        return quantidadePecas;
    }
}

package models;

public class Casa {

    private Peca peca;
    private int posicao;

    public Casa() {
        peca = new Peca();
    }

    public Peca getPeca() {
        return peca;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public int getPosicao() {
        return posicao;
    }
}

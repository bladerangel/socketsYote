package modulos.jogador.modelos;

public class Jogador {

    private int tipo;
    private int quantidadePecasForaTabuleiro;
    private int quantidadePecasDentroTabuleiro;
    private boolean removerOutraPeca;
    public static final int TIPO_JOGADOR_SERVIDOR = 1;
    public static final int TIPO_JOGADOR_CLIENTE = 2;
    public static final int QUANTIDADE_PECAS = 12;

    public Jogador(int tipo, int quantidadePecasForaTabuleiro, int quantidadePecasDentroTabuleiro) {
        this.tipo = tipo;
        this.quantidadePecasForaTabuleiro = quantidadePecasForaTabuleiro;
        this.quantidadePecasDentroTabuleiro = quantidadePecasDentroTabuleiro;
        removerOutraPeca = false;
    }

    public int getTipo() {
        return tipo;
    }

    public void removerPecasForaTabuleiro() {
        if (quantidadePecasForaTabuleiro > 0) {
            quantidadePecasForaTabuleiro--;
            quantidadePecasDentroTabuleiro++;
        }
    }

    public void removerPecasDentroTabuleiro() {
        quantidadePecasDentroTabuleiro--;
    }

    public int totalPecas() {
        return quantidadePecasForaTabuleiro + quantidadePecasDentroTabuleiro;
    }

    public int getQuantidadePecasForaTabuleiro() {
        return quantidadePecasForaTabuleiro;
    }

    public void setRemoverOutraPeca(boolean removerOutraPeca) {
        this.removerOutraPeca = removerOutraPeca;
    }

    public boolean isRemoverOutraPeca() {
        return removerOutraPeca;
    }

}

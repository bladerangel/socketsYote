package models;


import javafx.scene.control.Button;

public class CasaLayout extends Button {

    private Casa casa;

    public CasaLayout() {
        casa = new Casa();
        getStyleClass().add("imagemCasa");
    }

    public void colocarPeca(int jogador) {
        casa.getPeca().setJogador(jogador);
        if (casa.getPeca().getJogador() == 1)
            getStyleClass().add("imagemPeca");
        else
            getStyleClass().add("imagemPecaAdversaria");
    }

    public void removerPeca(int jogador) {
        casa.getPeca().setJogador(jogador);
        if (casa.getPeca().getJogador() == 1)
            getStyleClass().remove("imagemPeca");
        else
            getStyleClass().remove("imagemPecaAdversaria");
    }

    public boolean verificarMovimento(int posicaoInicial, int posicaoFinal) {
       // System.out.println("posicaoI:" + posicaoInicial);
        //System.out.println("posicaoF:" + posicaoFinal);
        if ((posicaoFinal - posicaoInicial) == 1) { //esquerda
            return true;
        } else if (posicaoInicial == -1) {
            return true;
        }

        return false;
    }

    public Casa getCasa() {
        return casa;
    }
}

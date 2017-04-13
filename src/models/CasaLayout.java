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

    public void removerPeca() {
        casa.getPeca().setJogador(0);
        getStyleClass().remove("imagemPeca");
    }

    public Casa getCasa() {
        return casa;
    }
}

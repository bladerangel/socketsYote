package models;


import javafx.scene.control.Button;

public class CasaLayout extends Button {

    private Casa casa;

    public CasaLayout() {
        casa = new Casa();
        getStyleClass().add("imagemCasa");
    }

    public void colocarPeca(int jogador) {
        casa.getPeca().setTipo(jogador);
        if (jogador == 1)
            getStyleClass().add("imagemPeca");
        else
            getStyleClass().add("imagemPecaAdversaria");
    }

    public void removerPeca(int jogador) {
        casa.getPeca().setTipo(0);
        if (jogador == 1)
            getStyleClass().remove("imagemPeca");
        else
            getStyleClass().remove("imagemPecaAdversaria");
    }

    public Casa getCasa() {
        return casa;
    }
}

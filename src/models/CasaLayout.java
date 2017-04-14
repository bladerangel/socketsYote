package models;


import javafx.scene.control.Button;

public class CasaLayout extends Button {

    private Casa casa;

    public CasaLayout() {
        casa = new Casa();
        getStyleClass().add("imagemCasa");
    }

    public void colocarPeca(Jogador jogador) {
        casa.getPeca().setTipo(jogador.getTipo());
        if (jogador.getTipo() == 1)
            getStyleClass().add("imagemPeca");
        else
            getStyleClass().add("imagemPecaAdversaria");
    }

    public void removerPeca(Jogador jogador) {
        casa.getPeca().setTipo(0);
        if (jogador.getTipo() == 1)
            getStyleClass().remove("imagemPeca");
        else
            getStyleClass().remove("imagemPecaAdversaria");
    }

    public Casa getCasa() {
        return casa;
    }
}

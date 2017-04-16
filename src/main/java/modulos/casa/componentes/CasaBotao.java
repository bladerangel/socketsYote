package modulos.casa.componentes;

import javafx.scene.control.Button;
import modulos.jogador.modelos.Jogador;
import modulos.casa.modelos.Casa;

public class CasaBotao extends Button {

    private Casa casa;

    public CasaBotao() {
        casa = new Casa();
        getStyleClass().add("imagemCasa");
    }

    public void colocarPeca(Jogador jogador) {
        casa.getPeca().setTipo(jogador.getTipo());
        if (jogador.getTipo() == Jogador.TIPO_JOGADOR_SERVIDOR)
            getStyleClass().add("imagemPeca");
        else
            getStyleClass().add("imagemPecaAdversaria");
    }

    public void removerPeca(Jogador jogador) {
        casa.getPeca().setTipo(Casa.CASA_VAZIA);
        if (jogador.getTipo() == Jogador.TIPO_JOGADOR_SERVIDOR)
            getStyleClass().remove("imagemPeca");
        else
            getStyleClass().remove("imagemPecaAdversaria");
    }

    public void resetarCasa() {
        casa.getPeca().setTipo(Casa.CASA_VAZIA);
        getStyleClass().remove("imagemPeca");
        getStyleClass().remove("imagemPecaAdversaria");
    }

    public Casa getCasa() {
        return casa;
    }
}

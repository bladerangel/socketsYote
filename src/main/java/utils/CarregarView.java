package utils;

import modulos.main.controladores.MainControlador;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CarregarView {

    private FXMLLoader fxmlLoader;
    private Stage estagio;
    private Scene cena;

    public CarregarView(String view) throws IOException {
        fxmlLoader = new FXMLLoader(getClass().getResource("/" + view + ".fxml"));
    }

    public void setEstagio(Stage estagio) {
        this.estagio = estagio;
    }

    public void setCena() {
        try {
            cena = new Scene(this.fxmlLoader.load());
            this.estagio.setScene(cena);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sairPartida() {
        MainControlador mainControlador = (MainControlador) fxmlLoader.getController();
        estagio.setOnCloseRequest(event -> mainControlador.sairPartida());
    }

    public void show() {
        this.estagio.show();
    }
}

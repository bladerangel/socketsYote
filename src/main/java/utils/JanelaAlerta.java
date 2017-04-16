package utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class JanelaAlerta {

    private Alert alert;

    public void janelaAlerta(String titulo, String cabecalho, String conteudo) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }

    public void janelaAlertaRunLater(String titulo, String cabecalho, String conteudo) {
        Platform.runLater(() -> janelaAlerta(titulo, cabecalho, conteudo));
    }
}

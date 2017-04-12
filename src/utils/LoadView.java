package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadView {

    private FXMLLoader fxmlLoader;
    private Stage stage;
    private Scene scene;

    public LoadView(String view) throws IOException {
        fxmlLoader = new FXMLLoader(getClass().getResource("/views/" + view + ".fxml"));
    }

    public void setState(Stage stage) {
        this.stage = stage;
    }

    public void setScene() throws IOException {
        scene = new Scene(this.fxmlLoader.load());
        this.stage.setScene(scene);
    }

    public void show() {
        this.stage.show();
    }
}

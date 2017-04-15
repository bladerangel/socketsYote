package modulos.main.init;

import javafx.application.Application;
import javafx.stage.Stage;
import utils.CarregarView;

public class Main extends Application {

    private CarregarView carregarView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        carregarView = new CarregarView("MainVisao");
        carregarView.setEstagio(primaryStage);
        carregarView.setCena();
        carregarView.show();
        carregarView.sairPartida();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

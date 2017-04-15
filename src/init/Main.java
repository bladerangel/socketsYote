package init;

import javafx.application.Application;
import javafx.stage.Stage;
import utils.CarregarView;

public class Main extends Application {

    private CarregarView carregarView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        carregarView = new CarregarView("index");
        carregarView.setEstagio(primaryStage);
        carregarView.setCena();
        carregarView.fecharConexao();
        carregarView.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

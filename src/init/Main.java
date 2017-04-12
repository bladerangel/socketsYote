package init;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.LoadView;

public class Main extends Application {

    private LoadView loadView;

    @Override
    public void start(Stage primaryStage) throws Exception{
        loadView = new LoadView("index");
        loadView.setState(primaryStage);
        loadView.setScene();
        loadView.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

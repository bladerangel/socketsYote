package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import services.IndexService;

import java.net.URL;
import java.util.ResourceBundle;


public class IndexController implements Initializable {

    @FXML
    Pane tabuleiro;

    @FXML
    Text numeroPecas;

    @FXML
    Text numeroPecasAdversarias;

    @FXML
    TextField escrever;

    @FXML
    TextArea chat;

    private IndexService indexService;

    public IndexController() {
        indexService = new IndexService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        indexService.criarTabuleiro(tabuleiro);

    }

    @FXML
    public void removerPeca() {
        if (indexService.getTabuleiroJogo().getTurnoJogador() == 1 && !indexService.getTabuleiroJogo().isEscolherCasa() && !indexService.getTabuleiroJogo().isTirouPeca()) {
            indexService.removerPeca(numeroPecas);
        }
    }

    @FXML
    public void removerPecaAdversario() {
        if (indexService.getTabuleiroJogo().getTurnoJogador() == 2 && !indexService.getTabuleiroJogo().isEscolherCasa() && !indexService.getTabuleiroJogo().isTirouPeca()) {
            indexService.removerPeca(numeroPecasAdversarias);
        }
    }

    @FXML
    public void passarTurno() {
        indexService.getTabuleiroJogo().setTurnoJogador();
    }

    @FXML
    public void limpar() {
        escrever.clear();
    }

    @FXML
    public void enviar() {
        chat.appendText(escrever.getText() + "\n");
        escrever.clear();
    }
}

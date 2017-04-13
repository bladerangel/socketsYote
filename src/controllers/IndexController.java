package controllers;

import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Comunicacao;
import models.ComunicacaoTCP;
import services.IndexService;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;


public class IndexController implements Initializable {

    @FXML
    BorderPane janela;

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

    @FXML
    Text numeroJogador;

    @FXML
    Text turno;

    @FXML
    Button removerPeca;

    @FXML
    Button passarTurno;

    private IndexService indexService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        indexService = new IndexService(tabuleiro, numeroJogador, chat, escrever, turno, numeroPecasAdversarias, removerPeca,numeroPecas, passarTurno);
        indexService.iniciarComunicacao();
        indexService.criarTabuleiro();
        indexService.iniciarThreadRecebePacotes();

    }

    @FXML
    public void removerPeca() {
        indexService.removerPeca();
    }

    @FXML
    public void passarTurno() {
        indexService.passarTurno(true);
    }

    @FXML
    public void limpar() {
        escrever.clear();
    }

    @FXML
    public void enviar() throws IOException {
        indexService.enviarMensagemChat();
    }
}

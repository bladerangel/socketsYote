package controllers;

import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.Comunicacao;
import models.ComunicacaoTCP;
import services.IndexService;

import java.io.IOException;
import java.net.SocketException;
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

    @FXML
    Text numeroJogador;

    @FXML
    Text turno;

    private IndexService indexService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        indexService = new IndexService(tabuleiro, numeroJogador, chat, escrever, turno, numeroPecasAdversarias);
        indexService.iniciarComunicacao();
        indexService.criarTabuleiro();
        indexService.iniciarThreadRecebePacotes();
    }

    @FXML
    public void removerPeca() {
        if (indexService.getTabuleiroJogo().getTurnoJogador() == indexService.getJogador() && !indexService.getTabuleiroJogo().isEscolherCasa() && !indexService.getTabuleiroJogo().isTirouPeca()) {
            indexService.removerPeca(numeroPecas);
        }
    }

    @FXML
    public void passarTurno() {
       indexService.passarTurno();
    }

    @FXML
    public void limpar() {
        escrever.clear();
    }

    @FXML
    public void enviar() throws IOException {
        indexService.chat();
    }
}

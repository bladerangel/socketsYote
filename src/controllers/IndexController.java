package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import services.IndexService;

public class IndexController implements Initializable {

    @FXML
    private Pane tabuleiroPane;

    @FXML
    private Text numeroPecas;

    @FXML
    private Text numeroPecasAdversarias;

    @FXML
    private Text tipoJogador;

    @FXML
    private Text turnoAtual;

    @FXML
    private TextField escreverMensagem;

    @FXML
    private TextArea chat;

    @FXML
    private Button pegarPeca;

    @FXML
    private Button passarTurno;

    private IndexService indexService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        indexService = new IndexService(tabuleiroPane, numeroPecas, numeroPecasAdversarias, tipoJogador, turnoAtual, escreverMensagem, chat, pegarPeca, passarTurno);
        indexService.iniciarComunicacao();
        indexService.iniciarThreadRecebePacotes();
    }

    @FXML
    public void pegarPeca() {
        indexService.enviarPacotePegarPeca();
    }

    @FXML
    public void passarTurno() {
        indexService.enviarPacotePassarTurno();
    }

    @FXML
    private void desistirPartida() {
        indexService.enviarPacoteDesistirPartida();
    }

    @FXML
    public void limparMensagem() {
        indexService.limparMensagem();
    }

    @FXML
    public void enviarMensagem() {
        indexService.enviarPacoteMensagemChat();
    }

    public void fecharConexao(){
        indexService.sairPartida();
    }
}

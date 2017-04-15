package modulos.main.controladores;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import modulos.main.servicos.MainServico;

public class MainControlador implements Initializable {

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

    private MainServico mainServico;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainServico = new MainServico(tabuleiroPane, numeroPecas, numeroPecasAdversarias, tipoJogador, turnoAtual, escreverMensagem, chat, pegarPeca, passarTurno);
    }

    @FXML
    public void pegarPeca() {
        mainServico.getTabuleiroEnviarPacoteServico().enviarPacotePegarPeca();
    }

    @FXML
    public void passarTurno() {
        mainServico.getTabuleiroEnviarPacoteServico().enviarPacotePassarTurno();
    }

    @FXML
    private void desistirPartida() {
        mainServico.getTabuleiroEnviarPacoteServico().enviarPacoteDesistirPartida();
    }

    @FXML
    public void limparMensagem() {
        mainServico.getChatServico().limparMensagem();
    }

    @FXML
    public void enviarMensagem() {
        mainServico.getTabuleiroEnviarPacoteServico().enviarPacoteMensagemChat();
    }

    public void sairPartida() {
        mainServico.sairPartida();
    }
}

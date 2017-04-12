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

    ComunicacaoTCP comunicacao;

    String mensagemRecebida;

    private int jogador;

    private IndexService indexService;


    public IndexController() {
        indexService = new IndexService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            comunicacao = new ComunicacaoTCP();
            comunicacao.iniciarServidor(9999);
            comunicacao.esperandoConexao();
            jogador = 1;
        } catch (IOException e) {
            try {
                comunicacao.iniciarCliente(9999);
                jogador = 2;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        indexService.criarTabuleiro(tabuleiro);
        numeroJogador.setText("Você: Jogador " + jogador);

        new Thread(() -> {
            try {
                while (true) {
                    mensagemRecebida = comunicacao.recebePacote();
                    System.out.println("mensagem= " + mensagemRecebida);
                    chat.appendText(mensagemRecebida + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


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
    public void enviar() throws IOException {
        comunicacao.enviarPacote(escrever.getText());
        chat.appendText(escrever.getText() + "\n");
        escrever.clear();
    }
}

package services;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatService {

    private TextField escreverMensagem;
    private TextArea chat;

    private ComunicacaoService comunicacaoService;

    public ChatService(TextField escreverMensagem, TextArea chat, ComunicacaoService comunicacaoService) {
        this.escreverMensagem = escreverMensagem;
        this.chat = chat;
        this.comunicacaoService = comunicacaoService;
    }

    public void adicionarMensagemChat(String mensagem) {
        chat.appendText(mensagem + "\n");
    }

    public void limparMensagem() {
        escreverMensagem.clear();
    }

    public void enviarPacoteMensagemChat() {
        comunicacaoService.getComunicacao().enviarPacote(escreverMensagem.getText());
        adicionarMensagemChat(escreverMensagem.getText());
        limparMensagem();
    }
}

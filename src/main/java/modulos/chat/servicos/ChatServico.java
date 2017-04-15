package modulos.chat.servicos;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import modulos.comunicacao.servicos.ComunicacaoServico;

public class ChatServico {

    private TextField escreverMensagem;
    private TextArea chat;

    private ComunicacaoServico comunicacaoServico;

    public ChatServico(TextField escreverMensagem, TextArea chat, ComunicacaoServico comunicacaoServico) {
        this.escreverMensagem = escreverMensagem;
        this.chat = chat;
        this.comunicacaoServico = comunicacaoServico;
    }

    public void adicionarMensagemChat(String mensagem) {
        chat.appendText(mensagem + "\n");
    }

    public void limparMensagem() {
        escreverMensagem.clear();
    }

    public TextField getEscreverMensagem() {
        return escreverMensagem;
    }
}

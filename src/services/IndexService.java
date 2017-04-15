package services;

import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import utils.JanelaAlerta;

public class IndexService {

    private ComunicacaoService comunicacaoService;
    private ChatService chatService;
    private TabuleiroService tabuleiroService;
    private TabuleiroEnviarPacoteService tabuleiroEnviarPacoteService;
    private TabuleiroReceberPacoteService tabuleiroReceberPacoteService;
    private JanelaAlerta janelaAlerta;

    public IndexService(Pane tabuleiroPane, Text numeroPecas, Text numeroPecasAdversarias, Text tipoJogador, Text turnoAtual, TextField escreverMensagem, TextArea chat, Button pegarPeca, Button passarTurno) {

        janelaAlerta = new JanelaAlerta();
        comunicacaoService = new ComunicacaoService(janelaAlerta);
        comunicacaoService.iniciarComunicacao();
        chatService = new ChatService(escreverMensagem, chat, comunicacaoService);
        tabuleiroService = new TabuleiroService(tabuleiroPane, numeroPecas, numeroPecasAdversarias, tipoJogador, turnoAtual, pegarPeca, passarTurno, janelaAlerta, chatService, comunicacaoService);
        tabuleiroEnviarPacoteService = new TabuleiroEnviarPacoteService(tabuleiroService);
        tabuleiroEnviarPacoteService.enviarPacoteIniciarPartida(comunicacaoService.isServidor());
        tabuleiroReceberPacoteService = new TabuleiroReceberPacoteService(janelaAlerta, tabuleiroService);
        tabuleiroReceberPacoteService.iniciarThreadRecebePacotes();
    }

    public void sairPartida() {
        tabuleiroEnviarPacoteService.enviarPacoteSairPartida();
    }

    public ChatService getChatService() {
        return chatService;
    }

    public TabuleiroEnviarPacoteService getTabuleiroEnviarPacoteService() {
        return tabuleiroEnviarPacoteService;
    }
}

package services;

import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import models.Tabuleiro;
import utils.JanelaAlerta;

public class IndexService {

    private Pane tabuleiroPane;
    private Text numeroPecas;
    private Text numeroPecasAdversarias;
    private Text tipoJogador;
    private Text turnoAtual;
    private TextField escreverMensagem;
    private TextArea chat;
    private Button pegarPeca;
    private Button passarTurno;
    private Tabuleiro tabuleiro;

    private ComunicacaoService comunicacaoService;
    private ChatService chatService;
    private TabuleiroService tabuleiroService;
    private TabuleiroEnviarPacoteService tabuleiroEnviarPacoteService;
    private TabuleiroReceberPacoteService tabuleiroReceberPacoteService;
    private JanelaAlerta janelaAlerta;

    public IndexService(Pane tabuleiroPane, Text numeroPecas, Text numeroPecasAdversarias, Text tipoJogador, Text turnoAtual, TextField escreverMensagem, TextArea chat, Button pegarPeca, Button passarTurno) {
        this.tabuleiroPane = tabuleiroPane;
        this.numeroPecas = numeroPecas;
        this.numeroPecasAdversarias = numeroPecasAdversarias;
        this.tipoJogador = tipoJogador;
        this.turnoAtual = turnoAtual;
        this.escreverMensagem = escreverMensagem;
        this.chat = chat;
        this.pegarPeca = pegarPeca;
        this.pegarPeca = pegarPeca;
        this.passarTurno = passarTurno;
        janelaAlerta = new JanelaAlerta();
    }

    public void iniciarJogo() {
        comunicacaoService = new ComunicacaoService(janelaAlerta);
        comunicacaoService.iniciarComunicacao();
        chatService = new ChatService(escreverMensagem, chat, comunicacaoService);
        tabuleiroService = new TabuleiroService(tabuleiroPane, numeroPecas, numeroPecasAdversarias, tipoJogador, turnoAtual, pegarPeca, passarTurno, janelaAlerta, chatService, comunicacaoService);
        tabuleiroService.iniciarNovoJogo(comunicacaoService.isServidor());
        tabuleiroEnviarPacoteService = new TabuleiroEnviarPacoteService(tabuleiroService);
        tabuleiroReceberPacoteService = new TabuleiroReceberPacoteService(tabuleiroService);
        tabuleiroReceberPacoteService.iniciarThreadRecebePacotes();
    }

    public void sairPartida() {
        tabuleiroEnviarPacoteService.enviarPacoteSairPartida();
    }

    public ChatService getChatService() {
        return chatService;
    }

    public TabuleiroService getTabuleiroService() {
        return tabuleiroService;
    }

    public TabuleiroEnviarPacoteService getTabuleiroEnviarPacoteService() {
        return tabuleiroEnviarPacoteService;
    }
}

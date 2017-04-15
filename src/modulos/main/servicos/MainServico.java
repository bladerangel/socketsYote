package modulos.main.servicos;

import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import modulos.chat.servicos.ChatServico;
import modulos.comunicacao.servicos.ComunicacaoServico;
import modulos.tabuleiro.servicos.TabuleiroEnviarPacoteServico;
import modulos.tabuleiro.servicos.TabuleiroReceberPacoteServicos;
import modulos.tabuleiro.servicos.TabuleiroServico;
import utils.JanelaAlerta;

public class MainServico {

    private ComunicacaoServico comunicacaoServico;
    private ChatServico chatServico;
    private TabuleiroServico tabuleiroServico;
    private TabuleiroEnviarPacoteServico tabuleiroEnviarPacoteServico;
    private TabuleiroReceberPacoteServicos tabuleiroReceberPacoteServicos;
    private JanelaAlerta janelaAlerta;

    public MainServico(Pane tabuleiroPane, Text numeroPecas, Text numeroPecasAdversarias, Text tipoJogador, Text turnoAtual, TextField escreverMensagem, TextArea chat, Button pegarPeca, Button passarTurno) {

        janelaAlerta = new JanelaAlerta();
        comunicacaoServico = new ComunicacaoServico(janelaAlerta);
        comunicacaoServico.iniciarComunicacao();
        chatServico = new ChatServico(escreverMensagem, chat, comunicacaoServico);
        tabuleiroServico = new TabuleiroServico(tabuleiroPane, numeroPecas, numeroPecasAdversarias, tipoJogador, turnoAtual, pegarPeca, passarTurno, janelaAlerta, chatServico, comunicacaoServico);
        tabuleiroEnviarPacoteServico = new TabuleiroEnviarPacoteServico(tabuleiroServico, comunicacaoServico, chatServico);
        tabuleiroEnviarPacoteServico.enviarPacoteIniciarPartida(comunicacaoServico.isServidor());
        tabuleiroReceberPacoteServicos = new TabuleiroReceberPacoteServicos(janelaAlerta, tabuleiroServico);
        tabuleiroReceberPacoteServicos.iniciarThreadRecebePacotes();
    }

    public void sairPartida() {
        tabuleiroEnviarPacoteServico.enviarPacoteSairPartida();
    }

    public ChatServico getChatServico() {
        return chatServico;
    }

    public TabuleiroEnviarPacoteServico getTabuleiroEnviarPacoteServico() {
        return tabuleiroEnviarPacoteServico;
    }
}
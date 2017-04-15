package services;

import models.ComunicacaoTCP;
import utils.JanelaAlerta;

import java.io.IOException;

public class ComunicacaoService {

    private ComunicacaoTCP comunicacao;
    private JanelaAlerta janelaAlerta;
    private boolean servidor;

    public ComunicacaoService(JanelaAlerta janelaAlerta) {
        this.janelaAlerta = janelaAlerta;
    }

    public void iniciarComunicacao() {
        try {
            comunicacao = new ComunicacaoTCP();
            comunicacao.iniciarServidor(9999);
            janelaAlerta.janelaAlerta("Iniciar Partida", null, "Aguarde o jogador 2 conectar-se ....");
            comunicacao.esperandoConexao();
            servidor = true;
        } catch (IOException e) {
            try {
                janelaAlerta.janelaAlertaRunLater("Iniciar Partida", null, "O jogador 2 se conectou!");
                comunicacao.iniciarCliente(9999);
                servidor = false;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    public ComunicacaoTCP getComunicacao() {
        return comunicacao;
    }

    public boolean isServidor() {
        return servidor;
    }
}

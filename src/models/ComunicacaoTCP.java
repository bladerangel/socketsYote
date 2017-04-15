package models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class ComunicacaoTCP {

    private Socket cliente;
    private ServerSocket servidor;
    private DataInputStream in;
    private DataOutputStream out;
    private String mensagem;

    public void iniciarServidor(int porta) throws IOException {
        servidor = new ServerSocket(porta);
    }

    public void iniciarCliente(int porta) throws IOException {
        cliente = new Socket(InetAddress.getLocalHost(), porta);
        iniciarStreams();
    }

    public void iniciarStreams() {
        try {
            out = new DataOutputStream(cliente.getOutputStream());
            in = new DataInputStream(cliente.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isConectado() {
       return !cliente.isOutputShutdown();
    }

    public void esperandoConexao() throws IOException {
        cliente = servidor.accept();
        iniciarStreams();
    }

    public void enviarPacote(String mensagem) {
        try {
            out.writeUTF(mensagem);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String receberPacote() {
        try {
            mensagem = in.readUTF();
            System.out.println("Mensagem Recebida: " + mensagem);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mensagem;

    }

    public void fecharConexao() {
        try {
            cliente.shutdownOutput();
            cliente.shutdownInput();
            cliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

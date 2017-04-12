package models;

import javax.swing.*;
import java.io.IOException;
import java.net.*;


public class Comunicacao {

    private DatagramSocket client, server;
    private byte[] serverbffr, clientbffr;

    public Comunicacao(int porta) throws SocketException {
        client = new DatagramSocket();
        server = new DatagramSocket(porta);
        serverbffr = new byte[1000];
        clientbffr = new byte[1000];
    }

    public void enviar(String mensagem, int porta) {
        try {
            serverbffr = mensagem.getBytes();
            DatagramPacket sendpack = new DatagramPacket(serverbffr,
                    serverbffr.length, InetAddress.getLocalHost(), porta);
            client.send(sendpack);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void recebePacote() {

        new Thread(() -> {
            try {
                System.out.println("entrou");
                JOptionPane.showMessageDialog(null,
                        "Conecte-se com o Player 2!", "Jogar",
                        JOptionPane.INFORMATION_MESSAGE);
                while (true) {

                    DatagramPacket datapack = new DatagramPacket(
                            clientbffr, clientbffr.length);
                    server.receive(datapack);
                    String msg = new String(datapack.getData(), 0,
                            datapack.getLength());
                    System.out.println(msg);
                    /*if (msg.matches("^[0-9]*$")) {
                        jogarDadosPlayer2(Integer.parseInt(msg));
                        textArea.append("\nPlayer2" + " tirou " + msg
                                + " no dado.");
                    } else if (msg.equals("comecar")) {
                        lbPlayer1.setVisible(true);
                        btDados.setVisible(true);
                        lbPlay.setVisible(true);
                        enviar("comecar");

                    } else
                        textArea.append("\nPlayer2:" + msg);*/
                }
            } catch (Exception e) {
            }

        }).start();

    }

}

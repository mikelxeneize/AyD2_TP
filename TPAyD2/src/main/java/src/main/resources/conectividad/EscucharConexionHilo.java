package src.main.resources.conectividad;

import src.main.resources.backEnd.Nucleo;

import java.io.IOException;
import java.net.ServerSocket;

public class EscucharConexionHilo extends Thread{
    private int puertopersonal;
    private ServerSocket serverSocket;

    public EscucharConexionHilo(int puertopersonal) {
        this.puertopersonal=puertopersonal;
    }

    @Override
    public void run(){
        try {
            this.serverSocket = new ServerSocket(puertopersonal);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            serverSocket.accept();
            Nucleo.getInstance().getConectividad().setConectado(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

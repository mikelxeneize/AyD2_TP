package src.main.resources.conectividad;

import src.main.resources.backEnd.Nucleo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RecibirMensajeHilo extends Thread {
    private Socket socket;

    public RecibirMensajeHilo(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String msg = null;
        try {
            msg = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Nucleo.getInstance().mostrarMensaje(msg);
    }



}

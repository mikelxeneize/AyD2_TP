package src.main.resources.conectividad;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class EnviarMensajeHilo extends Thread{
    private Socket socket;
    private String mensaje;
    public EnviarMensajeHilo(Socket socket, String mensaje){
        this.socket=socket;
        this.mensaje=mensaje;
    }
    @Override
    public void run() {

        PrintWriter out = null;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

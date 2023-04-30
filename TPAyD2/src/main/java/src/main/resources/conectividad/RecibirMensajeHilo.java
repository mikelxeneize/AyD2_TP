package src.main.resources.conectividad;

import src.main.resources.backEnd.Nucleo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RecibirMensajeHilo extends Thread {
    private Socket socket;
	private Conectividad conectividad;

    public RecibirMensajeHilo(Socket socket, Conectividad conectividad){
        this.socket=socket;
        this.conectividad=conectividad;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        Mensaje mensaje;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String msg = null;
        try {
			msg = in.readLine();
			mensaje = new Mensaje( msg,"mensaje recibido");
			this.conectividad.recibirMensaje();
		} catch (IOException e) {
			mensaje = new Mensaje( msg,"conexion cerrada");
		}
        
        this.conectividad.notificarAccion(mensaje);
    }



}

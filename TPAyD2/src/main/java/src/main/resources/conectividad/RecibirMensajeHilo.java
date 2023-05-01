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
    	String msg = null;
    	do {
	        BufferedReader in = null;
	        Mensaje mensaje;
	        try {
	            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	        try {
				msg = in.readLine();
				if (msg == null) {
					mensaje = new Mensaje( msg,"conexion cerrada");
				}else {
					mensaje = new Mensaje( msg,"mensaje recibido");	
				}
				this.conectividad.notificarAccion(mensaje);
			} catch (IOException e) {
				System.out.println("escepcion, creo que se cerro la conexion");
			}
	        
    	}while(msg != null); //implica que se cerro la conexion

    }

}

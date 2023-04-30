package src.main.resources.conectividad;

import src.main.resources.backEnd.Nucleo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EscucharConexionHilo extends Thread{
    private int puertopersonal;
    private ServerSocket serverSocket;
    private Socket socket;
    private Conectividad conectividad;
    public EscucharConexionHilo(int puertopersonal, Conectividad conectividad) {
        this.puertopersonal=puertopersonal;
        this.conectividad=conectividad;
    }

    @Override
    public void run(){
        try {
            System.out.println(puertopersonal);
            this.serverSocket = new ServerSocket(puertopersonal);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
        	socket=serverSocket.accept();
        	this.conectividad.setConectado(true);
        	this.conectividad.setSocket(socket);
        	this.conectividad.recibirMensaje();
        	Mensaje mensaje = new Mensaje( null,"conexion establecida");
        	this.conectividad.notificarAccion(mensaje);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

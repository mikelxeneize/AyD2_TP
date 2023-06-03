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
            System.out.println("server socket abierto");
            this.conectividad.setServerSocket(this.serverSocket);
        } catch (IOException e) {
        	Mensaje mensaje = new Mensaje("puerto ocupado" ,"error al escuchar");
        	this.conectividad.notificarAccion(mensaje);
        }
        try {
        	socket=serverSocket.accept();
        	this.socket.setReuseAddress(true);
        	System.out.println("conexion establecida con otro cliente ");
        	this.serverSocket.close();
            System.out.println("server socket cerrado");
        	this.conectividad.setConectado(true);
        	this.conectividad.setSocket(socket);
        	this.conectividad.recibirMensaje(); 
        	Mensaje mensaje = new Mensaje( null,"conexion establecida"); //alguien se conecto a vos
        	this.conectividad.notificarAccion(mensaje);
        } catch (IOException e) {
            System.out.println(""); //cerras la escucha cuando vos podes iniciar la conexion
        }
    }
}

package Servidor.Servidor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;



public class Servidor {

	private ArrayList<SocketBean> listaConectados = new ArrayList<SocketBean>();
	private ServerSocket serverSocket;
	private static Servidor instance;


	public static Servidor getInstance() throws IOException {
		if (Servidor.instance == null) {
			Servidor.instance = new Servidor();
		}
		return instance;
	}
	
	
	public Servidor() throws IOException {
		iniciarEscucha();
	}
	
	public void iniciarEscucha() throws IOException {
		
	}


	public void enviarMensajeACliente(MensajeEncriptado mensaje) throws IOException {
		//Verificar que este registrado en el arrraylist
		int puerto;
		String ip;
		
		for(SocketBean socket: listaConectados) {
			puerto=socket.getPuerto();
			ip=socket.getSocket().getInetAddress().getHostAddress();
			if(ip==mensaje.getIp() && puerto== mensaje.getPuerto()) {
				PrintWriter out = new PrintWriter(socket.getSocket().getOutputStream(), true);
				out.println(mensaje.toString());
			}
			else {
				//Si no esta registrado
				
			}
		}
		
		
	}


	public void agregarPuertoFalso(int puerto, Socket socket) {
		for(SocketBean socketBean: listaConectados) {
			if(socketBean.getSocket() ==socket) {
				socketBean.setPuerto(puerto);
			}
		}
	}
    
	
	
}

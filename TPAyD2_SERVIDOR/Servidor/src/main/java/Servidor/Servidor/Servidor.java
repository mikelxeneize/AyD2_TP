package Servidor.Servidor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;



public class Servidor {

	private ArrayList<Cliente> listaConectados = new ArrayList<Cliente>();
	private ServerSocket serverSocket;
	private static Servidor instance;

    private int puertoServidor= 5000;
    private String ipServidor="localhost";

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
		Socket socket;
		Cliente cliente;
		ServidorRecibirMensajeHilo recibirMensajeHilo;
		serverSocket= new ServerSocket(5000);
		while(true) {
			socket=serverSocket.accept();
			cliente= getRegistradoByIp(socket.getPort(),socket.getInetAddress().toString());
			if(cliente != null) {
				this.enlazarSocket(socket);
				recibirMensajeHilo=new ServidorRecibirMensajeHilo(cliente,this);
				recibirMensajeHilo.start();
			}
			else{
				this.listaConectados.add(new Cliente(socket.getPort(),socket.getInetAddress().toString()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		        out.println("CERRAR_SOCKET");
				socket.close();
				
			}
		}
		
	}


	private void enlazarSocket(Socket socket) {
		int puerto;
		String ip;
		for(Cliente socketaux: listaConectados) {
			puerto=socketaux.getPuerto();
			ip=socketaux.getIp();
			if(ip.equals(socket.getInetAddress().toString()) && puerto== socket.getPort()) {
				socketaux.setSocket(socket);
				socketaux.setEstado("Ocupado");
			}
		}
	}


	private Cliente getRegistradoByIp(int puertoObj, String ipObj) {
		int puerto;
		String ip;
		for(Cliente cliente: listaConectados) {
			puerto=cliente.getPuerto();
			ip=cliente.getIp();
			if(ip.equals(ipObj) && puerto== puertoObj) {
				return cliente;
			}
		}
		return null;
	}

/*
	public void enviarMensajeACliente(MensajeEncriptado mensaje, Socket socket) throws IOException {
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		out.println(mensaje.toString());
		
		
		
	}*/

	public void enviarMensajeACliente(MensajeEncriptado mensaje, String ipObj, int puertoObj) throws IOException {
		String ip;
		int puerto;
		for(Cliente cliente: listaConectados) {
			puerto=cliente.getPuerto();
			ip=cliente.getIp();
			if(ip.equals(ipObj) && puerto== puertoObj) {
				PrintWriter out = new PrintWriter(cliente.getSocket().getOutputStream(), true);
				out.println(mensaje.toString());
			}
		}
		
		
		
	}
	public Socket iniciarConexionAReceptor(MensajeEncriptado mensaje, MensajeEncriptado mensajeAReceptor) throws UnknownHostException, IOException, InterruptedException {
		Cliente cliente;
		cliente= getRegistradoByIp(mensaje.getPuerto(),mensaje.getIp());
		
		if(cliente != null ) {//cliente registrado y disponible para conectarse
			Socket socket=new Socket(mensaje.getIpTruncada(),mensaje.getPuerto());//se conecta al servidor
			Thread.sleep(500);
			cliente.setIpReceptor(mensajeAReceptor.getIp());
			cliente.setPuertoReceptor(mensajeAReceptor.getPuerto());
			cliente.setSocket(socket);
			//mensaje para informar quien lo contacto
			this.enviarMensajeACliente(mensajeAReceptor, socket.getInetAddress().toString(),socket.getPort());
			ServidorRecibirMensajeHilo recibirMensajeHilo=new ServidorRecibirMensajeHilo(cliente,this);
			recibirMensajeHilo.start();
			return socket;
			//cliente aun no registrado, devolver excepcion
		}
		return null;
		
	}
	public void cortarConexionAReceptor(String ip, int puerto) throws IOException {
		Cliente cliente;
		cliente= getRegistradoByIp(puerto,ip);
		
		if(cliente != null ) {//cliente registrado y disponible para conectarse
			cliente.getSocket().close();
			cliente.setEstado("Disponible");
			cliente.setIpReceptor(null);
		}
		
	}

    
	
	
}

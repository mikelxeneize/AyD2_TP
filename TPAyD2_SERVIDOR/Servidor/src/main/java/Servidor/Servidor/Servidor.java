package Servidor.Servidor;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class Servidor {

	private ArrayList<Cliente> listaConectados = new ArrayList<Cliente>();
	private ServerSocket serverSocket;
	private static Servidor instance;

	private int puertoServidor = 5000;
	private String ipServidor = "localhost";

	public static Servidor getInstance() throws IOException {
		if (Servidor.instance == null) {
			Servidor.instance = new Servidor();
		}
		return instance;
	}

	public Servidor() throws IOException {
		iniciarHeartBeat();
		iniciarEscucha();
	}

	private void iniciarHeartBeat() {
		HeartBeatHilo hilo = new HeartBeatHilo(this,false);
		hilo.start();
		
	}

	public void iniciarEscucha() throws IOException {
		Socket socket;
		Cliente cliente;
		ServidorRecibirMensajeHilo recibirMensajeHilo;
		serverSocket = new ServerSocket();
		serverSocket.setReuseAddress(true);
		serverSocket.bind(new InetSocketAddress(puertoServidor));
		while (true) {
			socket = serverSocket.accept();
			socket.setReuseAddress(true);
			cliente = new Cliente(socket.getPort(), socket.getInetAddress().toString(), socket);
			this.listaConectados.add(cliente);

			recibirMensajeHilo = new ServidorRecibirMensajeHilo(cliente, this);
			recibirMensajeHilo.start();
		}

	}

	/*
	 * private void enlazarSocket(Socket socket) { int puerto; String ip;
	 * for(Cliente socketaux: listaConectados) { puerto=socketaux.getPuerto();
	 * ip=socketaux.getIp(); if(ip.equals(socket.getInetAddress().toString()) &&
	 * puerto== socket.getPort()) { socketaux.setSocket(socket);
	 * socketaux.setEstado("Ocupado"); } } }
	 */

	private Cliente getRegistradoByIp(int puertoObj, String ipObj) {
		int puerto;
		String ip;
		for (Cliente cliente : listaConectados) {
			puerto = cliente.getPuerto();
			ip = cliente.getIp();
			if (ip.equals(ipObj) && puerto == puertoObj) {
				return cliente;
			}
		}
		return null;
	}

	/*
	 * public void enviarMensajeACliente(MensajeEncriptado mensaje, Socket socket)
	 * throws IOException { PrintWriter out = new
	 * PrintWriter(socket.getOutputStream(), true); out.println(mensaje.toString());
	 * 
	 * 
	 * 
	 * }
	 */

	public void enviarMensajeACliente(MensajeEncriptado mensaje, String ipObj, int puertoObj) throws IOException {
		String ip;
		int puerto;
		for (Cliente cliente : listaConectados) {
			puerto = cliente.getPuerto();
			ip = cliente.getIp();
			if (ip.equals(ipObj) && puerto == puertoObj) {
				PrintWriter out = new PrintWriter(cliente.getSocket().getOutputStream(), true);
				out.println(mensaje.toString());
				System.out.println("17: "+mensaje.toString());
			}
		}

	}

	public void iniciarConexionAReceptor(MensajeEncriptado mensaje, MensajeEncriptado mensajeAReceptor, MensajeEncriptado mensajeConfirmacion)
			throws UnknownHostException, IOException, InterruptedException {
		Cliente cliente;
		cliente = getRegistradoByIp(mensaje.getPuerto(), mensaje.getIp());

		if (cliente != null && cliente.getEstado().equals("Disponible")) {// cliente registrado y disponible para conectarse
			Socket socket = cliente.getSocket();
			cliente.setIpReceptor(mensajeAReceptor.getIp());
			cliente.setPuertoReceptor(mensajeAReceptor.getPuerto());
			mensajeConfirmacion.setMensaje("%Conexion_establecida%");
			// mensaje para informar quien lo contacto
			this.enviarMensajeACliente(mensajeAReceptor, socket.getInetAddress().toString(), socket.getPort());
			ServidorRecibirMensajeHilo recibirMensajeHilo = new ServidorRecibirMensajeHilo(cliente, this);
			recibirMensajeHilo.start();
			// cliente aun no registrado, devolver excepcion
		} else {// rechaza la conexion y le avisa al cliente 1 que no se pudo conectar
			mensajeConfirmacion.setMensaje("%Conexion_rechazada%");
		}

	}

	public void cortarConexionAReceptor(String ip, int puerto) throws IOException {
		Cliente cliente;
		cliente = getRegistradoByIp(puerto, ip);
		if (cliente != null) {// cliente registrado y disponible para conectarse

			PrintWriter out = new PrintWriter(cliente.getSocket().getOutputStream(), true);
			out.println(cliente.getIpReceptor() + ":" + Integer.toString(cliente.getPuertoReceptor()) + ":"
					+ "%cerrar_conexion%" + ":" + "pepe");
			System.out.println("14: "+cliente.getIpReceptor() + ":" + Integer.toString(cliente.getPuertoReceptor()) + ":"
					+ "%cerrar_conexion%" + ":" + "pepe");

			cliente.setEstado("Disponible");
			this.MandarLista1();
			cliente.setIpReceptor(null);
		}

	}

	public ArrayList<Cliente> getListaConectados() {
		return listaConectados;
	}

	public void MandarLista1()  {
		PrintWriter out = null;
		Cliente cliente;
		System.out.println("");
		String lista="";
		//Armo la lista en string primero 
		for (int j = 0; j < this.getListaConectados().size(); j++) {
			cliente=this.getListaConectados().get(j);
			lista+=  this.getListaConectados().get(j).actualizacion();	
		}
		

		for (int i = 0; i < this.getListaConectados().size(); i++) {
			cliente=this.getListaConectados().get(i);
			try {
				out = new PrintWriter(this.getListaConectados().get(i).getSocket().getOutputStream(), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			out.println(cliente.getIp() + ":" + Integer.toString(cliente.getPuerto()) + ":" + "%Actualizar%" + ":"
					+ lista);
			System.out.println("40: "+cliente.getIp() + ":" + Integer.toString(cliente.getPuerto()) + ":" + "%Actualizar%" + ":"
					+ lista);
		}
		
	}
}

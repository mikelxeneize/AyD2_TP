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

	private Socket servidorVecino;
	private boolean isPrincipal = false;

	// parametros de notificacion de configuracion
	private int PUERTO_1 = 5001;
	private int PUERTO_2 = 5002;

	private int puertoServidor;
	private String ipServidor = "localhost";

	public int getPuertoServidor() {
		return puertoServidor;
	}

	public String getIpServidor() {
		return ipServidor;
	}

	public static Servidor getInstance() throws IOException {
		if (Servidor.instance == null) {
			Servidor.instance = new Servidor();
		}
		return instance;
	}

	public Servidor() throws IOException {
		iniciarHeartBeat();
		redundanciaPasiva();
		if (!this.isPrincipal) {
			notificarServidor();
		}
		iniciarEscucha();

	}

	private void iniciarHeartBeat() {
		HeartBeatHilo hilo = new HeartBeatHilo(this, false);
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
			if (socket.getPort() != PUERTO_1 || socket.getPort() != PUERTO_2) {
				this.listaConectados.add(cliente);
			}
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
				// System.out.println("17: "+mensaje.toString());
			}
		}

	}

	public boolean iniciarConexionAReceptor(MensajeEncriptado mensaje, MensajeEncriptado mensajeAReceptor,
			MensajeEncriptado mensajeConfirmacion) throws UnknownHostException, IOException, InterruptedException {
		Cliente cliente;
		cliente = getRegistradoByIp(mensaje.getPuerto(), mensaje.getIp());

		if (cliente != null && cliente.getEstado().equals("Disponible")) {// cliente registrado y disponible para
																			// conectarse
			Socket socket = cliente.getSocket();
			cliente.setIpReceptor(mensajeAReceptor.getIp());
			cliente.setPuertoReceptor(mensajeAReceptor.getPuerto());
			mensajeConfirmacion.setMensaje("%Conexion_establecida%");
			mensajeConfirmacion.setPuerto(cliente.getPuerto());
			// mensaje para informar quien lo contacto
			cliente.setEstado("Ocupado");
			this.enviarMensajeACliente(mensajeAReceptor, socket.getInetAddress().toString(), socket.getPort());
			ServidorRecibirMensajeHilo recibirMensajeHilo = new ServidorRecibirMensajeHilo(cliente, this);
			recibirMensajeHilo.start();
			return true;
			// cliente aun no registrado, devolver excepcion
		} else {// rechaza la conexion y le avisa al cliente 1 que no se pudo conectar
			mensajeConfirmacion.setMensaje("%Conexion_rechazada%");
			return false;
		}

	}

	public void cortarConexionAReceptor(String ip, int puerto) throws IOException {
		Cliente cliente;
		cliente = getRegistradoByIp(puerto, ip);
		if (cliente != null) {// cliente registrado y disponible para conectarse

			PrintWriter out = new PrintWriter(cliente.getSocket().getOutputStream(), true);
			out.println(cliente.getIpReceptor() + ":" + Integer.toString(cliente.getPuertoReceptor()) + ":"
					+ "%cerrar_conexion%" + ":" + "pepe");
			System.out.println("14: " + cliente.getIpReceptor() + ":" + Integer.toString(cliente.getPuertoReceptor())
					+ ":" + "%cerrar_conexion%" + ":" + "pepe");

			cliente.setEstado("Disponible");
			this.MandarLista1();
			cliente.setIpReceptor(null);
		}

	}

	public ArrayList<Cliente> getListaConectados() {
		return listaConectados;
	}

	public void MandarLista1() {
		PrintWriter out = null;
		Cliente cliente;
		System.out.println("");
		String lista = "";
		// Armo la lista en string primero
		if (this.isPrincipal() == true) {
			for (Cliente cliente1 : this.getListaConectados()) {
				if (cliente1.getUsername() == null) {
					this.getListaConectados().remove(cliente1);
				}
			}

			for (int j = 0; j < this.getListaConectados().size(); j++) {
				cliente = this.getListaConectados().get(j);
				lista += this.getListaConectados().get(j).actualizacion();
			}

			for (int i = 0; i < this.getListaConectados().size(); i++) {
				cliente = this.getListaConectados().get(i);
				try {
					out = new PrintWriter(this.getListaConectados().get(i).getSocket().getOutputStream(), true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				out.println(cliente.getIp() + ":" + Integer.toString(cliente.getPuerto()) + ":" + "%Actualizar%" + ":"
						+ lista);
				System.out.println("40: " + cliente.getIp() + ":" + Integer.toString(cliente.getPuerto()) + ":"
						+ "%Actualizar%" + ":" + lista);
			}
		}
	}

	// determina si hay servidor principal y decide en que puerto va a escuchar
	private void redundanciaPasiva() {
		this.isPrincipal = false;
		this.puertoServidor = PUERTO_1;
		if (conectarServidor("localhost", PUERTO_1)) { // hay un servidor principal en puerto_1
			this.puertoServidor = PUERTO_2;
		} else if (conectarServidor("localhost", PUERTO_2)) { // hay servidor principal en puerto_2
			// seguis escuchando en el puerto_1
		} else { // no hay servidor principal, entonces este lo sera
			System.out.println("44: No hay server principal");
			this.isPrincipal = true;
		}
	}

	public boolean conectarServidor(String ip, int puerto) {
		boolean conecto = true;
		System.out.println("43: intentando conexion a servidor en puerto " + puerto);
		try {
			this.servidorVecino = new Socket(ip, puerto);
		} catch (Exception e) {
			conecto = false;
		}
		return conecto;
	}

	private void notificarServidor() {
		PrintWriter out;
		try {
			out = new PrintWriter(servidorVecino.getOutputStream(), true);
			out.println(this.ipServidor + ":" + this.puertoServidor + ":" + "%nuevoServidorPasivo%" + ":"
					+ "Servidor_Servidor");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(this.ipServidor + ":" + this.puertoServidor + ":" + "%nuevoServidorPasivo%" + ":"
				+ "Servidor_Servidor");
	}

	void notificarUsuarios(String ip, int puerto) {
		for (Cliente cliente : this.listaConectados) {
			try {
				PrintWriter out = new PrintWriter(cliente.getSocket().getOutputStream(), true);
				out.println(ip + ":" + puerto + ":" + "%nuevoServidorPasivo%" + ":" + "Servidor_Cliente");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(cliente.getIp() + ":" + Integer.toString(cliente.getPuerto()) + ":"
					+ "%nuevoServidorPasivo%" + ":" + "Servidor_Cliente");
		}
	}

	public boolean isPrincipal() {
		return isPrincipal;
	}

}

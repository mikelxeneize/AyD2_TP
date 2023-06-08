package src.main.resources.conectividad;

import src.main.resources.backEnd.Nucleo;
import src.main.resources.backEnd.Cliente;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Conectividad extends Observable implements IConectividad {
	// socket y datos ServidorPrincipal 
	private serverData servidorPrincipal;

	// socket y datos ServidorPrincipal 
	private serverData servidorSecundario;

	// Informacion personal
	private int puertoPersonal;
	private String ipPersonal;

	// Informacion receptor
	private int puertoReceptor;
	private String ipReceptor;

	private List<Observer> observers = new ArrayList<>();

	// Flag de si se encuentra en una conversacion
	private boolean conectado;
	private String estado = "";

	private String clave;
	private String algoritmo;

	private String username;

	public Conectividad() {
		this.conectado = false;
		this.servidorPrincipal = new serverData("localhost", 50100);
		this.servidorSecundario = null;
	}

	public List<Observer> getObservers() {
		return observers;
	}

	public void iniciarConversacion(String ipDestino, int puertoDestino)
			throws UnknownHostException, IOException, IllegalArgumentException { // tiene que devolver una excepcion de
																					// no conexion
		this.ipPersonal = "localhost";

		PrintWriter out = new PrintWriter(servidorPrincipal.socket.getOutputStream(), true);
		out.println(ipDestino + ":" + Integer.toString(puertoDestino) + ":" + "%mensaje%" + ":" + "pepe");

		this.ipReceptor = ipDestino;
		this.puertoReceptor = puertoDestino;

	}

	public void iniciarConexionServidorPrincipal() throws UnknownHostException, IOException, IllegalArgumentException {
		this.ipPersonal = "localhost";
		System.out.println("socket abierto servidor inicial");
		this.servidorPrincipal.socket = new Socket();
		this.servidorPrincipal.socket.setReuseAddress(true);
		this.servidorPrincipal.socket.bind(new InetSocketAddress(this.puertoPersonal));
		this.servidorPrincipal.socket.connect(new InetSocketAddress(this.servidorPrincipal.ip, this.servidorPrincipal.puerto));

		this.setSocketPrincipal(servidorPrincipal.socket);
		System.out.println("Socket conectado al servidorPrincipal");
		this.recibirMensaje();

		PrintWriter out = new PrintWriter(servidorPrincipal.socket.getOutputStream(), true);
		out.println(this.ipPersonal + ":" + this.puertoPersonal + ":" + "%nombre_usuario%" + ":" + this.username); // comunico al servidor mi nombre de usuario

	}
	
	public void iniciarConexionServidorSecundario() throws UnknownHostException, IOException, IllegalArgumentException {
		this.ipPersonal = "localhost";
		System.out.println("socket abierto servidorSecundario");
		this.servidorSecundario.socket = new Socket();
		this.servidorSecundario.socket.setReuseAddress(true);
		this.servidorSecundario.socket.bind(new InetSocketAddress(this.puertoPersonal));
		this.servidorSecundario.socket.connect(new InetSocketAddress(this.servidorSecundario.ip, this.servidorSecundario.puerto));

		this.setSocketSecundario(servidorSecundario.socket);
		System.out.println("Socket conectado al servidorSecundario");
		this.recibirMensaje();

		PrintWriter out = new PrintWriter(servidorPrincipal.socket.getOutputStream(), true);
		out.println(this.ipPersonal + ":" + this.puertoPersonal + ":" + "%nombre_usuario%" + ":" + this.username);
	}

	public int getPuertoReceptor() {
		return puertoReceptor;
	}

	public void setPuertoReceptor(int puertoReceptor) {
		this.puertoReceptor = puertoReceptor;
	}

	public String getIpReceptor() {
		return ipReceptor;
	}

	public void setIpReceptor(String ipReceptor) {
		this.ipReceptor = ipReceptor;
	}

	public void recibirMensaje() {
		RecibirMensajeHilo recibirMensaje = new RecibirMensajeHilo(this.servidorPrincipal.socket, this);
		recibirMensaje.start();
	}

	public void notificarAccion(Mensaje mensaje) {
		this.setChanged();
		this.notifyObservers(mensaje);
	}

	public void enviarMensaje(String mensajeaenviar) throws IOException {
		PrintWriter out = new PrintWriter(servidorPrincipal.socket.getOutputStream(), true);
		String mensajeencriptado = Codificacion.encriptar(getClave(), mensajeaenviar, getAlgoritmo());
		out.println(
				this.ipReceptor + ":" + Integer.toString(this.puertoReceptor) + ":" + mensajeencriptado + ":" + "pepe");
	}

	public void cerrarConexion() throws IOException {
		PrintWriter out = new PrintWriter(servidorPrincipal.socket.getOutputStream(), true);
		out.println(this.ipReceptor + ":" + Integer.toString(this.puertoReceptor) + ":" + "%cerrar_conexion%" + ":"
				+ "pepe");
		this.setIpReceptor(null);
	}

	public int getPuertopersonal() {
		return puertoPersonal;
	}

	public void setPuertopersonal(int puertopersonal) {
		this.puertoPersonal = puertopersonal;
	}

	public String getIppersonal() {
		return ipPersonal;
	}

	public void setIppersonal(String ippersonal) {
		this.ipPersonal = ippersonal;
	}

	public void setConectado(boolean conectado) {
		this.conectado = conectado;
	}

	public boolean isConectado() {
		return conectado;
	}

	public void setSocketPrincipal(Socket socket) {
		this.servidorPrincipal.socket = socket;
	}
	
	public void setSocketSecundario(Socket socket) {
		this.servidorSecundario.socket = socket;
	}
	public Socket getSocket() {
		return this.servidorPrincipal.socket;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getAlgoritmo() {
		return algoritmo;
	}

	public void setAlgoritmo(String algoritmo) {
		this.algoritmo = algoritmo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void actualizar(String cliente) {
		String[] partes = cliente.split("=");
		Cliente clienteNuevo = new Cliente(partes[0], partes[1], partes[2], partes[3]);
		int i = 0;
			while (i<Nucleo.getInstance().getConectados().size() &&(!Nucleo.getInstance().getConectados().get(i).getIp().equals(clienteNuevo.getIp()) || !Nucleo.getInstance().getConectados().get(i).getPuerto().equals(clienteNuevo.getPuerto()))) {
				i++;
			}
			if (i==Nucleo.getInstance().getConectados().size()) {
				Nucleo.getInstance().getConectados().add(clienteNuevo);
			}
			else{
				Nucleo.getInstance().getConectados().get(i).setEstado(clienteNuevo.getEstado());
			}
	}

	
}
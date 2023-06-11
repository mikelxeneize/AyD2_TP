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

	// parametros de notificacion de configuracion
	private int PUERTO_1 = 5001; 
	private int PUERTO_2 = 5002;
	
	private String clave;
	private String algoritmo;

	private String username;

	private long pingEchoTime=0;
	private long  pingEchoAbs=0;
	private long  HeartBeatTime=0;
	
	public Conectividad() {
		this.conectado = false;
		this.servidorPrincipal = new serverData("localhost", PUERTO_1);
		this.servidorSecundario = new serverData("localhost", PUERTO_2);
	}

	public List<Observer> getObservers() {
		return observers;
	}

	public void iniciarConversacion(String ipDestino, int puertoDestino)
			throws UnknownHostException, IOException, IllegalArgumentException { // tiene que devolver una excepcion de
																					// no conexion
		this.ipPersonal = "localhost";

		PrintWriter out = new PrintWriter(servidorPrincipal.socket.getOutputStream(), true);
		out.println(ipDestino + ":" + Integer.toString(puertoDestino) + ":" + "%Iniciar_Conversacion%" + ":" + "pepe");
		System.out.println("22: "+ipDestino + ":" + Integer.toString(puertoDestino) + ":" + "%Iniciar_Conversacion%" + ":" + "pepe");

		System.out.println("Esperando confirmacion de conexion del otro lado...");
		
		this.ipReceptor = ipDestino;
		this.puertoReceptor = puertoDestino;

	}

	public Socket iniciarConexionServidor(String ip, int puerto) throws IOException {
		Socket socket = new Socket();
		socket.setReuseAddress(true);
		socket.bind(new InetSocketAddress(this.puertoPersonal));
		socket.connect(new InetSocketAddress(ip,puerto));
		return socket;
	}
	
	public void iniciarConexionServidorPrincipal() throws UnknownHostException, IOException ,IllegalArgumentException {
		
		int banderaso=0;
		this.ipPersonal = "localhost";
		System.out.println("9: "+"intento conexion Servidor Principal");
			
		try {
			this.servidorPrincipal.socket = iniciarConexionServidor("localhost", PUERTO_1);
			PrintWriter out = new PrintWriter(this.servidorPrincipal.socket.getOutputStream(), true);
			out.println(this.ipPersonal + ":" + this.puertoPersonal + ":" + "%preguntar_principal%" + ":" + this.username);
			out.println(this.ipPersonal + ":" + this.puertoPersonal + ":" + "%nombre_usuario%" + ":" + this.username); // comunico al servidor mi nombre de usuario
			this.recibirMensaje();
		} catch (IOException e) {
			System.out.println("No se pudo conectar con el primer servidor");
			banderaso = 1;
		}
		try {
			this.servidorSecundario.socket = iniciarConexionServidor("localhost", PUERTO_2);
			PrintWriter out = new PrintWriter(this.servidorSecundario.socket.getOutputStream(), true);
			out.println(this.ipPersonal + ":" + this.puertoPersonal + ":" + "%preguntar_principal%" + ":" + this.username);
			out.println(this.ipPersonal + ":" + this.puertoPersonal + ":" + "%nombre_usuario%" + ":" + this.username); // comunico al servidor mi nombre de usuario
			this.servidorSwap();
		} catch (IOException e) {
			System.out.println("No se pudo conectar con el segundo servidor");
		}
		
		PingEchoHilo pingechohilo= new PingEchoHilo(this.servidorPrincipal,this);
		pingechohilo.start();
		System.out.println(this.ipPersonal + ":" + this.puertoPersonal + ":" + "%nombre_usuario%" + ":" + this.username);
	}

public void reintentarConexionServidorPrincipal() throws UnknownHostException, IOException ,IllegalArgumentException {
		
		Socket socket1, socket2;
		this.ipPersonal = "localhost";
		System.out.println("9: "+"intento conexion Servidor Principal");
			
		try {
			this.servidorPrincipal.socket = iniciarConexionServidor("localhost", PUERTO_1);
			PrintWriter out = new PrintWriter(this.servidorPrincipal.socket.getOutputStream(), true);
			out.println(this.ipPersonal + ":" + this.puertoPersonal + ":" + "%preguntar_principal%" + ":" + this.username);
			this.recibirMensaje();
			
		} catch (IOException e) {
			System.out.println("No se pudo conectar con el primer servidor");
		}
		

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
		System.out.println("20: "+
				this.ipReceptor + ":" + Integer.toString(this.puertoReceptor) + ":" + mensajeencriptado + ":" + "pepe");
	}

	public void cerrarConexion() throws IOException {
		PrintWriter out = new PrintWriter(servidorPrincipal.socket.getOutputStream(), true);
		out.println(this.ipReceptor + ":" + Integer.toString(this.puertoReceptor) + ":" + "%cerrar_conexion%" + ":"
				+ "pepe");
		System.out.println("21: "+this.ipReceptor + ":" + Integer.toString(this.puertoReceptor) + ":" + "%cerrar_conexion%" + ":"
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
 
		Nucleo.getInstance().getConectados().clear();

		String[] partes = cliente.split(";");
		
		for(int i=0; i<partes.length; i++) {
			
			String[] subpartes = partes[i].split("=");
			Cliente clienteNuevo = new Cliente(subpartes[0], subpartes[1], subpartes[2], subpartes[3]);
			Nucleo.getInstance().getConectados().add(clienteNuevo);
			
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Mensaje mensaje = new Mensaje("", "Actualizar");
		this.notificarAccion(mensaje);
	}

	public long getPingEchoTime() {
		return pingEchoTime;
	}

	public void setPingEchoTime(long pingEchoTime) {
		this.pingEchoTime = pingEchoTime;
	}
	public long getPingEchoAbs() {
		return pingEchoAbs;
	}

	public void setPingEchoAbs(long pingEchoTime) {
		this.pingEchoAbs = pingEchoTime;
	}

	public long getHeartBeatTime() {
		return HeartBeatTime;
	}

	public void setHeartBeatTime(long heartBeatTime) {
		HeartBeatTime = heartBeatTime;
	}

	public void reintento() {
		int cantIntentos=2;
		int tiempo=3000;
		int i = 0;
		while(i<cantIntentos) {
			System.out.println("Intento de reconexion "+ i+ "...");
			try {
				reintentarConexionServidorPrincipal();
			} catch (IllegalArgumentException | IOException e) {
				System.out.println("reintento de reconexion "+ i+ "fallido");
			}
			try {
				Thread.sleep(tiempo);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		if(i==cantIntentos) {
			servidorSwap();
		}
	}

	public void registrarServidorSecundario(String ip, int puerto) {
		this.servidorSecundario = new serverData(ip, puerto);
		try {
			this.servidorSecundario.setSocket(new Socket("localhost", puerto));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void actualizarServidorPrincipal(String ip, int puerto) {
		if (servidorSecundario != null && servidorSecundario.getPuerto() == puerto && servidorSecundario.getIp() == ip) {
			this.servidorSwap();
		}
	}

	private void servidorSwap() {
		serverData servidorAux; 
		servidorAux = this.servidorPrincipal; 
		servidorPrincipal = this.servidorSecundario;
		servidorSecundario = servidorAux;
		this.recibirMensaje();
	}
	
}
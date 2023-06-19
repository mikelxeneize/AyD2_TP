package src.main.resources.conectividad;

import src.main.resources.backEnd.Nucleo;
import utils.IComandos;
import src.main.resources.backEnd.Cliente;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Conectividad extends Observable implements IConectividad, IComandos{
	// socket y datos ServidorPrincipal 
	private serverData servidorPrincipal;

	// socket y datos ServidorPrincipal 
	private serverData servidorSecundario;

	// Informacion personal
	private int puertoPersonal;
	private String ipPersonal;
	private String usernamePersonal;
	
	// Informacion receptor
	private int puertoReceptor = -9999;
	private String ipReceptor = "";
	private String usernameReceptor = "";
	
	private List<Observer> observers = new ArrayList<>();

	// Flag de si se encuentra en una conversacion
	private boolean conectado;
	private String estado = "";

	// parametros de notificacion de configuracion
	private int PUERTO_1 = 5000; 
	private int PUERTO_2 = 5002;
	
	private String clave;
	private String algoritmo;

	

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

	public void iniciarConversacion(String ipDestino, int puertoDestino) throws UnknownHostException, IOException, IllegalArgumentException { // tiene que devolver una excepcion de
		this.ipPersonal = "localhost";
		
		MensajeExterno mensajeExterno = new MensajeExterno(
				ipPersonal, Integer.toString(puertoPersonal), usernamePersonal,
				ipDestino, Integer.toString(puertoDestino), usernameReceptor,
				INICIAR_CONVERSACION, " ", " ");														// no conexion
		enviarMensajeExterno(mensajeExterno, servidorPrincipal);
		
		System.out.println(mensajeExterno.toString());
		System.out.println("Esperando confirmacion de conexion del otro lado...");
		
	}

	public Socket iniciarConexionServidor(String ip, int puerto) throws IOException {
		Socket socket = new Socket();
		socket.setReuseAddress(true);
		socket.bind(new InetSocketAddress(this.puertoPersonal));
		socket.connect(new InetSocketAddress(ip,puerto));
		return socket;
	}
	
	public void iniciarConexionServidorPrincipal() throws UnknownHostException, IOException ,IllegalArgumentException {
		
		MensajeExterno mensajeExterno;
		int banderaso=0;
		this.ipPersonal = "localhost";
		System.out.println("9: "+"intento conexion Servidor Principal");
			
		try {
			this.servidorPrincipal.socket = iniciarConexionServidor("localhost", PUERTO_1);

			PrintWriter out = new PrintWriter(servidorPrincipal.socket.getOutputStream(), true);
			this.servidorPrincipal.setOut(out);
			
			mensajeExterno = new MensajeExterno(
					ipPersonal, Integer.toString(puertoPersonal), usernamePersonal,
					ipReceptor, Integer.toString(puertoReceptor), usernameReceptor,
					PREGUNTAR_PRINCIPAL, " ", " ");														// no conexion
			enviarMensajeExterno(mensajeExterno, servidorPrincipal);

			mensajeExterno.setComando(NOMBRE_USUARIO);			
			enviarMensajeExterno(mensajeExterno, servidorPrincipal); // comunico al servidor mi nombre de usuario
			
			this.recibirMensaje();
		} catch (IOException e) {
			System.out.println("No se pudo conectar con el primer servidor");
			banderaso = 1;
		}
		try {
			this.servidorSecundario.socket = iniciarConexionServidor("localhost", PUERTO_2);
			
			PrintWriter out = new PrintWriter(servidorSecundario.socket.getOutputStream(), true);
			this.servidorSecundario.setOut(out);
			
			mensajeExterno = new MensajeExterno(
					ipPersonal, Integer.toString(puertoPersonal), usernamePersonal,
					ipReceptor, Integer.toString(puertoReceptor), usernameReceptor,
					PREGUNTAR_PRINCIPAL, " ", " ");														// no conexion
			enviarMensajeExterno(mensajeExterno, servidorSecundario);
			
			enviarMensajeExterno(mensajeExterno, servidorSecundario);
			mensajeExterno.setComando(NOMBRE_USUARIO);			
			
			this.servidorSwap();
		} catch (IOException e) {
			System.out.println("No se pudo conectar con el segundo servidor");
			banderaso = 2;
		}
		if (banderaso == 2) {
			this.notificarAccion(new Mensaje("no se pudo conectar con ninguno de los servidores", "error ningun servidor conectado"));
		} else {
			PingEchoHilo pingechohilo= new PingEchoHilo(this.servidorPrincipal,this);
			pingechohilo.start();
			System.out.println(this.ipPersonal + ":" + this.puertoPersonal + ":" + "%nombre_usuario%" + ":" + this.usernamePersonal);		
		}
	}

public void reintentarConexionServidorPrincipal() throws UnknownHostException, IOException ,IllegalArgumentException {
		
		Socket socket1, socket2;
		MensajeExterno mensajeExterno; 
		this.ipPersonal = "localhost";
		System.out.println("9: "+"intento conexion Servidor Principal");
		
		try {
			this.servidorPrincipal.socket = iniciarConexionServidor("localhost", PUERTO_1);
			
			mensajeExterno = new MensajeExterno(
					ipPersonal, Integer.toString(puertoPersonal), usernamePersonal,
					ipReceptor, Integer.toString(puertoReceptor), usernameReceptor,
					PREGUNTAR_PRINCIPAL, " ", " ");
			
			enviarMensajeExterno(mensajeExterno, servidorPrincipal);
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

		
	public void enviarMensajeExterno(MensajeExterno mensajeExterno, serverData servidorData) {
		servidorData.out.println(mensajeExterno.toString());
	}

	public void cerrarConexion() throws IOException {
		
		MensajeExterno mensajeExterno = new MensajeExterno(
				ipPersonal, Integer.toString(puertoPersonal), usernamePersonal,
				ipReceptor, Integer.toString(puertoReceptor), usernameReceptor,
				CERRAR_CONVERSACION, " ", " ");														// no conexion
		enviarMensajeExterno(mensajeExterno, servidorPrincipal);

		System.out.println("21: "+ mensajeExterno.toString());
		this.setIpReceptor(null);
		this.setUsernameReceptor(null);
		this.setPuertoReceptor(0);
	}

	
	public void enviarMensajeCliente(String mensaje) throws IOException {
		String mensajeEncriptado = Codificacion.encriptar(getClave(), mensaje, getAlgoritmo());
		
		MensajeExterno mensajeExterno = new MensajeExterno(
				ipPersonal, Integer.toString(puertoPersonal), usernamePersonal,
				ipReceptor, Integer.toString(puertoReceptor), usernameReceptor,
				ENVIAR_MENSAJE, mensajeEncriptado, " ");														//
		
		enviarMensajeExterno(mensajeExterno, servidorPrincipal);
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

	public String getUsernamePersonal() {
		return usernamePersonal;
	}

	public void setUsernamePersonal(String username) {
		this.usernamePersonal = username;
	}
	
	public int getPuertoPersonal() {
		return puertoPersonal;
	}

	public void setPuertoPersonal(int puertoPersonal) {
		this.puertoPersonal = puertoPersonal;
	}
	
	public String getUsernameReceptor() {
		return usernameReceptor;
	}

	public void setUsernameReceptor(String usernameReceptor) {
		this.usernameReceptor = usernameReceptor;
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
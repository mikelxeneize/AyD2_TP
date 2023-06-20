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
	
	private ArrayList<serverData> servidores = new ArrayList<serverData>();
	
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

	// rango de puertos donde ser busca servidor  
	private int puertoDesde = 5000; 
	private int puertoHasta = 5100;
	private int puertoPaso = 1;
	
	private String clave;
	private String algoritmo;

	private long pingEchoTime=0;
	private long  pingEchoAbs=0;
	private long  HeartBeatTime=0;
	
	public Conectividad() {
		this.conectado = false;
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
	
	/**
	 * Intenta conectar a todos los servidor en el rango dado por conectividad.puertoDesde, conectividad.puertoHasta, en saltos de a puertoPaso
	 * Si logra conectar almenos un servidor, conectividad.conectado = true, caso contrario notificara a la interfaz
	 * @throws UnknownHostException
	 * @throws IllegalArgumentException
	 */
	public void conectarServidores() {
		Socket socket;
		serverData servidor;
		
		for (int puerto = this.puertoDesde; puerto <= this.puertoHasta; puerto = puerto + this.puertoPaso) {
			servidor = new serverData("localhost", puerto);
			
			try {
				socket = this.iniciarConexionServidor("localhost", puerto);
				servidor.setSocket(socket);
				
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				servidor.setOut(out);
				
				this.recibirMensaje(socket); //inicia la escuchar del servidor nuevo
				this.servidores.add(servidor);
				this.conectado = true; //se conecto al menos a 1 servidor
				
			} catch (IOException e) {
				//e.printStackTrace();
				//no printea nada aproposito
			}
		}
		if (!this.conectado) {
			this.notificarAccion(new Mensaje("no se pudo conectar con ninguno de los servidores", "error ningun servidor conectado"));
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

	//
	/**
	 * Inicia un hilo para la escucha de mensajes de un determinado socket
	 * @param socket
	 */
	public void recibirMensaje(Socket socket) {
		RecibirMensajeHilo recibirMensaje = new RecibirMensajeHilo(socket, this);
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

	public void inicilizarServidores() {
		
		this.conectarServidores();	
		this.notificarNombreServidores(this.servidores);
		this.pingEchoServidores(this.servidores);		
	}

	
	
	/**
	 * Notifica el nombre de usuario en todos los servidores
	 * @param servidores Conjunto donde se va a notificar
	 */
	private void notificarNombreServidores(ArrayList<serverData> servidores) {
		String puerto;
		
		MensajeExterno mensajeExterno = new MensajeExterno(
				ipPersonal, Integer.toString(puertoPersonal), usernamePersonal,
				"localhost", " ", " ", 
				NOMBRE_USUARIO, " ", " ");														
		
		for (serverData servidor:this.servidores) {
			puerto = Integer.toString(servidor.getPuerto());
			//se cambian partes del mensajeExterno en realcion a cada destinatario
			mensajeExterno.setPuertodestino(puerto); 
			mensajeExterno.setUsernamedestino("Servidor" + puerto);
			
			enviarMensajeExterno(mensajeExterno, servidor);
		}
	}

	/**
	 * inicia el ping echo hilo en todos los servidores
	 * @param servidores Conjunto donde se va a iniciar el echo hilo
	 */
	private void pingEchoServidores(ArrayList<serverData> servidores) {
		PingEchoHilo pingechohilo;
		for (serverData servidor:servidores) {
			pingechohilo= new PingEchoHilo(servidor, this);
			pingechohilo.start();
		}
	}
	
}
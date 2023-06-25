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

	private ArrayList<ServidorData> pendientes = new ArrayList<ServidorData>();
	private ArrayList<ServidorData> servidores = new ArrayList<ServidorData>();
	
	public ArrayList<ServidorData> getServidores() {
		return servidores;
	}

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
	private String estado = "";
	private ServidorData servidorPrincipal = null;

	// rango de puertos donde ser busca servidor  
	private int puertoDesde = 5000; 
	private int puertoHasta = 5020;
	private int puertoPaso = 1;
	
	private String clave;
	private String algoritmo;

	private long pingEchoTime=0;
	private long  pingEchoAbs=0;
	private long  HeartBeatTime=0;
	
	public Conectividad() {
	}

	public List<Observer> getObservers() {
		return observers;
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
	
	public void notificarAccion(Mensaje mensaje) {
		this.setChanged();
		this.notifyObservers(mensaje);
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

	/**
	 * Para conectarse a un puerto e ip, retorna el socket con la conexion correspondiente
	 * @param ip
	 * @param puerto
	 * @return
	 * @throws IOException
	 */
	public Socket conectarServidor(String ip, int puerto) throws IOException {
		Socket socket = new Socket();
		socket.setReuseAddress(true);
		socket.connect(new InetSocketAddress(ip,puerto));
		return socket;
	}
	
	/**
	 * Intenta conectar a todos los servidor en el rango dado por conectividad.puertoDesde, conectividad.puertoHasta, en saltos de a puertoPaso
	 * El primer servidor al que conecte sera el servidor principal
	 * Si logra conectar almenos un servidor, conectividad.servidorPrincipal != null, caso contrario notificara a la interfaz
	 * @throws UnknownHostException
	 * @throws IllegalArgumentException
	 */
	public void conectarServidores() {
		ServidorData servidor;
		
		for (int puerto = this.puertoDesde; puerto <= this.puertoHasta; puerto = puerto + this.puertoPaso) {
			servidor = new ServidorData("localhost", puerto);

			this.pendientes.add(servidor);
			ConexionesHilos conexiones=new ConexionesHilos("localhost", puerto,this,servidor);
			conexiones.start();
				
				
		}
		if (this.servidorPrincipal == null) {
			this.notificarAccion(new Mensaje("no se pudo conectar con ninguno de los servidores", "error ningun servidor conectado"));
		}
	}

	/**
	 * Inicia un hilo para la escucha de mensajes de un determinado socket
	 * @param socket
	 */
	public void recibirMensaje(Socket socket) {
		RecibirMensajeHilo recibirMensaje = new RecibirMensajeHilo(socket, this);
		recibirMensaje.start();
	}
			
	/**
	 * Envia un mensajeExterno por medio un un out asociado a un ServidorData
	 * @param mensajeExterno
	 * @param servidorData
	 */
	public void enviarMensajeExterno(MensajeExterno mensajeExterno, ServidorData servidorData) {
		servidorData.out.println(mensajeExterno.toString());
	}

	/**
	 * Cierra la conversacion
	 * Se envia a todos los servidores, mas que nada para que lo puedan eliminar de su lista
	 */
	public void cerrarConversacion() throws IOException {
		
		MensajeExterno mensajeExterno = new MensajeExterno(
				ipPersonal, Integer.toString(puertoPersonal), usernamePersonal,
				ipReceptor, Integer.toString(puertoReceptor), usernameReceptor,
				CERRAR_CONVERSACION, " ", " ");
		
		enviarMensajeExterno(mensajeExterno, servidorPrincipal);

		System.out.println("21: "+ mensajeExterno.toString());
		this.setIpReceptor(null);
		this.setUsernameReceptor(null);
		this.setPuertoReceptor(0);
	}

	
	/**
	 * Envia un mensaje por servidorPrincipal
	 * Dicho mensaje el receptor deberia recibir en la pantalla de su chat
	 * @param mensaje ,string que se envia al cliente
	 */
	public void enviarMensajeCliente(String mensaje) throws IOException {
		String mensajeEncriptado = Codificacion.encriptar(getClave(), mensaje, getAlgoritmo());
		MensajeExterno mensajeExterno = new MensajeExterno(
				ipPersonal, Integer.toString(puertoPersonal), usernamePersonal,
				ipReceptor, Integer.toString(puertoReceptor), usernameReceptor,
				ENVIAR_MENSAJE, mensajeEncriptado, " ");
		
		for (ServidorData servidor : servidores) {
			enviarMensajeExterno(mensajeExterno, servidor);
		}
		
	}

	/**
	 * Reintenta la conexion con un puerto e ip especificos
	 * Si tiene exito
	 * @param ip
	 * @param puerto
	 * @param mostrarEnInterfaz true--> lo muestra al usuario 
	 * @return
	 */
	public boolean reintento(String ip, int puerto, boolean mostrarEnInterfaz) {
		boolean resultado = false;
		int cantIntentos=2;
		int tiempo=4000;
		int i = 0;
		Mensaje mensaje;
		logReintento("Se esta iniciando el reintento de conexion", "iniciar_reintento", mostrarEnInterfaz);
		while(i<cantIntentos) {
			logReintento("Intento de reconexion "+ i+ " ...", "log_reintento", mostrarEnInterfaz);
			try {
				iniciarConexionServidorNuevo(ip, puerto);
				i = cantIntentos + 2;
				resultado = true;
			} catch (IllegalArgumentException | IOException e) {
				i++; //incrementa en 1 el numero de reintentos
				try {
					Thread.sleep(tiempo);
				} catch (InterruptedException e2) {
					e2.printStackTrace(); //error en el sleep, no deberia suceder
				}
			}
		}
		if (resultado) {
			logReintento("Reconexion exitosa", "finalizar_reintento", mostrarEnInterfaz);
		}else {
			logReintento("No se pudo reconectar", "finalizar_reintento", mostrarEnInterfaz);
			try {
				Thread.sleep(4000); //es para darle tiempo a leer
			} catch (InterruptedException e) {
				e.printStackTrace(); //error en el sleep, no deberia suceder
			}
		}
		return resultado;
	}
	
	/**
	 * imprime por consola y depende de los parametros si tambien por la interfaz de ususario
	 * pensada para ser usada solo por reintento
	 * @param mensaje
	 * @param estado
	 * @param mostrarEnInterfaz si esta en true se muestra al usuario en la conversacion
	 */
	private void logReintento(String mensaje, String estado, boolean mostrarEnInterfaz) {
		Mensaje mensajeInterno;
		System.out.println(mensaje);
		if (mostrarEnInterfaz) {
			mensajeInterno = new Mensaje(mensaje, estado);
			this.notificarAccion(mensajeInterno);	
		}
	}
	
	/**
	 * intenta conectarse a todos los servidores que puede dentro de el rango especificado
	 * luego a todos los servidores que pudo conectar, que estan en un array, les comunica el nombre de usuario
	 * envia la solicitud de iniciar el ping-echo hilo en dichos servidores
	 */
	public void inicilizarServidores() {
		this.conectarServidores();	
		
	}
	
	
	private ServidorData getRegistradoByIp(String ipObj,int puertoObj) {
		int puerto;
		String ip;
		for (ServidorData servidor : servidores) {
			puerto = servidor.getPuerto();
			ip = servidor.getIp();
			if (puerto == puertoObj) {
				return servidor;
			}
		}
		return null;
	}
	/**
	 * Notifica el nombre de usuario en todos los servidores
	 * @param socket Conjunto donde se va a notificar
	 */
	public void notificarNombreServidores(Socket socket) {
		
		MensajeExterno mensajeExterno = new MensajeExterno(
				ipPersonal, Integer.toString(puertoPersonal), usernamePersonal,
				"localhost", " ", " ", 
				NOMBRE_USUARIO, " ", " ");														
		
			ServidorData servidor=getRegistradoByIp(socket.getInetAddress().toString(),socket.getPort());
			
			enviarMensajeExterno(mensajeExterno, servidor);
	}

	/**
	 * inicia el ping echo hilo en todos los servidores
	 * @param socket Conjunto donde se va a iniciar el echo hilo
	 */
	public void pingEchoServidores() {
		PingEchoHilo pingechohilo;
		
		pingechohilo= new PingEchoHilo(this.servidorPrincipal, this);
		pingechohilo.start();
		
	}
	
	/**
	 * Si queda algun servidor conectado, elije el primero de conectividad.servidores como principal
	 * @return
	 * true si tuvo exito, de lo contrario false
	 */
	public synchronized boolean servidorPrincipalSwap() {
		for (ServidorData servidor : servidores) {
			if(!servidor.getSocket().isClosed()) {
				this.servidorPrincipal=servidor;
				return true;
			}
		}
		return false;
	}
	
	
	public void iniciarConexionServidorNuevo(MensajeExterno mensajeExterno) throws IOException {
		
		String [] partes= mensajeExterno.getCuerpo().split("=");
		String ip=partes[0];
		int puerto=Integer.parseInt(partes[1]);
		Socket socket;
		
			ServidorData servidor = new ServidorData("localhost", puerto);
			socket = this.conectarServidor("localhost", puerto);
			servidor.setSocket(socket);
			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			servidor.setOut(out);
			
			this.recibirMensaje(socket); //inicia la escuchar del servidor nuevo
			this.pendientes.add(servidor);
			
			MensajeExterno confirmacion =new MensajeExterno(socket.getInetAddress().toString(),Integer.toString(this.puertoPersonal),INDEFINIDO,socket.getInetAddress().toString(),
					Integer.toString(socket.getPort()), INDEFINIDO , CONFIRMACION_CLIENTE,INDEFINIDO,INDEFINIDO); 
			enviarMensajeExterno(confirmacion,servidor);
	}
	
	/**
	 * Realiza todos los pasos requeridos del lado del cliente para añadirlo a la lista de pendientes
	 * Tambien inicia el proceso de CONFIRMACION_CLIENTE para añadirlo al array de servidores 
	 * @param ip
	 * @param puerto 
	 * @throws IOException No se pudo realizar con exito
	 */
	public void iniciarConexionServidorNuevo(String ip, int puerto) throws IOException {
		Socket socket;
		
			ServidorData servidor = new ServidorData("localhost", puerto);
			socket = this.conectarServidor("localhost", puerto);
			servidor.setSocket(socket);
			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			servidor.setOut(out);
			
			this.recibirMensaje(socket); //inicia la escuchar del servidor nuevo
			this.pendientes.add(servidor);
			
			MensajeExterno confirmacion =new MensajeExterno(socket.getInetAddress().toString(),Integer.toString(this.puertoPersonal),INDEFINIDO,socket.getInetAddress().toString(),
					Integer.toString(socket.getPort()), INDEFINIDO , CONFIRMACION_CLIENTE,INDEFINIDO,INDEFINIDO); 
			enviarMensajeExterno(confirmacion,servidor);
	}
	
	
	public synchronized void eliminarPendientes(Socket socket) {
		ServidorData aux=null;
		for (ServidorData servidor : pendientes) {
			if(servidor.getSocket()==socket) {
				aux=servidor;
			}
				
		}
		this.servidores.add(aux);
		this.pendientes.remove(aux);
	}
	
	public synchronized void removerServidor(ServidorData servidorData) {
		this.servidores.remove(servidorData);
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
	
	public ServidorData getServidorPrincipal() {
		return servidorPrincipal;
	}

	public void setServidorPrincipal(ServidorData servidorPrincipal) {
		this.servidorPrincipal = servidorPrincipal;
	}

	

}
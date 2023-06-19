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

public class Servidor implements IComandos, IEstados {

	private ArrayList<Cliente> listaConectados = new ArrayList<Cliente>();
	private ServerSocket serverSocket;
	private static Servidor instance;

	private int puertoServidor;
	private String ipServidor = "localhost";
	private String UsernameServidor = "";


	public String getUsernameServidor() {
		return UsernameServidor;
	}

	public void setUsernameServidor(String usernameServidor) {
		UsernameServidor = usernameServidor;
	}

	public int getPuertoServidor() {
		return puertoServidor;
	}

	public void setPuertoServidor(int puertoServidor) {
		this.puertoServidor = puertoServidor;
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
		// iniciarHeartBeat();
		iniciarEscucha();

	}

	/*
	 * private void iniciarHeartBeat() { HeartBeatHilo hilo = new
	 * HeartBeatHilo(this, false); hilo.start();
	 * 
	 * }
	 */

	public void iniciarEscucha() throws IOException {
		Socket socket;
		Cliente cliente;
		ServidorRecibirMensajeHilo recibirMensajeHilo;
		int puerto = 5000;
		int i = 0;
		boolean encontrado = false;
		while (i < 100 && encontrado == false) { // conexion al puerto
			try {
				serverSocket = new ServerSocket(puerto);
				encontrado = true;
			} catch (IOException e) {
				i++;
				puerto+=i;
			}
		}
		System.out.println(puerto);
		if(encontrado) {
			this.setPuertoServidor(puerto);
			this.setUsernameServidor("SERVIDOR "+ puerto );
		}
		while (true) { //recepcion de nuevos usuarios a escuchar
			socket = serverSocket.accept();
			cliente = new Cliente(socket.getPort(), socket.getInetAddress().toString(), socket);
			if (socket.getPort() < 5000 || socket.getPort() > 5999) {
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

	private Cliente getRegistradoByIp(String ipObj,int puertoObj) {
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

	public void enviarMensajeACliente(MensajeExterno mensaje) throws IOException {
		String ip=mensaje.getIpdestino();
		int puerto=Integer.parseInt(mensaje.getPuertodestino());
		for (Cliente cliente : listaConectados) {
			if (cliente.getIp().equals(ip) && cliente.getPuerto() == puerto) {
				PrintWriter out = new PrintWriter(cliente.getSocket().getOutputStream(), true);
				out.println(mensaje.toString());
				// System.out.println("17: "+mensaje.toString());
			}
		}
	}

	// no solo envia el mensaje a todos, sino que setea en el mensaje la ip y puerto del receptor
	public void enviarMensajeAClienteTodos(MensajeExterno mensaje) throws IOException {
		for (Cliente cliente : listaConectados) {
			mensaje.setIpdestino(cliente.getIp());
			mensaje.setPuertodestino(Integer.toString(cliente.getPuerto()));
			PrintWriter out = new PrintWriter(cliente.getSocket().getOutputStream(), true);
			out.println(mensaje.toString());
			// System.out.println("17: "+mensaje.toString());
		}
	}

	public void iniciarConexionAReceptor(MensajeExterno mensaje) throws UnknownHostException, IOException, InterruptedException {
		
		Cliente clienteEmisor = getRegistradoByIp(mensaje.getIporigen(),Integer.parseInt(mensaje.getPuertoorigen()));
		Cliente clienteReceptor = getRegistradoByIp(mensaje.getIpdestino(),Integer.parseInt(mensaje.getPuertodestino()));
		
		// este lo recibe el emisor
		if (clienteReceptor != null && clienteReceptor.getEstado().equals(DISPONIBLE)) {// cliente registrado y disponible para																// conectarse
			
			clienteEmisor.setIpReceptor(clienteReceptor.getIp());
			clienteEmisor.setPuertoReceptor(clienteReceptor.getPuerto());
			clienteEmisor.setEstado(OCUPADO);
			
			clienteReceptor.setIpReceptor(clienteEmisor.getIp());
			clienteReceptor.setPuertoReceptor(clienteEmisor.getPuerto());
			clienteReceptor.setEstado(OCUPADO);
			
			this.MandarLista1();
			
			MensajeExterno mensajeAReceptor = new MensajeExterno(clienteEmisor.getIp(),
					Integer.toString(clienteEmisor.getPuerto()),clienteEmisor.getUsername(),
					clienteReceptor.getIpReceptor(), Integer.toString(clienteReceptor.getPuerto()), clienteReceptor.getUsername(),
					CONEXION_ESTABLECIDA, " ", " ");
			this.enviarMensajeACliente(mensajeAReceptor);
			
			MensajeExterno mensajeConfirmacion = new MensajeExterno(clienteReceptor.getIp(),
					Integer.toString(clienteReceptor.getPuerto()),clienteReceptor.getUsername(),
					clienteEmisor.getIp(), Integer.toString(clienteEmisor.getPuerto()),clienteEmisor.getUsername(),
					CONEXION_ESTABLECIDA," "," ");
			this.enviarMensajeACliente(mensajeConfirmacion);
			
			//ServidorRecibirMensajeHilo recibirMensajeHiloConversacion = new ServidorRecibirMensajeHilo(clienteReceptor, this);
			//recibirMensajeHiloConversacion.start();
			// cliente aun no registrado, devolver excepcion
		} else {// rechaza la conexion y le avisa al cliente 1 que no se pudo conectar
			//mensajeConfirmacion.setComando(CONEXION_RECHAZADA);
			MensajeExterno mensajeConfirmacion = new MensajeExterno(clienteReceptor.getIp(),
					Integer.toString(clienteReceptor.getPuerto()),clienteReceptor.getUsername(),
					clienteEmisor.getIp(), Integer.toString(clienteEmisor.getPuerto()),clienteEmisor.getUsername(),
					CONEXION_RECHAZADA, " ", " ");
			this.enviarMensajeACliente(mensajeConfirmacion);
		}
	}

	public void cortarConversacion(MensajeExterno mensaje) throws IOException {
		Cliente clienteEmisor= this.getRegistradoByIp(mensaje.getIporigen(),Integer.parseInt(mensaje.getPuertoorigen()));
		Cliente clienteReceptor=this.getRegistradoByIp(mensaje.getIpdestino(),Integer.parseInt(mensaje.getPuertodestino()));
		if (clienteReceptor != null) {// cliente registrado y disponible para conectarse
			
			clienteEmisor.setIpReceptor(null);
			clienteEmisor.setPuertoReceptor(NULL);
			clienteEmisor.setEstado(DISPONIBLE);
			
			clienteReceptor.setIpReceptor(null);
			clienteReceptor.setPuertoReceptor(NULL);
			clienteReceptor.setEstado(DISPONIBLE);
			
			
			this.enviarMensajeACliente(mensaje);
			System.out.println(mensaje.toString());

			this.MandarLista1();
			
		}

	}

	public ArrayList<Cliente> getListaConectados() {
		return listaConectados;
	}
	// 
	public void MandarLista1() {
		PrintWriter out = null;
		Cliente cliente;
		System.out.println("");
		String lista = "";
		// Armo la lista en string primero
		MensajeExterno mensaje= new MensajeExterno(this.getIpServidor(),Integer.toString(this.getPuertoServidor())," "," ", " "," ",ACTUALIZAR_LISTA," "," "); 
		for (Cliente cliente1 : this.getListaConectados()) {  //limpieza de los desconectados
			if (cliente1.getUsername() == null) {
				this.getListaConectados().remove(cliente1);
			}
		}
		for (int j = 0; j < this.getListaConectados().size(); j++) {
			cliente = this.getListaConectados().get(j);
			lista += this.getListaConectados().get(j).actualizacion();
		}
		mensaje.setCuerpo(lista);
		try { //este try and catch me lo pide el java
			this.enviarMensajeAClienteTodos(mensaje);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// determina si hay servidor principal y decide en que puerto va a escuchar
	/*
	 * private void redundanciaPasiva() { this.isPrincipal = false;
	 * this.puertoServidor = PUERTO_1; if (conectarServidor("localhost", PUERTO_1))
	 * { // hay un servidor principal en puerto_1 this.puertoServidor = PUERTO_2; }
	 * else if (conectarServidor("localhost", PUERTO_2)) { // hay servidor principal
	 * en puerto_2 // seguis escuchando en el puerto_1 } else { // no hay servidor
	 * principal, entonces este lo sera
	 * System.out.println("44: No hay server principal"); this.isPrincipal = true; }
	 * }
	 * 
	 * public boolean conectarServidor(String ip, int puerto) { boolean conecto =
	 * true; System.out.println("43: intentando conexion a servidor en puerto " +
	 * puerto); try { this.servidorVecino = new Socket(ip, puerto); } catch
	 * (Exception e) { conecto = false; } return conecto; }
	 * 
	 * private void notificarServidor() { PrintWriter out; try { out = new
	 * PrintWriter(servidorVecino.getOutputStream(), true);
	 * out.println(this.ipServidor + ":" + this.puertoServidor + ":" +
	 * "%nuevoServidorPasivo%" + ":" + "Servidor_Servidor"); } catch (IOException e)
	 * { e.printStackTrace(); } System.out.println(this.ipServidor + ":" +
	 * this.puertoServidor + ":" + "%nuevoServidorPasivo%" + ":" +
	 * "Servidor_Servidor"); }
	 * 
	 * void notificarUsuarios(String ip, int puerto) { for (Cliente cliente :
	 * this.listaConectados) { try { PrintWriter out = new
	 * PrintWriter(cliente.getSocket().getOutputStream(), true); out.println(ip +
	 * ":" + puerto + ":" + "%nuevoServidorPasivo%" + ":" + "Servidor_Cliente"); }
	 * catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } System.out.println(cliente.getIp() + ":" +
	 * Integer.toString(cliente.getPuerto()) + ":" + "%nuevoServidorPasivo%" + ":" +
	 * "Servidor_Cliente"); } }
	 * 
	 * public boolean isPrincipal() { return isPrincipal; }
	 */

}

package Servidor.Servidor;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


import Servidor.Bean.Cliente;
import Servidor.Bean.MensajeExterno;
import Servidor.Bean.Monitor;
import Servidor.Bean.ServerData;
import Servidor.Bean.SocketBean;
import Servidor.Util.IComandos;
import Servidor.Util.IEstados;

public class Servidor implements IComandos, IEstados {
	
	private ArrayList<SocketBean> listaPendientes = new ArrayList<SocketBean>();
	private ArrayList<Cliente> listaClientes = new ArrayList<Cliente>();
	private ArrayList<ServerData> listaServidores = new ArrayList<ServerData>();
	private ArrayList<Monitor> listaMonitores = new ArrayList<Monitor>();
	
	
	private ServerSocket serverSocket;
	private static Servidor instance;

	private int puertoServidor;
	private String ipServidor = "/127.0.0.1";
	private String UsernameServidor = "";
	private int CANTSERV = 20;

	private boolean isSincronizado=false;
	 int clientesConseguidos=0;
	public static Servidor getInstance() throws IOException {
		if (Servidor.instance == null) {
			Servidor.instance = new Servidor();
		}
		return instance;
	}
	public Servidor() throws IOException {
		buscarServerSocket();
		conseguirClientes();
		iniciarEscucha();
	}
	
	private void buscarServerSocket() {
		int puerto = 5000;
		int i = 0;
		boolean encontrado = false;
		while (i < CANTSERV && encontrado == false) { // conexion al puerto
			try {
				serverSocket = new ServerSocket(puerto);
				encontrado = true;
			} catch (IOException e) {
				i++;
				puerto+=1;
			}
		}
		System.out.println(puerto);
		if(encontrado) {
			this.setPuertoServidor(puerto);
			this.setUsernameServidor("SERVIDOR"+ puerto );
		}
	}
	private void conseguirClientes() {
		Socket socket;
		int puerto = 5000;
		int i = 0;
		while (i < CANTSERV ) { // conexion al puerto
			
			ServerData server= new ServerData(this.getIpServidor(),puerto);
			listaPendientes.add(server);
			ConexionesHilos conexiones=new ConexionesHilos("localhost", puerto,this,server);
			conexiones.start();
			
			i++;
			puerto += 1;
		}
		System.out.println(puerto);
	}

	
	public void iniciarEscucha() throws IOException {
		Socket socket;
		SocketBean socketBean;
		ServidorRecibirMensajeHilo recibirMensajeHilo;
		
		

		iniciarConexionMonitor();
		while (true) { //recepcion de nuevos usuarios a escuchar
			socket = serverSocket.accept();
			socketBean = new SocketBean(socket.getPort(), socket.getInetAddress().toString(), socket);
			listaPendientes.add(socketBean);
			recibirMensajeHilo = new ServidorRecibirMensajeHilo(socketBean, this);
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
		for (Cliente cliente : listaClientes) {
			puerto = cliente.getPuerto();
			ip = cliente.getIp();
			if (ip.equals(ipObj) && puerto == puertoObj) {
				return cliente;
			}
		}
		return null;
	}
	
	private ServerData getRegistradoByIpServidor(String ipObj,int puertoObj) {
		int puerto;
		String ip;
		for (ServerData serverData : listaServidores) {
			puerto = serverData.getPuerto();
			ip = serverData.getIp();
			if (ip.equals(ipObj) && puerto == puertoObj) {
				return serverData;
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
	
	public void crearLista(String clientesCrudos) {
		if(clientesCrudos!=null && !clientesCrudos.equals("")) {
			int largo = clientesCrudos.split(";").length;
			String[] clientecrudo = clientesCrudos.split(";");
			int i=0;
			while (i<largo) {
				String[] clientelimpio = clientecrudo[i].split("=");
				Cliente cliente = new Cliente(clientelimpio[0],Integer.parseInt(clientelimpio[1]),clientelimpio[2],clientelimpio[3]);
				this.getListaClientes().add(cliente);
				i++;
			}
		}
	}

	
	public void enviarMensajeACliente(MensajeExterno mensaje) throws IOException {
		String ip=mensaje.getIpdestino();
		int puerto=Integer.parseInt(mensaje.getPuertodestino());
		for (Cliente cliente : listaClientes) {
			if ((cliente.getIp().equals(ip) && cliente.getPuerto() == puerto) || cliente.getSocket().getPort()== puerto) {
				PrintWriter out = new PrintWriter(cliente.getSocket().getOutputStream(), true);
				out.println(mensaje.toString());
				// System.out.println("17: "+mensaje.toString());
			}
		}
	}
	public void enviarMensajeAServidor(MensajeExterno mensaje) throws IOException {
		String ip=mensaje.getIpdestino();
		int puerto=Integer.parseInt(mensaje.getPuertodestino());
		for (ServerData cliente : listaServidores) {
			if ( cliente.getPuerto() == puerto) {
				PrintWriter out = new PrintWriter(cliente.getSocket().getOutputStream(), true);
				out.println(mensaje.toString());
				// System.out.println("17: "+mensaje.toString());
			}
		}
	}
	
	public void enviarMensajeAPendientes(MensajeExterno mensaje) throws IOException {
		String ip=mensaje.getIpdestino();
		int puerto=Integer.parseInt(mensaje.getPuertodestino());
		for (SocketBean cliente : listaPendientes) {
			if ( cliente.getPuerto() == puerto) {
				PrintWriter out = new PrintWriter(cliente.getSocket().getOutputStream(), true);
				out.println(mensaje.toString());
				// System.out.println("17: "+mensaje.toString());
			}
		}
	}
	
	public void enviarMensajeAMonitor(MensajeExterno mensaje) throws IOException {
		String ip=mensaje.getIpdestino();
		int puerto=Integer.parseInt(mensaje.getPuertodestino());
		for (Monitor cliente : listaMonitores) {
			if ( cliente.getPuerto() == puerto) {
				PrintWriter out = new PrintWriter(cliente.getSocket().getOutputStream(), true);
				out.println(mensaje.toString());
				// System.out.println("17: "+mensaje.toString());
			}
		}
	}
	// no solo envia el mensaje a todos, sino que setea en el mensaje la ip y puerto del receptor
	public void enviarMensajeAClienteTodos(MensajeExterno mensaje) throws IOException {
		for (Cliente cliente : listaClientes) {
			mensaje.setIpdestino(cliente.getIp());
			mensaje.setPuertodestino(Integer.toString(cliente.getPuerto()));
			PrintWriter out = new PrintWriter(cliente.getSocket().getOutputStream(), true);
			out.println(mensaje.toString());
			// System.out.println("17: "+mensaje.toString());
		}
	}
	
	private void enviarMensajeAMonitores(MensajeExterno mensaje) throws IOException {
		// TODO Auto-generated method stub
		for (Monitor monitor : listaMonitores) {
			mensaje.setIpdestino(monitor.getIp());
			mensaje.setPuertodestino(Integer.toString(monitor.getPuerto()));
			PrintWriter out = new PrintWriter(monitor.getSocket().getOutputStream(), true);
			out.println(mensaje.toString());
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
			MensajeExterno mensajeConfirmacion = new MensajeExterno(this.ipServidor,
					Integer.toString(this.puertoServidor),this.UsernameServidor,
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
	// 
	public void MandarLista1() {
		PrintWriter out = null;
		Cliente cliente;
		System.out.println("");
		String lista = "";
		// Armo la lista en string primero
		MensajeExterno mensaje= new MensajeExterno(this.getIpServidor(),Integer.toString(this.getPuertoServidor())," "," ", " "," ",ACTUALIZAR_LISTA," "," "); 
		for (Cliente cliente1 : this.getListaClientes()) {  //limpieza de los desconectados
			if (cliente1.getUsername() == null) {
				this.getListaClientes().remove(cliente1);
			}
		}
		for (int j = 0; j < this.getListaClientes().size(); j++) {
			cliente = this.getListaClientes().get(j);
			lista += this.getListaClientes().get(j).actualizacion();
		}
		mensaje.setCuerpo(lista);
		try { //este try and catch me lo pide el java
			this.enviarMensajeAClienteTodos(mensaje);
			this.enviarMensajeAMonitores(mensaje);
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

	public void MandarLista2(MensajeExterno mensajerecibido) {
		System.out.println("");
		String lista = "";
		// Armo la lista en string primero
		MensajeExterno mensaje = new MensajeExterno(this.getIpServidor(), Integer.toString(this.getPuertoServidor()),
				" ", mensajerecibido.getIporigen(), mensajerecibido.getPuertoorigen(), " ", LISTA_COMPLETA, " ", " ");
		
		for (int j = 0; j < this.getListaClientes().size(); j++) {
			lista += this.getListaClientes().get(j).actualizacion();
		}
		mensaje.setCuerpo(lista);
		try { // este try and catch me lo pide el java
			this.enviarMensajeAServidor(mensaje);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
	public ArrayList<SocketBean> getListaPendientes() {
		return listaPendientes;
	}
	public ArrayList<ServerData> getListaServidores() {
		return listaServidores;
	}
	public ArrayList<Monitor> getListaMonitores() {
		return listaMonitores;
	}

	public ArrayList<Cliente> getListaClientes() {
		return listaClientes;
	}
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	
	public synchronized void  agregarALista(MensajeExterno mensaje, String tipo) throws IOException {
		String ip=mensaje.getIporigen(); 
		int puerto=Integer.parseInt(mensaje.getPuertoorigen());
		for (SocketBean socketBean : listaPendientes) {
			if ( socketBean.getPuerto() == puerto) {
				if(tipo.equals("CLIENTE")) {
					Cliente cliente=this.getRegistradoByIp(ip, puerto);
					if(cliente==null){//es una conexion de las nuevas
						cliente=new Cliente(Integer.parseInt(mensaje.getPuertoorigen()),mensaje.getIporigen(),socketBean.getSocket());
						this.listaClientes.add(cliente);
					}
					else {
						cliente.setSocket(socketBean.getSocket());
					}
					ServidorRecibirMensajeHiloCliente recibirMensajeHiloCliente = new ServidorRecibirMensajeHiloCliente(cliente, this);
					recibirMensajeHiloCliente.start();
				}
				else if(tipo.equals("SERVIDOR")){
					ServerData servidor=new ServerData(socketBean.getPuerto(),socketBean.getIp(),socketBean.getSocket());
					listaServidores.add(servidor);

					ServidorRecibirMensajeHiloServidor recibirMensajeHiloServidor = new ServidorRecibirMensajeHiloServidor(servidor, this,true);
					recibirMensajeHiloServidor.start();
				}
				
				else if(tipo.equals("MONITOR")) {
					Monitor monitor=new Monitor(socketBean.getPuerto(),socketBean.getIp(),socketBean.getSocket());
					listaMonitores.add(monitor);

					ServidorRecibirMensajeHiloMonitor recibirMensajeHiloMonitor = new ServidorRecibirMensajeHiloMonitor(monitor, this);
					recibirMensajeHiloMonitor.start();
				}
					
			}
		}
	}
	public void avisarClientesNuevoServidor(MensajeExterno mensajerecibido) throws IOException {
		String infoServidorNuevo=mensajerecibido.getCuerpo();
		MensajeExterno mensajeAviso= new MensajeExterno(this.ipServidor,Integer.toString(this.puertoServidor),this.UsernameServidor,INDEFINIDO,INDEFINIDO,INDEFINIDO,AVISAR_CLIENTES_DE_NUEVO_SERVIDOR,infoServidorNuevo,INDEFINIDO);
		this.enviarMensajeAClienteTodos(mensajeAviso);
	}
	
	public String miInformacionEnString() throws InterruptedException {
		Thread.sleep(200);
		return this.ipServidor+"="+this.puertoServidor;
	}
	public void iniciarConexionMonitor()  {
		Socket socket;
		ServerData monitor = new ServerData("localhost", 6000);
		
		try {
			socket = new Socket("localhost",6000);listaPendientes.add(monitor);
			monitor.setSocket(socket);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			monitor.setOut(out);

			ServidorRecibirMensajeHilo recibirMensaje = new ServidorRecibirMensajeHilo(monitor, this);
			recibirMensaje.start();
			
			
			
		} catch (UnknownHostException e) {
			
		} catch (IOException e) {
			
		}
		
		
	}
	public synchronized boolean isSincronizado() {
		return isSincronizado;
	}
	public  synchronized void setSincronizado(boolean isSincronizado) {
		this.isSincronizado = isSincronizado;
	}
	public synchronized void incrementar() {
		this.clientesConseguidos++;
		System.out.println(clientesConseguidos);
	}
	

}
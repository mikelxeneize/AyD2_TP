package monitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import bean.Cliente;
import bean.MensajeExterno;
import bean.Notificacion;
import bean.ServerData;
import util.IComandos;
import util.IEstados;
import util.ILogger;
import util.INotificacion;





@SuppressWarnings("deprecation")
public class Monitor extends Observable implements ILogger, INotificacion, IComandos, IEstados{
	
	private ArrayList<ServerData> listaPendientes = new ArrayList<ServerData>();
	private ArrayList<ServerData> listaServidores = new ArrayList<ServerData>();
	private ArrayList<Cliente> listaClientes = new ArrayList<Cliente>();
	 
	private static Monitor instance;
	private String IP = "localhost";
	private String PUERTO = "6000";
	private String USERNAME = "Monitor";
	private List<Observer> observers = new ArrayList<Observer>();
	
	
	//-------------------Singleton-----------------------//
	public static Monitor getInstance() throws IOException {
		if (Monitor.instance == null) {
			Monitor.instance = new Monitor();
		}
		return instance;
	}

	//-----------------Constructor-----------------------//
	
	public Monitor()  {
		//Algo que sea util
	}
	
	//------Eventos que deben ejecutarse al iniciar -----//
	
	public  void inicializarMonitor() {
		buscarServidores();
		//Algo que necesite ejecutar cuando se crea el monitor
	}

	//------Busca servidores en un rango de 100 puertos -----//

	private void buscarServidores() {
		Socket socket;
		String ip = "localhost";
		int puertoInicial = 5000;
		int puertoAux=0;
		for (int i = 0 ; i<100 ; i++ ) { 
			try {
				puertoAux=puertoInicial+i;

				socket = new Socket();
				socket.connect(new InetSocketAddress(ip,puertoAux));
				//Si continua es porque encontro un puerto. Solo queda verificar si es realmente un servidor
				listaPendientes.add(new ServerData(socket,ip,Integer.toString(puertoAux),PENDIENTE));
				

				RecibirMensajeHilo recibirMensaje = new RecibirMensajeHilo(socket, this);
				recibirMensaje.start();
				
				
				enviarMensajeAServidor(new MensajeExterno(socket.getInetAddress().toString(),Integer.toString(socket.getLocalPort()),INDEFINIDO,socket.getInetAddress().toString(),
						Integer.toString(socket.getPort()), INDEFINIDO , CONFIRMACION_MONITOR,INDEFINIDO,INDEFINIDO),socket);
				
			} catch (UnknownHostException e) {
				System.out.println(WARN + SERVIDOR_NO_DETECTADO + puertoAux);
			} catch (IOException e) {
				System.out.println(WARN + SERVIDOR_NO_DETECTADO + puertoAux);
			}
		}
	}

	//------Metodo para enviar mensajes a clientes -----//
	public void enviarMensajeAServidor(MensajeExterno mensaje,Socket socket) throws IOException {
		
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		out.println(mensaje.toString());
			
	}
	
	//------Metodo para poder llegar al front Todos llaman al notificaControlador  -----//
	
	public void logger(String mensaje) {
		notificaControlador( new Notificacion(mensaje,TIPO1) );
	}
	public void notificaActualizacionLista(ArrayList<Cliente> listaClientes) {
		notificaControlador( new Notificacion(listaClientes,TIPO2) );
	}
	public void notificaAltaFilaServidores(ServerData servidor) {
		notificaControlador( new Notificacion(servidor,TIPO3) );
	}
	public void notificaBajaFilaServidores(ServerData servidor) {
		notificaControlador( new Notificacion(servidor,TIPO4) );
	}
	public void notificaModificacionFilaServidores(ServerData servidor) {
		notificaControlador( new Notificacion(servidor,TIPO5) );
	}
	


	//------Metodo del patron Observer-Observable -----//
	
	public void notificaControlador(Notificacion notificacion) {
		this.setChanged();
		this.notifyObservers(notificacion);
	}
	

	//------Metodo para simplificar la creacion de un Mensaje externo -----//
	public MensajeExterno crearMensajeExterno(String ipdestino,
			String puertodestino, String usernamedestino, String comando, String cuerpo, String auxiliar) {
		return new MensajeExterno(IP,PUERTO,USERNAME,  ipdestino,
				 puertodestino,  usernamedestino,  comando,  cuerpo,  auxiliar);
	}
	//------Metodo que contiene la logica de los comandos de recbirmensajehilo-----//
	public void receptorDeMensajes(MensajeExterno mensajeExterno) {
		
		String comando=mensajeExterno.getComando();
		
		if(comando.equals(CONFIRMACION_MONITOR_RESPUESTA))
			agregarServidor(mensajeExterno);
		else  if(comando.equals(ACTUALIZAR_LISTA))
			actualizarListaClientes(mensajeExterno);
		else  if(comando.equals(RESPUESTA_PING_ECHO))
			calcularPing(mensajeExterno);
		
			
	}
	//------Metodo que actualiza la lista de clientes, y la envia al front-----//
	private void actualizarListaClientes(MensajeExterno mensajeExterno) {
		ServerData servidorPendiente= BuscarServidorEnActivos(mensajeExterno.getIporigen(),mensajeExterno.getPuertoorigen());
	
		if(listaServidores.get(0).getUsername().equals(servidorPendiente.getUsername() )){
			listaClientes.clear();
			String cliente= mensajeExterno.getCuerpo();
			
			String[] partes = cliente.split(";");
			
			for(int i=0; i<partes.length; i++) {
				String[] subpartes = partes[i].split("=");
				Cliente clienteNuevo = new Cliente(subpartes[0], subpartes[1], subpartes[2], subpartes[3]);
				listaClientes.add(clienteNuevo);
				
			}
			this.notificaActualizacionLista(listaClientes);
		}
	}

	//------Metodo que calcula el ping como la resta del ping absoluto-----//
	
	private void calcularPing(MensajeExterno mensajeExterno) {
		
		ServerData servidorPendiente= BuscarServidorEnActivos(mensajeExterno.getIporigen(),mensajeExterno.getPuertoorigen());
		Long time=System.currentTimeMillis() - servidorPendiente.getPingAbs();
		
		servidorPendiente.setPing(time);
		
	}

	//------Metodo que agrega a un servidor al monitor luego de la confirmacion-----//
	private void agregarServidor(MensajeExterno mensajeExterno) {
		ServerData servidorPendiente= BuscarServidorEnPendientes(mensajeExterno.getIporigen(),mensajeExterno.getPuertoorigen());
		servidorPendiente.setEstado(ACTIVO);
		servidorPendiente.setUsername(mensajeExterno.getUsernameorigen());
		listaServidores.add(servidorPendiente);
		listaPendientes.remove(servidorPendiente);
		
		PingEchoHilo pingechohilo= new PingEchoHilo(servidorPendiente);
		pingechohilo.start();
		notificaAltaFilaServidores(servidorPendiente);
		logger(INFO + SERVIDOR_DETECTADO + mensajeExterno.getPuertoorigen());
		
	}
	
	//------Metodo que agrega a un servidor del monitor -----//
	public void eliminarServidor(Socket socket) {
		int hayActivos=0;
		ServerData servidorPendiente= BuscarServidorEnActivos(socket.getInetAddress().toString(),Integer.toString(socket.getPort()));
		servidorPendiente.setEstado(CAIDO);
		notificaModificacionFilaServidores(servidorPendiente);
		logger(FATAL + SERVIDOR_CAIDO1 + socket.getPort() + SERVIDOR_CAIDO2 );
		for (ServerData servidor : listaServidores){
			if (servidor.getEstado().equals(ACTIVO) ) {
				hayActivos++;
			}
		}
		if(hayActivos==0)
			logger(CRITICAL + TODOS_CAIDOS );
		else if(hayActivos==1)
			logger(FATAL + SIN_SERVIDORES);
			
			
	}
	
	//------Metodo que busca un servidor en lista de pendientes por ip y puerto-----//
	private ServerData BuscarServidorEnPendientes(String ip, String puerto) {
		ServerData respuesta = null;
		for (ServerData servidor : listaPendientes){
			if (/*servidor.getIp().equals(ip) &&*/  servidor.getPuerto().equals(puerto) ) {
				return servidor;
			}
		}
		return respuesta;
	}

	//------Metodo que busca un servidor en lista de activos por ip y puerto-----//
	private ServerData BuscarServidorEnActivos(String ip, String puerto) {
		ServerData respuesta = null;
		for (ServerData servidor : listaServidores){
			if (/*servidor.getIp().equals(ip) &&*/  servidor.getPuerto().equals(puerto) ) {
				return servidor;
			}
		}
		return respuesta;
	}
	
	public ArrayList<ServerData> getListaServidores() {
		return listaServidores;
	}



	//------Metodo que envia el ping echo-----//

	public void enviarPingEcho(ServerData servidor) throws IOException {
		Socket socket= servidor.getSocket();
		
		enviarMensajeAServidor(crearMensajeExterno(socket.getInetAddress().toString(),
					Integer.toString(socket.getPort()), this.USERNAME , PING_ECHO ,INDEFINIDO,INDEFINIDO),socket);

	}

}

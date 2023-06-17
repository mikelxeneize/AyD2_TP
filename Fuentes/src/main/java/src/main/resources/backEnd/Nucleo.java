package src.main.resources.backEnd;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import src.main.resources.conectividad.Conectividad;
import src.main.resources.conectividad.Mensaje;
import src.main.resources.controlador.ControladorConversacion;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Nucleo extends Observable implements  Observer {
	private Conectividad conexion;
	private static Nucleo instance;
	private String ip;
	private int port;
	private String filePath = "./config.json";
	private String accionObserver;
	public static final String RECIBIR_MENSAJE = "mensaje recibido";
	public static final String INICIAR_CONEXION= "conexion establecida";
	public static final String CERRAR_CONEXION = "conexion finalizada";
	public static final String ACTUALIZAR = "Actualizar";
	private static final String PEDIR_SERVIDOR_SECUNDARIO = "informacionServerSecundario";
	private List<Observer> observers = new ArrayList<>();
    private String username;
    private ArrayList<Cliente> conectados=new ArrayList<Cliente>();
	

	public static Nucleo getInstance() {
		if (Nucleo.instance == null) {
			Nucleo.instance = new Nucleo();
		}
		return instance;
	}
	
	public Nucleo() {
		this.conexion= new Conectividad();
		this.conexion.addObserver(this);
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void cargarConfiguracion() throws IOException, ParseException {
		Long longPort = 0L;
		JSONParser jsonParser = new JSONParser();
		JSONObject configuracion = null;
		FileReader reader = new FileReader(this.filePath);
		Object obj = jsonParser.parse(reader);
		configuracion = (JSONObject) obj;
		
		//forma vieja carga desde la configuracion		
		//this.ip= (String) configuracion.get("ip"); 

		//forma entre otras pcs
		String clave=(String) configuracion.get("clave");
		String algot=(String) configuracion.get("algoritmo");
		this.conexion.setClave(clave);
		this.conexion.setAlgoritmo(algot);
	}
	public void setConfiguracion(String ip, int port) {
		this.setPort(port);
		this.setIp(ip);
		this.conexion.setIppersonal(ip);
		this.conexion.setPuertopersonal(port);
	}
	
	public void setConfiguracion(String ip, int port, String username) {
		this.setPort(port);
		this.setIp(ip);
		this.setUsername(username);
		this.conexion.setIppersonal(ip);
		this.conexion.setPuertopersonal(port);
		this.conexion.setUsernamePersonal(username);
	}

//	public void persistirConfiguracion() throws IOException {
//		JSONObject configuracion = new JSONObject();
//		configuracion.put("port",this.port);
//		configuracion.put("ip",this.ip);
//
//		FileWriter file = new FileWriter(filePath);
//		file.write(configuracion.toJSONString());
//		file.flush();
//
//	}
	
	public void iniciarConexion(String ipDestino, int puertoDestino) throws UnknownHostException, RuntimeException, IOException, 
	IllegalArgumentException{
		this.conexion.iniciarConversacion(ipDestino,puertoDestino);
	}

	public void recibirConexion(String ip, int port){
		this.setChanged();
		//crear un objeto para pasar informacion
		this.notifyObservers(this.INICIAR_CONEXION);
	}
 
	public void cerrarConexion() throws IOException {
		
		this.conexion.cerrarConexion();
	}

	public void enviarMensaje(String mensaje) throws IOException {
		this.conexion.enviarMensajeCliente(mensaje);
	}

	public void recibirMensaje(String mensaje){
		this.setChanged();
		this.notifyObservers(RECIBIR_MENSAJE);
	}

	public String getAccionObserver() {
		return accionObserver;
	}

	public Conectividad getConectividad() {
		return this.conexion;
	}

	
	@Override
	public void update(Observable o, Object arg) {	
			this.setChanged();
			this.notifyObservers(arg);
	}

	public void conectarServidorPrincipal() throws UnknownHostException, IllegalArgumentException, IOException {
		this.conexion.iniciarConexionServidorPrincipal();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void actualizarEstado(String mensaje){
		this.setChanged();
		this.notifyObservers(ACTUALIZAR);
	}

	public ArrayList<Cliente> getConectados() {
		return conectados;
	}

	public void setConectados(ArrayList<Cliente> conectados) {
		this.conectados = conectados;
	}

	public long getPingEcho() {
		return this.getConectividad().getPingEchoTime();
	}
}

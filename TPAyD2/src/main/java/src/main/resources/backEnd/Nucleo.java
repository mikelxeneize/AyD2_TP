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
    private List<Observer> observers = new ArrayList<>();
	

	public static Nucleo getInstance() {
		if (Nucleo.instance == null) {
			Nucleo.instance = new Nucleo();
		}
		return instance;
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

	public void cargarConfiguracion() {
		Long longPort = 0L;
		JSONParser jsonParser = new JSONParser();
		JSONObject configuracion = null;
		try (FileReader reader = new FileReader(this.filePath)) {
			Object obj = jsonParser.parse(reader);
			configuracion = (JSONObject) obj;
		} catch (IOException e) { //TO-DO me parece que hay que propagar
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.ip= (String) configuracion.get("ip");
		longPort = (Long) configuracion.get("port");
		this.port = longPort.intValue();
	}

	public void setConfiguracion(String ip, int port) {
		this.setPort(port);
		this.setIp(ip);
	}

	public void persistirConfiguracion() {
		JSONObject configuracion = new JSONObject();
		configuracion.put("port",this.port);
		configuracion.put("ip",this.ip);

		try (FileWriter file = new FileWriter(filePath)) {
			file.write(configuracion.toJSONString());
			file.flush();
		} catch (IOException e) { //TO-DO me parece que hay que propagar
			e.printStackTrace();
		}
	}


	public void activarEscucha() throws IOException {
		this.conexion = new Conectividad(this.ip, this.port);
		
		
		this.conexion.addObserver(this);
		this.conexion.escucharConexion(this.port);
	}

	public void iniciarConexion(String ip, int port) throws UnknownHostException, RuntimeException, IOException{
		this.conexion.iniciarConexion(ip,port);
	}

	public void recibirConexion(String ip, int port){
		this.setChanged();
		//crear un objeto para pasar informacion
		this.notifyObservers(this.INICIAR_CONEXION);
	}

	public void cerrarConexion() {
		this.conexion.cerrarConexion();
	}

	public void enviarMensaje(String mensaje) throws IOException {
		this.conexion.enviarMensaje(mensaje);
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
}

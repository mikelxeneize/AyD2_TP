package src.main.resources.backEnd;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import src.main.resources.conectividad.Conectividad;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Observable;

public class Nucleo extends Observable {
	private Conectividad conexion;
	private static Nucleo instance;
	private String ip;
	private int port;
	private String filePath = "./config.json";
	private String accionObserver;
	public static final String ENVIAR_MENSAJE = "enviar_mensaje";
	public static final String RECIBIR_MENSAJE = "recibir_mensaje";
	public static final String INICIAR_CONEXION= "iniciar_conexion";
	public static final String CERRAR_CONEXION = "cerrar_conexion";


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
		this.conexion.escucharConexion(this.port);
	}

	public void iniciarConexion(String ip, int port) throws UnknownHostException, RuntimeException, IOException{
		this.conexion.iniciarConexion(ip,port);
		this.accionObserver=this.INICIAR_CONEXION;
		
	}

	public void recibirConexion(String ip, int port){
		this.accionObserver=this.INICIAR_CONEXION;
		this.setChanged();
		this.notifyObservers();
	}

	public void cerrarConexion() {
		this.conexion.cerrarConexion();
		this.accionObserver=this.CERRAR_CONEXION;
		setChanged();
		this.notifyObservers();
	}

	public void enviarMensaje(String mensaje) {
		this.conexion.enviarMensaje(mensaje);
		this.accionObserver=this.ENVIAR_MENSAJE;
		this.setChanged();
		this.notifyObservers();
	}

	public void recibirMensaje(String mensaje){
		this.accionObserver=this.RECIBIR_MENSAJE;
		this.setChanged();
		this.notifyObservers(mensaje);
	}

	public String getAccionObserver() {
		return accionObserver;
	}

	public Conectividad getConectividad() {
		return this.conexion;
	}
}

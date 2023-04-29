package src.main.resources.backEnd;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import src.main.resources.conectividad.Conectividad;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Nucleo {
	private Conectividad conexion;
	private static Nucleo instance;
	private String ip;
	private int port;
	private String filePath = "./config.json";


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
		} catch (IOException e) {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void activarEscucha() throws IOException {
		this.conexion.escucharConexion(this.port);
	}

	public void iniciarConexion(String ip, int port) throws RuntimeException{
		this.conexion.iniciarConexion(ip,port);
	}

	public void cerrarConexion() throws RuntimeException{
		this.conexion.cerrarConexion();
	}

	public void enviarMensaje(String mensaje) {
		this.conexion.enviarMensaje(mensaje);
	}

	public void mostrarMensaje(String mensaje){
		this.conexion
	}

}

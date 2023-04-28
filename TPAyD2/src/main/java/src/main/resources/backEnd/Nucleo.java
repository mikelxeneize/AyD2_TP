package src.main.resources.backEnd;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import src.main.resources.conectividad.Cliente;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Nucleo {
	private Cliente cliente;
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

	public void cerrarConexion() {

		System.out.println("Conexion cerrada");
		// this.cliente.cerrarConexion(String ip, int port);
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

	public void iniciarConexion(String ip, int port){
		boolean conexion = false;
		this.cliente = new Cliente(ip,port);

		cliente.iniciarConexion(ip,port);
	}

	public void enviarMensaje(String mensaje) {
		this.cliente.enviarMensaje(mensaje);
	}

	public void mostrarMensaje(String mensaje){

	}
}

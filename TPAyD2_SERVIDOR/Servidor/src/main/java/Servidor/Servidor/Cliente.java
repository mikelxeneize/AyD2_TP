package Servidor.Servidor;

import java.net.Socket;
import java.util.ArrayList;

public class Cliente {
	int puerto;
	String ip;
	String username;
	
	Socket socket;
	String estado = "Disponible";

	int puertoReceptor;
	String ipReceptor;
	
	public int getPuerto() {
		return puerto;
	}
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	
	public Cliente(int puerto, String ip, Socket socket) {
		super();
		this.puerto = puerto;
		this.ip = ip;
		this.socket=socket;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
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
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String actualizacion() {
		return username+"="+puerto+"=" +ip+"="+estado+";";
	}
	
	
	
}

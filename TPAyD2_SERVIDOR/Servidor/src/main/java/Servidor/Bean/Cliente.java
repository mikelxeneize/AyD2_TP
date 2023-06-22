package Servidor.Bean;

import java.net.Socket;
import java.util.ArrayList;

public class Cliente extends SocketBean{
	
	
	String username;
	
	String estado = "Disponible";

	int puertoReceptor;
	String ipReceptor;
	
	
	
	public Cliente(int puerto, String ip, Socket socket) {
		super();
		this.puerto = puerto;
		this.ip = ip;
		this.socket=socket;
	}
	
	public Cliente( String username, int puerto, String ip, String estado) {
		this.puerto = puerto;
		this.ip = ip;
		this.estado=estado;
		this.username=username;
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

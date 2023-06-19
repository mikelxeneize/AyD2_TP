package bean;

import java.net.Socket;
import java.util.ArrayList;

public class Cliente {
	String puerto;
	String ip;
	String estado;
	String username;
	
	public String getPuerto() {
		return puerto;
	}
	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}
	
	public Cliente(String username,String puerto, String ip, String estado) {
		super();
		this.puerto = puerto;
		this.ip = ip;
		this.username=username;
		this.estado=estado;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String actualizacion() {
		return username+"="+puerto+"=" +ip+"="+estado;
	}
	@Override
	public String toString() {
		return username+"     "+puerto+"     " +ip+"     "+estado;
	}
	
	
	
}

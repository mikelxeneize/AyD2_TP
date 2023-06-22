package Servidor.Bean;

import java.net.Socket;

public class SocketBean {
	protected int puerto;
	protected String ip;
	protected Socket socket;
	protected String username;
	
	
	

	public SocketBean(int puerto, String ip, Socket socket) {
		super();
		this.puerto = puerto;
		this.ip = ip;
		this.socket = socket;
	}
	
	public SocketBean() {
	}
	
	public int getPuerto() {
		return puerto;
	}
	public void setPuerto(int puerto) {
		this.puerto = puerto;
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
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}

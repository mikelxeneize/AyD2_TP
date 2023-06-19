package bean;

import java.net.Socket;

public class ServerData {
	private Socket socket;
	private String ip;
	private String puerto;
	private String estado;
	private String username;
	private long ping=0;
	private long  pingAbs=0;
	
	public long getPingAbs() {
		return pingAbs;
	}

	public void setPingAbs(long pingAbs) {
		this.pingAbs = pingAbs;
	}

	public void setPing(long ping) {
		this.ping = ping;
	}

	public ServerData(String ip, String puerto) {
		this.ip = ip;
		this.puerto = puerto;
	}
	
	public Socket getSocket() {
		return socket;
	}
	public ServerData(Socket socket, String ip, String puerto) {
		super();
		this.socket = socket;
		this.ip = ip;
		this.puerto = puerto;
	}

	public ServerData(Socket socket, String ip, String puerto, String estado) {
		super();
		this.socket = socket;
		this.ip = ip;
		this.puerto = puerto;
		this.estado = estado;
		this.username = "";
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPuerto() {
		return puerto;
	}
	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}

	public String getEstado() {
		return estado;
	}

	public String getUsername() {
		return username;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getPing() {
		return ping;
	}

	public void setPing(Long ping) {
		this.ping = ping;
	}
	
	
}

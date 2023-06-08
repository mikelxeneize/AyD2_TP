package src.main.resources.conectividad;

import java.net.Socket;

public class serverData {
	Socket socket;
	String ip;
	int puerto;
	
	public serverData(String ip, int puerto) {
		this.ip = ip;
		this.puerto = puerto;
	}
	
	public Socket getSocket() {
		return socket;
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
	public int getPuerto() {
		return puerto;
	}
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
}
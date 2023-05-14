package Servidor.Servidor;

import java.net.Socket;

public class SocketBean {
	int puerto;
	Socket socket;
	public int getPuerto() {
		return puerto;
	}
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public SocketBean(int puerto, Socket socket) {
		super();
		this.puerto = puerto;
		this.socket = socket;
	}
	

	public SocketBean( Socket socket) {
		super();
		this.socket = socket;
	}
}

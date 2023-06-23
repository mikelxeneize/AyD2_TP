package Servidor.Bean;

import java.io.PrintWriter;
import java.net.Socket;

public class ServerData extends SocketBean{
	PrintWriter out;
	
	public ServerData(String ip, int puerto) {
		this.ip = ip;
		this.puerto = puerto;
	}
	
	public ServerData( int puerto,String ip,Socket socket) {
		super(puerto,ip,socket);
	}
	public PrintWriter getOut() {
		return out;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}

	@Override
	public String toString() {
		return this.ip+"="+this.puerto;
	}
	
}

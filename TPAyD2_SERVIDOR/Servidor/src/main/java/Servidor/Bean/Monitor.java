package Servidor.Bean;

import java.net.Socket;

public class Monitor extends SocketBean{
	
	public Monitor(int puerto, String ip, Socket socket) {
		super(puerto,ip,socket);
	}
	
}

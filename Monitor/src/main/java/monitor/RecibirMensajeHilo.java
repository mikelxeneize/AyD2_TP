package monitor;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import bean.MensajeExterno;

public class RecibirMensajeHilo extends Thread {
	private Socket socket;
	private Monitor monitor;
	private MensajeExterno mensajeExterno;
	private boolean isRun=true;
	
	public RecibirMensajeHilo(Socket socket, Monitor monitor) {
		this.socket = socket;
		this.monitor = monitor;
	}

	@Override
	public void run() {
		String mensaje = null;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
			isRun=false;
		}
		do {
			try {
				mensaje = in.readLine();
				mensajeExterno= new MensajeExterno(mensaje);
				monitor.receptorDeMensajes(mensajeExterno);
				
			} catch (SocketException e) {
				monitor.eliminarServidor(socket);
				isRun=false;
			} catch (IOException e) {
				e.printStackTrace();
				isRun=false;
			}
		} while (mensaje != null && isRun); // implica que se cerro la conexion con el servidor
	}

}

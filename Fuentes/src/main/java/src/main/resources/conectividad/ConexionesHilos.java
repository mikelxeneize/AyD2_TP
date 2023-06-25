package src.main.resources.conectividad;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import utils.IComandos;

public class ConexionesHilos  extends Thread implements IComandos{
	
	String ip;
	int puerto;
	ServidorData servidor;
	Conectividad conectividad;
	public ConexionesHilos(String string, int puerto, Conectividad conectividad, ServidorData servidor) {
		this.ip=string;
		this.puerto=puerto;
		this.conectividad=conectividad;
		this.servidor=servidor;
	}

	@Override
	public void run() {
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(ip,puerto));
			servidor.setSocket(socket);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			servidor.setOut(out);
			
			this.conectividad.recibirMensaje(socket); //inicia la escuchar del servidor nuevo
			if (this.conectividad.getServidorPrincipal() == null) {
				this.conectividad.setServidorPrincipal(servidor);  //se conecto al menos a 1 servidor
				this.conectividad.pingEchoServidores();	
			}
			
			
			MensajeExterno confirmacion =new MensajeExterno(socket.getInetAddress().toString(),Integer.toString(this.conectividad.getPuertopersonal()),INDEFINIDO,socket.getInetAddress().toString(),
					Integer.toString(socket.getPort()), INDEFINIDO , CONFIRMACION_CLIENTE,INDEFINIDO,INDEFINIDO); 
			this.conectividad.enviarMensajeExterno(confirmacion,servidor);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		
	}
}

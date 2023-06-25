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
			System.out.println("hilo de puerto"+puerto);
			socket.connect(new InetSocketAddress(ip,puerto));
			servidor.setSocket(socket);
			System.out.println("Coneion establecida");
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			servidor.setOut(out);
			
			this.conectividad.recibirMensaje(socket); //inicia la escuchar del servidor nuevo
			System.out.println("recibiendo mensajes del servidor");
			if (this.conectividad.getServidorPrincipal() == null) {
				this.conectividad.setServidorPrincipal(servidor);  //se conecto al menos a 1 servidor
				this.conectividad.pingEchoServidores();	
			}
			
			
			System.out.println("activar ping ");
			MensajeExterno confirmacion =new MensajeExterno(socket.getInetAddress().toString(),Integer.toString(this.conectividad.getPuertopersonal()),INDEFINIDO,socket.getInetAddress().toString(),
					Integer.toString(socket.getPort()), INDEFINIDO , CONFIRMACION_CLIENTE,INDEFINIDO,INDEFINIDO); 
			this.conectividad.enviarMensajeExterno(confirmacion,servidor);
			System.out.println("enviar confirmacion y exito de salida");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		
	}
}

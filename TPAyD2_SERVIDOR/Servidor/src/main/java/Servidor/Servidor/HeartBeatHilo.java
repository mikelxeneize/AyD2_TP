package Servidor.Servidor;

import java.io.IOException;
import java.io.PrintWriter;

public class HeartBeatHilo extends Thread {
	private Servidor servidor;
	private Boolean activo;
	public HeartBeatHilo(Servidor servidor, Boolean activo) {
		this.activo=activo;
		this.servidor = servidor;
	}

	@Override
	public void run() {
		int tiempo=5000; //tiempo en milisegundos 
		MensajeEncriptado mensaje;
		Cliente cliente;
		while(activo) {
			for (int i = 0; i < this.servidor.getListaConectados().size(); i++) {
				cliente= this.servidor.getListaConectados().get(i);
				mensaje=new MensajeEncriptado("%HeartBeat%", "", cliente.getIp(),
						cliente.getPuerto());
				PrintWriter out=null;
				try {
					out = new PrintWriter(cliente.getSocket().getOutputStream(), true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out.println(mensaje.toString());
				System.out.println("HeartBeat" + mensaje.toString());
			}
			
			try {
				Thread.sleep(tiempo);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}

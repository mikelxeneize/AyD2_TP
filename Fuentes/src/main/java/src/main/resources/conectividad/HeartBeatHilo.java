package src.main.resources.conectividad;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class HeartBeatHilo extends Thread {
	private Conectividad conectividad;
	public HeartBeatHilo(Conectividad conectividad) {

		this.conectividad= conectividad;
	}

	/**
	 * establece el ping-echo
	 * Reintenta la conexion contra servidorPrincipal todo el tiempo, hasta que lo logre
	 * Cuando el servidor principal cambia, eso lo ronoce solo
	 */
	@Override
	public void run() {
		int tiempo=1000; //tiempo en milisegundos 
		long limite=40000; //tolerancia de ping
		while(true) {
			if(System.currentTimeMillis()-this.conectividad.getHeartBeatTime()>limite)//No recibio el latido. Cambia a reintento
				//reintento de ping al server principal
				this.conectividad.reintento(this.conectividad.getServidorPrincipal());		
			try {
				Thread.sleep(tiempo);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}

package src.main.resources.conectividad;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class HeartBeatHilo extends Thread {
	private Conectividad conectividad;
	public HeartBeatHilo(Conectividad conectividad) {

		this.conectividad= conectividad;
	}

	@Override
	public void run() {
		int tiempo=1000; //tiempo en milisegundos 
		long limite=40000;
		while(true) {
			if(System.currentTimeMillis()-this.conectividad.getHeartBeatTime()>limite)//No recibio el latido. Cambia a reintento
				//reintento
				this.conectividad.reintento();
			try {
				Thread.sleep(tiempo);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}

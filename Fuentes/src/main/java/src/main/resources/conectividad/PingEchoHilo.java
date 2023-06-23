package src.main.resources.conectividad;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class PingEchoHilo extends Thread {
	private ServidorData servidor;
	private Conectividad conectividad;
	public PingEchoHilo(ServidorData servidor,Conectividad conectividad) {

		this.servidor = servidor;
		this.conectividad= conectividad;
	}

	@Override
	public void run() {
		int tiempo=1000; //tiempo en milisegundos 
		while(true) {
				PrintWriter out=null;
				try {
					out = new PrintWriter(servidor.getSocket().getOutputStream(), true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				this.conectividad.setPingEchoAbs(System.currentTimeMillis());
				Random random = new Random();
				
				try {
					Thread.sleep(random.nextInt(800));
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				out.println(this.conectividad.getIppersonal()+":"+this.conectividad.getPuertopersonal()+ ":%PingEcho%" + ": ");
				//System.out.println("PingEcho"+ this.conectividad.getIppersonal()+":"+this.conectividad.getPuertopersonal()+ "%PingEcho%" + ": ");
				
			try {
				Thread.sleep(tiempo);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}

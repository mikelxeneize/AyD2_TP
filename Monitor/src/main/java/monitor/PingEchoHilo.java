package monitor;

import java.io.IOException;
import java.util.Random;

import bean.ServerData;
import util.IComandos;

public class PingEchoHilo extends Thread implements IComandos{
	private ServerData servidor;
	
	public PingEchoHilo(ServerData servidor) {
		this.servidor = servidor;
	}

	@Override
	public void run() {
		int tiempo=1000; //tiempo en milisegundos 
		while(true) {
				servidor.setPingAbs(System.currentTimeMillis());
				
				Random random = new Random();
				try {
					Thread.sleep(random.nextInt(800));
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					Monitor.getInstance().enviarPingEcho(servidor);
				} catch (IOException e1) {
					e1.printStackTrace();
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

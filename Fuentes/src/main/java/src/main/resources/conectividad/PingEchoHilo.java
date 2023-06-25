package src.main.resources.conectividad;

import java.util.Random;

import utils.IComandos;

public class PingEchoHilo extends Thread implements IComandos {
	private ServidorData servidor;
	private Conectividad conectividad;

	public PingEchoHilo(ServidorData servidor, Conectividad conectividad) {
		this.servidor = servidor;
		this.conectividad = conectividad;
	}

	@Override
	public void run() {
		int tiempo = 1000; // tiempo en milisegundos
		while (true) {
			if (this.servidor.equals(this.conectividad.getServidorPrincipal())) {
				this.conectividad.setPingEchoAbs(System.currentTimeMillis());
				Random random = new Random();
				try {
					Thread.sleep(random.nextInt(800));
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				MensajeExterno mensajePingEcho = new MensajeExterno (this.conectividad.getIppersonal(),Integer.toString(this.conectividad.getPuertopersonal()),this.conectividad.getUsernamePersonal(),this.servidor.getIp(),Integer.toString(this.servidor.getPuerto())," ",PING_ECHO," "," ");
				this.conectividad.enviarMensajeExterno(mensajePingEcho,this.servidor);
				
				try {
					Thread.sleep(tiempo);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
				this.setServidor(this.conectividad.getServidorPrincipal());
		}
	}

	public ServidorData getServidor() {
		return servidor;
	}

	public void setServidor(ServidorData servidor) {
		this.servidor = servidor;
	}
}
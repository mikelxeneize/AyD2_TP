package Servidor.Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Servidor.Bean.MensajeExterno;
import Servidor.Bean.Monitor;
import Servidor.Util.IComandos;
import Servidor.Util.IEstados;

public class ServidorRecibirMensajeHiloMonitor extends Thread implements IComandos, IEstados{
	private Monitor monitor;
	private Servidor servidor;

	public ServidorRecibirMensajeHiloMonitor(Monitor monitor, Servidor servidor) {
		this.monitor = monitor;
		this.servidor = servidor;
	}

	@Override
	public void run() {
		String msg = null;
		MensajeExterno mensajerecibido;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(monitor.getSocket().getInputStream()));
			MensajeExterno mensajeConfirmacion = new MensajeExterno(servidor.getIpServidor(),
					Integer.toString(monitor.getSocket().getLocalPort()), " ", servidor.getIpServidor(),
					Integer.toString(monitor.getSocket().getPort()), " ", CONFIRMACION_MONITOR_RESPUESTA, " ", " ");
			servidor.enviarMensajeAMonitor(mensajeConfirmacion);
		} catch (IOException e1) {
			e1.printStackTrace(); 
		}

		do {
			try {
				msg = in.readLine();
				if (msg == null) {
					servidor.getListaPendientes().remove(this.monitor);
					
				} else {
					mensajerecibido = new MensajeExterno(msg);
					
					
					
					
				}
			} catch (IOException e) {
				
			} 

		} while (msg != null); 

	}
}

package Servidor.Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Servidor.Bean.SocketBean;
import Servidor.Bean.MensajeExterno;
import Servidor.Util.IComandos;
import Servidor.Util.IEstados;

public class ServidorRecibirMensajeHilo extends Thread implements IComandos, IEstados {
	private SocketBean socketBean;
	private Servidor servidor;

	public ServidorRecibirMensajeHilo(SocketBean socketBean, Servidor servidor) {
		this.socketBean = socketBean;
		this.servidor = servidor;
	}
 
	@Override
	public void run() {
		String msg = null;
		MensajeExterno mensajerecibido;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(socketBean.getSocket().getInputStream()));
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
 
		do {
			try {
				
				msg = in.readLine();
				if (msg == null) {
					servidor.getListaPendientes().remove(this.socketBean);
					
				} else {
					mensajerecibido = new MensajeExterno(msg);
					
					if (mensajerecibido.getComando().equals(CONFIRMACION_CLIENTE)) {
						this.socketBean.setPuerto(Integer.parseInt(mensajerecibido.getPuertoorigen()));
						servidor.agregarALista(mensajerecibido,CLIENTE);
						
					} 
					else if (mensajerecibido.getComando().equals(CONFIRMACION_SERVIDOR)) { 		
						servidor.agregarALista(mensajerecibido,SERVIDOR);
						
					}
					else if (mensajerecibido.getComando().equals(CONFIRMACION_MONITOR)) { 	
						servidor.agregarALista(mensajerecibido,MONITOR);
					
					}
					servidor.getListaPendientes().remove(this.socketBean);
					msg=null;
				}
			} catch (IOException e) {
				
			} 

		} while (msg != null); 

	}
}

package Servidor.Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Servidor.Bean.MensajeExterno;
import Servidor.Bean.ServerData;
import Servidor.Util.IComandos;
import Servidor.Util.IEstados;

public class ServidorRecibirMensajeHiloServidor extends Thread implements IComandos, IEstados{
	private ServerData serverData;
	private Servidor servidor;
	private boolean modo;
	
	public ServidorRecibirMensajeHiloServidor(ServerData serverData, Servidor servidor, boolean i) {
		this.serverData = serverData;
		this.servidor = servidor;
		this.modo=i;
	}

	@Override
	public void run() {
		String msg = null;
		MensajeExterno mensajerecibido;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(serverData.getSocket().getInputStream()));
			
			if(modo) {
			MensajeExterno mensajeConfirmacion = new MensajeExterno(servidor.getIpServidor(),
					Integer.toString(serverData.getSocket().getLocalPort()), " ", servidor.getIpServidor(),
					Integer.toString(serverData.getSocket().getPort()), " ", CONFIRMACION_SERVIDOR_RESPUESTA, " ", " ");
			servidor.enviarMensajeAServidor(mensajeConfirmacion);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		do {
			try {
				msg = in.readLine();
				if (msg == null) {
					servidor.getListaPendientes().remove(this.serverData);
					
				} else {
					mensajerecibido = new MensajeExterno(msg);
					if(mensajerecibido.getComando().equals(PEDIR_LISTA)) {
						servidor.MandarLista2(mensajerecibido);
						servidor.avisarClientesNuevoServidor(mensajerecibido);
					}
					else if (mensajerecibido.getComando().equals(CONFIRMACION_SERVIDOR_RESPUESTA)) {
						MensajeExterno mensaje = new MensajeExterno("localhost",
								Integer.toString(serverData.getSocket().getLocalPort()), " ", serverData.getIp(),
								Integer.toString(serverData.getSocket().getPort()), " ", PEDIR_LISTA, servidor.miInformacionEnString(), " ");
						servidor.enviarMensajeAServidor(mensaje);
					}
					else if (mensajerecibido.getComando().equals(LISTA_COMPLETA)) {
						servidor.crearLista(mensajerecibido.getCuerpo());
						msg=null;
					}
					
				}
			} catch (IOException e) {
				
			} 

		} while (msg != null); 

	}
}

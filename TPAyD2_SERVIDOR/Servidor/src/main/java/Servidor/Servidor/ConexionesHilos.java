package Servidor.Servidor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import Servidor.Bean.MensajeExterno;
import Servidor.Bean.ServerData;
import Servidor.Util.IComandos;
public class ConexionesHilos  extends Thread implements IComandos{
	
	String ip;
	int puerto;
	ServerData server;
	Servidor servidor;
	public ConexionesHilos(String string, int puerto, Servidor servidor, ServerData server) {
		this.ip=string;
		this.puerto=puerto;
		this.servidor=servidor;
		this.server=server;
	}

	@Override
	public void run() {
		Socket socket;
		try {
			//me conecto
			//SETEAR EL PUERTO
			if(this.servidor.getPuertoServidor()!=puerto) {
				socket = new Socket("localhost", puerto);
				System.out.println("Servidor encontrado para obtener informacion en el puerto:"+puerto);
				this.servidor.setSincronizado(true);
				this.server.setSocket(socket);
				this.server.setPuerto(socket.getPort());
				if(this.servidor.isSincronizado()) {
					//entro en escucha antes de enviar nada
					
					ServidorRecibirMensajeHiloServidor recibirMensaje = new ServidorRecibirMensajeHiloServidor(server, this.servidor,false);
					recibirMensaje.start();
					
					//envio confirmacion para registro 
					MensajeExterno mensajeConseguirClientes = new MensajeExterno("/127.0.0.1",
							Integer.toString(socket.getLocalPort()), " ", this.servidor.getIpServidor(),
							Integer.toString(socket.getPort()), " ", CONFIRMACION_SERVIDOR, " ", " ");
					
					this.servidor.enviarMensajeAPendientes(mensajeConseguirClientes);
				}
			}
			
			
		} catch (IOException e) {
			
		}
		
		this.servidor.incrementar();
		}
}

package Servidor.Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServidorRecibirMensajeHilo extends Thread implements IComandos, IEstados {
	private Cliente cliente;
	private Servidor servidor;

	public ServidorRecibirMensajeHilo(Cliente cliente, Servidor servidor) {
		this.cliente = cliente;
		this.servidor = servidor;
	}

	@Override
	public void run() {
		String msg = null;
		MensajeExterno mensajerecibido;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(cliente.getSocket().getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		do {
			try {
				msg = in.readLine();
				// System.out.println("1: "+msg);
				if (msg == null) {
					// this.cliente.setEstado("Desconectado");
					this.servidor.getListaConectados().remove(this.cliente);
					this.servidor.MandarLista1();
				} else {
					mensajerecibido = new MensajeExterno(msg);
					if (mensajerecibido.getComando().equals(NOMBRE_USUARIO)) {// Recibe comando de registro de usuario
						this.cliente.setUsername(mensajerecibido.getUsernameorigen()); // el mensaje tiene que llegar en
																						// vacio en receptor
						this.servidor.MandarLista1();

					}

					else if (mensajerecibido.getComando().equals(CERRAR_CONVERSACION)) { // Recibe comando de cerrar la
																						// conexion
																						// con el otro cliente cambiarEstado("Disponible") y avisarle al otro que no hay mas charla;
						this.servidor.cortarConversacion(mensajerecibido);
						
					}

					else if (mensajerecibido.getComando().equals(PING_ECHO)) {// Recibe comando de Ping, y emite una
																				// señal al
																				// cliente
						MensajeExterno mensajePingEcho = new MensajeExterno(this.servidor.getIpServidor(),
								Integer.toString(this.servidor.getPuertoServidor()), " ", this.cliente.ip,
								Integer.toString(this.cliente.puerto), this.cliente.username,RESPUESTA_PING_ECHO,
								" ", " ");
						this.servidor.enviarMensajeACliente(mensajePingEcho);
					}

					else if (mensajerecibido.getComando().equals(INICIAR_CONVERSACION)
							&& this.cliente.getEstado().equals(DISPONIBLE)) {// Recibe comando de iniciar conversacion
																				// con otro cliente
						this.servidor.iniciarConexionAReceptor(mensajerecibido);
					}
					
					else if (mensajerecibido.getComando().equals(INICIAR_CONEXION_SERVIDOR)) { // Recibe comando de Monitor y le devuelve la confirmacion
						
						MensajeExterno mensajeConfirmacion = new MensajeExterno(this.servidor.getIpServidor(),
								Integer.toString(this.servidor.getPuertoServidor()),this.servidor.getUsernameServidor(),this.cliente.getIp(),Integer.toString(this.cliente.getPuerto()) ,INDEFINIDO,INICIAR_CONEXION_SERVIDOR_CONFIRMACION,INDEFINIDO,
								INDEFINIDO);
						this.servidor.enviarMensajeACliente(mensajeConfirmacion);
						
					} 
                    
					else {// Recibe un mensaje a transmitir en un chat ya activo
						this.servidor.enviarMensajeACliente(mensajerecibido);
					}
				}
			} catch (IOException e) {
				MensajeExterno mensajeCerrado = new MensajeExterno(this.cliente.getIp(),Integer.toString(this.cliente.getPuerto()),this.cliente.getUsername(),this.cliente.getIpReceptor(),Integer.toString(this.cliente.getPuertoReceptor())," ",CERRAR_CONVERSACION," "," ");
				try {
					this.servidor.cortarConversacion(mensajeCerrado);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				msg = null;
				this.servidor.getListaConectados().remove(this.cliente);
				this.servidor.MandarLista1();
			} catch (InterruptedException e) {
				e.printStackTrace();
				this.servidor.getListaConectados().remove(this.cliente);
				this.servidor.MandarLista1();
			}

		} while (msg != null); // implica que se cerro la conexion

	}
}

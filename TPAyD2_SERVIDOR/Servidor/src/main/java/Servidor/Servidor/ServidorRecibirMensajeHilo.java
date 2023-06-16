package Servidor.Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServidorRecibirMensajeHilo extends Thread {
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
		MensajeExterno mensajeAReceptor;
		MensajeExterno mensajeConfirmacion;
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
					if (mensajerecibido.getComando().equals("%nombre_usuario%")) {// Recibe comando de registro de usuario
						this.cliente.setUsername(mensajerecibido.getUsernameorigen()); // el mensaje tiene que llegar en vacio en receptor
						this.servidor.MandarLista1();
						
					}

					else if (mensajerecibido.getComando().equals("%cerrar_conexion%")) { // Recibe comando de cerrar la conexion
																					// con el otro cliente
						// cambiarEstado("Disponible") y avisarle al otro que no hay mas charla;
						this.cliente.setEstado("Disponible");
						this.servidor.MandarLista1();
						this.servidor.cortarConexionAReceptor(this.cliente);
						this.cliente.setIpReceptor(null);
					}

					else if (mensajerecibido.getComando().equals("%PingEcho%")) {// Recibe comando de Ping, y emite una señal al
																			// cliente
						MensajeExterno mensajePingEcho = new MensajeExterno(this.servidor.getIpServidor(),Integer.toString(this.servidor.getPuertoServidor())," ",this.cliente.ip, Integer.toString(this.cliente.puerto), this.cliente.username,"%Respuesta_Ping_Echo%"," "," ");
						this.servidor.enviarMensajeACliente(mensajePingEcho, mensajerecibido.getIporigen(),Integer.parseInt(mensajerecibido.getPuertoorigen()));
					}

					else if (mensajerecibido.getComando().equals("%Iniciar_Conversacion%")
							&& this.cliente.getEstado().equals("Disponible")) {// Recibe comando de iniciar conversacion
																				// con otro cliente
						mensajeAReceptor = new MensajeExterno(this.servidor.getIpServidor(),Integer.toString(this.servidor.getPuertoServidor())," ",this.cliente.ip, Integer.toString(this.cliente.puerto), this.cliente.username,"%Conexion_establecida%"," "," ");
						mensajeAReceptor = new MensajeExterno("%Conexion_establecida%", "", this.cliente.getIp(),
								this.cliente.getPuerto());
						mensajeConfirmacion = new MensajeExterno("%Conexion_rechazada%", "", this.cliente.getIp(),
								this.cliente.getPuertoReceptor());
						// Inicia conexion con el receptor
						if (this.servidor.iniciarConexionAReceptor(mensajerecibido, mensajeAReceptor, mensajeConfirmacion)==true) {
						// Le envia un mensaje al cliente 1 con la confirmacion de que la conexion se
						// establecio	
						
						// Configura el arraylist con los nuevos estados
						this.cliente.setIpReceptor(mensajerecibido.getIp());
						this.cliente.setPuertoReceptor(mensajerecibido.getPuerto());
						this.cliente.setEstado("Ocupado");
						// Notifica a todos los usuarios del sistema sobre esta nueva conexion
						this.servidor.MandarLista1();
						}
						this.servidor.enviarMensajeACliente(mensajeConfirmacion, this.cliente.getIp(),
								this.cliente.getPuerto());
					}

					// else if (mensajerecibido.getComando().equals("%nuevoServidorPasivo%")) { // Recibe comando de Ping, y emite
																						// una señal al cliente
					//	this.servidor.notificarUsuarios(mensajerecibido.getIp(), mensajerecibido.getPuerto());
					//
					//} else if (mensajerecibido.getComando().equals("%preguntar_principal%")) { // Le preugntan al servidor si es
					//																	// el principal
					//	if (this.servidor.isPrincipal()) {
					//		mensajeConfirmacion = new MensajeExterno("%responder_principal%", "",
					//				this.servidor.getIpServidor(), this.servidor.getPuertoServidor());
					//		this.servidor.enviarMensajeACliente(mensajeConfirmacion, this.cliente.getIp(),
					//				this.cliente.getPuerto());
					//	}
					//} 

					else {// Recibe un mensaje a transmitir en un chat ya activo
						this.servidor.enviarMensajeACliente(mensajerecibido, this.cliente.getIpReceptor(),
								this.cliente.getPuertoReceptor());
					}
				}
			} catch (IOException e) {
				this.cliente.setEstado("Disponible");
				this.servidor.MandarLista1();
				try {
					this.servidor.cortarConexionAReceptor(this.cliente.getIpReceptor(),
							this.cliente.getPuertoReceptor());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.cliente.setIpReceptor(null);
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

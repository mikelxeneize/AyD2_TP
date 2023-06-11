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
		MensajeEncriptado mensaje;
		MensajeEncriptado mensajeAReceptor;
		MensajeEncriptado mensajeConfirmacion;
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
					mensaje = new MensajeEncriptado(msg);
					if (mensaje.getMensaje().equals("%nombre_usuario%")) {// Recibe comando de registro de usuario
						this.cliente.setUsername(mensaje.getUsername());
						this.servidor.MandarLista1();
						
					}

					else if (mensaje.getMensaje().equals("%cerrar_conexion%")) { // Recibe comando de cerrar la conexion
																					// con el otro cliente
						// cambiarEstado("Disponible") y avisarle al otro que no hay mas charla;
						this.cliente.setEstado("Disponible");
						this.servidor.MandarLista1();
						this.servidor.cortarConexionAReceptor(this.cliente.getIpReceptor(),
								this.cliente.getPuertoReceptor());
						this.cliente.setIpReceptor(null);
					}

					else if (mensaje.getMensaje().equals("%PingEcho%")) {// Recibe comando de Ping, y emite una señal al
																			// cliente
						this.servidor
								.enviarMensajeACliente(
										new MensajeEncriptado("%Respuesta_Ping_Echo%", "", this.cliente.getIp(),
												this.cliente.getPuerto()),
										this.cliente.getIp(), this.cliente.getPuerto());
					}

					else if (mensaje.getMensaje().equals("%Iniciar_Conversacion%")
							&& this.cliente.getEstado().equals("Disponible")) {// Recibe comando de iniciar conversacion
																				// con otro cliente
						mensajeAReceptor = new MensajeEncriptado("%Conexion_establecida%", "", this.cliente.getIp(),
								this.cliente.getPuerto());
						mensajeConfirmacion = new MensajeEncriptado("%Conexion_rechazada%", "", this.cliente.getIp(),
								this.cliente.getPuertoReceptor());
						// Inicia conexion con el receptor
						if (this.servidor.iniciarConexionAReceptor(mensaje, mensajeAReceptor, mensajeConfirmacion)==true) {
						// Le envia un mensaje al cliente 1 con la confirmacion de que la conexion se
						// establecio	
						
						// Configura el arraylist con los nuevos estados
						this.cliente.setIpReceptor(mensaje.getIp());
						this.cliente.setPuertoReceptor(mensaje.getPuerto());
						this.cliente.setEstado("Ocupado");
						// Notifica a todos los usuarios del sistema sobre esta nueva conexion
						this.servidor.MandarLista1();
						}
						this.servidor.enviarMensajeACliente(mensajeConfirmacion, this.cliente.getIp(),
								this.cliente.getPuerto());
					}

					else if (mensaje.getMensaje().equals("%nuevoServidorPasivo%")) { // Recibe comando de Ping, y emite
																						// una señal al cliente
						this.servidor.notificarUsuarios(mensaje.getIp(), mensaje.getPuerto());
					} else if (mensaje.getMensaje().equals("%preguntar_principal%")) { // Le preugntan al servidor si es
																						// el principal
						if (this.servidor.isPrincipal()) {
							mensajeConfirmacion = new MensajeEncriptado("%responder_principal%", "",
									this.servidor.getIpServidor(), this.servidor.getPuertoServidor());
							this.servidor.enviarMensajeACliente(mensajeConfirmacion, this.cliente.getIp(),
									this.cliente.getPuerto());
						}
					}

					else {// Recibe un mensaje a transmitir en un chat ya activo
						this.servidor.enviarMensajeACliente(mensaje, this.cliente.getIpReceptor(),
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

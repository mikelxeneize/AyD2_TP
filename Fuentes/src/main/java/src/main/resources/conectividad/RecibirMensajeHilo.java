package src.main.resources.conectividad;

import src.main.resources.backEnd.Nucleo;
import utils.IComandos;
import utils.IAcciones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class RecibirMensajeHilo extends Thread implements IComandos {
	private Socket socket;
	private Conectividad conectividad;
	private String clave;
	private String algoritmo;

	public RecibirMensajeHilo(Socket socket, Conectividad conectividad) {
		this.socket = socket;
		this.conectividad = conectividad;
	}

	@Override
	public void run() {
		String msg = null;
		Mensaje mensaje;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		do {
			try {
				msg = in.readLine();
			} catch (SocketException e) {  //TOBIAS dice: esto me parece que no pasa nunca e 
				if(this.socket==this.conectividad.getServidorPrincipal().getSocket()) {
					try {
						this.socket.close();
					} catch (IOException e1) {
						
					}
					//guardo los valores desl servidor caido para reintentar la conexion
					ServidorData servidorPrincipalViejo= this.conectividad.getServidorPrincipal();  
					System.out.println("Servidor caido");
					if(this.conectividad.getServidores().size() == 0){ //si no hay mas servidores
						System.out.println("No hay para swap, debe realizarse reintento");
						if (! this.conectividad.reintento(servidorPrincipalViejo.getIp(), servidorPrincipalViejo.getPuerto(),true)){ //si el reintento fallo
							mensaje = new Mensaje("", "volver_configuracion_inicial"); 
							this.conectividad.notificarAccion(mensaje);	
						}
							
					}else { //si hay mas de un servidor hace el swap e intenta la reconexion sin mostrar nada al usuario
						System.out.println("Iniciando Swap");
						this.conectividad.servidorPrincipalSwap();
						//this.conectividad.reintento(servidorPrincipalViejo.getIp(), servidorPrincipalViejo.getPuerto(),false);
						msg=null;
					}
				}
			} catch (IOException e) { 
				e.printStackTrace();
				System.out.println("12: " + "Error mientras se esperaba un mensaje");
			}
			this.clave = this.conectividad.getClave();
			this.algoritmo = this.conectividad.getAlgoritmo();
			if (msg != null) {
				MensajeExterno mensajeExterno = new MensajeExterno(msg);

				if (mensajeExterno.getComando().equals(ACTUALIZAR_LISTA)){ // Recibe comando de actualizacion de
					if(this.socket== this.conectividad.getServidorPrincipal().getSocket()) {												// directorio
						this.conectividad.actualizar(mensajeExterno.getCuerpo());
					}
					System.out.println("Mensaje Recibido: "+msg);
				}

				else if (mensajeExterno.getComando().equals(CERRAR_CONVERSACION)) { // Recibe comando de cerrar el

					System.out.println("Mensaje Recibido: "+msg);
					if(this.socket== this.conectividad.getServidorPrincipal().getSocket()) {// chat
						mensaje = new Mensaje(msg, "te cerraron");
						this.conectividad.notificarAccion(mensaje);
						this.conectividad.setIpReceptor(null);
						mensaje = new Mensaje("", "Actualizar");
						this.conectividad.notificarAccion(mensaje);
					}
				}

				else if (mensajeExterno.getComando().equals(RESPUESTA_PING_ECHO)) { // Recibe comando de
																	// respuesta de Ping Echo
					// CalculoTiempo
					Long time = System.currentTimeMillis() - this.conectividad.getPingEchoAbs();
					this.conectividad.setPingEchoTime(time);
				}
				
				else if (mensajeExterno.getComando().equals(HEART_BEAT)) { // Recibe comando de HeartBeat
					this.conectividad.setHeartBeatTime(System.currentTimeMillis());

				}

				else if (mensajeExterno.getComando().equals(CONEXION_RECHAZADA)) { // El cliente solicito iniciar una conexion y se le fue rechazada
					System.out.println("Mensaje Recibido: "+msg);
					if(this.socket== this.conectividad.getServidorPrincipal().getSocket()) {
					this.conectividad
							.notificarAccion(new Mensaje(mensajeExterno.getCuerpo(),"Conexion_rechazada" ));
					}
					
				}
				else if (mensajeExterno.getComando().equals(CONFIRMACION_CLIENTE_RESPUESTA)) { // El cliente solicito iniciar una conexion y se le fue rechazada
					System.out.println("Mensaje Recibido: "+msg);
					this.conectividad.eliminarPendientes(this.socket);
					this.conectividad.notificarNombreServidores(this.socket);	

					
				}
				else if (mensajeExterno.getComando().equals(AVISAR_CLIENTES_DE_NUEVO_SERVIDOR)) { // Se recibe la informacion del nuevo servidor al cual conectarse
					System.out.println("Mensaje Recibido: "+msg);
						try {
							this.conectividad.iniciarConexionServidorNuevo(mensajeExterno);
						} catch (IOException e) {
							e.printStackTrace();
						}

						
							
				}
				else if (mensajeExterno.getComando().equals(CONEXION_ESTABLECIDA)) { // Se le solicita al este cliente iniciar una conexion
					System.out.println("Mensaje Recibido: "+msg);
					if(this.socket== this.conectividad.getServidorPrincipal().getSocket()) {
						this.conectividad.setIpReceptor(mensajeExterno.getIporigen());
						this.conectividad.setPuertoReceptor(Integer.valueOf(mensajeExterno.getPuertoorigen()));
						this.conectividad.notificarAccion(new Mensaje("", "conexion establecida"));
					}
					
				}
				
				else {
					if(this.socket== this.conectividad.getServidorPrincipal().getSocket()) {
						String mensajeDesencriptado=Codificacion.desencriptar(this.conectividad.getClave(),mensajeExterno.getCuerpo(),this.conectividad.getAlgoritmo());
	                    mensaje = new Mensaje(mensajeDesencriptado, "mensaje recibido");																			// CREO
						mensaje.setIp(this.socket.getInetAddress().getHostAddress());
						mensaje.setPuerto(this.conectividad.getPuertopersonal());
						this.conectividad.notificarAccion(mensaje);
					}
					System.out.println("Mensaje Recibido: "+msg);
				}
			}
		} while (msg != null); // implica que se cerro la conexion con el servidor
		
		
		
	}

}

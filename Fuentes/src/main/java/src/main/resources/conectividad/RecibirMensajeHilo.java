package src.main.resources.conectividad;

import src.main.resources.backEnd.Nucleo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class RecibirMensajeHilo extends Thread {
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
		do {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			try {
				msg = in.readLine();
			} catch (SocketException e) {
				System.out.println("pepe");	//ESTO ES CLAVE
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("12: "+"Error mientras se esperaba un mensaje");
			}  
			this.clave = this.conectividad.getClave();
			this.algoritmo = this.conectividad.getAlgoritmo();
			if (msg != null) { 
				MensajeEncriptado mensajeEncriptado = new MensajeEncriptado(this.clave, msg, this.algoritmo);
				
				//System.out.println("30 :"+msg);
				if (!mensajeEncriptado.getMensaje().equals("%Respuesta_Ping_Echo%")) {
					System.out.println("30 :"+msg);
				}
				
				if (mensajeEncriptado.getMensaje().equals("%Actualizar%")) { // Recibe comando de actualizacion de directorio
					this.conectividad.actualizar(mensajeEncriptado.getClientecompleto());
				}
				
				else if (mensajeEncriptado.getMensaje().equals("%cerrar_conexion%")) { //  Recibe comando de cerrar el chat
					mensaje = new Mensaje(msg, "te cerraron la conexion papirrin");
					this.conectividad.notificarAccion(mensaje);
					this.conectividad.setIpReceptor(null);
					mensaje = new Mensaje("", "Actualizar");
					this.conectividad.notificarAccion(mensaje);
				} 
				
				else if (mensajeEncriptado.getMensaje().equals("%Respuesta_Ping_Echo%")) { //  Recibe comando de respuesta de Ping Echo
					//CalculoTiempo
					Long time=System.currentTimeMillis() - this.conectividad.getPingEchoAbs();
					this.conectividad.setPingEchoTime(time);
				}
				
				else if (mensajeEncriptado.getMensaje().equals("%HearBeat%")) {  //Recibe comando de HeartBeat
					this.conectividad.setHeartBeatTime(System.currentTimeMillis());
				
				} 
				
				else if (mensajeEncriptado.getMensaje().equals("%HearBeat%")) {  //Recibe comando de confirmacion de chat verdadero
					this.conectividad.setHeartBeatTime(System.currentTimeMillis());
				
				} 
				
				else if (mensajeEncriptado.getMensaje().equals("%Conexion_rechazada%")) {  //Recibe comando de confirmacion de chat falsa
					this.conectividad.notificarAccion(new Mensaje(mensajeEncriptado.getMensaje(), "Conexion_rechazada"));
				} 
				
				else if (mensajeEncriptado.getMensaje().equals("%Conexion_establecida%")){ // Recibe mensaje de conexion solicitada de otro cliente
					this.conectividad.setIpReceptor(mensajeEncriptado.getIp());
					this.conectividad.setPuertoReceptor(mensajeEncriptado.getPuerto());
					this.conectividad.notificarAccion(new Mensaje("", "conexion establecida"));
				} 
				
				else if (mensajeEncriptado.getMensaje().equals("%nuevoServidorPasivo%")){ // Servidor informa de nuesvo serverPasiovo, hay q conectarse
					this.conectividad.registrarServidorSecundario(mensajeEncriptado.getIp(),mensajeEncriptado.getPuerto());
				} 
				

				else if (mensajeEncriptado.getMensaje().equals("%responder_principal%")){ // Sos informado que el server q te envio esto es el principal
					this.conectividad.actualizarServidorPrincipal(mensajeEncriptado.getIp(),mensajeEncriptado.getPuerto());
				} 
				
				else {
					mensaje = new Mensaje(mensajeEncriptado.getMensaje(), "mensaje recibido"); //Recibe mensaje  normal CREO
					mensaje.setIp(this.socket.getInetAddress().getHostAddress());
					mensaje.setPuerto(this.conectividad.getPuertopersonal()); 
					this.conectividad.notificarAccion(mensaje);
				}
			}
		} while (msg != null); // implica que se cerro la conexion con el servidor
		System.out.println("13: "+"cerraron la ventana, sali por despues del while");
		mensaje = new Mensaje("", "servidor desconectado");
		this.conectividad.notificarAccion(mensaje);
		this.conectividad.reintento();
	}

}

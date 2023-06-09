package src.main.resources.conectividad;

import src.main.resources.backEnd.Nucleo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

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
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error mientras se esperaba un mensaje");
			}
			this.clave = this.conectividad.getClave();
			this.algoritmo = this.conectividad.getAlgoritmo();
			if (msg != null) {
				MensajeEncriptado mensajeEncriptado = new MensajeEncriptado(this.clave, msg, this.algoritmo);
				if (mensajeEncriptado.getMensaje().equals("%Actualizar%")) { // actualiza lista usuarios
					this.conectividad.actualizar(mensajeEncriptado.getClientecompleto());
					mensaje = new Mensaje("", "Actualizar");
					this.conectividad.notificarAccion(mensaje);

				} else if (mensajeEncriptado.getMensaje().equals("%Usuarios%")) { // crea lista usuarios
					this.conectividad.actualizar(mensajeEncriptado.getClientecompleto());
				} else if (mensajeEncriptado.getMensaje().equals("%Imprimir%")) { //
					mensaje = new Mensaje("", "Actualizar");
					this.conectividad.notificarAccion(mensaje);
				} else if (mensajeEncriptado.getMensaje().equals("%cerrar_conexion%")) { // cierra conversacion
					mensaje = new Mensaje(msg, "conexion cerrada");
					this.conectividad.notificarAccion(mensaje);
					this.conectividad.setIpReceptor(null);
					mensaje = new Mensaje("", "Actualizar");
					this.conectividad.notificarAccion(mensaje);
				} else { // mensaje recibido
					if (this.conectividad.getIpReceptor() == null) { // alguien te inicio un chat
						this.conectividad.setIpReceptor(mensajeEncriptado.getIp());
						this.conectividad.setPuertoReceptor(mensajeEncriptado.getPuerto());
						this.conectividad.notificarAccion(new Mensaje("", "conexion establecida"));
					} else {
						mensaje = new Mensaje(mensajeEncriptado.getMensaje(), "mensaje recibido");
						mensaje.setIp(this.socket.getInetAddress().getHostAddress());
						mensaje.setPuerto(this.conectividad.getPuertopersonal());
						this.conectividad.notificarAccion(mensaje);
					}
				}
			}
		} while (msg != null); // implica que se cerro la conexion con el servidor
		System.out.println("cerraron la ventana, sali por despues del while");
		mensaje = new Mensaje("", "servidor desconectado");
		this.conectividad.notificarAccion(mensaje);
	}

}

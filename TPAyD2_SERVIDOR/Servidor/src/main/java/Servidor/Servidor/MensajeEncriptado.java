package Servidor.Servidor;

import java.net.InetAddress;

public class MensajeEncriptado {
	private String mensaje;
	private String estado;
	private String ip;
	private int puerto;
	
	
	public MensajeEncriptado(String mensaje) {
		String[] partes = mensaje.split(":");
		this.setIp(partes[0]);
		this.setPuerto(Integer.parseInt(partes[1]));
		this.setMensaje(partes[2]);
	}
	
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getIp() {
		return ip;
	}


	public int getPuerto() {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public String toString() {
		return ip+":"+puerto+""+mensaje;
	}
	
	
}

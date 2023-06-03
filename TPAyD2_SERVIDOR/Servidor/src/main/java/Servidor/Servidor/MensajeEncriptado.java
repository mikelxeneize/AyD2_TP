package Servidor.Servidor;

import java.net.InetAddress;

public class MensajeEncriptado {
	private String mensaje;
	private String estado;
	private String ip;
	private int puerto;
	private String username;
	
	
	public MensajeEncriptado(String mensaje, String estado, String ip, int puerto) {
		super();
		this.mensaje = mensaje;
		this.estado = estado;
		this.ip = ip;
		this.puerto = puerto;
	}

	public MensajeEncriptado(String mensaje) {
		String[] partes = mensaje.split(":");
		this.setPuerto(Integer.parseInt(partes[1]));
		this.setMensaje(partes[2]);
		this.setUsername(partes[3]);
		if(partes[0].equals("localhost"))
			this.setIp("/127.0.0.1");
		else
			this.setIp("/"+partes[0]);
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


	public String getIpTruncada() {
		return ip.substring(1);
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return ip+":"+puerto+":"+mensaje+":"+"pepe";
	}
	
	
}

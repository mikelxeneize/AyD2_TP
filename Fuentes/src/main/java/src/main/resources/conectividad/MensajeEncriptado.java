package src.main.resources.conectividad;

import java.net.InetAddress;

public class MensajeEncriptado {
	private String mensaje; //[] comando 
	private String estado; //[2]
	private String ip; //[0]
	private int puerto; //[1]
	private String clientecompleto; //[3]

	public MensajeEncriptado(String clave, String mensaje, String algoritmo) throws java.lang.NullPointerException{
		String[] partes = mensaje.split(":");
		this.setPuerto(Integer.parseInt(partes[1]));
		this.setClientecompleto(partes[3]);
		if (partes[2].charAt(0) == '%') { //cuando el mensaje es un comando y NO esta encriptado
			this.setMensaje(partes[2]);
		}else
			this.setMensaje(Codificacion.desencriptar(clave, partes[2], algoritmo));
		if (partes[0].equals("localhost"))
			this.setIp("127.0.0.1");
		else
			this.setIp(partes[0]);
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

	@Override
	public String toString() {
		return ip + ":" + puerto + ":" + mensaje;
	}

	public String getClientecompleto() {
		return clientecompleto;
	}

	public void setClientecompleto(String clientecompleto) {
		this.clientecompleto = clientecompleto;
	}


}

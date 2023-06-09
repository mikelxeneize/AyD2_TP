package src.main.resources.conectividad;

import java.net.InetAddress;

public class MensajeEncriptado {
	private String mensaje;
	private String estado;
	private String ip;
	private int puerto;
	private String clientecompleto;

	public MensajeEncriptado(String clave, String mensaje, String algoritmo) throws java.lang.NullPointerException{
		String[] partes = mensaje.split(":");
		this.setPuerto(Integer.parseInt(partes[1]));
		this.setClientecompleto(partes[3]);
		if (partes[2].equals("%Actualizar%"))
			this.setMensaje("%Actualizar%");
		else if (partes[2].equals("%cerrar_conexion%"))
			this.setMensaje("%cerrar_conexion%");
		else if (partes[2].equals("%Solicitud_Conexion%"))
			this.setMensaje("%Solicitud_Conexion%");
		else if (partes[2].equals("%Usuarios%"))
			this.setMensaje("%Usuarios%");
		else if (partes[2].equals("%Imprimir%"))
			this.setMensaje("%Imprimir%");
		else
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

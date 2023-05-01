package src.main.resources.conectividad;

public class Mensaje {
	private String mensaje;
	private String estado;
	private String ip;
	private int puerto;
	
	
	public Mensaje(String mensaje, String estado) {
		super();
		this.mensaje = mensaje;
		this.estado = estado;
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

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPuerto() {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	
	
}

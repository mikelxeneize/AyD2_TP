package bean;

import java.net.InetAddress;

public class Notificacion {
	private String tipo;
	private Object mensaje;
	
	public Notificacion( Object mensaje, String tipo) {
		super();
		this.tipo = tipo;
		this.mensaje = mensaje;
	}
	public String getTipo() {
		return tipo;
	}
	public Object getMensaje() {
		return mensaje;
	}
	
	
	
	
	
}

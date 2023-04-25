package src.main.resources.backEnd;

import src.main.resources.conectividad.Cliente;

public class Nucleo {
	private Cliente cliente;
	private static Nucleo instance;
	
	
	
	
	public static Nucleo getInstance() {
		if (Nucleo.instance == null) {
			Nucleo.instance = new Nucleo();
		}
		return instance;
	}

	public void cerrarConexion() {
		
	}

}

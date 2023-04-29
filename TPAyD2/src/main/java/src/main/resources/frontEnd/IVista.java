package src.main.resources.frontEnd;

import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

public interface IVista {
	void addActionListener(ActionListener listener);

	void addWindowListener(WindowListener windowListener);
	void cerrar();
	
	// ----------------------MENSAJES------------------------//
	static final String INICIAR_CONEXION = "iniciar";
	static final String ENVIAR_MENSAJE = "enviar_mensaje";
	static final String MENUPRINCIPAL = "menu_principal";
	static final String CONFIGURACION = "configuracion";
	static final String CERRAR_CONEXION = "terminar_conversacion";
	
}

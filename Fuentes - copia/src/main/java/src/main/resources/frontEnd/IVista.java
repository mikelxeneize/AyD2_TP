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
	static final String ACEPTAR = "aceptar";
	static final String CANCELAR = "cancelar";
	static final String CONFIGURACION = "configuracion";
	static final String CERRAR_CONEXION = "terminar_conversacion";
	static final String TITULO_VENTANA = "Pepe-Chat";
	static final String SERVER_ERROR = "No se puedo establecer conexion con el servidor";
	
}

package frontEnd;

import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

public interface IVista {
	void addActionListener(ActionListener listener);

	void addWindowListener(WindowListener windowListener);
	void cerrar();
	
	// ----------------------MENSAJES------------------------//
	
}

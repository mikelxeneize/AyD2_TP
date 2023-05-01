package src.main.resources.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import src.main.resources.backEnd.Nucleo;
import src.main.resources.conectividad.Mensaje;
import src.main.resources.frontEnd.IVista;
import src.main.resources.frontEnd.VentanaConversacion;

public class ControladorConversacion implements ActionListener, Observer {
	private VentanaConversacion vista = null;
	private Nucleo modelo;
	
	public ControladorConversacion() {
		this.vista = new VentanaConversacion();
		this.modelo = Nucleo.getInstance();
		this.vista.addActionListener(this);

		this.modelo.addObserver(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals(IVista.CERRAR_CONEXION)) {
			this.vista.cerrar();
			ControladorMenuPrincipal controladorMenuPrincipal = new ControladorMenuPrincipal();
			this.modelo.cerrarConexion();
		}
		else if(e.getActionCommand().equals(IVista.ENVIAR_MENSAJE)) {
			String mensaje = this.vista.getMensaje();
			this.vista.recibirMensaje("Usted :" +mensaje);
			 if (! mensaje.isEmpty()){ //solo envia el mensaje si no esta vacio
					try {
						this.modelo.enviarMensaje(mensaje);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					this.vista.setInputChat("");
			 }
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Mensaje) {
			Mensaje datos = (Mensaje) arg;
			if(datos.getEstado().equals("mensaje recibido"))
				this.vista.recibirMensaje("Colocar ip aqui :" +datos.getMensaje());
			if(datos.getEstado().equals("conexion cerrada")) {
				this.vista.cerrar();
				this.modelo.deleteObserver(this);
				this.modelo.cerrarConexion();
				ControladorMenuPrincipal controladorMenuPrincipal = new ControladorMenuPrincipal();
			}
		
		}
	}
}

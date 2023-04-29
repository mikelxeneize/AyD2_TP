package src.main.resources.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import src.main.resources.backEnd.Nucleo;
import src.main.resources.frontEnd.IVista;
import src.main.resources.frontEnd.VentanaConversacion;

public class ControladorConversacion implements ActionListener, Observer {
	private VentanaConversacion vista = null;
	private Nucleo modelo;
	
	public ControladorConversacion() {
		this.vista = new VentanaConversacion();
		this.modelo = Nucleo.getInstance();
		this.vista.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals(IVista.CERRAR_CONEXION)) {
			this.vista.cerrar();
			ControladorMenuPrincipal controladorMenuPrincipal = new ControladorMenuPrincipal();
			this.modelo.cerrarConexion();
		}
		else if(e.getActionCommand().equals(IVista.ENVIAR_MENSAJE)) {
			String mensaje = this.vista.getMensaje();
			 if (! mensaje.isEmpty()){ //solo envia el mensaje si no esta vacio
					this.modelo.enviarMensaje(mensaje);
			 }
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		String accionObserver = this.modelo.getAccionObserver();
		if (accionObserver.equals(this.modelo.ENVIAR_MENSAJE)){
			this.vista.vaciarMensaje();
		} else if (accionObserver.equals(this.modelo.RECIBIR_MENSAJE)){
			String mensajeRecibido = (String) arg;
			this.vista.recibirMensaje(mensajeRecibido);
		} else if (accionObserver.equals(this.modelo.CERRAR_CONEXION)){
			this.vista.cerrar();
			ControladorMenuPrincipal controladorMenuPrincipal = new ControladorMenuPrincipal();
		}
	}
}

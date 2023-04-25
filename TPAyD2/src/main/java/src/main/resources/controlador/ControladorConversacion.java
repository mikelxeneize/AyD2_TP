package src.main.resources.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import src.main.resources.backEnd.Nucleo;
import src.main.resources.frontEnd.IVista;
import src.main.resources.frontEnd.VentanaConversacion;
import src.main.resources.frontEnd.VentanaMenuPrincipal;

public class ControladorConversacion implements ActionListener{
	private VentanaConversacion vista = null;
	private Nucleo modelo;
	
	public ControladorConversacion() {
		this.vista = new VentanaConversacion();
		this.modelo = Nucleo.getInstance();
		this.vista.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals(IVista.TERMINAR_CONVERSACION)) {
			this.vista.cerrar();
			ControladorMenuPrincipal controladorMenuPrincipal = new ControladorMenuPrincipal();
			this.modelo.cerrarConexion();
		}
		else if(e.getActionCommand().equals(IVista.ENVIAR_MENSAJE)) {
			
			//ACA SE CONECTA CON EL MODULO CONECTIVIDAD
			//Nucleo.getInstance().enviarMensaje();
		}
		
		
	}
}

package src.main.resources.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import src.main.resources.backEnd.Nucleo;
import src.main.resources.frontEnd.IVista;
import src.main.resources.frontEnd.VentanaMenuPrincipal;

public class ControladorMenuPrincipal implements ActionListener{
	private VentanaMenuPrincipal vista = null;
	private Nucleo modelo;
	
	public ControladorMenuPrincipal() {
		this.vista = new VentanaMenuPrincipal();
		this.modelo = Nucleo.getInstance();
		this.vista.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(IVista.CONFIGURACION)) {
			this.vista.cerrar();
			ControladorConfiguracion controladorConfiguracion = new ControladorConfiguracion();
			
		}
		else if(e.getActionCommand().equals(IVista.INICIAR_CONVERSACION)) {
			this.vista.cerrar();
			ControladorConversacion controladorConversacion = new ControladorConversacion();
			
		}
		
		
	}	
}

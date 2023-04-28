package src.main.resources.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import src.main.resources.backEnd.Nucleo;
import src.main.resources.frontEnd.IVista;
import src.main.resources.frontEnd.VentanaConfiguracion;

import static java.lang.Long.parseLong;

public class ControladorConfiguracion implements ActionListener{

	private VentanaConfiguracion vista = null;
	private Nucleo modelo;
	
	public ControladorConfiguracion() {
		this.vista = new VentanaConfiguracion();
		this.modelo = Nucleo.getInstance();
		this.vista.addActionListener(this);
		this.vista.cargarConfiguracion(this.modelo.getIp(), Integer.toString(this.modelo.getPort()));
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals(IVista.MENUPRINCIPAL)) {
			this.modelo.setConfiguracion(this.vista.getIp(), Integer.parseInt(this.vista.getPort()));
			this.modelo.persistirConfiguracion();
			this.vista.cerrar();
			ControladorMenuPrincipal controladorMenuPrincipal = new ControladorMenuPrincipal();
		}
		
		
	}
	
}

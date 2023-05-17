package src.main.resources.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import org.json.simple.parser.ParseException;

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
		try {
			this.modelo.desactivarEscucha();
		} catch (IOException e) {
			// no deebria pasar jamas
			e.printStackTrace();
		}
		this.vista.cargarConfiguracion(this.modelo.getIp(), Integer.toString(this.modelo.getPort()));
		this.vista.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if(actionCommand.equals(IVista.ACEPTAR)) {	
			this.modelo.setConfiguracion(this.vista.getIp(), Integer.parseInt(this.vista.getPort()));
			try {
				this.modelo.persistirConfiguracion();
				this.vista.ocultarLabelError();
				this.vista.cerrar();
				ControladorMenuPrincipal controladorMenuPrincipal = new ControladorMenuPrincipal();
			} catch (IOException e1) {
				this.vista.setTextlabelError("No se pudo guardar la configuracion");
				this.vista.mostrarLabelError();
			}
		}else if (actionCommand.equals(IVista.CANCELAR)) {
			this.vista.ocultarLabelError();
			this.vista.cerrar();
			try {
				ControladorMenuPrincipal controladorMenuPrincipal = new ControladorMenuPrincipal();
			} catch (IllegalArgumentException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	} 
	
}

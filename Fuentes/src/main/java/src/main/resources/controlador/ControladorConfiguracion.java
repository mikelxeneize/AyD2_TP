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
		this.vista.cargarConfiguracion(this.modelo.getIp(), Integer.toString(this.modelo.getPort()));
		this.vista.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		
		if(actionCommand.equals(IVista.ACEPTAR)) {	
			String ipVentana = this.vista.getIp();
			int portVentana = Integer.parseInt(this.vista.getPort());
			int portModelo = this.modelo.getPort();
			
			if (portVentana != portModelo) { // valida si se realizo algun cambio
				try {
					this.modelo.cerrarConexion();
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
				this.modelo.setConfiguracion(ipVentana, portVentana);
				try {
					Nucleo.getInstance().iniciarNucleo();
					this.vista.ocultarLabelError();
					this.vista.cerrar();
					try {
						ControladorMenuPrincipal controladorMenuPrincipal = new ControladorMenuPrincipal();
					} catch (IllegalArgumentException | IOException e1) {
						e1.printStackTrace();
					}
				} catch (IOException | ParseException e1) {
					this.vista.setTextlabelError(IVista.SERVER_ERROR);
					this.vista.mostrarLabelError();
					e1.printStackTrace();
				}
			}else { // vuelve al menu sin realizar cambios
				this.vista.ocultarLabelError();
				this.vista.cerrar();
				try {
					ControladorMenuPrincipal controladorMenuPrincipal = new ControladorMenuPrincipal();
				} catch (IllegalArgumentException | IOException e1) {
					e1.printStackTrace();
				}
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

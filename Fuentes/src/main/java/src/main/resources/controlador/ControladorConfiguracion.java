package src.main.resources.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import src.main.resources.backEnd.Nucleo;
import src.main.resources.frontEnd.IVista;
import src.main.resources.frontEnd.VentanaConfiguracion;
import src.main.resources.frontEnd.VentanaMenuPrincipal;

import static java.lang.Long.parseLong;

public class ControladorConfiguracion implements ActionListener{

	private VentanaConfiguracion vista = null;
	private Nucleo modelo;
	
	public ControladorConfiguracion(IVista vista2) {
		this.vista = new VentanaConfiguracion(this.vista);
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
				this.modelo.setConfiguracion(this.vista.getIp(), Integer.parseInt(this.vista.getPort()));
				this.vista.ocultarLabelError();
				try {
					this.modelo.establecerConexionConElServidor();
					try {
						ControladorMenuPrincipal controladorMenuPrincipal = new ControladorMenuPrincipal(this.vista);
					} catch (IllegalArgumentException | IOException e1) {
						e1.printStackTrace();
					}
					this.vista.cerrar();
				} catch (IllegalArgumentException | IOException e1) {
					this.vista.setTextlabelError(IVista.SERVER_ERROR);
					this.vista.mostrarLabelError();
					e1.printStackTrace();
			}
			}else { // vuelve al menu sin realizar cambios
				this.vista.ocultarLabelError();
				this.vista.cerrar();
				try {
					ControladorMenuPrincipal controladorMenuPrincipal = new ControladorMenuPrincipal(this.vista);
				} catch (IllegalArgumentException | IOException e1) {
					e1.printStackTrace();
				}
				this.vista.cerrar();
			}
		}else if (actionCommand.equals(IVista.CANCELAR)) {
			this.vista.ocultarLabelError();
			this.vista.cerrar();
			try {
				ControladorMenuPrincipal controladorMenuPrincipal = new ControladorMenuPrincipal(this.vista);
			} catch (IllegalArgumentException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	} 
	
}

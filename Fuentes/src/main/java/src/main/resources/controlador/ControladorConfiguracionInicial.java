package src.main.resources.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.json.simple.parser.ParseException;

import src.main.resources.backEnd.Nucleo;
import src.main.resources.frontEnd.IVista;
import src.main.resources.frontEnd.VentanaConfiguracion;
import src.main.resources.frontEnd.VentanaConfiguracionInicial;

import static java.lang.Long.parseLong;

public class ControladorConfiguracionInicial implements ActionListener{

	private VentanaConfiguracionInicial vista = null;
	private Nucleo modelo;
	
	public ControladorConfiguracionInicial(IVista vista2) {
		String ip;
		this.vista = new VentanaConfiguracionInicial(vista2);
		try {
			InetAddress localHost;
			localHost = InetAddress.getLocalHost();
			ip = localHost.getHostAddress();
		} catch (UnknownHostException e) {
			ip = "localhost";
			e.printStackTrace();
		}
		this.vista.setIp(ip);
		this.modelo = Nucleo.getInstance();
		
		this.vista.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if(actionCommand.equals(IVista.ACEPTAR)) {	
			this.modelo.setConfiguracion(this.vista.getIp(), Integer.parseInt(this.vista.getPort()), this.vista.getUsername());
			this.vista.ocultarLabelError();
			try {
				this.modelo.conectarServidorPrincipal();
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
			
		}
	} 
	
}

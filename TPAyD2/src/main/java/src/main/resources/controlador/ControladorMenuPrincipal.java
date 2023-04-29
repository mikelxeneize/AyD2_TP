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

	//valida que el puerto sea un numero y se encarga de castearlo
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(IVista.CONFIGURACION)) {
			this.vista.cerrar();
			ControladorConfiguracion controladorConfiguracion = new ControladorConfiguracion();
		} else if(e.getActionCommand().equals(IVista.INICIAR_CONVERSACION)) {
			String ipDestino = this.vista.getIpDestino();
			int puertoDestino = 0;
			try {
				puertoDestino = Integer.parseInt(this.vista.getPortDestino());
			} catch (NumberFormatException ex) {
				//TO-DO aca habria q notificar en pantalla lo de q no es un numero, podriamos validar rangos puertos tmb
			}
			try {
				this.modelo.iniciarConexion(ipDestino, puertoDestino);
				} catch (RuntimeException ex){
				//  TO-DO esto tendria que tener un catch para hacer un label que notifique
			}
			this.vista.cerrar(); //TO-DO esto se ejecuta despues de salir del catch?
			ControladorConversacion controladorConversacion = new ControladorConversacion();
		}


	}
}

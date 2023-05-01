package src.main.resources.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import src.main.resources.backEnd.Nucleo;
import src.main.resources.conectividad.Mensaje;
import src.main.resources.frontEnd.IVista;
import src.main.resources.frontEnd.VentanaMenuPrincipal;

public class ControladorMenuPrincipal implements ActionListener, Observer {
	private VentanaMenuPrincipal vista = null;
	private Nucleo modelo;

	public ControladorMenuPrincipal() {
		this.vista = new VentanaMenuPrincipal();
		this.modelo = Nucleo.getInstance();
		this.vista.addActionListener(this);

		this.modelo.addObserver(this);
	}

	//valida que el puerto sea un numero y se encarga de castearlo
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(IVista.CONFIGURACION)) {
			this.vista.cerrar();
			ControladorConfiguracion controladorConfiguracion = new ControladorConfiguracion();
		} else if(e.getActionCommand().equals(IVista.INICIAR_CONEXION)) {
			String ipDestino = this.vista.getIpDestino();
			int puertoDestino = 0;
			try {
				puertoDestino = Integer.parseInt(this.vista.getPortDestino());
				this.modelo.iniciarConexion(ipDestino, puertoDestino);
				this.vista.cerrar(); //TO-DO esto se ejecuta despues de salir del catch?
				ControladorConversacion controladorConversacion = new ControladorConversacion();
				this.vista.mostrarLabelErrorAlConectar(false);
				
			} catch (NumberFormatException ex) {
				this.vista.setTextErrorlabelTexto("Formato de puerto invalido");
				this.vista.mostrarLabelErrorAlConectar(true);
			} catch (IOException e1) {
				this.vista.setTextErrorlabelTexto("La conexion fue rechazada. Revisar el ip y puerto ingresados");
				this.vista.mostrarLabelErrorAlConectar(true);
			}
			 catch (
				IllegalArgumentException e1) {
				this.vista.setTextErrorlabelTexto("Rango de puerto invalido");
				this.vista.mostrarLabelErrorAlConectar(true);
			 }
			
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Mensaje) {
			Mensaje datos = (Mensaje) arg;
			if(datos.getEstado().equals("conexion establecida")) {
				this.vista.cerrar();
				this.modelo.deleteObserver(this);
				ControladorConversacion controladorConversacion = new ControladorConversacion();
			}
		}
	}
}

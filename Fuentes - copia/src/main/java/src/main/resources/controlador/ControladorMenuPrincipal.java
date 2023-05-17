package src.main.resources.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import org.json.simple.parser.ParseException;

import src.main.resources.backEnd.Nucleo;
import src.main.resources.conectividad.Mensaje;
import src.main.resources.frontEnd.IVista;
import src.main.resources.frontEnd.VentanaMenuPrincipal;

public class ControladorMenuPrincipal implements ActionListener, Observer {
	private VentanaMenuPrincipal vista = null;
	private Nucleo modelo;

	public ControladorMenuPrincipal() throws UnknownHostException, IllegalArgumentException, IOException {
		this.vista = new VentanaMenuPrincipal();
		this.modelo = Nucleo.getInstance();
		this.vista.addActionListener(this);
		try {
			Nucleo.getInstance().cargarConfiguracion();
		} catch (IOException | ParseException e) {
			this.vista.setTextlabelError("No se pudo cargar el archivo de configuracion .Ir a configuracion");
			this.vista.mostrarLabelErrorAlConectar(true);
		}
		Nucleo.getInstance().activarEscucha();
		this.vista.mostrarLabelErrorAlConectar(false);
		this.modelo.addObserver(this);
	}

	//valida que el puerto sea un numero y se encarga de castearlo
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(IVista.CONFIGURACION)) {
			this.vista.cerrar();
			ControladorConfiguracion controladorConfiguracion = new ControladorConfiguracion();
		} else if(e.getActionCommand().equals(IVista.INICIAR_CONEXION)) {
			String ipDestino = this.vista.getIpDestino();
			int puertoDestino;
			try {
				puertoDestino = Integer.parseInt(this.vista.getPortDestino());
				this.modelo.iniciarConexion(ipDestino, puertoDestino);
				this.vista.cerrar(); 
				ControladorConversacion controladorConversacion = new ControladorConversacion();
				this.vista.mostrarLabelErrorAlConectar(false);
				
			} catch (NumberFormatException ex) {
				this.vista.setTextlabelError("Formato de puerto invalido");
				this.vista.mostrarLabelErrorAlConectar(true);
			} catch (IOException e1) { //esta cachea el UnknownHostException tmb
				this.vista.setTextlabelError("La conexion fue rechazada. Revisar el ip y puerto ingresados");
				this.vista.mostrarLabelErrorAlConectar(true);
			} catch (
				IllegalArgumentException e1) {
				this.vista.setTextlabelError("Rango de puerto invalido");
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
			}else if (datos.getEstado().equals("error al escuchar")) {
				this.vista.setTextlabelError("No estas en modo escucha, tu puerto esta ocupado \nIr a configuracion");
				this.vista.mostrarLabelErrorAlConectar(true);
			}
		}
	}
}

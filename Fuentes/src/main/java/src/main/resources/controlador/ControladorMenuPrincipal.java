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
import src.main.resources.conectividad.PingEchoHilo;
import src.main.resources.frontEnd.IVista;
import src.main.resources.frontEnd.VentanaConfiguracionInicial;
import src.main.resources.frontEnd.VentanaMenuPrincipal;

public class ControladorMenuPrincipal implements ActionListener, Observer {
	private VentanaMenuPrincipal vista = null;
	private Nucleo modelo;

	public ControladorMenuPrincipal(IVista vista2) throws UnknownHostException, IllegalArgumentException, IOException {
		this.vista = new VentanaMenuPrincipal(vista2);
		this.modelo = Nucleo.getInstance();
		this.vista.addActionListener(this);
		try {
			Nucleo.getInstance().cargarConfiguracion();
		} catch (IOException | ParseException e) {
			this.vista.setTextlabelError("No se pudo cargar el archivo de configuracion .Ir a configuracion");
			this.vista.mostrarLabelErrorAlConectar(true);
		}
		this.vista.mostrarLabelErrorAlConectar(false);
		MostrarPingEchoHilo pingechohilo= new MostrarPingEchoHilo(this,this.vista);
		pingechohilo.start();
		this.modelo.addObserver(this);
	} 

	// valida que el puerto sea un numero y se encarga de castearlo
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(IVista.CONFIGURACION)) {
			ControladorConfiguracion controladorConfiguracion = new ControladorConfiguracion(this.vista);
			this.vista.cerrar();
			this.modelo.deleteObserver(this);
		} else if (e.getActionCommand().equals(IVista.INICIAR_CONEXION)) {
			String ipDestino = this.vista.getIpDestino();
			int puertoDestino;
			try {
				puertoDestino = Integer.parseInt(this.vista.getPortDestino());
				this.modelo.iniciarConexion(ipDestino, puertoDestino);

			} catch (NumberFormatException ex) {
				this.vista.setTextlabelError("Formato de puerto invalido");
				this.vista.mostrarLabelErrorAlConectar(true);
			} catch (IOException e1) { // esta cachea el UnknownHostException tmb
				this.vista.setTextlabelError("La conexion fue rechazada. Revisar el ip y puerto ingresados");
				this.vista.mostrarLabelErrorAlConectar(true);
			} catch (IllegalArgumentException e1) {
				this.vista.setTextlabelError("Rango de puerto invalido");
				this.vista.mostrarLabelErrorAlConectar(true);
			}

		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Mensaje) {
			Mensaje datos = (Mensaje) arg;
			if (datos.getEstado().equals("Actualizar")) {
					this.vista.recibirConectado(this.modelo.getConectados());
				}
			
			else if (datos.getEstado().equals("Conexion_rechazada")) {
				this.vista.setTextlabelError("La conexion fue rechazada. Revisar el ip y puerto ingresados");
				this.vista.mostrarLabelErrorAlConectar(true);
			}
			
			else if (datos.getEstado().equals("conexion establecida")) {
				
				ControladorConversacion controladorConversacion = new ControladorConversacion(this.vista);
				this.modelo.deleteObserver(this);
				this.vista.cerrar();
				this.vista.mostrarLabelErrorAlConectar(false);
			} 
			
			else if (datos.getEstado().equals("error al escuchar")) {
				this.vista.setTextlabelError("No estas en modo escucha, tu puerto esta ocupado \nIr a configuracion");
				this.vista.mostrarLabelErrorAlConectar(true);
			} 
			
			else if (datos.getEstado().equals("informacion servidor secundario")) {
			//pepe	
				
			}
			
		} 
	}
	public long getPingEcho() {
		return this.modelo.getConectividad().getPingEchoTime();
	}
}

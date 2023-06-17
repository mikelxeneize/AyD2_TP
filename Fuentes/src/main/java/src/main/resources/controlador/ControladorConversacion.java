package src.main.resources.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import src.main.resources.backEnd.Nucleo;
import src.main.resources.conectividad.Mensaje;
import src.main.resources.frontEnd.IVista;
import src.main.resources.frontEnd.VentanaConversacion;
import src.main.resources.frontEnd.VentanaMenuPrincipal;

public class ControladorConversacion implements ActionListener, Observer {
	private VentanaConversacion vista = null;
	private Nucleo modelo;
	
	public ControladorConversacion(IVista vista2) {
		this.vista = new VentanaConversacion(vista2);
		this.modelo = Nucleo.getInstance();
		this.vista.addActionListener(this);
		this.vista.recibirMensaje(">>Bienvenido a Pepe Chat!");
		this.vista.recibirMensaje(">>Conexion establecida ");
		this.modelo.addObserver(this);
		this.vista.setTextConectadoCon("Ip: "+this.modelo.getInstance().getConectividad().getIpReceptor() + "  Puerto: "+this.modelo.getInstance().getConectividad().getPuertoReceptor());
		
		this.vista.setTitle(this.vista.getTitle() + " - " +this.modelo.getUsername());
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals(IVista.CERRAR_CONEXION)) {
			try {
				this.modelo.cerrarConexion();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("MANDE PARA CERRAR Y ME ROMPI BEBEEEEE");
			}
			try {
				ControladorMenuPrincipal controladorMenuPrincipal = new ControladorMenuPrincipal(this.vista);
				this.modelo.deleteObserver(this);
			} catch (IllegalArgumentException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.vista.cerrar();
		}
		else if(e.getActionCommand().equals(IVista.ENVIAR_MENSAJE)) {
			String mensaje = this.vista.getMensaje();
			this.vista.recibirMensaje("Usted :" +mensaje);
			 if (! mensaje.isEmpty()){ //solo envia el mensaje si no esta vacio
					try {
						this.modelo.enviarMensaje(mensaje);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					this.vista.setInputChat("");
			 }
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Mensaje) {
			Mensaje datos = (Mensaje) arg;
			if(datos.getEstado().equals("mensaje recibido")) {
				this.vista.recibirMensaje("["+datos.getIp()+"]:  " +datos.getMensaje());
			} else if(datos.getEstado().equals("conexion cerrada")) {
				this.modelo.deleteObserver(this);
				try {
					this.modelo.cerrarConexion();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					ControladorMenuPrincipal controladorMenuPrincipal = new ControladorMenuPrincipal(this.vista);
					this.modelo.deleteObserver(this);
				} catch (IllegalArgumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.vista.cerrar();
			} else if(datos.getEstado().equals("te cerraron la conexion papirrin")) {
				this.modelo.deleteObserver(this);
				try {
					ControladorMenuPrincipal controladorMenuPrincipal = new ControladorMenuPrincipal(this.vista);
					this.modelo.deleteObserver(this);
				} catch (IllegalArgumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.vista.cerrar();
			} else if(datos.getEstado().equals("conexion establecida")) {
				this.vista.setTextConectadoCon("Ip: "+this.modelo.getInstance().getConectividad().getIpReceptor() + "  Puerto: "+this.modelo.getInstance().getConectividad().getPuertoReceptor());
			} else if(datos.getEstado().equals("servidor desconectado")){
				System.out.println("servidor desconectado");
			}
				
		}
	}
}

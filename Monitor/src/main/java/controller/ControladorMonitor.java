package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import bean.Cliente;
import bean.Notificacion;
import bean.ServerData;
import frontEnd.VentanaMonitor;
import monitor.Monitor;
import util.INotificacion;

@SuppressWarnings("deprecation")
public class ControladorMonitor implements ActionListener,Observer,INotificacion{

	private VentanaMonitor vista = null;
	private Monitor modelo;
	
	public ControladorMonitor() {
		this.vista = new VentanaMonitor();
		try {
			this.modelo = Monitor.getInstance();
			this.modelo.addObserver(this);
			//inicia el ping
			MostrarPingEchoHilo pingechohilo= new MostrarPingEchoHilo(this,this.vista);
			pingechohilo.start();
			
			this.modelo.inicializarMonitor();

			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.vista.addActionListener(this);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}

	public void update(Observable o, Object arg) {
		Notificacion notificacion = (Notificacion) arg;
		if (notificacion.getTipo().equals(TIPO1)) {
			this.vista.addLogger((String) notificacion.getMensaje());
		}
		else if (notificacion.getTipo().equals(TIPO2)) {
			this.vista.setListaClientes((ArrayList<Cliente>)notificacion.getMensaje());
		}
		else if (notificacion.getTipo().equals(TIPO3)) {
			this.vista.addRowListaServidores((ServerData)notificacion.getMensaje());
		}
		else if (notificacion.getTipo().equals(TIPO4)) {
			this.vista.removeRowListaServidores((ServerData)notificacion.getMensaje());
		}
		else if (notificacion.getTipo().equals(TIPO5)) {
			this.vista.modifyRowListaServidores((ServerData)notificacion.getMensaje());
		}
	}

	public ArrayList<ServerData> getPingEcho() {
		return this.modelo.getListaServidores();
	} 
	
}

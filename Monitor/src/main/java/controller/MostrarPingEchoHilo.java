package controller;

import java.util.ArrayList;

import bean.ServerData;
import frontEnd.VentanaMonitor;

public class MostrarPingEchoHilo extends Thread {
	VentanaMonitor vista;
	ControladorMonitor controladorMonitor;
	
	
	public MostrarPingEchoHilo(ControladorMonitor controladorMonitor,VentanaMonitor vista ) {
		this.controladorMonitor= controladorMonitor;
		this.vista=vista;
	}
	
	
	@Override
	public void run() {
		int tiempo=1000; //tiempo en milisegundos 
		
		while(true) {
			try {
				Thread.sleep(tiempo);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ArrayList<ServerData> ping=this.controladorMonitor.getPingEcho();
			
			this.vista.setPingTabla(ping);
			
			
		}

	}
}

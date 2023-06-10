package src.main.resources.controlador;

import src.main.resources.frontEnd.VentanaMenuPrincipal;

public class MostrarPingEchoHilo extends Thread {
	VentanaMenuPrincipal vista;
	ControladorMenuPrincipal controladorMenuPrincipal;
	
	public MostrarPingEchoHilo(ControladorMenuPrincipal controladorMenuPrincipal,VentanaMenuPrincipal vista ) {
		this.controladorMenuPrincipal= controladorMenuPrincipal;
		this.vista=vista;
	}
	static final String ROJO ="Rojo";
	static final String VERDE ="Verde";
	static final String AMARILLO ="Amarillo";
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
			Long ping=this.controladorMenuPrincipal.getPingEcho();
			
			
			this.vista.setLblPingEchoValor( String.valueOf(ping));
			if(ping<200)
				this.vista.setLblPingEchoColor(VERDE);
			else if( ping <500)
				this.vista.setLblPingEchoColor(AMARILLO);
			else 
				this.vista.setLblPingEchoColor(ROJO);
			
			
		}

	}
}

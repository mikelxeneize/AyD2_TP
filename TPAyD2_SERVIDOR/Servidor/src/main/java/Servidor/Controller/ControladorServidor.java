package Servidor.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import Servidor.FrontEnd.VentanaServidor;
import Servidor.Servidor.Servidor;


public class ControladorServidor implements ActionListener{

	private VentanaServidor vista = null;
	private Servidor modelo;
	
	public ControladorServidor() {
		this.vista = new VentanaServidor();
		try {
			this.modelo = Servidor.getInstance();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.vista.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		
	} 
	
}

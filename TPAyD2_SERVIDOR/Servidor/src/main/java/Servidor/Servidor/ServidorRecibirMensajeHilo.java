package Servidor.Servidor;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class ServidorRecibirMensajeHilo extends Thread {
    private Cliente cliente;
	private Servidor servidor;

    public ServidorRecibirMensajeHilo(Cliente cliente, Servidor servidor){
        this.cliente=cliente;
        this.servidor=servidor;
    }

    @Override
    public void run() {
    	String msg = null;
		MensajeEncriptado mensaje;
		MensajeEncriptado mensajeAReceptor;
    	do {
	        BufferedReader in = null;
	            try {
					in = new BufferedReader(new InputStreamReader(cliente.getSocket().getInputStream()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	        
	        try {
				msg = in.readLine();
				
				System.out.println(msg);
				mensaje = new MensajeEncriptado(msg);	
				if (mensaje.getMensaje().equals("%cerrar_conexion%")) {
					//cambiarEstado("Disponible") y avisarle al otro que no hay mas charla;
					this.cliente.setEstado("Disponible");
					this.servidor.cortarConexionAReceptor(this.cliente.getIpReceptor(),this.cliente.getPuertoReceptor());
					this.cliente.setIpReceptor(null);
				}else {	
					if(this.cliente.getIpReceptor()==null ){
						mensajeAReceptor= new MensajeEncriptado( "%Solicitud_Conexion%", "",this.cliente.getIp(), this.cliente.getPuerto());
						this.servidor.iniciarConexionAReceptor(mensaje,mensajeAReceptor);
						this.cliente.setIpReceptor(mensaje.getIp());
						this.cliente.setPuertoReceptor(mensaje.getPuerto());
						this.cliente.setEstado("Ocupado");
					}else {
						this.servidor.enviarMensajeACliente(mensaje,this.cliente.getIpReceptor(),this.cliente.getPuertoReceptor());
					}
				}
			} catch (IOException e) {
				this.cliente.setEstado("Disponible");
				try {
					this.servidor.cortarConexionAReceptor(this.cliente.getIpReceptor(),this.cliente.getPuertoReceptor());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.cliente.setIpReceptor(null);
				msg=null;

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        
    	}while(msg != null); //implica que se cerro la conexion

    }
}

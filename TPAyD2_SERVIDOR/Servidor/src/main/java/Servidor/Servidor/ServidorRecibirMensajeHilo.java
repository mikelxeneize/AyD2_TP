package Servidor.Servidor;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServidorRecibirMensajeHilo extends Thread {
    private Socket socket;
	private Servidor servidor;

    public ServidorRecibirMensajeHilo(Socket socket, Servidor servidor){
        this.socket=socket;
        this.servidor=servidor;
    }

    @Override
    public void run() {
    	String msg = null;
		MensajeEncriptado mensaje;
    	do {
	        BufferedReader in = null;
	            try {
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	        
	        try {
				msg = in.readLine();
				
				System.out.println(msg);
				if (msg == null) {
					//cambiarEstado("Disponible") y avisarle al otro que no hay mas charla;
				}else {
					mensaje = new MensajeEncriptado(msg);	//Se parsea dentro del constructor
					
					if(mensaje.getMensaje()=="este es mi puerto")
						this.servidor.getInstance().agregarPuertoFalso(mensaje.getPuerto(),socket);
					else
						this.servidor.enviarMensajeACliente(mensaje);
				}
			} catch (IOException e) {
				System.out.println("excepcion, creo que se cerro la conexion");
			}
	        
    	}while(msg != null); //implica que se cerro la conexion

    }
}

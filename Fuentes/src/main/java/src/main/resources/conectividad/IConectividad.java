package src.main.resources.conectividad;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public interface IConectividad {

	
	public void iniciarConversacion(String ipserver, int puertoserver) throws UnknownHostException, IOException, IllegalArgumentException;
	public void recibirMensaje();
	public void notificarAccion(Mensaje mensaje);
	public void enviarMensaje(String mensajeaenviar) throws IOException;
	public void cerrarConexion() throws IOException;
}

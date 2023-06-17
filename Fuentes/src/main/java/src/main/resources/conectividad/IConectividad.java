package src.main.resources.conectividad;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public interface IConectividad {

	
	public void iniciarConversacion(String ipserver, int puertoserver) throws UnknownHostException, IOException, IllegalArgumentException;
	public void recibirMensaje();
	public void notificarAccion(Mensaje mensaje);
	public void enviarMensajeExterno(MensajeExterno mensajeExterno, serverData servidorData) throws IOException;
	public void enviarMensajeCliente(String mensaje) throws IOException;
	public void cerrarConexion() throws IOException;
}

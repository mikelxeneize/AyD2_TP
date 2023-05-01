package src.main.resources.conectividad;

import src.main.resources.backEnd.Nucleo;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Conectividad extends Observable{
	//socket si inicias la conversacion
    private Socket socket;
    //server socket si te inician la conversacion
    private ServerSocket serverSocket;
    
    //Informacion personal
    private int puertopersonal;
    private String ippersonal;
    
    private List<Observer> observers = new ArrayList<>();

    //Flag de si se encuentra en una conversacion
    private boolean conectado;
    private String estado="";
    
    
	public Conectividad(String ippersonal, int puertopersonal)  {
        this.puertopersonal=puertopersonal;
        this.ippersonal=ippersonal;
        this.conectado=false;
    }
	
	public List<Observer> getObservers() {
		return observers;
	}



	
    public void iniciarConexion(String ipserver, int puertoserver) throws  UnknownHostException, IOException, 
	IllegalArgumentException { // tiene que devolver una excepcion de no conexion
 
        this.socket=new Socket(ipserver,puertoserver);
        this.serverSocket.close();
        this.recibirMensaje();
        

    }

    
    
    public void escucharConexion(int puertopersonal){ // tiene que devolver una excepcion de no conexion
    	
    	EscucharConexionHilo escucharConexion= new EscucharConexionHilo(puertopersonal,this);
        escucharConexion.start();
        } //lamada a nucleo


    

    public void recibirMensaje() {
    	
    	
    	RecibirMensajeHilo recibirMensaje = new RecibirMensajeHilo(this.socket,this);
        recibirMensaje.start();
    }
    

	public void notificarAccion(Mensaje mensaje) {
		this.setChanged();
		this.notifyObservers(mensaje);
	}
	

	public void enviarMensaje(String mensajeaenviar) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(mensajeaenviar);
    }
	

    public void cerrarConexion() throws IOException{
            socket.close();
    }

    public int getPuertopersonal() {
        return puertopersonal;
    }

    public void setPuertopersonal(int puertopersonal) {
        this.puertopersonal = puertopersonal;
    }

    public String getIppersonal() {
        return ippersonal;
    }

    public void setIppersonal(String ippersonal) {
        this.ippersonal = ippersonal;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public boolean isConectado() {
        return conectado;
    }

	public void setSocket(Socket socket2) {
		this.socket=socket2;
	}

	public Socket getSocket() {
		return this.socket;
	}

	public void setServerSocket(ServerSocket serverSocket2) {
		this.serverSocket = serverSocket2;
	}

	public void desactivarEscucharConexion() throws IOException {
		this.serverSocket.close();
	}





}
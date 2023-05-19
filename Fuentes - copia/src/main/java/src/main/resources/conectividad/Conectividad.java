package src.main.resources.conectividad;

import src.main.resources.backEnd.Nucleo;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Conectividad extends Observable implements IConectividad{
	//socket si inicias la conversacion
    private Socket socket;
    //server socket si te inician la conversacion
    private ServerSocket serverSocket;
    
    //Informacion personal
    private int puertopersonal;
    private String ippersonal;

    //Informacion personal
    private int puertoReceptor;
    private String ipReceptor;
    
  //Informacion Servidor
    private int puertoServidor= 50100;
    private String ipServidor="localhost";
    
    private List<Observer> observers = new ArrayList<>();

    //Flag de si se encuentra en una conversacion
    private boolean conectado;
    private String estado="";
    
    
	public Conectividad()  {
        this.conectado=false;
    }
	
	public List<Observer> getObservers() {
		return observers;
	}



	
    public void iniciarConversacion(String ipcliente2, int puertocliente2) throws  UnknownHostException, IOException, 
	IllegalArgumentException { // tiene que devolver una excepcion de no conexion

    	this.ippersonal="localhost";
    	
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(ipcliente2 +":" + Integer.toString(puertocliente2)+ ":" + "%mensaje%" );
        
        this.ipReceptor=ipcliente2;
        this.puertoReceptor=puertocliente2;
        

    }

	

    public void iniciarConexionServidor() throws  UnknownHostException, IOException, IllegalArgumentException {
    	this.ippersonal="localhost";
        System.out.println("socket abierto servidor inicial");
        this.socket = new Socket();
    	this.socket.setReuseAddress(true); 
    	this.socket.bind(new InetSocketAddress(this.puertopersonal)); 
    	this.socket.connect(new InetSocketAddress(this.ipServidor, this.puertoServidor));
        
    	this.setSocket(socket);
        System.out.println("Socket conectado al servidor");

        this.recibirMensaje();
        
    }

    
    public void escucharConexion(int puertopersonal){ // tiene que devolver una excepcion de no conexion
    	EscucharConexionHilo escucharConexion= new EscucharConexionHilo(puertopersonal,this);
        escucharConexion.start();
        } //lamada a nucleo


    

    public int getPuertoReceptor() {
		return puertoReceptor;
	}

	public void setPuertoReceptor(int puertoReceptor) {
		this.puertoReceptor = puertoReceptor;
	}

	public String getIpReceptor() {
		return ipReceptor;
	}

	public void setIpReceptor(String ipReceptor) {
		this.ipReceptor = ipReceptor;
	}

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
        out.println(this.ipReceptor +":" + Integer.toString(this.puertoReceptor)+ ":" + mensajeaenviar );
    }
	

    public void cerrarConexion() throws IOException{
    	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(this.ipReceptor +":" + Integer.toString(this.puertoReceptor)+ ":" + "%cerrar_conexion%" );
		this.setIpReceptor(null);
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
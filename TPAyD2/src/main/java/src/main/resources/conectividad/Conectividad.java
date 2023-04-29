package src.main.resources.conectividad;

import src.main.resources.backEnd.Nucleo;

import java.io.*;
import java.net.*;

public class Conectividad {
    private Socket socket;
    private ServerSocket serverSocket;
    private int puertopersonal;
    private String ippersonal;

    private EscucharConexionHilo escucharConexion;

    private EnviarMensajeHilo enviarMensaje;
    RecibirMensajeHilo recibirMensaje;
    private boolean conectado;

	public Conectividad(String ippersonal, int puertopersonal)  {
        this.puertopersonal=puertopersonal;
        this.ippersonal=ippersonal;
        this.conectado=false;
    }

    public void iniciarConexion(String ipserver, int puertoserver) throws RuntimeException, UnknownHostException, IOException { // tiene que devolver una excepcion de no conexion
        
           this.socket=new Socket(ipserver,puertoserver);
           this.recibirMensaje();
    }

    public void escucharConexion(int puertopersonal) throws IOException { // tiene que devolver una excepcion de no conexion
        
        this.escucharConexion= new EscucharConexionHilo(puertopersonal);
        escucharConexion.start();
        } //lamada a nucleo


    public void enviarMensaje(String mensajeaenviar){
       this.enviarMensaje =new EnviarMensajeHilo(this.socket,mensajeaenviar);
       this.enviarMensaje.start();
    }

    public void recibirMensaje() {
        this.recibirMensaje = new RecibirMensajeHilo(this.socket);
            this.recibirMensaje.start();
    }


        public void cerrarConexion(){
        try {
            enviarMensaje("conexion cerrada");
            socket.close();
            serverSocket.close(); // lo puse por las dudas, puede traer errores
            escucharConexion(puertopersonal);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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


}
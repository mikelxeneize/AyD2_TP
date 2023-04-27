package src.main.resources.conectividad;

import java.net.*;

public class Cliente {
    private Socket socket;
    private int puertopersonal;
    private String ippersonal;

	public Cliente (String ippersonal, int puertopersonal){
        this.puertopersonal=puertopersonal;
        this.ippersonal=ippersonal;
    }

    public void iniciarConexion(String ipserver, int puertoserver){ // tiene que devolver una excepcion de no conexion
        try {
            socket=new Socket(ipserver,puertoserver);
        }
        catch ();
    }

    public void enviarMensaje(String mensajeaenviar){
        new Thread(){
            public void run(){
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // enviar mensaje
                out.println(mensajeaenviar);
            }
        }.start();
    }

    public String recibirMensaje(){
        new Thread(){
            public void run(){
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// reibir mensaje
                return in.readLine();
            }
        }.start();
    }

    public void cerrarConexion(String ipserver, int puertoserver){
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
}

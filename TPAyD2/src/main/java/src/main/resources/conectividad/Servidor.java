package src.main.resources.conectividad;

import java.net.*;

public class Servidor {
    private ServerSocket serverSocket;
    private int puertopersonal;
    private String ippersonal;

    public Servidor (String ippersonal, int puertopersonal){
        this.puertopersonal=puertopersonal;
        this.ippersonal=ippersonal;
    }

    public void iniciarConexion(int puertocliente){ // tiene que devolver una excepcion de no conexion
        serverSocket = new ServerSocket(puertocliente);
        while (true){
            Socket socket
        }

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

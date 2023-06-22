package Servidor.Util;

public interface IComandos {
	static final String NOMBRE_USUARIO= "nombre_usuario";
	static final String CERRAR_CONVERSACION="cerrar_conexion";
	static final String PING_ECHO = "Ping_Echo";
	static final String RESPUESTA_PING_ECHO = "Respuesta_Ping_Echo";
	static final String INICIAR_CONVERSACION = "Iniciar_Convesacion";
	static final String CONEXION_ESTABLECIDA = "Conexion_establecida";
	static final String CONEXION_RECHAZADA = "Conexion_rechazada";
	static final String ACTUALIZAR_LISTA = "Actualizar_lista";
	public static final String ACTUALIZACION_DIRECTORIO = "ACTUALIZACION_DIRECTORIO";
	static final String PEDIR_LISTA = "Pedir_lista";
	static final String LISTA_COMPLETA = "Lista_completa";
    public static final String INDEFINIDO = " ";
    public static final String CONFIRMACION_CLIENTE = "CONFIRMACION_CLIENTE";
    public static final String CONFIRMACION_SERVIDOR = "CONFIRMACION_SERVIDOR";
    public static final String CONFIRMACION_MONITOR = "CONFIRMACION_MONITOR";
    public static final String CONFIRMACION_CLIENTE_RESPUESTA = "CONFIRMACION_CLIENTE_RESPUESTA";
    public static final String CONFIRMACION_SERVIDOR_RESPUESTA = "CONFIRMACION_SERVIDOR_RESPUESTA";
    public static final String CONFIRMACION_MONITOR_RESPUESTA = "CONFIRMACION_MONITOR_RESPUESTA";
    public static final String CLIENTE = "CLIENTE";
    public static final String SERVIDOR = "SERVIDOR";
    public static final String MONITOR = "MONITOR";
}

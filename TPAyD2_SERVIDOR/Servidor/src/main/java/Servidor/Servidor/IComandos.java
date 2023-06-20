package Servidor.Servidor;

public interface IComandos {
	static final String NOMBRE_USUARIO= "nombre_usuario";
	static final String CERRAR_CONVERSACION="cerrar_conexion";
	static final String PING_ECHO = "Ping_Echo";
	static final String RESPUESTA_PING_ECHO = "Respuesta_Ping_Echo";
	static final String INICIAR_CONVERSACION = "Iniciar_Convesacion";
	static final String CONEXION_ESTABLECIDA = "Conexion_establecida";
	static final String CONEXION_RECHAZADA = "Conexion_rechazada";
	static final String ACTUALIZAR_LISTA = "Actualizar_lista";
	public static final String INICIAR_CONEXION_SERVIDOR = "INICIAR_CONEXION_SERVIDOR";
	public static final String INICIAR_CONEXION_SERVIDOR_CONFIRMACION = "INICIAR_CONEXION_SERVIDOR_CONFIRMACION";
	public static final String ACTUALIZACION_DIRECTORIO = "ACTUALIZACION_DIRECTORIO";
	static final String PEDIR_LISTA = "Pedir_lista";
	static final String LISTA_COMPLETA = "Lista_completa";
    public static final String INDEFINIDO = " ";
}

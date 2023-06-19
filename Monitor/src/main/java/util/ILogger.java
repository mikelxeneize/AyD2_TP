package util;

public interface ILogger {
	static final String INFO = "[INFO]: ";
	static final String WARN = "[WARN]: ";
	static final String FATAL = "[FATAL]: ";
	static final String CRITICAL = "[FATAL]: ";
	
	
	static final String SERVIDOR_DETECTADO = "Servidor detectado en el puerto ";
	static final String SERVIDOR_NO_DETECTADO = "Servidor no detectado en el puerto ";
	static final String SERVIDOR_CAIDO1 = "El servidor del puerto ";
	static final String SERVIDOR_CAIDO2 = " esta fuera de linea.";
	static final String TODOS_CAIDOS = "TODOS LOS SERVIDORES HAN CAIDO, LOS CLIENTES \n NO LOGRARAN CONECTARSE SISTEMA COMPLETAMENTE CAIDO";
	static final String SIN_SERVIDORES = "ULTIMO SERVIDOR EN LINEA ACABA DE CAER!";
	static final String NUEVO_CLIENTE_REGISTRADO = "Se ha registrado un nuevo cliente con nombre de usuario: ";
	static final String NUEVO_CLIENTE_DESREGISTRADO = "Se ha desconectado el cliente con nombre de usuario: ";
	
	
	public void logger(String mensaje);
}

package Servidor.Bean;

import java.net.InetAddress;

// el vacio es representado por 

public class MensajeExterno {
	private String iporigen;
	private String puertoorigen;
	private String usernameorigen;

	private String ipdestino;
	private String puertodestino;
	private String usernamedestino;

	private String comando;
	private String cuerpo;
	private String auxiliar;

	// constructor que se usa cuando se envia un mensaje, para generar el toString
	public MensajeExterno(String iporigen, String puertoorigen, String usernameorigen, String ipdestino,
			String puertodestino, String usernamedestino, String comando, String cuerpo, String auxiliar) {
		this.iporigen = iporigen;
		this.puertoorigen = puertoorigen;
		this.usernameorigen = usernameorigen;
		this.ipdestino = ipdestino;
		this.puertodestino = puertodestino;
		this.usernamedestino = usernamedestino;
		this.comando = comando;
		this.cuerpo = cuerpo;
		this.auxiliar = auxiliar;
		if (iporigen==null ||iporigen.equals("")){
			this.iporigen = " ";
		}
		if (puertoorigen==null ||puertoorigen.equals("")){
			this.puertoorigen = " ";
		}
		if (usernameorigen==null ||usernameorigen.equals("")){
			this.usernameorigen = " ";
		}
		if (ipdestino==null ||ipdestino.equals("")){
			this.ipdestino = " ";
		}
		if (puertodestino==null ||puertodestino.equals("")){
			this.puertodestino = " ";
		}
		if (usernamedestino==null || usernamedestino.equals("")){
			this.usernamedestino = " ";
		}
		if (comando==null ||comando.equals("")){
			this.comando = " ";
		}
		if (cuerpo==null ||cuerpo.equals("")){
			this.cuerpo = " ";
		}
		if (auxiliar==null ||auxiliar.equals("")){
			this.auxiliar = " ";
		}
	}

	// cosntructor que se usa cuando llega un mensaje, para parsearlo
	public MensajeExterno(String mensaje) {
		String[] partes = mensaje.split(":");
		if (partes[0].equals("localhost"))
			this.setIporigen("/127.0.0.1");
		else {
			this.setIporigen("/127.0.0.1");
		}
		this.setPuertoorigen(partes[1]);
		this.setUsernameorigen(partes[2]);

		if (partes[3].equals("localhost"))
			this.setIpdestino("/127.0.0.1");
		else {
			this.setIpdestino("/127.0.0.1");
		}
		this.setPuertodestino(partes[4]);
		this.setUsernamedestino(partes[5]);
		this.setComando(partes[6]);
		this.setCuerpo(partes[7]);
		this.setAuxiliar(partes[8]);
	}

	public String getIporigen() {
		return iporigen;
	}

	public void setIporigen(String iporigen) {
		this.iporigen = iporigen;
	}

	public String getPuertoorigen() {
		return puertoorigen;
	}

	public void setPuertoorigen(String puertorigen) {
		this.puertoorigen = puertorigen;
	}

	public String getUsernameorigen() {
		return usernameorigen;
	}

	public void setUsernameorigen(String usernameorigen) {
		this.usernameorigen = usernameorigen;
	}

	public String getIpdestino() {
		return ipdestino;
	}

	public void setIpdestino(String ipdestino) {
		this.ipdestino = ipdestino;
	}

	public String getPuertodestino() {
		return puertodestino;
	}

	public void setPuertodestino(String partes) {
		this.puertodestino = partes;
	}

	public String getUsernamedestino() {
		return usernamedestino;
	}

	public void setUsernamedestino(String usernamedestino) {
		this.usernamedestino = usernamedestino;
	}

	public String getComando() {
		return comando;
	}

	public void setComando(String comando) {
		this.comando = comando;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}

	public String getAuxiliar() {
		return auxiliar;
	}

	public void setAuxiliar(String auxiliar) {
		this.auxiliar = auxiliar;
	}

	@Override
	public String toString() {
		return iporigen + ":" + puertoorigen + ":" + usernameorigen + ":" + ipdestino + ":" + puertodestino + ":"
				+ usernamedestino + ":" + comando + ":" + cuerpo + ":" + auxiliar;
	}

}

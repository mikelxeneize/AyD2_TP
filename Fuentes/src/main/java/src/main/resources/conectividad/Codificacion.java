package src.main.resources.conectividad;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Codificacion {
	
	public Codificacion() {}
	
	public static  String encriptar(String clave, String mensaje, String algoritmo){
		String codificado;
		byte[] codificadobyte = null;
			java.security.Key key = new SecretKeySpec(clave.getBytes(),algoritmo);
			Cipher cipher = null;
			try {
				cipher = Cipher.getInstance(algoritmo);
			} catch (NoSuchAlgorithmException e) {
				System.out.println("2: "+"no ecnripto porque lo que se paso no es un algoritmo");
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				System.out.println("3: "+"no ecnripto porque el relleno no es correcto");
				e.printStackTrace();
			}
			try {
				cipher.init(Cipher.ENCRYPT_MODE, key);
			} catch (InvalidKeyException e) {
				System.out.println("4: "+"no ecnripto porque la key es incorrecta");
				e.printStackTrace();
			}
			try {
				codificadobyte= cipher.doFinal(mensaje.getBytes());
			} catch (IllegalBlockSizeException e) {
				System.out.println("5: "+"no ecnripto porque el blocksize es ilegal");
				e.printStackTrace();
			} catch (BadPaddingException e) {
				System.out.println("6: "+"no ecnripto porque lo que se paso no es un algoritmo");
				e.printStackTrace();
			}
			codificado= Base64.getEncoder().encodeToString(codificadobyte);
			return codificado;
	}
	
	public static  String desencriptar(String clave, String mensaje, String algoritmo)  {
		String decodificado;
		byte[] decodificadobyte;
			decodificadobyte=Base64.getDecoder().decode(mensaje);
			java.security.Key key = new SecretKeySpec(clave.getBytes(),algoritmo);
			Cipher cipher = null;
			try {
				cipher = Cipher.getInstance(algoritmo);
			} catch (NoSuchAlgorithmException e) {
				System.out.println("no desecnripto porque lo que se paso no es un algoritmo");
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				System.out.println("no desecnripto porque relleno no es correcto");
				e.printStackTrace();
			}
			try {
				cipher.init(Cipher.DECRYPT_MODE, key);
			} catch (InvalidKeyException e) {
				System.out.println("no desecnripto porque la key es invalida");
				e.printStackTrace();
			}
			byte[] bytes = null;
			try {
				bytes = cipher.doFinal(decodificadobyte);
			} catch (IllegalBlockSizeException e) {
				System.out.println("no desecnripto porque el blocksize es ilegal");
				e.printStackTrace();
			} catch (BadPaddingException e) {
				System.out.println("no desecnripto porque relleno no es malo");
				e.printStackTrace();
			}
			decodificado=new String (bytes);
			return decodificado;
	}
}

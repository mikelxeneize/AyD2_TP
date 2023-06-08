package src.main.resources.frontEnd;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Component;

import javax.swing.SwingConstants;

public class VentanaConfiguracionInicial extends JFrame implements IVista{

	private JPanel contentPane;
	private JTextField textField_port;
	private JTextField textField_ip;
	private JButton btnAceptar;
	private JLabel lblError;
	private JTextField textField_username;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaConfiguracionInicial frame = new VentanaConfiguracionInicial(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaConfiguracionInicial(IVista vista) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 460, 389);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setTitle(TITULO_VENTANA);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblPuerto = new JLabel("Puerto:");
		lblPuerto.setBounds(88, 165, 49, 14);
		contentPane.add(lblPuerto);
		
		textField_port = new JTextField();
		textField_port.setBounds(155, 156, 261, 33);
		contentPane.add(textField_port);
		textField_port.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Ip:");
		lblNewLabel.setBounds(114, 210, 49, 14);
		contentPane.add(lblNewLabel);
		
		textField_ip = new JTextField();
		textField_ip.setEnabled(false);
		textField_ip.setColumns(10);
		textField_ip.setBounds(155, 201, 261, 33);
		contentPane.add(textField_ip);
		
		JLabel lblConfiguracion = new JLabel("Bienvenido");
		lblConfiguracion.setHorizontalAlignment(SwingConstants.CENTER);
		lblConfiguracion.setFont(new Font("Dialog", Font.PLAIN, 24));
		lblConfiguracion.setBounds(12, 28, 436, 50);
		contentPane.add(lblConfiguracion);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(165, 297, 130, 33);
		contentPane.add(btnAceptar);
		
		lblError = new JLabel("El puerto esta ocupado, debe elegir otro");
		lblError.setHorizontalAlignment(SwingConstants.CENTER);
		lblError.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblError.setForeground(new Color(255, 0, 0));
		lblError.setBounds(12, 246, 436, 33);
		contentPane.add(lblError);
		
		textField_username = new JTextField();
		textField_username.setColumns(10);
		textField_username.setBounds(155, 111, 261, 33);
		contentPane.add(textField_username);
		
		JLabel lblNombreDeUsuario = new JLabel("Nombre de Usuario:");
		lblNombreDeUsuario.setBounds(12, 119, 125, 14);
		contentPane.add(lblNombreDeUsuario);
		this.setVisible(true);
		this.lblError.setVisible(false);
		this.setLocationRelativeTo((Component) vista);
	}

	public void addActionListener(ActionListener listener) {
		this.btnAceptar.addActionListener(listener);
		this.btnAceptar.setActionCommand(ACEPTAR);
	}

	public void cerrar() {
		setVisible(false);
		
	}

	public void cargarConfiguracion(String ip, String port) {
		this.textField_ip.setText(ip);
		this.textField_port.setText(port);
	}

	public String getIp(){
		return this.textField_ip.getText();
	}
	
	public void setIp(String ip){
		this.textField_ip.setText(ip);
	}
	
	
	public void mostrarLabelError() {
		this.lblError.setVisible(true);
	}
	
	public void ocultarLabelError() {
		this.lblError.setVisible(false);
	}
	
	//el controlador se encarga de realizar los casteos
	public String getPort(){
		return this.textField_port.getText();
	}
	
	public void setTextlabelError(String text) {
		this.lblError.setText(text);
	}

	public String getUsername() {
		return this.textField_username.getText();
	}
	
}

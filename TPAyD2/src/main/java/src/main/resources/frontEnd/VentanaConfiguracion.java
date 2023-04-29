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

public class VentanaConfiguracion extends JFrame implements IVista{

	private JPanel contentPane;
	private JTextField textField_port;
	private JTextField textField_ip;
	private JButton btnNewButton;
	private JLabel lblErrorPuerto;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaConfiguracion frame = new VentanaConfiguracion();
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
	public VentanaConfiguracion() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 460, 389);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblPuerto = new JLabel("Puerto:");
		lblPuerto.setBounds(60, 69, 49, 14);
		contentPane.add(lblPuerto);
		
		textField_port = new JTextField();
		textField_port.setBounds(93, 94, 261, 33);
		contentPane.add(textField_port);
		textField_port.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Ip:");
		lblNewLabel.setBounds(60, 138, 49, 14);
		contentPane.add(lblNewLabel);
		
		textField_ip = new JTextField();
		textField_ip.setColumns(10);
		textField_ip.setBounds(93, 173, 261, 33);
		contentPane.add(textField_ip);
		
		JLabel lblConfiguracion = new JLabel("Configuracion");
		lblConfiguracion.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblConfiguracion.setBounds(161, 11, 144, 33);
		contentPane.add(lblConfiguracion);
		
		btnNewButton = new JButton("Menu principal");
		btnNewButton.setBounds(293, 297, 130, 33);
		contentPane.add(btnNewButton);
		
		 lblErrorPuerto = new JLabel("El puerto esta ocupado, debe elegir otro");
		lblErrorPuerto.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblErrorPuerto.setForeground(new Color(255, 0, 0));
		lblErrorPuerto.setBounds(41, 229, 334, 33);
		contentPane.add(lblErrorPuerto);
		this.setVisible(true);
		this.lblErrorPuerto.setVisible(false);
	}

	public void addActionListener(ActionListener listener) {
		this.btnNewButton.addActionListener(listener);
		this.btnNewButton.setActionCommand(MENUPRINCIPAL);
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
	
	public void mostrarErrorPuerto() {
		this.lblErrorPuerto.setVisible(true);
	}
	
	public void ocultarErrorPuerto() {
		this.lblErrorPuerto.setVisible(false);
	}
	
	//el controlador se encarga de realizar los casteos
	public String getPort(){
		return this.textField_port.getText();
	}
}

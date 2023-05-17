package src.main.resources.frontEnd;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.SwingConstants;

public class VentanaMenuPrincipal extends JFrame implements IVista{
	private JLabel lblErrorAlConectar;
	private JPanel contentPane;
	private JTextField textField_port;
	private JTextField textField_IP;

	private JButton btnIniciarConexion;
	private JButton btnConfigurarPuerto ;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaMenuPrincipal frame = new VentanaMenuPrincipal();
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
	public VentanaMenuPrincipal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 625, 493);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		this.setTitle(TITULO_VENTANA);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnIniciarConexion = new JButton("Iniciar dialogo");
		btnConfigurarPuerto = new JButton("Configurar puerto");
		
		textField_port = new JTextField();
		textField_IP = new JTextField();
		textField_port.setBounds(148, 177, 148, 43);
		contentPane.add(textField_port);
		textField_port.setColumns(10);
		
		textField_IP.setColumns(10);
		textField_IP.setBounds(148, 231, 148, 43);
		contentPane.add(textField_IP);
		
		
		btnIniciarConexion.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnIniciarConexion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnIniciarConexion.setBounds(366, 177, 178, 101);
		contentPane.add(btnIniciarConexion);
		btnConfigurarPuerto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnConfigurarPuerto.setBounds(408, 376, 193, 48);
		contentPane.add(btnConfigurarPuerto);
		
		JLabel lblNewLabel = new JLabel("<html>Para iniciar una conversación, pedile a la otra persona su dirección IP pública y el número de puerto al que deseas conectarte, e ingresalos debajo!");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(82, 85, 466, 101);
		contentPane.add(lblNewLabel);
		JLabel lblPepeChat = new JLabel("Pepe Chat");
		lblPepeChat.setHorizontalAlignment(SwingConstants.CENTER);
		lblPepeChat.setFont(new Font("Tahoma", Font.PLAIN, 43));
		lblPepeChat.setBounds(24, 12, 589, 104);
		contentPane.add(lblPepeChat);
		
		JLabel lblNewLabel_1 = new JLabel("Ip destino:");
		lblNewLabel_1.setBounds(64, 231, 116, 36);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Puerto destino:");
		lblNewLabel_1_1.setBounds(42, 183, 115, 31);
		contentPane.add(lblNewLabel_1_1);
		
		lblErrorAlConectar = new JLabel("La conexion fue rechazada. Revisar el ip y puerto ingresados");
		lblErrorAlConectar.setHorizontalAlignment(SwingConstants.CENTER);
		lblErrorAlConectar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblErrorAlConectar.setForeground(new Color(255, 0, 0));
		lblErrorAlConectar.setBounds(12, 299, 589, 43);
		contentPane.add(lblErrorAlConectar);
		this.setVisible(true);
		this.lblErrorAlConectar.setVisible(false);
	}


	public void addActionListener(ActionListener listener) {
		this.btnConfigurarPuerto.addActionListener(listener);
		this.btnIniciarConexion.addActionListener(listener);
		this.btnConfigurarPuerto.setActionCommand(CONFIGURACION);
		this.btnIniciarConexion.setActionCommand(INICIAR_CONEXION);
	}

	
	public void cerrar() {
		setVisible(false);
		
	}
	public String getIpDestino(){
		return this.textField_IP.getText();
	}
	public String getPortDestino(){
		return this.textField_port.getText();
	}
	
	public void setTextlabelError(String text) {
		this.lblErrorAlConectar.setText(text);
	}
	
	public void mostrarLabelErrorAlConectar(boolean boleano) {
		this.lblErrorAlConectar.setVisible(boleano);
	}
}


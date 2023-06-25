package src.main.resources.frontEnd;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import src.main.resources.backEnd.Cliente;

import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;

import javax.swing.SwingConstants;
import javax.swing.JTextArea;

public class VentanaMenuPrincipal extends JFrame implements IVista{
	private JLabel lblErrorAlConectar;
	private JPanel contentPane;
	private JTextField textField_port;
	private JTextField textField_IP;
	private JTextArea textField_clientes;
	private JLabel lblPingEcho = new JLabel("Ping: ");
	private JLabel lblPingEchoValor = new JLabel("");
	private JButton btnIniciarConexion;
	private JButton btnConfigurarPuerto ;
	
	static final String ROJO ="Rojo";
	static final String VERDE ="Verde";
	static final String AMARILLO ="Amarillo";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaMenuPrincipal frame = new VentanaMenuPrincipal(null);
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
	public VentanaMenuPrincipal(IVista vista) {
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
		textField_port.setBounds(148, 162, 148, 43);
		contentPane.add(textField_port);
		textField_port.setColumns(10);
		
		textField_IP.setColumns(10);
		textField_IP.setBounds(148, 217, 148, 43);
		contentPane.add(textField_IP);
		
		
		btnIniciarConexion.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnIniciarConexion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnIniciarConexion.setBounds(370, 162, 178, 101);
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
		lblNewLabel.setBounds(82, 75, 466, 76);
		contentPane.add(lblNewLabel);
		JLabel lblPepeChat = new JLabel("Pepe Chat");
		lblPepeChat.setHorizontalAlignment(SwingConstants.CENTER);
		lblPepeChat.setFont(new Font("Tahoma", Font.PLAIN, 43));
		lblPepeChat.setBounds(98, -18, 361, 104);
		contentPane.add(lblPepeChat);
		
		JLabel lblNewLabel_1 = new JLabel("Ip destino:");
		lblNewLabel_1.setBounds(52, 217, 116, 36);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Puerto destino:");
		lblNewLabel_1_1.setBounds(42, 168, 115, 31);
		contentPane.add(lblNewLabel_1_1);
		
		lblErrorAlConectar = new JLabel("La conexion fue rechazada. Revisar el ip y puerto ingresados");
		lblErrorAlConectar.setHorizontalAlignment(SwingConstants.CENTER);
		lblErrorAlConectar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblErrorAlConectar.setForeground(new Color(255, 0, 0));
		lblErrorAlConectar.setBounds(24, 272, 589, 43);
		contentPane.add(lblErrorAlConectar);
		
		textField_clientes = new JTextArea();
		textField_clientes.setEditable(false);
		textField_clientes.setBounds(24, 355, 372, 97);
		contentPane.add(textField_clientes);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Ip:");
		lblNewLabel_1_1_1.setBounds(162, 313, 27, 31);
		contentPane.add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("Nombre de \nusuario");
		lblNewLabel_1_1_1_1.setBounds(24, 309, 142, 36);
		contentPane.add(lblNewLabel_1_1_1_1);
		
		JLabel lblNewLabel_1_1_1_1_1 = new JLabel("Puerto:");
		lblNewLabel_1_1_1_1_1.setBounds(228, 313, 49, 31);
		contentPane.add(lblNewLabel_1_1_1_1_1);
		
		JLabel lblNewLabel_1_1_1_1_1_1 = new JLabel("Estado:");
		lblNewLabel_1_1_1_1_1_1.setBounds(315, 313, 49, 31);
		contentPane.add(lblNewLabel_1_1_1_1_1_1);
		lblPingEcho.setForeground(new Color(0, 0, 0));
		lblPingEcho.setBounds(10, 11, 49, 14);
		contentPane.add(lblPingEcho);
		
		lblPingEchoValor.setForeground(Color.GREEN);
		lblPingEchoValor.setBounds(47, 11, 55, 14);
		contentPane.add(lblPingEchoValor);
		
		JLabel lblGif = new JLabel("");
		lblGif.setIcon(new ImageIcon(VentanaMenuPrincipal.class.getResource("/src/main/resources/frontEnd/pepechatGif.gif")));
		lblGif.setBounds(420, 11, 148, 97);
		contentPane.add(lblGif);
		this.setVisible(true);
		this.lblErrorAlConectar.setVisible(false);
		this.setLocationRelativeTo((Component) vista);
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
	public synchronized void recibirConectado(ArrayList<Cliente> mensajeRecibido){
		String mensajesPrevios = "";
		this.textField_clientes.setText("");
		for (int i = 0; i < mensajeRecibido.size(); i++) {
			this.textField_clientes.setText(mensajesPrevios + mensajeRecibido.get(i).toString() + "\n");
			mensajesPrevios = this.textField_clientes.getText();
		}
	}


	public String getLblPingEchoValor() {
		return lblPingEchoValor.getText();
	}


	public void setLblPingEchoValor(String valor) {
		this.lblPingEchoValor.setText(valor + "  ms");
	}


	public void setLblPingEchoColor(String color) {
		if(color.equals(AMARILLO))
			this.lblPingEchoValor.setForeground(Color.orange);
		else if (color.equals(VERDE))
			this.lblPingEchoValor.setForeground(Color.green);
		else
			this.lblPingEchoValor.setForeground(Color.red);
	}
	
}


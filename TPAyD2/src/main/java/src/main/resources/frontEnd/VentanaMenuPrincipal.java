package src.main.resources.frontEnd;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JLabel;

public class VentanaMenuPrincipal extends JFrame implements IVista{

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	private JButton btnNewButton;
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
                return null;
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

		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnNewButton = new JButton("Iniciar dialogo");
		btnConfigurarPuerto = new JButton("Configurar puerto");
		
		textField = new JTextField();
		textField_1 = new JTextField();
		textField.setBounds(148, 177, 148, 43);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1.setColumns(10);
		textField_1.setBounds(148, 231, 148, 43);
		contentPane.add(textField_1);
		
		
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(366, 177, 162, 101);
		contentPane.add(btnNewButton);
		btnConfigurarPuerto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnConfigurarPuerto.setBounds(439, 397, 162, 48);
		contentPane.add(btnConfigurarPuerto);
		
		JLabel lblNewLabel = new JLabel("Para iniciar la conversacion solicitar la ip a la otra persona, y enviarle la tuya que esta");
		lblNewLabel.setBounds(86, 328, 434, 104);
		contentPane.add(lblNewLabel);
		JLabel lblPepeChat = new JLabel("Pepe Chat");
		lblPepeChat.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblPepeChat.setBounds(238, 11, 142, 104);
		contentPane.add(lblPepeChat);
		
		JLabel lblNewLabel_1 = new JLabel("Ip destino:");
		lblNewLabel_1.setBounds(55, 238, 83, 28);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Puerto destino:");
		lblNewLabel_1_1.setBounds(57, 191, 83, 28);
		contentPane.add(lblNewLabel_1_1);
		this.setVisible(true);
	}


	public void addActionListener(ActionListener listener) {
		this.btnConfigurarPuerto.addActionListener(listener);
		this.btnNewButton.addActionListener(listener);
		this.btnConfigurarPuerto.setActionCommand(CONFIGURACION);
		this.btnNewButton.setActionCommand(INICIAR_CONVERSACION);
	}


	public JTextField getTextField() {
		return textField;
	}



	public JTextField getTextField_1() {
		return textField_1;
	}


	public void cerrar() {
		setVisible(false);
		
	}
	public String getIpDestino(){
		return this.textField.getText();
	}
	public String getPortDestino(){
		return this.textField_1.getText();
	}
}


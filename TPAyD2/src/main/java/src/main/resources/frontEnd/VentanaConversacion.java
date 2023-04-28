package src.main.resources.frontEnd;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;
import javax.swing.JLabel;
import java.awt.Font;

public class VentanaConversacion extends JFrame implements IVista{

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JButton btnNewButton_1 ;
	private JButton btnNewButton;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaConversacion frame = new VentanaConversacion();
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
	public VentanaConversacion() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 681, 433);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(78, 338, 431, 35);
		contentPane.add(textField);
		textField.setColumns(10);
		
		 btnNewButton = new JButton("Enviar mensaje");
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(519, 338, 125, 35);
		contentPane.add(btnNewButton);
		
		textField_1 = new JTextField();
		textField_1.setBounds(78, 85, 431, 242);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("CHAT");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblNewLabel.setBounds(265, 11, 102, 63);
		contentPane.add(lblNewLabel);
		
		 btnNewButton_1 = new JButton("Terminar conversacion");
		btnNewButton_1.setBounds(484, 23, 154, 51);
		contentPane.add(btnNewButton_1);
		this.setVisible(true);
	}

	public void addActionListener(ActionListener listener) {
		this.btnNewButton.addActionListener(listener);
		this.btnNewButton_1.addActionListener(listener);
		this.btnNewButton_1.setActionCommand(TERMINAR_CONVERSACION);
		this.btnNewButton.setActionCommand(ENVIAR_MENSAJE);
		
		
	}

	public String getMensaje(){
		return this.textField.getText();
	}

	public void vaciarMensaje(){
		this.textField.setText("");
	}


	public void cerrar() {
		setVisible(false);
		
	}
}

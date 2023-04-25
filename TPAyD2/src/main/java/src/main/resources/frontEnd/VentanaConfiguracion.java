package src.main.resources.frontEnd;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class VentanaConfiguracion extends JFrame implements IVista{

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JButton btnNewButton;

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
		lblPuerto.setBounds(60, 48, 49, 14);
		contentPane.add(lblPuerto);
		
		textField = new JTextField();
		textField.setBounds(93, 73, 261, 33);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Ip:");
		lblNewLabel.setBounds(60, 138, 49, 14);
		contentPane.add(lblNewLabel);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(93, 173, 261, 33);
		contentPane.add(textField_1);
		
		JLabel lblConfiguracion = new JLabel("Configuracion");
		lblConfiguracion.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblConfiguracion.setBounds(161, 11, 144, 33);
		contentPane.add(lblConfiguracion);
		
		btnNewButton = new JButton("Menu principal");
		btnNewButton.setBounds(293, 297, 130, 33);
		contentPane.add(btnNewButton);
		this.setVisible(true);
	}

	public void addActionListener(ActionListener listener) {
		this.btnNewButton.addActionListener(listener);
		this.btnNewButton.setActionCommand(MENUPRINCIPAL);
	}

	public void cerrar() {
		setVisible(false);
		
	}

}

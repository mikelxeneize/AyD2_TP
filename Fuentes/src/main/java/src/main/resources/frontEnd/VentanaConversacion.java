package src.main.resources.frontEnd;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextPane;
import java.awt.Color;
import javax.swing.DropMode;

public class VentanaConversacion extends JFrame implements IVista{

	private JPanel contentPane;
	private JTextArea textField_enviarMensaje;
	private JTextArea textField_recibirMensaje;
	private JButton btnNewButton_1 ;
	private JButton btnNewButton;
	private JLabel lblReintentolabel;
	private JTextField textConectadoCon;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaConversacion frame = new VentanaConversacion(null);
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
	public VentanaConversacion(IVista vista) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 681, 486);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		this.setTitle(TITULO_VENTANA);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblReintentolabel = new JLabel("reintento");
		lblReintentolabel.setForeground(new Color(164, 0, 0));
		lblReintentolabel.setBounds(72, 414, 375, 14);
		contentPane.add(lblReintentolabel);
		
		textField_enviarMensaje = new JTextArea();
		textField_enviarMensaje.setBounds(78, 338, 431, 35);
		contentPane.add(textField_enviarMensaje);
		textField_enviarMensaje.setColumns(10);
		
		 btnNewButton = new JButton("Enviar mensaje");
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(519, 338, 125, 35);
		contentPane.add(btnNewButton);
		
		textField_recibirMensaje = new JTextArea();
		textField_recibirMensaje.setEditable(false);
		textField_recibirMensaje.setBounds(78, 85, 431, 242);
		contentPane.add(textField_recibirMensaje);
		textField_recibirMensaje.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("CHAT");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblNewLabel.setBounds(290, -8, 184, 72);
		contentPane.add(lblNewLabel);
		
		 btnNewButton_1 = new JButton("Terminar conversacion");
		btnNewButton_1.setBounds(465, 414, 192, 35);
		contentPane.add(btnNewButton_1);
		
		JLabel lblNewLabel_1 = new JLabel("Conectado con:");
		lblNewLabel_1.setBounds(35, 21, 137, 26);
		contentPane.add(lblNewLabel_1);
		
		textConectadoCon = new JTextField();
		textConectadoCon.setEditable(false);
		textConectadoCon.setBounds(35, 48, 231, 26);
		contentPane.add(textConectadoCon);
		textConectadoCon.setColumns(10);
		this.setVisible(true);
		this.setLocationRelativeTo((Component) vista);
	}

	public void addActionListener(ActionListener listener) {
		this.btnNewButton.addActionListener(listener);
		this.btnNewButton_1.addActionListener(listener);
		this.btnNewButton_1.setActionCommand(CERRAR_CONEXION);
		this.btnNewButton.setActionCommand(ENVIAR_MENSAJE);
		
		
	}

	public String getMensaje(){
		return this.textField_enviarMensaje.getText();
	}

	public void vaciarMensaje(){
		this.textField_enviarMensaje.setText("");
	}

	public void recibirMensaje(String mensajeRecibido){
		String mensajesPrevios = this.textField_recibirMensaje.getText();
		this.textField_recibirMensaje.setText(mensajesPrevios + "\n" + mensajeRecibido);
	}

	public void cerrar() {
		setVisible(false);
		
	}

	public void setInputChat(String text) {
		this.textField_enviarMensaje.setText(text);
		
	}
	
	public void mostrarReintento() {
		this.lblReintentolabel.setVisible(true);
	}
	
	public void ocultarReintento() {
		this.lblReintentolabel.setVisible(false);
	}
	
	public void logReintento(String log) {
		this.lblReintentolabel.setText(log);;
	}

	public void setTextConectadoCon(String text) {
		textConectadoCon.setText(text);;
	}
}

package frontEnd;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import bean.Cliente;
import bean.ServerData;
import util.IEstados;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class VentanaMonitor extends JFrame implements IVista, IEstados{

	private JPanel contentPane;
	private JTable tableServActivo;
	private JTable tableClientesActivos;
	private JTextArea textArea;
	private DefaultTableModel modeloServActivos;
	private DefaultTableModel modeloClientes;
	private JScrollPane scrollPaneServActivos_1;
	/**
	 * Launch the application.
	 */
	

	static final String ROJO ="Rojo";
	static final String VERDE ="Verde";
	static final String AMARILLO ="Amarillo";
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaMonitor frame = new VentanaMonitor();
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
	public VentanaMonitor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1047, 696);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitulo = new JLabel("Monitor");
		lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblTitulo.setBounds(458, 11, 139, 33);
		contentPane.add(lblTitulo);
		
		JLabel lblLogs = new JLabel("Logs");
		lblLogs.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblLogs.setBounds(37, 68, 139, 33);
		contentPane.add(lblLogs);
		
		JLabel lblServActivos = new JLabel("Servidores activos");
		lblServActivos.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblServActivos.setBounds(572, 68, 405, 33);
		contentPane.add(lblServActivos);
		
		JLabel lblClientes = new JLabel("Clientes activos");
		lblClientes.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblClientes.setBounds(572, 353, 329, 33);
		contentPane.add(lblClientes);
		
		JScrollPane scrollPaneLogs = new JScrollPane();
		scrollPaneLogs.setBounds(47, 112, 392, 507);
		contentPane.add(scrollPaneLogs);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPaneLogs.setViewportView(textArea);
		
		JScrollPane scrollPaneServActivos = new JScrollPane();
		scrollPaneServActivos.setBounds(572, 122, 412, 196);
		contentPane.add(scrollPaneServActivos); 
		 
		
		scrollPaneServActivos_1 = new JScrollPane();
		scrollPaneServActivos_1.setBounds(572, 409, 412, 196);
		contentPane.add(scrollPaneServActivos_1);
		
		
		Object[][] data = {};
		String[] columnNames = {"Servidores", "Estado", "Ping (ms)"};
		modeloServActivos = new DefaultTableModel(data, columnNames);
		
		
		Object[][] data2 = {};
		String[] columnNames2 = {"Nombre de usuario", "IP", "Puerto", "Estado"};
		modeloClientes = new DefaultTableModel(data2, columnNames2);
		
		
		tableServActivo = new JTable(modeloServActivos);
		tableServActivo.setEnabled(false);
		scrollPaneServActivos.setViewportView(tableServActivo);

		TableColumnModel columnModel = tableServActivo.getColumnModel();
		TableColumn column = columnModel.getColumn(0);
		column.setPreferredWidth(150);
		column = columnModel.getColumn(1);
		column.setPreferredWidth(180);
		
		tableClientesActivos = new JTable(modeloClientes);
		scrollPaneServActivos_1.setViewportView(tableClientesActivos);
		this.setVisible(true); 
		columnModel = tableClientesActivos.getColumnModel();
		column = columnModel.getColumn(0);
		column.setPreferredWidth(100);
		column = columnModel.getColumn(1);
		column.setPreferredWidth(100);
		column = columnModel.getColumn(2);
		column.setPreferredWidth(50);
		
		
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		tableServActivo.getColumnModel().getColumn(0).setCellRenderer(tcr);
		tableServActivo.getColumnModel().getColumn(1).setCellRenderer(tcr);
		tableServActivo.getColumnModel().getColumn(2).setCellRenderer(tcr);
		
		tableClientesActivos.getColumnModel().getColumn(0).setCellRenderer(tcr);
		tableClientesActivos.getColumnModel().getColumn(1).setCellRenderer(tcr);
		tableClientesActivos.getColumnModel().getColumn(2).setCellRenderer(tcr);
	}
	
	public void addActionListener(ActionListener listener) {
		
		
	}

	public void cerrar() {
		this.setVisible(false);
	}

	public void addLogger(String mensaje) {
		this.textArea.append(mensaje+"\n");
	}

	public void addRowListaServidores(ServerData servidor) {
		Object[] newRowData = {servidor.getUsername(), servidor.getEstado(), servidor.getPing()};
		this.modeloServActivos.addRow(newRowData);
		
	}
	
	public void removeRowListaServidores(ServerData servidor) {
		int fila=buscarNumeroDeFila(servidor.getUsername());
		this.modeloServActivos.removeRow(fila);
	}
	
	public void modifyRowListaServidores(ServerData servidor) {
		int fila=buscarNumeroDeFila(servidor.getUsername());
		this.modeloServActivos.setValueAt(servidor.getUsername(),fila, 0);
		this.modeloServActivos.setValueAt(servidor.getEstado(),fila, 1);
		if(servidor.getEstado().equals(CAIDO)) {
			TableCellRenderer cellRenderer = this.tableServActivo.getCellRenderer(fila, 2);
			Component cellComponent = tableServActivo.prepareRenderer(cellRenderer, fila, 2);
			cellComponent.setForeground(Color.red);
		}
	    
	    	
	}
	
	
	public void setPingTabla(ArrayList<ServerData> ListaServidores) {
		long ping = 0;
		int fila;
		
		for (ServerData servidor : ListaServidores){
			
			fila= buscarNumeroDeFila(servidor.getUsername());
			if(fila!=-1) {
				ping= servidor.getPing();
				modeloServActivos.setValueAt(Long.toString(ping),fila , 2);
			}
			
		    if(servidor.getEstado().equals(ACTIVO)) {
		    	TableCellRenderer cellRenderer = this.tableServActivo.getCellRenderer(fila, 2);
			    Component cellComponent = tableServActivo.prepareRenderer(cellRenderer, fila, 2);
			    if (ping<200)
			    	cellComponent.setForeground(Color.green);
			    else if (ping<500)
			    	cellComponent.setForeground(Color.orange);
			    else 
			    	cellComponent.setForeground(Color.red);
			}
		    
		}
	}
	
	public int buscarNumeroDeFila (String username) {
		
        int CantFilas = this.modeloServActivos.getRowCount();

		for (int fila = 0; fila < CantFilas; fila++) {
			String userAux = (String)modeloServActivos.getValueAt(fila, 0); // Obtiene el valor de la columna "ID"
			if (userAux.equals(username)) 
				return fila;
	    }
		return -1;
	}

	public void setListaClientes(ArrayList<Cliente> listaClientes) {
		Object[][] data = {};
		String[] columnNames = {"Username", "Estado", "Ping (ms)"};
		DefaultTableModel modeloReemplazo = new DefaultTableModel(data, columnNames);
		
		for (Cliente cliente : listaClientes){
			Object[]  data2 = { cliente.getUsername(),cliente.getIp(),cliente.getPuerto(),cliente.getEstado() };
			modeloReemplazo.addRow(data2);
		}
		
		tableClientesActivos = new JTable(modeloReemplazo);
		scrollPaneServActivos_1.setViewportView(tableClientesActivos);
		this.setVisible(true); 
	}

	
	
}

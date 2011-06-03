package gui;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import modelo.ModeloGeneral;
import javax.swing.JTextField;

import clases.Cliente;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.Font;

public class PanelCliente extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private JPanel paneBtnUser = null;
	private JButton btnAgregar = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private Connection dbConnect;
	private ResultSet rs;  //  @jve:decl-index=0:
	private ResultSet rsSec;
	private PreparedStatement psCliente;
	private PreparedStatement psTelefono;
	private PreparedStatement psEmail;
	private PreparedStatement psBorrarCliente;
	private ModeloGeneral modelo=new ModeloGeneral();
	private JFrame mainFrame;
	private Vector<Cliente> clientes=new Vector<Cliente>();
	private JPanel panelTitulo = null;
	private JLabel lblTitulo = null;
	/**
	 * This is the default constructor
	 */
	public PanelCliente( JFrame mainFrame) {
		super();
		initialize();
		//recibo la conexion con la base de datos
		dbConnect=((MainFrame) mainFrame).getConnection();
		this.mainFrame=mainFrame;
		
		if(dbConnect!=null){
			
			try {
				
				psCliente=dbConnect.prepareStatement("SELECT * FROM clientes");
				
				psTelefono=dbConnect.prepareStatement("SELECT telefono FROM telefonos WHERE idCliente=?");
				
				psEmail=dbConnect.prepareStatement("SELECT email FROM email WHERE idCliente=?");
				
				
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, e.getMessage()+"Constructor panelUsuario");
			}
			
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(758, 507);
		this.setLayout(new BorderLayout());
		this.add(getJScrollPane(), BorderLayout.CENTER);
		this.add(getPaneBtnUser(), BorderLayout.EAST);
		this.add(getPanelTitulo(), BorderLayout.NORTH);
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTable() {
		if (jTable == null) {
			
			Vector<String> head=new Vector<String>();
			//agrego las cabeceras de la tabla
			head.addElement("ID Cliente");
			head.addElement("DNI");
			head.addElement("Nombre");
			head.addElement("Apellidos");
			head.addElement("Direccion");
			head.addElement("Telefono");
			head.addElement("E-Mail");
			head.addElement("Ciudad");
			head.addElement("Provincia");
			head.addElement("Empresa");

			modelo.setHeader(head);
			
			jTable = new JTable(modelo);
			jTable.setAutoCreateRowSorter(true);
						jTable.addMouseListener(new java.awt.event.MouseAdapter() { 
							public void mouseClicked(java.awt.event.MouseEvent evt) {    
								if (evt.getClickCount() >= 2){// si es doble click
									
									int x = jTable.rowAtPoint(evt.getPoint());
									
									Cliente c=((MainFrame) mainFrame).getCliente(x);
									
									//Vector row=c.getCliente();
							        
									((MainFrame) mainFrame).rowSelected(c);
							        
									((MainFrame) mainFrame).cambiarCapa("tabCliente");
									
							    }
							    
							}
						});
		}
		return jTable;
	}

	/**
	 * This method initializes paneBtnUser	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPaneBtnUser() {
		if (paneBtnUser == null) {
			paneBtnUser = new JPanel();
			paneBtnUser.setLayout(new BoxLayout(getPaneBtnUser(), BoxLayout.Y_AXIS));
			paneBtnUser.add(getBtnAgregar(), null);
			paneBtnUser.add(getJButton(), null);
			paneBtnUser.add(getJButton1(), null);
		}
		return paneBtnUser;
	}

	/**
	 * This method initializes btnAgregar	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnAgregar() {
		if (btnAgregar == null) {
			btnAgregar = new JButton();
			btnAgregar.setText("Agregar");
			btnAgregar.setMaximumSize(new Dimension(85, 25));
			btnAgregar.setMinimumSize(new Dimension(85, 25));
			btnAgregar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//muestro el panel en el que se insertan los datos
					((MainFrame) mainFrame).cambiarCapa("panelAddClientes");
					
					
					
				}
			});
		}
		return btnAgregar;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Buscar");
			jButton.setMinimumSize(new Dimension(85, 25));
			jButton.setMaximumSize(new Dimension(85, 25));
			jButton.setPreferredSize(new Dimension(85, 25));
		}
		return jButton;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("Eliminar");
			jButton1.setMaximumSize(new Dimension(85, 25));
			jButton1.setMinimumSize(new Dimension(85, 25));
			jButton1.setPreferredSize(new Dimension(85, 25));
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					int seleccionado=jTable.getSelectedRow();
					
					if(seleccionado>=0){
						
						Cliente c=((MainFrame) mainFrame).getCliente(seleccionado);
						
						((MainFrame) mainFrame).delCliente(c);
						
					}
					
				}
			});
		}
		return jButton1;
	}

	
	public void actualizarTablaClientes(){
		
		
		Vector clientes=((MainFrame) mainFrame).getClientes();
		Vector data=new Vector();
		
		Iterator i=clientes.iterator();
		
		while(i.hasNext()){
			
			Cliente c=(Cliente) i.next();
			
			data.add(c.getCliente());
			
		}
		
		modelo.setData(data);
		
	}

	/**
	 * This method initializes panelTitulo	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelTitulo() {
		if (panelTitulo == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			panelTitulo = new JPanel();
			panelTitulo.setLayout(flowLayout);
			panelTitulo.add(getLblTitulo(), null);
		}
		return panelTitulo;
	}

	/**
	 * This method initializes lblTitulo	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getLblTitulo() {
		if (lblTitulo == null) {
			lblTitulo = new JLabel();
			lblTitulo.setText("Gesti�n de clientes");
			lblTitulo.setFont(new Font("Times New Roman", Font.BOLD, 18));
		}
		return lblTitulo;
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"

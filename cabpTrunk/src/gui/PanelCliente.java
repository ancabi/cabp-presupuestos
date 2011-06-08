package gui;

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

import modelo.ModeloClientes;
import javax.swing.JTextField;

import clases.Cliente;
import clases.Emails;
import clases.Telefonos;

import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.ImageIcon;

public class PanelCliente extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private JPanel paneBtnUser = null;
	private JButton btnAgregar = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private ModeloClientes modelo=new ModeloClientes();
	private JFrame mainFrame;
	private JPanel panelTitulo = null;
	private JLabel lblTitulo = null;
	private DialogoAddCliente dialogoAddCliente;
	private DialogoModCliente dialogoModCliente;
	private DialogoBuscarCliente dialogoBuscarCliente;
	private JButton btnVolver = null;
	/**
	 * This is the default constructor
	 */
	public PanelCliente( JFrame mainFrame) {
		super();
		initialize();
		
		this.mainFrame=mainFrame;
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
			jTable.getColumn("ID Cliente").setMaxWidth(70);
			jTable.getColumn("DNI").setMaxWidth(75);
			jTable.getColumn("Telefono").setMaxWidth(75);
					jTable.addMouseListener(new java.awt.event.MouseAdapter() { 
							public void mouseClicked(java.awt.event.MouseEvent evt) {    
								if (evt.getClickCount() >= 2){// si es doble click
									//tengo el index de la fila seleccionada
									int x = jTable.rowAtPoint(evt.getPoint());
									//traigo el cliente
									Cliente c=((MainFrame) mainFrame).getListadoClientes().getCliente(x);
									//construyo el dialogo de modificacion de clinete
									dialogoModCliente=getDialogoModCliente();
									//le asigno el cliente
									dialogoModCliente.setCliente(c);
									//le paso mainFrame
									dialogoModCliente.setMainFrame(mainFrame);
									//lo muestro
									dialogoModCliente.setVisible(true);
									
									if(dialogoModCliente.getValorPulsado()==dialogoModCliente.VALOR_ACEPTAR){
										//traigo el vector de los telefonos a borrar
										Vector<Telefonos> telefonosBorrar=dialogoModCliente.getTabbedCliente().getPanelDatosClientes().getTelefonoBorrar();
										//y los emails
										Vector<Emails> emailsBorrar=dialogoModCliente.getTabbedCliente().getPanelDatosClientes().getEmailsBorrar();
										//se lo paso al panel para borrarlos
										((MainFrame) mainFrame).getListadoClientes().actualizarCliente(c, telefonosBorrar, emailsBorrar);
										//vacio los campos
										dialogoModCliente.getTabbedCliente().getPanelDatosClientes().limpiarCampos();
										
										actualizarTablaClientes();
										
									}
									
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
			paneBtnUser.setLayout(new FlowLayout());
			paneBtnUser.add(getBtnAgregar(), null);
			paneBtnUser.add(getJButton1(), null);
			paneBtnUser.add(getJButton(), null);
			paneBtnUser.add(getBtnVolver(), null);
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
			btnAgregar.setText("");
			btnAgregar.setMaximumSize(new Dimension(85, 25));
			btnAgregar.setIcon(new ImageIcon(getClass().getResource("/img/add.png")));
			btnAgregar.setMinimumSize(new Dimension(85, 25));
			btnAgregar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					dialogoAddCliente=getDialogoAddCliente();
					
					dialogoAddCliente.setVisible(true);
					
					if(dialogoAddCliente.getValorPulsado()==dialogoAddCliente.VALOR_ACEPTAR){
						
						((MainFrame) mainFrame).getListadoClientes().addCliente(dialogoAddCliente.getNewCliente());
						
						actualizarTablaClientes();
						
					}
					
				}
			});
		}
		return btnAgregar;
	}
	
	private DialogoAddCliente getDialogoAddCliente(){
		
		if(dialogoAddCliente == null){
			
			dialogoAddCliente=new DialogoAddCliente(mainFrame);
			
		}
		
		return dialogoAddCliente;
		
	}
	
	private DialogoModCliente getDialogoModCliente(){
		
		if(dialogoModCliente == null){
			
			dialogoModCliente=new DialogoModCliente(mainFrame);
			
		}
		
		return dialogoModCliente;
		
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("");
			jButton.setMinimumSize(new Dimension(85, 25));
			jButton.setMaximumSize(new Dimension(85, 25));
			jButton.setIcon(new ImageIcon(getClass().getResource("/img/search.png")));
			jButton.setPreferredSize(new Dimension(58, 34));
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					dialogoBuscarCliente=getDialogoBuscarCliente();
					
					dialogoBuscarCliente.setVisible(true);
					
					if(dialogoBuscarCliente.getValorPulsado()==dialogoBuscarCliente.VALOR_ACEPTAR){
						
						String buscar=dialogoBuscarCliente.getBuscar();
						String criterio=dialogoBuscarCliente.getCriterio();
						
						Vector datos=((MainFrame) mainFrame).getListadoClientes().buscarCliente(buscar, criterio);
						
						
						if(datos.isEmpty()){
							try {
								throw new Exception("No hubo coincidencias en la busqueda");
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(null, e1.getMessage());
							}
						}else{
							modelo.setData(datos);
							btnVolver.setEnabled(true);
						}
					}
				}
			});
		}
		return jButton;
	}
	
	private DialogoBuscarCliente getDialogoBuscarCliente(){
		
		if(dialogoBuscarCliente==null){
			
			dialogoBuscarCliente=new DialogoBuscarCliente(mainFrame);
			
		}
		
		return dialogoBuscarCliente;
		
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("");
			jButton1.setMaximumSize(new Dimension(85, 25));
			jButton1.setMinimumSize(new Dimension(85, 25));
			jButton1.setIcon(new ImageIcon(getClass().getResource("/img/delete.png")));
			jButton1.setPreferredSize(new Dimension(58, 34));
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					int seleccionado=jTable.getSelectedRow();
					
					if(seleccionado>=0){
						
						Cliente c=((MainFrame) mainFrame).getListadoClientes().getCliente(seleccionado);
						
						((MainFrame) mainFrame).getListadoClientes().delCliente(c);
						
						actualizarTablaClientes();
						
					}
					
				}
			});
		}
		return jButton1;
	}

	
	public void actualizarTablaClientes(){
		
		
		Vector clientes=((MainFrame) mainFrame).getListadoClientes().getClientes();
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
			panelTitulo = new JPanel();
			panelTitulo.setLayout(new BorderLayout());
			panelTitulo.add(getLblTitulo(), BorderLayout.WEST);
			panelTitulo.add(getPaneBtnUser(), BorderLayout.EAST);
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
			lblTitulo.setText("Gestion de clientes");
			lblTitulo.setFont(new Font("Times New Roman", Font.BOLD, 18));
		}
		return lblTitulo;
	}

	/**
	 * This method initializes btnVolver	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnVolver() {
		if (btnVolver == null) {
			btnVolver = new JButton();
			btnVolver.setText("");
			btnVolver.setEnabled(false);
			btnVolver.setIcon(new ImageIcon(getClass().getResource("/img/refresh.png")));
			btnVolver.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					actualizarTablaClientes();
					
					btnVolver.setEnabled(false);
				}
			});
		}
		return btnVolver;
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"

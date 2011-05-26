package gui;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import modelo.ModeloGeneral;
import javax.swing.JTextField;

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
	private JTextField tf = null;
	private JButton btn = null;

	/**
	 * This is the default constructor
	 */
	public PanelCliente( Connection con, JFrame mainFrame) {
		super();
		initialize();
		//recibo la conexion con la base de datos
		dbConnect=con;
		this.mainFrame=mainFrame;
		
		if(dbConnect!=null){
			
			try {
				
				psCliente=dbConnect.prepareStatement("SELECT * FROM clientes");
				
				psTelefono=dbConnect.prepareStatement("SELECT telefono FROM telefonos WHERE idCliente=?");
				
				psEmail=dbConnect.prepareStatement("SELECT email FROM email WHERE idCliente=?");
				
				psBorrarCliente=dbConnect.prepareStatement("DELETE FROM clientes WHERE idCliente=?");
				
				
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
			head.addElement("Telefono");
			head.addElement("E-Mail");
			head.addElement("Direccion");
			head.addElement("Ciudad");
			head.addElement("Provincia");
			head.addElement("Empresa");

			modelo.setHeader(head);
			
			jTable = new JTable(modelo);
			jTable.setAutoCreateRowSorter(true);
						jTable.addMouseListener(new java.awt.event.MouseAdapter() { 
							public void mouseClicked(java.awt.event.MouseEvent evt) {    
								if (evt.getClickCount() >= 2){// si es doble click
									
									int fila = jTable.rowAtPoint(evt.getPoint());
									
									Vector row=modelo.getRow(fila);
							        
									((MainFrame) mainFrame).rowSelected(row);
							        
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
			paneBtnUser.add(getBtn(), null);
			paneBtnUser.add(getTf(), null);
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
						
						int idCliente= Integer.parseInt(""+modelo.getValueAt(seleccionado, 0));
						
						try {
							psBorrarCliente.setInt(1, idCliente);
							
							psBorrarCliente.executeUpdate();
							
							modelo.deleteRow(seleccionado);
							
							modelo.fireTableDataChanged();
							
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
						
						
						
					}
					
				}
			});
		}
		return jButton1;
	}

	
	public void actualizarTablaClientes(){
		
		try {
			rs=psCliente.executeQuery();
			
			Vector<Object> tupla;
			Vector<Vector> data=new Vector<Vector>();
			
			
			while(rs.next()){
				
				Vector<String> telefono=new Vector<String>();
				Vector<String> email=new Vector<String>();
				//construyo de nuevo el vector para que no se me acumulen todo en un solo vector
				tupla=new Vector<Object>();
				
				tupla.addElement(""+rs.getInt(1));
				tupla.addElement(rs.getString(2));
				tupla.addElement(rs.getString(3));
				tupla.addElement(rs.getString(4));
				
				psTelefono.setInt(1, rs.getInt(1));
				rsSec=psTelefono.executeQuery();

				
				while(rsSec.next()){
					
					telefono.addElement(""+rsSec.getInt(1));
					
				}

				tupla.addElement(telefono);
				
				psEmail.setInt(1, rs.getInt(1));
				rsSec=psEmail.executeQuery();
				
				while(rsSec.next()){
					
					email.addElement(rsSec.getString(1));
					
				}
				tupla.addElement(email);
				
				
				tupla.addElement(rs.getString(5));
				tupla.addElement(rs.getString(6));
				tupla.addElement(rs.getString(7));
				tupla.addElement(rs.getString(8));
				tupla.addElement(rs.getString(9));

				//asigno el vector de cada tupla al de las filas
				data.add(tupla);
			}
			
			//asigno al modelo el vector con las filas
			modelo.setData(data);
			//aviso a la tabla que se cambiaron los datos
			modelo.fireTableDataChanged();
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage()+"Actualizar tabla cliente");

		}
		
	}

	/**
	 * This method initializes tf	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTf() {
		if (tf == null) {
			tf = new JTextField();
		}
		return tf;
	}

	/**
	 * This method initializes btn	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtn() {
		if (btn == null) {
			btn = new JButton();
			btn.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
				}
			});
		}
		return btn;
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"

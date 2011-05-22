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

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import modelo.ModeloGeneral;

public class PanelCliente extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private JPanel paneBtnUser = null;
	private JButton btnAgregar = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private Connection dbConnect;
	private ResultSet rs;
	private PreparedStatement psCliente;
	ModeloGeneral modelo=new ModeloGeneral();

	/**
	 * This is the default constructor
	 */
	public PanelCliente( Connection con) {
		super();
		initialize();
		
		dbConnect=con;
		
		System.out.println(dbConnect);
		
		if(dbConnect!=null){
			
			try {
				
				psCliente=dbConnect.prepareStatement("SELECT * FROM clientes");
				
				
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
			
			head.addElement("DNI");
			head.addElement("Nombre");
			head.addElement("Tipo");
			head.addElement("E-Mail");
			head.addElement("Area");

			modelo.setHeader(head);
			
			jTable = new JTable(modelo);
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
		}
		return jButton1;
	}

	
	public void actualizarTablaClientes(){
		
		try {
			rs=psCliente.executeQuery();
			
			Vector<String> tupla;
			Vector<Vector> data=new Vector<Vector>();
			
			while(rs.next()){
				
				//construyo de nuevo el vector para que no se me acumulen todo en un solo vector
				tupla=new Vector<String>();
				
				tupla.addElement(""+rs.getInt(1));
				tupla.addElement(rs.getString(2));
				tupla.addElement(rs.getString(3));
				tupla.addElement(rs.getString(4));
				tupla.addElement(rs.getString(5));
				tupla.addElement(rs.getString(6));
				tupla.addElement(rs.getString(7));

				//asigno el vector de cada tupla al de las filas
				data.add(tupla);
			}
			
			//asigno al modelo el vector con las filas
			modelo.setData(data);
			//aviso a la tabla que se cambiaron los datos
			modelo.fireTableDataChanged();
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage()+"Actualizar tabla usuario");

		}
		
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"

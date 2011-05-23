package gui;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;

public class PanelDatosClientes extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblNombre = null;
	private JLabel lblApellidos = null;
	private JLabel lblDireccion = null;
	private JLabel lblDni = null;
	private JTextField tfDni = null;
	private JTextField tfNombre = null;
	private JTextField tfApellidos = null;
	private JTextField tfDireccion = null;
	private JLabel lblCiudad = null;
	private JLabel lblEmpresa = null;
	private JTextField tfCiudad = null;
	private JTextField tfEmpresa = null;
	private JLabel lblNotas = null;
	private JTextArea taNotas = null;
	private JPanel panelBotones = null;
	private JButton btnAceptar = null;
	private JButton btnVolver = null;
	private boolean agregar=true;
	private Connection dbConnect;
	private PreparedStatement psInsertar;
	private PreparedStatement psActualizar;
	private JFrame mainFrame;
	private int idCliente;
	
	
	/**
	 * This is the default constructor
	 */
	public PanelDatosClientes(Connection con, JFrame mainFrame) {
		super();
		initialize();
		
		dbConnect=con;
		this.mainFrame=mainFrame;
		
		if(dbConnect!=null){
			
			try {
				psInsertar=dbConnect.prepareStatement("INSERT INTO clientes(dni, nombre, apellidos, direccion, ciudad, empresa, notas)" +
						" VALUES (?,?,?,?,?,?,?)");
				
				psActualizar=dbConnect.prepareStatement("UPDATE clientes SET dni=?, nombre=?, apellidos=?, direccion=?, ciudad=?, empresa=?, notas=?" +
						"WHERE idCliente=?");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.gridheight = 1;
		gridBagConstraints21.gridwidth = 4;
		gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints21.anchor = GridBagConstraints.EAST;
		gridBagConstraints21.gridy = 6;
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints12.gridy = 4;
		gridBagConstraints12.weightx = 1.0;
		gridBagConstraints12.weighty = 1.0;
		gridBagConstraints12.gridwidth = 3;
		gridBagConstraints12.gridheight = 1;
		gridBagConstraints12.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints12.insets = new Insets(2, 10, 2, 10);
		gridBagConstraints12.gridx = 1;
		GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		gridBagConstraints13.gridx = 0;
		gridBagConstraints13.insets = new Insets(2, 10, 2, 2);
		gridBagConstraints13.anchor = GridBagConstraints.WEST;
		gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints13.gridy = 3;
		lblNotas = new JLabel();
		lblNotas.setText("Notas:");
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.fill = GridBagConstraints.NONE;
		gridBagConstraints11.gridy = 2;
		gridBagConstraints11.weightx = 1.0;
		gridBagConstraints11.insets = new Insets(2, 10, 2, 2);
		gridBagConstraints11.anchor = GridBagConstraints.WEST;
		gridBagConstraints11.gridx = 3;
		GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		gridBagConstraints10.fill = GridBagConstraints.NONE;
		gridBagConstraints10.gridy = 2;
		gridBagConstraints10.weightx = 1.0;
		gridBagConstraints10.insets = new Insets(2, 10, 2, 2);
		gridBagConstraints10.anchor = GridBagConstraints.WEST;
		gridBagConstraints10.gridx = 1;
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.gridx = 2;
		gridBagConstraints9.insets = new Insets(2, 10, 2, 2);
		gridBagConstraints9.anchor = GridBagConstraints.WEST;
		gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints9.gridy = 2;
		lblEmpresa = new JLabel();
		lblEmpresa.setText("Empresa:");
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.gridx = 0;
		gridBagConstraints8.insets = new Insets(2, 10, 2, 2);
		gridBagConstraints8.anchor = GridBagConstraints.WEST;
		gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints8.gridy = 2;
		lblCiudad = new JLabel();
		lblCiudad.setText("Ciudad:");
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.fill = GridBagConstraints.NONE;
		gridBagConstraints7.gridy = 1;
		gridBagConstraints7.weightx = 1.0;
		gridBagConstraints7.insets = new Insets(2, 10, 2, 2);
		gridBagConstraints7.anchor = GridBagConstraints.WEST;
		gridBagConstraints7.gridx = 3;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.fill = GridBagConstraints.NONE;
		gridBagConstraints6.gridy = 1;
		gridBagConstraints6.weightx = 1.0;
		gridBagConstraints6.insets = new Insets(2, 10, 2, 2);
		gridBagConstraints6.anchor = GridBagConstraints.WEST;
		gridBagConstraints6.gridx = 1;
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.fill = GridBagConstraints.NONE;
		gridBagConstraints5.gridy = 0;
		gridBagConstraints5.weightx = 1.0;
		gridBagConstraints5.insets = new Insets(2, 10, 2, 2);
		gridBagConstraints5.anchor = GridBagConstraints.WEST;
		gridBagConstraints5.gridx = 3;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.fill = GridBagConstraints.NONE;
		gridBagConstraints4.gridy = 0;
		gridBagConstraints4.weightx = 1.0;
		gridBagConstraints4.insets = new Insets(2, 10, 2, 2);
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.gridx = 1;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.insets = new Insets(2, 10, 2, 2);
		gridBagConstraints3.anchor = GridBagConstraints.WEST;
		gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints3.gridy = 0;
		lblDni = new JLabel();
		lblDni.setText("DNI:");
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 2;
		gridBagConstraints2.insets = new Insets(2, 10, 2, 2);
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints2.gridy = 1;
		lblDireccion = new JLabel();
		lblDireccion.setText("Direccion:");
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.insets = new Insets(2, 10, 2, 2);
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridy = 1;
		lblApellidos = new JLabel();
		lblApellidos.setText("Apellidos:");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.insets = new Insets(2, 10, 2, 2);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridy = 0;
		lblNombre = new JLabel();
		lblNombre.setText("Nombre:");
		this.setSize(667, 423);
		this.setLayout(new GridBagLayout());
		this.setMinimumSize(new Dimension(70, 20));
		this.setPreferredSize(new Dimension(110, 20));
		this.add(lblNombre, gridBagConstraints);
		this.add(lblApellidos, gridBagConstraints1);
		this.add(lblDireccion, gridBagConstraints2);
		this.add(lblDni, gridBagConstraints3);
		this.add(getTfDni(), gridBagConstraints4);
		this.add(getTfNombre(), gridBagConstraints5);
		this.add(getTfApellidos(), gridBagConstraints6);
		this.add(getTfDireccion(), gridBagConstraints7);
		this.add(lblCiudad, gridBagConstraints8);
		this.add(lblEmpresa, gridBagConstraints9);
		this.add(getTfCiudad(), gridBagConstraints10);
		this.add(getTfEmpresa(), gridBagConstraints11);
		this.add(lblNotas, gridBagConstraints13);
		this.add(getTaNotas(), gridBagConstraints12);
		this.add(getPanelBotones(), gridBagConstraints21);
	}

	/**
	 * This method initializes tfDni	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfDni() {
		if (tfDni == null) {
			tfDni = new JTextField();
			tfDni.setMinimumSize(new Dimension(70, 20));
			tfDni.setPreferredSize(new Dimension(110, 20));
		}
		return tfDni;
	}

	/**
	 * This method initializes tfNombre	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfNombre() {
		if (tfNombre == null) {
			tfNombre = new JTextField();
			tfNombre.setMinimumSize(new Dimension(70, 20));
			tfNombre.setPreferredSize(new Dimension(110, 20));
		}
		return tfNombre;
	}

	/**
	 * This method initializes tfApellidos	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfApellidos() {
		if (tfApellidos == null) {
			tfApellidos = new JTextField();
			tfApellidos.setMinimumSize(new Dimension(70, 20));
			tfApellidos.setPreferredSize(new Dimension(110, 20));
		}
		return tfApellidos;
	}

	/**
	 * This method initializes tfDireccion	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfDireccion() {
		if (tfDireccion == null) {
			tfDireccion = new JTextField();
			tfDireccion.setMinimumSize(new Dimension(70, 20));
			tfDireccion.setPreferredSize(new Dimension(110, 20));
		}
		return tfDireccion;
	}

	/**
	 * This method initializes tfCiudad	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfCiudad() {
		if (tfCiudad == null) {
			tfCiudad = new JTextField();
			tfCiudad.setPreferredSize(new Dimension(110, 20));
		}
		return tfCiudad;
	}

	/**
	 * This method initializes tfEmpresa	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfEmpresa() {
		if (tfEmpresa == null) {
			tfEmpresa = new JTextField();
			tfEmpresa.setPreferredSize(new Dimension(110, 20));
		}
		return tfEmpresa;
	}

	/**
	 * This method initializes taNotas	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTaNotas() {
		if (taNotas == null) {
			taNotas = new JTextArea();
			taNotas.setMinimumSize(new Dimension(400, 300));
			taNotas.setPreferredSize(new Dimension(250, 200));
		}
		return taNotas;
	}

	/**
	 * This method initializes panelBotones	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints15.fill = GridBagConstraints.NONE;
			gridBagConstraints15.anchor = GridBagConstraints.EAST;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints14.fill = GridBagConstraints.NONE;
			gridBagConstraints14.anchor = GridBagConstraints.EAST;
			panelBotones = new JPanel();
			panelBotones.setLayout(new GridBagLayout());
			panelBotones.add(getBtnAceptar(), gridBagConstraints14);
			panelBotones.add(getBtnVolver(), gridBagConstraints15);
		}
		return panelBotones;
	}

	/**
	 * This method initializes btnAceptar	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton();
			btnAceptar.setText("Aceptar");
			btnAceptar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					String dni=tfDni.getText();
					String nombre=tfNombre.getText();
					String apellidos=tfApellidos.getText();
					String direccion=tfDireccion.getText();
					String ciudad=tfCiudad.getText();
					String empresa=tfEmpresa.getText();
					String notas=taNotas.getText();
					
					if(agregar){
						
						try {
							
							psInsertar.setString(1, dni);
							psInsertar.setString(2, nombre);
							psInsertar.setString(3, apellidos);
							psInsertar.setString(4, direccion);
							psInsertar.setString(5, ciudad);
							psInsertar.setString(6, empresa);
							psInsertar.setString(7, notas);
							
							psInsertar.executeUpdate();
							
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
						
					}else{
						
						try {
							psActualizar.setString(1, dni);
							psActualizar.setString(2, nombre);
							psActualizar.setString(3, apellidos);
							psActualizar.setString(4, direccion);
							psActualizar.setString(5, ciudad);
							psActualizar.setString(6, empresa);
							psActualizar.setString(7, notas);
							psActualizar.setInt(8, idCliente);
							
							psActualizar.executeUpdate();
							
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}
					
					limpiarCampos();
					
					((MainFrame) mainFrame).cambiarCapa("panelCliente");
					
				}
			});
		}
		return btnAceptar;
	}

	/**
	 * This method initializes btnVolver	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnVolver() {
		if (btnVolver == null) {
			btnVolver = new JButton();
			btnVolver.setText("Volver");
			btnVolver.setPreferredSize(new Dimension(79, 26));
			btnVolver.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					((MainFrame) mainFrame).cambiarCapa("panelCliente");
					
					limpiarCampos();
					
				}
			});
		}
		return btnVolver;
	}
	
	private void limpiarCampos(){
		
		tfDni.setText("");
		tfNombre.setText("");
		tfApellidos.setText("");
		tfDireccion.setText("");
		tfCiudad.setText("");
		tfEmpresa.setText("");
		taNotas.setText("");
		
		agregar=true;
		
		
	}
	
	public void setAgregar(){
		agregar=false;
	}
	
	public void setCliente(Vector cliente){
		
		idCliente=Integer.parseInt((String) cliente.elementAt(0));
		tfDni.setText(""+cliente.elementAt(1));
		tfNombre.setText(""+cliente.elementAt(2));
		tfApellidos.setText(""+cliente.elementAt(3));
		tfDireccion.setText(""+cliente.elementAt(4));
		tfCiudad.setText(""+cliente.elementAt(5));
		tfEmpresa.setText(""+cliente.elementAt(6));
		taNotas.setText(""+cliente.elementAt(7));
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

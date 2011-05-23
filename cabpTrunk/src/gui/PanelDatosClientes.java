package gui;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;

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
	private JTextField tfTelefono = null;
	private JList jlTelefonos = null;
	private JButton btnAddTel = null;
	private JButton btnDelTel = null;
	private DefaultListModel modeloLista;
	private JTextField tfEmail = null;
	private JList jlEmail = null;
	private JButton btnAddEmail = null;
	private JButton btnDelEmail = null;
	private JPanel panelNotas = null;
	private JPanel panelContenedor = null;
	private JPanel jPanel = null;
	private JScrollPane scrollTel = null;
	private JScrollPane scrollEmail = null;
	private JLabel lblTelefonos = null;
	private JLabel lblEmail = null;
	private JPanel panelAddTel = null;
	private JPanel panelAddEmail = null;
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
		lblNotas = new JLabel();
		lblNotas.setText("Notas:");
		lblEmpresa = new JLabel();
		lblEmpresa.setText("Empresa:");
		lblCiudad = new JLabel();
		lblCiudad.setText("Ciudad:");
		lblDni = new JLabel();
		lblDni.setText("DNI:");
		lblDireccion = new JLabel();
		lblDireccion.setText("Direccion:");
		lblApellidos = new JLabel();
		lblApellidos.setText("Apellidos:");
		lblNombre = new JLabel();
		lblNombre.setText("Nombre:");
		this.setSize(920, 617);
		this.setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(70, 20));
		this.setPreferredSize(new Dimension(110, 20));
		this.add(getPanelBotones(), BorderLayout.SOUTH);
		this.add(getPanelContenedor(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes tfDni	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfDni() {
		if (tfDni == null) {
			tfDni = new JTextField();
			tfDni.setMinimumSize(new Dimension(110, 20));
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
			tfNombre.setMinimumSize(new Dimension(110, 20));
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
			tfApellidos.setMinimumSize(new Dimension(110, 20));
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
			tfDireccion.setMinimumSize(new Dimension(110, 20));
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
			tfCiudad.setMinimumSize(new Dimension(110, 20));
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
			tfEmpresa.setMinimumSize(new Dimension(110, 20));
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

	/**
	 * This method initializes tfTelefono	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfTelefono() {
		if (tfTelefono == null) {
			tfTelefono = new JTextField();
			tfTelefono.setMinimumSize(new Dimension(110, 20));
			tfTelefono.setPreferredSize(new Dimension(190, 20));
		}
		return tfTelefono;
	}

	/**
	 * This method initializes jlTelefonos	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getJlTelefonos() {
		if (jlTelefonos == null) {
			
			modeloLista=new DefaultListModel();
			
			jlTelefonos = new JList(modeloLista);
		}
		return jlTelefonos;
	}

	/**
	 * This method initializes btnAddTel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnAddTel() {
		if (btnAddTel == null) {
			btnAddTel = new JButton();
			btnAddTel.setText("+");
			btnAddTel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					try{
						int telefono=Integer.parseInt(tfTelefono.getText());
						
						modeloLista.addElement(telefono);
						
						tfTelefono.setText("");
						
					}catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null, "Debe introducir solo numeros en el telefono");
					}
					
				}
			});
		}
		return btnAddTel;
	}

	/**
	 * This method initializes btnDelTel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnDelTel() {
		if (btnDelTel == null) {
			btnDelTel = new JButton();
			btnDelTel.setText("-");
			btnDelTel.setMinimumSize(new Dimension(41, 26));
			btnDelTel.setPreferredSize(new Dimension(41, 26));
			btnDelTel.setMaximumSize(new Dimension(41, 26));
			btnDelTel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					int indice=jlTelefonos.getSelectedIndex();
					
					if(agregar){
						
						modeloLista.removeElementAt(indice);
						
					}
					
				}
			});
		}
		return btnDelTel;
	}

	/**
	 * This method initializes tfEmail	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfEmail() {
		if (tfEmail == null) {
			tfEmail = new JTextField();
			tfEmail.setPreferredSize(new Dimension(190, 20));
			tfEmail.setMinimumSize(new Dimension(110, 20));
		}
		return tfEmail;
	}

	/**
	 * This method initializes jlEmail	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getJlEmail() {
		if (jlEmail == null) {
			jlEmail = new JList();
		}
		return jlEmail;
	}

	/**
	 * This method initializes btnAddEmail	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnAddEmail() {
		if (btnAddEmail == null) {
			btnAddEmail = new JButton();
			btnAddEmail.setText("+");
		}
		return btnAddEmail;
	}

	/**
	 * This method initializes btnDelEmail	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnDelEmail() {
		if (btnDelEmail == null) {
			btnDelEmail = new JButton();
			btnDelEmail.setText("-");
		}
		return btnDelEmail;
	}

	/**
	 * This method initializes panelNotas	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelNotas() {
		if (panelNotas == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints12.insets = new Insets(2, 10, 2, 10);
			gridBagConstraints12.gridheight = 1;
			gridBagConstraints12.gridwidth = 3;
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.gridy = 1;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.weighty = 1.0;
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridy = 0;
			gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
			panelNotas = new JPanel();
			panelNotas.setLayout(new GridBagLayout());
			panelNotas.add(lblNotas, gridBagConstraints13);
			panelNotas.add(getTaNotas(), gridBagConstraints12);
		}
		return panelNotas;
	}

	/**
	 * This method initializes panelContenedor	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelContenedor() {
		if (panelContenedor == null) {
			panelContenedor = new JPanel();
			panelContenedor.setLayout(new BorderLayout());
			panelContenedor.add(getPanelNotas(), BorderLayout.SOUTH);
			panelContenedor.add(getJPanel(), BorderLayout.CENTER);
		}
		return panelContenedor;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 3;
			gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints21.gridy = 6;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 1;
			gridBagConstraints17.anchor = GridBagConstraints.WEST;
			gridBagConstraints17.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints17.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints17.gridy = 6;
			GridBagConstraints gridBagConstraints101 = new GridBagConstraints();
			gridBagConstraints101.gridx = 2;
			gridBagConstraints101.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints101.gridy = 6;
			lblEmail = new JLabel();
			lblEmail.setText("E-mail:");
			GridBagConstraints gridBagConstraints91 = new GridBagConstraints();
			gridBagConstraints91.gridx = 0;
			gridBagConstraints91.gridy = 6;
			lblTelefonos = new JLabel();
			lblTelefonos.setText("Telefonos:");
			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.fill = GridBagConstraints.BOTH;
			gridBagConstraints51.gridy = 8;
			gridBagConstraints51.weightx = 1.0;
			gridBagConstraints51.weighty = 1.0;
			gridBagConstraints51.gridwidth = 1;
			gridBagConstraints51.gridheight = 2;
			gridBagConstraints51.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints51.gridx = 3;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = GridBagConstraints.BOTH;
			gridBagConstraints16.gridy = 7;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.weighty = 1.0;
			gridBagConstraints16.gridwidth = 1;
			gridBagConstraints16.gridheight = 2;
			gridBagConstraints16.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints16.gridx = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints11.gridx = 3;
			gridBagConstraints11.gridy = 4;
			gridBagConstraints11.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints9.gridx = 2;
			gridBagConstraints9.gridy = 4;
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints7.gridx = 3;
			gridBagConstraints7.gridy = 2;
			gridBagConstraints7.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints2.gridx = 2;
			gridBagConstraints2.gridy = 2;
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints10.gridx = 1;
			gridBagConstraints10.gridy = 4;
			gridBagConstraints10.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridy = 4;
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 2;
			gridBagConstraints5.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 2;
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints6.gridx = 3;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.setMinimumSize(new Dimension(479, 190));
			jPanel.setPreferredSize(new Dimension(279, 144));
			jPanel.setBorder(BorderFactory.createTitledBorder(null, "Datos", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanel.add(lblDni, gridBagConstraints3);
			jPanel.add(getTfDni(), gridBagConstraints4);
			jPanel.add(lblNombre, gridBagConstraints);
			jPanel.add(getTfApellidos(), gridBagConstraints6);
			jPanel.add(lblApellidos, gridBagConstraints1);
			jPanel.add(getTfNombre(), gridBagConstraints5);
			jPanel.add(lblCiudad, gridBagConstraints8);
			jPanel.add(getTfCiudad(), gridBagConstraints10);
			jPanel.add(lblDireccion, gridBagConstraints2);
			jPanel.add(getTfDireccion(), gridBagConstraints7);
			jPanel.add(lblEmpresa, gridBagConstraints9);
			jPanel.add(getTfEmpresa(), gridBagConstraints11);
			jPanel.add(getScrollTel(), gridBagConstraints16);
			jPanel.add(getScrollEmail(), gridBagConstraints51);
			jPanel.add(lblTelefonos, gridBagConstraints91);
			jPanel.add(lblEmail, gridBagConstraints101);
			jPanel.add(getPanelAddTel(), gridBagConstraints17);
			jPanel.add(getPanelAddEmail(), gridBagConstraints21);
		}
		return jPanel;
	}

	/**
	 * This method initializes scrollTel	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollTel() {
		if (scrollTel == null) {
			scrollTel = new JScrollPane();
			scrollTel.setViewportView(getJlTelefonos());
		}
		return scrollTel;
	}

	/**
	 * This method initializes scrollEmail	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollEmail() {
		if (scrollEmail == null) {
			scrollEmail = new JScrollPane();
			scrollEmail.setViewportView(getJlEmail());
		}
		return scrollEmail;
	}

	/**
	 * This method initializes panelAddTel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelAddTel() {
		if (panelAddTel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setHgap(5);
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			flowLayout.setVgap(0);
			panelAddTel = new JPanel();
			panelAddTel.setLayout(flowLayout);
			panelAddTel.add(getTfTelefono(), null);
			panelAddTel.add(getBtnAddTel(), null);
			panelAddTel.add(getBtnDelTel(), null);
		}
		return panelAddTel;
	}

	/**
	 * This method initializes panelAddEmail	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelAddEmail() {
		if (panelAddEmail == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
			panelAddEmail = new JPanel();
			panelAddEmail.setLayout(flowLayout1);
			panelAddEmail.add(getTfEmail(), null);
			panelAddEmail.add(getBtnAddEmail(), null);
			panelAddEmail.add(getBtnDelEmail(), null);
		}
		return panelAddEmail;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

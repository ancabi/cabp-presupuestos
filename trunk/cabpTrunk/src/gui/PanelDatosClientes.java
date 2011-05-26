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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
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
import javax.swing.ImageIcon;

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
	private PreparedStatement psInsertarTel;
	private PreparedStatement psInsertarEmail;
	private PreparedStatement psBorrarTel;
	private PreparedStatement psBorrarEmail;
	private ResultSet rs;
	private PreparedStatement psActualizar;
	private JFrame mainFrame;
	private int idCliente;
	private JTextField tfTelefono = null;
	private JList jlTelefonos = null;
	private JButton btnAddTel = null;
	private JButton btnDelTel = null;
	private DefaultListModel modeloLista;
	private DefaultListModel modeloListaEmail;
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
	private JLabel lblProvincia = null;
	private JTextField tfProvincia = null;
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
				psInsertar=dbConnect.prepareStatement("INSERT INTO clientes(dni, nombre, apellidos, direccion, ciudad, provincia, empresa, notas)" +
						" VALUES (?,?,?,?,?,?,?,?)");
				
				psInsertarTel=dbConnect.prepareStatement("INSERT INTO telefonos(idCliente, telefono) VALUES (?,?)");
				
				psInsertarEmail=dbConnect.prepareStatement("INSERT INTO email(idCliente, email) VALUES (?,?)");
				
				psActualizar=dbConnect.prepareStatement("UPDATE clientes SET dni=?, nombre=?, apellidos=?, direccion=?, ciudad=?, provincia=?, empresa=?, notas=?" +
						"WHERE idCliente=?");
				
				psBorrarTel=dbConnect.prepareStatement("DELETE FROM telefonos WHERE idCliente=? AND telefono=?");
				
				psBorrarEmail=dbConnect.prepareStatement("DELETE FROM email WHERE idCliente=? AND email=?");
			} catch (SQLException e) {
				//JOptionPane.showMessageDialog(null, e.getMessage());
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
					
					//cargo los datos en variables
					String dni=tfDni.getText();
					String nombre=tfNombre.getText();
					String apellidos=tfApellidos.getText();
					String direccion=tfDireccion.getText();
					String ciudad=tfCiudad.getText();
					String provincia=tfProvincia.getText();
					String empresa=tfEmpresa.getText();
					String notas=taNotas.getText();
					
					//si es agregar la accion entonces de inserta
					if(agregar){
						
						int id;
						
						try {
							//asigno los campos al preparedStatement
							psInsertar.setString(1, dni);
							psInsertar.setString(2, nombre);
							psInsertar.setString(3, apellidos);
							psInsertar.setString(4, direccion);
							psInsertar.setString(5, ciudad);
							psInsertar.setString(6, provincia);
							psInsertar.setString(7, empresa);
							psInsertar.setString(8, notas);
							//lo inserto
							psInsertar.executeUpdate();
							//pido el id generado
							rs=psInsertar.getGeneratedKeys();
							//paso 1 ya que siempre deolvera solo 1
							rs.next();
							//guardo el id
							id=rs.getInt(1);
							//recorro el modelo de la lista
							for(int x=0; x<modeloLista.getSize(); x++){
								
								//asigno el id y el telefono, hago el parseInt sin try-catch porque ya esta controlado antes
								psInsertarTel.setInt(1, id);
								String telTemp=""+modeloLista.getElementAt(x);
								psInsertarTel.setString(2, telTemp);
								
								psInsertarTel.executeUpdate();
								
							}
							
							//recorro el modelo de la lista
							for(int x=0; x<modeloListaEmail.getSize(); x++){
								
								//asigno el id y el telefono, hago el parseInt sin try-catch porque ya esta controlado antes
								psInsertarEmail.setInt(1, id);
								String emailTemp=""+modeloListaEmail.getElementAt(x);
								psInsertarEmail.setString(2, emailTemp);
								
								psInsertarEmail.executeUpdate();
								
							}
							
							
							
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
							psActualizar.setString(6, provincia);
							psActualizar.setString(7, empresa);
							psActualizar.setString(8, notas);
							psActualizar.setInt(9, idCliente);
							
							psActualizar.executeUpdate();
							
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
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
		tfProvincia.setText("");
		tfEmpresa.setText("");
		taNotas.setText("");
		
		modeloLista.clear();
		modeloListaEmail.clear();
		
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
		
		Vector telTemp=(Vector) cliente.elementAt(4);
		Iterator i=telTemp.iterator();
		
		while(i.hasNext()){
			
			modeloLista.addElement(((String) i.next()));
			
		}
		
		Vector emailTemp=(Vector) cliente.elementAt(5);
		Iterator e=emailTemp.iterator();
		
		while(e.hasNext()){
			
			modeloListaEmail.addElement(((String) e.next()));
			
		}
		
		
		tfDireccion.setText(""+cliente.elementAt(6));
		tfCiudad.setText(""+cliente.elementAt(7));
		tfProvincia.setText(""+cliente.elementAt(8));
		tfEmpresa.setText(""+cliente.elementAt(9));
		taNotas.setText(""+cliente.elementAt(10));
		
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
			btnAddTel.setText("");
			btnAddTel.setPreferredSize(new Dimension(40, 34));
			btnAddTel.setIcon(new ImageIcon(getClass().getResource("/img/add.png")));
			btnAddTel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					String telefonoText=tfTelefono.getText();
					
					
					if(!telefonoText.equals("")){
					
						if(agregar){
							try{
								int telefono=Integer.parseInt(telefonoText);
								
								modeloLista.addElement(telefono);
								
							}catch (NumberFormatException e1) {
								JOptionPane.showMessageDialog(null, "Debe introducir solo numeros en el telefono");
							}
						}else{
	
							try {
								
								int telefono=Integer.parseInt(telefonoText);
								
								psInsertarTel.setInt(1, idCliente);
								psInsertarTel.setString(2, ""+telefono);
								
								psInsertarTel.executeUpdate();
								
								modeloLista.addElement(telefono);
								
							} catch (SQLException e1) {
								JOptionPane.showMessageDialog(null, e1.getMessage());
							} catch (NumberFormatException e1) {
								JOptionPane.showMessageDialog(null, "Debe introducir solo numeros en el telefono");
							}
							
						}
						
						tfTelefono.setText("");
					
					}else{
						try {
							throw new Exception("Debe rellenar el campo");
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
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
			btnDelTel.setText("");
			btnDelTel.setMinimumSize(new Dimension(40, 34));
			btnDelTel.setPreferredSize(new Dimension(40, 34));
			btnDelTel.setIcon(new ImageIcon(getClass().getResource("/img/delete.png")));
			btnDelTel.setMaximumSize(new Dimension(40, 34));
			btnDelTel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					
					int indice=jlTelefonos.getSelectedIndex();
					
					if(indice>-1){
					
						if(agregar){
							
							modeloLista.removeElementAt(indice);
							
						}else{
							
							String telefono=(String) modeloLista.getElementAt(indice);
								
							try {
								psBorrarTel.setInt(1, idCliente);
								psBorrarTel.setString(2, telefono);
								
								psBorrarTel.executeUpdate();
								
								modeloLista.removeElementAt(indice);
								
							} catch (SQLException e1) {
								JOptionPane.showMessageDialog(null, e1.getMessage());
							}
							
						}
					}else{
						try {
							throw new Exception("Debe seleccionar un telefono");
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
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
			
			modeloListaEmail=new DefaultListModel();
			
			jlEmail = new JList(modeloListaEmail);
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
			btnAddEmail.setText("");
			btnAddEmail.setMinimumSize(new Dimension(40, 34));
			btnAddEmail.setPreferredSize(new Dimension(40, 34));
			btnAddEmail.setIcon(new ImageIcon(getClass().getResource("/img/add.png")));
			btnAddEmail.setMaximumSize(new Dimension(40, 34));
			btnAddEmail.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					String email=tfEmail.getText();
					
					if(!email.equals("")){
						
						if(agregar){
								
								modeloListaEmail.addElement(email);
								
						}else{
	
							try {
								
								psInsertarEmail.setInt(1, idCliente);
								psInsertarEmail.setString(2, email);
								
								psInsertarEmail.executeUpdate();
								
								modeloListaEmail.addElement(email);
								
							} catch (SQLException e1) {
								JOptionPane.showMessageDialog(null, e1.getMessage());
							}
							
						}
						
						tfEmail.setText("");
					
					}else{
						try {
							throw new Exception("Debe rellenar el campo");
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
					}
					
				}
			});
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
			btnDelEmail.setText("");
			btnDelEmail.setMinimumSize(new Dimension(40, 34));
			btnDelEmail.setPreferredSize(new Dimension(40, 34));
			btnDelEmail.setIcon(new ImageIcon(getClass().getResource("/img/delete.png")));
			btnDelEmail.setMaximumSize(new Dimension(40, 34));
			btnDelEmail.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					int indice=jlEmail.getSelectedIndex();
					
					if(indice>-1){
					
						if(agregar){
							
							modeloListaEmail.removeElementAt(indice);
							
						}else{
							
							String email=(String) modeloListaEmail.getElementAt(indice);
								
							try {
								psBorrarEmail.setInt(1, idCliente);
								psBorrarEmail.setString(2, email);
								
								psBorrarEmail.executeUpdate();
								
								modeloListaEmail.removeElementAt(indice);
								
							} catch (SQLException e1) {
								JOptionPane.showMessageDialog(null, e1.getMessage());
							}
							
						}
					}else{
						try {
							throw new Exception("Debe seleccionar un E-mail");
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
					}
				}
			});
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
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints22.gridy = 4;
			gridBagConstraints22.weightx = 1.0;
			gridBagConstraints22.anchor = GridBagConstraints.WEST;
			gridBagConstraints22.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints22.gridx = 3;
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 2;
			gridBagConstraints18.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints18.gridy = 4;
			lblProvincia = new JLabel();
			lblProvincia.setText("Provincia:");
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
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.gridy = 5;
			gridBagConstraints11.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 5;
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
			gridBagConstraints5.gridx = 3;
			gridBagConstraints5.gridy = 0;
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
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 2;
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
			jPanel.add(lblProvincia, gridBagConstraints18);
			jPanel.add(getTfProvincia(), gridBagConstraints22);
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

	/**
	 * This method initializes tfProvincia	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfProvincia() {
		if (tfProvincia == null) {
			tfProvincia = new JTextField();
			tfProvincia.setPreferredSize(new Dimension(110, 20));
			tfProvincia.setMinimumSize(new Dimension(110, 20));
		}
		return tfProvincia;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

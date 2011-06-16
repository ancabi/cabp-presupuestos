/**
 * 
 */
package gui;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import javax.swing.JTextField;

import clases.Distribuidor;

import java.awt.Insets;

/**
 * @author ancabi
 *
 */
public class PanelDatosDistribuidor extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel panelSuperior = null;
	private JPanel panelDatos = null;
	private JLabel lblTitulo = null;
	private JLabel lblNombre = null;
	private JLabel lblDireccion = null;
	private JLabel lblTelefono = null;
	private JLabel lblEmail = null;
	private JLabel lblCiudad = null;
	private JLabel lblProvincia = null;
	private JLabel lblPais = null;
	private JLabel lblNumeroCuenta = null;
	private JLabel lblIban = null;
	private JLabel lblSwif = null;
	private JTextField tfNombre = null;
	private JTextField tfDireccion = null;
	private JTextField tfTelefono = null;
	private JTextField tfEmail = null;
	private JTextField tfCiudad = null;
	private JTextField tfProvincia = null;
	private JTextField tfPais = null;
	private JTextField tfNumeroCuenta = null;
	private JTextField tfIban = null;
	private JTextField tfSwif = null;
	/**
	 * This is the default constructor
	 */
	public PanelDatosDistribuidor() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(339, 324);
		this.setLayout(new BorderLayout());
		this.add(getPanelSuperior(), BorderLayout.NORTH);
		this.add(getPanelDatos(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes panelSuperior	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelSuperior() {
		if (panelSuperior == null) {
			panelSuperior = new JPanel();
			panelSuperior.setLayout(new BorderLayout());
			panelSuperior.add(getLblTitulo(), BorderLayout.NORTH);
		}
		return panelSuperior;
	}

	/**
	 * This method initializes panelDatos	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelDatos() {
		if (panelDatos == null) {
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints19.gridy = 10;
			gridBagConstraints19.weightx = 1.0;
			gridBagConstraints19.anchor = GridBagConstraints.WEST;
			gridBagConstraints19.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints19.gridx = 1;
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints18.gridy = 9;
			gridBagConstraints18.weightx = 1.0;
			gridBagConstraints18.anchor = GridBagConstraints.WEST;
			gridBagConstraints18.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints18.gridx = 1;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints17.gridy = 7;
			gridBagConstraints17.weightx = 1.0;
			gridBagConstraints17.anchor = GridBagConstraints.WEST;
			gridBagConstraints17.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints17.gridx = 1;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints16.gridy = 6;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.anchor = GridBagConstraints.WEST;
			gridBagConstraints16.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints16.gridx = 1;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints15.gridy = 5;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.anchor = GridBagConstraints.WEST;
			gridBagConstraints15.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints15.gridx = 1;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints14.gridy = 4;
			gridBagConstraints14.weightx = 1.0;
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints14.gridx = 1;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints13.gridy = 3;
			gridBagConstraints13.weightx = 1.0;
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints13.gridx = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints12.gridy = 2;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints12.gridx = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints11.gridx = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints10.gridy = 0;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints10.gridx = 1;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints9.gridy = 10;
			lblSwif = new JLabel();
			lblSwif.setText("SWIF:");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints8.gridy = 9;
			lblIban = new JLabel();
			lblIban.setText("IBAN:");
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints7.gridy = 7;
			lblNumeroCuenta = new JLabel();
			lblNumeroCuenta.setText("Numero de cuenta:");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints6.gridy = 6;
			lblPais = new JLabel();
			lblPais.setText("Pais:");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints5.gridy = 5;
			lblProvincia = new JLabel();
			lblProvincia.setText("Provincia:");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints4.gridy = 4;
			lblCiudad = new JLabel();
			lblCiudad.setText("Ciudad:");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints3.gridy = 3;
			lblEmail = new JLabel();
			lblEmail.setText("Email:");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints2.gridy = 2;
			lblTelefono = new JLabel();
			lblTelefono.setText("Telefono:");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints1.gridy = 1;
			lblDireccion = new JLabel();
			lblDireccion.setText("Direccion:");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints.gridy = 0;
			lblNombre = new JLabel();
			lblNombre.setText("Nombre:");
			panelDatos = new JPanel();
			panelDatos.setLayout(new GridBagLayout());
			panelDatos.add(lblNombre, gridBagConstraints);
			panelDatos.add(lblDireccion, gridBagConstraints1);
			panelDatos.add(lblTelefono, gridBagConstraints2);
			panelDatos.add(lblEmail, gridBagConstraints3);
			panelDatos.add(lblCiudad, gridBagConstraints4);
			panelDatos.add(lblProvincia, gridBagConstraints5);
			panelDatos.add(lblPais, gridBagConstraints6);
			panelDatos.add(lblNumeroCuenta, gridBagConstraints7);
			panelDatos.add(lblIban, gridBagConstraints8);
			panelDatos.add(lblSwif, gridBagConstraints9);
			panelDatos.add(getTfNombre(), gridBagConstraints10);
			panelDatos.add(getTfDireccion(), gridBagConstraints11);
			panelDatos.add(getTfTelefono(), gridBagConstraints12);
			panelDatos.add(getTfEmail(), gridBagConstraints13);
			panelDatos.add(getTfCiudad(), gridBagConstraints14);
			panelDatos.add(getTfProvincia(), gridBagConstraints15);
			panelDatos.add(getTfPais(), gridBagConstraints16);
			panelDatos.add(getTfNumeroCuenta(), gridBagConstraints17);
			panelDatos.add(getTfIban(), gridBagConstraints18);
			panelDatos.add(getTfSwif(), gridBagConstraints19);
		}
		return panelDatos;
	}

	/**
	 * This method initializes lblTitulo	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getLblTitulo() {
		if (lblTitulo == null) {
			lblTitulo = new JLabel();
			lblTitulo.setText("Agregar distribuidor");
			lblTitulo.setFont(new Font("Verdana", Font.BOLD, 18));
		}
		return lblTitulo;
	}

	/**
	 * This method initializes tfNombre	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfNombre() {
		if (tfNombre == null) {
			tfNombre = new JTextField();
			tfNombre.setPreferredSize(new Dimension(160, 20));
			tfNombre.setMinimumSize(new Dimension(160, 20));
		}
		return tfNombre;
	}

	/**
	 * This method initializes tfDireccion	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfDireccion() {
		if (tfDireccion == null) {
			tfDireccion = new JTextField();
			tfDireccion.setPreferredSize(new Dimension(160, 20));
			tfDireccion.setMinimumSize(new Dimension(160, 20));
		}
		return tfDireccion;
	}

	/**
	 * This method initializes tfTelefono	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfTelefono() {
		if (tfTelefono == null) {
			tfTelefono = new JTextField();
			tfTelefono.setPreferredSize(new Dimension(160, 20));
			tfTelefono.setMinimumSize(new Dimension(160, 20));
		}
		return tfTelefono;
	}

	/**
	 * This method initializes tfEmail	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfEmail() {
		if (tfEmail == null) {
			tfEmail = new JTextField();
			tfEmail.setPreferredSize(new Dimension(160, 20));
			tfEmail.setMinimumSize(new Dimension(160, 20));
		}
		return tfEmail;
	}

	/**
	 * This method initializes tfCiudad	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfCiudad() {
		if (tfCiudad == null) {
			tfCiudad = new JTextField();
			tfCiudad.setPreferredSize(new Dimension(160, 20));
			tfCiudad.setMinimumSize(new Dimension(160, 20));
		}
		return tfCiudad;
	}

	/**
	 * This method initializes tfProvincia	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfProvincia() {
		if (tfProvincia == null) {
			tfProvincia = new JTextField();
			tfProvincia.setPreferredSize(new Dimension(160, 20));
			tfProvincia.setMinimumSize(new Dimension(160, 20));
		}
		return tfProvincia;
	}

	/**
	 * This method initializes tfPais	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfPais() {
		if (tfPais == null) {
			tfPais = new JTextField();
			tfPais.setPreferredSize(new Dimension(160, 20));
			tfPais.setMinimumSize(new Dimension(160, 20));
		}
		return tfPais;
	}

	/**
	 * This method initializes tfNumeroCuenta	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfNumeroCuenta() {
		if (tfNumeroCuenta == null) {
			tfNumeroCuenta = new JTextField();
			tfNumeroCuenta.setPreferredSize(new Dimension(160, 20));
			tfNumeroCuenta.setMinimumSize(new Dimension(160, 20));
		}
		return tfNumeroCuenta;
	}

	/**
	 * This method initializes tfIban	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfIban() {
		if (tfIban == null) {
			tfIban = new JTextField();
			tfIban.setPreferredSize(new Dimension(160, 20));
			tfIban.setMinimumSize(new Dimension(160, 20));
		}
		return tfIban;
	}

	/**
	 * This method initializes tfSwif	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfSwif() {
		if (tfSwif == null) {
			tfSwif = new JTextField();
			tfSwif.setPreferredSize(new Dimension(160, 20));
			tfSwif.setMinimumSize(new Dimension(160, 20));
		}
		return tfSwif;
	}

	public Distribuidor getNewDistribuidor() {
		
		String nombre=tfNombre.getText();
		String direccion=tfDireccion.getText();
		String telefono=tfTelefono.getText();
		String email=tfEmail.getText();
		String ciudad=tfCiudad.getText();
		String provincia=tfProvincia.getText();
		String pais=tfPais.getText();
		String numeroCta=tfNumeroCuenta.getText();
		String iban=tfIban.getText();
		String swif=tfSwif.getText();
		
		Distribuidor d=new Distribuidor(nombre, direccion, email, telefono, ciudad, provincia, pais, numeroCta, iban, swif);
		
		return d;
	}
	
	public void limpiarCampos(){
		
		tfNombre.setText("");
		tfDireccion.setText("");
		tfTelefono.setText("");
		tfEmail.setText("");
		tfCiudad.setText("");
		tfProvincia.setText("");
		tfPais.setText("");
		tfNumeroCuenta.setText("");
		tfIban.setText("");
		tfSwif.setText("");
		
		lblTitulo.setText("Agregar distribuidor");
	}

	public void setDistribuidor(Distribuidor d) {
		
		tfNombre.setText(d.getNombre());
		tfDireccion.setText(d.getDireccion());
		tfTelefono.setText(d.getTelefono());
		tfEmail.setText(d.getEmail());
		tfCiudad.setText(d.getCiudad());
		tfProvincia.setText(d.getProvincia());
		tfPais.setText(d.getPais());
		tfNumeroCuenta.setText(d.getNumeroCta());
		tfIban.setText(d.getIban());
		tfSwif.setText(d.getSwif());
		
		lblTitulo.setText("Modificar distribuidor");
		
	}

	public void guardarDistribuidor(Distribuidor d) {
		
		String nombre=tfNombre.getText();
		String direccion=tfDireccion.getText();
		String telefono=tfTelefono.getText();
		String email=tfEmail.getText();
		String ciudad=tfCiudad.getText();
		String provincia=tfProvincia.getText();
		String pais=tfPais.getText();
		String numeroCta=tfNumeroCuenta.getText();
		String iban=tfIban.getText();
		String swif=tfSwif.getText();
		
		d.setNombre(nombre);
		d.setDireccion(direccion);
		d.setEmail(email);
		d.setTelefono(telefono);
		d.setCiudad(ciudad);
		d.setProvincia(provincia);
		d.setPais(pais);
		d.setNumeroCta(numeroCta);
		d.setIban(iban);
		d.setSwif(swif);
		
	}

	public void comprobarCampos() throws Exception {
		
		if(tfNombre.getText().equals("")){
			throw new Exception("Debe completar el nombre como minimo");
		}
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

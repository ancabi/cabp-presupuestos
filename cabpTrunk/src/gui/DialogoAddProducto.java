/**
 * 
 */
package gui;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.Font;

/**
 * @author ancabi
 *
 */
public class DialogoAddProducto extends JDialog {

	private static final long serialVersionUID = 1L;
	public static final int VALOR_ACEPTAR=1;
	public static final int VALOR_CANCELAR=0;
	private JPanel jContentPane = null;
	private JPanel panelDatosProducto = null;
	private JPanel panelBotones = null;
	private JButton btnAceptar = null;
	private JButton btnCancelar = null;
	private JLabel lblNombre = null;
	private JLabel lblPrecio = null;
	private JTextField tfNombre = null;
	private JTextField tfPrecio = null;
	private String nombre;
	private double precio;
	private int valorPulsado=0;
	private JLabel lblTitulo = null;
	/**
	 * @param owner
	 */
	public DialogoAddProducto(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(346, 206);
		this.setTitle("Agregar producto");
		this.setContentPane(getJContentPane());
		this.setModal(true);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				valorPulsado=VALOR_CANCELAR;
				
				setVisible(false);
				
				limpiarCampos();
			}
		});
		this.setLocationRelativeTo(getOwner());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getPanelDatosProducto(), BorderLayout.CENTER);
			jContentPane.add(getPanelBotones(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes panelDatosProducto	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelDatosProducto() {
		if (panelDatosProducto == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridwidth = 2;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.insets = new Insets(0, 5, 15, 0);
			gridBagConstraints11.gridy = 0;
			lblTitulo = new JLabel();
			lblTitulo.setText("Agregar producto");
			lblTitulo.setFont(new Font("Verdana", Font.BOLD, 18));
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints3.gridy = 2;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints2.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints1.gridy = 2;
			lblPrecio = new JLabel();
			lblPrecio.setText("Precio:");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints.gridy = 1;
			lblNombre = new JLabel();
			lblNombre.setText("Nombre:");
			panelDatosProducto = new JPanel();
			panelDatosProducto.setLayout(new GridBagLayout());
			panelDatosProducto.add(lblNombre, gridBagConstraints);
			panelDatosProducto.add(lblPrecio, gridBagConstraints1);
			panelDatosProducto.add(getTfNombre(), gridBagConstraints2);
			panelDatosProducto.add(getTfPrecio(), gridBagConstraints3);
			panelDatosProducto.add(lblTitulo, gridBagConstraints11);
		}
		return panelDatosProducto;
	}

	/**
	 * This method initializes panelBotones	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout());
			panelBotones.add(getBtnAceptar(), null);
			panelBotones.add(getBtnCancelar(), null);
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
					
					try{
					precio=Double.parseDouble(tfPrecio.getText());
					
					nombre=tfNombre.getText();
					
					valorPulsado=VALOR_ACEPTAR;
					
					setVisible(false);
					
					limpiarCampos();
					
					}catch(NumberFormatException e1){
						
						JOptionPane.showMessageDialog(null, "Debe poner un numero valido");
						
					}
				}

				
			});
		}
		return btnAceptar;
	}
	
	private void limpiarCampos() {
		tfNombre.setText("");
		tfPrecio.setText("");
		
	}

	/**
	 * This method initializes btnCancelar	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton();
			btnCancelar.setText("Cancelar");
			btnCancelar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					valorPulsado=VALOR_CANCELAR;
					
					setVisible(false);
					
					limpiarCampos();
					
				}
			});
		}
		return btnCancelar;
	}

	/**
	 * This method initializes tfNombre	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfNombre() {
		if (tfNombre == null) {
			tfNombre = new JTextField();
			tfNombre.setPreferredSize(new Dimension(240, 20));
			tfNombre.setMinimumSize(new Dimension(220, 20));
		}
		return tfNombre;
	}

	/**
	 * This method initializes tfPrecio	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfPrecio() {
		if (tfPrecio == null) {
			tfPrecio = new JTextField();
			tfPrecio.setMinimumSize(new Dimension(100, 20));
			tfPrecio.setPreferredSize(new Dimension(100, 20));
		}
		return tfPrecio;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @return the precio
	 */
	public double getPrecio() {
		return precio;
	}

	/**
	 * @return the valorPulsado
	 */
	public int getValorPulsado() {
		return valorPulsado;
	}
	
	

}  //  @jve:decl-index=0:visual-constraint="431,80"

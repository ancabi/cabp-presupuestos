/**
 * 
 */
package gui;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Dimension;

/**
 * @author ancabi
 *
 */
public class DialogoSeleccionProveedor extends JDialog {

	private static final long serialVersionUID = 1L;
	public static final int VALOR_ACEPTAR=1;
	public static final int VALOR_CANCELAR=0;
	private JPanel jContentPane = null;
	private JPanel panelBotones = null;
	private JPanel panelSeleccion = null;
	private JLabel lblSeleccion = null;
	private JComboBox cbProveedor = null;
	private JButton btnAceptar = null;
	private JButton btnCancelar = null;
	private JFrame mainFrame=null;
	private int valorPulsado=0;
	/**
	 * @param owner
	 */
	public DialogoSeleccionProveedor(Frame owner, JFrame mainFrame) {
		super(owner);
		initialize();
		this.mainFrame=mainFrame;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 156);
		this.setTitle("Seleccione un proveedor");
		this.setContentPane(getJContentPane());
		this.setModal(true);
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
			jContentPane.add(getPanelBotones(), BorderLayout.SOUTH);
			jContentPane.add(getPanelSeleccion(), BorderLayout.CENTER);
		}
		return jContentPane;
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
	 * This method initializes panelSeleccion	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelSeleccion() {
		if (panelSeleccion == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints.gridy = 0;
			lblSeleccion = new JLabel();
			lblSeleccion.setText("Seleccione un proveedor:");
			panelSeleccion = new JPanel();
			panelSeleccion.setLayout(new GridBagLayout());
			panelSeleccion.add(lblSeleccion, gridBagConstraints);
			panelSeleccion.add(getCbProveedor(), gridBagConstraints1);
		}
		return panelSeleccion;
	}

	/**
	 * This method initializes cbProveedor	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbProveedor() {
		if (cbProveedor == null) {
			cbProveedor = new JComboBox();
		}
		return cbProveedor;
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
					
					valorPulsado=VALOR_ACEPTAR;
					
					setVisible(false);
					
				}
			});
		}
		return btnAceptar;
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
		}
		return btnCancelar;
	}

}  //  @jve:decl-index=0:visual-constraint="435,96"

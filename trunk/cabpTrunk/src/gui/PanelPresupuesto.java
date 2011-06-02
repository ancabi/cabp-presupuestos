/**
 * 
 */
package gui;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * @author ancabi
 *
 */
public class PanelPresupuesto extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel panelTitulo = null;
	private JPanel panelDatos = null;
	private JLabel lblTitulo = null;
	/**
	 * This is the default constructor
	 */
	public PanelPresupuesto() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(654, 439);
		this.setLayout(new BorderLayout());
		this.add(getPanelTitulo(), BorderLayout.NORTH);
		this.add(getPanelDatos(), BorderLayout.CENTER);
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
	 * This method initializes panelDatos	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelDatos() {
		if (panelDatos == null) {
			panelDatos = new JPanel();
			panelDatos.setLayout(new GridBagLayout());
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
			lblTitulo.setText("Presupuestos");
			lblTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
		}
		return lblTitulo;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

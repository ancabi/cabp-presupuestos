package gui;

import javax.swing.JTabbedPane;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.GridBagLayout;

public class TabCliente extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	private JPanel panelDatos = null;
	private JPanel panelClientePres = null;

	/**
	 * This is the default constructor
	 */
	public TabCliente() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(548, 334);

		this.addTab("Datos", null, getPanelDatos(), null);
		this.addTab("Presupuestos", null, getPanelClientePres(), null);
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
	 * This method initializes panelClientePres	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelClientePres() {
		if (panelClientePres == null) {
			panelClientePres = new JPanel();
			panelClientePres.setLayout(new GridBagLayout());
		}
		return panelClientePres;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

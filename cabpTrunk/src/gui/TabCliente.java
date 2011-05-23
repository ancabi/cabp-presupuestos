package gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.sql.Connection;
import java.util.Vector;

public class TabCliente extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	private JPanel panelDatos = null;
	private JPanel panelClientePres = null;
	private Connection dbConnect;
	private JFrame mainFrame;

	/**
	 * This is the default constructor
	 */
	public TabCliente(Connection con, JFrame mainFrame) {
		super();
		
		dbConnect=con;
		this.mainFrame=mainFrame;
		
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
			panelDatos = new PanelDatosClientes(dbConnect, mainFrame);
			
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
	
	public void rowSelected(Vector row){
		
		((PanelDatosClientes) panelDatos).setCliente(row);
		
		((PanelDatosClientes) panelDatos).setAgregar();
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

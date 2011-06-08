/**
 * 
 */
package gui;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JTree;

import clases.ListadoPresupuestos;

/**
 * @author ancabi
 *
 */
public class PanelClientePres extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTree treePresupuestos = null;
	private JPanel panelPresupuesto = null;
	private ListadoPresupuestos presupuestos=new ListadoPresupuestos();

	/**
	 * This is the default constructor
	 */
	public PanelClientePres() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(747, 488);
		this.setLayout(new BorderLayout());
		this.add(getTreePresupuestos(), BorderLayout.WEST);
		this.add(getPanelPresupuesto(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes treePresupuestos	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getTreePresupuestos() {
		if (treePresupuestos == null) {
			treePresupuestos = new JTree();
		}
		return treePresupuestos;
	}

	/**
	 * This method initializes panelPresupuesto	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelPresupuesto() {
		if (panelPresupuesto == null) {
			panelPresupuesto = new PanelPresupuesto();
		}
		return panelPresupuesto;
	}
	
	public void setIdCliente(int idCliente){
		
		presupuestos.setIdCliente(idCliente);
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

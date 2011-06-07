/**
 * 
 */
package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JButton;

import clases.ListadoProductos;

import java.awt.GridBagConstraints;
import java.awt.FlowLayout;

/**
 * @author ancabi
 *
 */
public class DialogoAddPresupuesto extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel panelBotones = null;
	private JPanel panelPresupuesto = null;
	private JButton btnAceptar = null;
	private JButton btnCancelar = null;
	private int idCliente;
	private int idDistribuidor=1;
	private JFrame mainFrame;

	/**
	 * @param owner
	 */
	public DialogoAddPresupuesto(Frame owner, JFrame mainFrame) {
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
		this.setSize(846, 663);
		this.setResizable(false);
		this.setTitle("Agregar presupuesto");
		this.setMinimumSize(new Dimension(775, 441));
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
			jContentPane.add(getPanelPresupuesto(), BorderLayout.CENTER);
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

	/**
	 * This method initializes btnAceptar	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton();
			btnAceptar.setText("Aceptar");
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
	
	
	
	/**
	 * @return the idCliente
	 */
	public int getIdCliente() {
		return idCliente;
	}

	/**
	 * @param idCliente the idCliente to set
	 */
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	/**
	 * @return the idDistribuidor
	 */
	public int getIdDistribuidor() {
		return idDistribuidor;
	}

	/**
	 * @param idDistribuidor the idDistribuidor to set
	 */
	public void setIdDistribuidor(int idDistribuidor) {
		this.idDistribuidor = idDistribuidor;
	}

	public void cargarProductos(ListadoProductos listado){
		
		((PanelPresupuesto) panelPresupuesto).cargarProductos(listado);
		
	}

}  //  @jve:decl-index=0:visual-constraint="12,-18"

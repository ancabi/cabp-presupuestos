package gui;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.BoxLayout;
import javax.swing.JButton;

public class PanelUser extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private JPanel paneBtnUser = null;  //  @jve:decl-index=0:visual-constraint="786,145"
	private JButton btnAgregar = null;
	private JButton jButton = null;
	private JButton jButton1 = null;

	/**
	 * This is the default constructor
	 */
	public PanelUser() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(758, 507);
		this.setLayout(new BorderLayout());
		this.add(getJScrollPane(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTable() {
		if (jTable == null) {
			jTable = new JTable();
		}
		return jTable;
	}

	/**
	 * This method initializes paneBtnUser	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPaneBtnUser() {
		if (paneBtnUser == null) {
			paneBtnUser = new JPanel();
			paneBtnUser.setLayout(new BoxLayout(getPaneBtnUser(), BoxLayout.Y_AXIS));
			paneBtnUser.setSize(new Dimension(204, 214));
			paneBtnUser.add(getBtnAgregar(), null);
			paneBtnUser.add(getJButton(), null);
			paneBtnUser.add(getJButton1(), null);
		}
		return paneBtnUser;
	}

	/**
	 * This method initializes btnAgregar	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnAgregar() {
		if (btnAgregar == null) {
			btnAgregar = new JButton();
			btnAgregar.setText("Agregar");
			btnAgregar.setMaximumSize(new Dimension(85, 25));
			btnAgregar.setMinimumSize(new Dimension(85, 25));
		}
		return btnAgregar;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Buscar");
			jButton.setMinimumSize(new Dimension(85, 25));
			jButton.setMaximumSize(new Dimension(85, 25));
			jButton.setPreferredSize(new Dimension(85, 25));
		}
		return jButton;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("Eliminar");
		}
		return jButton1;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

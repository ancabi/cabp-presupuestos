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
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Insets;
import javax.swing.JTextField;

/**
 * @author ancabi
 *
 */
public class PanelPresupuesto extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel panelTitulo = null;
	private JPanel panelDatos = null;
	private JLabel lblTitulo = null;
	private JPanel panelProductos = null;
	private JComboBox cbProductos = null;
	private JScrollPane scrollTablaProductos = null;
	private JTable tablaProductos = null;
	private JPanel panelGastos = null;
	private JLabel lblNeto = null;
	private JLabel lblPrecioNeto = null;
	private JLabel lblIvaEmpresa = null;
	private JLabel lblIvaCalEmp = null;
	private JLabel lblTotalEmpresa = null;
	private JLabel lblTotalIvaEmpresa = null;
	private JLabel lblGanancia = null;
	private JTextField tfGanancia = null;
	private JLabel lblTransporte = null;
	private JTextField tfTransporte = null;
	private JPanel panelGastosExtra = null;
	private JLabel lblHotel = null;
	private JTextField tfHotel = null;
	private JLabel lblPasaje = null;
	private JTextField tfPasaje = null;
	private JLabel lblOtros = null;
	private JTextField tfOtros = null;
	private JLabel lblRestaurante = null;
	private JTextField tfRestaurante = null;
	private JLabel lblCombustible = null;
	private JTextField tfCombustible = null;
	private JLabel lblKm = null;
	private JTextField tfKm = null;
	private JLabel lblGastos = null;
	private JLabel lblTotGastos = null;
	private JLabel lblTotalGastos = null;
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
		this.setSize(1030, 586);
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
			panelDatos.setLayout(new BorderLayout());
			panelDatos.add(getPanelProductos(), BorderLayout.NORTH);
			panelDatos.add(getPanelGastos(), BorderLayout.CENTER);
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

	/**
	 * This method initializes panelProductos	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelProductos() {
		if (panelProductos == null) {
			panelProductos = new JPanel();
			panelProductos.setLayout(new BorderLayout());
			panelProductos.add(getCbProductos(), BorderLayout.NORTH);
			panelProductos.add(getScrollTablaProductos(), BorderLayout.CENTER);
		}
		return panelProductos;
	}

	/**
	 * This method initializes cbProductos	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbProductos() {
		if (cbProductos == null) {
			cbProductos = new JComboBox();
		}
		return cbProductos;
	}

	/**
	 * This method initializes scrollTablaProductos	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollTablaProductos() {
		if (scrollTablaProductos == null) {
			scrollTablaProductos = new JScrollPane();
			scrollTablaProductos.setPreferredSize(new Dimension(453, 100));
			scrollTablaProductos.setViewportView(getTablaProductos());
		}
		return scrollTablaProductos;
	}

	/**
	 * This method initializes tablaProductos	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTablaProductos() {
		if (tablaProductos == null) {
			tablaProductos = new JTable();
		}
		return tablaProductos;
	}

	/**
	 * This method initializes panelGastos	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelGastos() {
		if (panelGastos == null) {
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 1;
			gridBagConstraints31.anchor = GridBagConstraints.WEST;
			gridBagConstraints31.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints31.gridy = 5;
			lblTotalGastos = new JLabel();
			lblTotalGastos.setText("0 €");
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.gridx = 0;
			gridBagConstraints23.anchor = GridBagConstraints.WEST;
			gridBagConstraints23.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints23.gridy = 5;
			lblTotGastos = new JLabel();
			lblTotGastos.setText("Total gastos:");
			GridBagConstraints gridBagConstraints110 = new GridBagConstraints();
			gridBagConstraints110.gridx = 2;
			gridBagConstraints110.gridy = 2;
			lblGastos = new JLabel();
			lblGastos.setText("Gastos");
			lblGastos.setFont(new Font("Dialog", Font.BOLD, 18));
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 2;
			gridBagConstraints10.gridheight = 2;
			gridBagConstraints10.gridy = 3;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints9.gridy = 4;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints9.gridx = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints8.gridy = 4;
			lblTransporte = new JLabel();
			lblTransporte.setText("Transporte:");
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints7.gridy = 3;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints6.gridy = 3;
			lblGanancia = new JLabel();
			lblGanancia.setText("Ganancia:");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints5.gridy = 2;
			lblTotalIvaEmpresa = new JLabel();
			lblTotalIvaEmpresa.setText("0 €");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints4.gridy = 2;
			lblTotalEmpresa = new JLabel();
			lblTotalEmpresa.setText("Total con IVA:");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints3.gridy = 1;
			lblIvaCalEmp = new JLabel();
			lblIvaCalEmp.setText("0 €");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints2.gridy = 1;
			lblIvaEmpresa = new JLabel();
			lblIvaEmpresa.setText("IVA 7%:");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 0;
			lblPrecioNeto = new JLabel();
			lblPrecioNeto.setText("0 €");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			lblNeto = new JLabel();
			lblNeto.setText("Precio neto:");
			panelGastos = new JPanel();
			panelGastos.setLayout(new GridBagLayout());
			panelGastos.add(lblNeto, gridBagConstraints);
			panelGastos.add(lblPrecioNeto, gridBagConstraints1);
			panelGastos.add(lblIvaEmpresa, gridBagConstraints2);
			panelGastos.add(lblIvaCalEmp, gridBagConstraints3);
			panelGastos.add(lblTotalEmpresa, gridBagConstraints4);
			panelGastos.add(lblTotalIvaEmpresa, gridBagConstraints5);
			panelGastos.add(lblGanancia, gridBagConstraints6);
			panelGastos.add(getTfGanancia(), gridBagConstraints7);
			panelGastos.add(lblTransporte, gridBagConstraints8);
			panelGastos.add(getTfTransporte(), gridBagConstraints9);
			panelGastos.add(getPanelGastosExtra(), gridBagConstraints10);
			panelGastos.add(lblGastos, gridBagConstraints110);
			panelGastos.add(lblTotGastos, gridBagConstraints23);
			panelGastos.add(lblTotalGastos, gridBagConstraints31);
		}
		return panelGastos;
	}

	/**
	 * This method initializes tfGanancia	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfGanancia() {
		if (tfGanancia == null) {
			tfGanancia = new JTextField();
			tfGanancia.setMinimumSize(new Dimension(100, 20));
			tfGanancia.setPreferredSize(new Dimension(100, 20));
		}
		return tfGanancia;
	}

	/**
	 * This method initializes tfTransporte	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfTransporte() {
		if (tfTransporte == null) {
			tfTransporte = new JTextField();
			tfTransporte.setMinimumSize(new Dimension(100, 20));
			tfTransporte.setPreferredSize(new Dimension(100, 20));
		}
		return tfTransporte;
	}

	/**
	 * This method initializes panelGastosExtra	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelGastosExtra() {
		if (panelGastosExtra == null) {
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints22.gridy = 1;
			gridBagConstraints22.weightx = 1.0;
			gridBagConstraints22.anchor = GridBagConstraints.WEST;
			gridBagConstraints22.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints22.gridx = 5;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 4;
			gridBagConstraints21.anchor = GridBagConstraints.WEST;
			gridBagConstraints21.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints21.gridy = 1;
			lblKm = new JLabel();
			lblKm.setText("Kilometros:");
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints20.gridy = 1;
			gridBagConstraints20.weightx = 1.0;
			gridBagConstraints20.anchor = GridBagConstraints.WEST;
			gridBagConstraints20.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints20.gridx = 3;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.gridx = 2;
			gridBagConstraints19.anchor = GridBagConstraints.WEST;
			gridBagConstraints19.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints19.gridy = 1;
			lblCombustible = new JLabel();
			lblCombustible.setText("Combustible:");
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints18.gridy = 1;
			gridBagConstraints18.weightx = 1.0;
			gridBagConstraints18.anchor = GridBagConstraints.WEST;
			gridBagConstraints18.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints18.gridx = 1;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 0;
			gridBagConstraints17.anchor = GridBagConstraints.WEST;
			gridBagConstraints17.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints17.gridy = 1;
			lblRestaurante = new JLabel();
			lblRestaurante.setText("Restaurante:");
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints16.gridy = 0;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.anchor = GridBagConstraints.WEST;
			gridBagConstraints16.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints16.gridx = 5;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 4;
			gridBagConstraints15.anchor = GridBagConstraints.WEST;
			gridBagConstraints15.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints15.gridy = 0;
			lblOtros = new JLabel();
			lblOtros.setText("Otros:");
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints14.gridy = 0;
			gridBagConstraints14.weightx = 1.0;
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints14.gridx = 3;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 2;
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints13.gridy = 0;
			lblPasaje = new JLabel();
			lblPasaje.setText("Pasaje:");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(2, 10, 2, 2);
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints11.weightx = 1.0;
			panelGastosExtra = new JPanel();
			panelGastosExtra.setLayout(new GridBagLayout());
			panelGastosExtra.add(getLblHotel(), gridBagConstraints12);
			panelGastosExtra.add(getTfHotel(), gridBagConstraints11);
			panelGastosExtra.add(lblPasaje, gridBagConstraints13);
			panelGastosExtra.add(getTfPasaje(), gridBagConstraints14);
			panelGastosExtra.add(lblOtros, gridBagConstraints15);
			panelGastosExtra.add(getTfOtros(), gridBagConstraints16);
			panelGastosExtra.add(lblRestaurante, gridBagConstraints17);
			panelGastosExtra.add(getTfRestaurante(), gridBagConstraints18);
			panelGastosExtra.add(lblCombustible, gridBagConstraints19);
			panelGastosExtra.add(getTfCombustible(), gridBagConstraints20);
			panelGastosExtra.add(lblKm, gridBagConstraints21);
			panelGastosExtra.add(getTfKm(), gridBagConstraints22);
		}
		return panelGastosExtra;
	}

	/**
	 * This method initializes lblHotel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getLblHotel() {
		if (lblHotel == null) {
			lblHotel = new JLabel();
			lblHotel.setText("Hotel:");
		}
		return lblHotel;
	}

	/**
	 * This method initializes tfHotel	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfHotel() {
		if (tfHotel == null) {
			tfHotel = new JTextField();
			tfHotel.setMinimumSize(new Dimension(75, 20));
			tfHotel.setPreferredSize(new Dimension(75, 20));
		}
		return tfHotel;
	}

	/**
	 * This method initializes tfPasaje	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfPasaje() {
		if (tfPasaje == null) {
			tfPasaje = new JTextField();
			tfPasaje.setMinimumSize(new Dimension(75, 20));
			tfPasaje.setPreferredSize(new Dimension(75, 20));
		}
		return tfPasaje;
	}

	/**
	 * This method initializes tfOtros	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfOtros() {
		if (tfOtros == null) {
			tfOtros = new JTextField();
			tfOtros.setMinimumSize(new Dimension(75, 20));
			tfOtros.setPreferredSize(new Dimension(75, 20));
		}
		return tfOtros;
	}

	/**
	 * This method initializes tfRestaurante	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfRestaurante() {
		if (tfRestaurante == null) {
			tfRestaurante = new JTextField();
			tfRestaurante.setPreferredSize(new Dimension(75, 20));
			tfRestaurante.setMinimumSize(new Dimension(75, 20));
		}
		return tfRestaurante;
	}

	/**
	 * This method initializes tfCombustible	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfCombustible() {
		if (tfCombustible == null) {
			tfCombustible = new JTextField();
			tfCombustible.setMinimumSize(new Dimension(75, 20));
			tfCombustible.setPreferredSize(new Dimension(75, 20));
		}
		return tfCombustible;
	}

	/**
	 * This method initializes tfKm	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfKm() {
		if (tfKm == null) {
			tfKm = new JTextField();
			tfKm.setMinimumSize(new Dimension(75, 20));
			tfKm.setPreferredSize(new Dimension(75, 20));
		}
		return tfKm;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

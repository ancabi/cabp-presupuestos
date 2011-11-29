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

import net.sf.jasperreports.engine.*;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JCheckBox;

import clases.Cliente;
import clases.Facturas;
import clases.ListadoLineaFactura;
import clases.ListadoLineaPresup;
import clases.ListadoPdf;
import clases.ListadoProductos;
import clases.Presupuestos;
import clases.Productos;

import modelo.ModeloProductos;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import javax.swing.JViewport;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;

/**
 * @author ancabi
 *
 */
public class PanelPresupuesto extends JPanel {

	private static final long serialVersionUID = 1L;
	private final double IVA=0.08;
	private final double IMPUESTOgANANCIA=0.2;
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
	private JLabel lblTotGastos = null;
	private JLabel lblTotalGastos = null;
	private JPanel panelKm = null;
	private JLabel lblViajes = null;
	private JTextField tfViajes = null;
	private JLabel lblPrecioGasolina = null;
	private JTextField tfPrecioGasolina = null;
	private JLabel lblTotal = null;
	private JLabel lblTotalViaje = null;
	private JLabel lblSubTotal = null;
	private JLabel lblTotalSinImp = null;
	private JLabel lblTituloIva = null;
	private JLabel lblIva = null;
	private JLabel lblTituloImpGan = null;
	private JLabel lblImpGan = null;
	private JLabel sinIva = null;
	private JLabel lblTotalSinIva = null;
	private JLabel totalConIva = null;
	private JLabel lblTotalConIva = null;
	private JCheckBox cbGanancia = null;
	private JPanel panelPorcentaje = null;
	private JTextField tfPorcentaje1 = null;
	private JTextField tfPorcentaje2 = null;
	private JLabel lblPorcentaje1 = null;
	private JLabel lblPorcentaje2 = null;
	private JLabel totalPorcentaje = null;
	private JLabel lblPorcentaje100 = null;
	private ModeloProductos modelo;
	private DefaultComboBoxModel modeloCB;
	private double totalIva=0;
	private int porcentaje1;
	private int ganancia;
	private int hotel;
	private int pasaje;
	private int otros;
	private int restaurante;
	private int combustible;
	private boolean isGanancia;
	private boolean isCanarias;
	private int transporte;
	private JScrollPane scrollTexto = null;
	private JTextArea taTexto = null;
	private int kilometros=0;
	private int nViajes=0;
	private double precioGasolina=0.0;
	private double totalSinIva;
	private DecimalFormat formateador = new DecimalFormat ("#####.##");  //  @jve:decl-index=0:
	private JPanel panelHerramientas = null;
	private JButton btnBorrar = null;
	private boolean isPresupuesto=true;
	private JButton btnPrint = null;
	private int id;
	private int idCliente;
	private Cliente c;
	private JPanel panelCuadrados = null;
	private JPanel panelTextos = null;
	private JTextArea taFormaPago = null;
	private JTextArea taExplicativo = null;
	private JLabel lblFormaPago = null;
	private JLabel lblExplicativo = null;
	private JCheckBox cbCanarias = null;
	private JLabel lblFecha = null;
	private JPanel panelFecha = null;
	private JLabel lblSeparador = null;
	private SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd/MM/yyyy");
	private JScrollPane scrollFormaPago = null;
	private JScrollPane scrollExplicativo = null;
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
		this.setSize(1149, 606);
		this.setLayout(new BorderLayout());
		this.add(getPanelTitulo(), BorderLayout.NORTH);
		this.add(getPanelDatos(), BorderLayout.CENTER);
		this.add(getScrollTexto(), BorderLayout.SOUTH);
		this.add(getPanelCuadrados(), BorderLayout.EAST);
	}

	/**
	 * This method initializes panelTitulo	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelTitulo() {
		if (panelTitulo == null) {
			lblFecha = new JLabel();
			java.util.Date date = new Date();
		    java.sql.Date fecha = new java.sql.Date(date.getTime());
			lblFecha.setText(formateadorFecha.format(fecha));
			lblFecha.setFont(new Font("Dialog", Font.BOLD, 18));
			panelTitulo = new JPanel();
			panelTitulo.setLayout(new BorderLayout());
			panelTitulo.setMinimumSize(new Dimension(1024, 44));
			panelTitulo.setMaximumSize(new Dimension(1024, 44));
			panelTitulo.setPreferredSize(new Dimension(1024, 44));
			panelTitulo.add(getLblTitulo(), BorderLayout.WEST);
			panelTitulo.add(getPanelHerramientas(), BorderLayout.EAST);
			panelTitulo.add(getPanelFecha(), BorderLayout.CENTER);
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
			lblTitulo.setText("Presupuesto");
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
			
			modeloCB=getModeloCB();
			
			cbProductos = new JComboBox(modeloCB);
			cbProductos.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					try {
						//si no es 0, entra, ya que 0 esta vacio
						if(cbProductos.getSelectedIndex()>0){
							//cargo el producto que esta en el modelo del comboBox
							Productos p=(Productos) cbProductos.getSelectedItem();
							//contstruyo un vector que le paso al modelo de la tabla
							Vector<Object> row=new Vector<Object>();
							//compruebo que no hayan productos iguales
							for(int x=0; x < modelo.getRowCount(); x++){
								//traigo la fila
								Vector temp=modelo.getRow(x);
								//compruebo los id de producto
								if((Integer) temp.get(0)==p.getIdProducto()){
									
									cbProductos.setSelectedIndex(0);
									
									throw new Exception("No puede haber 2 productos iguales");
									
								}
								
							}
							row.add(p.getIdProducto());
							row.add(p.getNombre());
							row.add(p.getPrecio());
							row.add(1);
							//lo agrego a la tabla
							modelo.addRow(row);
							
							//vuelvo el indice a 0
							cbProductos.setSelectedIndex(0);
							
							//actualizo los valores
							actualizarValores();
						}
						
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
						
				}
				
			});
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
			
			modelo=new ModeloProductos();
			
			Vector<String> columnas=new Vector<String>();
			
			columnas.add("ID Producto");
			columnas.add("Nombre");
			columnas.add("Precio");
			columnas.add("Cantidad");
			
			modelo.setHeader(columnas);
			
			tablaProductos = new JTable(modelo);
			tablaProductos.getColumn("ID Producto").setMaxWidth(70);
			tablaProductos.getColumn("Precio").setMaxWidth(70);
			tablaProductos.getColumn("Cantidad").setMaxWidth(70);
			tablaProductos.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(java.awt.event.KeyEvent e) {
					 int key = e.getKeyCode();
				        if (key == KeyEvent.VK_ENTER && tablaProductos.getSelectedColumn()==3) {
				        	//actualizo los valores
				        	actualizarValores();
				        	
				        }
				}
			});
			tablaProductos.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					
					if(tablaProductos.isEnabled()){
						btnBorrar.setEnabled(true);
					}
				}
			});
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
			GridBagConstraints gridBagConstraints110 = new GridBagConstraints();
			gridBagConstraints110.gridx = 1;
			gridBagConstraints110.gridy = 9;
			GridBagConstraints gridBagConstraints151 = new GridBagConstraints();
			gridBagConstraints151.gridx = 4;
			gridBagConstraints151.gridheight = 3;
			gridBagConstraints151.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints151.gridy = 12;
			GridBagConstraints gridBagConstraints141 = new GridBagConstraints();
			gridBagConstraints141.gridx = 1;
			gridBagConstraints141.gridy = 11;
			GridBagConstraints gridBagConstraints131 = new GridBagConstraints();
			gridBagConstraints131.gridx = 2;
			gridBagConstraints131.anchor = GridBagConstraints.WEST;
			gridBagConstraints131.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints131.gridy = 13;
			lblTotalConIva = new JLabel();
			lblTotalConIva.setText("0 €");
			lblTotalConIva.setForeground(Color.red);
			GridBagConstraints gridBagConstraints121 = new GridBagConstraints();
			gridBagConstraints121.gridx = 0;
			gridBagConstraints121.anchor = GridBagConstraints.WEST;
			gridBagConstraints121.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints121.gridy = 13;
			totalConIva = new JLabel();
			totalConIva.setText("Total con IVA:");
			totalConIva.setForeground(Color.red);
			GridBagConstraints gridBagConstraints112 = new GridBagConstraints();
			gridBagConstraints112.gridx = 2;
			gridBagConstraints112.anchor = GridBagConstraints.WEST;
			gridBagConstraints112.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints112.gridy = 12;
			lblTotalSinIva = new JLabel();
			lblTotalSinIva.setText("0 €");
			lblTotalSinIva.setForeground(Color.red);
			GridBagConstraints gridBagConstraints101 = new GridBagConstraints();
			gridBagConstraints101.gridx = 0;
			gridBagConstraints101.anchor = GridBagConstraints.WEST;
			gridBagConstraints101.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints101.gridy = 12;
			sinIva = new JLabel();
			sinIva.setText("Total sin IVA:");
			sinIva.setForeground(Color.red);
			GridBagConstraints gridBagConstraints91 = new GridBagConstraints();
			gridBagConstraints91.gridx = 2;
			gridBagConstraints91.anchor = GridBagConstraints.WEST;
			gridBagConstraints91.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints91.gridy = 11;
			lblImpGan = new JLabel();
			lblImpGan.setText("0 €");
			GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
			gridBagConstraints81.gridx = 0;
			gridBagConstraints81.anchor = GridBagConstraints.WEST;
			gridBagConstraints81.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints81.gridy = 11;
			lblTituloImpGan = new JLabel();
			lblTituloImpGan.setText("Impuesto a las ganancia:");
			GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
			gridBagConstraints71.gridx = 2;
			gridBagConstraints71.anchor = GridBagConstraints.WEST;
			gridBagConstraints71.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints71.gridy = 9;
			lblIva = new JLabel();
			lblIva.setText("0 €");
			GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
			gridBagConstraints61.gridx = 0;
			gridBagConstraints61.anchor = GridBagConstraints.WEST;
			gridBagConstraints61.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints61.gridy = 9;
			lblTituloIva = new JLabel();
			lblTituloIva.setText("IVA:");
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.gridx = 2;
			gridBagConstraints32.anchor = GridBagConstraints.WEST;
			gridBagConstraints32.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints32.gridy = 8;
			lblTotalSinImp = new JLabel();
			lblTotalSinImp.setText("0 €");
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.gridx = 0;
			gridBagConstraints26.anchor = GridBagConstraints.WEST;
			gridBagConstraints26.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints26.gridy = 8;
			lblSubTotal = new JLabel();
			lblSubTotal.setText("Total sin impuestos:");
			GridBagConstraints gridBagConstraints111 = new GridBagConstraints();
			gridBagConstraints111.gridx = 4;
			gridBagConstraints111.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints111.gridheight = 6;
			gridBagConstraints111.gridy = 6;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 2;
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
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 4;
			gridBagConstraints10.gridheight = 3;
			gridBagConstraints10.gridy = 3;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints9.gridy = 4;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints9.gridx = 2;
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
			gridBagConstraints7.gridx = 2;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints6.gridy = 3;
			lblGanancia = new JLabel();
			lblGanancia.setText("Ganancia:");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 2;
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
			panelGastos.setMaximumSize(new Dimension(1024, 282));
			panelGastos.setMinimumSize(new Dimension(1024, 282));
			panelGastos.setPreferredSize(new Dimension(1024, 282));
			panelGastos.add(lblNeto, gridBagConstraints);
			panelGastos.add(lblPrecioNeto, gridBagConstraints1);
			panelGastos.add(lblGanancia, gridBagConstraints6);
			panelGastos.add(getTfGanancia(), gridBagConstraints7);
			panelGastos.add(lblTransporte, gridBagConstraints8);
			panelGastos.add(getTfTransporte(), gridBagConstraints9);
			panelGastos.add(getPanelGastosExtra(), gridBagConstraints10);
			panelGastos.add(lblTotGastos, gridBagConstraints23);
			panelGastos.add(lblTotalGastos, gridBagConstraints31);
			panelGastos.add(getPanelKm(), gridBagConstraints111);
			panelGastos.add(lblSubTotal, gridBagConstraints26);
			panelGastos.add(lblTotalSinImp, gridBagConstraints32);
			panelGastos.add(lblTituloIva, gridBagConstraints61);
			panelGastos.add(lblIva, gridBagConstraints71);
			panelGastos.add(lblTituloImpGan, gridBagConstraints81);
			panelGastos.add(lblImpGan, gridBagConstraints91);
			panelGastos.add(sinIva, gridBagConstraints101);
			panelGastos.add(lblTotalSinIva, gridBagConstraints112);
			panelGastos.add(totalConIva, gridBagConstraints121);
			panelGastos.add(lblTotalConIva, gridBagConstraints131);
			panelGastos.add(getCbGanancia(), gridBagConstraints141);
			panelGastos.add(getPanelPorcentaje(), gridBagConstraints151);
			panelGastos.add(getCbCanarias(), gridBagConstraints110);
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
			tfGanancia.setText("0");
			tfGanancia.setPreferredSize(new Dimension(100, 20));
			tfGanancia.addFocusListener(new java.awt.event.FocusAdapter() {   
				public void focusLost(java.awt.event.FocusEvent e) {    
					if(tfGanancia.isEditable()){
					
						if(tfGanancia.getText().equals("")){
							tfGanancia.setText("0");
						}else{
							//actualizo los valores
							actualizarValores();
						}
					}
				}
				public void focusGained(java.awt.event.FocusEvent e) {
					
					if(tfGanancia.isEditable()){
						tfGanancia.setText("");
					}
				}
			});
			tfGanancia.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(tfGanancia.getText().equals("")){
						tfGanancia.setText("0");
					}else{
						//actualizo los valores
						actualizarValores();
					}
				}
			});
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
			tfTransporte.setText("0");
			tfTransporte.setPreferredSize(new Dimension(100, 20));
			tfTransporte.addFocusListener(new java.awt.event.FocusAdapter() {   
				public void focusLost(java.awt.event.FocusEvent e) {    
					
					if(tfTransporte.isEditable()){
						if(tfTransporte.getText().equals("")){
							tfTransporte.setText("0");
						}else{
							//actualizo los valores
							actualizarValores();
						}
					}
				}
				public void focusGained(java.awt.event.FocusEvent e) {
					if(tfTransporte.isEditable()){
						tfTransporte.setText("");
					}
				}
			});
			tfTransporte.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					if(tfTransporte.isEditable()){
						if(tfTransporte.getText().equals("")){
							tfTransporte.setText("0");
						}else{
							//actualizo los valores
							actualizarValores();
						}
					}
				}
			});
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
			panelGastosExtra.setBorder(BorderFactory.createTitledBorder(null, "Gastos", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
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
			tfHotel.setText("0");
			tfHotel.setPreferredSize(new Dimension(75, 20));
			tfHotel.addFocusListener(new java.awt.event.FocusAdapter() {   
				public void focusGained(java.awt.event.FocusEvent e) {    
					
					if(tfHotel.isEditable()){
						tfHotel.setText("");
					}
				}
				public void focusLost(java.awt.event.FocusEvent e) {
					
					if(tfHotel.isEditable()){
						if(tfHotel.getText().equals("")){
							tfHotel.setText("0");
						}else{
							//actualizo los valores
							actualizarValores();
						}
					}
				}
			});
			tfHotel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					if(tfHotel.isEditable()){
						if(tfHotel.getText().equals("")){
							tfHotel.setText("0");
						}else{
							//actualizo los valores
							actualizarValores();
						}
					}
				}
			});
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
			tfPasaje.setText("0");
			tfPasaje.setPreferredSize(new Dimension(75, 20));
			tfPasaje.addFocusListener(new java.awt.event.FocusAdapter() {   
				public void focusLost(java.awt.event.FocusEvent e) {    
					
					if(tfPasaje.isEditable()){
						if(tfPasaje.getText().equals("")){
							tfPasaje.setText("0");
						}else{
							//actualizo los valores
							actualizarValores();
						}
					}
					
				}
				public void focusGained(java.awt.event.FocusEvent e) {
					
					if(tfPasaje.isEditable()){
						tfPasaje.setText("");
					}
				}
			});
			tfPasaje.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					if(tfPasaje.isEditable()){
						if(tfPasaje.getText().equals("")){
							tfPasaje.setText("0");
						}else{
							//actualizo los valores
							actualizarValores();
						}
					}
				}
			});
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
			tfOtros.setText("0");
			tfOtros.setPreferredSize(new Dimension(75, 20));
			tfOtros.addFocusListener(new java.awt.event.FocusAdapter() {   
				public void focusLost(java.awt.event.FocusEvent e) {    
					
					if(tfOtros.isEditable()){
						if(tfOtros.getText().equals("")){
							tfOtros.setText("0");
						}else{
							//actualizo los valores
							actualizarValores();
						}
					}
				}
				public void focusGained(java.awt.event.FocusEvent e) {
					
					if(tfOtros.isEditable()){
						tfOtros.setText("");
					}
				}
			});
			tfOtros.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					if(tfOtros.isEditable()){
						if(tfOtros.getText().equals("")){
							tfOtros.setText("0");
						}else{
							//actualizo los valores
							actualizarValores();
						}
					}
				}
			});
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
			tfRestaurante.setText("0");
			tfRestaurante.setMinimumSize(new Dimension(75, 20));
			tfRestaurante.addFocusListener(new java.awt.event.FocusAdapter() {   
				public void focusLost(java.awt.event.FocusEvent e) {    
					
					if(tfRestaurante.isEditable()){
						if(tfRestaurante.getText().equals("")){
							tfRestaurante.setText("0");
						}else{
							//actualizo los valores
							actualizarValores();
						}
					}
				}
				public void focusGained(java.awt.event.FocusEvent e) {
					
					if(tfRestaurante.isEditable()){
						tfRestaurante.setText("");
					}
				}
			});
			tfRestaurante.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					if(tfRestaurante.isEditable()){
						if(tfRestaurante.getText().equals("")){
							tfRestaurante.setText("0");
						}else{
							//actualizo los valores
							actualizarValores();
						}
					}
				}
			});
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
			tfCombustible.setText("0");
			tfCombustible.setPreferredSize(new Dimension(75, 20));
			tfCombustible.addFocusListener(new java.awt.event.FocusAdapter() {   
				public void focusLost(java.awt.event.FocusEvent e) {    
					
					if(tfCombustible.isEditable()){
						if(tfCombustible.getText().equals("")){
							tfCombustible.setText("0");
						}else{
							//actualizo los valores
							actualizarValores();
						}
					}
				}
				public void focusGained(java.awt.event.FocusEvent e) {
					
					if(tfCombustible.isEditable()){
						tfCombustible.setText("");
					}
				}
			});
			tfCombustible.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					if(tfCombustible.isEditable()){
						if(tfCombustible.getText().equals("")){
							tfCombustible.setText("0");
						}else{
							//actualizo los valores
							actualizarValores();
						}
					}
				}
			});
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
			tfKm.setText("0");
			tfKm.setPreferredSize(new Dimension(75, 20));
			tfKm.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(tfKm.isEditable()){
						if(tfKm.getText().equals("")){
							tfKm.setText("0");
						}else{
							//actualizo los valores
							actualizarGasolina();
						}
					}
				}

				
			});
			tfKm.addFocusListener(new java.awt.event.FocusAdapter() {   
				public void focusLost(java.awt.event.FocusEvent e) {    
					if(tfKm.isEditable()){
						
						if(tfKm.getText().equals("")){
							tfKm.setText("0");
						}else{
							//actualizo los valores
							actualizarGasolina();
						}
						
					}
				}
				public void focusGained(java.awt.event.FocusEvent e) {
					
					if(tfKm.isEditable()){
						tfKm.setText("");
					}
				}
			});
		}
		return tfKm;
	}
	
	private void actualizarGasolina() {
		
		kilometros=Integer.parseInt(tfKm.getText());
		
		nViajes=Integer.parseInt(tfViajes.getText());
		
		precioGasolina=Double.parseDouble(tfPrecioGasolina.getText());
		
		double total= (((double) kilometros*(double)nViajes)*0.07)*precioGasolina;
		
		lblTotalViaje.setText(formateador.format(total)+" €");
		
		
	}

	/**
	 * This method initializes panelKm	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelKm() {
		if (panelKm == null) {
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.gridx = 3;
			gridBagConstraints25.anchor = GridBagConstraints.WEST;
			gridBagConstraints25.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints25.gridy = 1;
			lblTotalViaje = new JLabel();
			lblTotalViaje.setText("0 €");
			lblTotalViaje.setForeground(Color.red);
			lblTotalViaje.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.gridx = 2;
			gridBagConstraints24.anchor = GridBagConstraints.WEST;
			gridBagConstraints24.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints24.gridy = 1;
			lblTotal = new JLabel();
			lblTotal.setText("Total:");
			lblTotal.setForeground(Color.red);
			lblTotal.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints22.gridy = 1;
			gridBagConstraints22.weightx = 1.0;
			gridBagConstraints22.anchor = GridBagConstraints.WEST;
			gridBagConstraints22.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints22.gridx = 1;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.anchor = GridBagConstraints.WEST;
			gridBagConstraints21.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints21.gridy = 1;
			lblPrecioGasolina = new JLabel();
			lblPrecioGasolina.setText("Precio de la gasolina:");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints5.gridx = 3;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 2;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints4.gridy = 0;
			lblViajes = new JLabel();
			lblViajes.setText("N€ de viajes:");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints3.gridx = -1;
			gridBagConstraints3.gridy = -1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.gridy = -1;
			gridBagConstraints2.insets = new Insets(2, 10, 2, 2);
			panelKm = new JPanel();
			panelKm.setLayout(new GridBagLayout());
			panelKm.setBorder(BorderFactory.createTitledBorder(null, "Calculo de viaje", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			panelKm.setPreferredSize(new Dimension(489, 74));
			panelKm.setMinimumSize(new Dimension(489, 74));
			panelKm.add(lblKm, gridBagConstraints2);
			panelKm.add(getTfKm(), gridBagConstraints3);
			panelKm.add(lblViajes, gridBagConstraints4);
			panelKm.add(getTfViajes(), gridBagConstraints5);
			panelKm.add(lblPrecioGasolina, gridBagConstraints21);
			panelKm.add(getTfPrecioGasolina(), gridBagConstraints22);
			panelKm.add(lblTotal, gridBagConstraints24);
			panelKm.add(lblTotalViaje, gridBagConstraints25);
		}
		return panelKm;
	}

	/**
	 * This method initializes tfViajes	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfViajes() {
		if (tfViajes == null) {
			tfViajes = new JTextField();
			tfViajes.setPreferredSize(new Dimension(75, 20));
			tfViajes.setText("2");
			tfViajes.setMinimumSize(new Dimension(75, 20));
			tfViajes.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					if(tfViajes.isEditable()){
						if(tfViajes.getText().equals("")){
							tfViajes.setText("0");
						}else{
							//actualizo los valores
							actualizarGasolina();
						}
					}
				}
			});
			tfViajes.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(java.awt.event.FocusEvent e) {
					if(tfViajes.isEditable()){
						if(tfViajes.getText().equals("")){
							tfViajes.setText("0");
						}else{
							//actualizo los valores
							actualizarGasolina();
						}
					}
				}
			});
		}
		return tfViajes;
	}

	/**
	 * This method initializes tfPrecioGasolina	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfPrecioGasolina() {
		if (tfPrecioGasolina == null) {
			tfPrecioGasolina = new JTextField();
			tfPrecioGasolina.setMinimumSize(new Dimension(75, 20));
			tfPrecioGasolina.setText("1.0");
			tfPrecioGasolina.setPreferredSize(new Dimension(75, 20));
			tfPrecioGasolina.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					if(tfPrecioGasolina.isEditable()){
						if(tfPrecioGasolina.getText().equals("")){
							tfPrecioGasolina.setText("0");
						}else{
							//actualizo los valores
							actualizarGasolina();
						}
					}
				}
			});
			tfPrecioGasolina.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(java.awt.event.FocusEvent e) {
					if(tfPrecioGasolina.isEditable()){
						if(tfPrecioGasolina.getText().equals("")){
							tfPrecioGasolina.setText("0");
						}else{
							//actualizo los valores
							actualizarGasolina();
						}
					}
				}
			});
		}
		return tfPrecioGasolina;
	}

	/**
	 * This method initializes cbGanancia	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getCbGanancia() {
		if (cbGanancia == null) {
			cbGanancia = new JCheckBox();
			cbGanancia.setSelected(true);
			cbGanancia.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					actualizarValores();
				}
			});
			
			
		}
		return cbGanancia;
	}

	/**
	 * This method initializes panelPorcentaje	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelPorcentaje() {
		if (panelPorcentaje == null) {
			GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
			gridBagConstraints34.gridx = 1;
			gridBagConstraints34.anchor = GridBagConstraints.WEST;
			gridBagConstraints34.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints34.gridy = 2;
			GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
			gridBagConstraints33.gridx = 0;
			gridBagConstraints33.fill = GridBagConstraints.NONE;
			gridBagConstraints33.insets = new Insets(2, 0, 2, 0);
			gridBagConstraints33.gridy = 2;
			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.gridx = 1;
			gridBagConstraints30.anchor = GridBagConstraints.WEST;
			gridBagConstraints30.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints30.gridy = 1;
			GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
			gridBagConstraints29.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints29.gridy = 1;
			gridBagConstraints29.weightx = 1.0;
			gridBagConstraints29.anchor = GridBagConstraints.WEST;
			gridBagConstraints29.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints29.gridx = 0;
			GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
			gridBagConstraints28.anchor = GridBagConstraints.WEST;
			gridBagConstraints28.insets = new Insets(2, 10, 2, 2);
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints27.anchor = GridBagConstraints.WEST;
			gridBagConstraints27.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints27.weightx = 1.0;
			panelPorcentaje = new JPanel();
			panelPorcentaje.setLayout(new GridBagLayout());
			panelPorcentaje.setBorder(BorderFactory.createTitledBorder(null, "Porcentajes", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			panelPorcentaje.setMinimumSize(new Dimension(489, 94));
			panelPorcentaje.add(getTfPorcentaje1(), gridBagConstraints27);
			panelPorcentaje.add(getLblPorcentaje1(), gridBagConstraints28);
			panelPorcentaje.add(getTfPorcentaje2(), gridBagConstraints29);
			panelPorcentaje.add(getLblPorcentaje2(), gridBagConstraints30);
			panelPorcentaje.add(getLblPorcentaje100(), gridBagConstraints33);
			panelPorcentaje.add(getTotalPorcentaje(), gridBagConstraints34);
		}
		return panelPorcentaje;
	}

	/**
	 * This method initializes tfPorcentaje1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfPorcentaje1() {
		if (tfPorcentaje1 == null) {
			tfPorcentaje1 = new JTextField();
			tfPorcentaje1.setPreferredSize(new Dimension(75, 20));
			tfPorcentaje1.setText("50");
			tfPorcentaje1.setMinimumSize(new Dimension(75, 20));
			tfPorcentaje1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					if(tfPorcentaje1.isEditable()){
						actualizarPorcentajes();
					}
				}
			});
			tfPorcentaje1.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(java.awt.event.FocusEvent e) {
					if(tfPorcentaje1.isEditable()){
						actualizarPorcentajes();
					}
				}
			});
		}
		return tfPorcentaje1;
	}

	/**
	 * This method initializes tfPorcentaje2	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfPorcentaje2() {
		if (tfPorcentaje2 == null) {
			tfPorcentaje2 = new JTextField();
			tfPorcentaje2.setMinimumSize(new Dimension(75, 20));
			tfPorcentaje2.setText("50");
			tfPorcentaje2.setPreferredSize(new Dimension(75, 20));
			tfPorcentaje2.setEditable(false);
		}
		return tfPorcentaje2;
	}

	/**
	 * This method initializes lblPorcentaje1	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getLblPorcentaje1() {
		if (lblPorcentaje1 == null) {
			lblPorcentaje1 = new JLabel();
			lblPorcentaje1.setText("0 €");
		}
		return lblPorcentaje1;
	}

	/**
	 * This method initializes lblPorcentaje2	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getLblPorcentaje2() {
		if (lblPorcentaje2 == null) {
			lblPorcentaje2 = new JLabel();
			lblPorcentaje2.setText("0 €");
		}
		return lblPorcentaje2;
	}
	
	private DefaultComboBoxModel getModeloCB(){
		
		if (modeloCB==null){
			
			modeloCB=new DefaultComboBoxModel();
			
		}
		
		return modeloCB;
		
	}

	/**
	 * This method initializes totalPorcentaje	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getTotalPorcentaje() {
		if (totalPorcentaje == null) {
			totalPorcentaje = new JLabel();
			totalPorcentaje.setText("0 €");
		}
		return totalPorcentaje;
	}

	/**
	 * This method initializes lblPorcentaje100	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getLblPorcentaje100() {
		if (lblPorcentaje100 == null) {
			lblPorcentaje100 = new JLabel();
			lblPorcentaje100.setText("100%");
		}
		return lblPorcentaje100;
	}

	public void cargarProductos(ListadoProductos listado) {
	
		modeloCB=getModeloCB();

		modeloCB.removeAllElements();
		modeloCB.addElement("");
		
		for(int x=0; x < listado.getSize(); x++){
			modeloCB.addElement(listado.getProducto(x));	
		}
	}
	
	private void actualizarValores(){
		try{
			double totalNeto=0;
			double totalSinImp=0;
			double iva=0;
			isGanancia=cbGanancia.isSelected();
			isCanarias=cbCanarias.isSelected();
			double impuestoGanancia=0;
			
			//guardo los datos de los gastos
			hotel=Integer.parseInt(tfHotel.getText());
			pasaje=Integer.parseInt(tfPasaje.getText());
			otros=Integer.parseInt(tfOtros.getText());
			restaurante=Integer.parseInt(tfRestaurante.getText());
			combustible=Integer.parseInt(tfCombustible.getText());
			//sumo los gastos
			int totalGastos=hotel+pasaje+otros+restaurante+combustible;
			//guardo la ganancia
			ganancia=Integer.parseInt(tfGanancia.getText());
			
			if(isGanancia){
				//calculo el impuesto a la ganancia
				impuestoGanancia=ganancia*IMPUESTOgANANCIA;
				
			}else{
				impuestoGanancia=0;
			}
			
			//lo muestro en el label
			lblImpGan.setText(formateador.format(impuestoGanancia)+" €");
			//guardo el transporte
			transporte=Integer.parseInt(tfTransporte.getText());
			
			//asigno el total de gastos al label
			lblTotalGastos.setText(totalGastos+" €");
			
			
			
			for(int x=0; x<modelo.getRowCount(); x++){
				
				double precio=Double.parseDouble(modelo.getValueAt(x, 2).toString());
				int cantidad=Integer.parseInt(modelo.getValueAt(x, 3).toString());
				
				totalNeto+=precio*cantidad;
				
			}
			
			lblPrecioNeto.setText(formateador.format(totalNeto)+" €");
			
			//guardo el total sin impuestos
			totalSinImp=totalNeto+totalGastos+ganancia+transporte;
			
			//asigno el total sin impuestos
			lblTotalSinImp.setText(formateador.format(totalSinImp)+" €");

			//calculo el total sin iva
			totalSinIva=totalSinImp+impuestoGanancia;
			//lo muestro en el label
			lblTotalSinIva.setText(formateador.format(totalSinIva)+" €");
			
			//calculo el iva
			if(!isCanarias){
				iva=0;
			}else{
				iva=totalSinIva*IVA;
			}
			//lo muestro en el label
			lblIva.setText(formateador.format(iva)+" €");
			
			//calculo el total con iva
			totalIva=iva+totalSinIva;
			//lo muestro en el label
			lblTotalConIva.setText(formateador.format(totalIva)+" €");
			
			actualizarPorcentajes();
			
		}catch(NumberFormatException e){
			
		}
		
		
	}

	private void actualizarPorcentajes() {
		
		porcentaje1=Integer.parseInt(tfPorcentaje1.getText());
		int porcentaje2=100-porcentaje1;
		
		tfPorcentaje2.setText(""+porcentaje2);
		
		lblPorcentaje1.setText(formateador.format((porcentaje1/100.0)*totalIva)+" €");
		lblPorcentaje2.setText(formateador.format((porcentaje2/100.0)*totalIva)+" €");
		
		totalPorcentaje.setText(formateador.format(totalIva)+" €");
		
		
	}

	/**
	 * This method initializes scrollTexto	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollTexto() {
		if (scrollTexto == null) {
			JTextArea jTextArea = new JTextArea();
			jTextArea.setMinimumSize(new Dimension(500, 76));
			JViewport jViewport = new JViewport();
			jViewport.setView(jTextArea);
			scrollTexto = new JScrollPane();
			scrollTexto.setMinimumSize(new Dimension(1024, 110));
			scrollTexto.setPreferredSize(new Dimension(1024, 110));
			scrollTexto.setBorder(BorderFactory.createTitledBorder(null, "Texto que va en el presupuesto", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			scrollTexto.setMaximumSize(new Dimension(1024, 110));
			scrollTexto.setViewport(jViewport);
			scrollTexto.setViewportView(getTaTexto());
		}
		return scrollTexto;
	}

	/**
	 * This method initializes taTexto	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTaTexto() {
		if (taTexto == null) {
			taTexto = new JTextArea(4,4);
			taTexto.setMinimumSize(new Dimension(0, 76));
			taTexto.setLineWrap(true);
		}
		return taTexto;
	}

	public void limpiarCampos() {
		
		modelo.removeAllElements();
		
		tfGanancia.setText("0");
		tfTransporte.setText("0");
		
		tfHotel.setText("0");
		tfPasaje.setText("0");
		tfOtros.setText("0");
		tfRestaurante.setText("0");
		tfCombustible.setText("0");
		
		cbGanancia.setSelected(true);
		cbCanarias.setSelected(true);
		
		tfPorcentaje1.setText("50");
		
		tfKm.setText("0");
		tfViajes.setText("2");
		tfPrecioGasolina.setText("1.0");
		
		taTexto.setText("");
		taExplicativo.setText("");
		taFormaPago.setText("");
		
		//btnPrint.setEnabled(false);
		
		actualizarValores();
		
		actualizarGasolina();
		
	}

	public double getTotalSinIva() {
		return totalSinIva;
	}

	public double getTotalIva() {
		return totalIva;
	}

	public int getPorcentaje1() {
		return porcentaje1;
	}

	public int getGanancia() {
		return ganancia;
	}

	public int getHotel() {
		return hotel;
	}

	public int getPasaje() {
		return pasaje;
	}

	public int getOtros() {
		return otros;
	}

	public int getRestaurante() {
		return restaurante;
	}

	public int getCombustible() {
		return combustible;
	}

	public boolean isGanancia() {
		return isGanancia;
	}

	public int getTransporte() {
		return transporte;
	}

	public int getKilometros() {
		return kilometros;
	}

	public int getnViajes() {
		return nViajes;
	}

	public double getPrecioGasolina() {
		return precioGasolina;
	}
	
	public String getTexto(){
		return taTexto.getText();
	}
	public Vector<Vector> getLineas(){
		
		return modelo.getData();
		
	}

	/**
	 * This method initializes panelHerramientas	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelHerramientas() {
		if (panelHerramientas == null) {
			panelHerramientas = new JPanel();
			panelHerramientas.setLayout(new FlowLayout());
			panelHerramientas.add(getBtnPrint(), null);
			panelHerramientas.add(getBtnBorrar(), null);
		}
		return panelHerramientas;
	}

	/**
	 * This method initializes btnBorrar	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnBorrar() {
		if (btnBorrar == null) {
			btnBorrar = new JButton();
			btnBorrar.setIcon(new ImageIcon(getClass().getResource("/img/delete.png")));
			btnBorrar.setEnabled(false);
			btnBorrar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					if(tablaProductos.getSelectedRow()>=0){
						//lo quito del modelo y actualizo la tabla
						modelo.removeElementAt(tablaProductos.getSelectedRow());
						//actualizo los valores ya que quite un producto
						actualizarValores();
					}else{
						try {
							throw new Exception("Debe seleccionar una fila para borrar");
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
					}
					
				}
			});
		}
		return btnBorrar;
	}

	public void setPresupuesto(Presupuestos p) {
		
		Vector data=new Vector();
		
		setIsPresupuesto(true);
		
		ListadoLineaPresup lineas=p.getListadoLineaPresup();
		
		for(int x=0; x< lineas.getSize(); x++){
			
			Vector linea=new Vector();
			
			linea.add(lineas.get(x).getIdProducto());
			linea.add(lineas.get(x).getNombre());
			linea.add(lineas.get(x).getPrecio());
			linea.add(lineas.get(x).getCantidad());
			
			data.add(linea);
			
		}
		
		modelo.setData(data);
		
		tfGanancia.setText(""+p.getGanancia());
		tfTransporte.setText(""+p.getTransporte());
		tfHotel.setText(""+p.getHotel());
		tfPasaje.setText(""+p.getPasaje());
		tfOtros.setText(""+p.getOtros());
		tfRestaurante.setText(""+p.getRestaurante());
		tfCombustible.setText(""+p.getCombustible());
		tfKm.setText(""+p.getKilometros());
		tfViajes.setText(""+p.getnViajes());
		tfPrecioGasolina.setText(""+p.getPrecioGasolina());
		tfPorcentaje1.setText(""+p.getPorcentaje());
		taTexto.setText(p.getTextoLinea());
		taFormaPago.setText(p.getTextoFormaPago());
		taExplicativo.setText(p.getTextoExplicativo());
		id=p.getIdPresupuesto();
		idCliente=p.getIdCliente();
		
		cbGanancia.setSelected(p.isGanancia());
		
		cbCanarias.setSelected(p.isCanarias());
		
		lblFecha.setText(formateadorFecha.format(p.getFecha()));
		
		btnPrint.setEnabled(true);
		
		
		
		actualizarValores();
		actualizarGasolina();
		
		
	}

	public void editableCampos(boolean b) {
		
		
		tfGanancia.setEditable(b);
		tfTransporte.setEditable(b);
		tfHotel.setEditable(b);
		tfPasaje.setEditable(b);
		tfOtros.setEditable(b);
		tfRestaurante.setEditable(b);
		tfCombustible.setEditable(b);
		tfKm.setEditable(b);
		tfViajes.setEditable(b);
		tfPrecioGasolina.setEditable(b);
		tfPorcentaje1.setEditable(b);
		taTexto.setEditable(b);
		tablaProductos.setEnabled(false);
		taExplicativo.setEditable(b);
		taFormaPago.setEditable(b);
		
		//estos son distintos
		cbGanancia.setEnabled(b);
		cbProductos.setEnabled(b);
		cbCanarias.setEnabled(b);
		
	}

	/**
	 * @param presupuesto the presupuesto to set
	 */
	public void setIsPresupuesto(boolean isPresupuesto) {
		this.isPresupuesto = isPresupuesto;
		
		if(isPresupuesto){
			lblTitulo.setText("Presupuesto");
			scrollTexto.setBorder(BorderFactory.createTitledBorder(null, "Texto que va en el presupuesto", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
		}else{
			lblTitulo.setText("Factura");
			scrollTexto.setBorder(BorderFactory.createTitledBorder(null, "Texto que va en la factura", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));

		}
		
	}

	public void setFactura(Facturas f) {
		
		Vector data=new Vector();
		
		setIsPresupuesto(false);
		
		ListadoLineaFactura lineas=f.getListadoLineaFactura();
		
		for(int x=0; x< lineas.getSize(); x++){
			
			Vector linea=new Vector();
			
			linea.add(lineas.get(x).getIdProducto());
			linea.add(lineas.get(x).getNombre());
			linea.add(lineas.get(x).getPrecio());
			linea.add(lineas.get(x).getCantidad());
			
			data.add(linea);
			
		}
		
		modelo.setData(data);
		
		tfGanancia.setText(""+f.getGanancia());
		tfTransporte.setText(""+f.getTransporte());
		tfHotel.setText(""+f.getHotel());
		tfPasaje.setText(""+f.getPasaje());
		tfOtros.setText(""+f.getOtros());
		tfRestaurante.setText(""+f.getRestaurante());
		tfCombustible.setText(""+f.getCombustible());
		tfKm.setText(""+f.getKilometros());
		tfViajes.setText(""+f.getnViajes());
		tfPrecioGasolina.setText(""+f.getPrecioGasolina());
		tfPorcentaje1.setText(""+f.getPorcentaje());
		taTexto.setText(f.getTextoLinea());
		taFormaPago.setText(f.getTextoFormaPago());
		taExplicativo.setText(f.getTextoExplicativo());
		id=f.getIdFactura();
		
		cbGanancia.setSelected(f.isGanancia());
		
		cbCanarias.setSelected(f.isCanarias());
		
		lblFecha.setText(formateadorFecha.format(f.getFecha()));
		
		btnPrint.setEnabled(true);
		
		actualizarValores();
		actualizarGasolina();
		
	}

	/**
	 * This method initializes btnPrint	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnPrint() {
		if (btnPrint == null) {
			btnPrint = new JButton();
			btnPrint.setIcon(new ImageIcon(getClass().getResource("/img/print.png")));
			btnPrint.setEnabled(true);
			btnPrint.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					try {
						
						if(taTexto.getText().isEmpty()){
							throw new Exception("Debe escribir algo en el campo");
						}
						
						HashMap<String, Object> param = new HashMap<String, Object>();
						String tipo;
						
						File carpeta=new File(c.getIdCliente()+c.getNomSinEspacios()+c.getApelSinEspacios());
						//si la carpeta del usuario no existe
						if(!carpeta.exists()){
							//la creo
							carpeta.mkdir();
							carpeta.setExecutable(true);
							
						}
						
						if(isPresupuesto){
							
							param.put("concepto", taTexto.getText());
							param.put("precioSinIva", lblTotalSinIva.getText());
							param.put("id", id);
							param.put("nombre", c.getNombre()+" "+c.getApellidos());
							param.put("ciudad", c.getCiudad());
							param.put("provincia", c.getProvincia());
							param.put("telefono", c.getTelefono());
							param.put("IVA", lblIva.getText());
							param.put("total", lblTotalConIva.getText());
							
							//esto es para el nombre del fichero
							tipo="presupuesto";
							
							
						}else{
							
							param.put("concepto", taTexto.getText());
							param.put("precioSinIva", lblTotalSinIva.getText());
							param.put("id", id);
							param.put("nombre", c.getNombre()+" "+c.getApellidos());
							param.put("ciudad", c.getCiudad());
							param.put("provincia", c.getProvincia());
							param.put("telefono", c.getTelefono());
							param.put("IVA", lblIva.getText());
							param.put("total", lblTotalConIva.getText());
							
							//esto es para el nombre del fichero
							tipo="factura";
							
						}
						
						//esto es para mostrar el pdf antes de guardarlo
						//DialogoViewer viewer=new DialogoViewer();
						//viewer.run("src/reportes/facturaCABP.jrxml", param);
						//viewer.setVisible(true);
						
						/*if(!new File("facturaCABP.jasper").exists()){
							JasperReport report = JasperCompileManager.compileReport("facturaCABP.jrxml");
						}*/
						
						JasperPrint print= JasperFillManager.fillReport("facturaCABP.jasper", param, new JREmptyDataSource());
						
						JasperExportManager.exportReportToPdfFile(print, carpeta.toString()+"/"+tipo+id+".pdf");
						
						//agrego el pdf a la base de datos del cliente
						ListadoPdf listadoPdf=new ListadoPdf(c.getIdCliente());//creo el listado donde agregar el pdf
						File pdf=new File(carpeta.toString()+"/"+tipo+id+".pdf");//creo el objeto file para pasarselo a la clase
						
						listadoPdf.addPdf(pdf, pdf.lastModified());//lo agrego


						JOptionPane.showMessageDialog(null, "Pdf generado con exito", "Información", JOptionPane.INFORMATION_MESSAGE);
					
						
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
					
				}
			});
		}
		return btnPrint;
	}
	
	public void setCliente(Cliente c){

		this.c=c;
		
	}

	public void setLastId(int lastId) {
		
		id=lastId;
		
	}

	/**
	 * This method initializes panelCuadrados	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelCuadrados() {
		if (panelCuadrados == null) {
			panelCuadrados = new JPanel();
			panelCuadrados.setLayout(new BorderLayout());
			panelCuadrados.setMinimumSize(new Dimension(0, 114));
			panelCuadrados.setPreferredSize(new Dimension(350, 114));
			panelCuadrados.setVisible(true);
			panelCuadrados.add(getPanelTextos(), BorderLayout.CENTER);
		}
		return panelCuadrados;
	}

	/**
	 * This method initializes panelTextos	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelTextos() {
		if (panelTextos == null) {
			GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
			gridBagConstraints35.fill = GridBagConstraints.BOTH;
			gridBagConstraints35.gridy = 3;
			gridBagConstraints35.weightx = 1.0;
			gridBagConstraints35.weighty = 1.0;
			gridBagConstraints35.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints35.gridx = 0;
			GridBagConstraints gridBagConstraints39 = new GridBagConstraints();
			gridBagConstraints39.fill = GridBagConstraints.BOTH;
			gridBagConstraints39.gridy = 1;
			gridBagConstraints39.weightx = 1.0;
			gridBagConstraints39.weighty = 1.0;
			gridBagConstraints39.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints39.gridx = 0;
			GridBagConstraints gridBagConstraints38 = new GridBagConstraints();
			gridBagConstraints38.gridx = 0;
			gridBagConstraints38.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints38.anchor = GridBagConstraints.WEST;
			gridBagConstraints38.gridy = 2;
			lblExplicativo = new JLabel();
			lblExplicativo.setText("Explicación:");
			GridBagConstraints gridBagConstraints37 = new GridBagConstraints();
			gridBagConstraints37.gridx = 0;
			gridBagConstraints37.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints37.anchor = GridBagConstraints.WEST;
			gridBagConstraints37.gridy = 0;
			lblFormaPago = new JLabel();
			lblFormaPago.setText("Forma de pago:");
			panelTextos = new JPanel();
			panelTextos.setLayout(new GridBagLayout());
			panelTextos.setVisible(true);
			panelTextos.add(lblFormaPago, gridBagConstraints37);
			panelTextos.add(lblExplicativo, gridBagConstraints38);
			panelTextos.add(getScrollFormaPago(), gridBagConstraints39);
			panelTextos.add(getScrollExplicativo(), gridBagConstraints35);
		}
		return panelTextos;
	}

	/**
	 * This method initializes taFormaPago	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTaFormaPago() {
		if (taFormaPago == null) {
			taFormaPago = new JTextArea();
			taFormaPago.setMaximumSize(new Dimension(100, 100));
			taFormaPago.setLineWrap(true);
			taFormaPago.setText("");
		}
		return taFormaPago;
	}

	/**
	 * This method initializes taExplicativo	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTaExplicativo() {
		if (taExplicativo == null) {
			taExplicativo = new JTextArea();
			taExplicativo.setText("");
			taExplicativo.setLineWrap(true);
		}
		return taExplicativo;
	}

	/**
	 * This method initializes cbCanarias	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getCbCanarias() {
		if (cbCanarias == null) {
			cbCanarias = new JCheckBox();
			cbCanarias.setSelected(true);
			cbCanarias.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					actualizarValores();
				}
			});
		}
		return cbCanarias;
	}

	/**
	 * This method initializes panelFecha	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelFecha() {
		if (panelFecha == null) {
			lblSeparador = new JLabel();
			lblSeparador.setText("   ---   ");
			lblSeparador.setFont(new Font("Dialog", Font.BOLD, 18));
			panelFecha = new JPanel();
			panelFecha.setLayout(new BoxLayout(getPanelFecha(), BoxLayout.X_AXIS));
			panelFecha.add(lblSeparador, null);
			panelFecha.add(lblFecha, null);
		}
		return panelFecha;
	}

	public boolean isCanarias() {
		
		return cbCanarias.isSelected();
	}

	public String getTextoFormaPago() {
		
		return taFormaPago.getText();
	}

	public String getTextoExplicativo() {
		
		return taExplicativo.getText();
	}

	/**
	 * This method initializes scrollFormaPago	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollFormaPago() {
		if (scrollFormaPago == null) {
			scrollFormaPago = new JScrollPane();
			scrollFormaPago.setViewportView(getTaFormaPago());
		}
		return scrollFormaPago;
	}

	/**
	 * This method initializes scrollExplicativo	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollExplicativo() {
		if (scrollExplicativo == null) {
			scrollExplicativo = new JScrollPane();
			scrollExplicativo.setViewportView(getTaExplicativo());
		}
		return scrollExplicativo;
	}

	

}  //  @jve:decl-index=0:visual-constraint="10,10" 

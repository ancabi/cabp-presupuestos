package gui;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;

import clases.ListadoProductos;
import clases.Facturas;

import java.awt.Dimension;
import java.util.Vector;

public class DialogoAddFactura extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final int VALOR_ACEPTAR=1;
	private static final int VALOR_CANCELAR=0;
	private JPanel jContentPane = null;
	private JPanel panelBotones = null;
	private JPanel panelPresupuesto = null;
	private JButton btnAceptar = null;
	private JButton btnCancelar = null;
	private int idCliente;
	private int valorPulsado=0;
	private int idDistribuidor;
	

	/**
	 * @param owner
	 */
	public DialogoAddFactura(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(846, 743);
		this.setResizable(false);
		this.setTitle("Agregar Factura");
		this.setMinimumSize(new Dimension(775, 441));
		this.setContentPane(getJContentPane());
		this.setModal(true);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				
				((PanelPresupuesto) panelPresupuesto).limpiarCampos();
				
				valorPulsado=VALOR_CANCELAR;
				
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
			btnAceptar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					
					valorPulsado=VALOR_ACEPTAR;
					
					setVisible(false);
					
					int idProducto;
					int cantidad;
					String nomProducto;
					double precio;
					double totalIva= ((PanelPresupuesto) panelPresupuesto).getTotalIva();
					int porcentaje=((PanelPresupuesto) panelPresupuesto).getPorcentaje1();
					int ganancia=((PanelPresupuesto) panelPresupuesto).getGanancia();
					int hotel=((PanelPresupuesto) panelPresupuesto).getHotel();
					int pasaje=((PanelPresupuesto) panelPresupuesto).getPasaje();
					int otros=((PanelPresupuesto) panelPresupuesto).getOtros();
					int restaurante=((PanelPresupuesto) panelPresupuesto).getRestaurante();
					int combustible=((PanelPresupuesto) panelPresupuesto).getCombustible();
					boolean isGanancia=((PanelPresupuesto) panelPresupuesto).isGanancia();
					int transporte=((PanelPresupuesto) panelPresupuesto).getTransporte();
					int kilometros=((PanelPresupuesto) panelPresupuesto).getKilometros();
					int nViajes=((PanelPresupuesto) panelPresupuesto).getnViajes();
					double precioGasolina=((PanelPresupuesto) panelPresupuesto).getPrecioGasolina();
					double totalSinIva=((PanelPresupuesto) panelPresupuesto).getTotalSinIva();
					String texto=((PanelPresupuesto) panelPresupuesto).getTexto();
					
					Vector<Vector> lineas=((PanelPresupuesto) panelPresupuesto).getLineas();
					
					Facturas f= new Facturas(ganancia, restaurante, pasaje, combustible, otros, hotel, kilometros, nViajes, 
							precioGasolina, isGanancia, porcentaje, totalIva, totalSinIva, transporte, texto, idCliente, idDistribuidor);
					
					f.addBD();
					
					for(int x=0; x<lineas.size(); x++){
						
						Vector temp=lineas.get(x);

						idProducto=(Integer) temp.get(0);
						nomProducto=(String) temp.get(1);
						precio=(Double) temp.get(2);
						cantidad=Integer.parseInt(temp.get(3).toString());
						
						
						f.addLineaFactura(idProducto, nomProducto, precio, cantidad);
						
					}
					
					((PanelPresupuesto) panelPresupuesto).limpiarCampos();
					
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
			btnCancelar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					valorPulsado=VALOR_CANCELAR;
					
					setVisible(false);
					
					((PanelPresupuesto) panelPresupuesto).limpiarCampos();
					
				}
			});
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

	public void cargarProductos(ListadoProductos listado){
		
		((PanelPresupuesto) panelPresupuesto).cargarProductos(listado);
		
	}

	public int getValorPulsado() {
		return valorPulsado;
	}

	/**
	 * @param idDistribuidor the idDistribuidor to set
	 */
	public void setIdDistribuidor(int idDistribuidor) {
		this.idDistribuidor = idDistribuidor;
	}

	public void setIsPresupuesto(boolean b) {
		
		((PanelPresupuesto) panelPresupuesto).setIsPresupuesto(b);
		
	}

	

}  //  @jve:decl-index=0:visual-constraint="10,10"
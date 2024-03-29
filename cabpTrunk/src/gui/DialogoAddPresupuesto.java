/**
 * 
 */
package gui;


import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.Dimension;
import javax.swing.JButton;


import clases.Cliente;
import clases.IVA;
import clases.ListadoProductos;
import clases.Presupuestos;


import java.awt.FlowLayout;
import java.util.Date;
import java.util.Vector;

/**
 * @author ancabi
 *
 */
public class DialogoAddPresupuesto extends JDialog {

	private static final long serialVersionUID = 1L;
	public static final int VALOR_ACEPTAR=1;
	public static final int VALOR_CANCELAR=0;
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
	public DialogoAddPresupuesto(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(1172, 743);
		this.setResizable(true);
		this.setTitle("Agregar presupuesto");
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
					
					int pdf=JOptionPane.showConfirmDialog(null, "¿Desea generar un pdf con este presupuesto?", "Generar PDF", JOptionPane.YES_NO_OPTION);
					
					if(pdf==0){
						((PanelPresupuesto) panelPresupuesto).generarPdf();
					}
					
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
					boolean isCanarias=((PanelPresupuesto) panelPresupuesto).isCanarias();
					int transporte=((PanelPresupuesto) panelPresupuesto).getTransporte();
					int kilometros=((PanelPresupuesto) panelPresupuesto).getKilometros();
					int nViajes=((PanelPresupuesto) panelPresupuesto).getnViajes();
					double precioGasolina=((PanelPresupuesto) panelPresupuesto).getPrecioGasolina();
					double totalSinIva=((PanelPresupuesto) panelPresupuesto).getTotalSinIva();
					String textoLinea=((PanelPresupuesto) panelPresupuesto).getTexto();
					String textoFormaPago=((PanelPresupuesto) panelPresupuesto).getTextoFormaPago();
					String textoExplicativo=((PanelPresupuesto) panelPresupuesto).getTextoExplicativo();
				    java.util.Date date = new Date();
				    java.sql.Date fecha = new java.sql.Date(date.getTime());
				    boolean isTotalManual=((PanelPresupuesto) panelPresupuesto).getIsTotalmanual();
					int totalManual=((PanelPresupuesto) panelPresupuesto).getTotalManual();
				    double valorA=((PanelPresupuesto) panelPresupuesto).getValorA();
				    double valorB=((PanelPresupuesto) panelPresupuesto).getValorB();
				    double valorC=((PanelPresupuesto) panelPresupuesto).getValorC();
				    double valorAux=((PanelPresupuesto) panelPresupuesto).getValorAux();
				    boolean stepper=((PanelPresupuesto) panelPresupuesto).isStepper();
				    double iva=IVA.getIVA();
				    
					Vector<Vector> lineas=((PanelPresupuesto) panelPresupuesto).getLineas();
					
					Presupuestos p= new Presupuestos(ganancia, restaurante, pasaje, combustible, otros, hotel, kilometros, nViajes, 
							precioGasolina, isGanancia, isCanarias, porcentaje, totalIva, totalSinIva, iva, transporte, textoLinea, 
							textoFormaPago, textoExplicativo, fecha, isTotalManual, totalManual, idCliente, valorA, valorB, valorC, 
							valorAux, stepper, idDistribuidor);
					
					p.addBD();
					
					for(int x=0; x<lineas.size(); x++){
						
						Vector temp=lineas.get(x);

						idProducto=(Integer) temp.get(0);
						nomProducto=(String) temp.get(1);
						precio=(Double) temp.get(2);
						cantidad=Integer.parseInt(temp.get(3).toString());
						
						
						p.addLineaPresupuesto(idProducto, nomProducto, precio, cantidad);
						
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
	public void setIdCliente(Cliente cliente) {
		
		((PanelPresupuesto) panelPresupuesto).setCliente(cliente);
		
		
		this.idCliente = cliente.getIdCliente();
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

	public void setIsPresupuesto(boolean b, String nombre) {
		
		((PanelPresupuesto) panelPresupuesto).setIsPresupuesto(b, nombre);
		
		//le pongo la fecha al label
		((PanelPresupuesto) panelPresupuesto).setLblFecha();
		
	}

	public void setLastId(int lastPresup) {
		
		((PanelPresupuesto) panelPresupuesto).setLastId(lastPresup);
		
	}
	
	
	

}  //  @jve:decl-index=0:visual-constraint="12,-18"

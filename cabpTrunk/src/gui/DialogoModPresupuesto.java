package gui;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.util.Date;
import java.util.Vector;

import javax.swing.JDialog;


import clases.Cliente;
import clases.Facturas;
import clases.ListadoProductos;
import clases.Presupuestos;

public class DialogoModPresupuesto extends JDialog {

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
	private Presupuestos presupuestoActual;  //  @jve:decl-index=0:
	private ListadoProductos listadoProductos;  //  @jve:decl-index=0:
	private Facturas facturaActual;  //  @jve:decl-index=0:
	private boolean isFactura=false;
	private Cliente c;

	/**
	 * @param panelClientePres
	 */
	public DialogoModPresupuesto(Frame panelClientePres) {
		super(panelClientePres);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(1169, 743);
		this.setResizable(false);
		this.setTitle("Modificar presupuesto");
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
					double totalConIva= ((PanelPresupuesto) panelPresupuesto).getTotalIva();
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
					boolean isTotalManual=((PanelPresupuesto) panelPresupuesto).getIsTotalmanual();
					int totalManual=((PanelPresupuesto) panelPresupuesto).getTotalManual();
					double valorA=((PanelPresupuesto) panelPresupuesto).getValorA();
					double valorB=((PanelPresupuesto) panelPresupuesto).getValorB();
					double valorC=((PanelPresupuesto) panelPresupuesto).getValorC();
					double valorAux=((PanelPresupuesto) panelPresupuesto).getValorAux();
					
					
					Vector<Vector> lineas=((PanelPresupuesto) panelPresupuesto).getLineas();
					
					if(isFactura){
						facturaActual.setGanancia(ganancia);
						facturaActual.setTransporte(transporte);
						facturaActual.setHotel(hotel);
						facturaActual.setPasaje(pasaje);
						facturaActual.setOtros(otros);
						facturaActual.setRestaurante(restaurante);
						facturaActual.setCombustible(combustible);
						facturaActual.setGanancia(isGanancia);
						facturaActual.setCanarias(isCanarias);
						facturaActual.setKilometros(kilometros);
						facturaActual.setnViajes(nViajes);
						facturaActual.setPrecioGasolina(precioGasolina);
						facturaActual.setTotalConIva(totalConIva);
						facturaActual.setTotalSinIva(totalSinIva);
						facturaActual.setTextoLinea(textoLinea);
						facturaActual.setTextoFormaPago(textoFormaPago);
						facturaActual.setTextoExplicativo(textoExplicativo);
						facturaActual.setPorcentaje(porcentaje);
						java.util.Date date = new Date();
					    java.sql.Date fecha = new java.sql.Date(date.getTime());
					    facturaActual.setFecha(fecha);
					    facturaActual.setTotalManual(isTotalManual);
					    facturaActual.setTotalManual(totalManual);
					    facturaActual.setValorA(valorA);
					    facturaActual.setValorB(valorB);
					    facturaActual.setValorC(valorC);
					    facturaActual.setValorAux(valorAux);
						
						facturaActual.removeAllLineas();
						
						for(int x=0; x<lineas.size(); x++){
							
							Vector temp=lineas.get(x);
	
							idProducto=(Integer) temp.get(0);
							nomProducto=(String) temp.get(1);
							precio=(Double) temp.get(2);
							cantidad=Integer.parseInt(temp.get(3).toString());
							
							
							facturaActual.addLineaPresupuesto(idProducto, nomProducto, precio, cantidad);
						}
					}else{
					
						presupuestoActual.setGanancia(ganancia);
						presupuestoActual.setTransporte(transporte);
						presupuestoActual.setHotel(hotel);
						presupuestoActual.setPasaje(pasaje);
						presupuestoActual.setOtros(otros);
						presupuestoActual.setRestaurante(restaurante);
						presupuestoActual.setCombustible(combustible);
						presupuestoActual.setGanancia(isGanancia);
						presupuestoActual.setCanarias(isCanarias);
						presupuestoActual.setKilometros(kilometros);
						presupuestoActual.setnViajes(nViajes);
						presupuestoActual.setPrecioGasolina(precioGasolina);
						presupuestoActual.setTotalConIva(totalConIva);
						presupuestoActual.setTotalSinIva(totalSinIva);
						presupuestoActual.setTexto(textoLinea);
						presupuestoActual.setTextoFormaPago(textoFormaPago);
						presupuestoActual.setTextoExplicativo(textoExplicativo);
						presupuestoActual.setPorcentaje(porcentaje);
						java.util.Date date = new Date();
					    java.sql.Date fecha = new java.sql.Date(date.getTime());
					    presupuestoActual.setFecha(fecha);
					    presupuestoActual.setTotalManual(isTotalManual);
					    presupuestoActual.setTotalManual(totalManual);
					    presupuestoActual.setValorA(valorA);
					    presupuestoActual.setValorB(valorB);
					    presupuestoActual.setValorC(valorC);
					    presupuestoActual.setValorAux(valorAux);
						
						presupuestoActual.removeAllLineas();
						
						for(int x=0; x<lineas.size(); x++){
							
							Vector temp=lineas.get(x);
	
							idProducto=(Integer) temp.get(0);
							nomProducto=(String) temp.get(1);
							precio=(Double) temp.get(2);
							try{
								cantidad=Integer.parseInt(temp.get(3).toString());
							}catch(NumberFormatException e1){
								cantidad=0;
							}
							
							
							presupuestoActual.addLineaPresupuesto(idProducto, nomProducto, precio, cantidad);
							
						}
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
	public void setIdCliente(Cliente c) {
		this.idCliente = c.getIdCliente();
		
		this.c=c;
		
		((PanelPresupuesto) panelPresupuesto).setCliente(c);
	}

	public void cargarProductos(){
		
		//traigo el listado de productos del proveedor elegido
		if(isFactura){
			listadoProductos=getListadoProductos(facturaActual.getIdDistribuidor());
		}else{
			listadoProductos=getListadoProductos(presupuestoActual.getIdDistribuidor());
		}
		//cargo los productos en memoria
		listadoProductos.cargarProductos();
		
		((PanelPresupuesto) panelPresupuesto).cargarProductos(listadoProductos);
		
	}

	public int getValorPulsado() {
		return valorPulsado;
	}
	
	public void setPresupuesto(Presupuestos p){
		((PanelPresupuesto) panelPresupuesto).setPresupuesto(p);
		
		presupuestoActual=p;
		
		isFactura=false;
	}
	
	public void setFactura(Facturas f){
		((PanelPresupuesto) panelPresupuesto).setFactura(f);
		
		facturaActual=f;
		
		isFactura=true;
	}
	
	private ListadoProductos getListadoProductos(int idDistribuidor){
		
		if(listadoProductos==null){
			
			listadoProductos=new ListadoProductos();
			
		}
		
		listadoProductos.setIdDistribuidor(idDistribuidor);
		
		return listadoProductos;
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

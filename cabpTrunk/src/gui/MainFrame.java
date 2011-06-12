package gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.CardLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ImageIcon;

import clases.Cliente;
import clases.Emails;
import clases.ListadoClientes;
import clases.ListadoDistribuidores;
import clases.ListadoProductos;
import clases.Telefonos;
import conexion.Conectar;
import javax.swing.JTabbedPane;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

import org.w3c.dom.ls.LSInput;

import java.awt.Font;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JMenuBar jJMenuBar = null;
	private JMenu jMenu = null;
	private JMenuItem jMenuItem = null;
	private JToolBar jJToolBarBar = null;
	private JButton jButton = null;
	private JPanel panelCard = null;
	private JPanel panelVacio = null;
	private JPanel panelCliente = null;
	private Connection dbConnect;  //  @jve:decl-index=0:
	private JButton btnPresupuesto = null;
	private ListadoClientes listadoClientes= null;  //  @jve:decl-index=0:
	private DialogoSeleccionCliente dialogoSeleccionCliente;
	private ListadoProductos listadoProductos = null;  //  @jve:decl-index=0:
	private DialogoAddPresupuesto dialogoAddPresupuesto;
	private DialogoAddFactura dialogoAddFactura;
	private ListadoDistribuidores listadoDistribuidores;  //  @jve:decl-index=0:
	private DialogoSeleccionDistribuidor dialogoSeleccionDistribuidor;
	private JButton btnFactura = null;
	private JButton btnProductos = null;
	private JButton btnDistribuidor = null;
	private JPanel panelProductos = null;
	private JPanel panelDistribuidor = null;

	/**
	 * This is the default constructor
	 */
	public MainFrame() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		
		this.setSize(925, 629);
		this.setForeground(Color.black);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/cabpTrans.png")));
		this.setJMenuBar(getJJMenuBar());
			
		try {
			//conecto con la BD
			Conectar.makeConnection();
		} catch (ClassNotFoundException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}

		dbConnect=Conectar.getConnection();
			
		if(dbConnect!=null){
			
			listadoClientes=new ListadoClientes();
				
			listadoClientes.cargarClientes();
			
			listadoDistribuidores=new ListadoDistribuidores();
			
			listadoDistribuidores.cargarDistribuidores();
			
		}

		this.setContentPane(getJContentPane());
		this.setTitle("CABP");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				
				try {
					//cierro la conexion
					Conectar.closeConnection();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				
			}
		});
		
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
			jContentPane.add(getJJToolBarBar(), BorderLayout.NORTH);
			jContentPane.add(getPanelCard(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getJMenu());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenu() {
		if (jMenu == null) {
			jMenu = new JMenu();
			jMenu.setText("Archivo");
			jMenu.add(getJMenuItem());
		}
		return jMenu;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem() {
		if (jMenuItem == null) {
			jMenuItem = new JMenuItem();
			jMenuItem.setText("Salir");
		}
		return jMenuItem;
	}

	/**
	 * This method initializes jJToolBarBar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getJJToolBarBar() {
		if (jJToolBarBar == null) {
			jJToolBarBar = new JToolBar();
			jJToolBarBar.add(getJButton());
			jJToolBarBar.add(getBtnPresupuesto());
			jJToolBarBar.add(getBtnFactura());
			jJToolBarBar.add(getBtnProductos());
			jJToolBarBar.add(getBtnDistribuidor());
		}
		return jJToolBarBar;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setIcon(new ImageIcon(getClass().getResource("/img/user2.png")));
			jButton.setMargin(new Insets(20, 34, 2, 34));
			jButton.setBorderPainted(false);
			jButton.setFocusPainted(true);
			jButton.setRolloverEnabled(true);
			jButton.setToolTipText("Gestion de usuario");
			jButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					((PanelCliente) panelCliente).actualizarTablaClientes();
										
					//mostramos el panel con los datos de los prestamos
					cambiarCapa("panelCliente");
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes panelCard	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelCard() {
		if (panelCard == null) {
			panelCard = new JPanel();
			panelCard.setLayout(new CardLayout());
			panelCard.add(getPanelVacio(), getPanelVacio().getName());
			panelCard.add(getPanelCliente(), getPanelCliente().getName());
			panelCard.add(getPanelProductos(), getPanelProductos().getName());
			panelCard.add(getPanelDistribuidor(), getPanelDistribuidor().getName());
		}
		return panelCard;
	}

	/**
	 * This method initializes panelVacio	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelVacio() {
		if (panelVacio == null) {
			panelVacio = new JPanel();
			panelVacio.setLayout(new GridBagLayout());
			panelVacio.setName("panelVacio");
		}
		return panelVacio;
	}

	/**
	 * This method initializes panelCliente	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	JPanel getPanelCliente() {
		if (panelCliente == null) {
			panelCliente = new PanelCliente(this);
			panelCliente.setName("panelCliente");
		}
		return panelCliente;
	}
	
	public void cambiarCapa(String panel){
		//obetener el layout
		CardLayout card=(CardLayout) panelCard.getLayout();
		//si es el panel cliente, tiene que recargar la tabla
		if(panel.equals("panelCliente")){
			
			((PanelCliente) panelCliente).actualizarTablaClientes();
			
		}
		
		//metodo next
		card.show(getPanelCard(), panel);
	}

	/**
	 * This method initializes btnPresupuesto	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnPresupuesto() {
		if (btnPresupuesto == null) {
			btnPresupuesto = new JButton();
			btnPresupuesto.setIcon(new ImageIcon(getClass().getResource("/img/presupuesto2.png")));
			btnPresupuesto.setMargin(new Insets(20, 34, 2, 34));
			btnPresupuesto.setToolTipText("Añadir presupuesto");
			btnPresupuesto.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
			btnPresupuesto.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					nuevoPresupuesto();
				}
			});
		}
		return btnPresupuesto;
	}
	
	private ListadoProductos getListatoProductos(int idDistribuidor){
		
		if(listadoProductos==null){
			
			listadoProductos=new ListadoProductos();
			
		}
		
		listadoProductos.setIdDistribuidor(idDistribuidor);
		
		return listadoProductos;
		
	}

	public ListadoClientes getListadoClientes() {

		return listadoClientes;
	}
	
	private DialogoSeleccionCliente getDialogoSeleccionCliente(){
		
		if(dialogoSeleccionCliente==null){
			
			dialogoSeleccionCliente=new DialogoSeleccionCliente(this);
			
		}
		
		return dialogoSeleccionCliente;
		
	}
	
	private DialogoSeleccionDistribuidor getDialogoSeleccionDistribuidor(){
		
		if(dialogoSeleccionDistribuidor==null){
			
			dialogoSeleccionDistribuidor=new DialogoSeleccionDistribuidor(this);
			
		}
		
		return dialogoSeleccionDistribuidor;
		
	}
	
	private DialogoAddPresupuesto getDialogoAddPresupuesto(){
		
		if(dialogoAddPresupuesto==null){
			
			dialogoAddPresupuesto=new DialogoAddPresupuesto(this);
			
		}
		
		return dialogoAddPresupuesto;
	}

	public ListadoDistribuidores getListadoDistribuidores() {

		return listadoDistribuidores;
	}
	
	public void nuevoPresupuesto(){
		
		cambiarCapa("panelVacio");
		
		dialogoSeleccionCliente=getDialogoSeleccionCliente();
		
		dialogoSeleccionCliente.actualizarCliente();
		
		dialogoSeleccionCliente.setVisible(true);
		
		if(dialogoSeleccionCliente.getValorPulsado()== DialogoSeleccionCliente.VALOR_ACEPTAR){
			
			int idCliente=dialogoSeleccionCliente.getIdCliente();
		
			nuevoPresupuesto(idCliente);
			
			
		}
		
	}
	
	public void nuevoPresupuesto(int idCliente){

		dialogoSeleccionDistribuidor=getDialogoSeleccionDistribuidor();
			
		dialogoSeleccionDistribuidor.cargarDistribuidores();
			
		dialogoSeleccionDistribuidor.setVisible(true);
			
		if(dialogoSeleccionDistribuidor.getValorPulsado()==DialogoSeleccionDistribuidor.VALOR_ACEPTAR){

			int idDistribuidor=dialogoSeleccionDistribuidor.getIdDistribuidor();
				
			//traigo el listado de productos del proveedor elegido
			listadoProductos=getListatoProductos(idDistribuidor);
			//cargo los productos en memoria
			listadoProductos.cargarProductos();
				
			//llamo a la ventana para crear presupuesto
			dialogoAddPresupuesto=getDialogoAddPresupuesto();
			
			dialogoAddPresupuesto.setIdDistribuidor(idDistribuidor);

			//le asigno el cliente para el cual va a ser el presupuesto
			dialogoAddPresupuesto.setIdCliente(idCliente);
				
			dialogoAddPresupuesto.cargarProductos(listadoProductos);
			
				
			dialogoAddPresupuesto.setVisible(true);
		}
			
	}

	public void nuevaFactura(){
		
		cambiarCapa("panelVacio");
		
		dialogoSeleccionCliente=getDialogoSeleccionCliente();
		
		dialogoSeleccionCliente.actualizarCliente();
		
		dialogoSeleccionCliente.setVisible(true);
		
		if(dialogoSeleccionCliente.getValorPulsado()== DialogoSeleccionCliente.VALOR_ACEPTAR){
			
			int idCliente=dialogoSeleccionCliente.getIdCliente();
		
			nuevaFactura(idCliente);
			
			
		}
		
	}
	
	public void nuevaFactura(int idCliente){

		dialogoSeleccionDistribuidor=getDialogoSeleccionDistribuidor();
			
		dialogoSeleccionDistribuidor.cargarDistribuidores();
			
		dialogoSeleccionDistribuidor.setVisible(true);
			
		if(dialogoSeleccionDistribuidor.getValorPulsado()==DialogoSeleccionDistribuidor.VALOR_ACEPTAR){

			int idDistribuidor=dialogoSeleccionDistribuidor.getIdDistribuidor();
				
			//traigo el listado de productos del proveedor elegido
			listadoProductos=getListatoProductos(idDistribuidor);
			//cargo los productos en memoria
			listadoProductos.cargarProductos();
				
			//llamo a la ventana para crear presupuesto
			dialogoAddFactura=getDialogoAddFactura();
			
			dialogoAddFactura.setIdDistribuidor(idDistribuidor);

			//le asigno el cliente para el cual va a ser el presupuesto
			dialogoAddFactura.setIdCliente(idCliente);
				
			dialogoAddFactura.cargarProductos(listadoProductos);
			
				
			dialogoAddFactura.setVisible(true);
		}
			
	}
	private DialogoAddFactura getDialogoAddFactura() {
		
		if(dialogoAddFactura==null){
			
			dialogoAddFactura=new DialogoAddFactura(this);
			
		}
		
		return dialogoAddFactura;
	}

	/**
	 * This method initializes btnFactura	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnFactura() {
		if (btnFactura == null) {
			btnFactura = new JButton();
			btnFactura.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
			btnFactura.setToolTipText("Añadir factura");
			btnFactura.setIcon(new ImageIcon(getClass().getResource("/img/factura.png")));
			btnFactura.addActionListener(new java.awt.event.ActionListener() {   
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					nuevaFactura();
				}
			
			});
		}
		return btnFactura;
	}

	/**
	 * This method initializes btnProductos	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnProductos() {
		if (btnProductos == null) {
			btnProductos = new JButton();
			btnProductos.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
			btnProductos.setIcon(new ImageIcon(getClass().getResource("/img/producto.png")));
			btnProductos.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
				
					dialogoSeleccionDistribuidor=getDialogoSeleccionDistribuidor();
					
					dialogoSeleccionDistribuidor.cargarDistribuidores();
						
					dialogoSeleccionDistribuidor.setVisible(true);
						
					if(dialogoSeleccionDistribuidor.getValorPulsado()==DialogoSeleccionDistribuidor.VALOR_ACEPTAR){

						int idDistribuidor=dialogoSeleccionDistribuidor.getIdDistribuidor();
							
						//traigo el listado de productos del proveedor elegido
						listadoProductos=getListatoProductos(idDistribuidor);
						//cargo los productos en memoria
						listadoProductos.cargarProductos();
						
						((PanelProductos) panelProductos).cargarProductos();
						
						cambiarCapa("panelProductos");
						
					}
				}
			});
		}
		return btnProductos;
	}

	/**
	 * This method initializes btnDistribuidor	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnDistribuidor() {
		if (btnDistribuidor == null) {
			btnDistribuidor = new JButton();
			btnDistribuidor.setIcon(new ImageIcon(getClass().getResource("/img/distribuidor.png")));
			btnDistribuidor.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
			btnDistribuidor.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					((PanelDistribuidor) panelDistribuidor).cargarDistribuidores();
					
					cambiarCapa("panelDistribuidor");
				}
			});
		}
		return btnDistribuidor;
	}

	/**
	 * This method initializes panelProductos	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelProductos() {
		if (panelProductos == null) {
			panelProductos = new PanelProductos(this);
			panelProductos.setName("panelProductos");
		}
		return panelProductos;
	}

	public ListadoProductos getListadoProductos() {
		
		return listadoProductos;
	}

	/**
	 * This method initializes panelDistribuidor	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelDistribuidor() {
		if (panelDistribuidor == null) {
			panelDistribuidor = new PanelDistribuidor(this);
			panelDistribuidor.setName("panelDistribuidor");
		}
		return panelDistribuidor;
	}

	public ListadoDistribuidores getDistribuidores() {
		
		return listadoDistribuidores;
	}
	
	

	


}  //  @jve:decl-index=0:visual-constraint="10,10"

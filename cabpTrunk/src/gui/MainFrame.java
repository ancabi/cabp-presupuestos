package gui;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.sql.Connection;

import java.sql.SQLException;


import javax.swing.ImageIcon;


import clases.Cliente;
import clases.IVA;
import clases.ListadoClientes;
import clases.ListadoDistribuidores;
import clases.ListadoProductos;

import conexion.Conectar;

import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.filechooser.FileNameExtensionFilter;

import filtos.FiltroImagenes;



/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
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
	private JPanel panelDatos;
	private JLabel lblIva;
	private JLabel lblBd;
	private JMenuItem menuItemBd;
	private JPanel panelVerFacturas;
	private JButton btnPresupuesto = null;
	private JButton btnVerFacturas;
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
	private JMenu menuConfiguracion = null;
	private JMenuItem itemIva = null;
	private String bd;
	private JFrame mainFrame=this;

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
		
		this.setSize(1260, 768);
		this.setForeground(Color.black);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/cabpTrans.png")));
		this.setJMenuBar(getJJMenuBar());
			
		try {
			
			File archivo = new File("bd.cabp");
			FileReader fr = new FileReader(archivo);
			BufferedReader br = new BufferedReader(fr);
			bd=br.readLine();
			
			//conecto con la BD
			Conectar.makeConnection(bd);
			
			
		} catch (ClassNotFoundException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}

		dbConnect=Conectar.getConnection();
			
		if(dbConnect!=null){
			
			listadoClientes=new ListadoClientes();
				
			listadoClientes.cargarClientes();
			
			listadoDistribuidores=new ListadoDistribuidores();
			
			listadoDistribuidores.cargarDistribuidores();
			
		}else{
			desactivarBotones();
		}

		this.setContentPane(getJContentPane());
		this.setTitle("CABP");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				
				try {
					if(dbConnect!=null){
						//cierro la conexion
						Conectar.closeConnection();
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				
			}
		});
		
	}

	private void desactivarBotones() {
		
		btnDistribuidor.setEnabled(false);
		btnFactura.setEnabled(false);
		btnPresupuesto.setEnabled(false);
		btnProductos.setEnabled(false);
		jButton.setEnabled(false);
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
			jJMenuBar.add(getMenuConfiguracion());
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
			jMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.exit(0);
				}
			});
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
			jJToolBarBar.add(getBtnVerFacturas());
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
			panelCard.add(getPanelVerFacturas(), getPanelVerFacturas().getName());
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
			BorderLayout panelVacioLayout = new BorderLayout();
			panelVacio.setLayout(panelVacioLayout);
			panelVacio.setName("panelVacio");
			panelVacio.add(getPanelDatos(), BorderLayout.SOUTH);

			lblBd.setText(lblBd.getText() + bd);

			
		}
		return panelVacio;
	}

	/**
	 * This method initializes panelCliente	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public JPanel getPanelCliente() {
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

			nuevoPresupuesto(listadoClientes.getClientePorId(idCliente));
			
			
		}
		
	}
	
	public void nuevoPresupuesto(Cliente c){

		dialogoSeleccionDistribuidor=getDialogoSeleccionDistribuidor();
			
		dialogoSeleccionDistribuidor.cargarDistribuidores();
			
		dialogoSeleccionDistribuidor.setVisible(true);
			
		if(dialogoSeleccionDistribuidor.getValorPulsado()==DialogoSeleccionDistribuidor.VALOR_ACEPTAR){

			int idDistribuidor=dialogoSeleccionDistribuidor.getIdDistribuidor();
			String nombre=dialogoSeleccionDistribuidor.getNombre();

			//traigo el listado de productos del proveedor elegido
			listadoProductos=getListatoProductos(idDistribuidor);
			//cargo los productos en memoria
			listadoProductos.cargarProductos();
				
			//llamo a la ventana para crear presupuesto
			dialogoAddPresupuesto=getDialogoAddPresupuesto();
			
			dialogoAddPresupuesto.setIdDistribuidor(idDistribuidor);

			//le asigno el cliente para el cual va a ser el presupuesto
			dialogoAddPresupuesto.setIdCliente(c);
			
			dialogoAddPresupuesto.setLastId(listadoClientes.getLastPresup());
				
			dialogoAddPresupuesto.cargarProductos(listadoProductos);
			
			dialogoAddPresupuesto.setIsPresupuesto(true, nombre);
			
				
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

			nuevaFactura(listadoClientes.getClientePorId(idCliente));
			
			
		}
		
	}
	
	public void nuevaFactura(Cliente c){

		dialogoSeleccionDistribuidor=getDialogoSeleccionDistribuidor();
			
		dialogoSeleccionDistribuidor.cargarDistribuidores();
			
		dialogoSeleccionDistribuidor.setVisible(true);
			
		if(dialogoSeleccionDistribuidor.getValorPulsado()==DialogoSeleccionDistribuidor.VALOR_ACEPTAR){

			int idDistribuidor=dialogoSeleccionDistribuidor.getIdDistribuidor();
			String nombre=dialogoSeleccionDistribuidor.getNombre();
				
			//traigo el listado de productos del proveedor elegido
			listadoProductos=getListatoProductos(idDistribuidor);
			//cargo los productos en memoria
			listadoProductos.cargarProductos();
				
			//llamo a la ventana para crear presupuesto
			dialogoAddFactura=getDialogoAddFactura();
			
			dialogoAddFactura.setIdDistribuidor(idDistribuidor);

			//le asigno el cliente para el cual va a ser el presupuesto
			dialogoAddFactura.setIdCliente(c);
			
			dialogoAddFactura.setLastId(listadoClientes.getLastFactura());
				
			dialogoAddFactura.cargarProductos(listadoProductos);
			
			dialogoAddFactura.setIsPresupuesto(false, nombre);
				
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
			btnProductos.setToolTipText("Gestion de productos");
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

						((PanelProductos) panelProductos).cargarProductos(idDistribuidor);
						
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
			btnDistribuidor.setToolTipText("Gestion de distribuidores");
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

	/**
	 * This method initializes menuConfiguracion	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getMenuConfiguracion() {
		if (menuConfiguracion == null) {
			menuConfiguracion = new JMenu();
			menuConfiguracion.setText("Opciones");
			menuConfiguracion.add(getItemIva());
			menuConfiguracion.add(getMenuItemBd());
		}
		return menuConfiguracion;
	}

	/**
	 * This method initializes itemIva	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getItemIva() {
		if (itemIva == null) {
			itemIva = new JMenuItem();
			itemIva.setText("Cambiar IVA");
			itemIva.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					
					try {
						File archivo = new File("iva.cabp");
						FileReader fr = new FileReader(archivo);
						BufferedReader br = new BufferedReader(fr);
						
						String linea = br.readLine();
						
						String iva=(String) JOptionPane.showInputDialog(null, "Introduzca el IVA", "Cambiar IVA", JOptionPane.QUESTION_MESSAGE, null, null, linea);
						
						if(iva!=null){
						
							FileWriter fichero = new FileWriter("iva.cabp");
				            PrintWriter pw = new PrintWriter(fichero);
				            
				            pw.println(iva);
				            
				            
				            if(fichero!=null){
				            	fichero.close();
				            }
				            
				            IVA.readIVA();
						}
						
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
					
					
				}
			});
		}
		return itemIva;
	}
	
	private JButton getBtnVerFacturas() {
		if(btnVerFacturas == null) {
			btnVerFacturas = new JButton();
			btnVerFacturas.setIcon(new ImageIcon(getClass().getClassLoader().getResource("img/verFacturas.png")));
			btnVerFacturas.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
			btnVerFacturas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					
					panelVerFacturas=getPanelVerFacturas();
					((PanelVerFacturas) panelVerFacturas).cargarListados();
					
					cambiarCapa("panelVerFacturas");
				}
			});

		}
		return btnVerFacturas;
	}
	
	private JPanel getPanelVerFacturas() {
		if(panelVerFacturas == null) {
			panelVerFacturas = new PanelVerFacturas(this);
			panelVerFacturas.setName("panelVerFacturas");
		}
		return panelVerFacturas;
	}
	
	private JMenuItem getMenuItemBd() {
		if(menuItemBd == null) {
			menuItemBd = new JMenuItem();
			menuItemBd.setText("Seleccionar base de datos");
			menuItemBd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					
					File bd=getFile();
					
					if(bd!=null){
					
						FileWriter fichero=null;
						try {
							
							fichero = new FileWriter("bd.cabp");
							
							PrintWriter pw = new PrintWriter(fichero);
				            
				            pw.println(bd.getName());
				            
				            
				            if(fichero!=null){
				            	fichero.close();
				            }
							
							JOptionPane.showMessageDialog(null, "Reinicie el programa para actualizar los cambios");
							
						} catch (IOException e) {
							JOptionPane.showMessageDialog(null, e.getMessage());
						} 
					}
						
					}
			});
		}
		return menuItemBd;
	}
	
	
	
	private File getFile(){
		
		String curDir = System.getProperty("user.dir");
		
		JFileChooser fc=new JFileChooser(curDir);
		
		FileNameExtensionFilter ff=new FileNameExtensionFilter("*.sqlite", "sqlite");
		
		fc.setFileFilter(ff);
		
		fc.setMultiSelectionEnabled(false);
		
		int result=fc.showSaveDialog(null);

		if(result==JFileChooser.APPROVE_OPTION){
			
			return fc.getSelectedFile();
			
		}
		
		return null;
	}
	
	private JLabel getLblBd() {
		if(lblBd == null) {
			lblBd = new JLabel();
			lblBd.setText("Version de base de datos: ");
		}
		return lblBd;
	}
	
	private JLabel getLblIva() {
		if(lblIva == null) {
			lblIva = new JLabel();
			try {
				IVA.readIVA();
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
			lblIva.setText("IVA: "+IVA.getIVA()*100+"%");
			
			
		}
		return lblIva;
	}
	
	private JPanel getPanelDatos() {
		if(panelDatos == null) {
			panelDatos = new JPanel();
			BoxLayout panelDatosLayout = new BoxLayout(panelDatos, javax.swing.BoxLayout.Y_AXIS);
			panelDatos.setLayout(panelDatosLayout);
			panelDatos.add(getLblBd());
			panelDatos.add(getLblIva());
		}
		return panelDatos;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

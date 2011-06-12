package gui;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.ImageIcon;

import clases.ListadoProductos;
import clases.Productos;

import modelo.ModeloProductos;

public class PanelProductos extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane scrollTabla = null;
	private JTable tablaProductos = null;
	private JPanel panelSup = null;
	private JLabel lblTitulo = null;
	private JPanel panelBotones = null;
	private JButton btnAddProducto = null;
	private JButton btnDelProducto = null;
	private ModeloProductos modelo=null;
	private MainFrame mainFrame=null;
	private DialogoAddProducto dialogoAddProducto;
	private int idDistribuidor=0;
	private DialogoModProducto dialogoModProducto;

	/**
	 * This is the default constructor
	 * @param mainFrame 
	 */
	public PanelProductos(MainFrame mainFrame) {
		super();
		initialize();
		
		this.mainFrame=mainFrame;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(734, 492);
		this.setLayout(new BorderLayout());
		this.add(getScrollTabla(), BorderLayout.CENTER);
		this.add(getPanelSup(), BorderLayout.NORTH);
	}

	/**
	 * This method initializes scrollTabla	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollTabla() {
		if (scrollTabla == null) {
			scrollTabla = new JScrollPane();
			scrollTabla.setViewportView(getTablaProductos());
		}
		return scrollTabla;
	}

	/**
	 * This method initializes tablaProductos	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTablaProductos() {
		if (tablaProductos == null) {
			
			modelo=new ModeloProductos();
			
			Vector<String> header=new Vector<String>();
			
			header.add("ID Producto");
			header.add("Nombre");
			header.add("Precio");
			
			modelo.setHeader(header);
			
			tablaProductos = new JTable(modelo);
			//achico las columnas que tienen menos ancho y asi el nombre se muestra entero
			tablaProductos.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (e.getClickCount() >= 2){
						
						int index=tablaProductos.getSelectedRow();
						//traigo el producto
						Productos p=mainFrame.getListadoProductos().getProducto(index);
						
						dialogoModProducto=getDialogoModProducto();
						
						dialogoModProducto.cargarProducto(p);
						
						dialogoModProducto.setVisible(true);
						
						if(dialogoModProducto.getValorPulsado()==DialogoModProducto.VALOR_ACEPTAR){
							
							String nombre=dialogoModProducto.getNombre();
							double precio=dialogoModProducto.getPrecio();
							
							mainFrame.getListadoProductos().actualizarProducto(p, nombre, precio);
							
							cargarProductos();
							
						}
						
					}
				}
			});
			tablaProductos.getColumn("ID Producto").setMaxWidth(110);
			tablaProductos.getColumn("Precio").setMaxWidth(110);
		}
		return tablaProductos;
	}

	/**
	 * This method initializes panelSup	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelSup() {
		if (panelSup == null) {
			lblTitulo = new JLabel();
			lblTitulo.setText("Gestion de productos");
			lblTitulo.setFont(new Font("Verdana", Font.BOLD, 18));
			panelSup = new JPanel();
			panelSup.setLayout(new BorderLayout());
			panelSup.add(lblTitulo, BorderLayout.WEST);
			panelSup.add(getPanelBotones(), BorderLayout.EAST);
		}
		return panelSup;
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
			panelBotones.add(getBtnAddProducto(), null);
			panelBotones.add(getBtnDelProducto(), null);
		}
		return panelBotones;
	}

	/**
	 * This method initializes btnAddProducto	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnAddProducto() {
		if (btnAddProducto == null) {
			btnAddProducto = new JButton();
			btnAddProducto.setIcon(new ImageIcon(getClass().getResource("/img/add.png")));
			btnAddProducto.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					dialogoAddProducto=getDialogoAddProducto();
					
					dialogoAddProducto.setVisible(true);
					
					if(dialogoAddProducto.getValorPulsado()==DialogoAddProducto.VALOR_ACEPTAR){
						//guardo los datos
						String nombre=dialogoAddProducto.getNombre();
						double precio=dialogoAddProducto.getPrecio();
						//al listado le paso el producto
						mainFrame.getListadoProductos().addProducto(new Productos(nombre, precio, idDistribuidor));
						//recargo los productos para que se actualice el nuevo
						cargarProductos();
						
					}
					
				}
			});
		}
		return btnAddProducto;
	}
	
	private DialogoAddProducto getDialogoAddProducto(){
		
		if(dialogoAddProducto==null){
			
			dialogoAddProducto=new DialogoAddProducto(mainFrame);
			
		}
		
		return dialogoAddProducto;
		
	}
	
	private DialogoModProducto getDialogoModProducto(){
		
		if(dialogoModProducto==null){
			
			dialogoModProducto=new DialogoModProducto(mainFrame);
			
		}
		
		return dialogoModProducto;
		
	}

	/**
	 * This method initializes btnDelProducto	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnDelProducto() {
		if (btnDelProducto == null) {
			btnDelProducto = new JButton();
			btnDelProducto.setIcon(new ImageIcon(getClass().getResource("/img/delete.png")));
			btnDelProducto.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					int index=tablaProductos.getSelectedRow();
					
					if(index>=0){
						
						mainFrame.getListadoProductos().removeElementAt(index);
						
						modelo.removeElementAt(index);
						
					}else{
						try {
							throw new Exception("Debe seleccionar un producto");
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
					}
					
				}
			});
		}
		return btnDelProducto;
	}
	
	public void cargarProductos(){
		//traigo el listado con el que voy a trabajar
		ListadoProductos listado=mainFrame.getListadoProductos();
		//guardo el distribuidor de estos productos
		idDistribuidor=listado.getProducto(0).getIdDistribuidor();
		Vector linea=new Vector();
		Vector<Vector> data=new Vector<Vector>();
		
		for(int x=0; x< listado.getSize(); x++){
			
			linea=new Vector();
			
			Productos p=listado.getProducto(x);
			
			linea.add(p.getIdProducto());
			linea.add(p.getNombre());
			linea.add(p.getPrecio());
		
			data.add(linea);
		}
		
		modelo.setData(data);
		
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

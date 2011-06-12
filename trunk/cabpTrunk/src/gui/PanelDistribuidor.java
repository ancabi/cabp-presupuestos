/**
 * 
 */
package gui;


import javax.swing.JPanel;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import clases.Distribuidor;
import clases.ListadoDistribuidores;

import modelo.ModeloDistribuidor;

import java.awt.Font;
import java.util.Vector;

/**
 * @author ancabi
 *
 */
public class PanelDistribuidor extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane scrollTabla = null;
	private JTable tablaDistribuidor = null;
	private JPanel panelBotones = null;
	private JButton btnAddDistribuidor = null;
	private JButton btnDelDistribuidor = null;
	private JPanel panelSuperior = null;
	private JLabel lblTitulo = null;
	private JFrame mainFrame;
	private ModeloDistribuidor modelo=null;
	private DialogoAddDistribuidor dialogoAddDistribuidor;

	/**
	 * This is the default constructor
	 */
	public PanelDistribuidor(JFrame mainFrame) {
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
		this.setSize(599, 408);
		this.setLayout(new BorderLayout());
		this.add(getScrollTabla(), BorderLayout.CENTER);
		this.add(getPanelSuperior(), BorderLayout.NORTH);
	}

	/**
	 * This method initializes scrollTabla	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollTabla() {
		if (scrollTabla == null) {
			scrollTabla = new JScrollPane();
			scrollTabla.setViewportView(getTablaDistribuidor());
		}
		return scrollTabla;
	}

	/**
	 * This method initializes tablaDistribuidor	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTablaDistribuidor() {
		if (tablaDistribuidor == null) {
			
			modelo=new ModeloDistribuidor();
			
			Vector<String> header=new Vector<String>();
			
			header.add("ID Distribuidor");
			header.add("Nombre");
			header.add("Direccion");
			header.add("Telefono");
			header.add("Email");
			header.add("Ciudad");
			header.add("Provincia");
			header.add("Pais");
			
			modelo.setHeader(header);
			
			tablaDistribuidor = new JTable(modelo);
		}
		
		tablaDistribuidor.setAutoCreateRowSorter(true);
		
		return tablaDistribuidor;
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
			panelBotones.add(getBtnAddDistribuidor(), null);
			panelBotones.add(getBtnDelDistribuidor(), null);
		}
		return panelBotones;
	}

	/**
	 * This method initializes btnAddDistribuidor	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnAddDistribuidor() {
		if (btnAddDistribuidor == null) {
			btnAddDistribuidor = new JButton();
			btnAddDistribuidor.setIcon(new ImageIcon(getClass().getResource("/img/add.png")));
			btnAddDistribuidor.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					dialogoAddDistribuidor=getDialogoAddDistribuidor();
					
					dialogoAddDistribuidor.setVisible(true);
					
					if(dialogoAddDistribuidor.getValorPulsado()==DialogoAddDistribuidor.VALOR_ACEPTAR){
						
						Distribuidor d=dialogoAddDistribuidor.getNewDistribuidor();
						
						((MainFrame) mainFrame).getListadoDistribuidores().addDistribuidor(d);
						
						cargarDistribuidores();
						
					}
					
					dialogoAddDistribuidor.limpiarCampos();
					
				}
			});
		}
		return btnAddDistribuidor;
	}
	
	private DialogoAddDistribuidor getDialogoAddDistribuidor(){
		
		if(dialogoAddDistribuidor==null){
			
			dialogoAddDistribuidor=new DialogoAddDistribuidor(mainFrame);
			
		}
		
		return dialogoAddDistribuidor;
		
	}

	/**
	 * This method initializes btnDelDistribuidor	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnDelDistribuidor() {
		if (btnDelDistribuidor == null) {
			btnDelDistribuidor = new JButton();
			btnDelDistribuidor.setIcon(new ImageIcon(getClass().getResource("/img/delete.png")));
			btnDelDistribuidor.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					int index=tablaDistribuidor.getSelectedRow();
					
					if(index>=0){
						
						((MainFrame) mainFrame).getListadoDistribuidores().removeElementAt(index);
						
						cargarDistribuidores();
						
					}else{
						try {
							throw new Exception("Debe seleccionar un distribuidor");
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(mainFrame, e1.getMessage());
						}
					}
					
				}
			});
		}
		return btnDelDistribuidor;
	}

	/**
	 * This method initializes panelSuperior	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelSuperior() {
		if (panelSuperior == null) {
			lblTitulo = new JLabel();
			lblTitulo.setText("Gestion de distribuidores");
			lblTitulo.setFont(new Font("Verdana", Font.BOLD, 18));
			panelSuperior = new JPanel();
			panelSuperior.setLayout(new BorderLayout());
			panelSuperior.add(lblTitulo, BorderLayout.WEST);
			panelSuperior.add(getPanelBotones(), BorderLayout.EAST);
		}
		return panelSuperior;
	}
	
	public void cargarDistribuidores(){
		
		ListadoDistribuidores listado=((MainFrame) mainFrame).getDistribuidores();
		Vector linea;
		Vector data=new Vector();
		for(int x=0; x<listado.getSize(); x++){
			
			linea=new Vector();
			
			Distribuidor d=listado.getDistribuidor(x);
			
			linea.add(d.getIdDistribuidor());
			linea.add(d.getNombre());
			linea.add(d.getDireccion());
			linea.add(d.getTelefono());
			linea.add(d.getEmail());
			linea.add(d.getCiudad());
			linea.add(d.getProvincia());
			linea.add(d.getPais());
			
			data.add(linea);
			
		}
		
		modelo.setData(data);
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

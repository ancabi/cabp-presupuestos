/**
 * 
 */
package gui;





import javax.swing.JPanel;

import java.awt.BorderLayout;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import clases.Cliente;
import clases.Distribuidor;
import clases.ListadoDistribuidores;
import clases.ListadoPresupuestos;
import clases.Presupuestos;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


import java.awt.FlowLayout;
import java.util.Vector;

/**
 * @author ancabi
 *
 */
public class PanelClientePres extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTree treePresupuestos = null;
	private JPanel panelPresupuesto = null;
	private ListadoPresupuestos presupuestos=new ListadoPresupuestos();  //  @jve:decl-index=0:
	private DefaultMutableTreeNode root;  //  @jve:decl-index=0:
	private DefaultTreeModel modelo=null;
	private JPanel panelHerramientas = null;
	private JButton btnConvert = null;
	private int id=-1;
	private TabCliente tabCliente=null;
	private DialogoModPresupuesto dialogoModPresupuesto;
	private Cliente c;  //  @jve:decl-index=0:
	private ListadoDistribuidores distribuidores=new ListadoDistribuidores();  //  @jve:decl-index=0:
	/**
	 * This is the default constructor
	 */
	public PanelClientePres(TabCliente tabCliente) {
		super();
		initialize();
		
		this.tabCliente=tabCliente;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(1343, 657);
		this.setLayout(new BorderLayout());
		this.add(getTreePresupuestos(), BorderLayout.WEST);
		this.add(getPanelPresupuesto(), BorderLayout.CENTER);
		this.add(getPanelHerramientas(), BorderLayout.NORTH);
		
		distribuidores.cargarDistribuidores();
	}

	/**
	 * This method initializes treePresupuestos	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getTreePresupuestos() {
		if (treePresupuestos == null) {
			
			root=new DefaultMutableTreeNode("Presupuestos");
			
			
			treePresupuestos = new JTree(root);
			treePresupuestos.setPreferredSize(new Dimension(190, 0));
			treePresupuestos.setMinimumSize(new Dimension(180, 0));
			treePresupuestos.setShowsRootHandles(true);
			treePresupuestos.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
						public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
							TreePath path = e.getPath();
							Object [] nodos = path.getPath();
							
							DefaultMutableTreeNode ultimoNodo =(DefaultMutableTreeNode)nodos[nodos.length-1];
							
							String temp=(String) ultimoNodo.getUserObject();
							
							temp=temp.substring(temp.indexOf('�')+2);
							
							try{
							//convierto lo que antes hice un substring a int, si lanza la excepcion, la capturo pero no hago nada con ella
							//si no lanza nada, es que el numero es valido y traigo el presupuesto con ese numero
							id=Integer.parseInt(temp);
							
							Presupuestos p=presupuestos.getPresupuesto(id);
							
							((PanelPresupuesto) panelPresupuesto).setPresupuesto(p);
							
							}catch(NumberFormatException e1){
								id=-1;

								limpiarCampos();
							}catch(NullPointerException e1){
								//no se porque salta esta excepcion
							}
							
						}
					});			
			treePresupuestos.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (e.getClickCount() >= 2){

						if(id>-1){
							//obtengo la instancia del objeto
							dialogoModPresupuesto=getDialogoModPresupuesto();
							//traigo el objeto clickeado
							Presupuestos p=presupuestos.getPresupuesto(id);
							//se lo asigno a la ventana de modificar presupuesto
							dialogoModPresupuesto.setPresupuesto(p);
							//cargo los productos del distribuidor asignado
							dialogoModPresupuesto.cargarProductos();
							//le asigno el cliente
							dialogoModPresupuesto.setIdCliente(c);
							//lo pongo visible
							dialogoModPresupuesto.setVisible(true);
							//si acepto
							if(dialogoModPresupuesto.getValorPulsado()==DialogoModPresupuesto.VALOR_ACEPTAR){
								
								//lo actualizo en memoria y en bd
								presupuestos.actualizarPresupuesto(id);
								
								//recargo los presupuestos en el tree
								cargarPresupuestos();
								
								limpiarCampos();
							}
						}
						
					}
				}
			});
		}
		return treePresupuestos;
	}

	/**
	 * This method initializes panelPresupuesto	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public JPanel getPanelPresupuesto() {
		if (panelPresupuesto == null) {
			panelPresupuesto = new PanelPresupuesto();
			
			((PanelPresupuesto) panelPresupuesto).setIsPresupuesto(true, "");
			
			((PanelPresupuesto) panelPresupuesto).editableCampos(false);
		}
		return panelPresupuesto;
	}
	
	public void setIdCliente(Cliente c){
		
		presupuestos.setIdCliente(c.getIdCliente());
		//cargo los presupuestos del cliente que acabo de traer
		presupuestos.cargarPresupuestos();
		
		this.c=c;
		
		((PanelPresupuesto) panelPresupuesto).setCliente(c);
		
		cargarTree();
		
	}
	
	public void cargarPresupuestos(){
		
		//cargo los presupuestos del cliente que acabo de traer
		presupuestos.cargarPresupuestos();
		
		cargarTree();
		
		
		id=-1;
	}
	
	private void cargarTree(){
		
		root=new DefaultMutableTreeNode("Presupuestos");
		
		Distribuidor d;
		
		Vector<DefaultMutableTreeNode> nombreDis=new Vector<DefaultMutableTreeNode>();
		
		for(int x=0; x<distribuidores.getSize(); x++){
			
			
			d=distribuidores.getDistribuidor(x);
			
			nombreDis.add(new DefaultMutableTreeNode(d.getNombre()));
			
			root.add(nombreDis.get(x));
			
		}
		
		modelo=new DefaultTreeModel(root);
		treePresupuestos.setModel(modelo);

		
		for(int x=0; x<presupuestos.getSize(); x++){

			Presupuestos temp=presupuestos.getPresupuestoProveedor(x);
			
			if(temp!=null){
				
				for(int j=0; j<distribuidores.getSize(); j++){
					
					if(temp.getIdDistribuidor()==distribuidores.getDistribuidor(j).getIdDistribuidor()){
						
						String titulo="Presupuesto N� "+temp.getIdPresupuesto();
						
						makeNode(titulo, nombreDis.get(j));
						
					}
					
				}
				
			}
			
		}
		
		if(root.getChildCount()==0){
			makeNode("No hay presupuestos", root);
		}

		
		treePresupuestos.expandRow(0);
		
	
	}
	
	private DefaultMutableTreeNode makeNode(String title, DefaultMutableTreeNode parent){
		
		DefaultMutableTreeNode node;
		
		node=new DefaultMutableTreeNode(title);
		
		parent.add(node);
		
		return node;
		
	}

	public void limpiarCampos() {
		((PanelPresupuesto) panelPresupuesto).limpiarCampos();
		
	}

	/**
	 * This method initializes panelHerramientas	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelHerramientas() {
		if (panelHerramientas == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			panelHerramientas = new JPanel();
			panelHerramientas.setLayout(flowLayout);
			panelHerramientas.add(getBtnConvert(), null);
		}
		return panelHerramientas;
	}

	/**
	 * This method initializes btnConvert	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnConvert() {
		if (btnConvert == null) {
			btnConvert = new JButton();
			btnConvert.setIcon(new ImageIcon(getClass().getResource("/img/convert.png")));
			btnConvert.setToolTipText("Convertir a factura");
			btnConvert.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					if(id>-1){
					
						Presupuestos p=presupuestos.getPresupuesto(id);
						
						presupuestos.convertir(p);
						
						//no se debe borrar un presup al pasarlo a factura
						//presupuestos.removeElement(p);
						//
						//cargarPresupuestos();
						
						((PanelClienteFactura) tabCliente.getPanelClienteFactura()).cargarFacturas();
						
						//((PanelPresupuesto) panelPresupuesto).limpiarCampos();
						
						JOptionPane.showMessageDialog(null, "El presupuesto se ha convertido satisfactoriamente", "Conversion", JOptionPane.INFORMATION_MESSAGE);
					
						
						
					}else{
						try {
							throw new Exception("Debe seleccionar un presupuesto");
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage()+"panelClientePres");
						}
					}
			
				}
					
			});
		}
		return btnConvert;
	}
	
	private DialogoModPresupuesto getDialogoModPresupuesto(){
		
		if(dialogoModPresupuesto==null){
			
			dialogoModPresupuesto=new DialogoModPresupuesto(null);
			
		}
		
		return dialogoModPresupuesto;
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

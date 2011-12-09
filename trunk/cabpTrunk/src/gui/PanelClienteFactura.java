/**
 * 
 */
package gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import clases.Cliente;
import clases.ListadoFacturas;
import clases.Facturas;
import clases.Presupuestos;

import java.awt.Dimension;

/**
 * @author ancabi
 *
 */
public class PanelClienteFactura extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTree treeFacturas = null;
	private JPanel panelPresupuesto = null;
	private ListadoFacturas facturas=new ListadoFacturas();  //  @jve:decl-index=0:
	private DefaultMutableTreeNode root;  //  @jve:decl-index=0:
	private DefaultTreeModel modelo;
	private int id;
	private DialogoModPresupuesto dialogoModPresupuesto;
	private Cliente c;
	
	/**
	 * This is the default constructor
	 */
	public PanelClienteFactura() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(747, 488);
		this.setLayout(new BorderLayout());
		this.add(getTreeFacturas(), BorderLayout.WEST);
		this.add(getPanelPresupuesto(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes treeFacturas	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getTreeFacturas() {
		if (treeFacturas == null) {
			
			root=new DefaultMutableTreeNode("Facturas");
			
			
			treeFacturas = new JTree(root);
			treeFacturas.setMinimumSize(new Dimension(100, 0));
			treeFacturas.setPreferredSize(new Dimension(170, 0));
			treeFacturas.setShowsRootHandles(true);
			treeFacturas.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
						public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
							TreePath path = e.getPath();
							Object [] nodos = path.getPath();
							
							DefaultMutableTreeNode ultimoNodo =(DefaultMutableTreeNode)nodos[nodos.length-1];
							
							String temp=(String) ultimoNodo.getUserObject();
							
							temp=temp.substring(temp.indexOf('º')+2);
							
							try{
							//convierto lo que antes hice un substring a int, si lanza la excepcion, la capturo pero no hago nada con ella
							//si no lanza nada, es que el numero es valido y traigo la factura
							id=Integer.parseInt(temp);
							
							Facturas f=facturas.getFactura(id);
							
							((PanelPresupuesto) panelPresupuesto).setFactura(f);
							
							}catch(NumberFormatException e1){
								
								id=-1;
								
								limpiarCampos();
							}catch(NullPointerException e1){
								//no se porque salta esta excepcion
							}
							
						}
					});
			treeFacturas.addMouseListener(new java.awt.event.MouseAdapter() {
				

				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (e.getClickCount() >= 2){

						if(id>-1){
							//obtengo la instancia del objeto
							dialogoModPresupuesto=getDialogoModPresupuesto();
							//traigo el objeto clickeado
							Facturas f=facturas.getFactura(id);

							//se lo asigno a la ventana de modificar presupuesto
							dialogoModPresupuesto.setFactura(f);
							//cargo los productos del distribuidor asignado
							dialogoModPresupuesto.cargarProductos();
							//le paso el cliente
							dialogoModPresupuesto.setIdCliente(c);
							//lo pongo visible
							dialogoModPresupuesto.setVisible(true);
							//si acepto
							if(dialogoModPresupuesto.getValorPulsado()==DialogoModPresupuesto.VALOR_ACEPTAR){
								
								//lo actualizo en memoria y en bd
								facturas.actualizarFactura(id);
								
								//recargo las facturas en el tree
								cargarFacturas();
								
								limpiarCampos();
							}
						}
						
					}
				}
			});
		}
		return treeFacturas;
	}

	/**
	 * This method initializes panelPresupuesto
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public JPanel getPanelPresupuesto() {
		if (panelPresupuesto == null) {
			panelPresupuesto = new PanelPresupuesto();
			
			((PanelPresupuesto) panelPresupuesto).setIsPresupuesto(false);
			
			((PanelPresupuesto) panelPresupuesto).editableCampos(false);
			
		}
		return panelPresupuesto;
	}
	
	public void setIdCliente(Cliente c){
		
		facturas.setIdCliente(c.getIdCliente());
		//cargo las facturas del cliente que acabo de traer
		facturas.cargarFacturas();
		
		this.c=c;
		
		((PanelPresupuesto) panelPresupuesto).setCliente(c);
		
		cargarTree();
		
	}
	
	public void cargarFacturas(){
		//cargo los presupuestos del cliente que acabo de traer
		facturas.cargarFacturas();
		
		cargarTree();
	}
	
	private void cargarTree(){
		
		root=new DefaultMutableTreeNode("Facturas");
		DefaultMutableTreeNode bison = new DefaultMutableTreeNode("Bison");
		DefaultMutableTreeNode acorn = new DefaultMutableTreeNode("Acorn");
		modelo=new DefaultTreeModel(root);
		treeFacturas.setModel(modelo);
		
		root.add(bison);
		root.add(acorn);
		
		for(int x=0; x<facturas.getSize(); x++){

			Facturas temp=facturas.getFacturaProveedor(x);
			
			if(temp!=null){
				
				if(temp.getIdDistribuidor()==1){
				
					String titulo="Factura Nº "+temp.getIdFactura();
				
					makeNode(titulo, bison);
					
				}else if(temp.getIdDistribuidor()==2){
					
					String titulo="Factura Nº "+temp.getIdFactura();
					
					makeNode(titulo, acorn);
				}else{
					
					String titulo="Factura Nº "+temp.getIdFactura();
					
					makeNode(titulo, root);
				}
			}
			
		}
		
		if(root.getChildCount()==0){
			makeNode("No hay facturas", root);
		}
		
		/*if(bison.getChildCount()==0){
			makeNode("No hay facturas", bison);
		}
		
		if(acorn.getChildCount()==0){
			makeNode("No hay facturas", acorn);
		}*/
		
		treeFacturas.expandRow(0);
		
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
	
	private DialogoModPresupuesto getDialogoModPresupuesto(){
		
		if(dialogoModPresupuesto==null){
			
			dialogoModPresupuesto=new DialogoModPresupuesto(null);
			
		}
		
		return dialogoModPresupuesto;
		
	}

}

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

import clases.ListadoPresupuestos;
import clases.Presupuestos;

/**
 * @author ancabi
 *
 */
public class PanelClientePres extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTree treePresupuestos = null;
	private JPanel panelPresupuesto = null;
	private ListadoPresupuestos presupuestos=new ListadoPresupuestos();  //  @jve:decl-index=0:
	private DefaultMutableTreeNode bison, acorn, root;  //  @jve:decl-index=0:
	private DefaultTreeModel modelo;
	/**
	 * This is the default constructor
	 */
	public PanelClientePres() {
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
		this.add(getTreePresupuestos(), BorderLayout.WEST);
		this.add(getPanelPresupuesto(), BorderLayout.CENTER);
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
			treePresupuestos
					.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
						public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
							TreePath path = e.getPath();
							Object [] nodos = path.getPath();
							
							DefaultMutableTreeNode ultimoNodo =(DefaultMutableTreeNode)nodos[nodos.length-1];
							
							// Por ejemplo, para ver si se ha seleccionado el "hijo1"...
							if(!ultimoNodo.getUserObject().equals("Presupuestos") && !ultimoNodo.getUserObject().equals("bison")
									&& !ultimoNodo.getUserObject().equals("acorn") && !ultimoNodo.getUserObject().equals("No hay presupuestos")){
								
								String temp=(String) ultimoNodo.getUserObject();
								
								temp=temp.substring(temp.indexOf('º')+2);
								System.out.println(temp);
								
								Presupuestos p=presupuestos.getPresupuesto(Integer.parseInt(temp));
								
								((PanelPresupuesto) panelPresupuesto).setPresupuesto(p);
								
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
	private JPanel getPanelPresupuesto() {
		if (panelPresupuesto == null) {
			panelPresupuesto = new PanelPresupuesto();
		}
		return panelPresupuesto;
	}
	
	public void setIdCliente(int idCliente){
		
		presupuestos.setIdCliente(idCliente);
		//cargo los presupuestos del cliente que acabo de traer
		presupuestos.cargarPresupuestos();
		
		cargarTree();
		
	}
	
	public void cargarPresupuestos(){
		//cargo los presupuestos del cliente que acabo de traer
		presupuestos.cargarPresupuestos();
		
		cargarTree();
	}
	
	private void cargarTree(){
		
		root=new DefaultMutableTreeNode("Presupuestos");
		modelo=new DefaultTreeModel(root);
		treePresupuestos.setModel(modelo);
		
		bison=makeNode("bison", root);
		acorn=makeNode("acorn", root);
		
		for(int x=0; x<presupuestos.getSize(); x++){
			
			Presupuestos temp=presupuestos.getPresupuestoProveedor(x, 1);
			
			if(temp!=null){
				
				String titulo="Presupuesto Nº "+temp.getIdPresupuesto();
				
				makeNode(titulo, bison);
			}
			
			temp=presupuestos.getPresupuestoProveedor(x, 2);
			
			if(temp!=null){
				
				String titulo="Presupuesto Nº "+temp.getIdPresupuesto();
				
				makeNode(titulo, acorn);
			}
		}
		
		if(bison.getChildCount()==0){
			makeNode("No hay presupuestos", bison);
		}
		
		if(acorn.getChildCount()==0){
			makeNode("No hay presupuestos", acorn);
		}
		
	}
	
	private DefaultMutableTreeNode makeNode(String title, DefaultMutableTreeNode parent){
		
		DefaultMutableTreeNode node;
		
		node=new DefaultMutableTreeNode(title);
		
		parent.add(node);
		
		return node;
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

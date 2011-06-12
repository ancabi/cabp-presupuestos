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
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;

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
	private DefaultTreeModel modelo=null;
	private JPanel panelHerramientas = null;
	private JButton btnConvert = null;
	private int id=-1;
	private TabCliente tabCliente=null;
	private DialogoModPresupuesto dialogoModPresupuesto;
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
		this.setSize(747, 488);
		this.setLayout(new BorderLayout());
		this.add(getTreePresupuestos(), BorderLayout.WEST);
		this.add(getPanelPresupuesto(), BorderLayout.CENTER);
		this.add(getPanelHerramientas(), BorderLayout.NORTH);
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
			treePresupuestos.setPreferredSize(new Dimension(180, 0));
			treePresupuestos.setMinimumSize(new Dimension(180, 0));
			treePresupuestos.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
						public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
							TreePath path = e.getPath();
							Object [] nodos = path.getPath();
							
							DefaultMutableTreeNode ultimoNodo =(DefaultMutableTreeNode)nodos[nodos.length-1];
							
							String temp=(String) ultimoNodo.getUserObject();
							
							temp=temp.substring(temp.indexOf('º')+2);
							
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
							//lo pongo visible
							dialogoModPresupuesto.setVisible(true);
							//si aceptó
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
	private JPanel getPanelPresupuesto() {
		if (panelPresupuesto == null) {
			panelPresupuesto = new PanelPresupuesto();
			
			((PanelPresupuesto) panelPresupuesto).setIsPresupuesto(true);
			
			((PanelPresupuesto) panelPresupuesto).editableCampos(false);
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
		
		id=-1;
	}
	
	private void cargarTree(){
		
		root=new DefaultMutableTreeNode("Presupuestos");
		modelo=new DefaultTreeModel(root);
		treePresupuestos.setModel(modelo);
		
		bison=makeNode("Bison", root);
		acorn=makeNode("Acorn", root);
		
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
						
						presupuestos.removeElement(p);
						
						cargarPresupuestos();
						
						((PanelClienteFactura) tabCliente.getPanelClienteFactura()).cargarFacturas();
						
						JOptionPane.showMessageDialog(null, "El presupuesto se ha convertido satisfactoriamente", "Conversion", JOptionPane.INFORMATION_MESSAGE);
					
					}else{
						try {
							throw new Exception("Debe seleccionar un presupuesto");
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
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

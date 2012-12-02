package gui;
import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import clases.Cliente;
import clases.Facturas;
import clases.ListadoClientes;
import clases.ListadoFacturas;


public class PanelVerFacturas extends javax.swing.JPanel {
	private JPanel panelFactura;
	private JScrollPane panelFactCliente;
	private JList listaFactCliente;
	private JScrollPane scrollListaFactClientes;
	private JList listaFactClientes;
	private DefaultListModel<String> modeloLista;
	private JFrame mainFrame=null;
	private ListadoClientes clientes;
	private Vector<Facturas> facturas=null;
	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new PanelVerFacturas(null));
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public PanelVerFacturas(JFrame mainFrame) {
		super();
		initGUI();
		this.mainFrame=mainFrame;
		
		
	}
	
	private void initGUI() {
		try {
			BorderLayout thisLayout = new BorderLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(770, 448));
			{
				scrollListaFactClientes = new JScrollPane();
				this.add(scrollListaFactClientes, BorderLayout.WEST);
				{
					
					modeloLista=new DefaultListModel<String>();
					
					listaFactCliente = new JList();
					scrollListaFactClientes.setViewportView(listaFactCliente);
					listaFactCliente.setModel(modeloLista);
					listaFactCliente.setPreferredSize(new java.awt.Dimension(167, 445));
					listaFactCliente.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							
							
							int id;							
							
							id=listaFactCliente.getSelectedIndex();
							Facturas f=facturas.get(id);
							Cliente c=clientes.getClientePorId(f.getIdCliente());
							
							((PanelPresupuesto) panelFactura).setFactura(f);
							((PanelPresupuesto) panelFactura).setCliente(c);
							
							
							
						}
					});
				}
			}
			{
				panelFactura = new PanelPresupuesto();
				this.add(panelFactura, BorderLayout.CENTER);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void cargarListados(){
		
		modeloLista.clear();
		((PanelPresupuesto) panelFactura).editableCampos(false);
		((PanelPresupuesto) panelFactura).limpiarCampos();
		((PanelPresupuesto) panelFactura).editableCampos(false);
		
		clientes=((MainFrame) mainFrame).getListadoClientes();
		
		ListadoFacturas listadoFacturas=new ListadoFacturas();
		listadoFacturas.cargarTodasFacturas();
		facturas=listadoFacturas.getFacturas();
		
		ListIterator<Facturas> i=facturas.listIterator();
		Facturas f;
		Cliente c;
		String factCliente;
		while(i.hasNext()){
			f=i.next();
			c=clientes.getClientePorId(f.getIdCliente());
			factCliente=""+f.getIdFactura()+" "+c.getNombre()+" "+c.getApellidos();
			
			modeloLista.addElement(factCliente);
			
		}
	}

}

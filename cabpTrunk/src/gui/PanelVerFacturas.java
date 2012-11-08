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
import clases.ListadoFacturas;

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
public class PanelVerFacturas extends javax.swing.JPanel {
	private JPanel panelFactura;
	private JScrollPane panelFactCliente;
	private JList listaFactCliente;
	private JScrollPane scrollListaFactClientes;
	private JList listaFactClientes;
	private DefaultListModel<String> modeloLista;
	private JFrame mainFrame=null;
	private Vector<Cliente> clientes=new Vector<Cliente>();
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
		clientes=((MainFrame) mainFrame).getListadoClientes().getClientes();
		
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
					ListadoFacturas listadoFacturas=new ListadoFacturas();
					listadoFacturas.cargarTodasFacturas();
					facturas=listadoFacturas.getFacturas();
					modeloLista=new DefaultListModel<String>();
					ListIterator<Facturas> i=facturas.listIterator();
					System.out.println(facturas.size());
					Facturas f;
					Cliente c;
					String factCliente;
					while(i.hasNext()){
						f=i.next();
						factCliente=""+f.getIdFactura();
						
						modeloLista.addElement(factCliente);
						
					}
					listaFactCliente = new JList();
					scrollListaFactClientes.setViewportView(listaFactCliente);
					listaFactCliente.setModel(modeloLista);
					listaFactCliente.setPreferredSize(new java.awt.Dimension(167, 445));
					listaFactCliente.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							listaFactCliente.getSelectedIndex();
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

}

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
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ImageIcon;

import clases.Cliente;
import conexion.Conectar;
import javax.swing.JTabbedPane;

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
	private Connection dbConnect;
	private Conectar con;  //  @jve:decl-index=0:
	private JTabbedPane tabCliente = null;
	private JPanel panelAddClientes = null;
	
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
		this.setJMenuBar(getJJMenuBar());
		
		con=new Conectar();
		
		try {
			
			dbConnect=con.makeConnection();
			
			PreparedStatement psFK=dbConnect.prepareStatement("PRAGMA foreign_keys=ON;");
			
			psFK.execute();
			
		} catch (ClassNotFoundException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
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
						con.closeConnection(dbConnect);
					}
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
			jButton.setIcon(new ImageIcon(getClass().getResource("/img/user.png")));
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
			panelCard.add(getTabCliente(), getTabCliente().getName());
			panelCard.add(getPanelAddClientes(), getPanelAddClientes().getName());
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
	private JPanel getPanelCliente() {
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
	 * This method initializes tabCliente	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getTabCliente() {
		if (tabCliente == null) {
			tabCliente = new TabCliente(this);
			tabCliente.setName("tabCliente");
		}
		return tabCliente;
	}

	/**
	 * This method initializes panelAddClientes	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelAddClientes() {
		if (panelAddClientes == null) {
			panelAddClientes = new PanelDatosClientes(this);
			panelAddClientes.setName("panelAddClientes");
		}
		return panelAddClientes;
	}
	
	public void rowSelected(Cliente c){
	
		 ((TabCliente) tabCliente).rowSelected(c);
		
	}
	
	public Connection getConnection(){
		
		return dbConnect;
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

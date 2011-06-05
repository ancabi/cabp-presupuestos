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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ImageIcon;

import clases.Cliente;
import clases.Emails;
import clases.ListadoClientes;
import clases.Telefonos;
import conexion.Conectar;
import javax.swing.JTabbedPane;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.Font;

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
	private JButton btnPresupuesto = null;
	private ListadoClientes listadoClientes= null;  //  @jve:decl-index=0:
	private JLabel lblLogo = null;
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
		this.setForeground(Color.black);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/cabp1.jpg")));
		this.setJMenuBar(getJJMenuBar());
			
		try {
			//conecto con la BD
			Conectar.makeConnection();
		} catch (ClassNotFoundException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}

		dbConnect=Conectar.getConnection();
			
		if(dbConnect!=null){
			
			listadoClientes=new ListadoClientes();
				
			listadoClientes.cargarClientes();
			
		}

		this.setContentPane(getJContentPane());
		this.setTitle("CABP");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				
				try {
					//cierro la conexion
					Conectar.closeConnection();
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
			jJToolBarBar.add(getBtnPresupuesto());
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
			jButton.setMargin(new Insets(20, 34, 2, 34));
			jButton.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
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
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(5, 318, 256, 318);
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridx = 0;
			lblLogo = new JLabel();
			lblLogo.setText("");
			lblLogo.setPreferredSize(new Dimension(281, 271));
			lblLogo.setIcon(new ImageIcon(getClass().getResource("/img/cabp1.jpg")));
			panelVacio = new JPanel();
			panelVacio.setLayout(new GridBagLayout());
			panelVacio.setName("panelVacio");
			panelVacio.add(lblLogo, gridBagConstraints);
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
	 * This method initializes btnPresupuesto	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnPresupuesto() {
		if (btnPresupuesto == null) {
			btnPresupuesto = new JButton();
			btnPresupuesto.setIcon(new ImageIcon(getClass().getResource("/img/presupuesto.png")));
			btnPresupuesto.setMargin(new Insets(20, 34, 2, 34));
			btnPresupuesto.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			btnPresupuesto.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					//cambio la capa para el panel de presupuestos
					cambiarCapa("panelPresupuesto");
				}
			});
		}
		return btnPresupuesto;
	}

	public ListadoClientes getListadoClientes() {

		return listadoClientes;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

/**
 * 
 */
package gui;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JTabbedPane;

import clases.Cliente;

/**
 * @author ancabi
 *
 */
public class DialogoModCliente extends JDialog {

	private static final long serialVersionUID = 1L;
	public static final int VALOR_ACEPTAR=1;
	public static final int VALOR_CANCELAR=0;
	private JPanel jContentPane = null;
	private JPanel panelBotones = null;
	private JButton btnAceptar = null;
	private JButton btnCancelar = null;
	private int valorPulsado=0;
	private JTabbedPane tabCliente = null;

	/**
	 * @param owner
	 */
	public DialogoModCliente(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(781, 558);
		this.setContentPane(getJContentPane());
		this.setModal(true);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				
				setVisible(false);
				
				((TabCliente) tabCliente).limpiarCampos();
				
				valorPulsado=VALOR_CANCELAR;
			}
		});
		this.setLocationRelativeTo(getOwner());
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
			jContentPane.add(getPanelBotones(), BorderLayout.SOUTH);
			jContentPane.add(getTabCliente(), BorderLayout.CENTER);
		}
		return jContentPane;
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
			panelBotones.add(getBtnAceptar(), null);
			panelBotones.add(getBtnCancelar(), null);
		}
		return panelBotones;
	}

	/**
	 * This method initializes btnAceptar	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton();
			btnAceptar.setText("Aceptar");
			btnAceptar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					((TabCliente) tabCliente).guardarCliente();
					
					setVisible(false);
					
					((TabCliente) tabCliente).limpiarCampos();
					
					valorPulsado=VALOR_ACEPTAR;
					
				}
			});
		}
		return btnAceptar;
	}

	/**
	 * This method initializes btnCancelar	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton();
			btnCancelar.setText("Cancelar");
			btnCancelar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					setVisible(false);
					
					((TabCliente) tabCliente).limpiarCampos();
					
					valorPulsado=VALOR_CANCELAR;
				}
			});
		}
		return btnCancelar;
	}

	public int getValorPulsado() {
		return valorPulsado;
	}
	
	public Cliente getClienteActual(){
		return ((TabCliente)tabCliente).getClienteActual();
	}

	/**
	 * This method initializes tabCliente	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getTabCliente() {
		if (tabCliente == null) {
			tabCliente = new TabCliente();
		}
		return tabCliente;
	}
	
	public void setCliente(Cliente c){
		
		((TabCliente) tabCliente).setCliente(c);
		
	}
	
	

}  //  @jve:decl-index=0:visual-constraint="10,10"
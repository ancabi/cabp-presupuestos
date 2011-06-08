/**
 * 
 */
package gui;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JFrame;

import clases.Cliente;
import clases.Distribuidor;
import clases.ListadoClientes;
import clases.ListadoDistribuidores;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Dimension;

/**
 * @author ancabi
 *
 */
public class DialogoSeleccionDistribuidor extends JDialog {

	private static final long serialVersionUID = 1L;
	public static final int VALOR_ACEPTAR=1;
	public static final int VALOR_CANCELAR=0;
	private JPanel jContentPane = null;
	private JPanel panelBotones = null;
	private JPanel panelSeleccion = null;
	private JLabel lblSeleccion = null;
	private JComboBox cbProveedor = null;
	private JButton btnAceptar = null;
	private JButton btnCancelar = null;
	private JFrame mainFrame=null;
	private int valorPulsado=0;
	private DefaultComboBoxModel modeloCB;
	private int idDistribuidor;
	/**
	 * @param owner
	 */
	public DialogoSeleccionDistribuidor(Frame owner) {
		super(owner);
		initialize();
		
		mainFrame=(JFrame) owner;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 156);
		this.setTitle("Seleccione un proveedor");
		this.setContentPane(getJContentPane());
		this.setModal(true);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
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
			jContentPane.add(getPanelSeleccion(), BorderLayout.CENTER);
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
	 * This method initializes panelSeleccion	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelSeleccion() {
		if (panelSeleccion == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints.gridy = 0;
			lblSeleccion = new JLabel();
			lblSeleccion.setText("Seleccione un proveedor:");
			panelSeleccion = new JPanel();
			panelSeleccion.setLayout(new GridBagLayout());
			panelSeleccion.add(lblSeleccion, gridBagConstraints);
			panelSeleccion.add(getCbProveedor(), gridBagConstraints1);
		}
		return panelSeleccion;
	}

	/**
	 * This method initializes cbProveedor	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbProveedor() {
		if (cbProveedor == null) {
			
			modeloCB=getModeloCombo();
			
			cbProveedor = new JComboBox(modeloCB);
		}
		return cbProveedor;
	}
	
	private DefaultComboBoxModel getModeloCombo() {

		if(modeloCB==null){
			
			modeloCB= new DefaultComboBoxModel();
			
		}
		
		return modeloCB;
		
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
					
					valorPulsado=VALOR_ACEPTAR;
					
					setVisible(false);
					
					String temp=(String) cbProveedor.getSelectedItem();
					
					temp=temp.substring(0, 1);
					
					idDistribuidor=Integer.parseInt(temp);
					
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
					
					valorPulsado=VALOR_CANCELAR;
					
					setVisible(false);
					
				}
			});
		}
		return btnCancelar;
	}
	
	public void cargarDistribuidores(){
		
		modeloCB.removeAllElements();
		//traigo los clientes
		
		ListadoDistribuidores listado=((MainFrame) mainFrame).getListadoDistribuidores();
		Distribuidor d;
		String nombre;
		//el primer lugar del combo box va a estar vacio
		if(modeloCB!=null){
			modeloCB.addElement("");
		}else{
			
			modeloCB=getModeloCombo();
			modeloCB.addElement("");
			
		}
		
		//cargo los clientes
		for(int x=0; x<listado.getSize(); x++){
			
			d=listado.getDistribuidor(x);
			
			
			nombre=d.getNombre();
			idDistribuidor=d.getIdDistribuidor();
			
			modeloCB.addElement(idDistribuidor+" "+nombre);
			
		}
		//pongo otra vez el seleccionado en el item vacio
		cbProveedor.setSelectedIndex(0);
		
	}

	public int getValorPulsado() {
		
		return valorPulsado;
	}

	public int getIdDistribuidor() {
		
		return idDistribuidor;
	}

}  //  @jve:decl-index=0:visual-constraint="435,96"

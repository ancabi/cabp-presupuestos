/**
 * 
 */
package gui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import java.awt.Insets;
import java.awt.Dimension;
import javax.swing.JButton;

import clases.Cliente;
import clases.ListadoClientes;

import java.awt.FlowLayout;

/**
 * @author ancabi
 *
 */
public class DialogoSeleccionCliente extends JDialog {

	private static final long serialVersionUID = 1L;
	public static final int VALOR_ACEPTAR=1;
	public static final int VALOR_CANCELAR=0;
	private JPanel jContentPane = null;
	private JLabel lblCliente = null;
	private JComboBox cbCliente = null;
	private JPanel panelSeleccion = null;
	private JPanel panelBotones = null;
	private JButton btnAceptar = null;
	private JButton btnCancelar = null;
	private DefaultComboBoxModel modeloCB;
	private int valorPulsado=0;
	private JFrame mainFrame;
	private int idCliente;

	/**
	 * @param owner
	 */
	public DialogoSeleccionCliente(Frame owner) {
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
		this.setSize(300, 147);
		this.setTitle("Seleccione un cliente");
		this.setContentPane(getJContentPane());
		this.setModal(true);
		this.setLocationRelativeTo(getOwner());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			lblCliente = new JLabel();
			lblCliente.setText("Seleccione el cliente:");
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getPanelSeleccion(), BorderLayout.CENTER);
			jContentPane.add(getPanelBotones(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes cbCliente	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbCliente() {
		if (cbCliente == null) {
			
			modeloCB=getModeloCombo();
			
			cbCliente = new JComboBox(modeloCB);
		}
		
		return cbCliente;
	}

	private DefaultComboBoxModel getModeloCombo() {

		if(modeloCB==null){
			
			modeloCB= new DefaultComboBoxModel();
			
		}
		
		return modeloCB;
		
	}

	/**
	 * This method initializes panelSeleccion	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelSeleccion() {
		if (panelSeleccion == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.NONE;
			panelSeleccion = new JPanel();
			panelSeleccion.setLayout(new GridBagLayout());
			panelSeleccion.add(lblCliente, gridBagConstraints);
			panelSeleccion.add(getCbCliente(), gridBagConstraints1);
		}
		return panelSeleccion;
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
					
					int conversion;
					
					if(cbCliente.getSelectedIndex()>0){
						setVisible(false);
						
						valorPulsado=VALOR_ACEPTAR;
						
						String temp=(String) modeloCB.getSelectedItem();
						
						temp=temp.substring(0, 2);

						try{
							conversion=Integer.parseInt(temp);
						}catch (NumberFormatException e1) {
							temp=temp.substring(0, 1);
						}
						
						idCliente=Integer.parseInt(temp);
					}else{
						try {
							throw new Exception("Debe seleccionar un cliente");
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(getOwner(), e1.getMessage());
						}
					}
					
					
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
					
					valorPulsado=VALOR_CANCELAR;
				}
			});
		}
		return btnCancelar;
	}
	
	public void actualizarCliente(){
		
		modeloCB.removeAllElements();
		//traigo los clientes
		ListadoClientes listado=((MainFrame) mainFrame).getListadoClientes();
		Cliente c;
		int idCliente;
		String nombre;
		String apellidos;
		//el primer lugar del combo box va a estar vacio
		if(modeloCB!=null){
			modeloCB.addElement("");
		}else{
			
			modeloCB=getModeloCombo();
			modeloCB.addElement("");
			
		}
		
		//cargo los clientes
		for(int x=0; x<listado.getSize(); x++){
			
			c=listado.getCliente(x);
			
			idCliente=c.getIdCliente();
			nombre=c.getNombre();
			apellidos=c.getApellidos();
			
			modeloCB.addElement(idCliente+" "+nombre+" "+apellidos);
			
		}
		//pongo otra vez el seleccionado en el item vacio
		cbCliente.setSelectedIndex(0);
		
	}

	/**
	 * @return the valorPulsado
	 */
	public int getValorPulsado() {
		return valorPulsado;
	}

	/**
	 * @return the idCliente
	 */
	public int getIdCliente() {
		return idCliente;
	}
	
	

}  //  @jve:decl-index=0:visual-constraint="473,84"

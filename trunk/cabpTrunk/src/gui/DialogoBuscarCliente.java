/**
 * 
 */
package gui;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Dimension;
import javax.swing.JRadioButton;
import java.awt.Insets;
import javax.swing.JComboBox;
import java.awt.FlowLayout;
import javax.swing.JButton;

/**
 * @author ancabi
 *
 */
public class DialogoBuscarCliente extends JDialog {

	private static final long serialVersionUID = 1L;
	public final int VALOR_ACEPTAR=1;
	public final int VALOR_CANCELAR=0;
	private JPanel jContentPane = null;
	private JLabel lblBuscar = null;
	private JTextField tfBuscar = null;
	private JLabel lblPor = null;
	private JComboBox cbCriterio = null;
	private JPanel panelDatos = null;
	private JPanel panelBotones = null;
	private JButton btnAceptar = null;
	private JButton btnCancelar = null;
	private DefaultComboBoxModel modeloCombo;
	private int valorPulsado=0;
	private String buscar;
	private String criterio;
	/**
	 * @param owner
	 */
	public DialogoBuscarCliente(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(236, 158);
		this.setContentPane(getJContentPane());
		this.setModal(true);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				
				valorPulsado=VALOR_CANCELAR;
				
				limpiarCampos();
				
				
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
			lblPor = new JLabel();
			lblPor.setText("Por:");
			lblBuscar = new JLabel();
			lblBuscar.setText("Buscar:");
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getPanelBotones(), BorderLayout.SOUTH);
			jContentPane.add(getPanelDatos(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes tfBuscar	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfBuscar() {
		if (tfBuscar == null) {
			tfBuscar = new JTextField();
			tfBuscar.setPreferredSize(new Dimension(100, 20));
		}
		return tfBuscar;
	}

	/**
	 * This method initializes cbCriterio	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbCriterio() {
		if (cbCriterio == null) {
			modeloCombo=new DefaultComboBoxModel();
			
			modeloCombo.addElement("");
			modeloCombo.addElement("DNI");
			modeloCombo.addElement("Nombre");
			modeloCombo.addElement("Apellidos");
			modeloCombo.addElement("Ciudad");
			modeloCombo.addElement("Provincia");
			
			cbCriterio = new JComboBox();
			cbCriterio.setPreferredSize(new Dimension(100, 25));
			
			cbCriterio.setModel(modeloCombo);
		}
		return cbCriterio;
	}

	/**
	 * This method initializes panelDatos	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelDatos() {
		if (panelDatos == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(2, 10, 2, 2);
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new Insets(2, 10, 2, 2);
			panelDatos = new JPanel();
			panelDatos.setLayout(new GridBagLayout());
			panelDatos.add(lblBuscar, gridBagConstraints);
			panelDatos.add(getTfBuscar(), gridBagConstraints1);
			panelDatos.add(lblPor, gridBagConstraints2);
			panelDatos.add(getCbCriterio(), gridBagConstraints3);
		}
		return panelDatos;
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
					
					buscar=tfBuscar.getText();
					
					criterio=cbCriterio.getSelectedItem().toString();
					if(buscar.equals("")){
						
						try {
							throw new Exception("Debe escribir algo para buscar");
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(getOwner(), e1.getMessage());
						}
						
					}else if(criterio.equals("")){
						try {
							throw new Exception("Debe seleccionar un criterio de busqueda");
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(getOwner(), e1.getMessage());
						}
					}else{
					
						setVisible(false);
						
						valorPulsado=VALOR_ACEPTAR;
						
						limpiarCampos();
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
					
					limpiarCampos();
					
				}
			});
		}
		return btnCancelar;
	}

	/**
	 * @return the valorPulsado
	 */
	public int getValorPulsado() {
		return valorPulsado;
	}
	
	public void limpiarCampos(){
		
		tfBuscar.setText("");
		cbCriterio.setSelectedIndex(0);
		
	}

	/**
	 * @return the buscar
	 */
	public String getBuscar() {
		return buscar;
	}

	/**
	 * @return the criterio
	 */
	public String getCriterio() {
		return criterio;
	}
	
	

}  //  @jve:decl-index=0:visual-constraint="478,103"

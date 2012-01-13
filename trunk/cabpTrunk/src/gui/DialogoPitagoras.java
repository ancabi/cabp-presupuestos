package gui;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.text.DecimalFormat;

public class DialogoPitagoras extends JDialog {

	private static final long serialVersionUID = 1L;
	public static final int VALOR_ACEPTAR=1;
	public static final int VALOR_CANCELAR=0;
	private JPanel jContentPane = null;
	private JPanel panelCard = null;
	private JPanel panelBotones = null;
	private JButton btnAceptar = null;
	private JButton btnCancelar = null;
	private JPanel panelPitagoras = null;
	private JTextField tfMedidaA = null;
	private JLabel lblA = null;
	private JLabel lblX1 = null;
	private JTextField tfCantidadA = null;
	private JLabel lblIgual1 = null;
	private JLabel lblTotalA = null;
	private JLabel lblB1 = null;
	private JTextField tfMedidaB = null;
	private JLabel lblX2 = null;
	private JTextField tfCantidadB = null;
	private JLabel lblIgual2 = null;
	private JLabel lblTotalB = null;
	private JLabel lblC = null;
	private JLabel lblTotalC = null;
	private JLabel lblMetros = null;
	private JLabel lblTotalMetros = null;
	private JLabel lblSumarPlataforma = null;
	private JTextField tfSumarPlataforma = null;
	private JLabel lblTotal = null;
	private JLabel lblTotalPlataforma = null;
	private JLabel lblTriangulo = null;
	private int valorPulsado=VALOR_CANCELAR;
	private double valorA=0;
	private double valorB=0;
	private double valorC=0;
	private double valorAux=0;
	private DecimalFormat formateador = new DecimalFormat ("#####.##");  //  @jve:decl-index=0:

	/**
	 * @param owner
	 */
	public DialogoPitagoras(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(573, 314);
		this.setContentPane(getJContentPane());
		this.setModal(true);
		this.setTitle("Calculo auxiliar");
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
			jContentPane.add(getPanelCard(), BorderLayout.CENTER);
			jContentPane.add(getPanelBotones(), BorderLayout.SOUTH);
		}
		return jContentPane;
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
			panelCard.add(getPanelPitagoras(), getPanelPitagoras().getName());
		}
		return panelCard;
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
					
					valorPulsado=VALOR_ACEPTAR;
					
					setVisible(false);
					
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

	/**
	 * This method initializes panelPitagoras	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelPitagoras() {
		if (panelPitagoras == null) {
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.gridx = 1;
			gridBagConstraints20.gridwidth = 3;
			gridBagConstraints20.gridheight = 4;
			gridBagConstraints20.gridy = 3;
			lblTriangulo = new JLabel();
			lblTriangulo.setText("");
			lblTriangulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			lblTriangulo.setIcon(new ImageIcon(getClass().getResource("/img/triangulo.png")));
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.gridx = 5;
			gridBagConstraints19.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints19.anchor = GridBagConstraints.WEST;
			gridBagConstraints19.gridy = 5;
			lblTotalPlataforma = new JLabel();
			lblTotalPlataforma.setText("0");
			lblTotalPlataforma.setForeground(Color.red);
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 4;
			gridBagConstraints18.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints18.anchor = GridBagConstraints.WEST;
			gridBagConstraints18.gridy = 5;
			lblTotal = new JLabel();
			lblTotal.setText("Total metros");
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints17.gridy = 4;
			gridBagConstraints17.weightx = 1.0;
			gridBagConstraints17.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints17.anchor = GridBagConstraints.WEST;
			gridBagConstraints17.gridx = 5;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 4;
			gridBagConstraints16.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints16.anchor = GridBagConstraints.WEST;
			gridBagConstraints16.gridy = 4;
			lblSumarPlataforma = new JLabel();
			lblSumarPlataforma.setText("Sumar plataforma");
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 5;
			gridBagConstraints15.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints15.anchor = GridBagConstraints.WEST;
			gridBagConstraints15.gridy = 3;
			lblTotalMetros = new JLabel();
			lblTotalMetros.setText("0");
			lblTotalMetros.setForeground(Color.red);
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 4;
			gridBagConstraints14.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.gridy = 3;
			lblMetros = new JLabel();
			lblMetros.setText("Metros");
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 5;
			gridBagConstraints13.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.gridy = 2;
			lblTotalC = new JLabel();
			lblTotalC.setText("0");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 4;
			gridBagConstraints12.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.gridy = 2;
			lblC = new JLabel();
			lblC.setText("C=");
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 5;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints11.gridy = 1;
			lblTotalB = new JLabel();
			lblTotalB.setText("0");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 4;
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints10.gridy = 1;
			lblIgual2 = new JLabel();
			lblIgual2.setText("igual =");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints9.gridy = 1;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints9.gridx = 3;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 2;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints8.gridy = 1;
			lblX2 = new JLabel();
			lblX2.setText("x cant. escalones");
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints6.gridy = 1;
			lblB1 = new JLabel();
			lblB1.setText("B=");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 5;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints5.gridy = 0;
			lblTotalA = new JLabel();
			lblTotalA.setText("0");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 4;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints4.gridy = 0;
			lblIgual1 = new JLabel();
			lblIgual1.setText("igual =");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints3.gridx = 3;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 2;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints2.gridy = 0;
			lblX1 = new JLabel();
			lblX1.setText("x cant. escalones");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints1.gridy = 0;
			lblA = new JLabel();
			lblA.setText("A=");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(3, 10, 3, 5);
			gridBagConstraints.gridx = 1;
			panelPitagoras = new JPanel();
			panelPitagoras.setLayout(new GridBagLayout());
			panelPitagoras.setName("panelPitagoras");
			panelPitagoras.add(getTfMedidaA(), gridBagConstraints);
			panelPitagoras.add(lblA, gridBagConstraints1);
			panelPitagoras.add(lblX1, gridBagConstraints2);
			panelPitagoras.add(getTfCantidadA(), gridBagConstraints3);
			panelPitagoras.add(lblIgual1, gridBagConstraints4);
			panelPitagoras.add(lblTotalA, gridBagConstraints5);
			panelPitagoras.add(lblB1, gridBagConstraints6);
			panelPitagoras.add(getTfMedidaB(), gridBagConstraints7);
			panelPitagoras.add(lblX2, gridBagConstraints8);
			panelPitagoras.add(getTfCantidadB(), gridBagConstraints9);
			panelPitagoras.add(lblIgual2, gridBagConstraints10);
			panelPitagoras.add(lblTotalB, gridBagConstraints11);
			panelPitagoras.add(lblC, gridBagConstraints12);
			panelPitagoras.add(lblTotalC, gridBagConstraints13);
			panelPitagoras.add(lblMetros, gridBagConstraints14);
			panelPitagoras.add(lblTotalMetros, gridBagConstraints15);
			panelPitagoras.add(lblSumarPlataforma, gridBagConstraints16);
			panelPitagoras.add(getTfSumarPlataforma(), gridBagConstraints17);
			panelPitagoras.add(lblTotal, gridBagConstraints18);
			panelPitagoras.add(lblTotalPlataforma, gridBagConstraints19);
			panelPitagoras.add(lblTriangulo, gridBagConstraints20);
		}
		return panelPitagoras;
	}

	/**
	 * This method initializes tfMedidaA	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfMedidaA() {
		if (tfMedidaA == null) {
			tfMedidaA = new JTextField();
			tfMedidaA.setPreferredSize(new Dimension(80, 20));
			tfMedidaA.setText("0");
			tfMedidaA.setMinimumSize(new Dimension(80, 20));
			tfMedidaA.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					calcularValores();
				}
			});
			tfMedidaA.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(java.awt.event.FocusEvent e) {
					calcularValores();
				}
			});
		}
		return tfMedidaA;
	}

	/**
	 * This method initializes tfCantidadA	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfCantidadA() {
		if (tfCantidadA == null) {
			tfCantidadA = new JTextField();
			tfCantidadA.setMinimumSize(new Dimension(80, 20));
			tfCantidadA.setText("0");
			tfCantidadA.setPreferredSize(new Dimension(80, 20));
			tfCantidadA.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					tfCantidadB.setText(tfCantidadA.getText());
					
					calcularValores();
					
				}
			});
			tfCantidadA.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(java.awt.event.FocusEvent e) {
					
					tfCantidadB.setText(tfCantidadA.getText());
					
					calcularValores();
					
				}
			});
		}
		return tfCantidadA;
	}

	/**
	 * This method initializes tfMedidaB	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfMedidaB() {
		if (tfMedidaB == null) {
			tfMedidaB = new JTextField();
			tfMedidaB.setMinimumSize(new Dimension(80, 20));
			tfMedidaB.setText("0");
			tfMedidaB.setPreferredSize(new Dimension(80, 20));
			tfMedidaB.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					calcularValores();
				}
			});
			tfMedidaB.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(java.awt.event.FocusEvent e) {
					calcularValores();
				}
			});
		}
		return tfMedidaB;
	}

	/**
	 * This method initializes tfCantidadB	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfCantidadB() {
		if (tfCantidadB == null) {
			tfCantidadB = new JTextField();
			tfCantidadB.setMinimumSize(new Dimension(80, 20));
			tfCantidadB.setPreferredSize(new Dimension(80, 20));
			tfCantidadB.setText("0");
			tfCantidadB.setEditable(false);
		}
		return tfCantidadB;
	}

	/**
	 * This method initializes tfSumarPlataforma	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfSumarPlataforma() {
		if (tfSumarPlataforma == null) {
			tfSumarPlataforma = new JTextField();
			tfSumarPlataforma.setPreferredSize(new Dimension(80, 20));
			tfSumarPlataforma.setText("0");
			tfSumarPlataforma.setMinimumSize(new Dimension(80, 20));
			tfSumarPlataforma.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					calcularValores();
					
				}
			});
			tfSumarPlataforma.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(java.awt.event.FocusEvent e) {
					
					calcularValores();
					
				}
			});
		}
		return tfSumarPlataforma;
	}
	
	private void calcularValores(){
		
		valorA=Double.parseDouble(tfMedidaA.getText())*Double.parseDouble(tfCantidadA.getText());
		valorB=Double.parseDouble(tfMedidaB.getText())*Double.parseDouble(tfCantidadB.getText());
		
		lblTotalA.setText(formateador.format(valorA));
		lblTotalB.setText(formateador.format(valorB));
		
		valorC=(valorA*valorA)+(valorB*valorB);
		valorC=Math.sqrt(valorC);
		lblTotalC.setText(formateador.format(valorC));
		
		double metros=valorC/100;
		lblTotalMetros.setText(formateador.format(metros));
		
		valorAux=metros+Double.parseDouble(tfSumarPlataforma.getText());
		lblTotalPlataforma.setText(formateador.format(valorAux));
		
	}

	/**
	 * @return the valorPulsado
	 */
	public int getValorPulsado() {
		return valorPulsado;
	}

	/**
	 * @return the valorA
	 */
	public double getValorA() {
		return valorA;
	}

	/**
	 * @return the valorB
	 */
	public double getValorB() {
		return valorB;
	}

	/**
	 * @return the valorC
	 */
	public double getValorC() {
		return valorC;
	}

	/**
	 * @return the valorAux
	 */
	public double getValorAux() {
		return valorAux;
	}
	
	private void limpiarCampos(){
		
		tfCantidadA.setText("0");
		tfCantidadB.setText("0");
		
		tfMedidaA.setText("0");
		tfMedidaB.setText("0");
		
		tfSumarPlataforma.setText("0");
		
		lblTotalA.setText("0");
		lblTotalB.setText("0");
		lblTotalC.setText("0");
		lblTotalMetros.setText("0");
		lblTotalPlataforma.setText("0");
		
	}
	
	

}  //  @jve:decl-index=0:visual-constraint="10,10"

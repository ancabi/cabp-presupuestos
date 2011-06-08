package gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import clases.Cliente;
import java.awt.GridBagLayout;


public class TabCliente extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	private JPanel panelDatos = null;
	private JPanel panelClientePres = null;
	private JPanel panelImg = null;
	private JPanel panelPdf = null;

	/**
	 * This is the default constructor
	 */
	public TabCliente() {
		super();
		initialize();
		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(548, 334);

		this.addTab("Datos", null, getPanelDatos(), null);
		this.addTab("Imagenes", null, getPanelImg(), null);
		this.addTab("PDF", null, getPanelPdf(), null);
		this.addTab("Presupuestos", null, getPanelClientePres(), null);
	}

	/**
	 * This method initializes panelDatos	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelDatos() {
		if (panelDatos == null) {
			panelDatos = new PanelDatosClientes();
			
		}
		return panelDatos;
	}

	/**
	 * This method initializes panelClientePres	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelClientePres() {
		if (panelClientePres == null) {
			panelClientePres = new JPanel();
			panelClientePres.setLayout(new GridBagLayout());
		}
		return panelClientePres;
	}
	
	public void setCliente(Cliente c){
		
		((PanelDatosClientes) panelDatos).setCliente(c);
		
		//pongo agregar a false
		((PanelDatosClientes) panelDatos).setAgregar();
		
	}
	
	public void guardarCliente(){
		
		((PanelDatosClientes) panelDatos).guardarCliente();
		
	}
	
	public PanelDatosClientes getPanelDatosClientes(){
		return (PanelDatosClientes) panelDatos;
	}

	/**
	 * This method initializes panelImg	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelImg() {
		if (panelImg == null) {
			panelImg = new JPanel();
		}
		return panelImg;
	}

	/**
	 * This method initializes panelPdf	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelPdf() {
		if (panelPdf == null) {
			panelPdf = new JPanel();
		}
		return panelPdf;
	}

	public void setMainFrame(JFrame mainFrame) {

		((PanelDatosClientes) panelDatos).setMainFrame(mainFrame);
		
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"

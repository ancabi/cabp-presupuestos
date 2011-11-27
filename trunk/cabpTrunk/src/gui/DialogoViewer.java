/**
 * 
 */
package gui;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JDialog;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;
import java.util.HashMap;

/**
 * @author ancabi
 *
 */
public class DialogoViewer extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;

	/**
	 * @param owner
	 */
	public DialogoViewer() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(832, 697);
		this.setContentPane(getJContentPane());
		this.setModal(true);
		this.setLocationRelativeTo(null);
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
		}
		return jContentPane;
	}
	
	public void run(String archivo, HashMap param){
		
		JasperReport report;
		try {
			report = JasperCompileManager.compileReport("facturaCABP.jrxml");
			
			JasperPrint print = JasperFillManager.fillReport(report, param, new JREmptyDataSource());
			
			//JasperViewer jviewer = new JasperViewer(print,false);
			
			JRViewer jviewer = new JRViewer(print); 
			
			this.getContentPane().add(jviewer, BorderLayout.CENTER);
			
		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

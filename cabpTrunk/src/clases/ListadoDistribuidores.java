/**
 * 
 */
package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;

import conexion.Conectar;

/**
 * @author ancabi
 *
 */
public class ListadoDistribuidores {

	Vector<Distribuidor> distribuidores=new Vector<Distribuidor>();
	Connection dbConnect=Conectar.getConnection();
	private PreparedStatement psDistribuidores;
	/**
	 * Constructor vacio
	 */
	public ListadoDistribuidores() {
		
		try {
			psDistribuidores=dbConnect.prepareStatement("SELEC * FROM distribuidores");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	
	
	
}

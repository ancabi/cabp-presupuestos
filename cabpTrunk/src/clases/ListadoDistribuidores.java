/**
 * 
 */
package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;

import conexion.Conectar;

/**
 * @author ancabi
 *
 */
public class ListadoDistribuidores {

	private Vector<Distribuidor> distribuidores=new Vector<Distribuidor>();
	private Connection dbConnect=Conectar.getConnection();
	private PreparedStatement psDistribuidores;
	/**
	 * Constructor vacio
	 */
	public ListadoDistribuidores() {
		
		try {
			psDistribuidores=dbConnect.prepareStatement("SELECT * FROM distribuidores");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	public void cargarDistribuidores(){
		
		try {
			ResultSet rs=psDistribuidores.executeQuery();
			
			while(rs.next()){
				
				int idDistribuidor=rs.getInt("idDistribuidor");
				String nombre=rs.getString("nombre");
				String direccion=rs.getString("direccion");
				String email=rs.getString("email");
				String telefono=rs.getString("telefono");
				String ciudad=rs.getString("ciudad");
				String provincia=rs.getString("provincia");
				String pais=rs.getString("pais");
				String numeroCta=rs.getString("numeroCta");
				String iban=rs.getString("iban");
				String swif=rs.getString("swif");
				
				distribuidores.addElement(new Distribuidor(idDistribuidor, nombre, direccion, email, telefono, ciudad, provincia, pais, numeroCta, iban, swif));
				
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public int getSize() {
		
		return distribuidores.size();
	}

	public Distribuidor getDistribuidor(int x) {
		
		return distribuidores.get(x);
	}
	
	
	
	
}

/**
 * 
 */
package conexion;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;
/**
 * @author ancabi
 *
 */
public class Conectar {
	
	private String bd="cabp.sqlite";

	
	/**
	 * Metodo que devulve la conexion
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Connection makeConnection() throws ClassNotFoundException, SQLException{
		
			File base=new File(bd);
			
			if(base.exists()){
				
				Class.forName("org.sqlite.JDBC");
				
				return DriverManager.getConnection("jdbc:sqlite:"+bd);
				
			}else{
				JOptionPane.showMessageDialog(null, "La base de datos no existe o no se encuentra en la ruta especificada.");  
				return null;
				
			}
		
			
			
	}
	
	/**
	 * Cierra la conexion y los preparedStatement
	 * 
	 * @throws SQLException
	 */
	public void closeConnection(Connection con) throws SQLException{
		
		if(con!=null){ 
			con.close();
		}
		
	}

}

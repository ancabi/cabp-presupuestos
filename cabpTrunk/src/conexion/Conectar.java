/**
 * 
 */
package conexion;

import java.io.File;
import org.sqlite.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;
/**
 * @author ancabi
 *
 */
public class Conectar {

	private static Connection dbConnect;

	
	/**
	 * Metodo que devulve la conexion
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void makeConnection(String bd) throws ClassNotFoundException, SQLException{
		
			File base=new File(bd);
			
			
			if(base.exists()){
				
				Class.forName("org.sqlite.JDBC");
				
				dbConnect=DriverManager.getConnection("jdbc:sqlite:"+bd);
				
			}else{
				JOptionPane.showMessageDialog(null, "La base de datos no existe o no se encuentra en la ruta especificada.");  
				
			}
		
			
			
	}
	
	/**
	 * Cierra la conexion y los preparedStatement
	 * 
	 * @throws SQLException
	 */
	public static void closeConnection() throws SQLException{
		
		if(dbConnect!=null){ 
			dbConnect.close();
		}
		
	}
	
	public static Connection getConnection(){
		
		return dbConnect;
		
	}

}

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
public class ListadoProductos {
	
	private Vector<Productos> productos=new Vector<Productos>();
	private Connection dbConnect=Conectar.getConnection();
	private int idDistribuidor; //esto debe pasarlo la ventana modal que selecciona el distribuidor;
	private PreparedStatement psProductos;
	
	public ListadoProductos(){
		
		try {
			psProductos=dbConnect.prepareStatement("SELECT * FROM productos WHERE idDistribuidor=?");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage()+"Constructor listadoProductos");
		}
		
	}
	
	public void cargarProductos(){
		
		try {
			psProductos.setInt(1, idDistribuidor);
		
		
			ResultSet rs=psProductos.executeQuery();
			
			while(rs.next()){
				
				int idProducto=rs.getInt("idProducto");
				String nombre=rs.getString("nombre");
				double precio=rs.getDouble("precio");
				
				productos.add(new Productos(idProducto, nombre, precio, idDistribuidor));
				
			}
		
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	public Productos getProducto(int index){
		
		return productos.get(index);
		
	}

	/**
	 * @param idDistribuidor the idDistribuidor to set
	 */
	public void setIdDistribuidor(int idDistribuidor) {
		this.idDistribuidor = idDistribuidor;
	}

	public int getSize() {
		
		return productos.size();
	}
	
	

}

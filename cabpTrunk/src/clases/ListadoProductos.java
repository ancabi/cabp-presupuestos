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
	private PreparedStatement psBorrarProducto;
	private PreparedStatement psInsertarProducto;
	private PreparedStatement psActualizarProducto;
	
	public ListadoProductos(){
		
		try {
			psProductos=dbConnect.prepareStatement("SELECT * FROM productos WHERE idDistribuidor=?");
			
			psBorrarProducto=dbConnect.prepareStatement("DELETE FROM productos WHERE idProducto=?");
			
			psInsertarProducto=dbConnect.prepareStatement("INSERT INTO productos(nombre, precio, idDistribuidor) VALUES (?,?,?)");
			
			psActualizarProducto=dbConnect.prepareStatement("UPDATE productos SET nombre=?, precio=? WHERE idProducto=?");
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage()+"Constructor listadoProductos");
		}
		
	}
	
	public void cargarProductos(){
		
		productos.removeAllElements();
		
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

	public void removeElementAt(int index) {
		
		Productos p=getProducto(index);
		
		try {
			psBorrarProducto.setInt(1, p.getIdProducto());
			
			psBorrarProducto.executeUpdate();
			
			productos.removeElementAt(index);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public void addProducto(Productos producto) {
		
		String nombre=producto.getNombre();
		double precio=producto.getPrecio();
		int idDistribuidor=producto.getIdDistribuidor();
		try {
			psInsertarProducto.setString(1, nombre);
			psInsertarProducto.setDouble(2, precio);
			psInsertarProducto.setInt(3, idDistribuidor);
			
			psInsertarProducto.executeUpdate();
			//pido el id generado por el auto increment
			ResultSet rs=psInsertarProducto.getGeneratedKeys();
			
			rs.next();
			//guardo el id generado por el auto increment
			int idProducto=rs.getInt(1);
			//le asigno el nuevo id
			producto.setIdProducto(idProducto);
			//lo agrego al listado
			productos.add(producto);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public void actualizarProducto(Productos p, String nombre, double precio) {
		
		
		
		int idProducto=p.getIdProducto();
		try {
			
			psActualizarProducto.setString(1, nombre);
			psActualizarProducto.setDouble(2, precio);
			psActualizarProducto.setInt(3, idProducto);
		
		
			psActualizarProducto.executeUpdate();
			//lo pongo despues para que si no se actualizo en la bd, no se actualice el objeto
			p.setNombre(nombre);
			p.setPrecio(precio);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		
	}
	
	

}

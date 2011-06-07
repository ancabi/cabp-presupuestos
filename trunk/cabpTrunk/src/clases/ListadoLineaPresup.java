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
public class ListadoLineaPresup {
	
	private Vector<LineaPresupuesto> lineaPresupuesto=new Vector<LineaPresupuesto>();
	private Connection dbConnect=Conectar.getConnection();
	private int idPresupuesto;
	private PreparedStatement psLineaPresup;
	private PreparedStatement psAddLineaPresup;
	
	/**
	 * @param idPresupuesto
	 */
	public ListadoLineaPresup(int idPresupuesto) {
		
		this();
		
		this.idPresupuesto = idPresupuesto;
		
	}
	
	public ListadoLineaPresup() {

		try {
			psLineaPresup=dbConnect.prepareStatement("SELECT * FROM lineaPresupuesto WHERE idPresupuesto=?");
			
			psAddLineaPresup=dbConnect.prepareStatement("INSERT INTO lineaPresupuesto(idPresupuesto, idProducto, cantidad, nomProducto, precio) VALUES" +
					"(?,?,?,?,?)");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	
	public void cargarLineaPresup(){
		
		try {
			psLineaPresup.setInt(1, idPresupuesto);
			
			ResultSet rs=psLineaPresup.executeQuery();
			
			while(rs.next()){
				
				String nombreProd=rs.getString("nomProducto");
				int cantidad=rs.getInt("cantidad");
				
				//Productos producto=new Productos();
				
				//lineaPresupuesto.add(new LineaPresupuesto(producto, cantidad));
				
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	/**
	 * @param idPresupuesto the idPresupuesto to set
	 */
	public void setIdPresupuesto(int idPresupuesto) {
		this.idPresupuesto = idPresupuesto;
	}

	public void addLinea(LineaPresupuesto linea) {
		
		
		
		try {
			psAddLineaPresup.setInt(1, linea.getIdPresupuesto());
			psAddLineaPresup.setInt(2, linea.getIdProducto());
			psAddLineaPresup.setInt(3, linea.getCantidad());
			psAddLineaPresup.setString(4, linea.getNombre());
			psAddLineaPresup.setDouble(5, linea.getPrecio());

			psAddLineaPresup.executeUpdate();
			
			lineaPresupuesto.add(linea);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	
	
	

}

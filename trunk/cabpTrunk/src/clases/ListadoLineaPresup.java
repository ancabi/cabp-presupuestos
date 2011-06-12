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
	private PreparedStatement psBorrarLineas;
	
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
			
			psBorrarLineas=dbConnect.prepareStatement("DELETE FROM lineaPresupuesto WHERE idPresupuesto=?");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	
	public void cargarLineaPresup(){
		
		ResultSet rs;
		
		try {
			//le paso el id del presupuesto
			psLineaPresup.setInt(1, idPresupuesto);
			
			rs=psLineaPresup.executeQuery();
			
			while(rs.next()){
				
				int idProducto=rs.getInt("idProducto");
				String nomProducto=rs.getString("nomProducto");
				double precio=rs.getDouble("precio");
				int cantidad=rs.getInt("cantidad");
				//le paso los datos para que cree la linea
				lineaPresupuesto.add(new LineaPresupuesto(idPresupuesto, idProducto,nomProducto, precio, cantidad));
				
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

	public String getNombre() {
		return lineaPresupuesto.get(1).getNombre();
	}

	public int getSize() {
		
		return lineaPresupuesto.size();
	}

	public LineaPresupuesto get(int index) {
		
		return lineaPresupuesto.get(index);
		
	}

	public void removeAllElements() {
		
		try {
			psBorrarLineas.setInt(1, idPresupuesto);
			
			psBorrarLineas.executeUpdate();
			
			lineaPresupuesto.removeAllElements();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		
		
		
		
	}
	
	
	
	

}

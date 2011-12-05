package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;

import conexion.Conectar;

public class ListadoLineaFactura {
	
	private Vector<LineaFactura> lineaFactura=new Vector<LineaFactura>();
	private Connection dbConnect=Conectar.getConnection();
	private int idFactura;
	private PreparedStatement psLineaFactura;
	private PreparedStatement psAddLineaFactura;
	private PreparedStatement psBorrarLineas;
	
	/**
	 * @param idFactura
	 */
	public ListadoLineaFactura(int idFactura) {
		
		this();
		
		this.idFactura = idFactura;
		
	}
	
	public ListadoLineaFactura() {

		try {
			psLineaFactura=dbConnect.prepareStatement("SELECT * FROM lineaFactura WHERE idFactura=?");
			
			psAddLineaFactura=dbConnect.prepareStatement("INSERT INTO lineaFactura(idFactura, idProducto, cantidad, nomProducto, precio) VALUES" +
					"(?,?,?,?,?)");
			
			psBorrarLineas=dbConnect.prepareStatement("DELETE FROM lineaFactura WHERE idFactura=?");
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	
	public void cargarLineaFactura(){
		
		ResultSet rs;
		
		try {
			//le paso el id del Factura
			psLineaFactura.setInt(1, idFactura);
			
			rs=psLineaFactura.executeQuery();
			
			while(rs.next()){
				
				int idProducto=rs.getInt("idProducto");
				String nomProducto=rs.getString("nomProducto");
				double precio=rs.getDouble("precio");
				int cantidad=rs.getInt("cantidad");
				//le paso los datos para que cree la linea
				lineaFactura.add(new LineaFactura(idFactura, idProducto,nomProducto, precio, cantidad));
				
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	/**
	 * @param idFactura the idFactura to set
	 */
	public void setIdFactura(int idFactura) {
		this.idFactura = idFactura;
	}

	public void addLinea(LineaFactura linea) {
		
		
		
		try {
			psAddLineaFactura.setInt(1, linea.getIdFactura());
			psAddLineaFactura.setInt(2, linea.getIdProducto());
			psAddLineaFactura.setInt(3, linea.getCantidad());
			psAddLineaFactura.setString(4, linea.getNombre());
			psAddLineaFactura.setDouble(5, linea.getPrecio());

			psAddLineaFactura.executeUpdate();
			
			lineaFactura.add(linea);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public String getNombre() {
		return lineaFactura.get(1).getNombre();
	}

	public int getSize() {
		
		return lineaFactura.size();
	}

	public LineaFactura get(int index) {
		
		return lineaFactura.get(index);
		
	}

	public void removeAllElements() {
		
		try {
			psBorrarLineas.setInt(1, idFactura);
			
			psBorrarLineas.executeUpdate();
			
			lineaFactura.removeAllElements();
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

}

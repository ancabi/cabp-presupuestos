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
public class ListadoPresupuestos {
	
	private Vector<Presupuestos> presupuestos=new Vector<Presupuestos>();
	private Connection dbConnect=Conectar.getConnection();
	private int idCliente;
	private PreparedStatement psPresupuestos=null;
	private ListadoLineaPresup listadoLineaPresup=null;
	
	public ListadoPresupuestos(int idCliente){
		
		this.idCliente=idCliente;
		listadoLineaPresup=new ListadoLineaPresup();
		
		try {
			psPresupuestos=dbConnect.prepareStatement("SELECT * FROM presupuestos WHERE idCliente=?");
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	public void cargarPresupuestos(){
		
		try {
			psPresupuestos.setInt(1, idCliente);
			
			ResultSet rs=psPresupuestos.executeQuery();
			
			while(rs.next()){
				
				int idPresupuesto=rs.getInt("idPresupuesto");
				int ganancia=rs.getInt("ganancia");
				int restaurante=rs.getInt("restaurante");
				int pasaje=rs.getInt("pasaje");
				int combustible=rs.getInt("combustible");
				int otros=rs.getInt("otros");
				int hotel=rs.getInt("hotel");
				int kilometros=rs.getInt("kilometros");
				boolean iva=rs.getBoolean("iva");
				int porcentaje=rs.getInt("porcentaje");
				double total=rs.getDouble("total");
				int transporte=rs.getInt("transporte");
				String texto=rs.getString("texto");
				
				
				//presupuestos.add(new Presupuestos(idPresupuesto, ganancia, restaurante, pasaje, combustible, otros, hotel, kilometros, iva, 
				//		porcentaje, total, transporte, texto));
				
				listadoLineaPresup.setIdPresupuesto(idPresupuesto);
				
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		
		
	}

}

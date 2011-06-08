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
	private PreparedStatement psLineasPresupuesto;
	
	public ListadoPresupuestos(int idCliente){
		
		this();
		
		this.idCliente=idCliente;
		
		
	}
	
	public ListadoPresupuestos() {
		
		listadoLineaPresup=new ListadoLineaPresup();
		
		try {
			psPresupuestos=dbConnect.prepareStatement("SELECT * FROM presupuestos WHERE idCliente=?");
			
			psLineasPresupuesto=dbConnect.prepareStatement("SELECT * FROM lineaPresupuesto WHERE idPresupuesto=?");
			
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
				boolean isGanancia=rs.getBoolean("isGanancia");
				int porcentaje=rs.getInt("porcentaje");
				double totalConIva=rs.getDouble("totalConIva");
				int transporte=rs.getInt("transporte");
				String texto=rs.getString("texto");
				int totViajes=rs.getInt("totViajes");
				double precioGasolina=rs.getDouble("precioGasolina");
				double totalSinIva=rs.getDouble("totalSinIva");
				
				
				presupuestos.add(new Presupuestos(idPresupuesto, ganancia, restaurante, pasaje, combustible, otros, hotel, kilometros, totViajes, precioGasolina, 
						isGanancia, porcentaje, totalConIva, totalSinIva, transporte, texto, idCliente));
				
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	
	

}

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
	private PreparedStatement psLineasPresupuesto;
	
	public ListadoPresupuestos(int idCliente){
		
		this();
		
		this.idCliente=idCliente;
		
		
	}
	
	public ListadoPresupuestos() {
		
		try {
			psPresupuestos=dbConnect.prepareStatement("SELECT * FROM presupuestos WHERE idCliente=?");
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public void cargarPresupuestos(){
		//vacio el vector
		presupuestos.removeAllElements();
		
		try {
			//le paso el cliente que tiene que traerme los presupuestos
			psPresupuestos.setInt(1, idCliente);
			//ejecuto la consulta
			ResultSet rs=psPresupuestos.executeQuery();
			
			while(rs.next()){
				//guardo los datos
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
				int idDistribuidor=rs.getInt("idDistribuidor");
				
				//creo el presupuesto y lo guardo en el vector
				presupuestos.add(new Presupuestos(idPresupuesto, ganancia, restaurante, pasaje, combustible, otros, hotel, kilometros, totViajes, precioGasolina, 
						isGanancia, porcentaje, totalConIva, totalSinIva, transporte, texto, idCliente, idDistribuidor));
				//traigo el ultimo indice introducido
				int index=presupuestos.size()-1;
				//cargo las lineas
				presupuestos.get(index).cargarLineas();
				
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	
	public void cargarLineas(int index){
		
		
		presupuestos.get(index).cargarLineas();
		
		
	}
	
	public Presupuestos getPresupuestoProveedor(int index, int idDistribuidor){
		
		if(idDistribuidor==presupuestos.get(index).getIdDistribuidor()){
			
			return presupuestos.get(index);
			
		}
		
		return null;
		
	}
	
	public int getSize(){
		return presupuestos.size();
	}
	
	

}

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
public class ListadoFacturas {
	
	private Vector<Facturas> facturas=new Vector<Facturas>();
	private Connection dbConnect=Conectar.getConnection();
	private int idCliente;
	private PreparedStatement psFacturas=null;
	private PreparedStatement psLineasFactura;
	
	public ListadoFacturas(int idCliente){
		
		this();
		
		this.idCliente=idCliente;
		
		
	}
	
	public ListadoFacturas() {
		
		try {
			psFacturas=dbConnect.prepareStatement("SELECT * FROM facturas WHERE idCliente=?");
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public void cargarFacturas(){
		//vacio el vector
		facturas.removeAllElements();
		
		try {
			//le paso el cliente que tiene que traerme los facturas
			psFacturas.setInt(1, idCliente);
			//ejecuto la consulta
			ResultSet rs=psFacturas.executeQuery();
			
			while(rs.next()){
				//guardo los datos
				int idFactura=rs.getInt("idFactura");
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
				
				//creo la factura y lo guardo en el vector
				facturas.add(new Facturas(idFactura, ganancia, restaurante, pasaje, combustible, otros, hotel, kilometros, totViajes, precioGasolina, 
						isGanancia, porcentaje, totalConIva, totalSinIva, transporte, texto, idCliente, idDistribuidor));
				//traigo el ultimo indice introducido
				int index=facturas.size()-1;
				//cargo las lineas
				facturas.get(index).cargarLineas();
				
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	
	public void cargarLineas(int index){
		
		
		facturas.get(index).cargarLineas();
		
		
	}
	
	public Facturas getFacturaProveedor(int index, int idDistribuidor){
		
		if(idDistribuidor==facturas.get(index).getIdDistribuidor()){
			
			return facturas.get(index);
			
		}
		
		return null;
		
	}
	
	public int getSize(){
		return facturas.size();
	}

	public Facturas getFactura(int idFactura) {
		
		for(int x=0; x < facturas.size(); x++){
			
			if(facturas.get(x).getIdFactura()==idFactura){
				return facturas.get(x);
			}
			
		}
		
		return null;
		
	}

}

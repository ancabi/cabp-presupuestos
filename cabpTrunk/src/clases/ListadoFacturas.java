/**
 * 
 */
package clases;

import java.sql.Connection;
import java.sql.Date;
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
	private PreparedStatement psActualizarFactura;
	
	public ListadoFacturas(int idCliente){
		
		this();
		
		this.idCliente=idCliente;
		
		
	}
	
	public ListadoFacturas() {
		
		try {
			psFacturas=dbConnect.prepareStatement("SELECT * FROM facturas WHERE idCliente=?");
			
			psActualizarFactura=dbConnect.prepareStatement("UPDATE facturas SET ganancia=?, combustible=?, pasaje=?, restaurante=?, otros=?, hotel=?, kilometros=?, totViajes=?," +
					"precioGasolina=?, isGanancia=?, isCanarias=?, porcentaje=?, totalConIva=?, totalSinIva=?, transporte=?, textoLinea=?, " +
					"textoFormaPago=?, textoExplicativo=?, fecha=?, isTotalManual=?, totalManual=? WHERE idFactura=?");
			
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
				boolean isCanarias=rs.getBoolean("isCanarias");
				int porcentaje=rs.getInt("porcentaje");
				double totalConIva=rs.getDouble("totalConIva");
				int transporte=rs.getInt("transporte");
				String textoLinea=rs.getString("textoLinea");
				String textoFormaPago=rs.getString("textoFormaPago");
				String textoExplicativo=rs.getString("textoExplicativo");
				Date fecha=rs.getDate("fecha");
				int totViajes=rs.getInt("totViajes");
				double precioGasolina=rs.getDouble("precioGasolina");
				double totalSinIva=rs.getDouble("totalSinIva");
				boolean isTotalManual= rs.getBoolean("isTotalManual");
				int totalManual = rs.getInt("totalManual");
				int idDistribuidor=rs.getInt("idDistribuidor");
				
				//creo la factura y lo guardo en el vector
				facturas.add(new Facturas(idFactura, ganancia, restaurante, pasaje, combustible, otros, hotel, kilometros, totViajes, precioGasolina, 
						isGanancia, isCanarias, porcentaje, totalConIva, totalSinIva, transporte, textoLinea, textoFormaPago, textoExplicativo, fecha,
						isTotalManual, totalManual, idCliente, idDistribuidor));
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
	
	public Facturas getFacturaProveedor(int index){

			
		return facturas.get(index);

		
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

	public void actualizarFactura(int id) {
		
		Facturas f=getFactura(id);
		
		try {
			psActualizarFactura.setInt(1, f.getGanancia());
			psActualizarFactura.setInt(2, f.getCombustible());
			psActualizarFactura.setInt(3, f.getPasaje());
			psActualizarFactura.setInt(4, f.getRestaurante());
			psActualizarFactura.setInt(5, f.getOtros());
			psActualizarFactura.setInt(6, f.getHotel());
			psActualizarFactura.setInt(7, f.getKilometros());
			psActualizarFactura.setInt(8, f.getnViajes());
			psActualizarFactura.setDouble(9, f.getPrecioGasolina());
			psActualizarFactura.setBoolean(10, f.isGanancia());
			psActualizarFactura.setBoolean(11, f.isCanarias());
			psActualizarFactura.setInt(12, f.getPorcentaje());
			psActualizarFactura.setDouble(13, f.getTotalConIva());
			psActualizarFactura.setDouble(14, f.getTotalSinIva());
			psActualizarFactura.setInt(15, f.getTransporte());
			psActualizarFactura.setString(16, f.getTextoLinea());
			psActualizarFactura.setString(17, f.getTextoFormaPago());
			psActualizarFactura.setString(18, f.getTextoExplicativo());
			psActualizarFactura.setDate(19, f.getFecha());
			psActualizarFactura.setBoolean(20, f.isTotalManual());
			psActualizarFactura.setInt(21, f.getTotalManual());
			psActualizarFactura.setInt(22, id);
		
			psActualizarFactura.executeUpdate();
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

}

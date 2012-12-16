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
	private PreparedStatement psTodasFacturas;
	
	public ListadoFacturas(int idCliente){
		
		this();
		
		this.idCliente=idCliente;
		
		
	}
	
	public ListadoFacturas() {
		
		try {
			psFacturas=dbConnect.prepareStatement("SELECT * FROM facturas WHERE idCliente=?");
			
			psTodasFacturas=dbConnect.prepareStatement("SELECT * FROM facturas");
			
			psActualizarFactura=dbConnect.prepareStatement("UPDATE facturas SET ganancia=?, combustible=?, " +
					"pasaje=?, restaurante=?, otros=?, hotel=?, kilometros=?, totViajes=?, precioGasolina=?, " +
					"isGanancia=?, isCanarias=?, porcentaje=?, totalConIva=?, totalSinIva=?, iva=?, transporte=?, " +
					"textoLinea=?, textoFormaPago=?, textoExplicativo=?, fecha=?, isTotalManual=?, valorA=?, " +
					"valorB=?, valorC=?, valorAux=?, stepper=?, totalManual=? WHERE idFactura=?");
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage()+"ListadoFacturas1");
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
				double iva=rs.getDouble("iva");
				boolean isTotalManual= rs.getBoolean("isTotalManual");
				int totalManual = rs.getInt("totalManual");
				double valorA=rs.getDouble("valorA");
				double valorB=rs.getDouble("valorB");
				double valorC=rs.getDouble("valorC");
				double valorAux=rs.getDouble("valorAux");
				boolean stepper=rs.getBoolean("stepper");
				int idDistribuidor=rs.getInt("idDistribuidor");
				
				//creo la factura y lo guardo en el vector
				facturas.add(new Facturas(idFactura, ganancia, restaurante, pasaje, combustible, otros, hotel, kilometros, totViajes, precioGasolina, 
						isGanancia, isCanarias, porcentaje, totalConIva, totalSinIva, iva, transporte, textoLinea, textoFormaPago, textoExplicativo, fecha,
						isTotalManual,  totalManual, idCliente, valorA, valorB, valorC, valorAux, stepper, idDistribuidor));
				//traigo el ultimo indice introducido
				int index=facturas.size()-1;
				//cargo las lineas
				facturas.get(index).cargarLineas();
				
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage()+"ListadoFacturas2");
		}
		
	}
	
	public void cargarTodasFacturas(){
		//vacio el vector
		facturas.removeAllElements();
		
		try {
			//ejecuto la consulta
			ResultSet rs=psTodasFacturas.executeQuery();
			
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
				double iva=rs.getDouble("iva");
				boolean isTotalManual= rs.getBoolean("isTotalManual");
				int totalManual = rs.getInt("totalManual");
				double valorA=rs.getDouble("valorA");
				double valorB=rs.getDouble("valorB");
				double valorC=rs.getDouble("valorC");
				double valorAux=rs.getDouble("valorAux");
				boolean stepper=rs.getBoolean("stepper");
				int idDistribuidor=rs.getInt("idDistribuidor");
				idCliente=rs.getInt("idCliente");
				
				//creo la factura y lo guardo en el vector
				facturas.add(new Facturas(idFactura, ganancia, restaurante, pasaje, combustible, otros, hotel, kilometros, totViajes, precioGasolina, 
						isGanancia, isCanarias, porcentaje, totalConIva, totalSinIva, iva, transporte, textoLinea, textoFormaPago, textoExplicativo, fecha,
						isTotalManual,  totalManual, idCliente, valorA, valorB, valorC, valorAux, stepper, idDistribuidor));
				//traigo el ultimo indice introducido
				int index=facturas.size()-1;
				//cargo las lineas
				facturas.get(index).cargarLineas();
				
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage()+"ListadoFacturas2");
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
	
	public Vector<Facturas> getFacturas(){
		return facturas;
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
			psActualizarFactura.setDouble(15, f.getIVA());
			psActualizarFactura.setInt(16, f.getTransporte());
			psActualizarFactura.setString(17, f.getTextoLinea());
			psActualizarFactura.setString(18, f.getTextoFormaPago());
			psActualizarFactura.setString(19, f.getTextoExplicativo());
			psActualizarFactura.setDate(20, f.getFecha());
			psActualizarFactura.setBoolean(21, f.isTotalManual());
			psActualizarFactura.setDouble(22, f.getValorA());
			psActualizarFactura.setDouble(23, f.getValorB());
			psActualizarFactura.setDouble(24, f.getValorC());
			psActualizarFactura.setDouble(25, f.getValorAux());
			psActualizarFactura.setBoolean(26, f.isStepper());
			psActualizarFactura.setInt(27, f.getTotalManual());
			psActualizarFactura.setInt(28, id);
		
			psActualizarFactura.executeUpdate();
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage()+"ListadoFacturas3");
		}
		
	}

}

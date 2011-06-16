/**
 * 
 */
package clases;

import gui.PanelPresupuesto;

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
	private PreparedStatement psBorrarPresupuesto;
	private PreparedStatement psBorrarLineas;
	private PreparedStatement psActualizarPresupuesto;
	
	public ListadoPresupuestos(int idCliente){
		
		this();
		
		this.idCliente=idCliente;
		
		
	}
	
	public ListadoPresupuestos() {
		
		try {
			psPresupuestos=dbConnect.prepareStatement("SELECT * FROM presupuestos WHERE idCliente=?");
			
			psBorrarPresupuesto=dbConnect.prepareStatement("DELETE FROM presupuestos WHERE idPresupuesto=?");
			
			psBorrarLineas=dbConnect.prepareStatement("DELETE FROM lineaPresupuesto WHERE idPresupuesto=?");
			
			psActualizarPresupuesto=dbConnect.prepareStatement("UPDATE presupuestos SET ganancia=?, combustible=?, pasaje=?, restaurante=?, otros=?, hotel=?, kilometros=?, totViajes=?," +
					"precioGasolina=?, isGanancia=?, porcentaje=?, totalConIva=?, totalSinIva=?, transporte=?, texto=?");
			
			
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
	
	public Presupuestos getPresupuestoProveedor(int index){
		
		
			
		return presupuestos.get(index);

		
	}
	
	public int getSize(){
		return presupuestos.size();
	}

	public Presupuestos getPresupuesto(int idPresupuesto) {
		
		for(int x=0; x < presupuestos.size(); x++){
			
			if(presupuestos.get(x).getIdPresupuesto()==idPresupuesto){
				return presupuestos.get(x);
			}
			
		}
		
		return null;
		
	}

	public void convertir(Presupuestos p) {
		
		ListadoLineaPresup lineas=p.getListadoLineaPresup();
		
		int ganancia=p.getGanancia();
		int restaurante=p.getRestaurante();
		int pasaje=p.getPasaje();
		int combustible=p.getCombustible();
		int otros=p.getOtros();
		int hotel=p.getHotel();
		int kilometros=p.getKilometros();
		int nViajes=p.getnViajes();
		double precioGasolina=p.getPrecioGasolina();
		boolean isGanancia=p.isGanancia();
		int porcentaje=p.getPorcentaje();
		double totalConIva=p.getTotalConIva();
		double totalSinIva=p.getTotalSinIva();
		int transporte=p.getTransporte();
		String texto=p.getTexto();
		int idDistribuidor=p.getIdDistribuidor();
		
		
		Facturas f=new Facturas(ganancia, restaurante, pasaje, combustible, otros, hotel, kilometros, nViajes, precioGasolina, 
				isGanancia, porcentaje, totalConIva, totalSinIva, transporte, texto, idCliente, idDistribuidor);
		
		f.addBD();
		
		for(int x=0; x< lineas.getSize(); x++){
			
			LineaPresupuesto linea=lineas.get(x);
			
			int idProducto=linea.getIdProducto();
			String nomProducto=linea.getNombre();
			double precio=linea.getPrecio();
			int cantidad=linea.getCantidad();
			
			f.addLineaFactura(idProducto, nomProducto, precio, cantidad);
			
		}
		
	}

	public void removeElement(Presupuestos p) {
		
		int idPresupuesto=p.getIdPresupuesto();
		
		try {
			psBorrarPresupuesto.setInt(1, idPresupuesto);
			
			psBorrarPresupuesto.executeUpdate();
			
			psBorrarLineas.setInt(1, idPresupuesto);
			
			psBorrarLineas.executeUpdate();
			
			presupuestos.removeElement(p);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		
		
	}

	public void actualizarPresupuesto(int id) {
		
		Presupuestos p=getPresupuesto(id);
		
		try {
			psActualizarPresupuesto.setInt(1, p.getGanancia());
			psActualizarPresupuesto.setInt(2, p.getCombustible());
			psActualizarPresupuesto.setInt(3, p.getPasaje());
			psActualizarPresupuesto.setInt(4, p.getRestaurante());
			psActualizarPresupuesto.setInt(5, p.getOtros());
			psActualizarPresupuesto.setInt(6, p.getHotel());
			psActualizarPresupuesto.setInt(7, p.getKilometros());
			psActualizarPresupuesto.setInt(8, p.getnViajes());
			psActualizarPresupuesto.setDouble(9, p.getPrecioGasolina());
			psActualizarPresupuesto.setBoolean(10, p.isGanancia());
			psActualizarPresupuesto.setInt(11, p.getPorcentaje());
			psActualizarPresupuesto.setDouble(12, p.getTotalConIva());
			psActualizarPresupuesto.setDouble(13, p.getTotalSinIva());
			psActualizarPresupuesto.setInt(14, p.getTransporte());
			psActualizarPresupuesto.setString(15, p.getTexto());
		
		
			psActualizarPresupuesto.executeUpdate();
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		
	}
	
	

}

/**
 * 
 */
package clases;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import conexion.Conectar;

/**
 * @author ancabi
 *
 */
public class Facturas {

private Connection dbConnect=Conectar.getConnection();
	
	private int idFactura;
	private int ganancia;
	private int restaurante;
	private int pasaje;
	private int combustible;
	private int otros;
	private int hotel;
	private int kilometros;
	private int nViajes;
	private double precioGasolina;
	private boolean isGanancia;
	private boolean isCanarias;
	private int porcentaje;
	private double totalConIva;
	private double totalSinIva;
	private int transporte;
	private String textoLinea;
	private String textoFormaPago;
	private String textoExplicativo;
	private ListadoLineaFactura listadoLineaFactura=null;
	private Date fecha;
	private int idCliente;
	private int idDistribuidor;
	
	/**
	 * @param idPresupuesto
	 * @param ganancia
	 * @param restaurante
	 * @param pasaje
	 * @param combustible
	 * @param otros
	 * @param hotel
	 * @param kilometros
	 * @param nViajes
	 * @param precioGasolina
	 * @param isGanancia
	 * @param isCanarias
	 * @param porcentaje
	 * @param totalConIva
	 * @param totalSinIva
	 * @param transporte
	 * @param textoLinea
	 * @param textoFormaPago
	 * @param textoExplicativo
	 * @param fecha
	 * @param idCliente
	 * @param idDistribuidor
	 */
	public Facturas(int idFactura, int ganancia, int restaurante,
			int pasaje, int combustible, int otros, int hotel, int kilometros,
			int nViajes, double precioGasolina, boolean isGanancia, boolean isCanarias,
			int porcentaje, double totalConIva, double totalSinIva,
			int transporte, String textoLinea, String textoFormaPago, String textoExplicativo, Date fecha,
			int idCliente, int idDistribuidor) {
		
		this(ganancia, restaurante, pasaje, combustible, otros, hotel, kilometros, nViajes, precioGasolina, isGanancia, isCanarias,
				porcentaje, totalConIva, totalSinIva, transporte, textoLinea, textoFormaPago, textoExplicativo
				, fecha, idCliente, idDistribuidor);
		
		this.idFactura = idFactura;
		listadoLineaFactura.setIdFactura(idFactura);
		
	}
	
	/**
	 * @param idPresupuesto
	 * @param ganancia
	 * @param restaurante
	 * @param pasaje
	 * @param combustible
	 * @param otros
	 * @param hotel
	 * @param kilometros
	 * @param nViajes
	 * @param precioGasolina
	 * @param isGanancia
	 * @param isCanarias
	 * @param porcentaje
	 * @param totalConIva
	 * @param totalSinIva
	 * @param transporte
	 * @param textoLinea
	 * @param textoFormaPago
	 * @param textoExplicativo
	 * @param fecha
	 * @param idCliente
	 * @param idDistribuidor
	 */
	public Facturas(int ganancia, int restaurante,
			int pasaje, int combustible, int otros, int hotel, int kilometros,
			int nViajes, double precioGasolina, boolean isGanancia, boolean isCanarias,
			int porcentaje, double totalConIva, double totalSinIva,
			int transporte, String textoLinea, String textoFormaPago, String textoExplicativo,
			Date fecha, int idCliente, int idDistribuidor) {
		
		this.ganancia = ganancia;
		this.restaurante = restaurante;
		this.pasaje = pasaje;
		this.combustible = combustible;
		this.otros = otros;
		this.hotel = hotel;
		this.kilometros = kilometros;
		this.nViajes = nViajes;
		this.precioGasolina = precioGasolina;
		this.isGanancia = isGanancia;
		this.isCanarias = isCanarias;
		this.porcentaje = porcentaje;
		this.totalConIva = totalConIva;
		this.totalSinIva = totalSinIva;
		this.transporte = transporte;
		this.textoLinea = textoLinea;
		this.textoFormaPago = textoFormaPago;
		this.textoExplicativo = textoExplicativo;
		this.fecha = fecha;
		this.idCliente = idCliente;
		this.idDistribuidor=idDistribuidor;
		
		listadoLineaFactura = new ListadoLineaFactura();
	}
	
	public void addBD(){
		
		ResultSet rs;
		
		try {
			PreparedStatement ps=dbConnect.prepareStatement("INSERT INTO facturas(ganancia, restaurante, pasaje, combustible, otros, " +
					"hotel, kilometros, isGanancia, isCanarias, porcentaje, totalConIva, transporte, textoLinea, textoFormaPago, textoExplicativo" +
					", idCliente, totViajes, precioGasolina, " +
					"totalSinIva, fecha, idDistribuidor) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			ps.setInt(1, ganancia);
			ps.setInt(2, restaurante);
			ps.setInt(3, pasaje);
			ps.setInt(4, combustible);
			ps.setInt(5, otros);
			ps.setInt(6, hotel);
			ps.setInt(7, kilometros);
			ps.setBoolean(8, isGanancia);
			ps.setBoolean(9, isCanarias);
			ps.setInt(10, porcentaje);
			ps.setDouble(11, totalConIva);
			ps.setInt(12, transporte);
			ps.setString(13, textoLinea);
			ps.setString(14, textoFormaPago);
			ps.setString(15, textoExplicativo);
			ps.setInt(16, idCliente);
			ps.setInt(17, nViajes);
			ps.setDouble(18, precioGasolina);
			ps.setDouble(19, totalSinIva);
			ps.setDate(20, fecha);
			ps.setInt(21, idDistribuidor);
			
			ps.executeUpdate();
			
			//pido el id generado
			rs=ps.getGeneratedKeys();
			//paso 1 ya que siempre deolvera solo 1
			rs.next();
			//guardo el id
			idFactura=rs.getInt(1);
			//guardo el idPresupuesto en el listado de lineas
			listadoLineaFactura.setIdFactura(idFactura);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public void addLineaFactura(int idProducto, String nomProducto, double precio, int cantidad) {
		
		listadoLineaFactura.addLinea(new LineaFactura(idFactura, idProducto, nomProducto, precio, cantidad));
		
	}

	public int getIdDistribuidor() {
		return idDistribuidor;
	}

	/**
	 * @return the idPresupuesto
	 */
	public int getIdFactura() {
		return idFactura;
	}
	
	
	
	/**
	 * @return the dbConnect
	 */
	public Connection getDbConnect() {
		return dbConnect;
	}

	/**
	 * @return the ganancia
	 */
	public int getGanancia() {
		return ganancia;
	}

	/**
	 * @return the restaurante
	 */
	public int getRestaurante() {
		return restaurante;
	}

	/**
	 * @return the pasaje
	 */
	public int getPasaje() {
		return pasaje;
	}

	/**
	 * @return the combustible
	 */
	public int getCombustible() {
		return combustible;
	}

	/**
	 * @return the otros
	 */
	public int getOtros() {
		return otros;
	}

	/**
	 * @return the hotel
	 */
	public int getHotel() {
		return hotel;
	}

	/**
	 * @return the kilometros
	 */
	public int getKilometros() {
		return kilometros;
	}

	/**
	 * @return the nViajes
	 */
	public int getnViajes() {
		return nViajes;
	}

	/**
	 * @return the precioGasolina
	 */
	public double getPrecioGasolina() {
		return precioGasolina;
	}

	/**
	 * @return the isGanancia
	 */
	public boolean isGanancia() {
		return isGanancia;
	}

	/**
	 * @return the porcentaje
	 */
	public int getPorcentaje() {
		return porcentaje;
	}

	/**
	 * @return the totalConIva
	 */
	public double getTotalConIva() {
		return totalConIva;
	}

	/**
	 * @return the totalSinIva
	 */
	public double getTotalSinIva() {
		return totalSinIva;
	}

	/**
	 * @return the transporte
	 */
	public int getTransporte() {
		return transporte;
	}

	/**
	 * @return the texto
	 */
	public String getTextoLinea() {
		return textoLinea;
	}

	/**
	 * @return the listadoLineaPresup
	 */
	public ListadoLineaFactura getListadoLineaFactura() {
		return listadoLineaFactura;
	}

	/**
	 * @return the idCliente
	 */
	public int getIdCliente() {
		return idCliente;
	}

	public void cargarLineas(){
		
		listadoLineaFactura.cargarLineaFactura();
		
	}

	/**
	 * @return the isCanarias
	 */
	public boolean isCanarias() {
		return isCanarias;
	}

	/**
	 * @return the textoFormaPago
	 */
	public String getTextoFormaPago() {
		return textoFormaPago;
	}

	/**
	 * @return the textoExplicativo
	 */
	public String getTextoExplicativo() {
		return textoExplicativo;
	}

	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}
	
	

	
	
}

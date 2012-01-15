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
	private boolean isTotalManual;
	private int totalManual;
	private int idCliente;
	private double valorA;
	private double valorB;
	private double valorC;
	private double valorAux;
	private boolean stepper;
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
	 * @param isTotalManual
	 * @param totalManual
	 * @param idCliente
	 * @param valorA
	 * @param valorB
	 * @param valorC
	 * @param valorAux
	 * @param stepper
	 * @param idDistribuidor
	 */
	public Facturas(int idFactura, int ganancia, int restaurante,
			int pasaje, int combustible, int otros, int hotel, int kilometros,
			int nViajes, double precioGasolina, boolean isGanancia, boolean isCanarias,
			int porcentaje, double totalConIva, double totalSinIva,
			int transporte, String textoLinea, String textoFormaPago, String textoExplicativo, Date fecha, boolean isTotalManual, int totalManual,
			int idCliente, double valorA, double valorB, double valorC, double valorAux, boolean stepper, int idDistribuidor) {
		
		this(ganancia, restaurante, pasaje, combustible, otros, hotel, kilometros, nViajes, precioGasolina, isGanancia, isCanarias,
				porcentaje, totalConIva, totalSinIva, transporte, textoLinea, textoFormaPago, textoExplicativo
				, fecha, isTotalManual, totalManual, idCliente, valorA, valorB, valorC, valorAux, stepper, idDistribuidor);
		
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
	 * @param isTotalManual
	 * @param totalManual
	 * @param idCliente
	 * @param valorA
	 * @param valorB
	 * @param valorC
	 * @param valorAux
	 * @param stepper
	 * @param idDistribuidor
	 */
	public Facturas(int ganancia, int restaurante,
			int pasaje, int combustible, int otros, int hotel, int kilometros,
			int nViajes, double precioGasolina, boolean isGanancia, boolean isCanarias,
			int porcentaje, double totalConIva, double totalSinIva,
			int transporte, String textoLinea, String textoFormaPago, String textoExplicativo,
			Date fecha, boolean isTotalManual, int totalManual, int idCliente, double valorA, 
			double valorB, double valorC, double valorAux, boolean stepper, int idDistribuidor) {
		
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
		this.isTotalManual = isTotalManual;
		this.totalManual = totalManual;
		this.idCliente = idCliente;
		this.valorA = valorA;
		this.valorB = valorB;
		this.valorC = valorC;
		this.valorAux = valorAux;
		this.stepper = stepper;
		this.idDistribuidor=idDistribuidor;
		
		listadoLineaFactura = new ListadoLineaFactura();
	}
	
	public void addBD(){
		
		ResultSet rs;
		
		try {
			PreparedStatement ps=dbConnect.prepareStatement("INSERT INTO facturas(ganancia, restaurante, pasaje, combustible, otros, " +
					"hotel, kilometros, isGanancia, isCanarias, porcentaje, totalConIva, transporte, textoLinea, textoFormaPago, textoExplicativo" +
					", idCliente, totViajes, precioGasolina, " +
					"totalSinIva, fecha, isTotalManual, totalManual, valorA, valorB, valorC, valorAux, stepper, idDistribuidor) " +
					"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
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
			ps.setBoolean(21, isTotalManual);
			ps.setInt(22, totalManual);
			ps.setDouble(23, valorA);
			ps.setDouble(24, valorB);
			ps.setDouble(25, valorC);
			ps.setDouble(26, valorAux);
			ps.setBoolean(27, stepper);
			ps.setInt(28, idDistribuidor);
			
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

	/**
	 * @return the isTotalManual
	 */
	public boolean isTotalManual() {
		return isTotalManual;
	}

	/**
	 * @return the totalManual
	 */
	public int getTotalManual() {
		return totalManual;
	}

	/**
	 * @param dbConnect the dbConnect to set
	 */
	public void setDbConnect(Connection dbConnect) {
		this.dbConnect = dbConnect;
	}

	/**
	 * @param idFactura the idFactura to set
	 */
	public void setIdFactura(int idFactura) {
		this.idFactura = idFactura;
	}

	/**
	 * @param ganancia the ganancia to set
	 */
	public void setGanancia(int ganancia) {
		this.ganancia = ganancia;
	}

	/**
	 * @param restaurante the restaurante to set
	 */
	public void setRestaurante(int restaurante) {
		this.restaurante = restaurante;
	}

	/**
	 * @param pasaje the pasaje to set
	 */
	public void setPasaje(int pasaje) {
		this.pasaje = pasaje;
	}

	/**
	 * @param combustible the combustible to set
	 */
	public void setCombustible(int combustible) {
		this.combustible = combustible;
	}

	/**
	 * @param otros the otros to set
	 */
	public void setOtros(int otros) {
		this.otros = otros;
	}

	/**
	 * @param hotel the hotel to set
	 */
	public void setHotel(int hotel) {
		this.hotel = hotel;
	}

	/**
	 * @param kilometros the kilometros to set
	 */
	public void setKilometros(int kilometros) {
		this.kilometros = kilometros;
	}

	/**
	 * @param nViajes the nViajes to set
	 */
	public void setnViajes(int nViajes) {
		this.nViajes = nViajes;
	}

	/**
	 * @param precioGasolina the precioGasolina to set
	 */
	public void setPrecioGasolina(double precioGasolina) {
		this.precioGasolina = precioGasolina;
	}

	/**
	 * @param isGanancia the isGanancia to set
	 */
	public void setGanancia(boolean isGanancia) {
		this.isGanancia = isGanancia;
	}

	/**
	 * @param isCanarias the isCanarias to set
	 */
	public void setCanarias(boolean isCanarias) {
		this.isCanarias = isCanarias;
	}

	/**
	 * @param porcentaje the porcentaje to set
	 */
	public void setPorcentaje(int porcentaje) {
		this.porcentaje = porcentaje;
	}

	/**
	 * @param totalConIva the totalConIva to set
	 */
	public void setTotalConIva(double totalConIva) {
		this.totalConIva = totalConIva;
	}

	/**
	 * @param totalSinIva the totalSinIva to set
	 */
	public void setTotalSinIva(double totalSinIva) {
		this.totalSinIva = totalSinIva;
	}

	/**
	 * @param transporte the transporte to set
	 */
	public void setTransporte(int transporte) {
		this.transporte = transporte;
	}

	/**
	 * @param textoLinea the textoLinea to set
	 */
	public void setTextoLinea(String textoLinea) {
		this.textoLinea = textoLinea;
	}

	/**
	 * @param textoFormaPago the textoFormaPago to set
	 */
	public void setTextoFormaPago(String textoFormaPago) {
		this.textoFormaPago = textoFormaPago;
	}

	/**
	 * @param textoExplicativo the textoExplicativo to set
	 */
	public void setTextoExplicativo(String textoExplicativo) {
		this.textoExplicativo = textoExplicativo;
	}

	/**
	 * @param listadoLineaFactura the listadoLineaFactura to set
	 */
	public void setListadoLineaFactura(ListadoLineaFactura listadoLineaFactura) {
		this.listadoLineaFactura = listadoLineaFactura;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @param isTotalManual the isTotalManual to set
	 */
	public void setTotalManual(boolean isTotalManual) {
		this.isTotalManual = isTotalManual;
	}

	/**
	 * @param totalManual the totalManual to set
	 */
	public void setTotalManual(int totalManual) {
		this.totalManual = totalManual;
	}

	/**
	 * @param idCliente the idCliente to set
	 */
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	/**
	 * @param idDistribuidor the idDistribuidor to set
	 */
	public void setIdDistribuidor(int idDistribuidor) {
		this.idDistribuidor = idDistribuidor;
	}

	public void removeAllLineas() {
		
		listadoLineaFactura.removeAllElements();
		
	}

	public void addLineaPresupuesto(int idProducto, String nomProducto,	double precio, int cantidad) {
		
		listadoLineaFactura.addLinea(new LineaFactura(idFactura, idProducto, nomProducto, precio, cantidad));
		
	}

	/**
	 * @return the valorA
	 */
	public double getValorA() {
		return valorA;
	}

	/**
	 * @param valorA the valorA to set
	 */
	public void setValorA(double valorA) {
		this.valorA = valorA;
	}

	/**
	 * @return the valorB
	 */
	public double getValorB() {
		return valorB;
	}

	/**
	 * @param valorB the valorB to set
	 */
	public void setValorB(double valorB) {
		this.valorB = valorB;
	}

	/**
	 * @return the valorC
	 */
	public double getValorC() {
		return valorC;
	}

	/**
	 * @param valorC the valorC to set
	 */
	public void setValorC(double valorC) {
		this.valorC = valorC;
	}

	/**
	 * @return the valorAux
	 */
	public double getValorAux() {
		return valorAux;
	}

	/**
	 * @param valorAux the valorAux to set
	 */
	public void setValorAux(double valorAux) {
		this.valorAux = valorAux;
	}

	/**
	 * @return the stepper
	 */
	public boolean isStepper() {
		return stepper;
	}

	/**
	 * @param stepper the stepper to set
	 */
	public void setStepper(boolean stepper) {
		this.stepper = stepper;
	}
	
	
	
	

	
	
}

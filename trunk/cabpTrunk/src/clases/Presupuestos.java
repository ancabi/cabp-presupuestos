/**
 * 
 */
package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import conexion.Conectar;

/**
 * @author ancabi
 *
 */
public class Presupuestos {
	
	private Connection dbConnect=Conectar.getConnection();
	
	private int idPresupuesto;
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
	private int porcentaje;
	private double totalConIva;
	private double totalSinIva;
	private int transporte;
	private String texto;
	private ListadoLineaPresup listadoLineaPresup=null;
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
	 * @param porcentaje
	 * @param totalConIva
	 * @param totalSinIva
	 * @param transporte
	 * @param texto
	 * @param listadoLineaPresup
	 * @param idCliente
	 * @param idDistribuidor
	 */
	public Presupuestos(int idPresupuesto, int ganancia, int restaurante,
			int pasaje, int combustible, int otros, int hotel, int kilometros,
			int nViajes, double precioGasolina, boolean isGanancia,
			int porcentaje, double totalConIva, double totalSinIva,
			int transporte, String texto, int idCliente, int idDistribuidor) {
		
		this(ganancia, restaurante, pasaje, combustible, otros, hotel, kilometros, nViajes, precioGasolina, isGanancia,
				porcentaje, totalConIva, totalSinIva, transporte, texto, idCliente, idDistribuidor);
		
		this.idPresupuesto = idPresupuesto;
		listadoLineaPresup.setIdPresupuesto(idPresupuesto);
		
	}
	
	/**
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
	 * @param porcentaje
	 * @param totalConIva
	 * @param totalSinIva
	 * @param transporte
	 * @param texto
	 * @param listadoLineaPresup
	 * @param idCliente
	 * @param idDistribuidor
	 */
	public Presupuestos(int ganancia, int restaurante,
			int pasaje, int combustible, int otros, int hotel, int kilometros,
			int nViajes, double precioGasolina, boolean isGanancia,
			int porcentaje, double totalConIva, double totalSinIva,
			int transporte, String texto, int idCliente, int idDistribuidor) {
		
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
		this.porcentaje = porcentaje;
		this.totalConIva = totalConIva;
		this.totalSinIva = totalSinIva;
		this.transporte = transporte;
		this.texto = texto;
		this.idCliente = idCliente;
		this.idDistribuidor=idDistribuidor;
		
		listadoLineaPresup = new ListadoLineaPresup();
	}
	
	public void addBD(){
		
		ResultSet rs;
		
		try {
			PreparedStatement ps=dbConnect.prepareStatement("INSERT INTO presupuestos(ganancia, restaurante, pasaje, combustible, otros, " +
					"hotel, kilometros, isGanancia, porcentaje, totalConIva, transporte, texto, idCliente, totViajes, precioGasolina, " +
					"totalSinIva, idDistribuidor) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			ps.setInt(1, ganancia);
			ps.setInt(2, restaurante);
			ps.setInt(3, pasaje);
			ps.setInt(4, combustible);
			ps.setInt(5, otros);
			ps.setInt(6, hotel);
			ps.setInt(7, kilometros);
			ps.setBoolean(8, isGanancia);
			ps.setInt(9, porcentaje);
			ps.setDouble(10, totalConIva);
			ps.setInt(11, transporte);
			ps.setString(12, texto);
			ps.setInt(13, idCliente);
			ps.setInt(14, nViajes);
			ps.setDouble(15, precioGasolina);
			ps.setDouble(16, totalSinIva);
			ps.setInt(17, idDistribuidor);
			
			ps.executeUpdate();
			
			//pido el id generado
			rs=ps.getGeneratedKeys();
			//paso 1 ya que siempre deolvera solo 1
			rs.next();
			//guardo el id
			idPresupuesto=rs.getInt(1);
			//guardo el idPresupuesto en el listado de lineas
			listadoLineaPresup.setIdPresupuesto(idPresupuesto);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public void addLineaPresupuesto(int idProducto, String nomProducto, double precio, int cantidad) {
		
		listadoLineaPresup.addLinea(new LineaPresupuesto(idPresupuesto, idProducto, nomProducto, precio, cantidad));
		
	}

	public int getIdDistribuidor() {
		return idDistribuidor;
	}

	/**
	 * @return the idPresupuesto
	 */
	public int getIdPresupuesto() {
		return idPresupuesto;
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
	public String getTexto() {
		return texto;
	}

	/**
	 * @return the listadoLineaPresup
	 */
	public ListadoLineaPresup getListadoLineaPresup() {
		return listadoLineaPresup;
	}

	/**
	 * @return the idCliente
	 */
	public int getIdCliente() {
		return idCliente;
	}

	public void cargarLineas(){
		
		listadoLineaPresup.cargarLineaPresup();
		
	}

	/**
	 * @param dbConnect the dbConnect to set
	 */
	public void setDbConnect(Connection dbConnect) {
		this.dbConnect = dbConnect;
	}

	/**
	 * @param idPresupuesto the idPresupuesto to set
	 */
	public void setIdPresupuesto(int idPresupuesto) {
		this.idPresupuesto = idPresupuesto;
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
	 * @param texto the texto to set
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}

	/**
	 * @param listadoLineaPresup the listadoLineaPresup to set
	 */
	public void setListadoLineaPresup(ListadoLineaPresup listadoLineaPresup) {
		this.listadoLineaPresup = listadoLineaPresup;
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
		
		listadoLineaPresup.removeAllElements();
		
	}

	
	
	
	
	
}
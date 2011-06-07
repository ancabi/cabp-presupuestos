/**
 * 
 */
package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	 */
	public Presupuestos(int idPresupuesto, int ganancia, int restaurante,
			int pasaje, int combustible, int otros, int hotel, int kilometros,
			int nViajes, double precioGasolina, boolean isGanancia,
			int porcentaje, double totalConIva, double totalSinIva,
			int transporte, String texto, int idCliente) {
		
		this(ganancia, restaurante, pasaje, combustible, otros, hotel, kilometros, nViajes, precioGasolina, isGanancia,
				porcentaje, totalConIva, totalSinIva, transporte, texto, idCliente);
		
		this.idPresupuesto = idPresupuesto;
		
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
	 */
	public Presupuestos(int ganancia, int restaurante,
			int pasaje, int combustible, int otros, int hotel, int kilometros,
			int nViajes, double precioGasolina, boolean isGanancia,
			int porcentaje, double totalConIva, double totalSinIva,
			int transporte, String texto, int idCliente) {
		
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
		this.listadoLineaPresup = new ListadoLineaPresup();
		this.idCliente = idCliente;
	}
	
	public void addBD(){
		
		ResultSet rs;
		
		try {
			PreparedStatement ps=dbConnect.prepareStatement("INSERT INTO presupuestos(ganancia, restaurante, pasaje, combustible, otros, " +
					"hotel, kilometros, isGanancia, porcentaje, totalConIva, transporte, texto, idCliente, totViajes, precioGasolina, " +
					"totalSinIva) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void addLineaPresupuesto(int idProducto, int cantidad,
			String nomProducto, double precio) {
		
		listadoLineaPresup.addLinea(new LineaPresupuesto(idPresupuesto, idProducto, nomProducto, precio, cantidad));
		
	}
	
	
	
	
}
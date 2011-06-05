/**
 * 
 */
package clases;

/**
 * @author ancabi
 *
 */
public class Presupuestos {
	
	private int idPresupuesto;
	private int ganancia;
	private int restaurante;
	private int pasaje;
	private int combustible;
	private int otros;
	private int hotel;
	private int kilometros;
	private boolean iva;
	private int porcentaje;
	private double total;
	private int transporte;
	private String texto;
	private ListadoLineaPresup listadoLineaPresup;
	
	/**
	 * @param idPresupuesto
	 * @param ganancia
	 * @param restaurante
	 * @param pasaje
	 * @param combustible
	 * @param otros
	 * @param hotel
	 * @param kilometros
	 * @param iva
	 * @param porcentaje
	 * @param total
	 * @param transporte
	 * @param texto
	 */
	public Presupuestos(int idPresupuesto, int ganancia, int restaurante,
			int pasaje, int combustible, int otros, int hotel, int kilometros,
			boolean iva, int porcentaje, double total, int transporte,
			String texto) {
		this.idPresupuesto = idPresupuesto;
		this.ganancia = ganancia;
		this.restaurante = restaurante;
		this.pasaje = pasaje;
		this.combustible = combustible;
		this.otros = otros;
		this.hotel = hotel;
		this.kilometros = kilometros;
		this.iva = iva;
		this.porcentaje = porcentaje;
		this.total = total;
		this.transporte = transporte;
		this.texto = texto;
		
		listadoLineaPresup=new ListadoLineaPresup(idPresupuesto);
		
	}
	
	
	
	
	
}
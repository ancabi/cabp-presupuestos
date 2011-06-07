/**
 * 
 */
package clases;

/**
 * @author ancabi
 *
 */
public class LineaPresupuesto {
	
	private int idProducto;
	private int idPresupuesto;
	private String nombre;
	private double precio;
	private int cantidad;
	
	
	/**
	 * @param idProducto
	 * @param idPresupuesto
	 * @param nombre
	 * @param precio
	 * @param cantidad
	 */
	public LineaPresupuesto(int idPresupuesto, int idProducto, String nombre,
			double precio, int cantidad) {
		this.idPresupuesto = idPresupuesto;
		this.idProducto = idProducto;
		this.nombre = nombre;
		this.precio = precio;
		this.cantidad = cantidad;
	}


	public int getIdProducto() {
		return idProducto;
	}


	public int getIdPresupuesto() {
		return idPresupuesto;
	}


	public String getNombre() {
		return nombre;
	}


	public double getPrecio() {
		return precio;
	}


	public int getCantidad() {
		return cantidad;
	}
	
	
	
	
	
	
	
}
	
/**
 * 
 */
package clases;

/**
 * @author ancabi
 *
 */
public class LineaPresupuesto {
	
	int idProducto;
	int idPresupuesto;
	String nombre;
	double precio;
	int cantidad;
	
	
	/**
	 * @param idProducto
	 * @param idPresupuesto
	 * @param nombre
	 * @param precio
	 * @param cantidad
	 */
	public LineaPresupuesto(int idProducto, int idPresupuesto, String nombre,
			double precio, int cantidad) {
		this.idProducto = idProducto;
		this.idPresupuesto = idPresupuesto;
		this.nombre = nombre;
		this.precio = precio;
		this.cantidad = cantidad;
	}
	
	
	
	
	
}
	
/**
 * 
 */
package clases;

/**
 * @author ancabi
 *
 */
public class LineaPresupuesto {
	
	Productos producto;
	int cantidad;
	/**
	 * @param producto
	 * @param cantidad
	 */
	public LineaPresupuesto(Productos producto, int cantidad) {
		this.producto = producto;
		this.cantidad = cantidad;
	}
	/**
	 * @return the producto
	 */
	public Productos getProducto() {
		return producto;
	}
	/**
	 * @param producto the producto to set
	 */
	public void setProducto(Productos producto) {
		this.producto = producto;
	}
	/**
	 * @return the cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}
	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	

}

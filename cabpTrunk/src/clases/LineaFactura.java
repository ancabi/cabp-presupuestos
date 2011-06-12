package clases;

public class LineaFactura {

	
	private int idProducto;
	private int idFactura;
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
	public LineaFactura(int idFactura, int idProducto, String nombre,
			double precio, int cantidad) {
		this.idFactura = idFactura;
		this.idProducto = idProducto;
		this.nombre = nombre;
		this.precio = precio;
		this.cantidad = cantidad;
	}


	public int getIdProducto() {
		return idProducto;
	}


	public int getIdFactura() {
		return idFactura;
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

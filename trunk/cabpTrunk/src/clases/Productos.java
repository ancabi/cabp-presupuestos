/**
 * 
 */
package clases;

/**
 * @author ancabi
 *
 */
public class Productos {
	
	private int idProducto;
	private String nombre;
	private double precio;
	private int idDistribuidor;
	
	/**
	 * @param idProducto
	 * @param nombre
	 * @param precio
	 * @param idDistribuidor
	 */
	public Productos(int idProducto, String nombre, double precio,
			int idDistribuidor) {
		
		this(nombre, precio, idDistribuidor);
		
		this.idProducto = idProducto;
		
	}

	public Productos(String nombre, double precio, int idDistribuidor) {
		this.nombre = nombre;
		this.precio = precio;
		this.idDistribuidor = idDistribuidor;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the precio
	 */
	public double getPrecio() {
		return precio;
	}

	/**
	 * @param precio the precio to set
	 */
	public void setPrecio(double precio) {
		this.precio = precio;
	}

	/**
	 * @return the idDistribuidor
	 */
	public int getIdDistribuidor() {
		return idDistribuidor;
	}

	/**
	 * @param idDistribuidor the idDistribuidor to set
	 */
	public void setIdDistribuidor(int idDistribuidor) {
		this.idDistribuidor = idDistribuidor;
	}
	
	

	/**
	 * @return the idProducto
	 */
	public int getIdProducto() {
		return idProducto;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return idProducto+" "+nombre+" "+precio+" €";
	}

	public void setIdProducto(int idProducto) {
		this.idProducto=idProducto;
		
	}
	
	
	
	

}

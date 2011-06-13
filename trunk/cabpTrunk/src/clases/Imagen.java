package clases;

import java.io.File;

public class Imagen {
	
	
	int idCliente;
	File imagen;
	long lastModified;
	
	
	/**
	 * @param idCliente
	 * @param imagen
	 */
	public Imagen(int idCliente, File imagen) {
		this.idCliente = idCliente;
		this.imagen = imagen;
		lastModified=imagen.lastModified();
	}
	
	

	/**
	 * @param idCliente
	 * @param imagen
	 * @param lastModified
	 */
	public Imagen(int idCliente, File imagen, long lastModified) {
		this.idCliente = idCliente;
		this.imagen = imagen;
		
		this.lastModified = lastModified;
	}



	/**
	 * @return the idCliente
	 */
	public int getIdCliente() {
		return idCliente;
	}

	/**
	 * @return the imagen
	 */
	public File getImagen() {
		return imagen;
	}

	/**
	 * @return the lastModified
	 */
	public long getLastModified() {
		return lastModified;
	}
	
	public String getName(){
		
		return imagen.getName();
		
	}
	
	
	
	

}

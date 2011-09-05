/**
 * 
 */
package clases;

import java.io.File;

/**
 * @author ancabi
 *
 */
public class Pdf {

	
	private int idCliente;
	private File pdf;
	private long lastModified;
	
	
	/**
	 * @param idCliente
	 * @param imagen
	 */
	public Pdf(int idCliente, File pdf) {
		this.idCliente = idCliente;
		this.pdf = pdf;
		lastModified=pdf.lastModified();
	}
	
	

	/**
	 * @param idCliente
	 * @param imagen
	 * @param lastModified
	 */
	public Pdf(int idCliente, File pdf, long lastModified) {
		this.idCliente = idCliente;
		this.pdf = pdf;
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
	public File getPdf() {
		return pdf;
	}

	/**
	 * @return the lastModified
	 */
	public long getLastModified() {
		return lastModified;
	}
	
	public String getName(){
		
		return pdf.getName();
		
	}


	public void setLastModified(long time) {
		
		lastModified=time;
		
	}
	
}

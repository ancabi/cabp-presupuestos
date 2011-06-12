/**
 * 
 */
package clases;

/**
 * @author ancabi
 *
 */
public class Distribuidor {
	
	private int idDistribuidor;
	private String nombre;
	private String direccion;
	private String email;
	private String telefono;
	private String ciudad;
	private String provincia;
	private String pais;
	private String numeroCta;
	private String iban;
	private String swif;
	
	/**
	 * @param idDistribuidor
	 * @param nombre
	 * @param direccion
	 * @param email
	 * @param telefono
	 * @param ciudad
	 * @param provincia
	 * @param pais
	 * @param numeroCta
	 * @param iban
	 * @param swif
	 */
	public Distribuidor(int idDistribuidor, String nombre, String direccion,
			String email, String telefono, String ciudad, String provincia,
			String pais, String numeroCta, String iban, String swif) {
		
		this(nombre, direccion, email, telefono, ciudad, provincia, pais, numeroCta, iban, swif);
		
		this.idDistribuidor = idDistribuidor;
		
	}

	

	/**
	 * @param nombre
	 * @param direccion
	 * @param email
	 * @param telefono
	 * @param ciudad
	 * @param provincia
	 * @param pais
	 * @param numeroCta
	 * @param iban
	 * @param swif
	 */
	public Distribuidor(String nombre, String direccion, String email,
			String telefono, String ciudad, String provincia, String pais,
			String numeroCta, String iban, String swif) {
		this.nombre = nombre;
		this.direccion = direccion;
		this.email = email;
		this.telefono = telefono;
		this.ciudad = ciudad;
		this.provincia = provincia;
		this.pais = pais;
		this.numeroCta = numeroCta;
		this.iban = iban;
		this.swif = swif;
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
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return the ciudad
	 */
	public String getCiudad() {
		return ciudad;
	}

	/**
	 * @param ciudad the ciudad to set
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	/**
	 * @return the provincia
	 */
	public String getProvincia() {
		return provincia;
	}

	/**
	 * @param provincia the provincia to set
	 */
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	/**
	 * @return the pais
	 */
	public String getPais() {
		return pais;
	}

	/**
	 * @param pais the pais to set
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}

	/**
	 * @return the numeroCta
	 */
	public String getNumeroCta() {
		return numeroCta;
	}

	/**
	 * @param numeroCta the numeroCta to set
	 */
	public void setNumeroCta(String numeroCta) {
		this.numeroCta = numeroCta;
	}

	/**
	 * @return the iban
	 */
	public String getIban() {
		return iban;
	}

	/**
	 * @param iban the iban to set
	 */
	public void setIban(String iban) {
		this.iban = iban;
	}

	/**
	 * @return the swif
	 */
	public String getSwif() {
		return swif;
	}

	/**
	 * @param swif the swif to set
	 */
	public void setSwif(String swif) {
		this.swif = swif;
	}
	
	
	
	
	
	
	
}

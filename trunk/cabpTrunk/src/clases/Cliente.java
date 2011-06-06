/**
 * 
 */
package clases;

import java.util.Vector;

/**
 * @author ancabi
 *
 */
public class Cliente {
	
	private int idCliente;
	private String dni;
	private String nombre;
	private String apellidos;
	private String direccion;
	private String ciudad;
	private String provincia;
	private String empresa;
	private String notas;
	private ListadoTelefonos telefonos;
	private ListadoEmails email;
	
	
	/**
	 * @param idCliente
	 * @param nombre
	 * @param apellidos
	 * @param direccion
	 * @param ciudad
	 * @param provincia
	 * @param empresa
	 * @param notas
	 * @param telefonos
	 * @param email
	 */
	public Cliente(int idCliente, String dni, String nombre, String apellidos,
			String direccion, ListadoTelefonos telefonos, ListadoEmails email, String ciudad, String provincia, String empresa,
			String notas) {

		this.idCliente = idCliente;
		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.direccion = direccion;
		this.ciudad = ciudad;
		this.provincia = provincia;
		this.empresa = empresa;
		this.notas = notas;
		this.telefonos = telefonos;
		this.email = email;
	}
	
	/**
	 * @param nombre
	 * @param apellidos
	 * @param direccion
	 * @param ciudad
	 * @param provincia
	 * @param empresa
	 * @param notas
	 * @param telefonos
	 * @param email
	 */
	public Cliente(String dni, String nombre, String apellidos,
			String direccion, ListadoTelefonos telefonos, ListadoEmails email, String ciudad, String provincia, String empresa,
			String notas) {

		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.direccion = direccion;
		this.ciudad = ciudad;
		this.provincia = provincia;
		this.empresa = empresa;
		this.notas = notas;
		this.telefonos = telefonos;
		this.email = email;
	}


	/**
	 * @return the idCliente
	 */
	public int getIdCliente() {
		return idCliente;
	}


	/**
	 * @param idCliente the idCliente to set
	 */
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	
	/**
	 * @return the dni
	 */
	public String getDni() {
		return dni;
	}


	/**
	 * @param nombre the dni to set
	 */
	public void setDni(String dni) {
		this.dni = dni;
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
	 * @return the apellidos
	 */
	public String getApellidos() {
		return apellidos;
	}


	/**
	 * @param apellidos the apellidos to set
	 */
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
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
	 * @return the empresa
	 */
	public String getEmpresa() {
		return empresa;
	}


	/**
	 * @param empresa the empresa to set
	 */
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}


	/**
	 * @return the notas
	 */
	public String getNotas() {
		return notas;
	}


	/**
	 * @param notas the notas to set
	 */
	public void setNotas(String notas) {
		this.notas = notas;
	}


	/**
	 * @return the telefonos
	 */
	public ListadoTelefonos getTelefonos() {
		
		return telefonos;

	}


	/**
	 * @param telefonos the telefonos to set
	 */
	public void setTelefonos(Vector<Telefonos> tel) {
		telefonos.setTelefonos(tel);
	}


	/**
	 * @return the email
	 */
	public ListadoEmails getEmail() {
		return email;
	}


	/**
	 * @param email the email to set
	 */
	public void setEmail(ListadoEmails email) {
		this.email = email;
	}
	
	
	
	/**
	 * @param email the email to delete
	 */
	public void delEmail(Emails e){

		email.delEmail(e);
	}
	
	public void delTelefono(Telefonos t){
		
		telefonos.delTelefono(t);
		
	}
	
	public void addTelefono(String newTelefono){
		
		telefonos.addTelefono(new Telefonos(newTelefono));
		
	}
	
	public void addEmail(String newEmail){
		
		email.setEmail(new Emails(newEmail));
		
	}
	
	public void delAllTelefonos(){
		
		telefonos.delAllTelefonos();
		
	}
	
	public void delAllEmails(){
		
		email.delAllEmails();
		
	}
	
	
	public Vector<Object> getCliente(){
		
		Vector<Object> v=new Vector<Object>();
		
		v.addElement(idCliente);
		v.addElement(dni);
		v.addElement(nombre);
		v.addElement(apellidos);
		v.addElement(direccion);
		v.addElement(telefonos);
		v.addElement(email);
		v.addElement(ciudad);
		v.addElement(provincia);
		v.addElement(empresa);
		v.addElement(notas);
		v.addElement(idCliente);
		
		return v;
	}

	public void setEmails(Vector<Emails> emails) {
		
		email.setEmails(emails);
		
	}
	

}

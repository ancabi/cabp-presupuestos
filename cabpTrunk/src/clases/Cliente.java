/**
 * 
 */
package clases;

import gui.MainFrame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
	private Vector telefonos;
	private Vector email;
	private PreparedStatement psInsertar;
	private ResultSet rs;
	private PreparedStatement psInsertarTel;
	private PreparedStatement psInsertarEmail;
	private PreparedStatement psBorrarTel;
	private PreparedStatement psBorrarEmail;
	private JFrame mainFrame;
	private Connection dbConnect;
	
	
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
			String direccion, Vector telefonos, Vector email, String ciudad, String provincia, String empresa,
			String notas, JFrame mainFrame) {

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
		this.mainFrame=mainFrame;
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
			String direccion, Vector telefonos, Vector email, String ciudad, String provincia, String empresa,
			String notas, JFrame mainFrame) {

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
		this.mainFrame=mainFrame;
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
	public Vector getTelefonos() {
		return telefonos;
	}


	/**
	 * @param telefonos the telefonos to set
	 */
	public void setTelefonos(Vector telefonos) {
		this.telefonos = telefonos;
	}


	/**
	 * @return the email
	 */
	public Vector getEmail() {
		return email;
	}


	/**
	 * @param email the email to set
	 */
	public void setEmail(Vector email) {
		this.email = email;
	}
	
	/**
	 * @param newEmail the email to add new email
	 */
	public void addEmail(String newEmail){

		if(dbConnect==null){
			dbConnect=((MainFrame) mainFrame).getConnection();
		}
		
		try {
			psInsertarEmail=dbConnect.prepareStatement("INSERT INTO email(idCliente, email) VALUES (?,?)");
			
			psInsertarEmail.setInt(1, idCliente);
			psInsertarEmail.setString(2, newEmail);
			
			psInsertarEmail.executeUpdate();
			
			email.addElement(newEmail);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	/**
	 * @param email the email to delete
	 */
	public void delEmail(String oldEmail){
		
		if(dbConnect==null){
			dbConnect=((MainFrame) mainFrame).getConnection();
		}
		
		try {
			psBorrarEmail=dbConnect.prepareStatement("DELETE FROM email WHERE idCliente=? AND telefono=?");
			
			psInsertarEmail.setInt(1, idCliente);
			psInsertarEmail.setString(2, oldEmail);
			
			psInsertarEmail.executeUpdate();
			
			email.remove(oldEmail);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	/**
	 * @param newtelefono the telefono to add new telefono
	 */
	public void addTelefono(String newTelefono){
		
		if(dbConnect==null){
			dbConnect=((MainFrame) mainFrame).getConnection();
		}
		
		try {
			psInsertarTel=dbConnect.prepareStatement("INSERT INTO telefonos(idCliente, telefono) VALUES (?,?)");
			
			psInsertarTel.setInt(1, idCliente);
			psInsertarTel.setString(2, newTelefono);
			
			psInsertarTel.executeUpdate();
			
			telefonos.addElement(newTelefono);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	/**
	 * @param telefono the telefono to delete
	 */
	public void delTelefono(String oldTelefono){
		
		if(dbConnect==null){
			dbConnect=((MainFrame) mainFrame).getConnection();
		}
		
		try {
			psBorrarTel=dbConnect.prepareStatement("DELETE FROM email WHERE idCliente=? AND telefono=?");
			
			psInsertarTel.setInt(1, idCliente);
			psInsertarTel.setString(2, oldTelefono);
			
			psInsertarTel.executeUpdate();
			
			telefonos.remove(oldTelefono);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	
	public Vector getCliente(){
		
		Vector v=new Vector();
		
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
	
	public void insertarClienteBD(){
		
		try {
			dbConnect=((MainFrame) mainFrame).getConnection();
			
			psInsertar=dbConnect.prepareStatement("INSERT INTO clientes(dni, nombre, apellidos, direccion, ciudad, provincia, empresa, notas)" +
			" VALUES (?,?,?,?,?,?,?,?)");
			
			
			
			psInsertarEmail=dbConnect.prepareStatement("INSERT INTO email(idCliente, email) VALUES (?,?)");
			
			//asigno los campos al preparedStatement
			psInsertar.setString(1, dni);
			psInsertar.setString(2, nombre);
			psInsertar.setString(3, apellidos);
			psInsertar.setString(4, direccion);
			psInsertar.setString(5, ciudad);
			psInsertar.setString(6, provincia);
			psInsertar.setString(7, empresa);
			psInsertar.setString(8, notas);
			
			//lo inserto
			psInsertar.executeUpdate();
			//pido el id generado
			rs=psInsertar.getGeneratedKeys();
			//paso 1 ya que siempre deolvera solo 1
			rs.next();
			//guardo el id
			idCliente=rs.getInt(1);
			
			//recorro el vector
			for(int x=0; x<telefonos.size(); x++){
				
				this.addTelefono((String) telefonos.get(x));
				
			}
			
			//recorro el vector
			for(int x=0; x<email.size(); x++){
				
				this.addEmail((String) email.get(x));
				
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		
		
	}
	

}

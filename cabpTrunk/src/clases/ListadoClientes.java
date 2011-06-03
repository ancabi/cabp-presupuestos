package clases;

import gui.MainFrame;
import gui.PanelCliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;

import conexion.Conectar;

public class ListadoClientes {
	
	private Vector<Cliente> clientes=new Vector<Cliente>();
	private Connection dbConnect=Conectar.getConnection();
	private PreparedStatement psClientes;
	private PreparedStatement psClientesTel;
	private PreparedStatement psClientesEmail;
	private PreparedStatement psInsertar;
	private PreparedStatement psInsertarTel;
	private PreparedStatement psInsertarEmail;
	private PreparedStatement psBorrarCliente;
	
	public ListadoClientes(){
		
		try {
			psClientes = dbConnect.prepareStatement("SELECT * FROM clientes");
			
			psClientesTel=dbConnect.prepareStatement("SELECT telefono FROM telefonos WHERE idCliente=?");
			
			psClientesEmail=dbConnect.prepareStatement("SELECT email FROM email WHERE idCliente=?");
			
			psInsertar=dbConnect.prepareStatement("INSERT INTO clientes(dni, nombre, apellidos, direccion, ciudad, provincia, empresa, notas)" +
			" VALUES (?,?,?,?,?,?,?,?)");
			
			psInsertarTel=dbConnect.prepareStatement("INSERT INTO telefonos(idCliente, telefono) VALUES (?,?)");
			
			psInsertarEmail=dbConnect.prepareStatement("INSERT INTO email(idCliente, email) VALUES (?,?)");
			
			psBorrarCliente=dbConnect.prepareStatement("DELETE FROM clientes WHERE idCliente=?");
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	public void cargarClientes(){
		
		ResultSet rs;
		ResultSet rsSec;
		Vector<Telefonos> telefonos=new Vector<Telefonos>();
		Vector<Emails> emails=new Vector<Emails>();
		
		try {
			
			//traigo todos los clientes
			rs=psClientes.executeQuery();
			
			//itero sobre los clientes
			while(rs.next()){
				
				//guardo los datos de los clientes
				int idCliente=rs.getInt("idCliente");
				String dni=rs.getString("dni");
				String nombre=rs.getString("nombre");
				String apellidos=rs.getString("apellidos");
				String direccion=rs.getString("direccion");
				String ciudad=rs.getString("ciudad");
				String provincia=rs.getString("provincia");
				String empresa=rs.getString("empresa");
				String notas=rs.getString("notas");
				
				//le digo al telefono el idCliente actual
				psClientesTel.setInt(1, idCliente);
				
				//ejecuto la sentencia
				rsSec=psClientesTel.executeQuery();
				
				telefonos=new Vector<Telefonos>();
				
				//itero sobre los telefonos
				while(rsSec.next()){
					//lo asigno
					String telefono=rsSec.getString("telefono");
					//guardo el vector de telefonos
					telefonos.add(new Telefonos(telefono));
					
				}
				//asigno el idCliente para los emails
				psClientesEmail.setInt(1, idCliente);
				
				//ejecuto la consulta
				rsSec=psClientesEmail.executeQuery();
				
				emails=new Vector<Emails>();
				
				//itero sobre los emails
				while(rsSec.next()){
					//lo asigno
					String email=rsSec.getString("email");
					//guardo el vector de emails
					emails.add(new Emails(email));
					
				}
				
				clientes.add(new Cliente(idCliente, dni, nombre, apellidos, direccion, telefonos, emails, ciudad, provincia, empresa, notas));
				
			}
		
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	public Cliente getCliente(int id){
		
		//guardo el cliente que piden
		Cliente c=clientes.get(id);
		//pido el vector con los datos y lo devuelvo
		return c;

	}
	
	public void addCliente(Vector cliente){
		
		ResultSet rs;
		Vector<Telefonos> telefonos=(Vector<Telefonos>) cliente.get(4);
		Vector<Emails> emails=(Vector<Emails>) cliente.get(5);
		int idCliente;
		String dni=(String) cliente.get(0);
		String nombre=(String) cliente.get(1);
		String apellidos=(String) cliente.get(2);
		String direccion=(String) cliente.get(3);
		String ciudad=(String) cliente.get(6);
		String provincia=(String) cliente.get(7);
		String empresa=(String) cliente.get(8);
		String notas=(String) cliente.get(9);
		
		try {

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
				System.out.println(x);
				String telefono=telefonos.get(x).getTelefono();
				
				this.addTelefono(telefono, idCliente);
				
			}
			
			//recorro el vector
			for(int x=0; x<emails.size(); x++){
				System.out.println(x);
				String email=emails.get(x).getEmail();
				
				this.addEmail(email, idCliente);
				
			}
			
			clientes.add(new Cliente(idCliente, dni, nombre, apellidos, direccion, telefonos, emails, ciudad, provincia, 
					empresa, notas));
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		
		
	}
	
	/**
	 * @param newtelefono the telefono to add new telefono
	 */
	public void addTelefono(String newTelefono, int idCliente){

		
		try {

			psInsertarTel.setInt(1, idCliente);
			psInsertarTel.setString(2, newTelefono);
			
			psInsertarTel.executeUpdate();
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	/**
	 * @param newEmail the email to add new email
	 */
	public void addEmail(String newEmail, int idCliente){

		try {
			
			psInsertarEmail.setInt(1, idCliente);
			psInsertarEmail.setString(2, newEmail);
			
			psInsertarEmail.executeUpdate();

			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	/**
	 * @param newtelefono the telefono to add new telefono
	 */
	public void addTelefono(String newTelefono, Cliente c){

		
		try {

			psInsertarTel.setInt(1, c.getIdCliente());
			psInsertarTel.setString(2, newTelefono);
			
			psInsertarTel.executeUpdate();
			
			c.addTelefono(newTelefono);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	/**
	 * @param newEmail the email to add new email
	 */
	public void addEmail(String newEmail, Cliente c){

		try {
			
			psInsertarEmail.setInt(1, c.getIdCliente());
			psInsertarEmail.setString(2, newEmail);
			
			psInsertarEmail.executeUpdate();

			c.addEmail(newEmail);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	public void delCliente(Cliente c) {
		
		try {

			psBorrarCliente.setInt(1, c.getIdCliente());
			
			psBorrarCliente.executeUpdate();
			
			clientes.removeElement(c);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	public Vector getClientes(){
		
		return clientes;
		
	}

}

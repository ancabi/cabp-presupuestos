/**
 * 
 */
package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;

import conexion.Conectar;

/**
 * @author ancabi
 *
 */
public class ListadoDistribuidores {

	private Vector<Distribuidor> distribuidores=new Vector<Distribuidor>();
	private Connection dbConnect=Conectar.getConnection();
	private PreparedStatement psDistribuidores;
	private PreparedStatement psInsertarDistribuidores;
	private PreparedStatement psBorrarDistribuidor;
	private PreparedStatement psActualizarDistribuidor;
	/**
	 * Constructor vacio
	 */
	public ListadoDistribuidores() {
		
		try {
			psDistribuidores=dbConnect.prepareStatement("SELECT * FROM distribuidores");
			
			psInsertarDistribuidores=dbConnect.prepareStatement("INSERT INTO distribuidores (nombre, direccion, email, telefono, ciudad, provincia," +
					"pais, numeroCta, iban, swif) VALUES(?,?,?,?,?,?,?,?,?,?)");
			
			psBorrarDistribuidor=dbConnect.prepareStatement("DELETE FROM distribuidores WHERE idDistribuidor=?");
			
			psActualizarDistribuidor=dbConnect.prepareStatement("UPDATE distribuidores SET nombre=?, ciudad=?, email=?, telefono=?, ciudad=?, provincia=?," +
					"pais=?, numeroCta=?, iban=?, swif=? WHERE idDistribuidor=?");
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	public void cargarDistribuidores(){
		
		try {
			ResultSet rs=psDistribuidores.executeQuery();
			
			while(rs.next()){
				
				int idDistribuidor=rs.getInt("idDistribuidor");
				String nombre=rs.getString("nombre");
				String direccion=rs.getString("direccion");
				String email=rs.getString("email");
				String telefono=rs.getString("telefono");
				String ciudad=rs.getString("ciudad");
				String provincia=rs.getString("provincia");
				String pais=rs.getString("pais");
				String numeroCta=rs.getString("numeroCta");
				String iban=rs.getString("iban");
				String swif=rs.getString("swif");
				
				distribuidores.addElement(new Distribuidor(idDistribuidor, nombre, direccion, email, telefono, ciudad, provincia, pais, numeroCta, iban, swif));
				
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public int getSize() {
		
		return distribuidores.size();
	}

	public Distribuidor getDistribuidor(int x) {
		
		return distribuidores.get(x);
	}

	public void addDistribuidor(Distribuidor d) {
		
		String nombre=d.getNombre();
		String direccion=d.getDireccion();
		String telefono=d.getTelefono();
		String email=d.getEmail();
		String ciudad=d.getCiudad();
		String provincia=d.getProvincia();
		String pais=d.getPais();
		String numeroCta=d.getNumeroCta();
		String iban=d.getIban();
		String swif=d.getSwif();
		
		try {
		
			psInsertarDistribuidores.setString(1, nombre);
			psInsertarDistribuidores.setString(2, direccion);
			psInsertarDistribuidores.setString(3, email);
			psInsertarDistribuidores.setString(4, telefono);
			psInsertarDistribuidores.setString(5, ciudad);
			psInsertarDistribuidores.setString(6, provincia);
			psInsertarDistribuidores.setString(7, pais);
			psInsertarDistribuidores.setString(8, numeroCta);
			psInsertarDistribuidores.setString(9, iban);
			psInsertarDistribuidores.setString(10, swif);
		
		
			psInsertarDistribuidores.executeUpdate();
			
			ResultSet rs=psInsertarDistribuidores.getGeneratedKeys();
			
			rs.next();
			
			d.setIdDistribuidor(rs.getInt(1));
			
			distribuidores.add(d);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public void removeElementAt(int index) {
		
		Distribuidor d=distribuidores.get(index);
		
		int id=d.getIdDistribuidor();
		
		try {
			
			psBorrarDistribuidor.setInt(1, id);
		
			psBorrarDistribuidor.executeUpdate();
			
			distribuidores.removeElement(d);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public void actualizarDistribuidor(Distribuidor d) {
		
		String nombre=d.getNombre();
		String direccion=d.getDireccion();
		String telefono=d.getTelefono();
		String email=d.getEmail();
		String ciudad=d.getCiudad();
		String provincia=d.getProvincia();
		String pais=d.getPais();
		String numeroCta=d.getNumeroCta();
		String iban=d.getIban();
		String swif=d.getSwif();
		
		try {
		
			psActualizarDistribuidor.setString(1, nombre);
			psActualizarDistribuidor.setString(2, direccion);
			psActualizarDistribuidor.setString(3, email);
			psActualizarDistribuidor.setString(4, telefono);
			psActualizarDistribuidor.setString(5, ciudad);
			psActualizarDistribuidor.setString(6, provincia);
			psActualizarDistribuidor.setString(7, pais);
			psActualizarDistribuidor.setString(8, numeroCta);
			psActualizarDistribuidor.setString(9, iban);
			psActualizarDistribuidor.setString(10, swif);
			psActualizarDistribuidor.setInt(11, d.getIdDistribuidor());
		
			psActualizarDistribuidor.executeUpdate();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	
	
	
}

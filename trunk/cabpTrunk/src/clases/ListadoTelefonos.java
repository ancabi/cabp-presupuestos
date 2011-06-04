/**
 * 
 */
package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;

import conexion.Conectar;

/**
 * @author ancabi
 *
 */
public class ListadoTelefonos {
	
	private Vector<Telefonos> telefonos=new Vector<Telefonos>();
	private Connection dbConnect=Conectar.getConnection();
	private PreparedStatement psInsertarTel;
	private int idCliente;
	private PreparedStatement psBorrarTel;
	
	public ListadoTelefonos(int idCliente){
		
		this.idCliente=idCliente;
		
		try {
			psInsertarTel=dbConnect.prepareStatement("INSERT INTO telefonos(idCliente, telefono) VALUES (?,?)");
			
			psBorrarTel=dbConnect.prepareStatement("DELETE FROM telefonos WHERE idCliente=? AND telefono=?");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public Vector<Telefonos> getTelefonos() {
		
		return telefonos;
		
	}

	public void setTelefono(Telefonos newTelefono) {
		
		telefonos.add(newTelefono);
		
		try {
			psInsertarTel.setInt(1, idCliente);
			psInsertarTel.setString(2, newTelefono.getTelefono());
			
			psInsertarTel.executeUpdate();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		
	}
	
	public void addTelefono(Telefonos newTelefono) {
		
		telefonos.add(newTelefono);		
		
	}
	
	/**
	 * @param telefono the telefono to delete
	 */
	public void delTelefono(int index){
		
		Telefonos oldTelefono=telefonos.get(index);
		
		try {
			psBorrarTel.setInt(1, idCliente);
			psBorrarTel.setString(2, oldTelefono.getTelefono());
			
			psBorrarTel.executeUpdate();
			
			telefonos.remove(oldTelefono);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

}

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
	private PreparedStatement psBorrarAllTel;
	
	public ListadoTelefonos(int idCliente){
		
		try {
			psInsertarTel=dbConnect.prepareStatement("INSERT INTO telefonos(idCliente, telefono) VALUES (?,?)");
			
			psBorrarTel=dbConnect.prepareStatement("DELETE FROM telefonos WHERE idCliente=? AND telefono=?");
			
			psBorrarAllTel=dbConnect.prepareStatement("DELETE FROM telefonos WHERE idCliente=?");
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		this.idCliente=idCliente;

	}

	public Vector<Telefonos> getTelefonos() {
		
		return telefonos;
		
	}
	
	public void setTelefono(Telefonos telefono){
		telefonos.add(telefono);
	}
	
	public void addTelefono(Telefonos newTelefono) {

		try {
			psInsertarTel.setInt(1, idCliente);
			psInsertarTel.setString(2, newTelefono.getTelefono());
			
			psInsertarTel.executeUpdate();
			
			telefonos.add(newTelefono);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	/**
	 * @param telefono the telefono to delete
	 */
	public void delTelefono(Telefonos t){
		
		String oldTelefono=t.getTelefono();
		
		try {
			psBorrarTel.setInt(1, idCliente);
			psBorrarTel.setString(2, oldTelefono);
			
			psBorrarTel.executeUpdate();
			
			telefonos.remove(t);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	public int getSize(){
		
		return telefonos.size();
		
	}
	
	public Telefonos getTelefono(int index){
		
		return telefonos.get(index);
		
	}

	public void delAllTelefonos() {
		
		try {
			
			psBorrarAllTel.setInt(1, idCliente);
			
			psBorrarAllTel.executeUpdate();
			
			telefonos.clear();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public void setTelefonos(Vector<Telefonos> tel) {
		
			for(int x=0; x<tel.size(); x++){
					
					this.addTelefono(tel.get(x));
	
			}
	}

}

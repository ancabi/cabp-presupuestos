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
public class ListadoEmails {
	
	private Vector<Emails> emails=new Vector<Emails>();
	private Connection dbConnect=Conectar.getConnection();
	private PreparedStatement psInsertarEmail;
	private int idCliente;
	private PreparedStatement psBorrarEmail;
	
	public ListadoEmails(int idCliente){
		
		this.idCliente=idCliente;
		
		try {
			psInsertarEmail=dbConnect.prepareStatement("INSERT INTO email(idCliente, email) VALUES (?,?)");
			
			psBorrarEmail=dbConnect.prepareStatement("DELETE FROM email WHERE idCliente=? AND email=?");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public Vector<Emails> getEmails() {
		
		return emails;
		
	}

	public void setEmail(Emails newEmail) {
		
		emails.add(newEmail);
		
		try {
			psInsertarEmail.setInt(1, idCliente);
			psInsertarEmail.setString(2, newEmail.getEmail());
			
			psInsertarEmail.executeUpdate();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		
	}
	
	public void addEmail(Emails newEmail) {
		
		emails.add(newEmail);		
		
	}
	
	/**
	 * @param telefono the telefono to delete
	 */
	public void delEmail(int index){
		
		Emails oldEmail=emails.get(index);
		
		try {
			psBorrarEmail.setInt(1, idCliente);
			psBorrarEmail.setString(2, oldEmail.getEmail());
			
			psBorrarEmail.executeUpdate();
			
			emails.remove(oldEmail);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

}

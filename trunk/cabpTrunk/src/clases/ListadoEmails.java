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
	private PreparedStatement psBorrarAllEmail;
	
	public ListadoEmails(int idCliente){
		
		this.idCliente=idCliente;
		
		try {
			psInsertarEmail=dbConnect.prepareStatement("INSERT INTO email(idCliente, email) VALUES (?,?)");
			
			psBorrarEmail=dbConnect.prepareStatement("DELETE FROM email WHERE idCliente=? AND email=?");
			
			psBorrarAllEmail=dbConnect.prepareStatement("DELETE FROM email WHERE idCliente=?");
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public Vector<Emails> getEmails() {
		
		return emails;
		
	}

	public void setEmail(Emails newEmail) {
		
		emails.add(newEmail);
		
	}
	
	public void addEmail(Emails newEmail) {

		try {
			psInsertarEmail.setInt(1, idCliente);
			psInsertarEmail.setString(2, newEmail.getEmail());
			
			psInsertarEmail.executeUpdate();
			
			emails.add(newEmail);	
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	/**
	 * @param telefono the telefono to delete
	 */
	public void delEmail(Emails e){
		
		String email=e.getEmail();
		
		try {
			psBorrarEmail.setInt(1, idCliente);
			psBorrarEmail.setString(2, email);
			
			psBorrarEmail.executeUpdate();
			
			emails.remove(e);
			
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
		
	}
	
	public int getSize(){
		return emails.size();
	}
	
	public Emails getEmail(int index){
		
		return emails.get(index);
		
	}

	public void delAllEmails() {
		
		try {
			psBorrarAllEmail.setInt(1, idCliente);
			
			psBorrarAllEmail.executeUpdate();
			
			emails.clear();
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public void setEmails(Vector<Emails> emails2) {
		
		for(int x=0; x<emails2.size(); x++){
			
			this.addEmail(emails2.get(x));
			
		}
		
	}

}

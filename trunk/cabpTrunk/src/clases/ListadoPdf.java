/**
 * 
 */
package clases;

import java.io.File;
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
public class ListadoPdf {
	
	private int idCliente;
	private Vector<Pdf> pdfs=new Vector<Pdf>();
	private Connection dbConnect=Conectar.getConnection();
	private PreparedStatement psAgregarPdf;
	private PreparedStatement psPdf;
	private PreparedStatement psBorrarPdf;
	private PreparedStatement psActualizarMod;
	
	/**
	 * @param idCliente
	 */
	public ListadoPdf(int idCliente) {
		
		try {
			
			psPdf=dbConnect.prepareStatement("SELECT pdf, lastModified FROM pdf WHERE idCliente=?");
			
			psAgregarPdf=dbConnect.prepareStatement("INSERT INTO pdf(idCliente, pdf, lastModified) VALUES(?,?,?)");
			
			psBorrarPdf=dbConnect.prepareStatement("DELETE FROM pdf WHERE idCliente=? AND pdf=?");
			
			psActualizarMod=dbConnect.prepareStatement("UPDATE pdf SET lastModified=? WHERE pdf=?");
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		this.idCliente = idCliente;
	}
	
	public void cargarPdf(){
		try {
			
			pdfs.removeAllElements();
			
			psPdf.setInt(1, idCliente);
		
			ResultSet rs=psPdf.executeQuery();
			
			while(rs.next()){
				
				String nombre=rs.getString("pdf");
				long lastModified = rs.getLong("lastModified");
				
				pdfs.add(new Pdf(idCliente, new File(nombre), lastModified));
				
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	public void addPdf(File pdf, long lastModified){
		try {
			psAgregarPdf.setInt(1, idCliente);
			psAgregarPdf.setString(2, pdf.getName());
			psAgregarPdf.setLong(3, lastModified);
		
		
			psAgregarPdf.executeUpdate();
			
			pdfs.add(new Pdf(idCliente, pdf));
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "No pueden haber 2 pdf con el mismo nombre");
		}
		
	}

	public int getSize() {
		
		return pdfs.size();
	}

	public Pdf getPdf(int x) {
		
		return pdfs.get(x);
	}

	public void removeElementAt(int index) {
		
		int idCliente=pdfs.get(index).getIdCliente();
		String nombre=pdfs.get(index).getName();
		
		try {
			psBorrarPdf.setInt(1, idCliente);
			psBorrarPdf.setString(2, nombre);
		
			psBorrarPdf.executeUpdate();
		
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public void setLastModified(Pdf p, long time) {
		
		try {
			psActualizarMod.setLong(1, time);
			
			psActualizarMod.setString(2, p.getName());
			
			psActualizarMod.executeUpdate();
			
			p.setLastModified(time);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		
		
		
	}

}

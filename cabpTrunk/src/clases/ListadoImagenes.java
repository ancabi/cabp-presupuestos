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
public class ListadoImagenes {
	
	private int idCliente;
	private Vector<Imagen> imagenes=new Vector<Imagen>();
	private Connection dbConnect=Conectar.getConnection();
	private PreparedStatement psAgregarImagen;
	private PreparedStatement psImagenes;
	private PreparedStatement psBorrarImagen;
	private PreparedStatement psActualizarMod;
	
	/**
	 * @param idCliente
	 */
	public ListadoImagenes(int idCliente) {
		
		try {
			
			psImagenes=dbConnect.prepareStatement("SELECT imagen, lastModified FROM imagenes WHERE idCliente=? ORDER BY lastModified DESC");
			
			psAgregarImagen=dbConnect.prepareStatement("INSERT INTO imagenes(idCliente, imagen, lastModified) VALUES(?,?,?)");
			
			psBorrarImagen=dbConnect.prepareStatement("DELETE FROM imagenes WHERE idCliente=? AND imagen=?");
			
			psActualizarMod=dbConnect.prepareStatement("UPDATE imagenes SET lastModified=? WHERE imagen=?");
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		this.idCliente = idCliente;
	}
	
	public void cargarImagenes(){
		try {
			
			imagenes.removeAllElements();
			
			psImagenes.setInt(1, idCliente);
		
			ResultSet rs=psImagenes.executeQuery();
			
			while(rs.next()){
				
				String nombre=rs.getString("imagen");
				long lastModified = rs.getLong("lastModified");
				
				imagenes.add(new Imagen(idCliente, new File(nombre), lastModified));
				
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	public void addImagen(File imagen, long lastModified){
		try {
			psAgregarImagen.setInt(1, idCliente);
			psAgregarImagen.setString(2, imagen.getName());
			psAgregarImagen.setLong(3, lastModified);
		
		
			psAgregarImagen.executeUpdate();
			
			imagenes.add(new Imagen(idCliente, imagen));
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "No pueden haber 2 imagenes con el mismo nombre");
		}
		
	}

	public int getSize() {
		
		return imagenes.size();
	}

	public Imagen getImagen(int x) {
		
		return imagenes.get(x);
	}

	public void removeElementAt(int index) {
		
		int idCliente=imagenes.get(index).getIdCliente();
		String nombre=imagenes.get(index).getName();
		
		try {
			psBorrarImagen.setInt(1, idCliente);
			psBorrarImagen.setString(2, nombre);
		
			psBorrarImagen.executeUpdate();
		
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

	public void setLastModified(Imagen i, long time) {
		
		try {
			psActualizarMod.setLong(1, time);
			
			psActualizarMod.setString(2, i.getName());
			
			psActualizarMod.executeUpdate();
			
			i.setLastModified(time);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		
		
		
	}
	
	

}

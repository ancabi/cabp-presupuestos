/**
 * 
 */
package modelo;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import clases.ListadoEmails;
import clases.ListadoTelefonos;

/**
 * @author ancabi
 *
 */
public class ModeloClientes extends AbstractTableModel {

	private Vector data=new Vector();
	private Vector head=new Vector();
	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return head.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {

		return data.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		if(columnIndex==5){
			
			Vector rowVector = (Vector)data.elementAt(rowIndex);

			ListadoTelefonos temp=(ListadoTelefonos) rowVector.elementAt(columnIndex);

			if(temp.getTelefonos().isEmpty()){
				
				return "";
				
			}else{
				return temp.getTelefonos().get(0);
			}
			
			
		}if(columnIndex==6){
			
			Vector rowVector = (Vector)data.elementAt(rowIndex);

			ListadoEmails temp=(ListadoEmails) rowVector.elementAt(columnIndex);

			if(temp.getEmails().isEmpty()){
				
				return "";
				
			}else{
				return temp.getEmails().get(0);
			}
				
		}else{
		
			Vector rowVector = (Vector)data.elementAt(rowIndex);
			
	        return rowVector.elementAt(columnIndex);
	        
		}
	}
	
	public void setHeader(Vector h){
		head=h;
	}
	
	public void setData(Vector d){

		data=d;
		
		fireTableDataChanged();

	}
	
	public String getColumnName(int columnIndex){

		return (String) head.elementAt(columnIndex);
		
		
	}
	
	public void setRowCount(int count){
		//vacio el vector
		data.clear();
		
	}
	
	public boolean isCellEditable(){
		return false;
	}
	
	public void deleteRow(int rowIndex){
		data.removeElementAt(rowIndex);
	}
	
	public Vector getRow(int rowIndex){

		return (Vector) data.get(rowIndex);
		
	}

}

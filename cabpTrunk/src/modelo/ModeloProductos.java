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
public class ModeloProductos extends AbstractTableModel {

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

		Vector rowVector = (Vector)data.elementAt(rowIndex);
		
		return rowVector.elementAt(columnIndex);

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
	
	public boolean isCellEditable (int row, int column){
	       
		if (column == 3){
			return true;
	    }
	       	return false;
	   }
	
	public Vector getRow(int rowIndex){

		return (Vector) data.get(rowIndex);
		
	}
	
	public void addRow(Vector row){
		
		data.add(row);
		
		fireTableDataChanged();
		
	}
	
	public void setValueAt(Object aValue, int row, int column){
		
		Vector temp=(Vector) data.get(row);
		
		temp.setElementAt(aValue, column);
		
	}
	
	public void removeAllElements(){
		
		data.removeAllElements();
		
		fireTableDataChanged();
		
	}
	
	public Vector<Vector> getData(){
		return data;
	}
	
	public void removeElementAt(int index){
		
		data.removeElementAt(index);
		
		fireTableDataChanged();
	}

}

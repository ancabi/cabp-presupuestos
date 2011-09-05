/**
 * 
 */
package gui;

import java.awt.GridBagLayout;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import javax.swing.ImageIcon;

import clases.Cliente;
import clases.Imagen;
import clases.ListadoImagenes;

import filtos.FiltroImagenes;

import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import modelo.ModeloImagenes;

/**
 * @author ancabi
 *
 */
public class PanelImagenes extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel panelSuperior = null;
	private JLabel lblTitulo = null;
	private JPanel panelHerramientas = null;
	private JButton btnAddImg = null;
	private JButton btnDelImg = null;
	private JFileChooser fc;
	private Cliente clienteActual;  //  @jve:decl-index=0:
	private ListadoImagenes listado;  //  @jve:decl-index=0:
	private JScrollPane scrollTabla = null;
	private JTable tabalImagenes = null;
	private ModeloImagenes modelo;
	private JPanel panelPrev = null;
	private JLabel lblPrev = new JLabel();

	/**
	 * This is the default constructor
	 */
	public PanelImagenes() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		
		this.setSize(630, 421);
		this.setLayout(new BorderLayout());
		this.add(getPanelSuperior(), BorderLayout.NORTH);
		this.add(getScrollTabla(), BorderLayout.WEST);
		this.add(getPanelPrev(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes panelSuperior	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelSuperior() {
		if (panelSuperior == null) {
			lblTitulo = new JLabel();
			lblTitulo.setText("Imagenes del cliente");
			lblTitulo.setFont(new Font("Verdana", Font.BOLD, 18));
			panelSuperior = new JPanel();
			panelSuperior.setLayout(new BorderLayout());
			panelSuperior.add(lblTitulo, BorderLayout.WEST);
			panelSuperior.add(getPanelHerramientas(), BorderLayout.EAST);
		}
		return panelSuperior;
	}

	/**
	 * This method initializes panelHerramientas	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelHerramientas() {
		if (panelHerramientas == null) {
			panelHerramientas = new JPanel();
			panelHerramientas.setLayout(new FlowLayout());
			panelHerramientas.add(getBtnAddImg(), null);
			panelHerramientas.add(getBtnDelImg(), null);
		}
		return panelHerramientas;
	}

	/**
	 * This method initializes btnAddImg	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnAddImg() {
		if (btnAddImg == null) {
			btnAddImg = new JButton();
			btnAddImg.setIcon(new ImageIcon(getClass().getResource("/img/add.png")));
			btnAddImg.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					File[] imagenes=getFiles();
					
					File carpeta=new File(clienteActual.getIdCliente()+clienteActual.getNombre()+clienteActual.getApellidos());
					//si la carpeta del usuario no existe
					if(!carpeta.exists()){
						//la creo
						carpeta.mkdir();
						carpeta.setExecutable(true);
						
					}
					
					if(imagenes!=null){
					
						for(int x=0; x<imagenes.length; x++){
							
							//copio el ficheron en la carpeta destino
							try {
								
								listado.addImagen(new File(imagenes[x].getName()), imagenes[x].lastModified());
								
								InputStream in = new FileInputStream(imagenes[x].getAbsoluteFile());
								OutputStream out = new FileOutputStream(new File(carpeta.toString()+"/"+imagenes[x].getName()));

								byte[] buf = new byte[5120];
						        int len;
						        while ((len = in.read(buf)) > 0) {
						            out.write(buf, 0, len);
						        }
						        in.close();
						        out.close(); 
						        
						        
						        
						        cargarImagenes();
						        
							} catch (FileNotFoundException e1) {
								JOptionPane.showMessageDialog(null, e1.getMessage());
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(null, e1.getMessage());
							}
							
						}
					}
					
					
				}
			});
		}
		return btnAddImg;
	}
	
	private File[] getFiles(){
		
		fc=new JFileChooser();
		
		fc.addChoosableFileFilter(new FiltroImagenes());
		
		fc.setMultiSelectionEnabled(true);
		
		int result=fc.showSaveDialog(null);

		if(result==JFileChooser.APPROVE_OPTION){
			
			return fc.getSelectedFiles();
			
		}
		
		return null;
	}

	/**
	 * This method initializes btnDelImg	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnDelImg() {
		if (btnDelImg == null) {
			btnDelImg = new JButton();
			btnDelImg.setIcon(new ImageIcon(getClass().getResource("/img/delete.png")));
			btnDelImg.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					int index=tabalImagenes.getSelectedRow();
					index=tabalImagenes.convertRowIndexToModel(index);

					
					if(index>=0){
						//lo borro de la BD
						listado.removeElementAt(index);
						//traigo el objeto Imagen
						Imagen i=listado.getImagen(index);
						//Consigo el objeto file
						File f=new File(clienteActual.getIdCliente()+clienteActual.getNombre()+clienteActual.getApellidos()+"/"+i.getName());
						//lo borro
						f.delete();
						
						lblPrev.setIcon(null);
						
						cargarImagenes();
						
					}
					
				}
			});
		}
		return btnDelImg;
	}

	public void setCliente(Cliente c) {
		
		clienteActual=c;
		
		listado=new ListadoImagenes(c.getIdCliente());
		
		cargarImagenes();
		
	}

	private void cargarImagenes() {
		
		listado.cargarImagenes();
		Vector info;
		Vector data=new Vector();
		Date d;
		for(int x=0; x<listado.getSize(); x++){
			
			
			Imagen i=listado.getImagen(x);
			
			info=new Vector();
			
			info.add(i.getName());
			info.add(d=new Date(i.getLastModified()));
			
			data.add(info);

		}
		
		modelo.setData(data);
		
	}

	/**
	 * This method initializes scrollTabla	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollTabla() {
		if (scrollTabla == null) {
			scrollTabla = new JScrollPane();
			scrollTabla.setViewportView(getTabalImagenes());
		}
		return scrollTabla;
	}

	/**
	 * This method initializes tabalImagenes	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTabalImagenes() {
		if (tabalImagenes == null) {
			
			modelo=new ModeloImagenes();
			
			Vector<String> head=new Vector<String>();
			
			head.add("Nombre");
			head.add("Ultima modificacion");
			
			modelo.setHeader(head);
			
			tabalImagenes = new JTable(modelo);
			tabalImagenes.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					
					if (e.getClickCount() >= 2){
						
						//traigo la seleccion
						int index=tabalImagenes.getSelectedRow();
						
						index=tabalImagenes.convertRowIndexToModel(index);
						//traigo el objeto Imagen
						Imagen i=listado.getImagen(index);
							
						//Consigo el objeto file
						File f=new File(clienteActual.getIdCliente()+clienteActual.getNombre()+clienteActual.getApellidos()+"/"+i.getName());

						
						
						
						if(System.getProperty("os.name").equals("Linux") && !Desktop.isDesktopSupported()){
							
							Runtime obj = Runtime.getRuntime();
							try {
								obj.exec("gimp "+f.toString());
								
								Date d=new Date();
								
								listado.setLastModified(i, d.getTime());
								
								cargarImagenes();
								
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(null, e1.getMessage());
							}  
							
						}else{
							try {
								Desktop.getDesktop().edit(f);
								
								Date d=new Date();
								
								listado.setLastModified(i, d.getTime());
								
								cargarImagenes();
								
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(null, e1.getMessage());
							}
						}
						
						
						
					}else{
						//traigo la seleccion
						int index=tabalImagenes.getSelectedRow();
						
						index=tabalImagenes.convertRowIndexToModel(index);
							
						lblPrev.setText("");
						//traigo el objeto Imagen
						Imagen i=listado.getImagen(index);
							
						//Consigo el objeto file
						File f=new File(clienteActual.getIdCliente()+clienteActual.getNombre()+clienteActual.getApellidos()+"/"+i.getName());
							
						ImageIcon imagen=new ImageIcon(f.getAbsolutePath());
						try{
							int panelW=panelPrev.getWidth();
							int panelH=panelPrev.getHeight();
							int imagenW=imagen.getIconWidth();
							int imagenH=imagen.getIconHeight();
							
							while(imagenW>panelW){
								imagenW=(int) (imagenW*0.5);
							}
							
							while(imagenH>panelH){
								imagenH=(int) (imagenH*0.5);
							}
								//redimenciono la imagen
							imagen=new ImageIcon(imagen.getImage().getScaledInstance(imagenW, 
									imagenH, Image.SCALE_FAST));
								
							lblPrev.setIcon(imagen);
							
						}catch(java.lang.IllegalArgumentException e1){
								
							lblPrev.setIcon(null);
							lblPrev.setText("Previsualizacion no disponible");
								
						}
					}
				}
					
			});
		}
		
		tabalImagenes.setAutoCreateRowSorter(true);
		
		
		return tabalImagenes;
	}

	/**
	 * This method initializes panelPrev	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelPrev() {
		if (panelPrev == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			lblPrev.setText("");
			panelPrev = new JPanel();
			panelPrev.setLayout(new GridBagLayout());
			panelPrev.add(lblPrev, gridBagConstraints);
		}
		return panelPrev;
	}

	public void limpiarPrevisualizacion() {
		
		lblPrev.setIcon(null);
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

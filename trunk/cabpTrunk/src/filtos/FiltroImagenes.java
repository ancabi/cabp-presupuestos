package filtos;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FiltroImagenes extends FileFilter {

	@Override
	public boolean accept(File arg0) {

		if(arg0.isDirectory()){
			return true;
		}
		
		String nombre=arg0.getName();
			
		if(nombre.matches(".*\\.jpg") || nombre.matches(".*\\.png") || nombre.matches(".*\\.gif") || nombre.matches(".*\\.bmp")){
			
			return true;
			
		}else{
			
			return false;
			
		}
	}

	@Override
	public String getDescription() {
		
		return "*.jpg, *.png, *.gif, *.bmp";
	}

}

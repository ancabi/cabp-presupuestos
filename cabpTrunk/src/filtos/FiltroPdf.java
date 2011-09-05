package filtos;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FiltroPdf extends FileFilter {

	@Override
	public boolean accept(File arg0) {

		if(arg0.isDirectory()){
			return true;
		}
		
		String nombre=arg0.getName();
			
		if(nombre.matches(".*\\.pdf") ){
			
			return true;
			
		}else{
			
			return false;
			
		}
	}

	@Override
	public String getDescription() {
		
		return "*.pdf";
	}

}

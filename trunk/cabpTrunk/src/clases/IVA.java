package clases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

public class IVA {
	
	private static double iva;
	
	public static void readIVA() throws NumberFormatException, IOException{

			
		File archivo = new File("iva.cabp");
		FileReader fr = new FileReader(archivo);
		BufferedReader br = new BufferedReader(fr);
		iva=Double.parseDouble(br.readLine())/100.0;
			
		
	}
	
	public static double getIVA(){
		
		return iva;
		
	}

}

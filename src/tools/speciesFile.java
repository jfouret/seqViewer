package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class speciesFile extends File {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3350825225331311885L;

	public speciesFile(String pathname) {
		super(pathname);
		// TODO Auto-generated constructor stub
	}

	public Species readSpecies() throws FileNotFoundException{
		Species species = new Species();
		Scanner sc = new Scanner(this);
		ArrayList<String> backSpeciesList = new ArrayList<String>();
		ArrayList<String> foreSpeciesList = new ArrayList<String>();
		String currentLine=sc.nextLine().trim();
		backSpeciesList.add(currentLine);
		boolean background=true;
		while(sc.hasNext()){
			currentLine=sc.nextLine().trim();
			//System.out.println(currentLine);
			if (currentLine.startsWith("-")){
				//System.out.println("OK detecting the ---");
				species.setBackground(backSpeciesList);
				background=false;
			}else{
				if (background){
					backSpeciesList.add(currentLine);
				}else{
					foreSpeciesList.add(currentLine);
				}
			}
		}
		species.setForeground(foreSpeciesList);
		sc.close();
		return(species);
	}
}

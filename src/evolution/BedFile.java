package evolution;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class BedFile extends File {
	
	private static final long serialVersionUID = 3309506459232402197L;

	private BedEntry[] bedEntries;
	
	public BedFile(String arg0) {
		super(arg0);
		try {
			Scanner sc;
			sc = new Scanner(this);
			String currentLine=sc.nextLine().trim(); //pass the first line			
			ArrayList<BedEntry> bedList =new  ArrayList<BedEntry>();
			while(sc.hasNext()){
				currentLine=sc.nextLine().trim();
				bedList.add(new BedEntry(currentLine)); 
				}
			sc.close();
			bedEntries=bedList.toArray(new BedEntry[bedList.size()]);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public BedEntry get(int i){
		return(this.bedEntries[i]);
	}
	
	public long length(){
		return(this.bedEntries.length);
	}
	
}

package evolution;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class BedFile {
	
	private BedEntry[] bedEntries;
	
	public BedFile(BufferedReader bf) {
		try {
			String currentLine=bf.readLine();
			ArrayList<BedEntry> bedList =new  ArrayList<BedEntry>();
			while(currentLine!=null){
				if (currentLine.startsWith("#")){
					currentLine=bf.readLine(); // pass if comment
				}else{
					currentLine=currentLine.trim();
					bedList.add(new BedEntry(currentLine)); 
					currentLine=bf.readLine();					
					}
				}
			bedEntries=bedList.toArray(new BedEntry[bedList.size()]);
		} catch (IOException e) {
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

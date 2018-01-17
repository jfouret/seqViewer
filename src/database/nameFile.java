package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class nameFile extends File {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3060830017553932103L;
	//private String geneName;
	private String RefSeq;
	private String uniprotID;
	//private String kgName;
	private String kgID;
	public nameFile(String arg0) throws FileNotFoundException {
		super(arg0);
		Scanner sc = new Scanner(nameFile.this);
		//pass the first line
		String currentLine=sc.nextLine().trim();
		String[] split;
		currentLine=sc.nextLine().trim();
		split=currentLine.split("\t");
		sc.close();
		//geneName=split[0];
		kgID=split[1];
		uniprotID=split[2];
		RefSeq=split[3];
		//kgName=split[4];
	}
	public String getRefSeq(){
		return(RefSeq);
	}
	public String getUniprot(){
		return(uniprotID);
	}
	public String getKgID(){
		return(kgID);
	}
}

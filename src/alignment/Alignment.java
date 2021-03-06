/*
 *   seqViewer is a program dedicated to the visualization of sites under 
 *   branch-site positive selection or similar approaches with superpositions 
 *   with annotated UniProt features.
 *   
 *   Copyright (C) 2018  Julien Fouret
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/**
 * 
 */
package alignment;
import java.util.*;
/**
 * @author julien.fouret
 *
 */

public class Alignment {
	
	/**
	 * @param Input_geneticCode the genetic code to be used to analyze the alignment
	 * @param InputSequence name of the imported sequence as a key with the sequence as a value
	 */
	
	private int nuclLen ; 
	private int codLen ; 
	private int nCol;
	private int nLine;
	private HashMap<String,String> letterColor = new HashMap<String,String>();
	private genCode geneticCode ;
	private HashMap<String,String> Sequences ;
	private String HTML;
	private HashMap<String,String[]> nuclText  = new HashMap<String,String[]>(); 
	private HashMap<String,String[]> codText = new HashMap<String,String[]>() ;
	private HashMap<String,String[]> aaText = new HashMap<String,String[]>() ;
	
	public String getSeqRef(String ref){
		StringBuilder builder = new StringBuilder();
		for (String aa : aaText.get(ref)) {
			builder.append(aa);
		}
		return (builder.toString()).replaceAll("-", "");
	}
	
	public String[] get_ref_dna(String ref){
		return(nuclText.get(ref));
	}
	
	public Alignment(HashMap<String,String> Input_Sequences, genCode Input_geneticCode) {
		// initiate colors
		//seqhg19=Input_Sequences.get("hg19").replaceAll("-", "");
		geneticCode=Input_geneticCode;
		Sequences=Input_Sequences; //wash it ...
		nuclLen = Sequences.get(Sequences.keySet().toArray()[0]).length();
		codLen = nuclLen / 3;
		//System.out.println("LENGTH:"+codLen);
		String species ;
		String seq ;
		int charCounter = 0;
		int posCounter = 0;
		String codon = "";
		String[] codArray = new String[codLen];
		String[] aaArray = new String[codLen];
		for (HashMap.Entry<String, String> entry : Sequences.entrySet()) {

		    species = entry.getKey();
		    seq = entry.getValue();
		    
		    //System.out.println("KEY:"+species);
		    //System.out.println("SEQ:"+seq);
		    

		    nuclText.put(species,seq.split("")); //TODO test is the split is OK

		    for ( String nucleotid : nuclText.get(species) ){
		    	codon+=nucleotid;
		    	charCounter+=1;
		    	if ((charCounter==3)  && (codon.length()==3)){


		    		aaArray[posCounter]=geneticCode.translate(codon);

		    		codArray[posCounter]=codon;

		    		codon="";
		    		charCounter=0;
		    		posCounter+=1;
		    	}
		    }
		    if (codon.length()==3){

		    	aaArray[posCounter]=geneticCode.translate(codon);

		    	codArray[posCounter]=codon;
		    }else if (codon.length()!=0){

		    }
		    codon="";
		    charCounter=0;
		    posCounter=0;
		    aaText.put(species, aaArray.clone());
		    codText.put(species, codArray.clone());
		}
	}
	
	public void buildHTML(String seqType,String[] species) {
		HashMap<String, String[]> letterText;
		if (seqType=="Nucleotids"){
			letterText=nuclText;
			nCol=nuclLen;
			letterColor.put("A","145AFF");letterColor.put("T","E6E600");letterColor.put("C","0F820F");letterColor.put("G","E60A0A");
		}else if (seqType=="Amino acids"){
			letterText=aaText;
			nCol=codLen;
			letterColor.put("D","E60A0A"); letterColor.put("E","E60A0A"); letterColor.put("C","E6E600"); letterColor.put("M","E6E600"); letterColor.put("K","145AFF"); letterColor.put("R","145AFF"); letterColor.put("S","FA9600"); letterColor.put("T","FA9600"); letterColor.put("F","3232AA"); letterColor.put("Y","3232AA"); letterColor.put("N","00DCDC"); letterColor.put("Q","00DCDC"); letterColor.put("G","EBEBEB"); letterColor.put("V","0F820F"); letterColor.put("I","0F820F"); letterColor.put("L","0F820F"); letterColor.put("A","C8C8C8"); letterColor.put("W","B45AB4"); letterColor.put("H","8282D2"); letterColor.put("P","DC9682"); 
		} else {
			letterColor.put("D","E60A0A"); letterColor.put("E","E60A0A"); letterColor.put("C","E6E600"); letterColor.put("M","E6E600"); letterColor.put("K","145AFF"); letterColor.put("R","145AFF"); letterColor.put("S","FA9600"); letterColor.put("T","FA9600"); letterColor.put("F","3232AA"); letterColor.put("Y","3232AA"); letterColor.put("N","00DCDC"); letterColor.put("Q","00DCDC"); letterColor.put("G","EBEBEB"); letterColor.put("V","0F820F"); letterColor.put("I","0F820F"); letterColor.put("L","0F820F"); letterColor.put("A","C8C8C8"); letterColor.put("W","B45AB4"); letterColor.put("H","8282D2"); letterColor.put("P","DC9682"); 
			letterText=codText;
			nCol=codLen;
		}
		// When sequences are stored in char
		String letter;
		String color;

		StringBuilder builder = new StringBuilder();
		builder.append("<html><span >");
		for (String spec: species ) {

			for ( int i = 0; i < letterText.get(spec).length ; i+=1){	

				letter=letterText.get(spec)[i];
				if (seqType=="Codons") {
					color=letterColor.get(geneticCode.translate(letter));
				}else {
					color=letterColor.get(letter);
				}
				builder.append("<b style=\"background-color: #"+color+";\">"+letter+"</b>");
			}
			builder.append("<br>");
			nLine++;
		}
		HTML = builder.toString();
	}
	
	public String getHTML(){
		return(HTML);
	}
	
	public int getLine(){
		return(nLine);
	}
	
	public String getPos(String seqType) {
		int alnLen;
		String pos;
		if (seqType=="Nucleotids"){
			alnLen=nCol;
		}else if (seqType=="Amino acids"){
			alnLen=nCol;
		} else {
			alnLen=nCol*3;
		}
		StringBuilder builder=new StringBuilder();
		builder.append("1   .    ");
		for (int i = 10; i < alnLen+10 ; i+=10) {
			//TODO make the string
			pos = String.valueOf(i);
			builder.append(pos);
			builder.append(String.join("", Collections.nCopies(5-pos.length(), " ")));
			builder.append(".    ");
		}
		return builder.toString();
	}
	
	public int getSize(){
		return(nCol);
	}

	public int getLength(String seqType) {
		int alnLen=0;
		switch (seqType){
		case "Amino acids":
			alnLen= nCol;
			break;
		case "Codons":
			alnLen= nCol*3;
			break;
		case "Nucleotids":
			alnLen= nCol;
			break;
		}
		return alnLen;
	}
}

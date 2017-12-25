/**
 * 
 */
package alignment;
import java.util.*;

import evolution.Species;
import tools.myFONT;

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
	private HashMap<String,String> colorAA = new HashMap<String,String>();
	private HashMap<String,String> colorNucl = new HashMap<String,String>();
	private alignment.htmlBlock htmlTextMap ;
	private genCode geneticCode ;
	private HashMap<String,String> Sequences ;
	
	private HashMap<String,String[]> nuclText  = new HashMap<String,String[]>(); 
	private HashMap<String,String[]> codText = new HashMap<String,String[]>() ;
	private HashMap<String,String[]> aaText = new HashMap<String,String[]>() ;
	
	public String getSeqRef(){
		StringBuilder builder = new StringBuilder();
		for (String aa : aaText.get("hg19")) {
			builder.append(aa);
		}
		return (builder.toString()).replaceAll("-", "");
	}
	
	public Alignment(HashMap<String,String> Input_Sequences, genCode Input_geneticCode) {
		// initiate colors
		//seqhg19=Input_Sequences.get("hg19").replaceAll("-", "");
		colorAA.put("D","E60A0A"); colorAA.put("E","E60A0A"); colorAA.put("C","E6E600"); colorAA.put("M","E6E600"); colorAA.put("K","145AFF"); colorAA.put("R","145AFF"); colorAA.put("S","FA9600"); colorAA.put("T","FA9600"); colorAA.put("F","3232AA"); colorAA.put("Y","3232AA"); colorAA.put("N","00DCDC"); colorAA.put("Q","00DCDC"); colorAA.put("G","EBEBEB"); colorAA.put("V","0F820F"); colorAA.put("I","0F820F"); colorAA.put("L","0F820F"); colorAA.put("A","C8C8C8"); colorAA.put("W","B45AB4"); colorAA.put("H","8282D2"); colorAA.put("P","DC9682"); 
		colorNucl.put("A","145AFF");colorNucl.put("T","E6E600");colorNucl.put("C","0F820F");colorNucl.put("G","E60A0A");
		geneticCode=Input_geneticCode;
		Sequences=Input_Sequences; //wash it ...
		nuclLen = Sequences.get(Sequences.keySet().toArray()[0]).length();
		codLen = nuclLen / 3;
		String species ;
		String seq ;
		int charCounter = 0;
		int posCounter = 0;
		String codon = "";
		String[] codArray = new String[codLen];
		String[] aaArray = new String[codLen];
		for (HashMap.Entry<String, String> entry : Sequences.entrySet()) {
			//System.out.println("OK entering loop");
		    species = entry.getKey();
		    seq = entry.getValue();
		    //System.out.println("ENTRY:"+species+"="+seq);
		    nuclText.put(species,seq.split("")); //TODO test is the split is OK
		    //System.out.println("ENTRY_length:"+nuclText.get(species).length);
		    for ( String nucleotid : nuclText.get(species) ){
		    	codon+=nucleotid;
		    	charCounter+=1;
		    	if ((charCounter==3)  && (codon.length()==3)){
		    		aaArray[posCounter]=geneticCode.translate(codon);
		    		codArray[posCounter]=codon;
		    		//System.out.println("Translating codon "+codon+" in aa "+geneticCode.translate(codon)+"." );
		    		codon="";
		    		charCounter=0;
		    		posCounter+=1;
		    	}
		    }
		    if (codon.length()==3){
		    	aaArray[posCounter]=geneticCode.translate(codon);
		    	codArray[posCounter]=codon;
		    }else if (codon.length()!=0){
		    	//System.out.println("WTF");
		    }
		    codon="";
		    charCounter=0;
		    posCounter=0;
		    aaText.put(species, aaArray.clone());
		    codText.put(species, codArray.clone());
		}
	}
	
	public String buildHTML(String seqType,Species species) {
		int nCol;
		HashMap<String, String> letterColor;
		HashMap<String, String[]> letterText;
		if (seqType=="Nucleotids"){
			letterText=nuclText;
			letterColor=colorNucl;
			nCol=nuclLen;
		}else if (seqType=="Amino acids"){
			letterText=aaText;
			letterColor=colorAA;
			nCol=codLen;
		} else {
			letterText=codText;
			letterColor=geneticCode.getColorCodons(colorAA);
			nCol=codLen;
			htmlTextMap = new alignment.htmlBlock(codLen);
			htmlTextMap.addALNString(species, codText, geneticCode.getColorCodons(colorAA));
		}
		// When sequences are stored in char
		ArrayList<String> backgroundList = species.getBackground();
		ArrayList<String> foregroundList = species.getForeground();
		String letter;
		//System.out.println("OK entering the if statement");
		StringBuilder builder = new StringBuilder();
		builder.append("<html><span >");
		for (String backgroundSpecies: backgroundList ) {
			//System.out.println("OK entering the for statement on species");
			for ( int i = 0; i < letterText.get(backgroundSpecies).length ; i+=1){	
				//System.out.println("OK entering the for statement on letter");
				letter=letterText.get(backgroundSpecies)[i];
				builder.append("<b style=\"background-color: #"+letterColor.get(letter)+";\">"+letter+"</b>");
			}
			builder.append("<br>");
		}
		for (String foregroundSpecies: foregroundList ) {
			//System.out.println("OK entering the for statement on species");
			//for (Character letter : letterText.get(backgroundSpecies) ){
			for (int i = 0; i < letterText.get(foregroundSpecies).length ; i+=1){	
				//System.out.println("OK entering the for statement on letter");
				letter=letterText.get(foregroundSpecies)[i];
				builder.append("<u><b style=\"background-color: #"+letterColor.get(letter)+";\">"+letter+"</b></u>");
				//System.out.println(letter+"formatted as : "+formatHTML[i]);
			}
			builder.append("<br>");
		}
		return builder.toString();
	}
	public alignment.htmlBlock getHtmlBlock(){
		return(htmlTextMap);
	}
	
	public int getHeight(){
		double height = htmlTextMap.getNLine()*myFONT.getHeight();
		return((int)height+1);
	}
	
	public int getSize(){
		return(htmlTextMap.getNCol());
	}
	public int getWidth(){
		double width = htmlTextMap.getNCol()*myFONT.getWidth();
		return((int)width+1);
	}
}

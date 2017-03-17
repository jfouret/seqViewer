/**
 * 
 */
package tools;
import java.util.*;
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
	private HashMap<Character,String> colorAA = new HashMap<Character,String>();
	private HashMap<Character,String> colorNucl = new HashMap<Character,String>();
	private tools.htmlBlock htmlTextMap ;
	private genCode geneticCode ;
	private HashMap<String,String> Sequences ;
	private HashMap<String,char[]> nuclText  = new HashMap<String,char[]>(); 
	private HashMap<String,String[]> codText = new HashMap<String,String[]>() ;
	private HashMap<String,char[]> aaText = new HashMap<String,char[]>() ;
	public Alignment(HashMap<String,String> Input_Sequences, genCode Input_geneticCode) {
		// initiate colors
		colorAA.put('D',"E60A0A"); colorAA.put('E',"E60A0A"); colorAA.put('C',"E6E600"); colorAA.put('M',"E6E600"); colorAA.put('K',"145AFF"); colorAA.put('R',"145AFF"); colorAA.put('S',"FA9600"); colorAA.put('T',"FA9600"); colorAA.put('F',"3232AA"); colorAA.put('Y',"3232AA"); colorAA.put('N',"00DCDC"); colorAA.put('Q',"00DCDC"); colorAA.put('G',"EBEBEB"); colorAA.put('V',"0F820F"); colorAA.put('I',"0F820F"); colorAA.put('L',"0F820F"); colorAA.put('A',"C8C8C8"); colorAA.put('W',"B45AB4"); colorAA.put('H',"8282D2"); colorAA.put('P',"DC9682"); 
		colorNucl.put('A',"145AFF");colorNucl.put('T',"E6E600");colorNucl.put('C',"0F820F");colorNucl.put('G',"E60A0A");
		geneticCode=Input_geneticCode;
		Sequences=Input_Sequences;
		nuclLen = Sequences.get(Sequences.keySet().toArray()[0]).length();
		codLen = nuclLen / 3;
		String species ;
		String seq ;
		int charCounter = 0;
		int posCounter = 0;
		String codon = "";
		String[] codArray = new String[codLen];
		char[] aaArray = new char[codLen];
		for (HashMap.Entry<String, String> entry : Sequences.entrySet()) {
			//System.out.println("OK entering loop");
		    species = entry.getKey();
		    seq = entry.getValue();
		    //System.out.println("ENTRY:"+species+"="+seq);
		    nuclText.put(species,seq.toCharArray());
		    //System.out.println("ENTRY_length:"+nuclText.get(species).length);
		    for ( char nucleotid : nuclText.get(species) ){
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
		    	System.out.println("WTF");
		    }
		    codon="";
		    charCounter=0;
		    posCounter=0;
		    aaText.put(species, aaArray.clone());
		    codText.put(species, codArray.clone());
		}
	}
	public void format(String colType,Species species){
		/**
		 * Pre-build the HTML code to print colored sequences aligned.
		 * @param colType type of coloration needed: limited to three choice "Nucleotids", "Amino acids" and "Codons" 
		 * @param species Background and Foreground species names and order in specis class
		 */
		//String formattedAlignment = "<html><div style=\"font-family: 'Lucida Console', Courier, monospace;font-size:13px;\">";
		if (colType=="Nucleotids"){
			htmlTextMap = new tools.htmlBlock(nuclLen);
			//System.out.println("OK entering the if statement");
			htmlTextMap.addALNChar(species, nuclText, colorNucl);
		}else if (colType=="Amino acids"){
			htmlTextMap = new tools.htmlBlock(codLen);
			htmlTextMap.addALNChar(species, aaText, colorAA);
		} if (colType=="Codons"){
			htmlTextMap = new tools.htmlBlock(codLen);
			htmlTextMap.addALNString(species, codText, geneticCode.getColorCodons(colorAA));
		}
	}
	public tools.htmlBlock getHtmlBlock(){
		return(htmlTextMap);
	}
	public int getHeight(){
		double height = htmlTextMap.getNLine()*myFONT.fontHeight;
		return((int)height+1);
	}
	public int getSize(){
		return(htmlTextMap.getNCol());
	}
}

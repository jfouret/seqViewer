/**
 * 
 */
package alignment;
import java.util.*;
import evolution.Species;
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
	
	public void buildHTML(String seqType,Species species) {
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
		ArrayList<String> backgroundList = species.getBackground();
		ArrayList<String> foregroundList = species.getForeground();
		String letter;
		String color;
		//System.out.println("OK entering the if statement");
		StringBuilder builder = new StringBuilder();
		builder.append("<html><span >");
		for (String backgroundSpecies: backgroundList ) {
			//System.out.println("OK entering the for statement on species");
			for ( int i = 0; i < letterText.get(backgroundSpecies).length ; i+=1){	
				//System.out.println("OK entering the for statement on letter");
				letter=letterText.get(backgroundSpecies)[i];
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
		for (String foregroundSpecies: foregroundList ) {
			//System.out.println("OK entering the for statement on species");
			//for (Character letter : letterText.get(backgroundSpecies) ){
			for (int i = 0; i < letterText.get(foregroundSpecies).length ; i+=1){	
				//System.out.println("OK entering the for statement on letter");
				letter=letterText.get(foregroundSpecies)[i];
				if (seqType=="Codons") {
					color=letterColor.get(geneticCode.translate(letter));
				}else {
					color=letterColor.get(letter);
				}
				builder.append("<u><b style=\"background-color: #"+color+";\">"+letter+"</b></u>");
				//System.out.println(letter+"formatted as : "+formatHTML[i]);
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

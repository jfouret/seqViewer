/**
 * 
 */
package alignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author julien.fouret
 *
 */
public class genCode {
	
	
	
	private char[] aa;
	private char[] base1;
	private char[] base2;
	private char[] base3;
	
	private HashMap<Character,char[]> iupac2nucl ;
	
	
	/**
	 * 
	 */
	
	
	public genCode(String codeType) {

		if (codeType=="standard"){
			aa="FFLLSSSSYY**CC*WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG".toCharArray();
			base1="TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG".toCharArray();
			base2="TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG".toCharArray();
			base3="TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG".toCharArray();
		}
		iupac2nucl = new HashMap<Character,char[]>();
		iupac2nucl.put('R',"AG".toCharArray());
		iupac2nucl.put('Y',"CT".toCharArray());
		iupac2nucl.put('S',"GC".toCharArray());
		iupac2nucl.put('W',"AT".toCharArray());
		iupac2nucl.put('K',"GT".toCharArray());
		iupac2nucl.put('M',"AC".toCharArray());
	}
	
	
	
	public HashMap<String,String> getColorCodons(HashMap<String, String> colorAA){
		HashMap<String,String> colorCode= new HashMap<String,String>();
		String codon;
		
		for ( int i=0 ; i<64 ; i+=1 ){
			codon=""+base1[i]+base2[i]+base3[i];
			colorCode.put(codon, colorAA.get(this.translate(codon)));
		}
		colorCode.put("!",colorAA.get("!"));
		colorCode.put("-",colorAA.get("-"));
		return(colorCode);
	}
	
	
	
	private String translate_1aa(String codon){

		int i=0 ;
		char[] codInChar=codon.toCharArray();
		
		while ( (i<64) && (!((base1[i]==codInChar[0]) && (base2[i]==codInChar[1]) && (base3[i]==codInChar[2]))) ){
			i+=1;
		}
		
		if (i==64){
			if (('!'==codInChar[0]) || ('!'==codInChar[1]) || ('!'==codInChar[2])){
				return("!");
			}else{
				return("-");
			}
		}else{
			char[] aaa=new char[1];
			aaa[0]=aa[i];
			return(new String(aaa));
		}
	}
	
	public String translate(String codon){

		char[] codInChar=codon.toCharArray();
		ArrayList<String> codons = new ArrayList<String>();
		ArrayList<String> newCodons;
		
		codons.add("");
		for (int i=0;i<3;i++){
			newCodons = new ArrayList<String>();
			if (this.iupac2nucl.containsKey(codInChar[i])){
				for (int j=0; j< codons.size();j++){
					newCodons.add(codons.get(j)+iupac2nucl.get(codInChar[i])[0]);
					newCodons.add(codons.get(j)+iupac2nucl.get(codInChar[i])[1]);
				}
			}else{
				for (int j=0; j< codons.size();j++){
					newCodons.add(codons.get(j)+codInChar[i]);
				}
			}
			codons=newCodons;
		}

		String[] aa= new String[codons.size()];
		
		for (int j=0; j< codons.size();j++){
			aa[j]=this.translate_1aa(codons.get(j));
		}
		
		String[] unique = Arrays.stream(aa).distinct().toArray(String[]::new);
		
		String final_aa;
		
		if (unique.length==1){
			final_aa=unique[0];
		}else{
			final_aa="X";
		}
		
		return final_aa;

	}
	
	
	
	
}

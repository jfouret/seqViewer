/**
 * 
 */
package alignment;

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
	
	public String translate(String codon){
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
}

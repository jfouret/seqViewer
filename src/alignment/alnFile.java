/**
 * 
 */
package alignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;  
import java.util.HashMap;

/**
 * @author julien.fouret
 *
 */

public class alnFile extends File {
	/**
	 * @param arg0 alignment File (.fasta format)
	 */
	private static final long serialVersionUID = -6361268453956756191L;

	
	public alnFile(String arg0) {
		super(arg0);
		// constructor stub
	}
	public HashMap<String,String> readALN() throws FileNotFoundException{
		/**
		 * @return name of the imported sequence as a key with the sequence as a value
		 */
		Scanner sc = new Scanner(this);
		HashMap<String,String> alignments = new HashMap<String,String>();
		String currentLine=sc.nextLine().trim();
		String Key = currentLine.substring(1);
		currentLine=sc.nextLine().trim();
		String Value = currentLine;
		while(sc.hasNext()){
			currentLine=sc.nextLine().trim();
			//System.out.println(currentLine);
			if (currentLine.startsWith(">")){
				//System.out.println("OK:"+Key+"="+Value);
				alignments.put(Key,Value);
				Key = currentLine.substring(1);
				Value = "";
			}else{
				Value += currentLine;
			}
		}
		alignments.put(Key,Value);
		sc.close();
		return(alignments);
	}
}







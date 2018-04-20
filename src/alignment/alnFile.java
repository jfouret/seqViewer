/**
 * 
 */
package alignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.HashMap;

/**
 * @author julien.fouret
 *
 */

public class alnFile{
	private BufferedReader bf;
	
	public alnFile(BufferedReader in_bf) {
		bf=in_bf;
	}
	public HashMap<String,String> readALN() throws FileNotFoundException{
		/**
		 * @return name of the imported sequence as a key with the sequence as a value
		 */
		
		HashMap<String,String> alignments = new HashMap<String,String>();
		String currentLine;
		try {
			currentLine = bf.readLine().trim();
			
			String Key = currentLine.substring(1);
			currentLine=bf.readLine().trim();
			String Value = "";
			while(currentLine!=null){
				currentLine=currentLine.trim();
				//System.out.println(currentLine);
				if (currentLine.startsWith(">")){
					//System.out.println("OK:"+Key+"="+Value);
					alignments.put(Key,Value);
					Key = currentLine.substring(1);
					Value = "";
				}else{
					Value += currentLine;
				}
				currentLine=bf.readLine();
			}
			alignments.put(Key,Value);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return(alignments);
	}
}







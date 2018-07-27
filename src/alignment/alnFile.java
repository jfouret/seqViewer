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







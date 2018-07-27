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

package evolution;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class BedFile {
	
	private BedEntry[] bedEntries;
	
	public BedFile(BufferedReader bf) {
		try {
			String currentLine=bf.readLine();
			ArrayList<BedEntry> bedList =new  ArrayList<BedEntry>();
			while(currentLine!=null){
				if (currentLine.startsWith("#")){
					currentLine=bf.readLine(); // pass if comment
				}else{
					currentLine=currentLine.trim();
					bedList.add(new BedEntry(currentLine)); 
					currentLine=bf.readLine();					
					}
				}
			bedEntries=bedList.toArray(new BedEntry[bedList.size()]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public BedEntry get(int i){
		return(this.bedEntries[i]);
	}
	
	public long length(){
		return(this.bedEntries.length);
	}
	
}

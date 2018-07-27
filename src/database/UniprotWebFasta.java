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

package database;

import java.net.*;
import java.io.*;


public class UniprotWebFasta{
	
	private String seq;
	
	public String getSeq(){
		return seq;
	}
	
	public UniprotWebFasta(String arg0) throws IOException {
		
        URL uniprotURL = new URL(arg0);
        BufferedReader in = new BufferedReader(new InputStreamReader(uniprotURL.openStream()));
        StringBuilder seqBuild = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null){
        	if (!inputLine.startsWith(">")){
        		seqBuild.append(inputLine.trim());
        	}
        }
        in.close();
        seq=seqBuild.toString();
	}
}

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

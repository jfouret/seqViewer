package alignment;

import java.util.ArrayList;
import java.util.HashMap;

public class htmlBlock {
	private int nCol ;
	private int nLine = 0; 
	private HashMap<String,String[]> arrayHTML;
	
	public int getNLine(){
		return(nLine);
	}
	
	public int getNCol(){
		return(nCol);
	}
	
	public htmlBlock(int Input_nCol) {
		nCol=Input_nCol;
		arrayHTML = new HashMap<String,String[]>();
	}
	
	public String getHTMLSpecies(evolution.Species species){
		ArrayList<String> backgroundList = species.getBackground();
		ArrayList<String> foregroundList = species.getForeground();
		StringBuilder builder = new StringBuilder();
		builder.append("<html><span style=\"text-align: right;\">");
		for (String s: backgroundList ) {
			builder.append("<b>"+s+tools.myCST.STARTARROW+"</b><br>");
		}
		for (String s: foregroundList ) {
			builder.append("<b style=\"color : red;\">"+s+tools.myCST.STARTARROW+"</b><br>");
		}
		builder.append("</span></html>");
		return(builder.toString());
	}
	
	public String getHTML(int start,int size,evolution.Species species){
		ArrayList<String> backgroundList = species.getBackground();
		ArrayList<String> foregroundList = species.getForeground();
		ArrayList<String> speciesList=new ArrayList<String>();
		speciesList.addAll(backgroundList); // add first arraylist
		speciesList.addAll(foregroundList); // add Second arraylist
		StringBuilder builder = new StringBuilder();
		builder.append("<html><span >");
		for (String s: speciesList ) {
		//for (HashMap.Entry<String, String[]> entry : arrayHTML.entrySet()) {
			//System.out.println("SPECIES:"+entry.getKey());
		    String[] htmlArray = arrayHTML.get(s);
		    for (int i=start;(i<htmlArray.length)&&(i<=start+size);i+=1){
			    builder.append(htmlArray[i]);
			}
		    builder.append("<br>");
		}
		builder.append("</span></html>");
		return(builder.toString());
	}
	
	public String getHTML(evolution.Species species){
		ArrayList<String> backgroundList = species.getBackground();
		ArrayList<String> foregroundList = species.getForeground();
		ArrayList<String> speciesList=new ArrayList<String>();
		speciesList.addAll(backgroundList); // add first arraylist
		speciesList.addAll(foregroundList); // add Second arraylist
		StringBuilder builder = new StringBuilder();
		builder.append("<html><span >");
		for (String s: speciesList ) {
		//for (HashMap.Entry<String, String[]> entry : arrayHTML.entrySet()) {
			//System.out.println("SPECIES:"+entry.getKey());
		    String[] htmlArray = arrayHTML.get(s);
		    for (int i=0;(i<htmlArray.length);i+=1){
			    builder.append(htmlArray[i]);
			}
		    builder.append("<br>");
		}
		builder.append("</span></html>");
		return(builder.toString());
	}
	
	public void addALNChar(evolution.Species species, HashMap<String,char[]> letterText, HashMap<Character,String> letterColor ){
		// When sequences are stored in char
		ArrayList<String> backgroundList = species.getBackground();
		ArrayList<String> foregroundList = species.getForeground();
		Character letter;
		String[] formatHTML=new String[nCol];
		//System.out.println("OK entering the if statement");
		for (String backgroundSpecies: backgroundList ) {
			nLine+=1;
			//System.out.println("OK entering the for statement on species");
			for ( int i = 0; i < letterText.get(backgroundSpecies).length ; i+=1){	
				//System.out.println("OK entering the for statement on letter");
				letter=letterText.get(backgroundSpecies)[i];
				formatHTML[i]="<b style=\"background-color: #"+letterColor.get(letter)+";\">"+letter+"</b>";
				//System.out.println(letter+" formatted as : "+formatHTML[i]);
			}
			arrayHTML.put(backgroundSpecies, formatHTML.clone());
		}
		for (String foregroundSpecies: foregroundList ) {
			nLine+=1;
			//System.out.println("OK entering the for statement on species");
			//for (Character letter : letterText.get(backgroundSpecies) ){
			for (int i = 0; i < letterText.get(foregroundSpecies).length ; i+=1){	
				//System.out.println("OK entering the for statement on letter");
				letter=letterText.get(foregroundSpecies)[i];
				formatHTML[i]="<u><b style=\"background-color: #"+letterColor.get(letter)+";\">"+letter+"</b></u>";
				//System.out.println(letter+"formatted as : "+formatHTML[i]);
			}
			arrayHTML.put(foregroundSpecies, formatHTML.clone());
		}
		System.out.println(nLine);
	}
	public void addALNString(evolution.Species species, HashMap<String,String[]> letterText, HashMap<String,String> letterColor ){
		// When sequences are stored in char
		ArrayList<String> backgroundList = species.getBackground();
		ArrayList<String> foregroundList = species.getForeground();
		String letter;
		String[] formatHTML=new String[nCol];
		//System.out.println("OK entering the if statement");
		for (String backgroundSpecies: backgroundList ) {
			nLine+=1;
			//System.out.println("OK entering the for statement on species");
			//for (Character letter : letterText.get(backgroundSpecies) ){
			for ( int i = 0; i < letterText.get(backgroundSpecies).length ; i+=1){	
				//System.out.println("OK entering the for statement on letter");
				letter=letterText.get(backgroundSpecies)[i];
				formatHTML[i]="<b style=\"background-color: #"+letterColor.get(letter)+";\">"+letter+"</b>";
				//System.out.println(letter+"formatted as : "+formatHTML[i]);
			}
			arrayHTML.put(backgroundSpecies, formatHTML.clone());
		}
		for (String foregroundSpecies: foregroundList ) {
			nLine+=1;
			//System.out.println("OK entering the for statement on species");
			//for (Character letter : letterText.get(backgroundSpecies) ){
			for (int i = 0; i < letterText.get(foregroundSpecies).length ; i+=1){	
				//System.out.println("OK entering the for statement on letter");
				letter=letterText.get(foregroundSpecies)[i];
				formatHTML[i]="<u><b style=\"background-color: #"+letterColor.get(letter)+";\">"+letter+"</b></u>";
				//System.out.println(letter+"formatted as : "+formatHTML[i]);
			}
			arrayHTML.put(foregroundSpecies, formatHTML.clone());
		}
	}
}

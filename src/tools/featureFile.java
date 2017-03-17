package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class featureFile extends File {
	
	/**
	 * 
	 */
	private int iterNum;
	private static final long serialVersionUID = 4097500482038573748L;
	public featureType featType= new featureType();
	public HashMap<String,Boolean> availableType  = new HashMap<String, Boolean>();
	public feature[] featureArray;
	
	public featureFile(String arg0,positions positions) throws FileNotFoundException {
		super(arg0);
		iterNum=0;
		Scanner sc = new Scanner(featureFile.this);
		//pass the first line
		String currentLine=sc.nextLine().trim();
		String[] split;
		ArrayList<feature> featureArrayList = new ArrayList<feature>();
		Object value = null;
		while(sc.hasNext()){
			currentLine=sc.nextLine().trim();
			split=currentLine.split("\t");
			//0=geneSymbol
			//1=start
			//2=end
			//3=class //id
			//4=type //description
			//5= OutOfRange ... not used ...
			// step 1 add in featureArrayList
			// System.out.println("LINE ==> "+currentLine);
			featureArrayList.add(new feature(split[3],Integer.parseInt(split[1]), Integer.parseInt(split[2]),split[4],featType,positions,iterNum));
			iterNum+=1; // num == position in feature array !!!
			// step 2 check in availableType is the type is present ? add if not with False
			value = availableType.get(split[3]);
			if (value == null) {
				availableType.put(split[3],false);
			}
		}
		// step final ==> arraylist in array
		featureArray= featureArrayList.toArray(new feature[featureArrayList.size()]);
		sc.close();
	}
	
	public String getHTML(int start,int size,String input_seq_type,int input_num){
		StringBuilder builder = new StringBuilder();
		builder.append("<html><div style=\"font-family: "+myFONT.getFontFamilly()+";font-size:"+myFONT.fontSize+";\">");
		
		feature feat=featureArray[input_num];
				builder.append(feat.getHTML_line(start,size,input_seq_type));
		builder.append("</div></html>");
		return(builder.toString());
	}
	public long getHeight(){
		return(featureArray.length*myFONT.fontHeight);
	}
	
	public String getToolTipText(Integer input_num){
		return(featureArray[input_num].getDescription());
	}
	
}



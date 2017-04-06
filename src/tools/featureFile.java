package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class featureFile {
	
	/**
	 * 
	 */
	private int iterNum;
	public featureType featType= new featureType();
	public HashMap<String,Boolean> availableType  = new HashMap<String, Boolean>();
	public feature[] featureArray;
	
	public featureFile(URL uniprotURL,positions positions) throws IOException {
		//super(arg0);
		
        BufferedReader in = new BufferedReader(new InputStreamReader(uniprotURL.openStream()));
		iterNum=0;
		String inputLine = in.readLine();
		String currentLine=inputLine.trim();
		String[] split;
		ArrayList<feature> featureArrayList = new ArrayList<feature>();
		Object value = null;
		while ((inputLine = in.readLine()) != null){
			currentLine=inputLine.trim();
			split=currentLine.split("  +");
			//0=FT (if)
			//1= class
			//2= start
			//3= end
			//4++= Descritpion 
			// step 1 add in featureArrayList
			// System.out.println("LINE ==> "+currentLine);
			if (split[0].equals("FT")){
				//System.out.println(currentLine+" class :"+split[1]);
				if (featType.isClass(split[1])){
					if (split[3].equals("?")){
						split[3]=split[2];
					}
					if (split.length==5){
						featureArrayList.add(new feature(split[1],Integer.parseInt(split[2])-1, Integer.parseInt(split[3]),split[4],featType,positions,iterNum));
					}else{
						featureArrayList.add(new feature(split[1],Integer.parseInt(split[2])-1, Integer.parseInt(split[3]),"",featType,positions,iterNum));
					}
					iterNum+=1; // num == position in feature array !!!
					// step 2 check in availableType is the type is present ? add if not with False
					value = availableType.get(split[1]);
					if (value == null) {
						availableType.put(split[1],false);
					}
				}else{
					featureArrayList.get(iterNum-1).appendDescription("<br>"+split[1]);
				}
			}
		}
		// step final ==> arraylist in array
		featureArray= featureArrayList.toArray(new feature[featureArrayList.size()]);
		in.close();
	}
	
	public featureFile(String arg0,positions positions) throws FileNotFoundException {
		//super(arg0);
		iterNum=0;
		File fileToRead = new File(arg0);
		Scanner sc = new Scanner(fileToRead);
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
		return("<html><b>"+featureArray[input_num].getType()+" : </b>"+featureArray[input_num].getDescription()+"</html>");
	}
	
}



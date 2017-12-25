package evolution;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class pamlFile extends File {
	/**
	 * 
	 */
	
	String[] selectedHTML;
	String[] selectedToolTip;
	public String currentToolTip = new String();
	private HashMap<Integer,Double> alnPosProb= new HashMap<Integer,Double>();
	private HashMap<Integer,String> alnPosRef= new HashMap<Integer,String>();
	
	private static final long serialVersionUID = 1361747962180404066L;
	
	public pamlFile(String arg0,evolution.positions positions) throws FileNotFoundException {
		super(arg0);
		int blockPos;
		String[] split;
		// TODO Auto-generated constructor stub
		Scanner sc = new Scanner(this);
		boolean BEBTable=false;
		String currentLine=sc.nextLine().trim();
		while(sc.hasNext()){
			currentLine=sc.nextLine().trim();
			if (BEBTable){
				if (currentLine.length()==0){
					break;
				}else{
					split=currentLine.split(" ");
					//0=> pos
					//2=> prob
					blockPos=(Integer.parseInt(split[0])-1)*3;
					//System.out.println("OK parsing the line : "+currentLine);
					//System.out.println("blockPos:"+blockPos+" ==> alnPos:"+positions.getPosFromBlock(blockPos));
					alnPosRef.put(positions.getPosFromBlock(blockPos),positions.getRefFromAln(positions.getPosFromBlock(Integer.parseInt(split[0]))));
					alnPosRef.put(positions.getPosFromBlock(blockPos)+1,positions.getRefFromAln(positions.getPosFromBlock(Integer.parseInt(split[0]))));
					alnPosRef.put(positions.getPosFromBlock(blockPos)+2,positions.getRefFromAln(positions.getPosFromBlock(Integer.parseInt(split[0]))));
					alnPosProb.put(positions.getPosFromBlock(blockPos),Double.parseDouble(split[2].substring(0, 4))*100);
					alnPosProb.put(positions.getPosFromBlock(blockPos)+1,Double.parseDouble(split[2].substring(0, 4))*100);
					alnPosProb.put(positions.getPosFromBlock(blockPos)+2,Double.parseDouble(split[2].substring(0, 4))*100);
				}
			}else if (currentLine.startsWith("Bayes Empirical Bayes (BEB) analysis ")){
				currentLine=sc.nextLine().trim();
				if (currentLine.startsWith("Positive sites for foreground lineages")){
					BEBTable=true;
				}
			}
		}
		sc.close();
	}

	public String buildHTML(int input_alnLen,String seqType){
		int alnLen;
		int iter;
		if (seqType=="Codons") {
			alnLen=3*input_alnLen;
			iter=1;
		}else if (seqType=="Amino acids") {
			alnLen=3*input_alnLen;
			iter=3;
		}else {
			alnLen=input_alnLen;
			iter=1;
		}
		System.out.println("size char : "+tools.myFONT.getWidth());
		selectedHTML= new String[alnLen];
		String color;
		Double Prob;	
		StringBuilder builder = new StringBuilder();
		builder.append("<html><span style=\"text-align: right;\">");
		for (int i=0;(i<alnLen);i+=iter){
			if (alnPosProb.keySet().contains(i)){
				Prob=alnPosProb.get(i);
				if (Prob>50 && Prob<80){
					color="green";
				} else if(Prob>80 && Prob<90){
					color="orange";
				}else if(Prob>90){
					color="red";
				}else{
					color="grey";
				}
				;
				builder.append("<b style=\"color: "+color+"\">"+tools.myCST.FILLARROW+"</b>");
			}else{
				color="white";
				builder.append("<b style=\"color: "+color+"\">"+tools.myCST.BLOCK+"</b>");
			}
		}
		builder.append("FIN</span></html>");
		return(builder.toString());
	}
}

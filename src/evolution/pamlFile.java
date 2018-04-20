package evolution;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class pamlFile {
	/**
	 * 
	 */
	
	String[] selectedHTML;
	String[] selectedToolTip;
	public String currentToolTip = new String();
	private HashMap<Integer,Integer> alnPosProb= new HashMap<Integer,Integer>();
	private HashMap<Integer,String> alnPosRef= new HashMap<Integer,String>();
		
	public pamlFile(BufferedReader bf,evolution.positions positions) throws IOException {
		int blockPos;
		int blockProb;
		int blockProb_2nd;
		int blockProb_3rd;
		String blockRef;
		String[] split;
		boolean BEBTable=false;
		String currentLine=bf.readLine();
		while(currentLine!=null){
			currentLine=currentLine.trim();
			if (BEBTable){
				if (currentLine.length()==0){
					break;
				}else{
					split=currentLine.split(" ");
					//1=> pos
					//4-5=> prob
					blockProb=(int) Math.floor((Double.parseDouble(split[6])*10)+(Double.parseDouble(split[7])*10));
					if (blockProb>0){
						blockPos=(Integer.parseInt(split[0])-1)*3;
						blockRef=positions.getRefFromAln(positions.getPosFromBlock(Integer.parseInt(split[0])));
						blockProb_2nd=(int) Math.floor((Double.parseDouble(split[6])*100)+(Double.parseDouble(split[7])*100))-10*blockProb;
						blockProb_3rd=(int) Math.floor((Double.parseDouble(split[6])*1000)+(Double.parseDouble(split[7])*1000))-10*blockProb_2nd-100*blockProb;
						alnPosRef.put(positions.getPosFromBlock(blockPos),blockRef);
						alnPosRef.put(positions.getPosFromBlock(blockPos)+1,blockRef);
						alnPosRef.put(positions.getPosFromBlock(blockPos)+2,blockRef);
						alnPosProb.put(positions.getPosFromBlock(blockPos),blockProb);
						alnPosProb.put(positions.getPosFromBlock(blockPos)+1,blockProb_2nd);
						alnPosProb.put(positions.getPosFromBlock(blockPos)+2,blockProb_3rd);
					}
				}
			}else if (currentLine.startsWith("Bayes Empirical Bayes (BEB) probabilities")){
				currentLine=bf.readLine();
				currentLine=bf.readLine();
				BEBTable=true;
			}
			currentLine=bf.readLine();
		}
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
		selectedHTML= new String[alnLen];
		int Prob;
		String color;
		StringBuilder builder = new StringBuilder();
		builder.append("<html><span style=\"text-align: right;\">");
		
		for (int i=0;(i<alnLen);i+=iter){
			if (alnPosProb.keySet().contains(i)){
				Prob=alnPosProb.get(i);
				color="black";
				builder.append("<b style=\"color: "+color+"\">"+Prob+"</b>");
			}else{
				color=tools.myCST.backColorString;
				builder.append("<b style=\"color: "+color+"\">"+tools.myCST.BLOCK+"</b>");
			}
		}
		builder.append("FIN</span></html>");
		return(builder.toString());
	}
}

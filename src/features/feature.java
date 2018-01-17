package features;

import java.util.Arrays;
import java.util.HashMap;

import evolution.positions;
import tools.myCST;

public class feature {
	private int Num ; //number id of the feature
	private String id;// id of the class
	private String id_desc;// id of the class description
	private String description;//description of uUniprot Field
	private String type;// type of uUniprot Field for representation
	private int start; //0-based start (ref & prot pos)
	private int end; //0-based end (ref & prot pos)
	private String[] HTML_built; //html enriched character 
	
	public int getNum(){
		return(Num);
	}
	
	public Boolean isSelected(HashMap<String,Boolean> availableType){
		return(availableType.get(id));
	}
	
	public String getDescription(){
		return(description); // 
	}
	
	public String getType(){
		return((id_desc)); // 
	}
	
	public void appendDescription(String toAppend){
		description=description+toAppend; // 
	}
	
	public feature(String Input_id, int in_start, int in_end,String input_desc, featureType featType, positions positions,int input_num){
		Num=input_num;
		id=Input_id;
		id_desc=featType.getDesc(Input_id);
		description=input_desc;
		type=featType.getType(Input_id);
		start=in_start;
		end=in_end-1; // from 0-based excluded to 0-based included
		int alnStart=(positions.getPosFromRef(start*3+1)-1); 
		int alnEnd=positions.getPosFromRef(end*3+1); // excluded
		HTML_built=new String[positions.getAlnSize()];
		Arrays.fill(HTML_built, "<b color=\""+myCST.backColorString+"\" >"+myCST.BLOCK+"</b>");
		switch (type) {
		case "interval":  
			if (id.equals("STRAND")) {
				for (int i=alnStart;i<=alnEnd+1;i+=1){
					HTML_built[i]="<b color=\""+featType.getCol(id)+"\">S</b>";
				}
			}else if (id.equals("HELIX")){
				for (int i=alnStart;i<=alnEnd+1;i+=1){
					HTML_built[i]="<b color=\""+featType.getCol(id)+"\">H</b>";
				}
			}else if (id.equals("TURN")){
				for (int i=alnStart;i<=alnEnd+1;i+=1){
					HTML_built[i]="<b color=\""+featType.getCol(id)+"\">T</b>";
				}
			}else {
				for (int i=alnStart;i<=alnEnd+1;i+=1){
					HTML_built[i]="<b color=\""+featType.getCol(id)+"\">"+myCST.BLOCK+"</b>";
				}
			}
			break;
		case "site":
			HTML_built[alnStart]="<b color=\""+featType.getCol(id)+"\">"+myCST.FILLARROW+"</b>";
        	HTML_built[alnStart+1]="<b color=\""+featType.getCol(id)+"\">"+myCST.FILLARROW+"</b>";
        	HTML_built[alnStart+2]="<b color=\""+featType.getCol(id)+"\">"+myCST.FILLARROW+"</b>";
        	for (int i=alnStart+3;i<alnEnd-1;i+=1){
				HTML_built[i]="<b color=\""+featType.getCol(id)+"\">"+myCST.FILLARROW+"</b>";
			}
        	HTML_built[alnEnd+1]="<b color=\""+featType.getCol(id)+"\">"+myCST.FILLARROW+"</b>";
        	HTML_built[alnEnd]="<b color=\""+featType.getCol(id)+"\">"+myCST.FILLARROW+"</b>";
        	HTML_built[alnEnd-1]="<b color=\""+featType.getCol(id)+"\">"+myCST.FILLARROW+"</b>";
        	break;
        case "2site":
        	HTML_built[alnStart]="<b color=\""+featType.getCol(id)+"\">"+myCST.BLOCK+"</b>";
        	HTML_built[alnStart+1]="<b color=\""+featType.getCol(id)+"\">"+myCST.BLOCK+"</b>";
        	HTML_built[alnStart+2]="<b color=\""+featType.getCol(id)+"\">"+myCST.BLOCK+"</b>";
        	for (int i=alnStart+3;i<alnEnd-1;i+=1){
				HTML_built[i]="<b color=\""+featType.getCol(id)+"\">"+myCST.LINE+"</b>";
			}
        	HTML_built[alnEnd+1]="<b color=\""+featType.getCol(id)+"\">"+myCST.BLOCK+"</b>";
        	HTML_built[alnEnd]="<b color=\""+featType.getCol(id)+"\">"+myCST.BLOCK+"</b>";
        	HTML_built[alnEnd-1]="<b color=\""+featType.getCol(id)+"\">"+myCST.BLOCK+"</b>";
        	break;
		}
	}
	
	public String getHTML_line(String input_seq_type){
		StringBuilder builder = new StringBuilder();
		builder.append("<html><span>");
		int alnLength=HTML_built.length;
		int iter = 0;
		switch (input_seq_type){
		case "Amino acids":
			iter=3;
			break;
		case "Codons":
			iter=1;
			break;
		case "Nucleotids":
			iter=1;
			break;
		}
		for (int i=0;i<alnLength;i+=iter){
			builder.append(HTML_built[i]);
		}
		builder.append("</span></html>");
		return(builder.toString());
	}
}



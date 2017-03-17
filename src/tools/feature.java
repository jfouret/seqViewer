package tools;

import java.util.Arrays;
import java.util.HashMap;

public class feature {
	private String id;// id of the class
	private String description;//description of uUniprot Field
	private String type;// type of uUniprot Field for representation
	private int start; //0-based start (ref & prot pos)
	private int end; //0-based end (ref & prot pos)
	private String[] HTML_built; //html enriched character 
	
	public Boolean isSelected(HashMap<String,Boolean> availableType){
		return(availableType.get(id));
	}
	
	public feature(String Input_id, int in_start, int in_end, featureType featType, positions positions){
		id=Input_id;
		description=featType.getDesc(id);
		type=featType.getType(Input_id);
		start=in_start;
		end=in_end-1; // from 0-based excluded to 0-based included
		int alnStart=(positions.getPosFromRef(start*3+1)-1); 
		int alnEnd=positions.getPosFromRef(end*3+1); // excluded
		HTML_built=new String[positions.getAlnSize()];
		Arrays.fill(HTML_built, "<b color=\"white\" >"+myCST.BLOCK+"</b>");
		switch (type) {
		case "interval":  
			for (int i=alnStart;i<=alnEnd+1;i+=1){
				HTML_built[i]="<b color=\""+featType.getCol(id)+"\">"+myCST.BLOCK+"</b>";
			}
			break;
		case "site":
			HTML_built[alnStart]="<b color=\""+featType.getCol(id)+"\">"+myCST.FILLARROW+"</b>";
			HTML_built[alnStart+1]="<b color=\""+featType.getCol(id)+"\">"+myCST.FILLARROW+"</b>";
			HTML_built[alnStart+2]="<b color=\""+featType.getCol(id)+"\">"+myCST.FILLARROW+"</b>";
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
	
	public String getHTML_line(int start, int size, String input_seq_type){
		StringBuilder builder = new StringBuilder();
		switch (input_seq_type){
		case "Amino acids":
			for (int i=start;i<=start+size+1;i+=1){
				builder.append(HTML_built[i*3]);
			}
			break;
		case "Codons":
			for (int i=start;i<=start+size;i+=1){
				builder.append(HTML_built[i]);
			}
			break;
		case "Nucleotids":
			for (int i=start;i<start+size+1;i+=1){
				builder.append(HTML_built[i]);
			}
			break;
		}
		return(builder.toString());
	}
}



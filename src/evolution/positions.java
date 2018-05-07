package evolution;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

public class positions {
	
	private String[] exons;
	private Integer[] ref2aln;
	private String[] aln2ref;
	private Integer[] block2aln;
	
	//public positions(String posPath,int Input_alnLen,String seqType) throws FileNotFoundException {
		
	public positions(BufferedReader exonBedBuffer,BufferedReader blockBedBuffer,String[] ref_dna) throws FileNotFoundException {	
		
		int alnLen=ref_dna.length;
		
		ArrayList<Integer> Tref2aln= new ArrayList<Integer>();
		int refPos=0;
		aln2ref=new String[alnLen];
		for (int i=0 ; i < ref_dna.length ; i++ ){
			if ( ref_dna[i].equals("-") | ref_dna[i]=="!" | ref_dna[i]=="*" | ref_dna[i]=="?"){
				aln2ref[i]=".";
			}else{
				Tref2aln.add(i);
				System.out.println(ref_dna[i]+"|"+refPos+"=ref2aln>"+i);
				aln2ref[i]=String.valueOf(refPos);
				refPos++;
			}
		}
		
		ref2aln=Tref2aln.toArray(new Integer[Tref2aln.size()]);
		
		BedFile exonBedFile=new BedFile(exonBedBuffer);
		int exonNum=1;
		exons=new String[alnLen];
		
		for (int i=0 ; i < exonBedFile.length() ; i++ ){
			for (int pos=ref2aln[exonBedFile.get(i).start] ; pos<ref2aln[exonBedFile.get(i).end-1]+1 ; pos++){
				
				exons[pos]=String.valueOf(exonNum);
			}
			exonNum++;
		}
				
		BedFile blockBedFile=new BedFile(blockBedBuffer); // WARNING BASED ON AMINO ACID
		ArrayList<Integer> block2aln_array=new ArrayList<Integer>();
		for (int i=0 ; i < blockBedFile.length() ; i++ ){
			for (int pos=blockBedFile.get(i).start*3 ; pos<blockBedFile.get(i).end*3 ; pos++){
				block2aln_array.add(pos);
			}
		}
		
		block2aln=block2aln_array.toArray(new Integer[block2aln_array.size()]);
	}
	
	// Default pos is aln
	public int getPosFromRef(int refPos){
		return(ref2aln[refPos]);
	}
	public String getRefFromAln(int alnPos){
		return(aln2ref[alnPos]);
	}
	public int getPosFromBlock(int blockPos){
		return(block2aln[blockPos]);
	}
	public int getAlnSize(){
		return(aln2ref.length);
	}
	public String[] getaln2ref(){
		return(aln2ref);
	}
	public String[] getExons(){
		return(exons);
	}
	
	public String buildHTML(String seqType){
		int iter;
		if (seqType=="Amino acids") {
			iter=3;
		}else {
			iter=1;
		}
		StringBuilder builder = new StringBuilder();
		builder.append("<html><span>");
		double test0;
		for (int i=0;i<exons.length;i+=iter){
			try {
				test0=Integer.parseInt(exons[i]) % 2;
				//System.out.println(test0);
				if (test0==0){
					//System.out.println("EVEN");
					builder.append("<b style=\"color: #A52A2A;\">"+tools.myCST.BLOCK+"</b>");
				}else{
					//System.out.println("ODD");
					builder.append("<b style=\"color: #DEB887;\">"+tools.myCST.BLOCK+"</b>");
				}
			}catch (java.lang.NumberFormatException e) {
					builder.append("<b style=\"color: #000000;\">"+tools.myCST.BLOCK+"</b>");
			}
		}
		builder.append("</span></html>");
		return builder.toString();
	}
	
	
	
	public String getPos(String seqType,int nCol) {
		int alnLen;
		String pos;
		String posToWrite;
		int fact;
		if (seqType=="Nucleotids"){
			alnLen=nCol;
			fact=1;
		}else if (seqType=="Amino acids"){
			alnLen=nCol*3;
			fact=3;
		} else {
			alnLen=nCol*3;
			fact=1;
		}
		StringBuilder builder=new StringBuilder();
		builder.append("1   .    ");
		for (int i = fact*10; i < alnLen ; i+=fact*10) {
			pos =aln2ref[i];
			if (pos.equals(".")) {
				builder.append("-    ");
			}else {
				posToWrite=String.valueOf((int)Integer.parseInt(pos)/fact);
				builder.append(posToWrite);
				builder.append(String.join("", Collections.nCopies(5-posToWrite.length(), " ")));
			}
			builder.append(".    ");
		}
		return builder.toString();
	}
	
}

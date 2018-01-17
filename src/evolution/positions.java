package evolution;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class positions {
	private String[] exons;
	private String[] refPos;
	private boolean[] Cblocks;
	private Integer[] ref2aln;
	private String[] aln2ref;
	private Integer[] block2aln;
	
	//foo.toArray(new Integer[.size()]);
	
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
			test0=Integer.parseInt(exons[i]) % 2;
			//System.out.println(test0);
			if (test0==0){
				//System.out.println("EVEN");
				builder.append("<b style=\"color: #A52A2A;\">"+tools.myCST.BLOCK+"</b>");
			}else{
				//System.out.println("ODD");
				builder.append("<b style=\"color: #DEB887;\">"+tools.myCST.BLOCK+"</b>");
			}
		}
		builder.append("</span></html>");
		return builder.toString();
	}
	
	public positions(String posPath,int Input_alnLen,String seqType) throws FileNotFoundException {
		int alnLen;
		if (seqType=="Nucleotids"){
			alnLen=Input_alnLen;
		}else{
			alnLen=Input_alnLen*3;
		}
		//work in 0-based ==> minus 1 everywhere 
		exons= new String[alnLen];
		refPos= new String[alnLen];
		Cblocks = new boolean[alnLen];
		aln2ref = new String[alnLen];
		ArrayList<Integer> Tref2aln= new ArrayList<Integer>();
		ArrayList<Integer> Tblock2aln= new ArrayList<Integer>();
		File posFile= new File(posPath);
		Scanner sc = new Scanner(posFile);
		//pass the first line
		String currentLine=sc.nextLine().trim();
		String[] split;
		
		int i=-1;
		while(sc.hasNext()){
			i+=1;
			currentLine=sc.nextLine().trim();
			split=currentLine.split("\t");
			//ref=0
			//aln=1
			//exon=2
			//block=3
			exons[i]=split[2];
			aln2ref[Integer.parseInt(split[1])-1]=split[0];
			refPos[i]=split[0];
			if (!split[3].startsWith(".")){
				Cblocks[i]=true;
				Tblock2aln.add(Integer.parseInt(split[1])-1);
			}else{
				Cblocks[i]=false;
			}
			if(!split[0].startsWith(".")){
				Tref2aln.add(Integer.parseInt(split[1])-1);
			}
		}
		ref2aln=Tref2aln.toArray(new Integer[Tref2aln.size()]);
		block2aln=Tblock2aln.toArray(new Integer[Tblock2aln.size()]);
		sc.close();
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
	public boolean[] getBlocks(){
		return(Cblocks);
	}
	
	public String getPos(String seqType,int nCol) {
		int alnLen;
		String pos;
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
			pos =aln2ref[i-1];
			if (pos.equals(".")) {
				builder.append("-    ");
			}else {
				builder.append(String.valueOf((int)Integer.parseInt(pos)/fact));
				builder.append(String.join("", Collections.nCopies(5-String.valueOf((int)Integer.parseInt(pos)/fact).length(), " ")));
			}
			builder.append(".    ");
		}
		return builder.toString();
	}
	
}

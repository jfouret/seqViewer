package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import tools.myFONT;

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
	
	public pamlFile(String arg0,tools.positions positions) throws FileNotFoundException {
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

	public HTMLEditorKit buildSelectedHTML(int Input_alnLen,String seqType){
		int alnLen;
		if (seqType=="Nucleotids"){
			alnLen=Input_alnLen;
		} else{
			alnLen=Input_alnLen*3;
		}
		HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
		StyleSheet styleSheet = new StyleSheet();
		styleSheet.addRule(".site {color: white;font-family: "+myFONT.fontFamilly+";font-size:"+myFONT.fontSize+";}");
		styleSheet.addRule(".site b span {display:none;}");
		styleSheet.addRule(".site b:hover span {display:inline; font-weight:normal;}");
		styleSheet.addRule(".site b:hover {display:inline;}</style>");
		htmlEditorKit.setStyleSheet(styleSheet);
		selectedHTML= new String[alnLen];
		selectedToolTip=new String[alnLen];
		String color;
		Double Prob;
		// stop attempts with the hover...
		//String Ref;
		for (int i=0;(i<alnLen);i+=1){
			//System.out.println("Aln pos:"+i+" in aa:"+(int)(i/3));
			if (alnPosProb.keySet().contains(i)){
				//System.out.println("OK entering the if");
				Prob=alnPosProb.get(i);
				// stop attempts with the hover...
				//Ref=alnPosRef.get((int)(i/3));
				selectedToolTip[i]="Aln pos:"+i+" in aa:"+(int)(i/3)+"| P="+Prob+"% <br>";
				if (Prob>50 && Prob<80){
					color="green";
				} else if(Prob>80 && Prob<90){
					color="orange";
				}else if(Prob>90){
					color="red";
				}else{
					color="grey";
				}
				selectedHTML[i]="<b style=\"color: "+color+"\">"+tools.myCST.FILLARROW+"</b>";
				//selectedHTML[i]="<b style=\"color: "+color+"\">"+"OK"+"</b>";
				// stop attempts with the hover...
				//selectedHTML[i]="<b style=\"color: "+color+"\">"+tools.myCST.FILLARROW+"<span style=\"color: black;\" >|alnPos:"+i+"|refPos:"+Ref+"|P:"+Prob.intValue()+"%|</span></b>";
			}else{
				selectedToolTip[i]="";
				selectedHTML[i]="<b>"+tools.myCST.BLOCK+"</b>";
			}
		}
	return(htmlEditorKit);
	}
	
	public HTMLDocument getSelectedHTML(int start,int size,String seqType,HTMLEditorKit htmlEditorKit){
		HTMLDocument htmlDocument;
		Element bodyElement;
		StringBuilder builder = new StringBuilder();
		StringBuilder toolTipBuilder = new StringBuilder();
		toolTipBuilder.append("<html>");
		builder.append("<div class=\"site\">");
		for (int i=start;(i<=start+size);i+=1){
			if (seqType=="Amino acids"){
				builder.append(selectedHTML[(int)(i)*3]);
				toolTipBuilder.append(selectedToolTip[(int)(i)*3]);
			}else if (seqType=="Codons") {
				toolTipBuilder.append(selectedToolTip[(int)(i)*3]);
				builder.append(selectedHTML[i*3]+selectedHTML[i*3+1]+selectedHTML[i*3+2]);
			}else {
				System.out.println(i+"multiple of 3 :"+(i % 3));
				if (i % 3 == 0){
					System.out.println("YES");
					toolTipBuilder.append(selectedToolTip[i]);
				}else{
					System.out.println("NO");
				}
				builder.append(selectedHTML[i]);
			}
		}
		builder.append("</div>");
		toolTipBuilder.append("</html>");
		//System.out.println(builder.toString());
		currentToolTip=toolTipBuilder.toString();
		htmlDocument = (HTMLDocument) htmlEditorKit.createDefaultDocument();
	    try {
	      Element htmlElement = htmlDocument.getRootElements()[0];
	      bodyElement = htmlElement.getElement(0);
	      Element contentElement = bodyElement.getElement(bodyElement
	    	        .getElementCount() - 1);
	      StringBuffer sbHtml = new StringBuffer();
	      sbHtml.append(builder.toString());
	      htmlDocument.insertBeforeEnd(contentElement, sbHtml.toString());
	    } catch (Exception e) {
	      e.printStackTrace();
	  }
		return(htmlDocument);
	}
	
	

}

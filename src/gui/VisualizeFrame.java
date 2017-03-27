package gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.JSlider;
import javax.swing.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.text.html.HTMLEditorKit;
import tools.feature;
import tools.myFONT;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.JTextField;
import java.awt.Font;

public class VisualizeFrame extends JFrame {
	
	private tools.Alignment aln;
	private String seqType;
	private String frametitle;
	private tools.Species species;
	private tools.featureFile featureFile;
	
	tools.positions positions;
	// private String pamlPath= "./out";
	// private String SpeciesPath= "./species.txt";
	// private String alnPath= "./codon_aln.fa";
	// private String positionsPath= "./posDict.tab";
	tools.pamlFile selection;
	private int iterPos;
	private int viewSize=61;
	private int start=0;
	private int legendSize=110;
	private int startFeature;
	private HTMLEditorKit htmlEditorKit_selection = new HTMLEditorKit();
	
	// Dimensions
	int windowWidth;
	int windowHeight;
	int ctrlWidth;
	int ctrlHeight;
	int AvailViewSize;
	int numFT;
	int maxFTbyPage;
	int totPage;
	int numPage=1;
	int heightFTPane;
	
	private static final long serialVersionUID = 3034391181248326868L;
	private JPanel contentPane;
	private JPanel FeaturePane;
	private JTextPane txtpnAln;
	private HashMap<Integer,JTextPane> HashUniprot;
	//private JTextPane txtpnUniprot;
	private JTextPane txtpnAlnSpecies;
	private JTextPane txtpnRefPos;
	private JTextPane txtpnSelection;
	private JTextPane txtpnlegendSelection;
	private JSlider slider;
	private Label slideEndLab;
	private Label slideStartLab;
	private gui.featureSelect featSelect;
	private JButton btnUpdateUniprot ;
	private JTextField txtFeaturePageOn = new JTextField();
	
	//private String GENETEST;
	
	/**
	 * set dynamic change to the frame
	 */
	
	public void updateFT() {
  	  featureFile.availableType=featSelect.UpdateAvailableType();
  	  // remove all JTextPane
  	  for (JTextPane featTextPaneToRemove : HashUniprot.values()){
  		  gui.VisualizeFrame.this.FeaturePane.remove(featTextPaneToRemove);
  	  }
  	  // re-initiate HashUniprot
  	  HashUniprot= new HashMap<Integer,JTextPane>();
  	  // 
  	  for (feature featToAdd: featureFile.featureArray){
  		  if (featToAdd.isSelected(featureFile.availableType)){
  			  HashUniprot.put(featToAdd.getNum(), new JTextPane());
  			  HashUniprot.get(featToAdd.getNum()).setBorder(new TitledBorder(null,"", TitledBorder.LEADING, TitledBorder.TOP, null, null));
  			  HashUniprot.get(featToAdd.getNum()).setContentType("text/html");
  			  HashUniprot.get(featToAdd.getNum()).setBackground(Color.WHITE);
  		  }
  	  }
  	  //numFT=HashUniprot.size();
  	  //FeaturePane = new JPanel();
	  FeaturePane.repaint();
	  //FeaturePane.setBorder(new EmptyBorder(0, 0, 0, 0));
  	  update_size();
  	  int iterFTNum=1;
  	  gui.VisualizeFrame.this.contentPane.remove(FeaturePane);
  	  for (JTextPane txtPaneToAdd: HashUniprot.values()){
  		  if ((iterFTNum>(numPage-1)*maxFTbyPage)&&(iterFTNum<=(numPage)*maxFTbyPage)){
  			  gui.VisualizeFrame.this.FeaturePane.add(txtPaneToAdd);
  		  }
  		  iterFTNum++;
  	  }
  	  gui.VisualizeFrame.this.contentPane.add(FeaturePane);
    }
	
	public void setPageFT(int input_numFT){
		numFT=input_numFT;
		maxFTbyPage=(int) (heightFTPane/(myFONT.getHeight()+10));
		totPage=(numFT/maxFTbyPage)+1;
		txtFeaturePageOn.setText("Features: page "+numPage+"/"+totPage+" ("+maxFTbyPage+" FT/page) ");
	}
	
	
	
	public void Set_txtpnAln_Start(int slide_start){
		start=slide_start;
		update_slider();
	}
	
	public void update_slider(){
		if  (seqType=="Codons"){
			txtpnRefPos.setText(positions.getExonHTML(start, (int)(viewSize/3)-1,seqType));
			txtpnAln.setText(aln.getHtmlBlock().getHTML(start, (int)(viewSize/3)-1,species));
			txtpnSelection.setDocument(selection.getSelectedHTML(start, (int)(viewSize/3)-1, seqType,htmlEditorKit_selection));
			txtpnSelection.setToolTipText(selection.currentToolTip);
			//txtpnUniprot.setText(featureFile.getHTML(3*start, 3*(int)(viewSize/3)-1, seqType));
			for (Integer NumTxtPaneToAdd: HashUniprot.keySet()){
				HashUniprot.get(NumTxtPaneToAdd).setText(featureFile.getHTML(3*start, 3*(int)(viewSize/3)-1, seqType,NumTxtPaneToAdd));
				HashUniprot.get(NumTxtPaneToAdd).setToolTipText(featureFile.getToolTipText(NumTxtPaneToAdd));
			}
			slideStartLab.setText("Start "+(start+1));
			slideEndLab.setText("End "+(start+(int)(viewSize/3)));
		}else{
			txtpnRefPos.setText(positions.getExonHTML(start, viewSize,seqType));
			txtpnAln.setText(aln.getHtmlBlock().getHTML(start, viewSize,species));
			txtpnSelection.setToolTipText(selection.currentToolTip);
			txtpnSelection.setDocument(selection.getSelectedHTML(start, viewSize, seqType,htmlEditorKit_selection));
			//txtpnUniprot.setText(featureFile.getHTML(start, viewSize, seqType));
			if (seqType.equals("Amino acids")){
				for (Integer NumTxtPaneToAdd: HashUniprot.keySet()){
					HashUniprot.get(NumTxtPaneToAdd).setText(featureFile.getHTML(start, viewSize-1, seqType,NumTxtPaneToAdd));
					HashUniprot.get(NumTxtPaneToAdd).setToolTipText(featureFile.getToolTipText(NumTxtPaneToAdd));
				}
			}else{
				for (Integer NumTxtPaneToAdd: HashUniprot.keySet()){
					HashUniprot.get(NumTxtPaneToAdd).setText(featureFile.getHTML(start, viewSize, seqType,NumTxtPaneToAdd));
					HashUniprot.get(NumTxtPaneToAdd).setToolTipText(featureFile.getToolTipText(NumTxtPaneToAdd));
				}
			}
			
			slideStartLab.setText("Start "+(start+1));
			slideEndLab.setText("End "+(start+viewSize+1));
		}
	}
	
	public void update_size(){
		windowWidth=this.getWidth();
		windowHeight=this.getHeight();
		ctrlWidth=windowWidth-10;
		ctrlHeight=windowHeight-10;
		slideEndLab.setBounds(legendSize+285, 10, 80, 20);
		slideStartLab.setBounds(legendSize+5, 10, 80, 20);
		slider.setBounds(legendSize+85, 10, 200, 20);
		btnUpdateUniprot.setBounds(510, 10, 229, 23);
		AvailViewSize=ctrlWidth-15-legendSize;
		viewSize=(int)(AvailViewSize/myFONT.getWidth());
		//viewSize=(int)(AvailViewSize/(Long.parseLong(GENETEST)));
		Integer alnWidth=txtpnAln.getPreferredSize().width;
		txtpnRefPos.setBounds(legendSize+5, 60, alnWidth, 20);
		txtpnAlnSpecies.setBounds(5, 80, legendSize-5, aln.getHeight());
		//txtpnAln.setBounds(legendSize+5, 80, (int) (viewSize*myFONT.getWidth()), aln.getHeight());
		//txtpnAln.setBounds(legendSize+5, 80, (int) (viewSize*(Long.parseLong(GENETEST))), aln.getHeight());
		txtpnAln.setBounds(legendSize+5, 80,alnWidth , aln.getHeight());
		txtpnlegendSelection.setBounds(5,80+aln.getHeight(),legendSize-5,20);
		txtpnSelection.setBounds(legendSize+5, 80+aln.getHeight(), alnWidth, 20);
		//txtpnUniprot.setBounds();
		iterPos=0;
		startFeature=(int) (80+aln.getHeight()+myFONT.getHeight())+1;
		FeaturePane.setBounds(legendSize,startFeature,alnWidth+10,(int) (HashUniprot.size()*(10.0+myFONT.getHeight())));
		int iterFTNum=1;
		heightFTPane=ctrlHeight-startFeature-10;
		this.setPageFT(HashUniprot.size());
		for (Integer NumTxtPaneToAdd: HashUniprot.keySet()){
			if ((iterFTNum>(numPage-1)*maxFTbyPage)&&(iterFTNum<=(numPage)*maxFTbyPage)){
				HashUniprot.get(NumTxtPaneToAdd).setBounds(legendSize+5, (int) (iterPos*(myFONT.getHeight())), alnWidth+10, (int) (0+myFONT.getHeight()));
				iterPos+=1;
			}
			iterFTNum++;
		}
		if (seqType=="Codons"){
			slider.setMaximum(aln.getSize()-(int)(viewSize/3));
		}else{
			slider.setMaximum(aln.getSize()-viewSize-1);
		}
		update_slider();
	}
	
	public void changeAvailableType(String type_id, Boolean boxChecked){
		featureFile.availableType.replace(type_id, boxChecked);
	}
	
	/**
	 * Create the frame.
	 */
	
	public VisualizeFrame(String alignmentPath, String speciesPath,String pamlPath,String positionsPath,String uniprotPath,tools.genCode genCode,String input_seqType,String geneName, String UniprotID) throws FileNotFoundException {
		tools.UniprotWebFasta webFasta;	
		frametitle=geneName + " - " + input_seqType;
		//GENETEST=geneName;
		setTitle(frametitle);
		seqType=input_seqType;
		tools.alnFile alnFile= new tools.alnFile(alignmentPath);
		aln= new tools.Alignment(alnFile.readALN(),genCode);
		try {
			webFasta = new tools.UniprotWebFasta("http://www.uniprot.org/uniprot/"+UniprotID+".fasta");
			//System.out.println(aln.getSeqRef());
			//System.out.println(webFasta.getSeq());
			if (!aln.getSeqRef().startsWith(webFasta.getSeq())){
				String errMessage = "\"BE CAREFULL\"\n"
	    		        + "The Uniprot Sequence does not match with the one in the alignment\n"
	    		        + "Uniprot datas are likely to be false! ;-) ";
	    		    JOptionPane.showMessageDialog(new JFrame(), errMessage, "Database Error",
	    		        JOptionPane.ERROR_MESSAGE);
			}
		} catch (IOException e) {
			String errMessage = "\"URL Error\"\n"
    		        + "Please check the URL correct.\n"
    		        + "Check also internet connexion! ;-) ";
    		    JOptionPane.showMessageDialog(new JFrame(), errMessage, "URL is not found",
    		        JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		tools.speciesFile speciesFile=new tools.speciesFile(speciesPath);
		species= speciesFile.readSpecies();
		aln.format(seqType, species);
		positions= new tools.positions(positionsPath,aln.getSize(),seqType);
		selection = new tools.pamlFile(pamlPath,positions);
		//featureFile = new tools.featureFile(uniprotPath,positions);
		try {
			featureFile = new tools.featureFile(new URL("http://www.uniprot.org/uniprot/"+UniprotID+".txt"),positions);
		} catch (MalformedURLException e) {
			featureFile = new tools.featureFile(uniprotPath,positions);
			e.printStackTrace();
		} catch (IOException e) {
			featureFile = new tools.featureFile(uniprotPath,positions);
			e.printStackTrace();
		}
		htmlEditorKit_selection=selection.buildSelectedHTML(aln.getSize(),seqType);
		windowWidth=800;
		windowHeight=600;
		
		setBounds(100, 100, windowWidth, windowHeight);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		FeaturePane = new JPanel();
		FeaturePane.setBackground(Color.WHITE);
		FeaturePane.setBorder(new EmptyBorder(0, 0, 0, 0));
			
		txtpnAln = new JTextPane();
		txtpnAln.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtpnAln.setContentType("text/html");
		txtpnAln.setBackground(Color.WHITE);
		txtpnAln.setEditable(false);
		
		//txtpnUniprot = new JTextPane();
		//txtpnUniprot.setContentType("text/html");
		//txtpnUniprot.setBackground(Color.WHITE);
		//txtpnUniprot.setEditable(false);
		
		txtpnAlnSpecies= new JTextPane();
		txtpnAlnSpecies.setContentType("text/html");
		txtpnAlnSpecies.setBackground(Color.WHITE);
		txtpnAlnSpecies.setEditable(false);
		txtpnAlnSpecies.setText(aln.getHtmlBlock().getHTMLSpecies(species));
		txtpnAlnSpecies.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		txtpnlegendSelection= new JTextPane();
		txtpnlegendSelection.setForeground(Color.RED);
		txtpnlegendSelection.setFont(new Font("Tahoma", Font.PLAIN, 9));
		txtpnlegendSelection.setContentType("text/html");
		txtpnlegendSelection.setBackground(Color.WHITE);
		txtpnlegendSelection.setEditable(false);
		txtpnlegendSelection.setText("Positive seleciton");
		txtpnlegendSelection.setBorder(new LineBorder(new Color(0, 0, 0)));
			
		txtpnRefPos= new JTextPane();
		txtpnRefPos.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtpnRefPos.setContentType("text/html");
		txtpnRefPos.setBackground(Color.WHITE);
		txtpnRefPos.setEditable(false);
		
		txtpnSelection= new JTextPane();
		txtpnSelection.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtpnSelection.setContentType("text/html");
		txtpnSelection.setBackground(Color.WHITE);
		txtpnSelection.setEditable(false);
		txtpnSelection.setEditorKit(htmlEditorKit_selection);
		
		slider = new JSlider();
		slider.setForeground(Color.GRAY);
		slider.setMinimum(0);
		slider.setValue(0);
		slider.setBackground(Color.CYAN);
		slider.setBorder(null);
		
		slider.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent ce) {
	        	VisualizeFrame.this.Set_txtpnAln_Start(((JSlider) ce.getSource()).getValue());
	        }
	    });
				
		slideEndLab = new Label("End : ");
		slideEndLab.setBackground(Color.CYAN);
		
		contentPane.add(slideEndLab);
		
		slideStartLab = new Label("Start: ");
		slideStartLab.setBackground(Color.CYAN);

		this.addComponentListener(new ComponentAdapter() 
		{  
		        public void componentResized(ComponentEvent evt) {
		            //Component c = (Component)evt.getSource();
		        	numPage=1;
			    	updateFT();
		        	update_size();
		        }
		});
		btnUpdateUniprot = new JButton("Uniprot: update selection");
		btnUpdateUniprot.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ae) {
		      	  numPage=1;
		    	  updateFT();
		      }
		    });
		HashUniprot= new HashMap<Integer,JTextPane>();
		update_size();
		
		contentPane.add(txtpnAln);
		// NO PANE FOR UNIPROT contentPane.add(txtpnUniprot);
		contentPane.add(txtpnAlnSpecies);
		contentPane.add(txtpnRefPos);
		contentPane.add(txtpnSelection);
		contentPane.add(slider);
		contentPane.add(slideStartLab);
		contentPane.add(btnUpdateUniprot);
		contentPane.add(txtpnlegendSelection);
		contentPane.add(FeaturePane);
		contentPane.setLayout(null);
		
		txtFeaturePageOn.setText("Features: page ##/## (## features by page) ");
		txtFeaturePageOn.setBounds(160, 36, 200, 23);
		contentPane.add(txtFeaturePageOn);
		txtFeaturePageOn.setColumns(10);
		
		JButton buttonNextFT = new JButton(">");
		buttonNextFT.setBounds(364, 36, 41, 23);
		contentPane.add(buttonNextFT);
		buttonNextFT.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ae) {
		    	  if (numPage<totPage){
		    		  numPage++;
		    		  updateFT();
		    	  }
		      }
		});   
		JButton buttonBeforeFT = new JButton("<");
		buttonBeforeFT.setBounds(115, 36, 41, 23);
		contentPane.add(buttonBeforeFT);
		buttonBeforeFT.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ae) {
		    	  if (numPage>1){
		    		  numPage--;
		    		  updateFT();
		    	  }
		      }
		});
		
		featSelect = new gui.featureSelect(featureFile,frametitle);
		featSelect.setVisible(true);
		
		featSelect.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	}
}

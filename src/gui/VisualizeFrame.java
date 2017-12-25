package gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JScrollPane;
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
import features.feature;
import tools.myFONT;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.JTextField;
import java.awt.Font;

import javax.swing.SwingConstants;

public class VisualizeFrame extends JFrame {
	
	private alignment.Alignment aln;
	private String seqType;
	private String frametitle;
	private evolution.Species species;
	private features.featureFile featureFile;
	
	evolution.positions positions;
	// private String pamlPath= "./out";
	// private String SpeciesPath= "./species.txt";
	// private String alnPath= "./codon_aln.fa";
	// private String positionsPath= "./posDict.tab";
	evolution.pamlFile selection;
	private int iterPos;
	private int viewSize=61;
	private int start=0;
	private int legendSize=110;
	private int startFeature;
	
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
	private JLabel txtpnAln;
	private HashMap<Integer,JTextPane> HashUniprot;
	//private JTextPane txtpnUniprot;
	private JLabel txtpnAlnSpecies;
	private JScrollPane scrolledTxtpnAln;
	private JLabel txtpnRefPos;
	private JScrollPane scrolledTxtpnRefPos;
	private JLabel txtpnSelection;
	private JScrollPane scrolledTxtpnSelection;
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
		maxFTbyPage=(int) (heightFTPane/(myFONT.getFontSize()+10));
		totPage=(numFT/maxFTbyPage)+1;
		txtFeaturePageOn.setText("Features: page "+numPage+"/"+totPage+" ("+maxFTbyPage+" FT/page) ");
	}
	
	public void Set_scroll_Start(int slide_start){
		JScrollBar NewScroll= scrolledTxtpnAln.getHorizontalScrollBar();
		double charwidth = tools.myFONT.getWidth();
		if (Math.abs(slide_start-NewScroll.getValue())>=tools.myFONT.getWidth()) {
			start=(int) (slide_start/charwidth);
			NewScroll.setValue((int) (slide_start-slide_start%tools.myFONT.getWidth()));
			NewScroll.setSize(0, 0);
			scrolledTxtpnAln.setHorizontalScrollBar(NewScroll);
			scrolledTxtpnSelection.setHorizontalScrollBar(NewScroll);
			scrolledTxtpnRefPos.setHorizontalScrollBar(NewScroll);
			slideStartLab.setText("Start "+(start+1));
			slideEndLab.setText("End "+(start+viewSize+1));
		}
	}
	
	public void update_size(){
		windowWidth=this.getWidth();
		windowHeight=this.getHeight();
		ctrlWidth=windowWidth-10;
		ctrlHeight=windowHeight-10;
		AvailViewSize=ctrlWidth-15-legendSize;
		viewSize=(int)(AvailViewSize/myFONT.getWidth());
		int alnWidth=(int)AvailViewSize;
		scrolledTxtpnAln.setBounds(legendSize+5, 80,alnWidth , aln.getHeight());
		txtpnlegendSelection.setBounds(5,80+aln.getHeight(),legendSize-5,20);
		scrolledTxtpnSelection.setBounds(legendSize+5, 80+aln.getHeight(), alnWidth, 20);
		scrolledTxtpnRefPos.setBounds(legendSize+5, 60, alnWidth, 20);
		//txtpnUniprot.setBounds();
		iterPos=0;
		startFeature=(int) (80+aln.getHeight()+myFONT.getFontSize())+1;
		FeaturePane.setBounds(legendSize,startFeature,alnWidth+10,(int) (HashUniprot.size()*(10.0+myFONT.getFontSize())));
		int iterFTNum=1;
		heightFTPane=ctrlHeight-startFeature-10;
		this.setPageFT(HashUniprot.size());
		for (Integer NumTxtPaneToAdd: HashUniprot.keySet()){
			if ((iterFTNum>(numPage-1)*maxFTbyPage)&&(iterFTNum<=(numPage)*maxFTbyPage)){
				HashUniprot.get(NumTxtPaneToAdd).setBounds(legendSize+5, (int) (iterPos*(myFONT.getFontSize())), alnWidth+10, (int) (0+myFONT.getFontSize()));
				iterPos+=1;
			}
			iterFTNum++;
		}
		slider.setMaximum(scrolledTxtpnAln.getHorizontalScrollBar().getMaximum());
	}
	
	public void changeAvailableType(String type_id, Boolean boxChecked){
		featureFile.availableType.replace(type_id, boxChecked);
	}
	
	/**
	 * Create the frame.
	 * @param scrolledTxtpnSelection 
	 */
	
	public VisualizeFrame(String alignmentPath, String speciesPath,String pamlPath,String positionsPath,String uniprotPath,alignment.genCode genCode,String input_seqType,String geneName, String UniprotID) throws FileNotFoundException {
		database.UniprotWebFasta webFasta;	
		frametitle=geneName + " - " + input_seqType;
		//GENETEST=geneName;
		setTitle(frametitle);
		seqType=input_seqType;
		alignment.alnFile alnFile= new alignment.alnFile(alignmentPath);
		aln= new alignment.Alignment(alnFile.readALN(),genCode);
		try {
			webFasta = new database.UniprotWebFasta("http://www.uniprot.org/uniprot/"+UniprotID+".fasta");
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
		evolution.speciesFile speciesFile=new evolution.speciesFile(speciesPath);
		species= speciesFile.readSpecies();	
		aln.buildHTML(seqType, species);
		positions= new evolution.positions(positionsPath,aln.getSize(),seqType);
		selection = new evolution.pamlFile(pamlPath,positions);
		//featureFile = new tools.featureFile(uniprotPath,positions);
		try {
			featureFile = new features.featureFile(new URL("http://www.uniprot.org/uniprot/"+UniprotID+".txt"),positions);
		} catch (MalformedURLException e) {
			featureFile = new features.featureFile(uniprotPath,positions);
			e.printStackTrace();
		} catch (IOException e) {
			featureFile = new features.featureFile(uniprotPath,positions);
			e.printStackTrace();
		}
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
		txtpnAln = new JLabel();
		txtpnAln.setVerticalAlignment(SwingConstants.TOP);
		txtpnAln.setBorder(null);
		txtpnAln.setBackground(Color.WHITE);
		//if (seqType=="codon") {
		//	txtpnAln.setPreferredSize(new Dimension(3*aln.getWidth(), aln.getHeight()));
		//}else {
		//	txtpnAln.setPreferredSize(new Dimension(aln.getWidth(), aln.getHeight()));
		//}
		txtpnAln.setText(aln.getHTML());
		txtpnAln.getFontMetrics(myFONT.getFont()).getHeight();
		txtpnAln.setFont(myFONT.getFont());
		
		scrolledTxtpnAln = new JScrollPane(txtpnAln);
		scrolledTxtpnAln.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrolledTxtpnAln.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
		
		//txtpnUniprot = new JTextPane();
		//txtpnUniprot.setContentType("text/html");
		//txtpnUniprot.setBackground(Color.WHITE);
		//txtpnUniprot.setEditable(false);
		
		txtpnAlnSpecies= new JLabel();
		txtpnAlnSpecies.setBackground(Color.WHITE);
		txtpnAlnSpecies.setFont(myFONT.getFont());
		//TODO CORRECT txtpnAlnSpecies.setText(aln.getHtmlBlock().getHTMLSpecies(species));
		txtpnAlnSpecies.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtpnAlnSpecies.setBounds(5, 80, legendSize-5, aln.getHeight());
		
		txtpnlegendSelection= new JTextPane();
		txtpnlegendSelection.setForeground(Color.RED);
		txtpnlegendSelection.setFont(new Font("Tahoma", Font.PLAIN, 9));
		txtpnlegendSelection.setContentType("text/html");
		txtpnlegendSelection.setBackground(Color.WHITE);
		txtpnlegendSelection.setEditable(false);
		txtpnlegendSelection.setText("Positive seleciton");
		txtpnlegendSelection.setBorder(new LineBorder(new Color(0, 0, 0)));
			
		txtpnRefPos= new JLabel();
		txtpnRefPos.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtpnRefPos.setBackground(Color.WHITE);
		txtpnRefPos.setText(positions.buildHTML(seqType));
		txtpnRefPos.setFont(myFONT.getFont());
		scrolledTxtpnRefPos = new JScrollPane(txtpnRefPos);
		scrolledTxtpnRefPos.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrolledTxtpnRefPos.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
		
		
		txtpnSelection= new JLabel();
		txtpnSelection.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtpnSelection.setBackground(Color.WHITE);
		txtpnSelection.setText(selection.buildHTML(aln.getSize(), seqType));
		txtpnSelection.setFont(myFONT.getFont());
		scrolledTxtpnSelection = new JScrollPane(txtpnSelection);
		scrolledTxtpnSelection.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrolledTxtpnSelection.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
				
		slider = new JSlider();
		slider.setForeground(Color.GRAY);
		slider.setMinimum(0);
		slider.setValue(0);
		slider.setBackground(Color.CYAN);
		slider.setBorder(null);
		
		slider.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent ce) {
	        	VisualizeFrame.this.Set_scroll_Start(((JSlider) ce.getSource()).getValue());
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
		
		slideEndLab.setBounds(legendSize+285, 10, 80, 20);
		slideStartLab.setBounds(legendSize+5, 10, 80, 20);
		slider.setBounds(legendSize+85, 10, 200, 20);
		btnUpdateUniprot.setBounds(510, 10, 229, 23);
		
		update_size();
		
		contentPane.add(scrolledTxtpnAln);
		// NO PANE FOR UNIPROT contentPane.add(txtpnUniprot);
		contentPane.add(txtpnAlnSpecies);
		contentPane.add(scrolledTxtpnRefPos);
		contentPane.add(scrolledTxtpnSelection);
		contentPane.add(slider);
		contentPane.add(slideStartLab);
		contentPane.add(btnUpdateUniprot);
		contentPane.add(txtpnlegendSelection);
		contentPane.add(FeaturePane);
		contentPane.setLayout(null);
		
		txtFeaturePageOn.setText("Features: page ##/## (## features by page) ");
		txtFeaturePageOn.setBounds(177, 36, 200, 23);
		contentPane.add(txtFeaturePageOn);
		txtFeaturePageOn.setColumns(10);
		
		JButton buttonNextFT = new JButton(">");
		buttonNextFT.setBounds(386, 36, 53, 23);
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
		buttonBeforeFT.setBounds(115, 36, 53, 23);
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

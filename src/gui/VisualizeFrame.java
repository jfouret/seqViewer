package gui;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.JSlider;
import javax.swing.event.*;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.text.html.HTMLEditorKit;

import tools.feature;
import tools.myFONT;

public class VisualizeFrame extends JFrame {

	/**
	 * 
	 */
	
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
	private HTMLEditorKit htmlEditorKit_selection = new HTMLEditorKit();
	
	// Dimensions
	int windowWidth;
	int windowHeight;
	int ctrlWidth;
	int ctrlHeight;
	
	
	private static final long serialVersionUID = 3034391181248326868L;
	private JPanel contentPane;
	private JTextPane txtpnAln;
	private HashMap<Integer,JTextPane> HashUniprot;
	//private JTextPane txtpnUniprot;
	private JTextPane txtpnAlnSpecies;
	private JTextPane txtpnRefPos;
	private JTextPane txtpnSelection;
	private JSlider slider;
	private Label slideEndLab;
	private Label slideStartLab;
	private gui.featureSelect featSelect;
	private JButton btnUpdateUniprot ;
	
	
	/**
	 * set dynamic change to the frame
	 */
	
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
			for (Integer NumTxtPaneToAdd: HashUniprot.keySet()){
				HashUniprot.get(NumTxtPaneToAdd).setText(featureFile.getHTML(start, viewSize, seqType,NumTxtPaneToAdd));
				HashUniprot.get(NumTxtPaneToAdd).setToolTipText(featureFile.getToolTipText(NumTxtPaneToAdd));
			}
			slideStartLab.setText("Start "+(start+1));
			slideEndLab.setText("End "+(start+viewSize+1));
		}
	}
	
	public void update_size(){
		windowWidth=this.getWidth();
		ctrlWidth=windowWidth-10;
		ctrlHeight=windowHeight-10;
		slideEndLab.setBounds(legendSize+285, 10, 80, 20);
		slideStartLab.setBounds(legendSize+5, 10, 80, 20);
		slider.setBounds(legendSize+85, 10, 200, 20);
		btnUpdateUniprot.setBounds(legendSize+400, 10, 105, 23);
		viewSize=(int)(((windowWidth)-10-legendSize)/myFONT.fontwidth);
		txtpnRefPos.setBounds(legendSize+5, 60, ctrlWidth-100, 20);
		txtpnAlnSpecies.setBounds(5, 80, legendSize-5, aln.getHeight());
		txtpnAln.setBounds(legendSize+5, 80, ctrlWidth-100, aln.getHeight());
		txtpnSelection.setBounds(legendSize+5, 80+aln.getHeight(), ctrlWidth-100, 20);
		//txtpnUniprot.setBounds();
		iterPos=0;
		for (Integer NumTxtPaneToAdd: HashUniprot.keySet()){
			HashUniprot.get(NumTxtPaneToAdd).setBounds(legendSize+5, (int) ((80+aln.getHeight()+myFONT.fontHeight)+iterPos*(myFONT.fontHeight+1)), ctrlWidth-100, (int) (0+myFONT.fontHeight));
			iterPos+=1;
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
	
	public VisualizeFrame(String alignmentPath, String speciesPath,String pamlPath,String positionsPath,String uniprotPath,tools.genCode genCode,String input_seqType,String geneName) throws FileNotFoundException {
		frametitle=geneName + " - " + input_seqType;
		setTitle( frametitle );
		seqType=input_seqType;
		tools.alnFile alnFile= new tools.alnFile(alignmentPath);
		aln= new tools.Alignment(alnFile.readALN(),genCode);
		tools.speciesFile speciesFile=new tools.speciesFile(speciesPath);
		species= speciesFile.readSpecies();
		aln.format(seqType, species);
		positions= new tools.positions(positionsPath,aln.getSize(),seqType);
		selection = new tools.pamlFile(pamlPath,positions);
		featureFile = new tools.featureFile(uniprotPath,positions);
		htmlEditorKit_selection=selection.buildSelectedHTML(aln.getSize(),seqType);
		windowWidth=800;
		windowHeight=600;
		
		setBounds(100, 100, windowWidth, windowHeight);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
			
		txtpnAln = new JTextPane();
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
		
		txtpnRefPos= new JTextPane();
		txtpnRefPos.setContentType("text/html");
		txtpnRefPos.setBackground(Color.WHITE);
		txtpnRefPos.setEditable(false);
		
		txtpnSelection= new JTextPane();
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
		        	update_size();
		        }
		});
		btnUpdateUniprot = new JButton("Update Uniprot");
		btnUpdateUniprot.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ae) {
		    	  featureFile.availableType=featSelect.UpdateAvailableType();
		    	  // remove all JTextPane
		    	  for (JTextPane featTextPaneToRemove : HashUniprot.values()){
		    		  gui.VisualizeFrame.this.contentPane.remove(featTextPaneToRemove);
		    	  }
		    	  // re-initiate HashUniprot
		    	  HashUniprot= new HashMap<Integer,JTextPane>();
		    	  // 
		    	  for (feature featToAdd: featureFile.featureArray){
		    		  if (featToAdd.isSelected(featureFile.availableType)){
		    			  HashUniprot.put(featToAdd.getNum(), new JTextPane());
		    			  HashUniprot.get(featToAdd.getNum()).setContentType("text/html");
		    			  HashUniprot.get(featToAdd.getNum()).setBackground(Color.WHITE);
		    		  }
		    	  }
		    	  update_size();
		    	  for (JTextPane txtPaneToAdd: HashUniprot.values()){
		    		  gui.VisualizeFrame.this.contentPane.add(txtPaneToAdd);
		    	  }
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
		contentPane.setLayout(null);
		
		featSelect = new gui.featureSelect(featureFile,frametitle);
		featSelect.setVisible(true);
		
		featSelect.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	}
}

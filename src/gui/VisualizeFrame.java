/*
 *   seqViewer is a program dedicated to the visualization of sites under 
 *   branch-site positive selection or similar approaches with superpositions 
 *   with annotated UniProt features.
 *   
 *   Copyright (C) 2018  Julien Fouret
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package gui;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.ScrollPaneConstants;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.event.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import tools.myFONT;

import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;


import jaligner.Alignment;
import jaligner.Sequence;
import jaligner.SmithWatermanGotoh;
import jaligner.matrix.Matrix;
import jaligner.matrix.MatrixLoader;
import jaligner.matrix.MatrixLoaderException;
import jaligner.util.SequenceParser;
import jaligner.util.SequenceParserException;


public class VisualizeFrame extends JFrame {
	
	private JSplitPane splitPane;
	private tools.myFONT myFont= new myFONT();
	private alignment.Alignment aln;
	private String seqType;
	private String frametitle;
	private features.featureAPI featureAPI;
	private evolution.treeFile treeFile;
	evolution.positions positions;
	evolution.pamlFile selection;
	private static final long serialVersionUID = 3034391181248326868L;
	private JPanel contentPane;
	private JLabel txtpnAln;
	private JLabel txtpnAlnSpecies;
	private JScrollPane scrolledTxtpnAln;
	private JLabel txtpnRefPos;
	private JScrollPane scrolledTxtpnRefPos;
	private JLabel txtpnExons;
	private JScrollPane scrolledTxtpnExons;
	private JLabel txtpnSelection;
	private JScrollPane scrolledTxtpnSelection;
	private JLabel txtpnlegendSelection;
	private JLabel txtpnlegendExons;
	private JSlider slider;
	private JComboBox<String> posTypeBox;
	private gui.featureSelect featSelect;
	private JButton btnUpdateUniprot ;
	private JButton btnPlus ;
	private JButton btnMinus ;
	private JScrollPane scrollFeatPane;
	private JPanel FeatPane ;
	private JScrollBar featBar;
	private ZipFile database;
	
	private JLabel[] featLabelArray;
	
	public void Set_scroll_Start(int slide_start){
		JScrollBar NewScroll= scrolledTxtpnAln.getHorizontalScrollBar();
		if (Math.abs(slide_start-NewScroll.getValue())>=myFont.getWidth()) {
			NewScroll.setValue((int) (slide_start-slide_start%myFont.getWidth()));
			NewScroll.setSize(0, 0);
			scrolledTxtpnAln.setHorizontalScrollBar(NewScroll);
			scrolledTxtpnSelection.setHorizontalScrollBar(NewScroll);
			scrolledTxtpnRefPos.setHorizontalScrollBar(NewScroll);
			scrolledTxtpnExons.setHorizontalScrollBar(NewScroll);
			scrollFeatPane.setHorizontalScrollBar(NewScroll);
		}
	}
	
	// Dimensions
	private int legendWidth;
	private int legendX=5;
	private int txtX=legendWidth;
	private int startY=40;	
	private int windowWidth;
	private int windowHeight;
	private int ctrlWidth;
	private int ctrlHeight;
	private int AvailViewSize;
	private int alnHeight;
	HashMap<Integer,Integer> uniprot2ref;

	
	public void update_size(){
		legendWidth=1+(int) ((1+treeFile.getSize())*myFont.getWidth());
		txtX=legendWidth+10;
		windowWidth=contentPane.getWidth();
		windowHeight=contentPane.getHeight();
		ctrlWidth=windowWidth-10;
		ctrlHeight=windowHeight-10;
		AvailViewSize=ctrlWidth-10-legendWidth;
		int height=myFont.getHeight().intValue();
		alnHeight=height*aln.getLine();
		
		txtpnlegendExons.setBounds(legendX,startY,legendWidth,height+2);
		scrolledTxtpnExons.setBounds(txtX, startY, AvailViewSize, height+2);
		
		scrolledTxtpnRefPos.setBounds(txtX, startY+height+5, AvailViewSize, height+2);
		posTypeBox.setBounds(legendX,startY+height+5,legendWidth,height+2);
		
		txtpnAlnSpecies.setBounds(legendX, startY+2*(height+5), legendWidth, 2+alnHeight);
		scrolledTxtpnAln.setBounds(txtX, startY+2*(height+5),AvailViewSize , 2+alnHeight);
		
		txtpnlegendSelection.setBounds(legendX,startY+2*(height+5)+alnHeight+5,legendWidth,height+2);
		scrolledTxtpnSelection.setBounds(txtX, startY+2*(height+5)+alnHeight+5, AvailViewSize, height+2);
		
		int featHeight=featLabelArray.length*height+2;
		
		if (featHeight+startY+3*(height+5)+alnHeight+5>ctrlHeight) {
			featHeight=ctrlHeight-(startY+3*(height+5)+alnHeight+5);
			featBar.setBounds(txtX-30, startY+3*(height+5)+alnHeight+5, 25, featHeight);
		}else {
			featBar.setBounds(txtX-30, startY+3*(height+5)+alnHeight+5, 0, 0);
		}
		
		scrollFeatPane.setBounds(txtX, startY+3*(height+5)+alnHeight+5, AvailViewSize, featHeight);
		
		
		slider.setMaximum((int) (scrolledTxtpnAln.getHorizontalScrollBar().getMaximum()-AvailViewSize+myFont.getWidth()));
		slider.validate();
		contentPane.validate();
		scrollFeatPane.validate();
	}
	
	public void update_font(){
		Font theFont=myFont.getFont();
		
		txtpnlegendExons.setFont(theFont);
		txtpnExons.setFont(theFont);
		
		txtpnRefPos.setFont(theFont);
		posTypeBox.setFont(theFont);
		
		txtpnAlnSpecies.setFont(theFont);
		txtpnAln.setFont(theFont);
		
		txtpnlegendSelection.setFont(theFont);
		txtpnSelection.setFont(theFont);
		
		for (JLabel feat : featLabelArray) {
			feat.setFont(theFont);
		}
		update_size();
	}
	
	public void changeAvailableType(String type_id, Boolean boxChecked){
		featureAPI.availableType.replace(type_id, boxChecked);
	}
	
	/**
	 * Create the frame.
	 * @param scrolledTxtpnSelection 
	 */

	public BufferedReader getBufferedReader_fromDB(String entryName){
		BufferedReader bf=null;
		try {
			bf=new BufferedReader(new InputStreamReader(database.getInputStream(database.getEntry(entryName))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return(bf);
	}
	
	public VisualizeFrame(String alignmentPath, String treePath,String pamlPath,String exonBedPath,String blockBedPath,alignment.genCode genCode,String input_seqType,String geneName, String UniprotID, String ref_species,ZipFile in_database,float open_gap,float extend_gap) throws FileNotFoundException {
		long startTime = System.currentTimeMillis();
		database=in_database;
		
		//System.out.println(treePath);
		database.UniprotWebFasta webFasta;
		
		String pattern = "(.*)-\\d*?";
		
		Pattern r = Pattern.compile(pattern);
		
		Matcher m = r.matcher(UniprotID);
		String canonicalUniprotID;
		if (m.find( )) {
			canonicalUniprotID=m.group(1);
		}else{
			canonicalUniprotID=UniprotID;
		}
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Frame initiated in "+totalTime+"ms");
		startTime=endTime;
		
		try {
			treeFile=new evolution.treeFile(this.getBufferedReader_fromDB(treePath));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		String[] species = treeFile.getSpecies();
		//System.out.println("step1");
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Tree file loaded in "+totalTime+"ms");
		startTime=endTime;
		
		frametitle=geneName + " - " + input_seqType;
		//GENETEST=geneName;
		setTitle(frametitle);
		seqType=input_seqType;
		
		
		alignment.alnFile alnFile= new alignment.alnFile(this.getBufferedReader_fromDB(alignmentPath));
		//System.out.println("step2");
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Alignment file loaded in "+totalTime+"ms");
		startTime=endTime;
		
		aln= new alignment.Alignment(alnFile.readALN(),genCode);
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Alignment object initiated in "+totalTime+"ms");
		startTime=endTime;
		
		//System.out.println("step3");
		try {
			//System.out.println("step4");
			webFasta = new database.UniprotWebFasta("https://www.uniprot.org/uniprot/"+canonicalUniprotID+".fasta");
			//System.out.println(aln.getSeqRef());
			//System.out.println(webFasta.getSeq());
			
			endTime   = System.currentTimeMillis();
			totalTime = endTime - startTime;
			System.out.println("Fasta sequence from Uniprot loaded in "+totalTime+"ms");
			startTime=endTime;

			if (UniprotID.contains("-") || !aln.getSeqRef(ref_species).startsWith(webFasta.getSeq()) ){
				String errMessage = "\"BE CAREFULL\"\n"
	    		        + "The Uniprot Sequence does not match perfectly with the one in the alignment.\n";
				try {
					Sequence s1 = SequenceParser.parse(aln.getSeqRef(ref_species));
					s1.setId("Reference");
					s1.setDescription("Reference in the alignment");
					//System.out.println(s1);
					//System.out.println(webFasta.getSeq());
					Sequence s2 = SequenceParser.parse(webFasta.getSeq());
					s2.setId("Uniprot");
					s2.setDescription("Sequence from uniprot API");
					Matrix matrix = MatrixLoader.load("BLOSUM62");
					Alignment alignment = SmithWatermanGotoh.align(s1, s2, matrix, open_gap,extend_gap);
					errMessage=errMessage+ "A Smith Waterman Gotoh alignment have been performed to ensure the mapping of feature positions in uniprot sequence (available via uniprot API) with the reference sequence used in the alignment. \n" 
							+"This could result from differences in the definition of canonical isoform (UCSC vs Uniprot) or simply sequence conflict.\n" +
							"Issues are automatically fixed in most cases but if below is reported a high number of mismatches please be carefull and consider revisiting the alignment with provided ids.\n" +
							"\n" +alignment.getSummary()+ "Start in reference: "+(alignment.getStart1()+1)+"\n"+"End in reference: "+(alignment.getStart1()+alignment.getLength()) ;
					uniprot2ref = new HashMap<Integer,Integer>();
					char letterRef;
					char letterUniprot;
					int gap_count=0;
					for(int i=alignment.getStart2(); i<alignment.getStart2()+alignment.getLength(); i++){
						letterRef=alignment.getSequence1()[i-alignment.getStart2()];
						letterUniprot=alignment.getSequence2()[i-alignment.getStart2()];
						if (letterRef=='-'){
							gap_count=gap_count+1;
						}
						else if (letterUniprot=='-'){
							gap_count=gap_count-1;
						}else if (letterRef==letterUniprot){
							uniprot2ref.put(1+i,1+i-alignment.getStart2()+alignment.getStart1()-gap_count);
							//int j =i-alignment.getStart2()+alignment.getStart1()-gap_count;
							//System.out.println("uniprot: "+i+" / ref:"+j);
						}
					}

				} catch (SequenceParserException e1) {
					e1.printStackTrace();
				} catch (MatrixLoaderException e1) {
					e1.printStackTrace();
				}
				endTime   = System.currentTimeMillis();
				totalTime = endTime - startTime;
				System.out.println("Exact alignment (optional in case of conflicts) performed in "+totalTime+"ms");
				startTime=endTime;
	    		    JOptionPane.showMessageDialog(new JFrame(), errMessage, "Database Error",
	    		        JOptionPane.WARNING_MESSAGE);
			}
			//System.out.println("step5");

		} catch (IOException e) {

			String errMessage = "\"URL Error\"\n"
    		        + "Please check the URL correct.\n"
    		        + "Check also internet connexion! ;-) ";
    		    JOptionPane.showMessageDialog(new JFrame(), errMessage, "URL is not found",
    		        JOptionPane.WARNING_MESSAGE);

		}
		
		aln.buildHTML(seqType, species);
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("HTML code built for alignment in "+totalTime+"ms");
		startTime=endTime;
		
		positions= new evolution.positions(this.getBufferedReader_fromDB(exonBedPath),this.getBufferedReader_fromDB(blockBedPath),aln.get_ref_dna(ref_species));

		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Positions object egenrated in "+totalTime+"ms");
		startTime=endTime;
		
		try {
			selection = new evolution.pamlFile(this.getBufferedReader_fromDB(pamlPath),positions);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//featureAPI = new tools.featureAPI(uniprotPath,positions);
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("PAML file (or equivalent) parsed in "+totalTime+"ms");
		startTime=endTime;
		
		try {
			featureAPI = new features.featureAPI(new URL("https://www.uniprot.org/uniprot/"+canonicalUniprotID+".txt"),positions,uniprot2ref);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Fearture uniprot API request parsed in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		windowWidth=800;
		windowHeight=600;
		setBounds(100, 100, windowWidth, windowHeight);
		contentPane = new JPanel();
		contentPane.setBackground(tools.myCST.backColor);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Some graphical(0) interfaces generation in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		
		
		
		txtpnAln = new JLabel();
		txtpnAln.setVerticalAlignment(SwingConstants.TOP);
		txtpnAln.setBackground(tools.myCST.backColor);
		txtpnAln.setBorder(null);
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Alignment graphical interface initiation in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		String alignment_txt=aln.getHTML();
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Get HTML text of the alignment in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		scrolledTxtpnAln = new JScrollPane(txtpnAln);
		scrolledTxtpnAln.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrolledTxtpnAln.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		
		scrolledTxtpnAln = new JScrollPane(txtpnAln);
		scrolledTxtpnAln.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrolledTxtpnAln.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		
		scrolledTxtpnAln.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Some graphical(1) interfaces generation in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		
		
		txtpnAlnSpecies= new JLabel();
		txtpnAlnSpecies.setText(treeFile.getHTreeML());
		txtpnAlnSpecies.setVerticalAlignment(SwingConstants.TOP);
		txtpnAlnSpecies.setBorder(new LineBorder(new Color(0, 0, 0), 1));

		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Tree graphical interface generation in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		txtpnlegendSelection= new JLabel();
		txtpnlegendSelection.setHorizontalAlignment(SwingConstants.RIGHT);
		txtpnlegendSelection.setText("Selection");
		txtpnlegendSelection.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		
		txtpnlegendExons= new JLabel();
		txtpnlegendExons.setHorizontalAlignment(SwingConstants.RIGHT);
		txtpnlegendExons.setText("Exons switch");
		txtpnlegendExons.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Some graphical(2) interfaces generation in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		txtpnRefPos= new JLabel();
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Postions graphical interface generated in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		posTypeBox = new JComboBox<String>();
		posTypeBox.addItem("Aln positions");
		posTypeBox.addItem("Ref positions");
		posTypeBox.setSelectedItem("Aln positions");
		
		posTypeBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				long startTime=System.currentTimeMillis();
				if (posTypeBox.getSelectedItem()=="Aln positions") {
					txtpnRefPos.setText(aln.getPos(seqType));
				}else if (posTypeBox.getSelectedItem()=="Ref positions"){
					txtpnRefPos.setText(positions.getPos(seqType,aln.getSize()));//position in ref
				}
				contentPane.validate();
				long endTime   = System.currentTimeMillis();
				long totalTime = endTime - startTime;
				System.out.println("Position graphical interface modified in "+totalTime+"ms");
			}
		});
		scrolledTxtpnRefPos = new JScrollPane(txtpnRefPos);
		scrolledTxtpnRefPos.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrolledTxtpnRefPos.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		scrolledTxtpnRefPos.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Some graphical(3) interfaces generation in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		txtpnExons= new JLabel();
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Exon alternance graphical interface generation in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		scrolledTxtpnExons = new JScrollPane(txtpnExons);
		scrolledTxtpnExons.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrolledTxtpnExons.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		scrolledTxtpnExons.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Some graphical(4) interfaces generation in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		txtpnSelection= new JLabel();
		txtpnSelection.setBackground(tools.myCST.backColor);
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Selection interface generation in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		scrolledTxtpnSelection = new JScrollPane(txtpnSelection);
		scrolledTxtpnSelection.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		scrolledTxtpnSelection.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrolledTxtpnSelection.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
		
		slider = new JSlider();
		slider.setForeground(Color.GRAY);
		slider.setMinimum(0);
		slider.setValue(0);
		slider.setBackground(Color.LIGHT_GRAY);
		slider.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		slider.setBounds(195, 10, 311, 20);
		
		slider.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent ce) {
	        	VisualizeFrame.this.Set_scroll_Start(((JSlider) ce.getSource()).getValue());
	        }
	    });
		
		this.addComponentListener(new ComponentAdapter() 
		{  
		    public void componentResized(ComponentEvent evt) {
		    	//Component c = (Component)evt.getSource();
		    	update_size();
		    }
		});
		
		btnUpdateUniprot = new JButton("Uniprot: update selection");
		btnUpdateUniprot.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ae) {
		    	  long startTime=System.currentTimeMillis();
		    	  int saveStart=scrolledTxtpnAln.getHorizontalScrollBar().getValue();
		    	  contentPane.remove(scrollFeatPane);
		    	  contentPane.remove(featBar);
		    	  contentPane.validate();
		    	  featureAPI.update_AvailableType(featSelect.UpdateAvailableType());
		    	  featLabelArray=featureAPI.getLabels(seqType);
		    	  updateFeatures();
		    	  update_size();
		    	  Set_scroll_Start(0);
		    	  Set_scroll_Start(saveStart);
		    	  long endTime   = System.currentTimeMillis();
			  	long totalTime = endTime - startTime;
			  	System.out.println("Uniprot feature update in "+totalTime+"ms");
		      }
		    });
		btnUpdateUniprot.setBounds(5, 10, 178, 23);
		
		btnPlus = new JButton("+");
		btnPlus.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ae) {
		    	  myFont.modSize(1);
		    	  update_font();
		      }
		    });
		btnPlus.setBounds(570, 7, 45, 23);
		
		
		btnMinus = new JButton("-");
		btnMinus.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ae) {
		    	  myFont.modSize(-1);
		    	  update_font();
		      }
		    });
		btnMinus.setBounds(518, 7, 45, 23);
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Some graphical(5) interfaces generation in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		featSelect = new gui.featureSelect(featureAPI,frametitle);
		featLabelArray=featureAPI.getLabels(seqType);
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Feature graphical interfaces generation in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		updateFeatures();
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Feature first update in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		update_font();

		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Font first update in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		txtpnAln.setText(alignment_txt);
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Set HTML text of the alignment in graphical interface in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		
		txtpnExons.setText(positions.buildHTML(seqType));
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Set HTML text of the exons alternance in graphical interface in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		txtpnSelection.setText(selection.buildHTML(aln.getSize(), seqType));
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Set HTML text of the selection in graphical interface in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		txtpnRefPos.setText(aln.getPos(seqType));
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Set HTML text of the positions in graphical interface in "+totalTime+"ms");
		startTime=System.currentTimeMillis();
		
		contentPane.add(scrolledTxtpnAln);
		contentPane.add(txtpnAlnSpecies);
		contentPane.add(scrolledTxtpnRefPos);
		contentPane.add(scrolledTxtpnExons);
		contentPane.add(scrolledTxtpnSelection);
		contentPane.add(posTypeBox);
		contentPane.add(txtpnlegendExons);
		contentPane.add(slider);
		contentPane.add(btnUpdateUniprot);
		contentPane.add(btnPlus);
		contentPane.add(btnMinus);
		contentPane.add(txtpnlegendSelection);
		contentPane.setLayout(null);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                featSelect,contentPane);
		
		setContentPane(splitPane);
		
		splitPane.setContinuousLayout(true);
		
		splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, 
			    new PropertyChangeListener() {
			        @Override
			        public void propertyChange(PropertyChangeEvent pce) {
				    	update_size();
			        }
			});
		
		splitPane.setDividerLocation(150);
		splitPane.setVisible(true);
		
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Some graphical(6) interfaces generation in "+totalTime+"ms");
	}
	
	private void updateFeatures() {
		FeatPane = new JPanel(new FlowLayout());
		((FlowLayout)FeatPane.getLayout()).setVgap(0);
		((FlowLayout)FeatPane.getLayout()).setHgap(0);
		int height=myFont.getHeight().intValue();
		int i=0;
		for (JLabel feat : featLabelArray) {
			feat.setBounds(0,i*height, AvailViewSize,height);
			feat.setFont(myFont.getFont());
			FeatPane.add(feat);
			i++;
		}
		FeatPane.setBackground(tools.myCST.backColor);
		FeatPane.setPreferredSize(new Dimension((int) (aln.getLength(seqType)*myFont.getWidth()), featLabelArray.length*height+2));
		scrollFeatPane = new JScrollPane(FeatPane);
		scrollFeatPane.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		scrollFeatPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
		scrollFeatPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
		featBar = scrollFeatPane.getVerticalScrollBar();
		contentPane.add(scrollFeatPane);
		contentPane.add(featBar);
	}
}

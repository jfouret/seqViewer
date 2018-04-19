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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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

	public VisualizeFrame(String alignmentPath, String treePath,String pamlPath,String exonBedPath,String blockBedPath,alignment.genCode genCode,String input_seqType,String geneName, String UniprotID, String ref_species) throws FileNotFoundException {
		System.out.println("step0");
		database.UniprotWebFasta webFasta;
		
		
		treeFile=new evolution.treeFile(treePath);
		String[] species = treeFile.getSpecies();
		System.out.println("step1");
		
		frametitle=geneName + " - " + input_seqType;
		//GENETEST=geneName;
		setTitle(frametitle);
		seqType=input_seqType;
		alignment.alnFile alnFile= new alignment.alnFile(alignmentPath);
		System.out.println("step2");
		
		
		
		aln= new alignment.Alignment(alnFile.readALN(),genCode);
		System.out.println("step3");
		try {
			System.out.println("step4");
			webFasta = new database.UniprotWebFasta("http://www.uniprot.org/uniprot/"+UniprotID+".fasta");
			//System.out.println(aln.getSeqRef());
			//System.out.println(webFasta.getSeq());

			if (!aln.getSeqRef(ref_species).startsWith(webFasta.getSeq())){
				String errMessage = "\"BE CAREFULL\"\n"
	    		        + "The Uniprot Sequence does not match with the one in the alignment\n"
	    		        + "Uniprot datas are likely to be false! ;-) ";
	    		    JOptionPane.showMessageDialog(new JFrame(), errMessage, "Database Error",
	    		        JOptionPane.WARNING_MESSAGE);
			}
			System.out.println("step5");

		} catch (IOException e) {

			String errMessage = "\"URL Error\"\n"
    		        + "Please check the URL correct.\n"
    		        + "Check also internet connexion! ;-) ";
    		    JOptionPane.showMessageDialog(new JFrame(), errMessage, "URL is not found",
    		        JOptionPane.WARNING_MESSAGE);

		}
		
		
		System.out.println("step6");
		aln.buildHTML(seqType, species);
		System.out.println("step7");
		positions= new evolution.positions(exonBedPath,blockBedPath,aln.get_ref_dna(ref_species));
		System.out.println("step8");

		selection = new evolution.pamlFile(pamlPath,positions);
		System.out.println("step9");

		//featureAPI = new tools.featureAPI(uniprotPath,positions);
		try {
			featureAPI = new features.featureAPI(new URL("http://www.uniprot.org/uniprot/"+UniprotID+".txt"),positions);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		windowWidth=800;
		windowHeight=600;
		setBounds(100, 100, windowWidth, windowHeight);
		contentPane = new JPanel();
		contentPane.setBackground(tools.myCST.backColor);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		
		txtpnAln = new JLabel();
		txtpnAln.setVerticalAlignment(SwingConstants.TOP);
		txtpnAln.setBackground(tools.myCST.backColor);
		txtpnAln.setBorder(null);

		txtpnAln.setText(aln.getHTML());
		
		scrolledTxtpnAln = new JScrollPane(txtpnAln);
		scrolledTxtpnAln.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrolledTxtpnAln.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		
		scrolledTxtpnAln = new JScrollPane(txtpnAln);
		scrolledTxtpnAln.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrolledTxtpnAln.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		
		scrolledTxtpnAln.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

		txtpnAlnSpecies= new JLabel();
		txtpnAlnSpecies.setText(treeFile.getHTreeML());
		txtpnAlnSpecies.setVerticalAlignment(SwingConstants.TOP);
		txtpnAlnSpecies.setBorder(new LineBorder(new Color(0, 0, 0), 1));

		txtpnlegendSelection= new JLabel();
		txtpnlegendSelection.setHorizontalAlignment(SwingConstants.RIGHT);
		txtpnlegendSelection.setText("Selection");
		txtpnlegendSelection.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		
		txtpnlegendExons= new JLabel();
		txtpnlegendExons.setHorizontalAlignment(SwingConstants.RIGHT);
		txtpnlegendExons.setText("Exons switch");
		txtpnlegendExons.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		
		txtpnRefPos= new JLabel();
		txtpnRefPos.setText(aln.getPos(seqType));
		
		posTypeBox = new JComboBox<String>();
		posTypeBox.addItem("Aln positions");
		posTypeBox.addItem("Ref positions");
		posTypeBox.setSelectedItem("Aln positions");
		
		posTypeBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (posTypeBox.getSelectedItem()=="Aln positions") {
					txtpnRefPos.setText(aln.getPos(seqType));
				}else if (posTypeBox.getSelectedItem()=="Ref positions"){
					txtpnRefPos.setText(positions.getPos(seqType,aln.getSize()));//position in ref
				}
				contentPane.validate();
			}
		});
		
		scrolledTxtpnRefPos = new JScrollPane(txtpnRefPos);
		scrolledTxtpnRefPos.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrolledTxtpnRefPos.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		scrolledTxtpnRefPos.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

		txtpnExons= new JLabel();
		txtpnExons.setText(positions.buildHTML(seqType));
		
		scrolledTxtpnExons = new JScrollPane(txtpnExons);
		scrolledTxtpnExons.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrolledTxtpnExons.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		scrolledTxtpnExons.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
		
		txtpnSelection= new JLabel();
		txtpnSelection.setBackground(tools.myCST.backColor);
		txtpnSelection.setText(selection.buildHTML(aln.getSize(), seqType));
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
		featSelect = new gui.featureSelect(featureAPI,frametitle);
		
		featLabelArray=featureAPI.getLabels(seqType);
		updateFeatures();
		update_font();

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

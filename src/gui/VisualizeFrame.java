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
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.SystemColor;

public class VisualizeFrame extends JFrame {
	
	private JSplitPane splitPane;
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
	private int legendSize=110;
	
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
	private JLabel txtpnAln;
	//private JTextPane txtpnUniprot;
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
	
	//private String GENETEST;
	
	/**
	 * set dynamic change to the frame
	 */
	
	public void Set_scroll_Start(int slide_start){
		JScrollBar NewScroll= scrolledTxtpnAln.getHorizontalScrollBar();
		if (Math.abs(slide_start-NewScroll.getValue())>=tools.myFONT.getWidth()) {
			NewScroll.setValue((int) (slide_start-slide_start%tools.myFONT.getWidth()));
			NewScroll.setSize(0, 0);
			scrolledTxtpnAln.setHorizontalScrollBar(NewScroll);
			scrolledTxtpnSelection.setHorizontalScrollBar(NewScroll);
			scrolledTxtpnRefPos.setHorizontalScrollBar(NewScroll);
			scrolledTxtpnExons.setHorizontalScrollBar(NewScroll);
		}
	}
	
	public void update_size(){
		windowWidth=contentPane.getWidth();
		windowHeight=contentPane.getHeight();
		ctrlWidth=windowWidth-10;
		ctrlHeight=windowHeight-10;
		AvailViewSize=ctrlWidth-15-legendSize;
		int alnWidth=(int)AvailViewSize;
		scrolledTxtpnAln.setBounds(legendSize+5, 80,alnWidth , 5+aln.getHeight());
		txtpnlegendSelection.setBounds(5,85+aln.getHeight(),legendSize-5,20);
		txtpnlegendExons.setBounds(5,(int) (55-myFONT.getHeight()),legendSize-5,20);
		scrolledTxtpnSelection.setBounds(legendSize+5, 85+aln.getHeight(), alnWidth, 20);
		scrolledTxtpnRefPos.setBounds(legendSize+5, 60, alnWidth, 20);
		scrolledTxtpnExons.setBounds(legendSize+5, (int) (55-myFONT.getHeight()), alnWidth, 20);
		posTypeBox.setBounds(5,60,legendSize-5,20);
		//txtpnUniprot.setBounds();
		slider.setMaximum((int) (scrolledTxtpnAln.getHorizontalScrollBar().getMaximum()-alnWidth+myFONT.getHeight()));
		slider.validate();
		contentPane.validate();
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
	    		        JOptionPane.WARNING_MESSAGE);
			}

		} catch (IOException e) {

			String errMessage = "\"URL Error\"\n"
    		        + "Please check the URL correct.\n"
    		        + "Check also internet connexion! ;-) ";
    		    JOptionPane.showMessageDialog(new JFrame(), errMessage, "URL is not found",
    		        JOptionPane.WARNING_MESSAGE);

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
		contentPane.setBackground(SystemColor.menu);
		contentPane.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		txtpnAln = new JLabel();
		txtpnAln.setVerticalAlignment(SwingConstants.TOP);
		txtpnAln.setBorder(null);
		
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
		scrolledTxtpnAln.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		scrolledTxtpnAln = new JScrollPane(txtpnAln);
		scrolledTxtpnAln.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrolledTxtpnAln.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		scrolledTxtpnAln.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
		
		//txtpnUniprot = new JTextPane();
		//txtpnUniprot.setContentType("text/html");
		//txtpnUniprot.setBackground(Color.WHITE);
		//txtpnUniprot.setEditable(false);

		txtpnAlnSpecies= new JLabel();
		txtpnAlnSpecies.setFont(myFONT.getFont());
		txtpnAlnSpecies.setText(species.getHTML());
		txtpnAlnSpecies.setHorizontalAlignment(SwingConstants.RIGHT);
		txtpnAlnSpecies.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtpnAlnSpecies.setBounds(5, 80, legendSize-5, aln.getHeight());

		txtpnlegendSelection= new JLabel();
		txtpnlegendSelection.setFont(myFONT.getFont());
		txtpnlegendSelection.setHorizontalAlignment(SwingConstants.RIGHT);
		txtpnlegendSelection.setText("Selection");
		txtpnlegendSelection.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		txtpnlegendExons= new JLabel();
		txtpnlegendExons.setFont(myFONT.getFont());
		txtpnlegendExons.setHorizontalAlignment(SwingConstants.RIGHT);
		txtpnlegendExons.setText("Exons switch");
		txtpnlegendExons.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		txtpnRefPos= new JLabel();
		txtpnRefPos.setText(aln.getPos(seqType));
		txtpnRefPos.setFont(myFONT.getFont());
		
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
		scrolledTxtpnRefPos.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrolledTxtpnRefPos.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

		txtpnExons= new JLabel();
		txtpnExons.setText(positions.buildHTML(seqType));
		txtpnExons.setFont(myFONT.getFont());
		
		scrolledTxtpnExons = new JScrollPane(txtpnExons);
		scrolledTxtpnExons.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrolledTxtpnExons.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrolledTxtpnExons.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
		
		txtpnSelection= new JLabel();
		txtpnSelection.setText(selection.buildHTML(aln.getSize(), seqType));
		txtpnSelection.setFont(myFONT.getFont());
		scrolledTxtpnSelection = new JScrollPane(txtpnSelection);
		scrolledTxtpnSelection.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrolledTxtpnSelection.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrolledTxtpnSelection.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
		
		slider = new JSlider();
		slider.setForeground(Color.GRAY);
		slider.setMinimum(0);
		slider.setValue(0);
		slider.setBackground(Color.LIGHT_GRAY);
		slider.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		
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
		    	  
		      }
		    });
		slider.setBounds(195, 10, 311, 20);
		btnUpdateUniprot.setBounds(5, 7, 178, 23);

		update_size();

		contentPane.add(scrolledTxtpnAln);
		// NO PANE FOR UNIPROT contentPane.add(txtpnUniprot);
		contentPane.add(txtpnAlnSpecies);
		contentPane.add(scrolledTxtpnRefPos);
		contentPane.add(scrolledTxtpnExons);
		contentPane.add(scrolledTxtpnSelection);
		contentPane.add(posTypeBox);
		contentPane.add(txtpnlegendExons);
		contentPane.add(slider);
		contentPane.add(btnUpdateUniprot);
		contentPane.add(txtpnlegendSelection);
		contentPane.setLayout(null);
		
		featSelect = new gui.featureSelect(featureFile,frametitle);
		
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
}

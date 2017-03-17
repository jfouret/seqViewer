package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import javax.swing.JComboBox;
import java.awt.Label;


public class startMenu extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7496753617994981739L;
	private JPanel contentPane;
	private JTextField textFieldGenePath;
	private JComboBox<String> seqTypeBox;
	private String suffix_pamlPath= "/out";
	private String suffix_speciesPath= "/species.txt";
	private String suffix_alignmentPath= "/codon_aln.fa";
	private String suffix_positionsPath= "/posDict.tab";
	private String suffix_uniprotPath= "/uniprot.tab";
	/** 
	 * Definition of tools to choose species file and alignment file
	 */
	String alignmentPath = new String();
	String speciesPath = new String();
	String pamlPath = new String();
	String positionsPath = new String();
	String uniprotPath = new String();
	String genCodeChoice = "standard";
	private JTextField txtGenename;
	
	public void SetPaths(String Input_Path){
		alignmentPath=Input_Path+suffix_alignmentPath;
		speciesPath=Input_Path+suffix_speciesPath;
		pamlPath=Input_Path+suffix_pamlPath;
		positionsPath=Input_Path+suffix_positionsPath;
		uniprotPath=Input_Path+suffix_uniprotPath;
		textFieldGenePath.setText(Input_Path);
		Pattern pattern = Pattern.compile(".*\\\\(.*?)$");
		Matcher matcher = pattern.matcher(Input_Path);
		if (matcher.find())
		{
			txtGenename.setText(matcher.group(1));
		}else {
			pattern = Pattern.compile(".*/(.*?)$");
			matcher = pattern.matcher(Input_Path);
			if (matcher.find()) {
				txtGenename.setText(matcher.group(1));
			}else{
				txtGenename.setText("GeneName");
			}
		}
	}

	/**
	 * Create the frame.
	 */
	public startMenu() {	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 394);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnLoadGene = new JButton("Load folder");
		btnLoadGene.setToolTipText("test!!!");
		
		btnLoadGene.setBounds(10, 95, 105, 23);
		contentPane.add(btnLoadGene);
		
		
		/**
		 * Define actions for files
		 */
		
		btnLoadGene.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ae) {
		    	JFileChooser fileChooser = new JFileChooser();
		    	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        //
		        // disable the "All files" option.
		        //
		    	fileChooser.setAcceptAllFileFilterUsed(false);
		        int returnValue = fileChooser.showOpenDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
		          File selectedFile = fileChooser.getSelectedFile();
		          startMenu.this.SetPaths(selectedFile.getPath());
		          
		        }
		      }
		    });

		JSeparator separator = new JSeparator();
		separator.setBounds(22, 46, 389, 13);
		contentPane.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(22, 129, 389, 2);
		contentPane.add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(22, 214, 389, 2);
		contentPane.add(separator_2);
		
		JButton btnVisualize = new JButton("Visualize");
		btnVisualize.setBounds(293, 227, 89, 23);
		contentPane.add(btnVisualize);
		
		btnVisualize.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ae) {
		          String alignmentPath = startMenu.this.alignmentPath;
		          String speciesPath = startMenu.this.speciesPath;
		          tools.genCode chosenGenCode= new tools.genCode(startMenu.this.genCodeChoice);
		          String seqType= startMenu.this.seqTypeBox.getSelectedItem().toString();
		          try{
		        	  gui.VisualizeFrame VisualizeFrame = new gui.VisualizeFrame(alignmentPath,speciesPath,pamlPath,positionsPath,uniprotPath,chosenGenCode,seqType,txtGenename.getText());
			          VisualizeFrame.setVisible(true);
			          VisualizeFrame.setDefaultCloseOperation(gui.VisualizeFrame.DISPOSE_ON_CLOSE);
		          }catch(FileNotFoundException e){
		        	  String errMessage = "\"FileNotFound Error\"\n"
		        		        + "Please check that files paths are correct.\n"
		        		        + "Check also access rights! ;-) ";
		        		    JOptionPane.showMessageDialog(new JFrame(), errMessage, "File is not found",
		        		        JOptionPane.ERROR_MESSAGE);
		          }
		          }
		    });
		
		JFormattedTextField frmtdtxtfldSeqviewerForPositive = new JFormattedTextField();
		frmtdtxtfldSeqviewerForPositive.setFont(new Font("Castellar", Font.BOLD, 10));
		frmtdtxtfldSeqviewerForPositive.setForeground(new Color(0, 0, 255));
		frmtdtxtfldSeqviewerForPositive.setEditable(false);
		frmtdtxtfldSeqviewerForPositive.setHorizontalAlignment(SwingConstants.CENTER);
		frmtdtxtfldSeqviewerForPositive.setBackground(new Color(255, 255, 224));
		frmtdtxtfldSeqviewerForPositive.setText("SeqViewer for positive selection impacts visualization");
		frmtdtxtfldSeqviewerForPositive.setBounds(0, 0, 434, 37);
		contentPane.add(frmtdtxtfldSeqviewerForPositive);
		
		JFormattedTextField frmtdtxtfldLoadAMultiple = new JFormattedTextField();
		frmtdtxtfldLoadAMultiple.setEditable(false);
		frmtdtxtfldLoadAMultiple.setText("Load the gene folder");
		frmtdtxtfldLoadAMultiple.setHorizontalAlignment(SwingConstants.LEFT);
		frmtdtxtfldLoadAMultiple.setBackground(new Color(255, 255, 255));
		frmtdtxtfldLoadAMultiple.setBounds(32, 61, 366, 23);
		contentPane.add(frmtdtxtfldLoadAMultiple);
		
		textFieldGenePath = new JTextField();
		textFieldGenePath.setBounds(125, 96, 299, 20);
		contentPane.add(textFieldGenePath);
		textFieldGenePath.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(10, 227, 46, 23);
		contentPane.add(lblName);
		
		txtGenename = new JTextField();
		txtGenename.setText("geneName");
		txtGenename.setBounds(48, 227, 86, 23);
		contentPane.add(txtGenename);
		txtGenename.setColumns(10);
		
		Label label = new Label("Alignment Type:");
		label.setBounds(10, 264, 89, 23);
		contentPane.add(label);
		
		seqTypeBox = new JComboBox<String>();
		seqTypeBox.setBounds(100, 264, 161, 23);
		seqTypeBox.addItem("Nucleotids");
		seqTypeBox.addItem("Amino acids");
		seqTypeBox.addItem("Codons");
		seqTypeBox.setSelectedItem("Nucleotids");
		contentPane.add(seqTypeBox);
	}
}

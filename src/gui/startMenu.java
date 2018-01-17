package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import javax.swing.JComboBox;
import java.awt.Label;
import javax.swing.JTextPane;
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
	private String suffix_namePath= "/name.tab";
	/** 
	 * Definition of tools to choose species file and alignment file
	 */
	String alignmentPath = new String();
	String speciesPath = new String();
	String pamlPath = new String();
	String positionsPath = new String();
	String uniprotPath = new String();
	String namePath = new String();
	String WorkDir = ".";
	String genCodeChoice = "standard";
	database.nameFile nameFile;
	private JTextField txtGenename;
	private JTextField textRefSeq;
	private JTextField textUniProt;
	private JTextField textkgID;
	
	public void SetPaths(String Input_Path){
		alignmentPath=Input_Path+suffix_alignmentPath;
		speciesPath=Input_Path+suffix_speciesPath;
		pamlPath=Input_Path+suffix_pamlPath;
		positionsPath=Input_Path+suffix_positionsPath;
		uniprotPath=Input_Path+suffix_uniprotPath;
		namePath=Input_Path+suffix_namePath;
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
		try {
			nameFile= new database.nameFile(namePath);
			textRefSeq.setText(nameFile.getRefSeq());
			textUniProt.setText(nameFile.getUniprot());
			textkgID.setText(nameFile.getKgID());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			textRefSeq.setText("No name.tab file");
			textUniProt.setText("No name.tab file");
			textkgID.setText("No name.tab file");
		}
		
		
	}
	/**
	 * Create the frame.
	 */
	public startMenu() {	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 405);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnLoadGene = new JButton("Load");
		JButton btnDefaultFolder = new JButton("Set default folder");
		btnDefaultFolder.setFont(new Font("Tahoma", Font.PLAIN, 9));
		
		btnLoadGene.setBounds(15, 88, 105, 23);
		btnDefaultFolder.setBounds(15, 114, 145, 13);
		contentPane.add(btnLoadGene);
		contentPane.add(btnDefaultFolder);
		
		
		/**
		 * Define actions for files
		 */
		
		btnLoadGene.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ae) {
		    	  JFileChooser fileChooser = new JFileChooser(startMenu.this.WorkDir);
		    	  fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    	fileChooser.setAcceptAllFileFilterUsed(false);
		        int returnValue = fileChooser.showOpenDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
		          File selectedFile = fileChooser.getSelectedFile();
		          startMenu.this.SetPaths(selectedFile.getPath());
		        }
		      }
		    });
		
		btnDefaultFolder.addActionListener(new ActionListener() {
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
		          startMenu.this.WorkDir=selectedFile.getAbsolutePath();
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
		btnVisualize.setBounds(66, 261, 168, 26);
		contentPane.add(btnVisualize);
		
		btnVisualize.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ae) {
		          String alignmentPath = startMenu.this.alignmentPath;
		          String speciesPath = startMenu.this.speciesPath;
		          alignment.genCode chosenGenCode= new alignment.genCode(startMenu.this.genCodeChoice);
		          String seqType= startMenu.this.seqTypeBox.getSelectedItem().toString();
		          try{
		        	  gui.VisualizeFrame VisualizeFrame = new gui.VisualizeFrame(alignmentPath,speciesPath,pamlPath,positionsPath,uniprotPath,chosenGenCode,seqType,txtGenename.getText(),nameFile.getUniprot());
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
		
		JFormattedTextField frmtdtxtfldLoadAMultiple = new JFormattedTextField();
		frmtdtxtfldLoadAMultiple.setEditable(false);
		frmtdtxtfldLoadAMultiple.setFont(new Font("Tahoma", Font.BOLD, 12));
		frmtdtxtfldLoadAMultiple.setHorizontalAlignment(SwingConstants.CENTER);
		frmtdtxtfldLoadAMultiple.setText("Load the gene folder");
		frmtdtxtfldLoadAMultiple.setBackground(new Color(255, 255, 255));
		frmtdtxtfldLoadAMultiple.setBounds(32, 54, 366, 23);
		contentPane.add(frmtdtxtfldLoadAMultiple);
		
		textFieldGenePath = new JTextField();
		textFieldGenePath.setBounds(125, 88, 299, 23);
		contentPane.add(textFieldGenePath);
		textFieldGenePath.setColumns(10);
		
		JLabel lblName = new JLabel("Name :");
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setBounds(10, 142, 96, 23);
		contentPane.add(lblName);
		
		txtGenename = new JTextField();
		txtGenename.setText("geneName");
		txtGenename.setBounds(116, 142, 86, 23);
		contentPane.add(txtGenename);
		txtGenename.setColumns(10);
		
		Label label = new Label("Alignment Type:");
		label.setAlignment(Label.RIGHT);
		label.setBounds(42, 232, 105, 23);
		contentPane.add(label);

		ClassLoader classLoader = getClass().getClassLoader();
		JLabel v3dimgLab = new JLabel();
		v3dimgLab.setBounds(10, 296, 155, 56);
		ImageIcon v3dimg = new ImageIcon(new ImageIcon(classLoader.getResource("ressources/viroscan.jpg")).getImage().getScaledInstance(v3dimgLab.getWidth(), v3dimgLab.getHeight(), Image.SCALE_DEFAULT));
		v3dimgLab.setIcon(v3dimg);
		contentPane.add(v3dimgLab);
		
		JLabel ciriimgLab = new JLabel();
		ciriimgLab.setBounds(175, 298, 59, 56);
		ImageIcon ciriimg = new ImageIcon(new ImageIcon(classLoader.getResource("ressources/CIRI.png")).getImage().getScaledInstance(ciriimgLab.getWidth(), ciriimgLab.getHeight(), Image.SCALE_DEFAULT));
		ciriimgLab.setIcon(ciriimg);
		contentPane.add(ciriimgLab);
		
		seqTypeBox = new JComboBox<String>();
		seqTypeBox.setBounds(156, 232, 161, 23);
		seqTypeBox.addItem("Nucleotids");
		seqTypeBox.addItem("Amino acids");
		seqTypeBox.addItem("Codons");
		seqTypeBox.setSelectedItem("Nucleotids");
		contentPane.add(seqTypeBox);
		
		JLabel lblTitle = new JLabel("<html><div align=\"center\"><b>SeqViewer: </b><br><i> Impacts visualizer of branch-specific evolution for sites/domains</div></html>");
		lblTitle.setForeground(Color.BLUE);
		lblTitle.setBackground(new Color(153, 255, 153));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblTitle.setBounds(5, 5, 420, 44);
		contentPane.add(lblTitle);
				
		JTextPane txtpnHuh = new JTextPane();
		txtpnHuh.setContentType("text/html");
		txtpnHuh.setEditable(false);
		txtpnHuh.setText("<html><div align=\"center\"> Author : Julien FOURET<br> Version 1.4.0 </div></html>");
		txtpnHuh.setBounds(237, 298, 197, 54);
		contentPane.add(txtpnHuh);
		
		JLabel lblRefseqId = new JLabel("RefSeq ID :");
		lblRefseqId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRefseqId.setBounds(212, 142, 86, 23);
		contentPane.add(lblRefseqId);
		
		textRefSeq = new JTextField();
		textRefSeq.setText("");
		textRefSeq.setColumns(10);
		textRefSeq.setBounds(308, 142, 86, 23);
		contentPane.add(textRefSeq);
		
		JLabel lblUniprotId = new JLabel("UniProt ID :");
		lblUniprotId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUniprotId.setBounds(212, 176, 86, 23);
		contentPane.add(lblUniprotId);
		
		textUniProt = new JTextField();
		textUniProt.setText("");
		textUniProt.setColumns(10);
		textUniProt.setBounds(308, 176, 86, 23);
		contentPane.add(textUniProt);
		
		JLabel lblKnowngeneId = new JLabel("KnownGene ID :");
		lblKnowngeneId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblKnowngeneId.setBounds(10, 176, 96, 23);
		contentPane.add(lblKnowngeneId);
		
		textkgID = new JTextField();
		textkgID.setText("");
		textkgID.setColumns(10);
		textkgID.setBounds(116, 176, 86, 23);
		contentPane.add(textkgID);
		
	}
}

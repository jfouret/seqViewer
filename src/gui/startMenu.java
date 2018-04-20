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
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import javax.swing.JComboBox;
import java.awt.Label;
import javax.swing.JTextPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
public class startMenu extends JFrame {
	
	private static final long serialVersionUID = 7496753617994981739L;
	private JPanel contentPane;
	private JTextField textFieldGenePath;
	private JComboBox<String> seqTypeBox;
	private String suffix_pamlPath= "/rst";
	private String suffix_treePath= "/tree.nh";
	private String suffix_alignmentPath= "/aln.fa";
	private String suffix_exonsPath= "/exons.bed";
	private String suffix_blockPath= "/block.bed";
	private String suffix_spidPath= "/spid.txt";
	private String suffix_database= "/table.txt";
	private String spid;
	private String ref_species;
	
	/** 
	 * Definition of tools to choose species file and alignment file
	 */
	
	String alignmentPath = new String();
	String treePath = new String();
	String pamlPath = new String();
	String exonsPath = new String();
	String blockPath = new String();
	String spidPath = new String();
	String databasePath = new String();
	String WorkDir = ".";
	String genCodeChoice = "standard";
	private JTable table;
	
	public void SetPaths(String Input_Path){
		alignmentPath=Input_Path+suffix_alignmentPath;
		treePath=Input_Path+suffix_treePath;
		pamlPath=Input_Path+suffix_pamlPath;
		exonsPath=Input_Path+suffix_exonsPath;
		blockPath=Input_Path+suffix_blockPath;
		spidPath=Input_Path+suffix_spidPath;
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
		
		File spidFile=new File(spidPath);
		Scanner sc;

		try {
			sc = new Scanner(spidFile);
			String[] split = sc.nextLine().trim().split(";");
			spid=split[0];
			ref_species=split[1];
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		textRefSeq.setText("");
		textUniProt.setText(spid);
		textkgID.setText("");
	}
	/**
	 * Create the frame.
	 */
	public startMenu() {	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 818, 535);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnLoadGene = new JButton("Load");
		
		btnLoadGene.setBounds(46, 88, 105, 23);
		contentPane.add(btnLoadGene);
		
		
		/**
		 * Define actions for files
		 */
		
		btnLoadGene.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ae) {
		    	  JFileChooser fileChooser = new JFileChooser(startMenu.this.WorkDir);
		    	  //fileChooser.setFileSelectionMode(JFileChooser.);
		    	fileChooser.setAcceptAllFileFilterUsed(false);
		        int returnValue = fileChooser.showOpenDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
		          File selectedFile = fileChooser.getSelectedFile();
		          startMenu.this.SetPaths(selectedFile.getPath());
		        }
		      }
		    });
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(22, 129, 748, 2);
		contentPane.add(separator_1);
		
		JButton btnVisualize = new JButton("Visualize");
		btnVisualize.setBounds(609, 142, 161, 26);
		contentPane.add(btnVisualize);
		
		btnVisualize.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ae) {
		          String alignmentPath = startMenu.this.alignmentPath;
		          String treePath = startMenu.this.treePath;
		          alignment.genCode chosenGenCode= new alignment.genCode(startMenu.this.genCodeChoice);
		          String seqType= startMenu.this.seqTypeBox.getSelectedItem().toString();
		          try{
		        	  gui.VisualizeFrame VisualizeFrame = new gui.VisualizeFrame(alignmentPath,treePath,pamlPath,exonsPath,blockPath,chosenGenCode,seqType,txtGenename.getText(),spid,ref_species);
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
		frmtdtxtfldLoadAMultiple.setText("Load the database");
		frmtdtxtfldLoadAMultiple.setBackground(new Color(255, 255, 255));
		frmtdtxtfldLoadAMultiple.setBounds(165, 54, 366, 23);
		contentPane.add(frmtdtxtfldLoadAMultiple);
		
		textFieldGenePath = new JTextField();
		textFieldGenePath.setBounds(210, 88, 272, 23);
		contentPane.add(textFieldGenePath);
		textFieldGenePath.setColumns(10);

		ClassLoader classLoader = getClass().getClassLoader();
		JLabel v3dimgLab = new JLabel();
		v3dimgLab.setBounds(0, 0, 155, 56);
		ImageIcon v3dimg = new ImageIcon(new ImageIcon(classLoader.getResource("ressources/viroscan.jpg")).getImage().getScaledInstance(v3dimgLab.getWidth(), v3dimgLab.getHeight(), Image.SCALE_DEFAULT));
		v3dimgLab.setIcon(v3dimg);
		contentPane.add(v3dimgLab);
		
		JLabel ciriimgLab = new JLabel();
		ciriimgLab.setBounds(743, 3, 59, 56);
		ImageIcon ciriimg = new ImageIcon(new ImageIcon(classLoader.getResource("ressources/CIRI.png")).getImage().getScaledInstance(ciriimgLab.getWidth(), ciriimgLab.getHeight(), Image.SCALE_DEFAULT));
		ciriimgLab.setIcon(ciriimg);
		contentPane.add(ciriimgLab);
		
		seqTypeBox = new JComboBox<String>();
		seqTypeBox.setBounds(424, 144, 161, 23);
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
		lblTitle.setBounds(165, -1, 420, 44);
		contentPane.add(lblTitle);
				
		JTextPane txtpnHuh = new JTextPane();
		txtpnHuh.setContentType("text/html");
		txtpnHuh.setEditable(false);
		txtpnHuh.setText("<html><div align=\"center\"> Author : Julien FOURET<br> Version 1.4.0 </div></html>");
		txtpnHuh.setBounds(563, 57, 197, 54);
		contentPane.add(txtpnHuh);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(800, 182, -802, 314);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setBounds(0, 182, 802, 314);
		
		JScrollPane.add(table);
		
	}
}

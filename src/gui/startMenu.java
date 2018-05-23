package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import java.io.BufferedReader;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;
import javax.swing.JComboBox;
import javax.swing.JTextPane;
import javax.swing.RowFilter;
import javax.swing.JTable;

public class startMenu extends JFrame {
	
	private static final long serialVersionUID = 7496753617994981739L;
	private JPanel contentPane;
	private JComboBox<String> seqTypeBox;
	private JScrollPane scrollpane;
	private JTable table;
	private JTextField txtfilter;
	private String suffix_pamlPath= "/rst";
	private String suffix_treePath= "/tree.nh";
	private String suffix_alignmentPath= "/aln.fa";
	private String suffix_exonsPath= "/exons.bed";
	private String suffix_blockPath= "/block.bed";
	private String table_path= "table.txt";
	private String ref_path= "ref.txt";
	private String spid;
	private String ref_species;
	DefaultTableModel model;
	
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
	ZipFile database ;
	String genCodeChoice = "standard";
	
	
	public void SetPaths(String Input_Path){
		alignmentPath=Input_Path+suffix_alignmentPath;
		treePath=Input_Path+suffix_treePath;
		pamlPath=Input_Path+suffix_pamlPath;
		exonsPath=Input_Path+suffix_exonsPath;
		blockPath=Input_Path+suffix_blockPath;
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(database.getInputStream(database.getEntry(ref_path))));
			ref_species=bufferedReader.readLine().trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public startMenu() {	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 818, 535);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnLoadGene = new JButton("Load the database");
		
		btnLoadGene.setBounds(107, 67, 143, 34);
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
		          
		       try {
					database = new ZipFile(fileChooser.getSelectedFile());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		          
		          model =new DefaultTableModel();
		          model.setColumnIdentifiers(new Object[]{"UCSC id","Uniprot id","Primary name","Secondary names"});
				try {
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(database.getInputStream(database.getEntry(table_path))));
			        String line=bufferedReader.readLine();
					while (line!=null){
			      	  model.addRow(line.trim().split("\t"));
			      	  line=bufferedReader.readLine();
			        }
			        table.setModel(model);
			        contentPane.remove(scrollpane);
			        scrollpane= new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					scrollpane.setBounds(0, 180, 802, 314);
					contentPane.add(scrollpane);
				} catch (IOException e) {
					e.printStackTrace();
				}
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
		    	  int selected_row=table.convertRowIndexToModel(table.getSelectedRow());
		    	  String gene_id = (String) table.getModel().getValueAt(selected_row, 0);
		    	  spid = (String) table.getModel().getValueAt(selected_row, 1);
		    	  String gene_name = (String) table.getModel().getValueAt(selected_row, 3);
		    	  startMenu.this.SetPaths(gene_id);
		          alignment.genCode chosenGenCode= new alignment.genCode(startMenu.this.genCodeChoice);
		          String seqType= startMenu.this.seqTypeBox.getSelectedItem().toString();
		          try{
		        	  gui.VisualizeFrame VisualizeFrame = new gui.VisualizeFrame(alignmentPath,treePath,pamlPath,exonsPath,blockPath,chosenGenCode,seqType,gene_name,spid,ref_species,database);
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
		
		txtfilter = new JTextField();
		txtfilter.setBounds(153, 144, 161, 23);
		contentPane.add(txtfilter);

		txtfilter.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
				    filterTable();
				  }
				  public void removeUpdate(DocumentEvent e) {
					 filterTable();
				  }
				  public void insertUpdate(DocumentEvent e) {
					 filterTable();
				  }

				  public void filterTable() {
					  System.out.println("YO");
					  TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(model);
					  RowFilter<DefaultTableModel, Object> rf = null;
					  
					  List<RowFilter<Object,Object>> rfs = 
					              new ArrayList<RowFilter<Object,Object>>();

					  try {
					      String text = txtfilter.getText();
					      
					      char[] letters = text.toCharArray();
					      
					      StringBuilder sb = new StringBuilder();
					      
					      for (int i =0;i< letters.length;i++){
					    	  String upper=Character.toString(Character.toUpperCase(letters[i]));
					      	  String lower=Character.toString(Character.toLowerCase(letters[i]));
					    	  sb.append("["+upper+lower+"]{1}");
					      }
					      rfs.add(RowFilter.regexFilter(sb.toString(),0,1,2,3,Pattern.CASE_INSENSITIVE));
					      
					      rf = RowFilter.orFilter(rfs);

					  } catch (java.util.regex.PatternSyntaxException e) {
					          return;
					  }
					  sorter.setRowFilter(rf);
					  table.setRowSorter(sorter);
				  }
				});
		
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
		lblTitle.setBounds(216, 11, 420, 44);
		contentPane.add(lblTitle);
				
		JTextPane txtpnHuh = new JTextPane();
		txtpnHuh.setContentType("text/html");
		txtpnHuh.setEditable(false);
		txtpnHuh.setText("<html><div align=\"center\"> Author : Julien FOURET<br> Version 2.0.1 </div></html>");
		txtpnHuh.setBounds(326, 54, 197, 54);
		contentPane.add(txtpnHuh);
		
		
		
		scrollpane= new JScrollPane();
		scrollpane.setBounds(0, 180, 802, 314);
		contentPane.add(scrollpane);
		
		JLabel lblSearch = new JLabel("SEARCH :");
		lblSearch.setBounds(42, 142, 94, 26);
		contentPane.add(lblSearch);
		
		JTextPane txtpnNbYou = new JTextPane();
		txtpnNbYou.setEditable(false);
		txtpnNbYou.setForeground(Color.RED);
		txtpnNbYou.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtpnNbYou.setText("NB : you need a working internet connection for the software to query protein annotation via uniprot API");
		txtpnNbYou.setBounds(32, 108, 770, 23);
		contentPane.add(txtpnNbYou);
		
		table = new JTable();
		table.setBounds(0, 0, 802, 314);

	}
}

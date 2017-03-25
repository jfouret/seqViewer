package gui;

import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import tools.featureFile;
import javax.swing.JCheckBox;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.BorderLayout;

public class featureSelect extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public  HashMap<String,JCheckBox> idCheck= new HashMap<String,JCheckBox>() ;
	public  HashMap<String,Canvas> idCanvas = new HashMap<String,Canvas>() ;
	private JPanel contentPane;
	
	public HashMap<String,Boolean> UpdateAvailableType(){
		HashMap<String,Boolean> availableType  = new HashMap<String, Boolean>();
		for (String id: idCheck.keySet()){
			availableType.put(id,idCheck.get(id).isSelected());
		}
		return(availableType);
	}
	
	public featureSelect(featureFile featFile,String input_title) {
		
		JCheckBox checkBox = new JCheckBox("New check box");
		getContentPane().add(checkBox, BorderLayout.NORTH);
		setTitle( "Uniprot legends :"+input_title );
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		//getContentPane().setLayout(null);
		int i = 0;
		for (String id: featFile.availableType.keySet()){
			//System.out.println(id);
			//System.out.println(featFile.featType.getCol(id));
			idCheck.put(id,new JCheckBox(featFile.featType.getDesc(id)));
			idCheck.get(id).setBounds(50, 10+i*25, 300, 23);
			idCheck.get(id).setToolTipText(id);
			getContentPane().add(idCheck.get(id));
			idCanvas.put(id,new Canvas());
			idCanvas.get(id).setBackground(Color.decode(featFile.featType.getCol(id)));
			idCanvas.get(id).setBounds(10, 10+i*25, 35, 23);
			contentPane.add(idCanvas.get(id));
			i+=1;
			contentPane.add(idCanvas.get(id));
			contentPane.add(idCheck.get(id));
		}
		this.setSize(350, 50+i*25);
	}
}




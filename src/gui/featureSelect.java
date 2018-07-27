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

import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import features.featureAPI;

import javax.swing.JCheckBox;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.BorderLayout;

public class featureSelect extends JPanel {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	public  HashMap<String,JCheckBox> idCheck= new HashMap<String,JCheckBox>() ;
	public  HashMap<String,Canvas> idCanvas = new HashMap<String,Canvas>() ;
	
	public HashMap<String,Boolean> UpdateAvailableType(){
		HashMap<String,Boolean> availableType  = new HashMap<String, Boolean>();
		for (String id: idCheck.keySet()){
			availableType.put(id,idCheck.get(id).isSelected());
			
		}
		return(availableType);
	}
	
	public featureSelect(featureAPI featFile,String input_title) {
		
		JCheckBox checkBox = new JCheckBox("New check box");
		add(checkBox, BorderLayout.NORTH);
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(null);
		//getContentPane().setLayout(null);
		int i = 0;
		for (String id: featFile.availableType.keySet()){
			//System.out.println(id);
			//System.out.println(featFile.featType.getCol(id));
			idCheck.put(id,new JCheckBox(featFile.featType.getDesc(id)));
			idCheck.get(id).setBounds(50, 10+i*25, 300, 23);
			idCheck.get(id).setToolTipText(id);
			add(idCheck.get(id));
			idCanvas.put(id,new Canvas());
			//System.out.println(id);
			idCanvas.get(id).setBackground(Color.decode(featFile.featType.getCol(id)));
			idCanvas.get(id).setBounds(10, 10+i*25, 35, 23);
			add(idCanvas.get(id));
			i+=1;
			add(idCanvas.get(id));
			add(idCheck.get(id));
		}
		this.setSize(350, 50+i*25);
	}
}




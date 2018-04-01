package features;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JLabel;
import evolution.positions;
import tools.myFONT;

public class featureAPI {
	
	/**
	 * 
	 */
	
	private int iterNum;
	public featureType featType= new featureType();
	public HashMap<String,Boolean> availableType  = new HashMap<String, Boolean>();
	public feature[] featureArray;
	
	public featureAPI(URL uniprotURL,positions positions) throws IOException {
		//super(arg0);
		
        BufferedReader in = new BufferedReader(new InputStreamReader(uniprotURL.openStream()));
		iterNum=0;
		String inputLine = in.readLine();
		String currentLine=inputLine.trim();
		String[] split;
		ArrayList<feature> featureArrayList = new ArrayList<feature>();
		Object value = null;
		while ((inputLine = in.readLine()) != null){
			currentLine=inputLine.trim();
			split=currentLine.split("  +");
			//0=FT (if)
			//1= class
			//2= start
			//3= end
			//4++= Descritpion 
			// step 1 add in featureArrayList
			if (split[0].equals("FT")){
				if (featType.isClass(split[1])){
					if (split[3].equals("?")){
						split[3]=split[2];
					}
					if (split.length==5){
						featureArrayList.add(new feature(split[1],Integer.parseInt(split[2])-1, Integer.parseInt(split[3]),split[4],featType,positions,iterNum));
					}else{
						featureArrayList.add(new feature(split[1],Integer.parseInt(split[2])-1, Integer.parseInt(split[3]),"",featType,positions,iterNum));
					}
					iterNum+=1; // num == position in feature array !!!
					// step 2 check in availableType is the type is present ? add if not with False
					value = availableType.get(split[1]);
					if (value == null) {
						availableType.put(split[1],false);
					}
				}else{
					featureArrayList.get(iterNum-1).appendDescription("<br>"+split[1]);
				}
			}
		}
		// step final ==> arraylist in array
		featureArray= featureArrayList.toArray(new feature[featureArrayList.size()]);
		in.close();
	}
	
	public void update_AvailableType(HashMap<String,Boolean> newAvailableType){
		availableType=newAvailableType;
	}
	
	public JLabel[] getLabels(String seqType) {
		List<JLabel> Labels = new ArrayList<JLabel>();
		JLabel newLabel;
		for (feature feat: featureArray){
			if (feat.isSelected(availableType)) {
				newLabel= new JLabel();
				newLabel.setBorder(null);
				newLabel.setText(feat.getHTML_line(seqType));
				newLabel.setToolTipText("<html><b>"+feat.getType()+" : </b>"+feat.getDescription()+"</html>");
				Labels.add(newLabel);
			}
		}
		JLabel[] array = new JLabel[Labels.size()];
		Labels.toArray(array);
		return array;
	}
	
	public long getHeight(){
		return(featureArray.length*myFONT.getFontSize());
	}
	
	public String getToolTipText(Integer input_num){
		return("<html><b>"+featureArray[input_num].getType()+" : </b>"+featureArray[input_num].getDescription()+"</html>");
	}
	
}


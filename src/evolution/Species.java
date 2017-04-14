package evolution;
import java.util.ArrayList;

public class Species {
	
	private ArrayList<String> foreground = new ArrayList<String>();
	private ArrayList<String> background = new ArrayList<String>();
	
	public void setForeground(ArrayList<String> specList){
		foreground=specList;
	}
	
	public ArrayList<String> getForeground(){
		return(foreground);
	}
	
	public void setBackground(ArrayList<String> specList){
		background=specList;
	}
	
	public ArrayList<String> getBackground(){
		return(background);
	}
	
	public Species() {
		// TODO Auto-generated constructor stub
	}

}

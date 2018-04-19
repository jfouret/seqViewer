package evolution;

public class BedEntry {
	
	public String template;
	public int start;
	public int end;
	
	public BedEntry(String Line) {
		String[] split = Line.split("\t");
		this.template=split[0];
		this.start=Integer.parseInt(split[1]); // 0-based IN
		this.end=Integer.parseInt(split[2]); //0-based OUT
	}
	
	

}

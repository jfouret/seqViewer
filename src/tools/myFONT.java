package tools;

public class myFONT {
	//public static final String fontFamilly= "'DejaVu Sans Mono', monospace" ;
	public static OSvalidator OSvalidator=new OSvalidator();
	public static final String fontFamilly= "'Courier New', Courier, monospace" ;
	public static final String fontSize= "13px" ;
	public static Long fontHeight = (long) 21 ;
	public static Long fontwidth = (long) 11.5 ;

	public static Double getWidth(){
		if (tools.OSvalidator.isWindows() | tools.OSvalidator.isMac()){
			return 10.15;
		}else{
			return 10.0;
		}
	}
	
	public static String getFontFamilly(){
		if (tools.OSvalidator.isWindows() | tools.OSvalidator.isMac()){
			return "'Courier New', Courier, monospace";
		}else{
			return "'DejaVu Sans Mono', monospace";
		}
	}
	

} 


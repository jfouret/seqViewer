package tools;

public class myFONT {
	//public static final String fontFamilly= "'DejaVu Sans Mono', monospace" ;
	public static OSvalidator OSvalidator=new OSvalidator();

	public static String getFontFamilly(){
		if (tools.OSvalidator.isWindows() | tools.OSvalidator.isMac()){
			return "'Courier New', Courier, monospace";
		}else{
			return "'DejaVu Sans Mono', monospace";
		}
	}
	
	public static final String fontFamilly= "'Courier New', Courier, monospace" ;
	public static final String fontSize= "13px" ;
	public static final Long fontHeight = (long) 21 ;
	public static final Long fontwidth = (long) 11 ;
} 


package tools;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Font;

public class myFONT {
	//public static final String fontFamilly= "'DejaVu Sans Mono', monospace" ;
	private static int fontSize= 13;
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	private static Graphics2D g = image.createGraphics();
	public static String getFontFamilly(){
		String fontFamilly;
		if ((OS.indexOf("win") >= 0)| (OS.indexOf("mac") >= 0)){
			fontFamilly= "'Courier New', Courier, monospace";
		}else{
			fontFamilly= "'DejaVu Sans Mono', monospace";
		}
		return fontFamilly;
	}
	public static int getFontSize(){
		return fontSize;
	}
	public static Double getWidth(){
		String fontFamilly;
		if ((OS.indexOf("win") >= 0)| (OS.indexOf("mac") >= 0)){
			fontFamilly= "Courier New";
		}else{
			fontFamilly= "DejaVu Sans Mono";
		}
		Font theFont=new Font(fontFamilly,Font.PLAIN,fontSize);
		g.setFont(theFont);
		return (double) (g.getFontMetrics(theFont).charWidth('A'));
		//return (double) (g.getFontMetrics(theFont).stringWidth("0000000000111111111100000000001111111111000000000011111111110000000000111111111100000000001111111111")/100);
	}
	
	public static Double getHeight(){
		String fontFamilly;
		if ((OS.indexOf("win") >= 0)| (OS.indexOf("mac") >= 0)){
			fontFamilly= "Courier New";
		}else{
			fontFamilly= "DejaVu Sans Mono";
		}
		Font theFont=new Font(fontFamilly,Font.PLAIN,fontSize);
		g.setFont(theFont);
		return (double) (g.getFontMetrics(theFont).getHeight());
		//return (double) (g.getFontMetrics(theFont).stringWidth("0000000000111111111100000000001111111111000000000011111111110000000000111111111100000000001111111111")/100);
	}
	
	public static Font getFont(){
		String fontFamilly;
		if ((OS.indexOf("win") >= 0)| (OS.indexOf("mac") >= 0)){
			fontFamilly= "Courier New";
		}else{
			fontFamilly= "DejaVu Sans Mono";
		}
		return new Font(fontFamilly,Font.PLAIN,fontSize);
	}
} 


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
	public Double getWidth(){
		String fontFamilly;
		if ((OS.indexOf("win") >= 0)| (OS.indexOf("mac") >= 0)){
			fontFamilly= "Courier New";
		}else{
			fontFamilly= "DejaVu Sans Mono";
		}
		Font theFont=new Font(fontFamilly,Font.PLAIN,fontSize);
		g.setFont(theFont);
		return (double) (g.getFontMetrics(theFont).charWidth('A'));	
	}
	
	public void modSize(int alter){
		fontSize=fontSize+alter;
	}
	
	public Double getHeight(){
		String fontFamilly;
		if ((OS.indexOf("win") >= 0)| (OS.indexOf("mac") >= 0)){
			fontFamilly= "Courier New";
		}else{
			fontFamilly= "DejaVu Sans Mono";
		}
		Font theFont=new Font(fontFamilly,Font.PLAIN,fontSize);
		g.setFont(theFont);
		
		return (double) (g.getFontMetrics(theFont).getHeight());
		
		
	}
	
	public Font getFont(){
		String fontFamilly;
		if ((OS.indexOf("win") >= 0)| (OS.indexOf("mac") >= 0)){
			fontFamilly= "Courier New";
		}else{
			fontFamilly= "DejaVu Sans Mono";
		}
		return new Font(fontFamilly,Font.PLAIN,fontSize);
	}
} 


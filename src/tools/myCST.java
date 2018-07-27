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

import java.awt.Color;

public class myCST {
	public static final char BLANK=' ';
	public static final char BLOCK='\u2588';
	public static final char SEMIBLOCK='\u25AC';
	public static final char LINE='\u25A0';
	public static final char UNFILLARROW='\u2206';
	public static final char FILLARROW='\u25B2';
	public static final char STARTARROW='\u25BA';
	public static final char STOPSIGN='\u2666';
	public static final Color backColor = new Color(255, 255, 240);
	public static final String backColorString = "#FFFFF0";
}

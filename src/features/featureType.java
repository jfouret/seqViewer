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

package features;

import java.util.HashMap;

public class featureType {
	private HashMap<String,String> id2desc=new HashMap<String,String>();
	private HashMap<String,String> id2type=new HashMap<String,String>();
	private HashMap<String,String> id2col=new HashMap<String,String>();
	private String[] colors= {"#1f78b4","#fdbf6f","#a6cee3","#33a02c","#fb9a99","#b2df8a","#e31a1c"};
	public featureType() {
		// TODO Auto-generated constructor stub
		id2desc.put("CHAIN","PTM / Processing");id2desc.put("COMPBIAS","Compositional bias"); id2desc.put("TRANSMEM","Transmembran"); id2desc.put("SIGNAL","Signal peptide"); id2desc.put("DOMAIN","Domain"); id2desc.put("COILED","Coiled coil"); id2desc.put("NP_BIND","Nucleotid binding"); id2desc.put("MOTIF","Motif"); id2desc.put("ACT_SITE","Active site"); id2desc.put("BINDING","Binding site"); id2desc.put("INIT_MET","Initial metionine"); id2desc.put("LIPID","Lipidation"); id2desc.put("METAL","Metal binding"); id2desc.put("ZN_FING","Zinc Finger"); id2desc.put("DISULFID","Disulfid Bridge"); id2desc.put("PROPEP","Propeptide"); id2desc.put("CARBOHYD","Glycosylation"); id2desc.put("TOPO_DOM","Topological domain"); id2desc.put("REPEAT","Repeat"); id2desc.put("REGION","Region"); id2desc.put("NON_TER","Non-terminal residue"); id2desc.put("MOD_RES","Modified residue"); id2desc.put("CONFLICT","Conflict"); id2desc.put("STRAND","Strand"); id2desc.put("HELIX","Helix"); id2desc.put("TURN","Turn"); id2desc.put("VAR_SEQ","Alternative sequences"); id2desc.put("NON_CONS","Non-adjacent residues"); id2desc.put("SITE","Site"); id2desc.put("VARIANT","Natural variant"); id2desc.put("MUTAGEN","Mutagenesis"); id2desc.put("UNSURE","Sequence Uncertainity"); id2desc.put("CA_BIND","Calcium binding"); id2desc.put("CROSSLNK","Cross-link"); id2desc.put("TRANSIT","Transit peptide"); id2desc.put("PEPTIDE","Peptide"); id2desc.put("DNA_BIND","DNA-binding"); id2desc.put("INTRAMEM","Intramembrane"); id2desc.put("NON_STD","Non standard residue");
		id2col.put("CHAIN",colors[0]);id2col.put("COMPBIAS",colors[1]); id2col.put("TRANSMEM",colors[0]); id2col.put("SIGNAL",colors[2]); id2col.put("DOMAIN",colors[2]); id2col.put("COILED",colors[0]); id2col.put("NP_BIND",colors[3]); id2col.put("MOTIF",colors[2]); id2col.put("ACT_SITE",colors[4]); id2col.put("BINDING",colors[4]); id2col.put("INIT_MET",colors[4]); id2col.put("LIPID",colors[4]); id2col.put("METAL",colors[4]); id2col.put("ZN_FING",colors[3]); id2col.put("DISULFID",colors[5]); id2col.put("PROPEP",colors[0]); id2col.put("CARBOHYD",colors[4]); id2col.put("TOPO_DOM",colors[0]); id2col.put("REPEAT",colors[2]); id2col.put("REGION",colors[2]); id2col.put("NON_TER",colors[6]); id2col.put("MOD_RES",colors[4]); id2col.put("CONFLICT",colors[6]); id2col.put("STRAND",colors[0]); id2col.put("HELIX",colors[0]); id2col.put("TURN",colors[0]); id2col.put("VAR_SEQ",colors[6]); id2col.put("NON_CONS",colors[4]); id2col.put("SITE",colors[4]); id2col.put("VARIANT",colors[6]); id2col.put("MUTAGEN",colors[6]); id2col.put("UNSURE",colors[6]); id2col.put("CA_BIND",colors[4]); id2col.put("CROSSLNK",colors[5]); id2col.put("TRANSIT",colors[2]); id2col.put("PEPTIDE",colors[2]); id2col.put("DNA_BIND",colors[3]); id2col.put("INTRAMEM",colors[2]); id2col.put("NON_STD",colors[1]);
		id2type.put("CHAIN","interval");id2type.put("COMPBIAS","interval"); id2type.put("TRANSMEM","interval"); id2type.put("SIGNAL","interval"); id2type.put("DOMAIN","interval"); id2type.put("COILED","interval"); id2type.put("NP_BIND","interval"); id2type.put("MOTIF","interval"); id2type.put("ACT_SITE","site"); id2type.put("BINDING","site"); id2type.put("INIT_MET","site"); id2type.put("LIPID","site"); id2type.put("METAL","site"); id2type.put("ZN_FING","interval"); id2type.put("DISULFID","2site"); id2type.put("PROPEP","interval"); id2type.put("CARBOHYD","site"); id2type.put("TOPO_DOM","interval"); id2type.put("REPEAT","interval"); id2type.put("REGION","interval"); id2type.put("NON_TER","site"); id2type.put("MOD_RES","site"); id2type.put("CONFLICT","site"); id2type.put("STRAND","interval"); id2type.put("HELIX","interval"); id2type.put("TURN","interval"); id2type.put("VAR_SEQ","site"); id2type.put("NON_CONS","site"); id2type.put("SITE","site"); id2type.put("VARIANT","site"); id2type.put("MUTAGEN","site"); id2type.put("UNSURE","site"); id2type.put("CA_BIND","interval"); id2type.put("CROSSLNK","2site"); id2type.put("TRANSIT","interval"); id2type.put("PEPTIDE","interval"); id2type.put("DNA_BIND","interval"); id2type.put("INTRAMEM","interval"); id2type.put("NON_STD","interval"); 
	}
	public String getType(String id){
		return(id2type.get(id));
	}
	public String getDesc(String id){
		return(id2desc.get(id));
	}
	public String getCol(String id){
		return(id2col.get(id));
	}
	public Boolean isClass(String testString){
		return id2desc.keySet().contains(testString);
	}
}


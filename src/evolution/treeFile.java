package evolution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class treeFile{
	private String content;
	private String html_tree;
	private String[] leafs;
	private int size;
	public treeFile(String pathname) {
		System.out.println("step0.1");
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		System.out.println("step0.2");
		
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathname));) {
			while((line =bufferedReader.readLine())!=null){
				stringBuilder.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("step0.3");
		content=stringBuilder.toString().trim();

		/*
		 * 
		 * INSERT DISRUPTREE
		 * 
		 */
		
		content=content.replaceAll(" ", "").replaceAll(";","");
		Pattern re_leaf=Pattern.compile("[(),]+(#[\\d+])*[(),]*`|[(),]*(#[\\d+])*[(),]+");
		Pattern re_clade=Pattern.compile("^(.*)(#\\d+)$");
		leafs=re_leaf.split(content);
		
		System.out.println(String.join("|", leafs));
		
		ArrayList<String> leaf_list=new ArrayList<String>();
		for (int i=0 ; i < leafs.length; i++){
			if (!leafs[i].equals("")){
				leaf_list.add(leafs[i]);
			}
		}
		leafs=leaf_list.toArray(new String[leaf_list.size()]);
				System.out.println(String.join("|", leafs));

		ArrayList<String> node_list=new ArrayList<String>();
		node_list.add(content);
		HashMap<String,String> clades = new HashMap<String,String>();
		HashMap<String,String[]> divisions = new HashMap<String,String[]>();
		

		while (node_list.size()!=0){
			String node = node_list.remove(0);
			divisions.put(node, this.parse_nw(node));
			System.out.println("###");
			System.out.println(node+"\n\n");
			System.out.println(String.join("\n", divisions.get(node)));
			System.out.println("###");
			for (int i = 0 ; i <2 ; i++){
				String child=divisions.get(node)[i];
				Matcher m = re_clade.matcher(child);
				if (m.matches()){
					child=m.group(1);
					divisions.get(node)[i]=child;
					clades.put(child, m.group(2));
				}
				if ( ! leaf_list.contains(child) ){
					node_list.add(child);
				}
			}
		}
		
		String[] Tree= new String[leaf_list.size()];
		String[] Chosen= new String[leaf_list.size()];
		HashMap<String,Integer> nodes_index=new HashMap<String,Integer>();
		HashMap<String,Boolean> isTerminal=new HashMap<String,Boolean>();

		for (int i =0; i<leaf_list.size();i++){
			Tree[i]="";
			Chosen[i]="-";
			nodes_index.put(leaf_list.get(i), i);
			isTerminal.put(leaf_list.get(i), true);
		}
		
		ArrayList<String> parentDone=new ArrayList<String>();
		int count=0;
		while(divisions.size()>parentDone.size()){
			count++;
			for (int idx =0; idx<leaf_list.size();idx++){
				String toWrite="";
				for(Entry<String, String[]> entry : divisions.entrySet()){
					String parent=entry.getKey();
					if ( ! parentDone.contains(parent)){
						String[] childs=entry.getValue();
						if (nodes_index.containsKey(childs[0]) & nodes_index.containsKey(childs[1])){
							int idx1=nodes_index.get(childs[0]);
							int idx2=nodes_index.get(childs[1]);
							if (idx1-idx2==-1){
								if (idx==idx1 & leaf_list.contains(childs[0])){
									toWrite=",";
									if (clades.containsKey(childs[0])){
										toWrite="<span style=\"color:red\"><b>"+toWrite+"</b></span>";
									}
									Chosen[idx]="_";
									break;
								}else if (idx==idx2){
									if (leaf_list.contains(childs[1])){
										if (isTerminal.get(childs[1])){
											toWrite="'";
										}else{
											toWrite="\\";
										}
										if (clades.containsKey(childs[1])){
											toWrite="<span style=\"color:red\"><b>"+toWrite+"</b></span>";
										}
										Chosen[idx]=".";
										isTerminal.put(childs[1],false);
									}else{
										toWrite="\\";
										if (clades.containsKey(childs[1])){
											toWrite="<span style=\"color:red\"><b>"+toWrite+"</b></span>";
										}
										Chosen[idx]=".";
									}
									parentDone.add(parent);
								}else if (idx == idx1+1 & ! leaf_list.contains(childs[0])){
									toWrite="/";
									Chosen[idx]="_";
								}
								if (!toWrite.equals("")){
									nodes_index.put(parent, nodes_index.get(childs[0]));
									break;
								}
							}else{
								if (idx==idx2){
									toWrite="\\";
									
									if (clades.containsKey(childs[1])){
										toWrite="<span style=\"color:red\"><b>"+toWrite+"</b></span>";
									}
									
									if (leaf_list.contains(childs[1])){
										if (isTerminal.get(childs[1])){
											toWrite="'";
											isTerminal.put(childs[1],false);
										}
									}
									
									Chosen[idx]=".";
									Chosen[idx-1]="_";
								}
								if (!toWrite.equals("")){
									nodes_index.put(childs[1], nodes_index.get(childs[1])-1);
									break;
								}
							}
						}
					}
				}
				if (!toWrite.equals("")){
					Tree[idx]=toWrite+Tree[idx];
				}else{
					Tree[idx]=Chosen[idx]+Tree[idx];
				}
			}
		}
		
		size=0;
		
		StringBuilder sb=new StringBuilder();
		sb.append("<html>");
		for (int idx =0; idx<leaf_list.size();idx++){
			Tree[idx]=Tree[idx].replaceAll("\\.", "<span style=\"color:white\">|</span>")+leaf_list.get(idx);
			if (Tree[idx].length()>size){
				size=count+leafs[idx].length();
			}
			sb.append(Tree[idx]+"<br>");
		}
		
		/*
		 * END DISRUPTREE
		 */
		sb.append("</html>");
		html_tree=sb.toString();
		System.out.println(html_tree);
	}

	private String[] parse_nw(String nw){
		Pattern re_clade=Pattern.compile("^(#\\d+)(.*)$");
		String[] toReturn=new String[2];
		if (nw.startsWith("(")){
			nw=nw.substring(1, nw.length()-1);
			int count=0;
			for (int i=0;i<nw.length();i++){
				String letter = nw.substring(i, i+1);
				if (letter.equals("(")){
					count++;
				}else if (letter.equals(")")){
					count--;
				}else if (letter.equals(",") & count==0){
					Matcher m = re_clade.matcher(nw.substring(i,nw.length()));
					toReturn=new String[2];
					int break_pos=i;
					if (m.matches()){
						break_pos+=Integer.parseInt(m.group(0));
					}
					toReturn[0]=nw.substring(0,break_pos);
					toReturn[1]=nw.substring(break_pos+1,nw.length());
				}
			}
		}
		return(toReturn);
	}
	
	public String getHTreeML() {
		return(html_tree);
	}
	
	public String[] getSpecies() {
		return(leafs);
	}

	public Double getSize() {
		return (double) (size);
	}
}

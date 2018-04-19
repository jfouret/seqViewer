package evolution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class treeFile{
	private String content;
	private String html_tree;
	private String[] speciesList;
	private Double size;
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
		System.out.println(content);
		ClassLoader classLoader = getClass().getClassLoader();
		PythonInterpreter interp = new PythonInterpreter();
		System.out.println("step0.4");
		interp.execfile(classLoader.getResource("ressources/disruptree.py").getFile());
		System.out.println("step0.5");
		interp.exec("res=disruptree('"+content+"')");
		System.out.println("step0.6");
		PyObject html_tree_python = interp.eval("res['html_tree']");
		html_tree=(String) html_tree_python.__str__().getString();
		PyObject leafs_python=interp.eval("res['leafs']");
		speciesList=((String) leafs_python.__str__().getString()).split(",");
		PyObject size_python=interp.eval("res['size']");
		size = Double.parseDouble(size_python.__str__().getString());
		interp.close();
	}

	public String getHTreeML() {
		return(html_tree);
	}
	
	public String[] getSpecies() {
		return(speciesList);
	}

	public Double getSize() {
		return(size);
	}
}

package topic;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeSet;

public class similarityMatrix {
	
	private HashMap<String, HashSet<String>>simMatrix;
	
	
	
	public  similarityMatrix(String filename) throws IOException 
	{
		
		simMatrix=load(filename);

	}
	
	
	public HashMap<String, HashSet<String>> load(String filename) throws IOException
	{
	
		simMatrix=new HashMap<String, HashSet<String>>();
		Scanner s = new Scanner(new File(filename));
		while (s.hasNextLine())
		{
			String line = s.nextLine().trim();
			String[] tokens = line.split(",");
			

           String w = tokens[0];
           String sw = tokens[1];

           if(!simMatrix.containsKey(w))
           {
               HashSet<String> dummy=new HashSet<String>();
               dummy.add(sw);
               simMatrix.put(w, dummy);

           }else
           {


        	   HashSet<String> dummy=simMatrix.get(w);
             dummy.add(sw);
             simMatrix.put(w, dummy);

           }
		
		}
	
		s.close();
		return simMatrix;
	}
	
	public HashSet<String> getSimWords(String word){
		
		HashSet<String> simWords=new HashSet<String>();
		
		if(simMatrix.containsKey(word))
			simWords=simMatrix.get(word);
		
		return simWords;
		
	}


	public TreeSet<String> getKeys() {
		 TreeSet<String> sortedKeys= new TreeSet<String>();
		 sortedKeys.addAll(simMatrix.keySet());
		return sortedKeys;
	}
	

}

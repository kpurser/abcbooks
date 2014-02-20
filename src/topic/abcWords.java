package topic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class abcWords {
	
	private Topic chosenTopic=null; 
	private similarityMatrix matrix;
	private HashMap<String, String> alphabeticalWords;
	private ArrayList<String> alphabet= new ArrayList<String>();
	private TreeSet<String> sortedPool= new TreeSet<String>();
	private PorterStemmer stem=new PorterStemmer();
	
	private static HashMap<String, String>likelyStem=new HashMap<String, String>();

	public abcWords(Topic[] topics, String inputTopic, String matrixFname) throws IOException {
		
		loadLikelyStem();
		 matrix= new similarityMatrix(matrixFname);
		Topic choseTopic = null;
		
		HashSet<String> givenTopics=new HashSet<String>();
		StringTokenizer tk= new StringTokenizer(inputTopic,"::");
		while(tk.hasMoreTokens())
			givenTopics.add(stem.stem(tk.nextToken()));
		
		System.out.println(givenTopics);
		double probabilityTopic=0;
		
		for (Topic topic: topics)
		{
			double tempprob=0;
			for(String gt:givenTopics){
								
			if(topic.hasWord(gt)){
				
				
				tempprob+=topic.getProb(gt);
						
			}
											
			}
			
			if(tempprob >probabilityTopic){
				probabilityTopic=tempprob; 
				choseTopic=topic;
			}
		}

		chosenTopic=choseTopic;
		System.out.println("Chosen Topic: "+ chosenTopic.name + " with probability: "+probabilityTopic);
		//System.out.println(this. matrix.getSimWords(inputTopic));
	}

	private void loadLikelyStem() throws FileNotFoundException {

		
		Scanner s = new Scanner(new File("mostLikelyStem.txt"));
		while (s.hasNextLine())
		{
			String line = s.nextLine().trim();
			String[] tokens = line.split("\t");
			

           String w = tokens[0];
           String sw = tokens[1];

           likelyStem.put(w, sw);
		
		}
	
		s.close();
	
	
	}

	public HashMap<String, String> get26Words() throws IOException {
		
		alphabeticalWords=new HashMap<String, String>();
		loadKeys();
				
		for(int i=0; i<chosenTopic.num_words(); i++){
			
			
		//	System.out.println(i + " "+chosenTopic.getWordPair(i).word);
			
			sortedPool.addAll(matrix.getSimWords(chosenTopic.getWordPair(i).word));
		}
	

			
		
		for(int l=0; l<alphabet.size(); l++){
			
			
			String selectedWord="";
			if(l+1>=alphabet.size())
				selectedWord= getWordforLetter(alphabet.get(l) ,"zz");
			else
				selectedWord=getWordforLetter(alphabet.get(l) ,alphabet.get(l+1));	
			
			if(likelyStem.containsKey(selectedWord))
			selectedWord=likelyStem.get(selectedWord);
			
			 alphabeticalWords.put(alphabet.get(l), selectedWord);
			
		}
		
		
		//write words to file
		
		FileWriter file = new FileWriter("26Words.txt");
		BufferedWriter bf = new BufferedWriter(file);
		
		Iterator<String>it=alphabet.iterator();
		while(it.hasNext()){
			String letter=it.next();
			//if(alphabeticalWords.containsKey(letter))
		bf.write(alphabeticalWords.get(letter)+"\n");
		}
		
		
		bf.close();
		//
		
		return alphabeticalWords;
	}

	private String getWordforLetter(String start, String end) {

		String word="";
		
		
		Set<String> possibilities = sortedPool.subSet(start, end);
		
				
		//System.out.println("for letter "+ start + " ");
		//System.out.println(possibilities);
		
	
		
		word=getWord(possibilities);
		
		//random word if nothing is found
		if(word.length()<1){
		
			Set<String> rpossibilities = matrix.getKeys().subSet(start, end);
			word=getWord(rpossibilities);
			
		}
			
		return word;
	}



	private String getWord(Set<String> possibilities) {
		if(!possibilities.isEmpty()){
		List<String> list = new ArrayList(possibilities);
	      Collections.shuffle(list);
		return list.get(0);
		}else{
			return "";
		}
	}

	private void loadKeys() {
		alphabet.add("a");
		alphabet.add("b");
		alphabet.add("c");
		alphabet.add("d");
		alphabet.add("e");
		alphabet.add("f");
		alphabet.add("g");
		alphabet.add("h");
		alphabet.add("i");
		alphabet.add("j");
		alphabet.add("k");
		alphabet.add("l");
		alphabet.add("m");
		alphabet.add("n");
		alphabet.add("o");
		alphabet.add("p");
		alphabet.add("q");
		alphabet.add("r");
		alphabet.add("s");
		alphabet.add("t");
		alphabet.add("u");
		alphabet.add("v");
		alphabet.add("w");
		alphabet.add("x");
		alphabet.add("y");
		alphabet.add("z");
		
				
	}
	
	
	
	

}

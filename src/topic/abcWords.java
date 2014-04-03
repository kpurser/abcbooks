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
	private TreeSet<String> dictionary= new TreeSet<String>();
	private static HashMap<String, String>likelyStem=new HashMap<String, String>();
	
	private static HashSet<String>possibleLocation=new HashSet<String>();
	
	private static Topic[] allTopics;

	public abcWords(Topic[] topics, String inputTopic, String matrixFname) throws IOException {
		
		loadLikelyStem();
		loadDictionary();
		loadPossibleLocation();
		
		allTopics=topics;
		 matrix= new similarityMatrix(matrixFname);
		Topic choseTopic = null;
		
	
		HashSet<String> givenTopics=new HashSet<String>();
		StringTokenizer tk= new StringTokenizer(inputTopic,"*");
		while(tk.hasMoreTokens())
			givenTopics.add(stem.stem(tk.nextToken()));
		
		System.out.println("Given Input "+givenTopics);
		double probabilityTopic=-1;
		
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

	private void loadPossibleLocation() throws FileNotFoundException {


		
		Scanner s = new Scanner(new File("data/vocab_lists/locationList.txt"));
		while (s.hasNextLine())
		{
			 possibleLocation.add(s.nextLine().trim());
		
		}
	
		s.close();
	
	
		
	}

	private void loadDictionary() throws FileNotFoundException {


		
		Scanner s = new Scanner(new File("data/vocab_lists/dictionaryVocab.txt"));
		while (s.hasNextLine())
		{
			
           dictionary.add(s.nextLine());
		
		}
	
		s.close();
	
	
	
		
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

	public HashSet<String> getLocation() throws IOException {
	
		HashSet<String>chosenLocation=new HashSet<String>();
		
		for(int i=0; i<chosenTopic.num_words(); i++){
			
			String selectedWord="";
			if(likelyStem.containsKey(chosenTopic.getWordPair(i).word))
				selectedWord=likelyStem.get(chosenTopic.getWordPair(i).word);
			else
				selectedWord=chosenTopic.getWordPair(i).word;
			
			if(possibleLocation.contains(selectedWord))
				chosenLocation.add(selectedWord);
			}
		
		
		//in case no location
		
		if(!(chosenLocation.size()>0)){
			List asList = new ArrayList(possibleLocation);
			Collections.shuffle(asList);
			chosenLocation.add(asList.get(0).toString());
			
		}
	//write words to file
		
		FileWriter file = new FileWriter("location.txt");
		BufferedWriter bf = new BufferedWriter(file);
		
		for(String cl:chosenLocation)
				bf.write(cl+"\n");
		
				
		
		bf.close();

		return chosenLocation;
	
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
			
			
			if(dictionary.contains(selectedWord))
			 alphabeticalWords.put(alphabet.get(l), selectedWord);
			else
			 checkAlternative(l, 0);	
		}
		
		
		//System.out.println("Checking correct topic");
		//checking correct topic
		Iterator<String>itc=alphabet.iterator();
		String extendedTopic=" ";
		while(itc.hasNext()){
			extendedTopic+=alphabeticalWords.get(itc.next()) + "\t";
			//System.out.println("et "+ extendedTopic);
			}
		extendedTopic=extendedTopic.trim();
		
		Boolean isOK=verifyChosenTopic(extendedTopic);
		int count=0;
		while(!isOK && count<3){
			count++;
			for(int l=0; l<alphabet.size(); l++){
				
				
				String selectedWord="";
				if(l+1>=alphabet.size())
					selectedWord= getWordforLetter(alphabet.get(l) ,"zz");
				else
					selectedWord=getWordforLetter(alphabet.get(l) ,alphabet.get(l+1));	
				
				if(likelyStem.containsKey(selectedWord))
				selectedWord=likelyStem.get(selectedWord);
				
				
				if(dictionary.contains(selectedWord))
				 alphabeticalWords.put(alphabet.get(l), selectedWord);
				else
				 checkAlternative(l, 0);	
			}
			
		
			Iterator<String>itc2=alphabet.iterator();
			String extendedTopic2=" ";
			while(itc2.hasNext()){
				extendedTopic+=alphabeticalWords.get(itc2.next()) + "\t";
				//System.out.println("et "+ extendedTopic);
				}
			extendedTopic2=extendedTopic.trim();
			
			 isOK=verifyChosenTopic(extendedTopic2);
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

	private Boolean verifyChosenTopic(String extendedTopic) {

			Topic choseTopic = null;
		
		HashSet<String> givenTopics=new HashSet<String>();
		StringTokenizer tk= new StringTokenizer(extendedTopic, "\t");
		while(tk.hasMoreTokens())
			givenTopics.add(stem.stem(tk.nextToken()));
		
		//System.out.println(givenTopics);
		double probabilityTopic=-1;
		
		for (Topic topic: allTopics)
		{
			//System.out.println(topic.name);
			double tempprob=0;
			for(String gt:givenTopics){
				//System.out.println(gt);
								
			if(topic.hasWord(gt)){
				
				
				tempprob+=topic.getProb(gt);
						
			}
											
			}
			
			if(tempprob >probabilityTopic){
				probabilityTopic=tempprob; 
				choseTopic=topic;
			}
		}

		
		
		if(chosenTopic.name.equals(choseTopic.name)){
			//System.out.println("Yay! correct topic");
			return true;
			}		else{
				//System.out.println("Opps! bad topic :(");
			return false;
			}
		//System.out.println("Original Topic: "+ chosenTopic.name + " Chose Topic: "+choseTopic.name+ "  with Probability  "+probabilityTopic);
		//System.out.println(this. matrix.getSimWords(inputTopic));
	
		
	}

	private void checkAlternative(int l, int soFar) {
		int seen=soFar;
			
		if(seen<3){	
		
			String selectedWord="";
			if(l+1>=alphabet.size())
				selectedWord= getWordforLetter(alphabet.get(l) ,"zz");
			else
				selectedWord=getWordforLetter(alphabet.get(l) ,alphabet.get(l+1));	
			
			if(likelyStem.containsKey(selectedWord))
			selectedWord=likelyStem.get(selectedWord);
			
			
			if(dictionary.contains(selectedWord))
			 alphabeticalWords.put(alphabet.get(l), selectedWord);
			else
			 checkAlternative(l, seen+1);	
		}else{
			if(l+1>=alphabet.size())
				alphabeticalWords.put(alphabet.get(l), getDictWordforLetter(alphabet.get(l) ,"zz"));
			else
				alphabeticalWords.put(alphabet.get(l), getDictWordforLetter(alphabet.get(l) ,alphabet.get(l+1)));
			
		}
		
	}

	private String getDictWordforLetter(String start, String end) {


		String word="";
		
		
		Set<String> possibilities=new HashSet<String>();
		
		
				
		//System.out.println("for letter "+ start + " ");
		//System.out.println(possibilities);
		
	
		
	if(start.equals("x")){
			
			//System.out.println("Look for X words");
			possibilities.addAll(dictionary.subSet("exa", "exu"));
			possibilities.addAll(dictionary.subSet(start, end));
			
		//	System.out.println(possibilities);
			
			}else{
				possibilities.addAll(dictionary.subSet(start, end));
			}
	
		
		word=getWord(possibilities);
		
		//random word if nothing is found
		if(word.length()<1){
		
			Set<String> rpossibilities = matrix.getKeys().subSet(start, end);
			word=getWord(rpossibilities);
			
		}
			
		return word;
	
	}

	private String getWordforLetter(String start, String end) {

		String word="";
		
		
		Set<String> possibilities=new HashSet<String>();
		
				
		if(start.equals("x")){
			
			//System.out.println("Look for X words");
			possibilities.addAll(sortedPool.subSet("exa", "exz"));
			possibilities.addAll(sortedPool.subSet(start, end));
			//System.out.println(possibilities);
			}else{
				possibilities.addAll(sortedPool.subSet(start, end));
			}
				
		//System.out.println("for letter "+ start + " ");
		
		
	
		
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

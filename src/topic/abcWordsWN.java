package topic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import rita.wordnet.RiWordnet;

public class abcWordsWN {
	
	private Topic chosenTopic=null; 
	private static RiWordnet wordnet;
	private similarityMatrix matrix;
	private HashMap<String, String> alphabeticalWords;
	private ArrayList<String> alphabet= new ArrayList<String>();
	private TreeSet<String> sortedPool= new TreeSet<String>();

	public abcWordsWN(Topic[] topics, String inputTopic, String matrixFname) throws IOException {
		
		Topic choseTopic = null;
		matrix= new similarityMatrix(matrixFname);
		wordnet = new RiWordnet();
		double probabilityTopic=0;
		
		for (Topic topic: topics)
		{
			
			if(topic.hasWord(inputTopic)){
				//System.out.println("==> in  "+topic.name+" "+topic.getProb(inputTopic) );
				
				if(topic.getProb(inputTopic) >probabilityTopic){
					probabilityTopic=topic.getProb(inputTopic); 
					choseTopic=topic;
				}
			}
		
		}
		
		

		chosenTopic=choseTopic;
		//System.out.println("Chosen Topic: "+ chosenTopic.name + " with probability: "+probabilityTopic);
		//System.out.println(this. matrix.getSimWords(inputTopic));
	}

	public HashMap<String, String> get26Words() throws IOException {
		
		alphabeticalWords=new HashMap<String, String>();
		loadKeys();
				
		for(int i=0; i<chosenTopic.num_words(); i++){
			
			
		//	System.out.println(i + " "+chosenTopic.getWordPair(i).word);
			String[] pos=wordnet.getPos(chosenTopic.getWordPair(i).word);
			
			for(int p=0;p<pos.length; p++){
				String[] synonyms = wordnet.getSynonyms(chosenTopic.getWordPair(i).word, pos[p]);
				//String[] synonyms = wordnet.getSimilar(chosenTopic.getWordPair(i).word, pos[p]);
				if (synonyms != null) {

					for (int j = 0; j < synonyms.length; j++) {

						sortedPool.add(synonyms[j]);

					}
				}
				
			}
			
	
		}
	
		
	
			
			
		//	System.out.println(i + " "+chosenTopic.getWordPair(i).word);
		//	String[] pos=wordnet.getPos("king");
			
//			for(int p=0;p<pos.length; p++){
//				String[] synonyms = wordnet.getAllAlsoSees(query, pos) .getAllDerivedTerms("horse", pos[p]);
//				//String[] synonyms = wordnet.getSimilar(chosenTopic.getWordPair(i).word, pos[p]);
//				if (synonyms != null) {
//
//					for (int j = 0; j < synonyms.length; j++) {
//
//						System.out.println("KING ==> "+synonyms[j]);
//
//					}
//				}
//				
//			}
//			
	
		

			
		
		for(int l=0; l<alphabet.size()-1; l++){
			
			
			
			
			alphabeticalWords.put(alphabet.get(l), getWordforLetter(alphabet.get(l) ,alphabet.get(l+1)));
			
		}
		
		
		//write words to file
		
		FileWriter file = new FileWriter("26WordsWN.txt");
		BufferedWriter bf = new BufferedWriter(file);
		
		Iterator<String>it=alphabet.iterator();
		while(it.hasNext()){
			String letter=it.next();
			if(alphabeticalWords.containsKey(letter))
		bf.write(alphabeticalWords.get(letter) +"\n");
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

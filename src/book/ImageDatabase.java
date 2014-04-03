
package book;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;


public class ImageDatabase {
	private final int NUM_IMAGES = 25000;
	private Map<String,Set<Integer>> tags;
	
	
	public ImageDatabase() throws FileNotFoundException{
		
		tags = new TreeMap<String, Set<Integer>>();
		File tagDir = new File("tags");
		
		
		Scanner reader;
		
		for(int i = 1; i < NUM_IMAGES;i++){
			File curTags = new File(tagDir.getAbsolutePath() +"\\tags"+ i +".txt");
			reader = new Scanner(new FileReader(curTags));
			while(reader.hasNext()){
				String nextTag = reader.next();
				if(!tags.containsKey(nextTag)){
					TreeSet<Integer> images = new TreeSet<Integer>();
					images.add(i);
					tags.put(nextTag, images);
				}
				else{
					Set<Integer> images = tags.get(nextTag);
					images.add(i);
					tags.put(nextTag, images);
				}
			}
			
		}
		
	}
	
	public void printTags(){
		for(String string: tags.keySet()){
			System.out.print(string);
		}
	}
	
	public void printDatabase(){
		for(Entry<String, Set<Integer>> entry : tags.entrySet()) {
			  String key = entry.getKey();
			  Set<Integer> value = entry.getValue();

			  System.out.print(key + ": ");
			  for(Integer i: value){

				  System.out.print(i + " ");
			  }

			  System.out.println();
			}
	}
	
	public void printDatabaseToFile(String fileName) throws IOException{
		
		File databaseFile = new File(fileName);
		BufferedWriter writer = new BufferedWriter(new FileWriter(databaseFile));
		for(Entry<String, Set<Integer>> entry : tags.entrySet()) {
			  String key = entry.getKey();
			  Set<Integer> value = entry.getValue();
			  writer.write(key + ": ");
			  System.out.print(key + ": ");
			  for(Integer i: value){
				  writer.write(i + " ");
				  System.out.print(i + " ");
			  }
			  writer.newLine();
			  System.out.println();
			}
		writer.close();
	}
	
	public Set<Integer> getImagePool(String tag){
		return tags.get(tag);
	}
	
	public double scoreImage(String theme, String word, String sentence, int imageNum){
		
		//populate the keywords
		Set<String> keywords = new TreeSet<String>();
		keywords.add(theme);
		keywords.add(word);
		while(sentence != ""){
			int wordLength = sentence.indexOf(" ");
			if(wordLength == -1 )
				break;
			String curWordInSentence = sentence.substring(0, wordLength);
			keywords.add(curWordInSentence); 
			sentence = sentence.substring(wordLength + 1);
		}
		
		// find all words that are both keywords and a tag on the image
		double tagsThatAreKeywords = 0;
		try {
			Set<String> imageTags = getTagsForImage(imageNum, "tags");
			
			for(String tag: imageTags){
				if(keywords.contains(tag)){
					tagsThatAreKeywords++;
				}
				if(tag.equals(theme) || tag.equals(word)){
					tagsThatAreKeywords++;
				}
				if(tag.equals(word)){
					tagsThatAreKeywords+=2;
				}
			}
			
			int totalTags = imageTags.size();
			int totalKeywords = keywords.size() + 3;
			return tagsThatAreKeywords/(totalKeywords + totalTags);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	private Set<String> getTagsForImage(int imageNum, String tagDirectory) throws FileNotFoundException{
		Set<String> tags = new TreeSet<String>();
		File curTags = new File(tagDirectory +"\\tags"+ imageNum +".txt");
		Scanner reader = new Scanner(new FileReader(curTags));
		while(reader.hasNext()){
			String nextTag = reader.next();
			tags.add(nextTag);
		}
		reader.close();
		return tags;
	}
	
	public ImageDatabase(String fileName) throws FileNotFoundException{
		tags = new TreeMap<String, Set<Integer>>();
		Scanner scanner = new Scanner(new FileReader(new File(fileName)));
		Scanner lineScanner;
		while(scanner.hasNext()){
			
			String line = scanner.nextLine();
			lineScanner = new Scanner(line);
			
			String tag = lineScanner.next();
			tag = tag.substring(0, tag.length() - 1);
			Set<Integer> imageNums = new TreeSet<Integer>();
			tags.put(tag, imageNums);
			
			while(lineScanner.hasNextInt()){
				imageNums.add(lineScanner.nextInt());
			}
		}
		scanner.close();
	}
	
	public static void main(String[]args) throws IOException{
		//ImageDatabase database = new ImageDatabase("images.txt");
		//database.printDatabase();
		ImageDatabase database = new ImageDatabase();
		database.printDatabaseToFile("images.txt");
	}
}

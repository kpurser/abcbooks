import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;


public class ABCBookCreator {

	public static void main(String[] args) throws IOException{
		if(args.length != 3){
			System.out.println("wordsfile sentencesfile booktitle");
		}
		
		ABCBookCreator creator = new ABCBookCreator(new File(args[0]), new File(args[1]));
		creator.generateBook(args[2]);
	}
	
	private String[] words;
	private String[] sentences;
	private final int NUM_LETTERS = 26;
	private final int NUM_SENTENCES = 13;
	
	public ABCBookCreator(File wordFile, File sentenceFile){
		words = new String[NUM_LETTERS];
		sentences = new String[NUM_SENTENCES];
		
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader(wordFile));
			for(int wordIndex = 0; wordIndex < NUM_LETTERS; wordIndex++){
				words[wordIndex] = reader.readLine();
			}
			reader.close();
			
			reader = new BufferedReader(new FileReader(sentenceFile));
			for(int sentenceIndex = 0; sentenceIndex < NUM_SENTENCES; sentenceIndex++){
				sentences[sentenceIndex] = reader.readLine();
			}
			reader.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void generateBook(String bookTitle) throws IOException{
		File book = new File(bookTitle + ".html");
		BufferedWriter bookWriter = new BufferedWriter(new FileWriter(book));
		
		bookWriter.append("<html>");
		bookWriter.append("<h1>");
		bookWriter.append(bookTitle);
		bookWriter.append("</h1>");
		
		for(char letter = 'A'; letter < 'Z'; letter += 2){
			
			String word1 = words[letter - 'A'];
			String query1 = word1.replaceAll(" ", "%20");
			String word2 = words[letter + 1 - 'A'];
			String query2 = word2.replaceAll(" ", "%20");
			bookWriter.append("<img src=\"" + getImageURL(query1 + "%20" + query2) + 
					"\" alt=\"" + word1 + " " + word2 + "\" height=\"200\">");
			
			String sentence1 = letter + " is for " + word1;
			bookWriter.append("<br>");
			bookWriter.append(sentence1);
			
			String sentence2 = (char)(letter + 1) + " is for " + word2;
			bookWriter.append("<br>");
			bookWriter.append(sentence2);
			
			String combinedSentence = sentences[(letter + 1 - 'A')/2];
			bookWriter.append("<br>");
			bookWriter.append(combinedSentence);
			bookWriter.append("<br>");
			bookWriter.append("<br>");
			bookWriter.append("<br>");
		}
		bookWriter.append("</html>");
		bookWriter.close();
	}
	
	public String getImageURL(String query){
		try {
			URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +
			        "v=1.0&q=" + query);
			URLConnection connection = url.openConnection();
			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}
			JSONObject json = new JSONObject(builder.toString());
			JSONArray results = json.getJSONObject("responseData").getJSONArray("results");
			if(results.length() != 0)
				return results.getJSONObject(0).getString("unescapedUrl");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
}

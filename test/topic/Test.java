
package topic;
import java.io.IOException;
import java.util.HashMap;

public class Test
{
	public static void main(String args[]) throws IOException
	{
		String filename = args[0];
		LDA_Parser parser = new LDA_Parser(filename);
		
		Topic[] topics = parser.parse();
		
		PorterStemmer stem= new PorterStemmer();
	
		for (Topic topic: topics)
		{
			
			System.out.println(topic);
		
		}
		
		String matrix=args[1];
		String inputTopic=args[2];
		String sinputTopic=stem.stem(inputTopic); // stem input topic to match words
		abcWords theWords=new abcWords(topics, sinputTopic, matrix);
		HashMap<String, String> chosenWords=theWords.get26Words();
		
		System.out.println("Selected 26 words ");
		for(String w:chosenWords.keySet())
			System.out.println(w + " " + chosenWords.get(w));


		abcWordsWN theWordsWN=new abcWordsWN(topics, sinputTopic, matrix);
		HashMap<String, String> chosenWordsWN=theWordsWN.get26Words();
		
		System.out.println("Selected 26 words ");
		for(String w:chosenWordsWN.keySet())
			System.out.println(w + " " + chosenWordsWN.get(w));
		

		
		
	}
}


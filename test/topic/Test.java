
package topic;
import java.io.IOException;

public class Test
{
	public static void main(String args[]) throws IOException
	{
		System.out.println(args);
		String filename = args[0];
		LDA_Parser parser = new LDA_Parser(filename);
		Topic[] topics = parser.parse();
		for (Topic topic: topics)
		{
			System.out.println(topic);
		}
	}
}


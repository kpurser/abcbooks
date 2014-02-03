
package topic;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class LDA_Parser
{
	private String filename;

	public LDA_Parser(String filename)
	{
		this.filename = filename;
	}

	public Topic[] parse() throws IOException
	{
		int topic_idx = 0;
		Topic[] topics = null;
		Topic curTopic = null;
		Scanner s = new Scanner(new File(this.filename));
		while (s.hasNextLine())
		{
			String line = s.nextLine().trim();
			if (line.startsWith("TOTAL TOPICS COUNT: ")) {
				int topic_count = Integer.parseInt(line.split("\\s+")[3]);
				topics = new Topic[topic_count];
				continue;
			}

			if (line.startsWith("#")) {
				if (curTopic != null)
				{
					topics[topic_idx] = curTopic;
					topic_idx++;
				}
				String[] tokens = line.split("\\s+");
				curTopic = new Topic(Integer.parseInt(tokens[1]),
											Double.parseDouble(tokens[3]),
											tokens[4]);
				continue;
			}

			if (line.startsWith(".")) {
				String[] tokens = line.split("\\s+");
				for (int k = 0; k < 8; k+=2)
				{
					WordPair wp = new WordPair(tokens[k+1],
														Double.parseDouble(tokens[k]));
					curTopic.addWordPair(wp);
				}
			}
		}
		if (curTopic != null)
			topics[topic_idx] = curTopic;
		return topics;
	}
}


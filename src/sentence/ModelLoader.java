
package sentence;

import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class ModelLoader
{
	private File modelDir;
	private Map<String, Map<String, Categorical<String>>> cache;

	public ModelLoader(String indir)
	{
		modelDir = new File(indir);
		if (!modelDir.exists())
			throw new IllegalArgumentException("Model Directory does not exist");
		cache = new HashMap<String, Map<String, Categorical<String>>>();
	}

	public Categorical<String> get(String rel, String word)
	{
		// check the cache for the distribution
		Map<String, Categorical<String>> word_dist = cache.get(rel);
		if (word_dist == null)
		{
			word_dist = new HashMap<String, Categorical<String>>();
			cache.put(rel, word_dist);
		}
		else
		{
			Categorical<String> dist = word_dist.get(word);
			if (dist != null)
				return dist;
		}

		File relDir = new File(modelDir, rel);
		if (!relDir.isDirectory())
			throw new IllegalArgumentException("Relation " + rel + " dir does not exist");
		File wordFile = new File(relDir, word + ".txt");
		if (!wordFile.isFile()) {
			relDir = new File(modelDir, reverse_rel(rel));
			wordFile = new File(relDir, "_all.txt");
			if (!wordFile.isFile())
				throw new IllegalArgumentException(rel + " " + word + " could not be loaded, nor _all.txt");
		}
	
		Categorical<String> dist = read(wordFile);
		word_dist.put(word, dist);
		return dist;
	}

	private static String reverse_rel(String rel)
	{
		if (rel.endsWith("_r"))
			return rel.substring(0, rel.length() - 2);
		else
			return rel + "_r";
	}

	public Categorical<String> read(File f)
	{
		try
		{
			Scanner s = new Scanner(f);
			Map<String, Integer> map = new HashMap<String, Integer>();
			while (s.hasNextLine()) {
				String[] toks = s.nextLine().split("\\s+");
				map.put(toks[0], Integer.parseInt(toks[1]));
			}
			return new Categorical<String>(map);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}



}


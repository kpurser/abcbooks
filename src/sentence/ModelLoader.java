
package sentence;

import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class ModelLoader
{
	private File modelDir;

	public ModelLoader(String indir)
	{
		modelDir = new File(indir);
		if (!modelDir.exists())
			throw new IllegalArgumentException("Model Directory does not exist");
	}

	public Categorical<String> get(String rel, String word)
	{
		File relDir = new File(modelDir, rel);
		if (!relDir.isDirectory())
			throw new IllegalArgumentException("Relation " + rel + " dir does not exist");
		File wordFile = new File(relDir, word + ".txt");
		if (!wordFile.isFile()) {
			wordFile = new File(relDir, "_all.txt");
			if (!wordFile.isFile())
				throw new IllegalArgumentException(rel + " " + word + " could not be loaded, nor _all.txt");
		}
	
		return read(wordFile);
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


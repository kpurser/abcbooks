
package sentence;

import simplenlg.lexicon.*;
import simplenlg.framework.*;
import rita.wordnet.RiWordnet;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class ModelGen
{
	private static String[] stopwords = {
		"i", "he", "she", "we", "it", "him", "her", "us", "you",
		"your", "his", "hers", "yours", "himself", "herself", "yourself",
		"say", "sex", "bloody", "who"};
	private int freq_thresh;
	private boolean reverse;
	private String indir;
	private String model_dir;
	private String rel;
	private File rel_dir;
	private Map<String, Integer> relation;
	private Map<String, Integer> word_counts;
	private Set<String> vocab;
	private Set<String> names;
	private RiWordnet net;
	private String pos1;
	private String pos2;
	private Lexicon lexicon;
	
	public ModelGen(String indir, String model_dir, String vocab_path, String name_path)
	{
		lexicon = Lexicon.getDefaultLexicon();
		net = new RiWordnet();
		this.indir = indir;
		this.model_dir = model_dir;
		loadVocab(vocab_path);
		loadNames(name_path);
		removeStopWords();
		System.out.println(this.vocab);
	}

	private void removeStopWords()
	{
		for (String w: stopwords)
			vocab.remove(w);
	}

	private void loadNames(String path)
	{
		names = new HashSet<String>();
		try
		{
			Scanner s = new Scanner(new File(path));
			while(s.hasNext())
			{
				String name = s.next().trim().toLowerCase();
				names.add(name);
				vocab.remove(name);
			}
		}
		catch(IOException e)
		{
			System.out.println("Could not read vocab file");
		}
		vocab.add("_person");
	}

	private void loadVocab(String path)
	{
		vocab = new HashSet<String>();
		try
		{
			Scanner s = new Scanner(new File(path));
			while(s.hasNext())
			{
				vocab.add(s.next().trim().toLowerCase());
			}
		}
		catch(IOException e)
		{
			System.out.println("Could not read vocab file");
		}
	}

	public void go(String relationship, String pos1, String pos2, int freq_thresh, boolean reverse)
	{
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.freq_thresh = freq_thresh;
		this.reverse = reverse;
		this.rel = relationship;
		populateRelation();
		writeRelation();
	}

	private void pruneRelationship()
	{
		for (String word_pair: relation.keySet())
		{
			if (relation.get(word_pair) == 1)
			{
				relation.remove(word_pair);
			}
		}
	}

	private void writeWord(String w)
	{
		try
		{
			File outfile = new File(rel_dir, w + ".txt");
			BufferedWriter out = new BufferedWriter(new FileWriter(outfile));
			String[] words;

			for (String word_pair: relation.keySet())
			{
				words = word_pair.split(" ");
				if (w.equals(words[0]) &&
					((this.pos2.equals("*") && !words[1].equals("_person")) ||
						(words[1].equals("_person") && this.pos2.equals("n")) ||
						this.pos2.equals(net.getBestPos(words[1]))) &&
					relation.get(word_pair) > 1)
				{
					out.write(words[1] + " " + relation.get(word_pair) + "\n");
				}
			}

			out.flush();
			out.close();
			if (outfile.length() == 0)
				outfile.delete();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	private void writeCounts()
	{
		try
		{
			File outfile = new File(rel_dir, "_all.txt");
			FileWriter out = new FileWriter(outfile);

			for (String word: word_counts.keySet())
			{
				if (word_counts.get(word) > freq_thresh &&
					( (this.pos1.equals("*") && !word.equals("_person")) ||
						(word.equals("_person") && this.pos1.equals("n")) ||
						this.pos1.equals(net.getBestPos(word))))
				{
					out.write(word + " " + word_counts.get(word) + "\n");
				}
			}

			out.flush();
			out.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

	}

	private void writeRelation()
	{
		// create dir or empty it as need be
		String dir_name = this.rel;
		if (this.reverse)
			dir_name += "_r";
		this.rel_dir = new File(new File(model_dir), dir_name);
		rel_dir.mkdir();
		for (File c : rel_dir.listFiles())
		{
			c.delete();
		}

		int count = 0;
		for (String w: word_counts.keySet())
		{
			if (word_counts.get(w) > freq_thresh &&
				( (this.pos1.equals("*")  && !w.equals("_person")) ||
					(w.equals("_person") && this.pos1.equals("n")) ||
					this.pos1.equals(net.getBestPos(w))))
			{
				count++;
				writeWord(w);
				if (count % 10 == 0)
					System.out.println("Written " + count + " Files");
			}
		}

		writeCounts();
	}

	private LexicalCategory translate(String s)
	{
		if (s.equals("n"))
			return LexicalCategory.NOUN;
		if (s.equals("v"))
			return LexicalCategory.VERB;
		if (s.equals("a"))
			return LexicalCategory.ADJECTIVE;
		return LexicalCategory.ANY;
	}

	private void processLine(String line)
	{
		//System.out.println(line);
		try
		{
			String w1, w2;
			int idx1, idx2, idx3, idx4;
			idx1 = line.indexOf('(') + 1;
			idx2 = line.indexOf('-');
			idx3 = line.indexOf(' ') + 1;
			idx4 = line.indexOf('-', idx2 + 1);
			w1 = line.substring(idx1, idx2);
			w2 = line.substring(idx3, idx4);

			if (reverse)
			{
				String swap = w1;
				w1 = w2;
				w2 = swap;
			}

			if (lexicon.hasWordFromVariant(w1, translate(this.pos1)))
				w1 = lexicon.getWordFromVariant(w1, translate(this.pos1)).getBaseForm();

			if (lexicon.hasWordFromVariant(w2, translate(this.pos2)))
				w2 = lexicon.getWordFromVariant(w2, translate(this.pos2)).getBaseForm();

			if (names.contains(w1))
				w1 = "_person";
			if (names.contains(w2))
				w2 = "_person";

			if (!(isGood(w1) && isGood(w2)))
				return;

			inc(word_counts, w1, 1);
			inc(relation, w1 + " " + w2, 1);
		}
		catch (RuntimeException e)
		{
			//e.printStackTrace();
			//System.out.println(line);
		}
	}

	private void processFile(File input) throws IOException
	{
		String line;
		Scanner s = new Scanner(input);
		while (s.hasNextLine())
		{
			line = s.nextLine();
			if (line.startsWith(this.rel + "("))
				processLine(line.toLowerCase());
		}
	}

	private void processFileObj(File f)
	{
		if (f.isDirectory())
		{
			File[] inputs = f.listFiles();

			for (int k = 0; k < inputs.length; k++)
			{
				processFileObj(inputs[k]);
			}
		}
		else if (f.isFile())
		{
			try
			{
				processFile(f);
				numProcessed++;
				if (numProcessed % 10 == 0)
					System.out.println(numProcessed + " files completed");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private int numProcessed;
	private void populateRelation()
	{
		numProcessed = 0;
		relation = new HashMap<String, Integer>();
		word_counts = new HashMap<String, Integer>();
		processFileObj(new File(this.indir));
		/*
		try
		{
			File folder = new File(this.indir);
			File[] inputs = folder.listFiles();

			for (int k = 0; k < inputs.length; k++)
			{
				processFile(inputs[k]);
				if (k % 10 == 0)
					System.out.println(k + " files completed");
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		*/
	}

	private void inc(Map<String, Integer> map, String key, int amount)
	{
		if (map.containsKey(key))
			map.put(key, map.get(key) + amount);
		else
			map.put(key, amount);
	}

	private boolean isGood(String in)
	{
		return vocab.contains(in);
	}

}


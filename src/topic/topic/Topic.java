
package topic;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class Topic
{
	private List<WordPair> word_pairs;
	private Map<String, Double> word_map;
	public final int id;
	public final double size;
	public final String name;

	public Topic(int id, double size, String name)
	{
		this.id = id;
		this.size = size;
		this.name = name;
		word_pairs = new ArrayList<WordPair>();
		word_map = new HashMap<String, Double>();
	}

	public void addWordPair(WordPair w)
	{
		int pos = findPos(w);
		word_pairs.add(pos, w);
		if (word_map.containsKey(w.word))
			throw new RuntimeException("Topic already has a key");
		word_map.put(w.word, w.prob);
	}

	public WordPair getWordPair(int idx)
	{
		return word_pairs.get(idx);
	}

	public int num_words()
	{
		return word_pairs.size();
	}

	public double getProb(String word)
	{
		if (!word_map.containsKey(word))
			return 0.0;
		return word_map.get(word);
	}
	
	public boolean hasWord(String word)
	{
		return word_map.containsKey(word);
	}

	private double epsilon = 1e-8;
	public double KLDivergence(Topic other)
	{
		return this.KLDivergence(other, epsilon);
	}

	// smooths p(w|t) by approximating plus eps smoothing
	// p(w|t) is *not* renormalized after plus eps
	public double KLDivergence(Topic other, double eps)
	{
		double divergence, prob1, prob2;
		divergence = prob1 = prob2 = 0.0;
		Set<String> support = new HashSet<String>();
		support.addAll(this.word_map.keySet());
		support.addAll(other.word_map.keySet());
		for (String word: support)
		{
			prob1 = this.getProb(word) + eps;
			prob2 = other.getProb(word) + eps;
			divergence += Math.log(prob1 / prob2) * prob1;
		}

		return divergence;
	}

	private int findPos(WordPair w)
	{
		int pos = 0;
		while(pos < word_pairs.size() && w.compareTo(word_pairs.get(pos)) < 0)
		{
			pos++;
		}
		return pos;
	}

	private static int num_per_line = 4;
	public String toString()
	{
		String s = "";
		s += "Topic " + this.name + " (" + this.id + ") size: " + this.size + "\n";
		for (int k = 0; k < this.num_words(); k += num_per_line)
		{
			for (int j = 0; j < num_per_line; j++)
			{
				if ( (k + j) >= this.num_words())
					break;
				WordPair w = word_pairs.get(k + j);
				s += w.toString() + "\t\t";
			}
			s += "\n";
		}
		return s;
	}
}


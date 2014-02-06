
package topic;

public class WordPair implements Comparable<WordPair>
{
	public final String word;
	public final double prob;

	public WordPair(String w, double p)
	{
		this.word = w;
		this.prob = p;
	}

	public int hashCode()
	{
		Double tmp = new Double(prob);
		return word.hashCode() + 37 * tmp.hashCode();
	}

	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof WordPair))
			return false;

		WordPair other = (WordPair) obj;
		return this.prob == other.prob && this.word.equals(other.word);
	}

	public int compareTo(WordPair other)
	{
		double diff = this.prob - other.prob;
		if (diff > 0)
			return 1;
		else if (diff < 0)
			return -1;
		else
			return this.word.compareTo(other.word);
	}

	public String toString()
	{
		return this.prob + " " + this.word;
	}
}



package sentence;

import java.util.*;
import java.io.IOException;
import java.io.File;

public class ProDict
{
	private Set<String> vocab;
	private Map<String, ProDictEntry> dict;
	
	public ProDict(String vocab_path, String dict_path)
	{
		this.loadVocab(vocab_path);
		this.loadDict(dict_path);
	}

	private void loadDict(String path)
	{
		dict = new HashMap<String, ProDictEntry>();
		try
		{
			Scanner s = new Scanner(new File(path));
			while(s.hasNextLine())
			{
				String[] tokens = s.nextLine().split("\\s+");
				if (tokens.length < 1)
					continue;
				String word = tokens[0].toLowerCase();
				List<String> phones = new ArrayList<String>();
				for (int k = 1; k < tokens.length; k++)
					phones.add(tokens[k]);
				dict.put(word, new ProDictEntry(word, phones));
			}
		}
		catch(IOException e)
		{
			System.out.println("Could not read dict file");
		}
		//System.out.println(dict);
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

	public double rhymeScore(String w1, String w2)
	{
		if (w1.equals(w2))
			return 0.1;
		ProDictEntry one = this.dict.get(w1);
		ProDictEntry two = this.dict.get(w2);
		if (one == null || two == null)
			return 0.0;
		else
			return one.rhymeScore(two);
	}

	// slow for now
	public List<String> rhymingWords(String word)
	{
		List<String> words = new ArrayList<String>();
		for (String w : vocab)
			if (this.rhymeScore(word, w) > 0.7)
				words.add(w);
		return words;
	}

	public int numSyllables(String sentence)
	{
		int count = 0;
		for (String word: sentence.split("\\s+"))
		{
			word = removePunc(word).toLowerCase();
			ProDictEntry entry = this.dict.get(word);
			if (entry == null)
			{
				System.out.println(word + " does not have a prouniation entry");
				count++;  // assume one syllable
			}
			else
			{
				count += entry.numSyllables();
			}
		}
		return count;
	}

	private String removePunc(String w)
	{
		while (!Character.isLetter(w.charAt(w.length()-1)))
			w = w.substring(0, w.length()-1);
		return w;
	}

	// 1 for each repeated beginning consonant, +1 extra for consecutive words
	public int consonance(String sentence)
	{
		int count = 0;
		String prev_cons = null;
		Set<String> seen_cons = new HashSet<String>();
		for (String word: sentence.split("\\s+"))
		{
			word = removePunc(word).toLowerCase();
			ProDictEntry entry = this.dict.get(word);
			if (entry == null)
				continue;
			String cons = entry.startPhone(); 
			if (this.dict.get(word).vowels.contains(cons))
				continue;
			if (cons.equals(prev_cons))
				count++;
			prev_cons = cons;
			if (seen_cons.contains(cons))
				count++;
			seen_cons.add(cons);
		}

		return count;
	}

	public int assonance(String sentence)
	{
		int count = 0;
		List<String> prev_vowels = new ArrayList<String>();
		Set<String> seen_vowels = new HashSet<String>();
		for (String word: sentence.split("\\s+"))
		{
			word = removePunc(word).toLowerCase();
			ProDictEntry entry = this.dict.get(word);
			if (entry == null)
				continue;
			List<String> vowels = entry.vowels;
			for (String vowel: vowels)
			{
				if (prev_vowels.contains(vowel))
					count++;
				if (seen_vowels.contains(vowel))
					count++;
			}
			prev_vowels = vowels;
			seen_vowels.addAll(vowels);
		}

		return count;
	}
}

class ProDictEntry
{
	String word;
	List<String> phones;  // raw data
	Set<String> rhyme_part;  // for rhyming
	List<String> vowels;  // for assonance

	ProDictEntry(String word, List<String> phones)
	{
		this.word = word;
		this.phones = phones;

		this.vowels = new ArrayList<String>();
		for (String phone: this.phones)
			if (phone.matches("[A-Z]{1,}\\d"))
				this.vowels.add(removeNumber(phone));
			
		this.rhyme_part = new HashSet<String>();
		boolean found_stress = false;
		for (int k = 0; k < this.phones.size(); k++)
		{
			String phone = this.phones.get(k);
			if (phone.matches("[A-Z]{1,}1")) // primary stress
				found_stress = true;
			if (found_stress)
				this.rhyme_part.add(removeNumber(phone));
		}
	}

	static String removeNumber(String phone)
	{
		if (phone.matches("[A-Z]{1,}\\d"))
			return phone.substring(0, phone.length() - 1);
		return phone;
	}

	// for consonance
	String startPhone()
	{
		return this.phones.get(0);
	}
	
	// for syllable counting
	int numSyllables()
	{
		return this.vowels.size();
	}

	// returns 1.0 for perfect rhyme, 0 for no rhyme and a score in between for slant rhyme
	double rhymeScore(ProDictEntry other)
	{
		Set<String> intersection = new HashSet<String>();
		Set<String> union = new HashSet<String>();
		intersection.addAll(this.rhyme_part);
		intersection.retainAll(other.rhyme_part);
		int num = intersection.size();
		if (num == 0)
			return 0.0;
		union.addAll(this.rhyme_part);
		union.addAll(other.rhyme_part);
		return num / (double) union.size();
	}
}


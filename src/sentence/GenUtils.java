
package sentence;

import java.util.List;
import java.util.ArrayList;

class GenUtils
{

	static String chooseWordHelper(SentenceContext context, List<String> list_words,
														Categorical<String> dist)
	{
		String word = null;
		for (String w: list_words)
		{
			if (dist.elements().contains(w) && context.useListWord())
			{
				word = w;
				context.markUsed(word);
				break;
			}
		}
		if (word == null)
			word = dist.draw();
		//System.out.println("Choosing word: " + word);
		return word;
	}

	static String chooseWord(SentenceContext context, String pos, List<String> rels, List<String> words)
	{
		assert !rels.isEmpty();
		assert rels.size() == words.size();
		Categorical<String> dist = null;
		List<String> list_words = context.getWords(pos);
		List<Categorical<String>> dists = new ArrayList<Categorical<String>>();
		for (int k = 0; k < rels.size(); k++)
		{
			String rel = rels.get(k);
			String word = words.get(k);
			if (word == null)
				dists.add(context.getModelLoader().get(reverse_rel(rel), "_all"));
			else
				dists.add(context.getModelLoader().get(rel, word));
		}
		dist = dists.get(0);
		for (int k = 1; k < dists.size(); k++)
		{
			Categorical<String> result = new Categorical<String>(dist, dists.get(k));
			if (!result.elements().isEmpty())
				dist = result;
		}
		return chooseWordHelper(context, list_words, dist);
	}

	static String chooseNounAdj(SentenceContext context, String noun)
	{
		List<String> rels = new ArrayList<String>();
		List<String> words = new ArrayList<String>();
		rels.add("amod");
		words.add(noun);
		return chooseWord(context, "n", rels, words);
	}

	static String chooseVerbAdv(SentenceContext context, String verb)
	{
		List<String> rels = new ArrayList<String>();
		List<String> words = new ArrayList<String>();
		rels.add("advmod");
		words.add(verb);
		return chooseWord(context, "v", rels, words);

	}

	static String chooseNounDet(SentenceContext context, String noun)
	{
		List<String> rels = new ArrayList<String>();
		List<String> words = new ArrayList<String>();
		rels.add("det");
		words.add(noun);
		return chooseWord(context, "none", rels, words);
	}

	private static String reverse_rel(String rel)
	{
		if (rel.endsWith("_r"))
			return rel.substring(0, rel.length() - 2);
		else
			return rel + "_r";
	}

}


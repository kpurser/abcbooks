
package sentence;

import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import rita.wordnet.RiWordnet;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class SentenceGen
{
	private static double prob_adj = 0.5;
	private static double prob_iobj = 0.5;
	private Lexicon lexicon;
	private NLGFactory nlgFactory;
	private Realiser realiser;
	private RiWordnet net;
	private Random r;
	private boolean has_iobj;
	private NPPhraseSpec subject, dobj, iobj;
	private VPPhraseSpec verb;
	private ModelLoader loader;
	private String model_dir;
	private ProDict prodict;
	private SentenceContext context;
	private static final int NUM_ATTEMPTS = 100;

	public SentenceGen(String modelDir, ProDict prodict)
	{
		lexicon = Lexicon.getDefaultLexicon();
		nlgFactory = new NLGFactory(lexicon);
		realiser = new Realiser(lexicon);
		net = new RiWordnet();
		loader = new ModelLoader(modelDir);
		this.model_dir = modelDir;
		this.prodict = prodict;
		String[] names = {"George", "Mary", "Bob"};
		List<String> locations = getLocations();
		this.context = new SentenceContext(this.model_dir, new ArrayList<String>(), names, locations);
		this.r = new Random();
	}

	public List<String> getLocations()
	{
		List<String> locs = new ArrayList<String>();
		try
		{
			Scanner s = new Scanner(new File("locationWN.txt"));
			while(s.hasNextLine())
			{
				locs.add(s.nextLine().trim());
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return locs;
	}

	private String capitalize(String word)
	{
		return Character.toUpperCase(word.charAt(0)) + word.substring(1);
	}

	public String genTitle(String topic_word)
	{
		this.context.words.clear();
		String noun = "book";
		String det = null;
		String adj1 = null;
		if (context.hasDeterminer())
			det = GenUtils.chooseNounDet(this.context, noun);
		if (context.hasAdj())
			adj1 = GenUtils.chooseNounAdj(this.context, noun);

		String adj2 = GenUtils.chooseNounAdj(this.context, topic_word);

		String title = "";
		if (det != null)
			title = capitalize(det) + " ";
		if (adj1 != null)
			title += capitalize(adj1) + " ";
		title += capitalize(noun) + " of " + capitalize(adj2) + " " + capitalize(topic_word);
		return title;
	}

	public String genSentence(String word)
	{
		List<String> rhymes = this.find_rhymes(word);
		System.out.println(rhymes);

		String best = null;
		double best_score = -1.0;
		for (int k = 0; k < NUM_ATTEMPTS; k++)
		{
			String w2 = null;
			if (rhymes.size() != 0)
				w2 = rhymes.get(r.nextInt(rhymes.size()));
			String sentence = this.gen_sentence_helper(word, w2);
			double score = this.score_sentence(sentence, word);
			System.out.println(score + ":\t" + sentence);
			if (score > best_score)
			{
				best = sentence;
				best_score = score;
			}
		}
		return best;
	}

	private List<String> find_rhymes(String word)
	{
		return this.prodict.rhymingWords(word);
	}

	private String gen_sentence_helper(String word1, String word2)
	{
		this.context.words.clear();
		this.context.words.add(word1);
		if (word2 != null)
			this.context.words.add(word2);
		NLGElement phrase = SimpleSentence.generate(context);
		//System.out.println(phrase.printTree(" "));
		return realiser.realiseSentence(phrase);
	}

	// secret sauce right here
	private double score_sentence(String sentence, String w1)
	{
		String[] toks = sentence.split("\\s+");
		String last_word = toks[toks.length - 1];
		last_word = last_word.substring(0, last_word.length() - 1); // strip period
		int target_syb = 8;//this.prodict.numSyllables(w1 + " " + w2) + (2 * 3);  // A is for ____.  B is for ____
		int num_syb = this.prodict.numSyllables(sentence);
		double syb_mult = 0.0;
		if (num_syb >= target_syb)
		{
			if (num_syb - target_syb <= 2)
				syb_mult = 1.0;
			else
				syb_mult = 2.0 /  (num_syb - target_syb);
		} else 
		{
			if (target_syb - num_syb >= 2)
				syb_mult = 1.0;
			else
				syb_mult = 2.0 / (target_syb - num_syb);
		}
		//double rhyme_score = Math.max(this.prodict.rhymeScore(w1, last_word), this.prodict.rhymeScore(w2, last_word));
		double rhyme_score = this.prodict.rhymeScore(w1, last_word);
		int assonance = this.prodict.assonance(sentence);
		int consonance = this.prodict.consonance(sentence);
		double num_words = (double) toks.length;
		double word_mult = 0.2;
		for (String tok: toks)
			if (tok.equals(w1))
				word_mult = 1.0;
		return word_mult * (rhyme_score + 0.2) * (assonance / num_words + 0.2) * (consonance / num_words + 0.2) * syb_mult;
	}
}


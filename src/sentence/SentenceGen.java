
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

public class SentenceGen
{
	private static double prob_adj = 0.5;
	private static double prob_iobj = 0.5;
	private Lexicon lexicon;
	private NLGFactory nlgFactory;
	private Realiser realiser;
	private RiWordnet net;
	private Random r = new Random();
	private boolean has_iobj;
	private NPPhraseSpec subject, dobj, iobj;
	private VPPhraseSpec verb;
	private ModelLoader loader;
	private String model_dir;

	public SentenceGen(String modelDir)
	{
		lexicon = Lexicon.getDefaultLexicon();
		nlgFactory = new NLGFactory(lexicon);
		realiser = new Realiser(lexicon);
		net = new RiWordnet();
		loader = new ModelLoader(modelDir);
		this.model_dir = modelDir;
	}

	public String genSentence(String word1, String word2)
	{
		List<String> words = new ArrayList<String>();
		words.add(word1);
		words.add(word2);
		SentenceContext context = new SentenceContext(this.model_dir, words);
		NLGElement phrase = SimpleSentence.generate(context);
		//System.out.println(phrase.printTree(" "));
		return realiser.realiseSentence(phrase);
	}
}


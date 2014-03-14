
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

public class SentenceContext
{
	private Random r;
	private List<String> words;
	private Lexicon lexicon;
	private NLGFactory nlgFactory;
	private Realiser realiser;
	private RiWordnet net;
	private ModelLoader loader;

	public SentenceContext(String modelDir, List<String> words)
	{
		this.words = words;
		this.r = new Random();
		lexicon = Lexicon.getDefaultLexicon();
		nlgFactory = new NLGFactory(lexicon);
		realiser = new Realiser(lexicon);
		net = new RiWordnet();
		loader = new ModelLoader(modelDir);
	}

	public ModelLoader getModelLoader()
	{
		return loader;
	}

	public Lexicon getLexicon()
	{
		return lexicon;
	}
	
	public NLGFactory getNLGFactory()
	{
		return nlgFactory;
	}

	public Realiser getRealiser()
	{
		return realiser;
	}

	public RiWordnet getWordNet()
	{
		return net;
	}

	public boolean hasAdj()
	{
		return r.nextBoolean();
	}

	public boolean hasAdv()
	{
		return r.nextBoolean();
	}

	public boolean hasDeterminer()
	{
		return r.nextDouble() < 0.8;
	}

	public boolean useListWord()
	{
		return r.nextDouble() < 0.8;
	}

	public boolean hasDobj()
	{
		return r.nextBoolean();
	}

	public boolean hasPPhrase()
	{
		return false;
		//return r.nextBoolean();
	}

	public boolean isNoun(String w)
	{
		return net.getBestPos(w).equals("n");
	}

	public boolean isVerb(String w)
	{
		return net.getBestPos(w).equals("v");
	}

	public boolean isAdj(String w)
	{
		return net.getBestPos(w).equals("a");
	}

	public void markUsed(String w)
	{
		this.words.remove(w);
	}

	public List<String> getNouns()
	{
		List<String> nouns = new ArrayList<String>();
		for (String w: this.words)
			if (isNoun(w))
				nouns.add(w);
		return nouns;
	}

	public List<String> getVerbs()
	{
		List<String> verbs = new ArrayList<String>();
		for (String w: this.words)
			if (isVerb(w))
				verbs.add(w);
		return verbs;
	}

	public List<String> getAdjs()
	{
		List<String> adjs = new ArrayList<String>();
		for (String w: this.words)
			if (isAdj(w))
				adjs.add(w);
		return adjs;

	}
}


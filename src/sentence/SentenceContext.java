
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
import java.util.Map;
import java.util.HashMap;

public class SentenceContext
{
	private Random r;
	List<String> words;
	private Lexicon lexicon;
	private NLGFactory nlgFactory;
	private Realiser realiser;
	private RiWordnet net;
	private ModelLoader loader;
	private Categorical<Integer> noun_number_dist;
	private Categorical<String> connectors;
	private Categorical<String> names;

	public SentenceContext(String modelDir, List<String> words, String[] names)
	{
		this.words = words;
		this.r = new Random();
		lexicon = Lexicon.getDefaultLexicon();
		nlgFactory = new NLGFactory(lexicon);
		realiser = new Realiser(lexicon);
		net = new RiWordnet();
		loader = new ModelLoader(modelDir);

		Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
		counts.put(1, 20);
		counts.put(2, 1);
		counts.put(3, 1);
		this.noun_number_dist = new Categorical<Integer>(counts);

		Map<String, Integer> counts2 = new HashMap<String, Integer>();
		counts2.put(", and", 5);
		counts2.put(", but", 2);
		counts2.put(", so", 2);
		counts2.put("because", 2);
		counts2.put("while", 2);
		counts2.put(", yet", 1);
		counts2.put("when", 1);
		this.connectors = new Categorical<String>(counts2);

		Map<String, Integer> counts3 = new HashMap<String, Integer>();
		counts3.put(names[0], 4);
		counts3.put(names[1], 2);
		counts3.put(names[2], 1);
		this.names = new Categorical<String>(counts3);
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

	public boolean rand()
	{
		return r.nextBoolean();
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
		//return r.nextDouble() < 0.8;
		return true;
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

	public boolean isCompound()
	{
		return r.nextDouble() < 0.2; 
	}

	public boolean isNoun(String w)
	{
		return "n".equals(net.getBestPos(w));
	}

	public boolean isVerb(String w)
	{
		return "v".equals(net.getBestPos(w));
	}

	public boolean isAdj(String w)
	{
		return "a".equals(net.getBestPos(w));
	}

	public void markUsed(String w)
	{
		this.words.remove(w);
	}

	public int getNumNouns()
	{
		return this.noun_number_dist.draw();
	}

	public String getComplementiser()
	{
		return this.connectors.draw();
	}

	public List<String> getWords(String pos)
	{
		if (pos.equals("n"))
			return getNouns();
		else if (pos.equals("v"))
			return getVerbs();
		else if (pos.equals("a"))
			return getAdjs();
		return new ArrayList<String>();
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

	public String getPerson()
	{
		return this.names.draw();
	}
}


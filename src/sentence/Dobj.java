
package sentence;
import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import rita.wordnet.RiWordnet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class Dobj
{
	public static NLGElement generate(SentenceContext context, String verb)
	{
		NPPhraseSpec one, two, three;
		switch (context.getNumNouns())
		{
			case 1:
				return generateSingleDobj(context, verb);
			case 2:
				one = generateSingleDobj(context, verb);
				two = generateSingleDobj(context, verb);
				return context.getNLGFactory().createCoordinatedPhrase(one, two);
			case 3:
				one = generateSingleDobj(context, verb);
				two = generateSingleDobj(context, verb);
				three = generateSingleDobj(context, verb);
				CoordinatedPhraseElement ele = context.getNLGFactory().createCoordinatedPhrase(one, two);
				ele.addCoordinate(three);
				ele.setFeature(Feature.CONJUNCTION, "and"); // TODO: change this
				return ele;
			default:
					throw new RuntimeException("Unrecognized op");
		}
	}

	private static NPPhraseSpec generateSingleDobj(SentenceContext context, String verb)
	{
		NPPhraseSpec phrase;
		String dobj, adj;
		dobj = adj = null;

		// ensure operations are done in random order
		List<Integer> ops = new ArrayList<Integer>();
		ops.add(0);
		if (context.hasAdj())
			ops.add(1);
		Collections.shuffle(ops);

		for (Integer i: ops)
		{
			switch (i)
			{
				case 0:
					//dobj
					//System.out.println("\tNoun");
					dobj = chooseDobjNoun(context, verb, adj);
					break;
				case 1:
					// adj
					//System.out.println("\tAdj");
					adj = GenUtils.chooseNounAdj(context, dobj);
					break;
				default:
					throw new RuntimeException("Unrecognized op");
			}
		}
 		phrase = context.getNLGFactory().createNounPhrase(dobj);

		if (adj != null)
			phrase.addModifier(adj);

		if (context.hasDeterminer())
			phrase.setDeterminer(GenUtils.chooseNounDet(context, dobj));

		return phrase;
	}

	private static String chooseDobjNoun(SentenceContext context, String verb, String adj)
	{
		List<String> rels = new ArrayList<String>();
		List<String> words = new ArrayList<String>();
		rels.add("dobj");
		words.add(verb);
		if (adj != null)
		{
			rels.add("amod_r");
			words.add(adj);
		}
		return GenUtils.chooseWord(context, "n", rels, words);
	}
}



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
import java.util.Collections;


public class Subject
{
	public static NLGElement generate(SentenceContext context, String verb)
	{
		NPPhraseSpec one, two, three;
		switch (context.getNumNouns())
		{
			case 1:
				return generateSingleSubject(context, verb);
			case 2:
				one = generateSingleSubject(context, verb);
				two = generateSingleSubject(context, verb);
				return context.getNLGFactory().createCoordinatedPhrase(one, two);
			case 3:
				one = generateSingleSubject(context, verb);
				two = generateSingleSubject(context, verb);
				three = generateSingleSubject(context, verb);
				CoordinatedPhraseElement ele = context.getNLGFactory().createCoordinatedPhrase(one, two);
				ele.addCoordinate(three);
				ele.setFeature(Feature.CONJUNCTION, "and"); // TODO: change this
				return ele;
			default:
					throw new RuntimeException("Unrecognized op");
		}
	}

	private static NPPhraseSpec generateSingleSubject(SentenceContext context, String verb)
	{
		NPPhraseSpec phrase;
		String noun, adj;
		noun = adj = null;

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
					// noun
					//System.out.println("\tNoun");
					noun = chooseSubjectNoun(context, verb, adj);
					//System.out.println("Subject Noun: " + noun);
					break;
				case 1:
					// adj
					//System.out.println("\tAdj");
					adj = GenUtils.chooseNounAdj(context, noun);
					//System.out.println("Subject adj: " + adj);
					break;
				default:
					throw new RuntimeException("Unrecognized op");
			}
		}
		if (noun.equals("_person"))
			phrase = context.getNLGFactory().createNounPhrase(context.getPerson());
		else
			phrase = context.getNLGFactory().createNounPhrase(noun);

		if (adj != null)
			phrase.addModifier(adj);

		if (context.hasDeterminer())
		{
			String det = GenUtils.chooseNounDet(context, noun);
			//System.out.println("Subject det: " + det);
			phrase.setDeterminer(det);
		}

		return phrase;
	}

	private static String chooseSubjectNoun(SentenceContext context, String verb, String adj)
	{
		List<String> rels = new ArrayList<String>();
		List<String> words = new ArrayList<String>();
		rels.add("nsubj");
		words.add(verb);
		if (adj != null)
		{
			rels.add("amod_r");
			words.add(adj);
		}
		return GenUtils.chooseWord(context, "n", rels, words);
	}
}


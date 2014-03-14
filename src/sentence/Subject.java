
package sentence;
import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import rita.wordnet.RiWordnet;
import java.util.Random;
import java.util.List;


public class Subject
{
	public static NPPhraseSpec generate(SentenceContext context, String verb)
	{
		NPPhraseSpec phrase;
		List<String> nouns = context.getNouns();
		boolean has_determiner = context.hasDeterminer();
		boolean has_adjective = context.hasAdj();
		Categorical<String> dist = null;
		String noun = null;

		// choose the dobj
		if (verb != null)
		{
			dist = context.getModelLoader().get("nsubj", verb);
		}
		if (dist == null)
		{
			dist = context.getModelLoader().get("nsubj_r", "_all");
		}

		for (String n: nouns)
		{
			if (dist.elements().contains(n) && context.useListWord())
			{
				noun = n;
				context.markUsed(noun);
				break;
			}
		}

		if (noun == null)
			noun = dist.draw();
 		phrase = context.getNLGFactory().createNounPhrase(noun);

		if (has_determiner) 
		{
			dist = context.getModelLoader().get("det", noun);
			if (dist != null)
				phrase.setDeterminer(dist.draw());
		}

		// TODO, let use be able to choose adjectives first
		if (has_adjective)
		{
			dist = context.getModelLoader().get("amod", noun);
			if (dist != null)
				phrase.addModifier(dist.draw());
		}

		return phrase;
	}
}


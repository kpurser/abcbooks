
package sentence;
import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import rita.wordnet.RiWordnet;
import java.util.List;


public class Dobj
{
	public static NPPhraseSpec generate(SentenceContext context, String verb)
	{
		NPPhraseSpec phrase;
		List<String> dobjs = context.getNouns();
		boolean has_determiner = context.hasDeterminer();
		boolean has_adjective = context.hasAdj();
		Categorical<String> dist = null;
		String dobj = null;

		// choose the dobj
		if (verb != null)
		{
			dist = context.getModelLoader().get("dobj", verb);
		}
		if (dist == null)
		{
			dist = context.getModelLoader().get("dobj_r", "_all");
		}

		for (String d: dobjs)
		{
			if (dist.elements().contains(d) && context.useListWord())
			{
				dobj = d;
				context.markUsed(dobj);
				break;
			}
		}

		if (dobj == null)
			dobj = dist.draw();
 		phrase = context.getNLGFactory().createNounPhrase(dobj);

		if (has_determiner) 
		{
			dist = context.getModelLoader().get("det", dobj);
			if (dist != null)
				phrase.setDeterminer(dist.draw());
		}

		if (has_adjective)
		{
			dist = context.getModelLoader().get("amod", dobj);
			if (dist != null)
				phrase.addModifier(dist.draw());
		}

		return phrase;
	}
}



package sentence;
import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import rita.wordnet.RiWordnet;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class Predicate
{
	public static VPPhraseSpec generate(SentenceContext context, String subject, String dobj)
	{
		VPPhraseSpec phrase;
		boolean has_adverb = context.hasAdv();
		List<String> verbs = context.getVerbs();
		Categorical<String> dist = null;
		String verb = null;

		// build distribution to draw verb from
		if (subject != null && dobj != null)
		{
			Categorical<String> dist1 = context.getModelLoader().get("nsubj_r", subject);
			Categorical<String> dist2 = context.getModelLoader().get("dobj_r", dobj);
			Set<String> verb_set = dist1.elements();
			verb_set.retainAll(dist2.elements()); // intersection operation

			if (verb_set.isEmpty())
			{
				dist = context.getModelLoader().get("nsubj", "_all");
			}
			else
			{
				Map<String, Integer> counts = new HashMap<String, Integer>();
				for (String key: verb_set)
				{
					counts.put(key, new Integer(1));
				}
				dist = new Categorical<String>(counts);
			}
		}
		if (subject != null && dobj == null && verb == null)
		{
			dist = context.getModelLoader().get("nsubj_r", subject);
		}
		else if (subject == null && dobj != null && verb == null)
		{
			dist = context.getModelLoader().get("dobj_r", dobj);
		}
		else if (verb == null)
		{
			dist = context.getModelLoader().get("nsubj", "_all");
		}

		// check the list for any verbs we can use
		for (String v: verbs)
		{
			if (dist.elements().contains(v) && context.useListWord())
			{
				verb = v;
				context.markUsed(verb);
				break;
			}
		}
		if (verb == null)
			verb = dist.draw();

 		phrase = context.getNLGFactory().createVerbPhrase(verb);
		if (has_adverb)
		{
			dist = context.getModelLoader().get("advmod", verb);
			if (dist != null)
				phrase.addModifier(dist.draw());
		}
		return phrase;
	}
}


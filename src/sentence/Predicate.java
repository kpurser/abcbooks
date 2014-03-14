
package sentence;

import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import rita.wordnet.RiWordnet;
import java.util.List;
import java.util.ArrayList;


public class Predicate
{
	public static VPPhraseSpec generate(SentenceContext context, String subject, String dobj)
	{
		List<String> rels = new ArrayList<String>();
		List<String> words = new ArrayList<String>();
		rels.add("nsubj_r");
		words.add(subject);
		if (dobj != null)
		{
			rels.add("dobj_r");
			words.add("dobj");
		}
		String verb = GenUtils.chooseWord(context, "v", rels, words);
 		VPPhraseSpec phrase = context.getNLGFactory().createVerbPhrase(verb);

		if (context.hasAdv())
		{
			Categorical<String> dist = context.getModelLoader().get("advmod", verb);
			if (dist != null)
				phrase.addModifier(dist.draw());
		}
		return phrase;
	}
}


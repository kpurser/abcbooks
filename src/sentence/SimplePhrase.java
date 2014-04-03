
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


public class SimplePhrase
{
	public static SPhraseSpec generate(SentenceContext context)
	{
		String s, v, d;
		s = v = d = null;

		NLGElement subj = null;
		NLGElement dobj = null;
		VPPhraseSpec pred = null;
		PPPhraseSpec prep = null;
		SPhraseSpec phrase = null;

		// ensure operations are done in random order
		List<Integer> ops = new ArrayList<Integer>();
		ops.add(0);
		ops.add(1);
		if (context.hasDobj())	
			ops.add(2);
		if (context.hasPPhrase())
			ops.add(3);
		Collections.shuffle(ops);

		for (int k = 0; k < ops.size(); k++)
		{
			Integer i = ops.get(k);
			switch (i)
			{
				case 0:
					// subject
					//System.out.println("Subject");
					subj = Subject.generate(context, v);
					s = extractNoun(subj);
					//System.out.println("Subject: " + s);
					break;
				case 1:
					//System.out.println("Verb");
					pred = Predicate.generate(context, s, d);
					WordElement tmp = (WordElement) pred.getVerb();
					v = tmp.getBaseForm();
					if (tmp.getFeature(LexicalFeature.INTRANSITIVE) == null && d == null &&
							!ops.contains(2)) {
						//System.out.println("Adding in a Direct Object");
						ops.add(2);
					}
					break;
				case 2:
					// dobj
					//System.out.println("Dobj");
					dobj = Dobj.generate(context, v);
					d = extractNoun(dobj);
					break;
				case 3:
					// prep phrase
					prep = PrepPhrase.generate(context);
					break;
				default:
					throw new RuntimeException("Unrecognized op");
			}
		}

		if (dobj != null)
			phrase = context.getNLGFactory().createClause(subj, pred, dobj);
		else
			phrase = context.getNLGFactory().createClause(subj, pred);

		if (prep != null)
		{
			// don't interfere with rhyming
			if (context.rand())
				phrase.addFrontModifier(prep);
			else
				phrase.addPreModifier(prep);
		}
		return phrase;
	}

	private static String extractNoun(NLGElement ele)
	{
		if (ele instanceof NPPhraseSpec)
			return ((WordElement) (((NPPhraseSpec) ele).getNoun())).getBaseForm();
		else if (ele instanceof CoordinatedPhraseElement)
		{
			NLGElement first = ele.getChildren().get(0);
			return extractNoun(first);
		}
		return null;
	}
}



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
		NPPhraseSpec subj, dobj;
		VPPhraseSpec pred = null;
		SPhraseSpec phrase = null;
		subj = dobj = null;
		s = v = d = null;

		// ensure operations are done in random order
		List<Integer> ops = new ArrayList<Integer>();
		ops.add(0);
		ops.add(1);
		if (context.hasDobj())	
			ops.add(2);
		if (context.hasPPhrase())
			ops.add(3);
		Collections.shuffle(ops);

		for (Integer i: ops)
		{
			switch (i)
			{
				case 0:
					// subject
					subj = Subject.generate(context, v);
					s = ((WordElement) (subj.getNoun())).getBaseForm();
					break;
				case 1:
					pred = Predicate.generate(context, s, d);
					v = ((WordElement) (pred.getVerb())).getBaseForm();
					break;
				case 2:
					// dobj
					dobj = Dobj.generate(context, v);
					d = ((WordElement) (dobj.getNoun())).getBaseForm();
					break;
				case 3:
					// prep phrase
					throw new UnsupportedOperationException("Prep Phrase");
					//break;
				default:
					throw new RuntimeException("Unrecognized op");
			}
		}

		if (dobj != null)
			phrase = context.getNLGFactory().createClause(subj, pred, dobj);
		else
			phrase = context.getNLGFactory().createClause(subj, pred);
		return phrase;
	}
}


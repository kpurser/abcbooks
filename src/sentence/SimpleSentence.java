
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


public class SimpleSentence
{
	public static SPhraseSpec generate(SentenceContext context)
	{
		SPhraseSpec sentence = SimplePhrase.generate(context);

		if (context.isCompound())
		{
			SPhraseSpec subclause = SimplePhrase.generate(context);
			subclause.setFeature(Feature.COMPLEMENTISER, context.getComplementiser());
			sentence.addComplement(subclause);
		}
		return sentence;
	}
}


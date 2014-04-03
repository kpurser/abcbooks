
package sentence;
import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import rita.wordnet.RiWordnet;
import java.util.Random;


public class PrepPhrase
{
	public static PPPhraseSpec generate(SentenceContext context)
	{
		String location = context.getLocation();
		String prep = context.getPrep();
		NPPhraseSpec noun = context.getNLGFactory().createNounPhrase(location);
		if (context.hasDeterminer())
		{
			String det = GenUtils.chooseNounDet(context, location);
			noun.setDeterminer(det);
		}
		if (context.hasAdj())
		{
			String adj = GenUtils.chooseNounAdj(context, location);
			noun.addModifier(adj);
		}
		PPPhraseSpec phrase = context.getNLGFactory().createPrepositionPhrase();
		phrase.addComplement(noun);
		phrase.setPreposition(prep);
		return phrase;
	}
}


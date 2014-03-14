
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
	private SentenceContext context;
	private String prep;
	private String noun;
	private String determinator;
	private String adjective;
	private boolean has_determinator;
	private boolean has_adjective;

	public PrepPhrase(SentenceContext context)
	{
		this.context = context;
	}

	public PPPhraseSpec genPPP()
	{
		return null;
	}
}


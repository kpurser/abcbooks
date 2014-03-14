
package sentence;

import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;

public class Test
{
	public static void main(String args[])
	{
		System.out.println("Hello World!");
		SentenceGen sg = new SentenceGen(args[0]);
		for(int k = 0; k < 5; k++)
		{
			String out = sg.genSentence(args[1], args[2]);
			System.out.println(out);
			System.out.println("");
		}
	}
}


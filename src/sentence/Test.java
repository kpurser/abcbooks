
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
		String model_dir = args[0];
		String vocab_path = args[1];
		String prodict_path = args[2];
		String w1 = args[3];
		String w2 = args[4];
		ProDict pd = new ProDict(vocab_path, prodict_path);
		SentenceGen sg = new SentenceGen(model_dir, pd);
		for(int k = 0; k < 5; k++)
		{
			String out1 = sg.genSentence(w1);
			String out2 = sg.genSentence(w2);
			System.out.println(out1);
			System.out.println(out2);
			System.out.println("");
		}
	}
}


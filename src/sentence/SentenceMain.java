
package sentence;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import java.util.Scanner;
import java.util.Arrays;


public class SentenceMain
{

	public static void main(String[] args) throws IOException
	{
		String model_dir = args[0];
		String vocab_file = args[1];
		String prodict_file = args[2];
		String words_file = args[3];
		String out_file = args[4];
		String topic_word = args[5];

		String[] words = read_words(words_file);
		System.out.println(Arrays.toString(words));
		String[] sentences = make_sentences(model_dir, vocab_file, prodict_file, words, topic_word);
		System.out.println(Arrays.toString(sentences));
		write_sentences(out_file, sentences);
	}

	private static void write_sentences(String filename, String[] sentences) throws IOException
	{
		FileWriter w = new FileWriter(new File(filename));
		for (int k = 0; k < 27; k++)
			w.write(sentences[k] + "\n");
		w.flush();
		w.close();
	}

	private static String[] make_sentences(String model_dir, String vocab_file, String prodict_file, String[] words, String topic_word)
	{
		ProDict pd = new ProDict(vocab_file, prodict_file);
		SentenceGen sg = new SentenceGen(model_dir, pd);
		String[] sentences = new String[27];
		sentences[0] = sg.genTitle(topic_word);
		for (int k = 0; k < 26; k++)
		{
			String word = words[k];
			sentences[k+1] = sg.genSentence(word);
		}
		return sentences;
	}

	private static String[] read_words(String filename) throws IOException
	{
		String[] words = new String[26];
		Scanner s = new Scanner(new File(filename));
		int k = 0;
		while (s.hasNext())
			words[k++] = s.next();
		if (k != 26)
			throw new RuntimeException("Word tokens found: " + k);
		return words;
	}

}


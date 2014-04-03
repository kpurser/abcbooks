
package sentence;
import java.io.File;

public class ModelGenDriver
{
	public static void main(String args[])
	{
		String indir = args[0];
		String model_dir = args[1];
		String vocab_file = args[2];
		String name_file = args[3];
		String determiner_file = args[4];
		ModelGen mg = new ModelGen(indir, model_dir, vocab_file, name_file, determiner_file);

		//System.exit(0);

		String[] relations = {"nsubj",   "dobj",     "amod",     "det",      "advmod"};
		String[][] poses = { {"v", "n"}, {"v", "n"}, {"n", "a"}, {"n", "*"}, {"v", "r"} };

		int freq_thresh = 1;
		for (int k = 0; k < relations.length; k++)
		{
			String rel = relations[k];
			String[] pos = poses[k];
			System.out.println("********************************************************");
			System.out.println("Starting " + rel);
			System.out.println("********************************************************");
			mg.go(rel, pos[0], pos[1], freq_thresh, false);

			System.out.println("********************************************************");
			System.out.println("Starting " + rel + " in reverse");
			System.out.println("********************************************************");
			mg.go(rel, pos[1], pos[0], freq_thresh, true);
		}
	}
}


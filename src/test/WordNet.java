
import rita.wordnet.RiWordnet;
import java.util.Arrays;

public class WordNet
{

	public static void main(String args[])
	{
		String word = args[0];
		String pos = args[1];
		RiWordnet net = new RiWordnet();

		System.out.println("Number of senses:" + net.getSenseCount(word, pos));

		int[] senseids = net.getSenseIds(word, pos);
		if (senseids != null)
		{
			System.out.println("Descriptions of various word senses");
			for (int k = 0; k < senseids.length; k++)
			{
				int senseid = senseids[k];
				System.out.println("\t" + net.getDescription(senseid));
			}
		}
		System.out.println("");

		System.out.println("Examples...");
		System.out.println(Arrays.toString(net.getAllExamples(word, pos)));
		System.out.println("");


		System.out.println("getAllAlsoSees...");
		System.out.println(Arrays.toString(net.getAllAlsoSees(word, pos)));
		System.out.println("");

		System.out.println("getAllAntonyms...");
		System.out.println(Arrays.toString(net.getAllAntonyms(word, pos)));
		System.out.println("");

		System.out.println("getAllCoordinates...");
		System.out.println(Arrays.toString(net.getAllCoordinates(word, pos)));
		System.out.println("");

		System.out.println("getAllDerivedTerms...");
		System.out.println(Arrays.toString(net.getAllDerivedTerms(word, pos)));
		System.out.println("");

		System.out.println("getAllGlosses...");
		System.out.println(Arrays.toString(net.getAllGlosses(word, pos)));
		System.out.println("");

		System.out.println("getAllHypernyms...");
		System.out.println(Arrays.toString(net.getAllHypernyms(word, pos)));
		System.out.println("");

		System.out.println("getAllHyponyms...");
		System.out.println(Arrays.toString(net.getAllHyponyms(word, pos)));
		System.out.println("");

		System.out.println("getAllMeronyms...");
		System.out.println(Arrays.toString(net.getAllMeronyms(word, pos)));
		System.out.println("");

		System.out.println("getAllNominalizations...");
		System.out.println(Arrays.toString(net.getAllNominalizations(word, pos)));
		System.out.println("");

		System.out.println("getAllSimilar...");
		System.out.println(Arrays.toString(net.getAllSimilar(word, pos)));
		System.out.println("");

		System.out.println("getAllSynonyms...");
		System.out.println(Arrays.toString(net.getAllSynonyms(word, pos)));
		System.out.println("");

		System.out.println("getAllSynsets...");
		System.out.println(Arrays.toString(net.getAllSynsets(word, pos)));
		System.out.println("");

		System.out.println("getAllVerbGroups...");
		System.out.println(Arrays.toString(net.getAllVerbGroups(word, pos)));
		System.out.println("");

		System.out.println("***************** First sense Only ******************");

		System.out.println("getAlsoSees...");
		System.out.println(Arrays.toString(net.getAlsoSees(word, pos)));
		System.out.println("");

		System.out.println("getAntonyms...");
		System.out.println(Arrays.toString(net.getAntonyms(word, pos)));
		System.out.println("");

		System.out.println("getCoordinates...");
		System.out.println(Arrays.toString(net.getCoordinates(word, pos)));
		System.out.println("");

		System.out.println("getDerivedTerms...");
		System.out.println(Arrays.toString(net.getDerivedTerms(word, pos)));
		System.out.println("");

		System.out.println("getGloss...");
		System.out.println(net.getGloss(word, pos));
		System.out.println("");

		System.out.println("getHypernyms...");
		System.out.println(Arrays.toString(net.getHypernyms(word, pos)));
		System.out.println("");

		System.out.println("getHyponyms...");
		System.out.println(Arrays.toString(net.getHyponyms(word, pos)));
		System.out.println("");

		System.out.println("getMeronyms...");
		System.out.println(Arrays.toString(net.getMeronyms(word, pos)));
		System.out.println("");

		System.out.println("getNominalizations...");
		System.out.println(Arrays.toString(net.getNominalizations(word, pos)));
		System.out.println("");

		System.out.println("getSimilar...");
		System.out.println(Arrays.toString(net.getSimilar(word, pos)));
		System.out.println("");

		System.out.println("getSynonyms...");
		System.out.println(Arrays.toString(net.getSynonyms(word, pos)));
		System.out.println("");

		System.out.println("getSynset...");
		System.out.println(Arrays.toString(net.getSynset(word, pos)));
		System.out.println("");

		System.out.println("getVerbGroups...");
		System.out.println(Arrays.toString(net.getVerbGroup(word, pos)));
		System.out.println("");
	}
}

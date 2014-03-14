
package sentence;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;


public class Categorical<T>
{
	private Map<T, Double> probs;
	private static Random r = new Random();
	private double total;
	private T mode;

	public Categorical(Map<T, Integer> counts)
	{
		double max_prob = 0.0;
		T max_ele = null;
		total = 0.0;
		for (T key : counts.keySet())
		{
			double val = counts.get(key).doubleValue();
			total += val;
			if (val > max_prob)
			{
				max_ele = key;
				max_prob = val;
			}
		}

		if (total == 0.0) throw new IllegalArgumentException("Empty Distribution");
		this.probs = new HashMap<T, Double>();

		for (T key : counts.keySet())
		{
			Integer tmp = counts.get(key);
			this.probs.put(key, new Double(tmp.doubleValue() / total));
		}
	}

	public Double getProb(T key)
	{
		return probs.get(key);
	}

	public Set<T> elements()
	{
		return probs.keySet();
	}

	public T draw()
	{
		double target = r.nextDouble() - 10e-6; // handle floating point rounding
		double cumulative = 0.0;
		for (T key : probs.keySet())
		{
			cumulative += probs.get(key);
			if (cumulative >= target) {
				return key;
			}
		}
		throw new RuntimeException("Did not draw from categorical");
	}

	public double total()
	{
		return total;
	}

	public T mode()
	{
		return mode;
	}
}


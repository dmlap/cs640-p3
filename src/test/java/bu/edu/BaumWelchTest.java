package bu.edu;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class BaumWelchTest {

	@Test
	public void updateObservationProbabilitesSimple() {
		List<String> states = new ArrayList<String>();
		states.add("1");
		List<String> vocabulary = new ArrayList<String>();
		vocabulary.add("a");
		vocabulary.add("b");
		double[][] transitions = new double[][] { { 1.0D } };
		double[][] observations = new double[][] { { 0.1D, 0.9D } };
		double[] initials = new double[] { 1.0D };
		HiddenMarkovModel hmm = new HiddenMarkovModel(states, 1, vocabulary,
				transitions, observations, initials);
		List<String[]> obs = new ArrayList<String[]>();
		obs.add(new String[] { "a", "a" });
		BaumWelchOptimizer bwo = new BaumWelchOptimizer();
		HiddenMarkovModel hmm0 = bwo.optimize(hmm, new Observations(2, obs));

		// pi
		assertEquals(1.0D, hmm0.getInitialStateDistribution()[0], 0.001D);

		// A
		assertArrayEquals(new double[] { 1.0D }, hmm0
				.getTransitionProbabilities()[0], 0.001D);

		// B
		assertArrayEquals(new double[] { 1D, 0D }, hmm0
				.getObservationProbabilities()[0], 0.001D);

		// quick tests
		assertTrue(hmm0.getObservationProbabilities()[0][0] > hmm
				.getObservationProbabilities()[0][0]);
		assertTrue(hmm0.getObservationProbabilities()[0][1] < hmm
				.getObservationProbabilities()[0][1]);
		assertEquals(1.0D, hmm0.getObservationProbabilities()[0][0]
				+ hmm0.getObservationProbabilities()[0][1], 0.001D);
	}

}

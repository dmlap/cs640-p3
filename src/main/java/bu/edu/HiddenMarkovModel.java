/**
 * 
 */
package bu.edu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An object which simulates a hidden Markov model.
 * 
 * @author dml
 * 
 */
public class HiddenMarkovModel {
	private final List<String> states;
	private final int T;
	private final List<String> vocabulary;
	private final double[][] transitions;
	private final double[][] observations;
	private final double[] initials;
	
	public HiddenMarkovModel() {
		this.states = new ArrayList<String>();
		this.T = 0;
		this.vocabulary = new ArrayList<String>();
		this.transitions = new double[0][0];
		this.observations = new double[0][0];
		this.initials = new double[0];
	}

	/**
	 * n/ Constructs a new {@link HiddenMarkovModel} with the specified
	 * parameters.
	 * 
	 * @param states
	 *            - the {@link State States} of the model
	 * @param T
	 *            - the number of time steps or length of observation sequences
	 * @param vocabulary
	 *            - the observation symbol alphabet
	 * @param transitions
	 *            - the state transition probability distribution matrix
	 * @param observations
	 *            - the observation symbol probability distribution
	 * @param initials
	 *            - the initial state distribution
	 */
	public HiddenMarkovModel(List<String> states, int T, List<String> vocabulary,
			double[][] transitions, double[][] observations, double[] initials) {
		this.states = states;
		this.T = T;
		this.vocabulary = vocabulary;
		this.transitions = transitions;
		this.observations = observations;
		this.initials = initials;
	}

	public List<String> getStates() {
		return states;
	}
	
	public int getT() {
		return T;
	}
	
	public List<String> getVocabulary() {
		return vocabulary;
	}

	public double[][] getTransitionProbabilities() {
		return transitions;
	}

	public double[][] getObservationProbabilities() {
		return observations;
	}

	public double[] getInitialStateDistribution() {
		return initials;
	}
	
	/**
	 * Returns a table of the observation set number to the observed vocabulary
	 * index at a given time.
	 * 
	 * @param observations
	 *            - the set of {@link Observations} to build the table from.
	 * @return a table of the observation set number to the observed vocabulary
	 *         index at a given time.
	 */
	public int[][] translate(Observations observations) {
		// build vocab lookup table
		Map<String, Integer> translations = new HashMap<String, Integer>();
		for (int i = 0; i < getVocabulary().size(); ++i) {
			translations.put(getVocabulary().get(i), i);
		}
		int[][] words;
		words = new int[observations.getDatasets().size()][];
		for (int i = 0; i < words.length; ++i) {
			words[i] = translate(observations.getDatasets().get(i), translations);
		}
		return words;
	}
	
	/**
	 * @see #translate(Observations)
	 * @param observation
	 * @return
	 */
	public int[] translate(String[] observation) {
		return translate(new Observations(1, Collections
				.singletonList(observation)))[0];
	}
	
	private int[] translate(String[] observation, Map<String, Integer> lookup) {
		int[] result = new int[observation.length];
			for (int i = 0; i < result.length; ++i) {
				result[i] = lookup.get(observation[i]);
			}
		return result;
	}
}

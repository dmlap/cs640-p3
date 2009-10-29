/**
 * 
 */
package bu.edu;

import java.util.List;

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

	/**n/ 
	 * Constructs a new {@link HiddenMarkovModel} with the specified parameters.
	 * 
	 * @param states
	 *            - the {@link State States} of the model
	 * @param T
	 * 			  - the number of time steps or length of observation sequences
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

	public double[][] getTransitionProbabilites() {
		return transitions;
	}

	public double[][] getObservationProbabilities() {
		return observations;
	}

	public double[] getInitialStateDistribution() {
		return initials;
	}
}

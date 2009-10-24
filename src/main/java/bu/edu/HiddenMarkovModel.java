/**
 * 
 */
package bu.edu;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An object which simulates a hidden Markov model.
 * 
 * @author dml
 * 
 */
public class HiddenMarkovModel {
	private final List<? extends State> states;
	private final double[][] transitions;
	private final double[][] observations;
	private final double[] initials;

	/**
	 * Constructs a new {@link HiddenMarkovModel} with the specified parameters.
	 * 
	 * @param states
	 *            - the {@link State States} of the model
	 * @param transitions
	 *            - the state transition probability distribution matrix
	 * @param observations
	 *            - the observation symbol probability distribution
	 * @param initials
	 *            - the initial state distribution
	 */
	public HiddenMarkovModel(List<? extends State> states,
			double[][] transitions, double[][] observations, double[] initials) {
		this.states = states;
		this.transitions = transitions;
		this.observations = observations;
		this.initials = initials;
	}

	public Set<State> getStates() {
		return new HashSet<State>(states);
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

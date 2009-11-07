/**
 * 
 */
package bu.edu;




/**
 * An object that produces an optimized {@link HiddenMarkovModel} using the
 * Baum-Welch algorithm.
 * 
 * @author dml
 * 
 */
public class BaumWelchOptimizer {
	
	public HiddenMarkovModel optimize(HiddenMarkovModel input, Observations observations) {
		int[][] output = input.translate(observations);
		return optimize(input, output[0]);
	};
	
	public HiddenMarkovModel optimize(HiddenMarkovModel input, int[] observation) {
		Forward forward = new Forward(input);
		Backward backward = new Backward(input);
		double [][] alphas = forward.alpha(observation);
		double [][] betas = backward.beta(observation);
		
		// calculate pi'
		double[] pi = new double[input.getObservationProbabilities().length];
		for (int i = 0; i < pi.length; ++i) {
			pi[i] = gamma(0, i, alphas, betas);
		}
		// calculate A'
		double[][] a = new double[input.getTransitionProbabilities().length][input.getTransitionProbabilities()[0].length];
		for (int i = 0; i < input.getTransitionProbabilities().length; ++i) {
			for (int j = 0; j < input.getTransitionProbabilities()[0].length; ++j) {
				double sumXi = 0D;
				double sumGamma = 0D;
				for (int t = 0; t < observation.length - 1; ++t) {
					sumXi += xi(t, i, j, input, observation, alphas, betas);
					sumGamma += gamma(t, i, alphas, betas);
				}
				// only update the transition probability if it is defined
				a[i][j] = sumGamma != 0 ? sumXi / sumGamma : input.getTransitionProbabilities()[i][j];
			}
		}
		
		// calculate B'
		double[][] b = new double[input.getObservationProbabilities().length][input
				.getObservationProbabilities()[0].length];
		for (int j = 0; j < input.getObservationProbabilities().length; ++j) {
			for (int k = 0; k < input.getObservationProbabilities()[0].length; ++k) {
				double sumGammaK = 0D; // gammas for times t where observation[t] == k
				double sumGamma = 0D; // all gammas
				for (int t = 0; t < observation.length; ++t) {
					double gamma = gamma(t, j, alphas, betas);
					sumGammaK += observation[t] == k ? gamma : 0;
					sumGamma += gamma;
				}
				// only update observation probability if it is defined
				b[j][k] = sumGamma != 0 ? sumGammaK / sumGamma : input.getObservationProbabilities()[j][k];
			}
		}
		return new HiddenMarkovModel(input.getStates(), observation.length, input
				.getVocabulary(), a, b, pi);
	}

	/**
	 * @param t
	 * @param i
	 * @param j
	 * @param input
	 * @param observation
	 * @param alphas
	 * @param betas
	 * @return
	 */
	private double xi(int t, int i, int j, HiddenMarkovModel input,
			int[] observation, double[][] alphas, double[][] betas) {
		double numerator = alphas[t][i]
				* input.getTransitionProbabilities()[i][j]
				* input.getObservationProbabilities()[j][observation[t + 1]]
				* betas[t + 1][j];
		double denominator = 0D;
		for (int l = 0; l < input.getTransitionProbabilities().length; ++l) {
			for (int m = 0; m < input.getTransitionProbabilities().length; ++m) {
				denominator += alphas[t][l]
						* input.getTransitionProbabilities()[l][m]
						* input.getObservationProbabilities()[m][observation[t + 1]]
						* betas[t + 1][m];
			}
		}
		return numerator / denominator;
	}

	/**
	 * Returns the value of gamma at time <code>t</code> for state
	 * <code>j</code> from the given state-time indexed values of alpha and
	 * beta.
	 * 
	 * @param t
	 * @param j
	 * @param alphas
	 * @param betas
	 * @return
	 */
	private double gamma(int t, int i, double[][] alphas, double[][] betas) {
		double denominator = 0D;
		for(int j = 0; j < alphas[0].length; ++j) {
			denominator += alphas[t][j] * betas[t][j];
		}
		return (alphas[t][i] * betas[t][i]) / denominator;
	}

}

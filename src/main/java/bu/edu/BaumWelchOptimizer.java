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
	
	private HiddenMarkovModel optimize(HiddenMarkovModel input, int[] observation) {
		Forward forward = new Forward(input);
		Backward backward = new Backward(input);
		double [][] alphas = forward.alpha(observation);
		double [][] betas = backward.beta(observation);
		
		// calculate pi'
		double[] pi = new double[input.getObservationProbabilities().length];
		for (int i = 0; i < pi.length; ++i) {
			pi[i] = gamma(1, i, alphas, betas);
		}
		// calculate A'
		double[][] a = new double[input.getObservationProbabilities().length][input.getObservationProbabilities().length];
		for (int i = 0; i < input.getTransitionProbabilities().length; ++i) {
			for (int j = 0; j < input.getTransitionProbabilities()[0].length; ++j) {
				double sumEpsilon = 0D;
				double sumGamma = 0D;
				for (int t = 0; t < observation.length - 1; ++t) {
					sumEpsilon += epsilon(t, i, j, input, observation, alphas, betas);
					sumGamma += gamma(t, i, alphas, betas);
				}
				a[i][j] = sumEpsilon / sumGamma;
			}
		}
		
		// calculate B'
		double[][] b = new double[input.getObservationProbabilities().length][input
				.getObservationProbabilities()[0].length];
		for (int j = 0; j < input.getObservationProbabilities().length; ++j) {
			for (int k = 0; k < input.getObservationProbabilities()[0].length; ++k) {
				double sumGammaK = 0D;
				for (int t = 0; t < observation.length; ++t) {
					sumGammaK += gamma(t, j, alphas, betas);
				}
				b[j][k] = sumGammaK * input.getObservationProbabilities()[j][k] / sumGammaK;
			}
		}
		return new HiddenMarkovModel(input.getStates(), observation.length, input
				.getVocabulary(), a, b, pi);
	}

	private double epsilon(int t, int i, int j, HiddenMarkovModel input,
			int[] observation, double[][] alphas, double[][] betas) {
		double numerator = alphas[t][i]
				* input.getTransitionProbabilities()[i][j]
				* input.getObservationProbabilities()[j][observation[t + 1]]
				* betas[t + 1][j];
		double denominator = 0D;
		for (int l = 0; l < input.getTransitionProbabilities().length; ++l) {
			for (int m = 0; m < input.getTransitionProbabilities().length; ++m) {
				denominator += alphas[t][i]
						* input.getTransitionProbabilities()[i][j]
						* input.getObservationProbabilities()[j][observation[t + 1]]
						* betas[t + 1][j];
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

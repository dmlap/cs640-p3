package bu.edu;


public class Forward {
	private HiddenMarkovModel hmm;

	public Forward(HiddenMarkovModel h) {
		this.hmm = h;
	}

	public double recognize(int[] observation) {
		int T = observation.length; // size of observation sequence
		int N = hmm.getStates().size(); // number of state nodes
		double[][] alpha = alpha(observation);
		
		double sum = 0;
		for (int i = 0; i < N; i++)
			sum += alpha[T-1][i];
		
		return sum;
	}
	
	/**
	 * Returns the values of alpha in a state-time matrix
	 * 
	 * @param observation
	 *            - the time-ordered sequence of observed symbols
	 * @return a state-by-time indexed matrix of the values of alpha for the
	 *         observation sequence.
	 */
	public double[][] alpha(int[] observation) {
		int T = observation.length;
		int N = hmm.getStates().size(); // number of state nodes
		double[][] A = hmm.getTransitionProbabilities();
		double[][] B = hmm.getObservationProbabilities();
		double[] pi = hmm.getInitialStateDistribution();
		double[][] alpha = new double[T][N];
		
		for (int t = 0; t < T; t++) { // outer loop (t=1...time)
			for (int i = 0; i < N; i++) { // inner loop (i=1...N)
				if (t == 0) // initialization
					alpha[t][i] = pi[i] * B[i][observation[t]];
				else {
					for (int j = 0; j < N; j++) {
						alpha[t][i] += alpha[t - 1][j] * A[j][i];
					}// j = N
					
					alpha[t][i] *= B[i][observation[t]];
				}
			}// i = N
		}// t = time
		return alpha;
	}
	
	public void recognizeDatasets(Observations observations) {
		int[][] translateds = hmm.translate(observations);
		for (int[] translated : translateds) {
			double prob = recognize(translated);
			System.out.println(prob);
		}
	}
}

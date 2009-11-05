package bu.edu;

public class Backward {
	private HiddenMarkovModel hmm;

	public Backward(HiddenMarkovModel h) {
		this.hmm = h;
	}

//	public double backward(int set) {
//		int T = obs.getDatasets().get(set).length; // size of observation sequence
//		double[][] beta = beta(words[set]);
//		
//		double sum = 0;
//		for (int i = 0; i < T; i++)
//			sum += beta[0][i];
//
//		return sum;
//	}
	
	public double[][] beta(int[] observation) {
		double[][] A = hmm.getTransitionProbabilities();
		double[][] B = hmm.getObservationProbabilities();
		int T = observation.length;
		int N = hmm.getStates().size(); // number of state nodes
		double[][] beta = new double[T][N]; 

		for (int t = T; t > 0; t--) {
			for (int i = 0; i < N; i++) {
				if (t == T)
					beta[T-1][i] = 1;
				else {
					for (int j = 0; j < N; j++) {
						beta[t-1][i] += A[i][j] * B[j][observation[t]] * beta[t][j];
					}
				}
			}
		}
		return beta;
	}
}
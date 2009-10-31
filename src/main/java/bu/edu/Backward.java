package bu.edu;

public class Backward {
	private HiddenMarkovModel hmm;
	private Observations obs;

	public Backward(HiddenMarkovModel h, Observations o) {
		this.hmm = h;
		this.obs = o;
	}

	public double backward(int set) {
		double[][] A = hmm.getTransitionProbabilities();
		double[][] B = hmm.getObservationProbabilities();
		int T = obs.getDatasets().get(set).length; // size of observation sequence
		int N = hmm.getStates().size(); // number of state nodes
		double[][] beta = new double[T][N];

		for (int t = T; t > 0; t--) {
			for (int i = 0; i < N; i++) {
				if (t == T)
					beta[T-1][i] = 1;
				else {
					for (int j = 0; j < N; j++) {
						beta[t-1][i] += A[i][j] * B[j][wordIndex(set, t)] * beta[t][j];
					}
				}
			}
		}

		double sum = 0;
		for (int i = 0; i < T; i++)
			sum += beta[0][i];

		return sum;
	}

	private int wordIndex(int set, int t) {
		String currentWord = this.obs.getDatasets().get(set)[t];
		return hmm.getVocabulary().indexOf(currentWord);
	}
}
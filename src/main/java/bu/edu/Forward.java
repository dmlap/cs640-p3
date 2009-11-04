package bu.edu;

public class Forward {
	private HiddenMarkovModel hmm;
	private Observations obs;

	public Forward(HiddenMarkovModel h, Observations o) {
		this.hmm = h;
		this.obs = o;
	}

	private int wordIndex(int set, int t) {
		String currentWord = this.obs.getDatasets().get(set)[t];
		return hmm.getVocabulary().indexOf(currentWord);
	}

	private double recognize(int set) {
		double[][] A = hmm.getTransitionProbabilities();
		double[][] B = hmm.getObservationProbabilities();
		double[] pi = hmm.getInitialStateDistribution();
		int T = obs.getDatasets().get(set).length; // size of observation sequence
		int N = hmm.getStates().size(); // number of state nodes
		double[][] alpha = new double[N][T];
		
		for (int t = 0; t < T; t++) { // outer loop (t=1...T)
			for (int i = 0; i < N; i++) { // inner loop (i=1...N)
				if (t == 0) // initialization
					alpha[i][t] = pi[i] * B[i][wordIndex(set, t)];
				else {
					for (int ii = 0; ii < N; ii++) {
						alpha[i][t] += alpha[ii][t - 1] * A[ii][i];
					}// ii = N
					
					alpha[i][t] *= B[i][wordIndex(set, t)];
				}
			}// i = N
		}// t = T
		
		double sum = 0;
		for (int i = 0; i < N; i++)
			sum += alpha[i][T-1];
		
		return sum;
	}
	
	public void recognizeDatasets() {
		for (int i = 0; i < obs.getCount(); i++) {
			double prob = recognize(i);
			System.out.println(prob);
		}
	}
}

package bu.edu;

import java.lang.String;
import java.util.List;

public class ViterbiAlgorithm {
	private HiddenMarkovModel hmm;
	private Observations obs;
	
	public ViterbiAlgorithm(HiddenMarkovModel h, Observations o) {
		this.hmm = h;
		this.obs = o;
	}
	
	private int wordIndex(int set, int t) {
		String currentWord = this.obs.getDatasets().get(set)[t];
		return hmm.getVocabulary().indexOf(currentWord);
	}
	
	private void algorithm(int set) {
		int N=hmm.getStates().size(); 
		int T=obs.getDatasets().get(set).length; 
		int i=0;
		double a[][] = hmm.getTransitionProbabilities();
		double b[][] = hmm.getObservationProbabilities();
		double pi[]  = hmm.getInitialStateDistribution();
		List <String> S = hmm.getStates();

		double[][] del = new double[T+1][N+1];
		double p8 = 0.00; 
		int[][] si = new int[T+1][N+1];
		int qT8 = 0;
		int[] ss = new int[T+1];
	
		// Initialization
		for(i=1; i<=N; i++) {
			del[1][i] = pi[i-1] * b[i-1][wordIndex(set, 0)];
			si[1][i] = 0;
		}
	
		// Recursion
		for(int t=2; t<=T; t++) {		
			for(int j=1; j<=N; j++) {
				double maxdel = 0.00, temp = 0.00;
				int argmaxdel = 0;
				
				for(i=1; i<=N; i++) {
					temp = del[t-1][i] * a[i-1][j-1];
					if(maxdel<temp) {
						maxdel = temp;
						argmaxdel = i;
					}
				}
			
				del[t][j] = maxdel * b[j-1][wordIndex(set, t-1)]; 
				si[t][j] = argmaxdel;
			}
		}
	
		// Termination
		for(i=1; i<=N; i++) {
			if(p8 < del[T][i]) {
				p8 = del[T][i];
				qT8 = i;
			}
		}
	
		// Path (state sequence) backtracking
		ss[T]=qT8;
		for(i=T-1; i>=1; i--) {
			ss[i] = si[i+1][ss[i+1]];
		}
	
		// Output optimal state sequence
		System.out.print(p8);
		System.out.print(" ");
		for(i = 1; i <= T; i++) {
			System.out.print(S.get(ss[i]-1));
			System.out.print(" ");
		}
		System.out.print("\n");
	}
	
	public void statepathofDatasets() {
		for (int i = 0; i < obs.getCount(); i++) {
			algorithm(i);
		}
	}
} 
package bu.edu;

/**
 * 
 */
public class App {
	public static void main(String[] args) {
		if (args.length == 3) {
			String operation, hmmfile, obsfile;

			 operation = args[0]; 
			 hmmfile = args[1]; obsfile = args[2];

			 Parser parser = new Parser();
			HiddenMarkovModel hmm = parser.parseHMMfile(hmmfile);
			Observations obs = parser.parseObsFile(obsfile);
			BaumWelchOptimizer bwo = new BaumWelchOptimizer();

			if ("optimize".equals(operation)) {
				Forward f = new Forward(hmm);
				int[][] translateds = hmm.translate(obs);
				for(int[] translated : translateds) {
					System.out.print(f.recognize(translated));
					System.out.print(" ");
					System.out.println(new Forward(bwo.optimize(hmm, translated)).recognize(translated));
				}
			} else if ("recognize".equals(operation)) {
				Forward fwd = new Forward(hmm);
				fwd.recognizeDatasets(obs);
			}
			else if ("statepath".equals(operation)) {
				ViterbiAlgorithm viterbi = new ViterbiAlgorithm(hmm, obs);
				viterbi.statepathofDatasets();
			}
			else
				System.err.println("Invalid operation switch (" + operation + ")");
		} else
			System.err.println("Expecting 3 strings as input (switch, hmm file, observation file)");
	}
}

package bu.edu;

/**
 * 
 */
public class App {
	public static void main(String[] args) {
		if (args.length == 0) {
			String operation, hmmfile, obsfile;

			// TODO: remove comment
			/*
			 * operation = args[0]; hmmfile = args[1]; obsfile = args[2];
			 */

			// TODO: remove
			operation = "statepath";
			hmmfile = "C:\\sentence.hmm";
			obsfile = "C:\\example1.obs";
			
			Parser parser = new Parser();
			HiddenMarkovModel hmm = parser.parseHMMfile(hmmfile);
			Observations obs = parser.parseObsFile(obsfile);

			if (operation == "optimize")
				; // TODO: call function
			else if (operation == "recognize") {
				Forward fwd = new Forward(hmm);
				fwd.recognizeDatasets(obs);
			}
			else if (operation == "statepath") {
				ViterbiAlgorithm viterbi = new ViterbiAlgorithm(hmm, obs);
				viterbi.statepathofDatasets();
			}
			else
				System.err.println("Invalid operation switch (" + operation + ")");
		} else
			System.err.println("Expecting 3 strings as input (switch, hmm file, observation file)");
	}
}

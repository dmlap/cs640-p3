package bu.edu;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
	public HiddenMarkovModel parseHMMfile(String hmmFile) {
		FileInputStream fstream = null;
		DataInputStream dstream = null;
		BufferedReader reader = null;

		// Read the hmm file
		try {
			fstream = new FileInputStream(hmmFile);
			dstream = new DataInputStream(fstream);
			reader = new BufferedReader(new InputStreamReader(dstream));

			// Read header
			String line = reader.readLine();
			String[] header = line.split(" ");

			if (header.length != 3)
				throw new ParseException("Invalid header in hmm file", 0);

			// Save header values
			int N = Integer.parseInt(header[0]); // number of states
			int M = Integer.parseInt(header[1]); // number of observation states
			int T = Integer.parseInt(header[2]); // length of observation sequence

			// Pull the states
			line = reader.readLine();
			List<String> states = new ArrayList<String>(Arrays.asList(line.split(" ")));

			// Check size
			if (states.size() != N)
				throw new ParseException("Number of states does not match header value", 0);

			// Pull the vocabulary
			line = reader.readLine();
			List<String> vocabulary = new ArrayList<String>(Arrays.asList(line.split(" ")));

			// Check size
			if (vocabulary.size() != M)
				throw new ParseException("Number of observation values does not match header value",
						0);

			// Check for A array
			double[][] a = new double[states.size()][states.size()];
			line = reader.readLine();
			if (line.trim().compareToIgnoreCase("a:") == 0) {
				for (int i = 0; i < states.size(); i++) {
					line = reader.readLine();
					String[] values = line.split(" ");

					// Check length
					if (values.length != states.size())
						throw new ParseException("Invalid number of columns in the a matrix", 0);

					// Load array
					for (int j = 0; j < values.length; j++) {
						a[i][j] = Double.parseDouble(values[j]);
					}
				}
			} else
				throw new ParseException("Missing 'a' matrix or wrong order", 0);

			// Check for B array
			double[][] b = new double[states.size()][vocabulary.size()];
			line = reader.readLine();
			if (line.trim().compareToIgnoreCase("b:") == 0) {
				for (int i = 0; i < states.size(); i++) {
					line = reader.readLine();
					String[] values = line.split(" ");

					// Check length
					if (values.length != vocabulary.size())
						throw new ParseException("Invalid number of columns in the b matrix", 0);

					// Load array
					for (int j = 0; j < values.length; j++) {
						b[i][j] = Double.parseDouble(values[j]);
					}
				}
			} else
				throw new ParseException("Missing 'b' matrix or wrong order", 0);

			// Check for pi array
			double[] pi = new double[states.size()];
			line = reader.readLine();
			if (line.trim().compareToIgnoreCase("pi:") == 0) {
				line = reader.readLine();
				String[] values = line.split(" ");

				// Check length
				if (values.length != states.size())
					throw new ParseException("Invalid number of pi starting values", 0);

				// Load array
				for (int i = 0; i < states.size(); i++)
					pi[i] = Double.parseDouble(values[i]);
			}

			// Clean up
			dstream.close();

			// Return hmm object
			return new HiddenMarkovModel(states, T, vocabulary, a, b, pi);
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			return new HiddenMarkovModel();
		}
	}

	public Observations parseObsFile(String obsFile) {
		FileInputStream fstream = null;
		DataInputStream dstream = null;
		BufferedReader reader = null;

		// Read the obs file
		try {
			// Read observation file
			fstream = new FileInputStream(obsFile);
			dstream = new DataInputStream(fstream);
			reader = new BufferedReader(new InputStreamReader(dstream));

			// Get count
			String line = reader.readLine();
			int count = Integer.parseInt(line);

			// Get data sets
			List<String[]> datasets = new ArrayList<String[]>(count);
			for (int i = 0; i < count; i++) {
				line = reader.readLine();
				int datacount = Integer.parseInt(line);

				line = reader.readLine();
				datasets.add(line.split(" "));

				// Check length
				if (datacount != datasets.get(i).length)
					throw new ParseException("Data count does not match number of words found",	0);
			}

			// Clean up
			dstream.close();

			// Create observations object
			return new Observations(count, datasets);
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			return new Observations();
		}
	}
}

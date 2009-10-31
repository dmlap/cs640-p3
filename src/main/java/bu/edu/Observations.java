package bu.edu;

import java.util.ArrayList;
import java.util.List;

public class Observations {
	private final int count;
	private final List<String[]> datasets;
	
	public Observations() {
		this.count = 0;
		this.datasets = new ArrayList<String[]>();
	}
	
	public Observations(int count, List<String[]> datasets) {
		this.count = count;
		this.datasets = datasets;
	}
	
	public int getCount() {
		return count;
	}
	
	public List<String[]> getDatasets() {
		return datasets;
	}
}

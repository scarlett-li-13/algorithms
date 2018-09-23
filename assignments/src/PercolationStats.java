import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private double[] results;
	private double mean;
	private double std;
	private double alpha;

	// perform trials independent experiments on an n-by-n grid
	public PercolationStats(int grid_size, int n_trials) {
		if (grid_size <= 0 || n_trials <= 0) {
			throw new IllegalArgumentException("Please enter an integer.");
		}
		results = new double[n_trials];
		for (int i = 0; i < n_trials; i++) {
			Percolation trial = new Percolation(grid_size);
			while (!trial.percolates()) {
				int row = StdRandom.uniform(1, grid_size + 1);
				int col = StdRandom.uniform(1, grid_size + 1);
				trial.open(row, col);
			}
			results[i] = (double) trial.numberOfOpenSites() / (grid_size * grid_size);
		}
		mean = StdStats.mean(results);
		std = StdStats.stddev(results);
		alpha = 1.96 / Math.sqrt((double) n_trials); // 95% confidence interval from CLT
	}

	// sample mean of percolation threshold
	public double mean() {
		return mean;
	}

	// sample standard deviation of percolation threshold
	public double stddev() {
		return std;
	}

	// low endpoint of 95% confidence interval
	public double confidenceLo() {
		return mean - alpha * std;
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi() {
		return mean + alpha * std;
	}

	// test client
	public static void main(String[] args) {
		int grid_size = Integer.parseInt(args[0]);
		int n_trials = Integer.parseInt(args[1]);
		PercolationStats experiment = new PercolationStats(grid_size, n_trials);
		double mean = experiment.mean();
		double stddev = experiment.stddev();
		double low = experiment.confidenceLo();
		double high = experiment.confidenceHi();
		System.out.format("mean                     = %f %n" + "stddev                   = %f %n"
				+ "95%% confidence interval  = [%f, %f]", mean, stddev, low, high);
	}
}
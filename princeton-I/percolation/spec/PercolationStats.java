import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

  private double[] history = null;
  private int trails = 0;

  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n < 1 || trials < 1) {
      throw new IllegalArgumentException();
    }
    this.trails = trials;
    history = new double[trials];
    for (int t = 0; t < trials; t++) {
      Percolation perc = new Percolation(n);
      while (!perc.percolates()) {
        int i = StdRandom.uniform(1, n + 1);
        int j = StdRandom.uniform(1, n + 1);
        perc.open(i, j);
      }
      history[t] = (double) perc.numberOfOpenSites() / (n * n);
    }
  }

  // sample mean of percolation threshold
  public double mean() {
    return StdStats.mean(history);
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return StdStats.stddev(history);
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return mean() - 1.96 * stddev() / Math.sqrt(trails);
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return mean() + 1.96 * stddev() / Math.sqrt(trails);
  }

  // test client (see below)
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);
    PercolationStats s = new PercolationStats(n, trials);
    StdOut.printf("mean=%s\n", s.mean());
    StdOut.printf("stddev=%s\n", s.stddev());
    String f = "95%% confidence interval = [%f, %f]\n";
    StdOut.printf(f, s.confidenceLo(), s.confidenceHi());
    // // System.out.println(Arrays.toString(args));
  }

}

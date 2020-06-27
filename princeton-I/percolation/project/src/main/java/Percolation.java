import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

  private final int dimension;
  private boolean[][] arr = null;
  private int numberOfOpenSites = 0;
  private WeightedQuickUnionUF uf = null;
  private WeightedQuickUnionUF ufF = null;
  private int virtualTopSiteIndex = -1;
  private int virtualBottomSiteIndex = -1;
  private int virtualTopSiteIndexF = -1;

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(final int n) {
    if (n <= 0) {
      throw new IllegalArgumentException("n needs to be bigger than 0");
    }
    dimension = n;
    arr = new boolean[n][n];
    uf = new WeightedQuickUnionUF(n * n + 2);
    ufF = new WeightedQuickUnionUF(n * n + 1);
    virtualTopSiteIndex = n * n;
    virtualBottomSiteIndex = n * n + 1;
    virtualTopSiteIndexF = n * n;
  }

  // opens the site (row, col) if it is not open already
  public void open(final int row, final int col) {
    checkInput(row, col);
    if (isOpen(row, col)) {
      return;
    }

    arr[row - 1][col - 1] = true;
    numberOfOpenSites++;
    final int idx = (row - 1) * dimension + (col - 1);
    if (row > 1) {
      // up
      if (isOpen(row - 1, col)) {
        uf.union(idx, idx - dimension);
        ufF.union(idx, idx - dimension);
      }
    }
    if (row < dimension) {
      // down
      if (isOpen(row + 1, col)) {
        uf.union(idx, idx + dimension);
        ufF.union(idx, idx + dimension);
      }
    }
    if (col > 1) {
      // left
      if (isOpen(row, col - 1)) {
        uf.union(idx, idx - 1);
        ufF.union(idx, idx - 1);
      }
    }
    if (col < dimension) {
      // right
      if (isOpen(row, col + 1)) {
        uf.union(idx, idx + 1);
        ufF.union(idx, idx + 1);
      }
    }
    if (row == 1) {
      // top virtual site
      uf.union(idx, virtualTopSiteIndex);
      ufF.union(idx, virtualTopSiteIndex);
    }
    if (row == dimension) {
      uf.union(idx, virtualBottomSiteIndex);
    }
  }

  private void checkInput(final int row, final int col) {
    if (row < 1 || row > dimension || col < 1 || col > dimension) {
      throw new IllegalArgumentException("site index is invalid");
    }
  }

  // is the site (row, col) open?
  public boolean isOpen(final int row, final int col) {
    checkInput(row, col);
    return arr[row - 1][col - 1] == true;
  }

  // is the site (row, col) full?
  public boolean isFull(final int row, final int col) {
    checkInput(row, col);
    final int idx = (row - 1) * dimension + (col - 1);
    return ufF.find(idx) == ufF.find(virtualTopSiteIndexF);
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return this.numberOfOpenSites;
  }

  // does the system percolate?
  public boolean percolates() {
    return uf.find(virtualBottomSiteIndex) == uf.find(virtualTopSiteIndex);
    // for (int i = 1; i <= dimension; i++) {
    // if (isFull(dimension, i)) {
    // return true;
    // }
    // }
    // return false;
  }

  // test client (optional)
  public static void main(final String[] args) {
    // StdOut.printf("[%.2f, %f]", (double) 0.2, (double) 0.3);
  }
}

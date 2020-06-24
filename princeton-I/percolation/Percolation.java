import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int dimention = -1;
    private int[][] arr = null;
    private WeightedQuickUnionUF uf = null;
    private int virtualTopSiteIndex = -1;
    private int virtualBottomSiteIndex = -1;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n needs to be bigger than 0");
        }
        dimention = n;
        arr = new int[n][n];
        uf = new WeightedQuickUnionUF(n * n + 2);
        virtualTopSiteIndex = n * n;
        virtualBottomSiteIndex = n * n + 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkInput(row, col);
        if (isOpen(row, col)) {
            return;
        }

        arr[row - 1][col - 1] = 1;
        int idx = (row - 1) * dimention + (col - 1);
        if (row > 1) {
            // up
            if (isOpen(row - 1, col)) {
                uf.union(idx, idx - dimention);
            }
        }
        if (row < dimention) {
            // down
            if (isOpen(row + 1, col)) {
                uf.union(idx, idx + dimention);
            }
        }
        if (col > 1) {
            // left
            if (isOpen(row, col - 1)) {
                uf.union(idx, idx - 1);
            }
        }
        if (col < dimention) {
            // right
            if (isOpen(row, col + 1)) {
                uf.union(idx, idx + 1);
            }
        }
        if (row == 1) {
            // top virtual site
            uf.union(idx, virtualTopSiteIndex);
        }
        if (row == dimention) {
            // bottom virtual site
            uf.union(idx, virtualBottomSiteIndex);
        }
    }

    private void checkInput(int row, int col) {
        if (row < 1 || row > dimention || col < 1 || col > dimention) {
            throw new IllegalArgumentException("site index is invalid");
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkInput(row, col);
        return arr[row - 1][col - 1] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkInput(row, col);
        int idx = (row - 1) * dimention + (col - 1);
        return uf.find(idx) == uf.find(virtualTopSiteIndex);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int cnt = 0;
        for (int i = 1; i <= dimention; i++) {
            for (int j = 1; j <= dimention; j++) {
                if (isOpen(i, j)) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(virtualTopSiteIndex) == uf.find(virtualBottomSiteIndex);
    }

    // test client (optional)
    public static void main(String[] args) {
        // int[] in = StdIn.readAllInts();
        // System.out.println(in.length);
    }
}

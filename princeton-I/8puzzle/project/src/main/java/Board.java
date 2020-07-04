import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import edu.princeton.cs.algs4.StdRandom;

public class Board {

  private final int[][] tiles;
  private final int n;
  private int hamming = -1;
  private int manhattan = -1;
  private Board twin = null;
  private int[] emptySpace = null;

  // create a board from an n-by-n array of tiles,
  // where tiles[row][col] = tile at (row, col)
  public Board(int[][] tiles) {
    if (tiles == null) {
      throw new IllegalArgumentException();
    }
    this.tiles = tiles;
    this.n = tiles.length;
  }

  // string representation of this board
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(tiles.length).append("\n");

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        String cell = String.format("%3d ", tiles[i][j]);
        sb.append(cell);
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  // board dimension n
  public int dimension() {
    return n;
  }

  // number of tiles out of place
  public int hamming() {
    if (hamming < 0) {
      hamming = 0;
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          int cell = tiles[i][j];
          if (cell == 0) {
            continue;
          }

          if (cell - 1 != i * n + j) {
            hamming++;
          }
        }
      }
    }
    return hamming;
  }

  // sum of Manhattan distances between tiles and goal
  public int manhattan() {
    if (manhattan < 0) {

      manhattan = 0;
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          int cell = tiles[i][j];
          if (cell == 0) {
            continue;
          }

          int iM = (cell - 1) / n;
          int jM = (cell - 1) - iM * n;
          manhattan = manhattan + Math.abs(iM - i) + Math.abs(jM - j);
        }
      }
    }
    return manhattan;
  }

  // is this board the goal board?
  public boolean isGoal() {
    return hamming() == 0;
  }

  // does this board equal y?
  public boolean equals(Object y) {
    if (y == null || !(y instanceof Board)) {
      return false;
    }

    Board that = (Board) y;
    if (!Arrays.equals(this.getEmptySpace(), that.getEmptySpace())) {
      return false;
    }
    return (this.n == that.n && Arrays.deepEquals(this.tiles, that.tiles));
  }

  private int[] getEmptySpace() {
    if (emptySpace != null) {
      return emptySpace;
    }
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (tiles[i][j] == 0) {
          emptySpace = new int[] { i, j };
          return emptySpace;
        }
      }
    }
    throw new IllegalArgumentException();
  }

  private Board move(int dir) {
    int[] orig = getEmptySpace();

    int[] dest = new int[2];
    if (dir == 0) {
      dest[0] = orig[0] - 1;
      dest[1] = orig[1];
    } else if (dir == 1) {
      dest[0] = orig[0] + 1;
      dest[1] = orig[1];
    } else if (dir == 2) {
      dest[0] = orig[0];
      dest[1] = orig[1] - 1;
    } else if (dir == 3) {
      dest[0] = orig[0];
      dest[1] = orig[1] + 1;
    }

    if (dest[0] < 0 || dest[0] >= n || dest[1] < 0 || dest[1] >= n) {
      return null;
    }

    int[][] tilesCopy = new int[n][];
    for (int k = 0; k < n; k++) {
      tilesCopy[k] = tiles[k].clone();
    }
    tilesCopy[orig[0]][orig[1]] = tilesCopy[dest[0]][dest[1]];
    tilesCopy[dest[0]][dest[1]] = 0;

    return new Board(tilesCopy);
  }

  // all neighboring boards
  public Iterable<Board> neighbors() {

    ArrayList<Board> neighborsBoards = new ArrayList<>();
    for (int dir = 0; dir < 4; dir++) {
      Board move = move(dir);
      if (move != null) {
        neighborsBoards.add(move);
      }
    }

    return neighborsBoards;
  }

  // a board that is obtained by exchanging any pair of tiles
  public Board twin() {
    if (twin != null) {
      return twin;
    }

    int i, j;
    do {
      i = StdRandom.uniform(n);
      j = StdRandom.uniform(n);
    } while (tiles[i][j] == 0);

    ArrayList<Integer[]> neighbors = new ArrayList<>();
    neighbors.add(new Integer[] { i - 1, j, getTile(i - 1, j) });
    neighbors.add(new Integer[] { i + 1, j, getTile(i + 1, j) });
    neighbors.add(new Integer[] { i, j - 1, getTile(i, j - 1) });
    neighbors.add(new Integer[] { i, j + 1, getTile(i, j + 1) });

    ArrayList<Integer[]> neighborsCompact = new ArrayList<>();
    for (Integer[] neighbor : neighbors) {
      if (neighbor[2] > 0) {
        neighborsCompact.add(neighbor);
      }
    }

    int r = StdRandom.uniform(neighborsCompact.size());
    Integer[] neighbor = neighborsCompact.get(r);

    int[][] tilesCopy = new int[n][];
    for (int k = 0; k < n; k++) {
      tilesCopy[k] = tiles[k].clone();
    }
    tilesCopy[neighbor[0]][neighbor[1]] = tilesCopy[i][j];
    tilesCopy[i][j] = neighbor[2];

    twin = new Board(tilesCopy);
    return twin;
  }

  private int getTile(int i, int j) {
    if (i < 0 || i >= n || j < 0 || j >= n) {
      return -1;
    } else {
      return tiles[i][j];
    }
  }

  // unit testing (not graded)
  public static void main(String[] args) {
    Board b = new Board(new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } });
    System.out.println(b);

    Board twin = b.twin();
    System.out.println("twin=" + twin);

    for (Board neighbor : b.neighbors()) {
      System.out.println("neighbor:" + neighbor);
    }

    System.out.println("dimension=" + b.dimension());
    System.out.println("humming=" + b.hamming());
    System.out.println("manhattan=" + b.manhattan());

    Board bb = new Board(new int[][] { { 8, 3, 1 }, { 4, 0, 2 }, { 7, 6, 5 } });
    System.out.println("b=bb? " + b.equals(bb));

  }

}

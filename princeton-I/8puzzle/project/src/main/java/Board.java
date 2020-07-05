import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.StdRandom;

public class Board {

  private final int[][] tiles;
  private int hamming = -1;
  private int manhattan = -1;
  // private int[] twinOrig = null;
  // private int[] twinDest = null;
  private Board twin;

  // create a board from an n-by-n array of tiles,
  // where tiles[row][col] = tile at (row, col)
  public Board(int[][] tiles) {
    if (tiles == null) {
      throw new IllegalArgumentException();
    }
    this.tiles = tiles;
  }

  // string representation of this board
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(tiles.length).append("\n");

    int n = dimension();
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
    return tiles.length;
  }

  // number of tiles out of place
  public int hamming() {
    if (hamming < 0) {
      hamming = 0;
      int n = dimension();
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

      int n = dimension();
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
    return Arrays.deepEquals(this.tiles, that.tiles);
  }

  private int[] getEmptyPos() {
    int n = dimension();
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (tiles[i][j] == 0) {
          return new int[] { i, j };
        }
      }
    }
    throw new IllegalArgumentException();
  }

  private int[] getPosAfterMove(int[] orig, int dir) {
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

    return dest;
  }

  private List<int[]> getPossibleMoves(int[] orig) {
    ArrayList<int[]> moves = new ArrayList<>();

    for (int dir = 0; dir < 4; dir++) {
      int[] dest = getPosAfterMove(orig, dir);
      if (isPosValid(dest)) {
        moves.add(dest);
      }
    }

    return moves;
  }

  private boolean isPosValid(int[] pos) {
    int n = dimension();
    return pos[0] >= 0 && pos[0] < n && pos[1] >= 0 && pos[1] < n;
  }

  private Board getBoardAfterMove(int[] dest) {
    int[] orig = getEmptyPos();
    int[][] tilesCopy = getTilesAfterMove(orig, dest);
    return new Board(tilesCopy);
  }

  private int[][] getTilesAfterMove(int[] orig, int[] dest) {
    int[][] tilesCopy = cloneTiles();
    int origValue = tilesCopy[orig[0]][orig[1]];
    int destValue = tilesCopy[dest[0]][dest[1]];
    tilesCopy[orig[0]][orig[1]] = destValue;
    tilesCopy[dest[0]][dest[1]] = origValue;
    return tilesCopy;
  }

  private int[][] cloneTiles() {
    int n = dimension();
    int[][] tilesCopy = new int[n][];
    for (int k = 0; k < n; k++) {
      tilesCopy[k] = tiles[k].clone();
    }
    return tilesCopy;
  }

  // all neighboring boards
  public Iterable<Board> neighbors() {

    int[] orig = getEmptyPos();
    List<int[]> moves = getPossibleMoves(orig);
    ArrayList<Board> neighborsBoards = new ArrayList<>();
    for (int[] move : moves) {
      Board board = getBoardAfterMove(move);
      neighborsBoards.add(board);
    }

    return neighborsBoards;
  }

  // a board that is obtained by exchanging any pair of tiles
  public Board twin() {
    if (twin != null) {
      return twin;
    }

    int n = dimension();
    int i, j;
    do {
      i = StdRandom.uniform(n);
      j = StdRandom.uniform(n);
    } while (tiles[i][j] == 0);
    int[] twinOrig = new int[] { i, j };

    List<int[]> swaps = getPossibleMoves(twinOrig);
    int r = StdRandom.uniform(swaps.size());
    int[] twinDest = swaps.get(r);

    int[][] tilesCopy = getTilesAfterMove(twinOrig, twinDest);
    twin = new Board(tilesCopy);
    return twin;
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

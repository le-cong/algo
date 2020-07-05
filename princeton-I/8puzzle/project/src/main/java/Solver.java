import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class Solver {

  private Node solution;

  private class Node implements Comparable<Node> {

    final Board board;
    final Node prev;
    final int moves;

    Node(Board self, Node prev) {
      this.board = self;
      this.prev = prev;
      this.moves = prev == null ? 0 : prev.moves + 1;
    }

    @Override
    public int compareTo(Node that) {
      return (this.board.manhattan() + this.moves) - (that.board.manhattan() + that.moves);
    }

  }

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {

    if (initial == null) {
      throw new IllegalArgumentException();
    }

    MinPQ<Node> pq = new MinPQ<>();
    pq.insert(new Node(initial, null));

    MinPQ<Node> pqTwin = new MinPQ<>();
    pqTwin.insert(new Node(initial.twin(), null));

    Node min = pq.delMin();
    Node minTwin = pqTwin.delMin();
    while (!min.board.isGoal() && !minTwin.board.isGoal()) {
      for (Board board : min.board.neighbors()) {
        if (min.prev == null || !board.equals(min.prev.board)) {
          Node node = new Node(board, min);
          pq.insert(node);
        }
      }
      min = pq.delMin();
      // Twin
      for (Board board : minTwin.board.neighbors()) {
        if (minTwin.prev == null || !board.equals(minTwin.prev.board)) {
          Node node = new Node(board, minTwin);
          pqTwin.insert(node);
        }
      }
      minTwin = pqTwin.delMin();
    }

    if (min.board.isGoal()) {
      solution = min;
    }
  }

  // is the initial board solvable? (see below)
  public boolean isSolvable() {
    return solution != null;
  }

  // min number of moves to solve initial board; -1 if unsolvable
  public int moves() {
    if (!isSolvable()) {
      return -1;
    }

    return solution.moves;
  }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution() {
    if (!isSolvable()) {
      return null;
    }

    Stack<Board> st = new Stack<>();
    st.push(solution.board);

    Node curr = solution.prev;
    while (curr != null) {
      st.push(curr.board);
      curr = curr.prev;
    }
    return st;
  }

  // test client (see below)
  public static void main(String[] args) {
    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);

    // solve the puzzle
    Stopwatch watch = new Stopwatch();
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
      StdOut.println("No solution possible");
    else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution())
        StdOut.println(board);
    }
    StdOut.printf("took %f seconds. \n", watch.elapsedTime());
  }

}

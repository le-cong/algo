import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
  public static void main(String[] args) {
    int n=new Integer(args[0]);
    String [] items = StdIn.readAllStrings();
    RandomizedQueue<String> ranq = new RandomizedQueue<>();
    for(String s: items) {
      ranq.enqueue(s);
    }
    for(int i=0;i<n; i++) {
      if (ranq.isEmpty()) {
        break;
      }
      String item = ranq.dequeue();
      StdOut.println(item);
    }
  }
}

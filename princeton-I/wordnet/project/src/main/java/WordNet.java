import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

public class WordNet {

  private Map<Integer, String> dict = new HashMap<>();
  private Map<String, Integer> dictName2Id = new HashMap<>();
  private Digraph graph;
  private int root = -1;

  // constructor takes the name of the two input files
  public WordNet(String synsets, String hypernyms) {
    if (synsets == null || hypernyms == null) {
      throw new IllegalArgumentException();
    }

    In synsetsInput = new In(synsets);
    // int cnt=0;
    while (!synsetsInput.isEmpty()) {
      String line = synsetsInput.readLine();
      String[] lineSplits = line.split(",");
      dict.put(Integer.valueOf(lineSplits[0]), lineSplits[1]);
      dictName2Id.put(lineSplits[1], Integer.valueOf(lineSplits[0]));
      // cnt++;
    }
    // System.out.println("size of synsets="+cnt);
    // System.out.println("size of dict="+dict.size());

    In hypernymsInput = new In(hypernyms);
    graph = new Digraph(dict.size());
    while (!hypernymsInput.isEmpty()) {
      String line = hypernymsInput.readLine();
      String[] lineSplits = line.split(",");
      if (lineSplits.length < 2) {
        continue;
      }
      int hypo = Integer.parseInt(lineSplits[0]);
      for (int i = 1; i < lineSplits.length; i++) {
        int hyper = Integer.parseInt(lineSplits[i]);
        graph.addEdge(hypo, hyper);
      }
    }
    // System.out.println("size of Edges=" + graph.E());
    // System.out.println("size of Vertices=" + graph.V());

    // check root
    for (int v : dict.keySet()) {
      if (graph.outdegree(v) == 0) {
        if (root < 0) {
          root = v;
        } else {
          throw new IllegalArgumentException("invalid - multiple roots");
        }
      }
    }

    // check cycle
    Set<Integer> hist = new HashSet<>();
    for (int v : dict.keySet()) {
      if (!hist.contains(v)) {
        checkCycle(v, v, hist);
      }
    }

  }

  private void checkCycle(int v, int origin, Set<Integer> hist) {
    // System.out.println("checking " + v + " in " + origin);
    if (hist.contains(v)) {
      if (v == origin) {
        throw new IllegalArgumentException("invalid - cycle");
      } else {
        // skip already tested vertices
      }
    } else {
      hist.add(v);
      // System.out.println("save " + v + " in " + origin);
      for (int p : graph.adj(v)) {
        checkCycle(p, origin, hist);
      }
    }
  }

  // returns all WordNet nouns
  public Iterable<String> nouns() {
    return dict.values();
  }

  // is the word a WordNet noun?
  public boolean isNoun(String word) {
    return dict.containsValue(word);
  }

  // distance between nounA and nounB (defined below)
  public int distance(String nounA, String nounB) {
    if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) {
      throw new IllegalArgumentException();
    }

    int v = dictName2Id.get(nounA);
    int w = dictName2Id.get(nounB);
    Map<Integer, Integer> hist = new HashMap<>();
    Queue<Integer> q = new Queue<>();
    q.enqueue(v);
    hist.put(v, 0);

    int distance = -1;
    while (!q.isEmpty()) {
      int u = q.dequeue();
      int d = hist.get(u);
      for (int adj : graph.adj(u)) {
        if (adj == w) {
          distance = d + 1;
          break;
        } else {
          q.enqueue(adj);
          hist.put(adj, d + 1);
        }
      }
    }
    return distance;
  }

  // a synset (second field of synsets.txt) that is the common ancestor of nounA
  // and nounB
  // in a shortest ancestral path (defined below)
  public String sap(String nounA, String nounB) {
    if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) {
      throw new IllegalArgumentException();
    }

    return null;
  }

  // do unit testing of this class
  public static void main(String[] args) {
    // WordNet wn = new WordNet("synsets6.txt", "hypernyms6TwoAncestors.txt");
    WordNet wn = new WordNet("synsets3.txt", "hypernyms3InvalidCycle.txt");

  }
}
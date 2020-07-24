import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet {

  private Map<Integer, String> dict = new HashMap<>();
  private Map<String, Integer> dictName2Id = new HashMap<>();
  private Digraph graph;
  private int root = -1;
  private SAP sap;

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
      Integer synsetId = Integer.valueOf(lineSplits[0]);
      String synsetContents = lineSplits[1];
      dict.put(synsetId, synsetContents);
      String[] nouns = synsetContents.split(" ");
      for (String noun : nouns) {
        dictName2Id.put(noun, synsetId);
      }
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

    sap = new SAP(graph);
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
    return dictName2Id.keySet();
  }

  // is the word a WordNet noun?
  public boolean isNoun(String word) {
    return dictName2Id.containsKey(word);
  }

  // distance between nounA and nounB (defined below)
  public int distance(String nounA, String nounB) {
    if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) {
      throw new IllegalArgumentException();
    }

    int v = dictName2Id.get(nounA);
    int w = dictName2Id.get(nounB);

    return sap.length(v, w);
  }

  // a synset (second field of synsets.txt) that is the common ancestor of nounA
  // and nounB
  // in a shortest ancestral path (defined below)
  public String sap(String nounA, String nounB) {
    if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) {
      throw new IllegalArgumentException();
    }

    int v = dictName2Id.get(nounA);
    int w = dictName2Id.get(nounB);

    int ancestor = sap.ancestor(v, w);
    return dict.get(ancestor);
  }

  // do unit testing of this class
  public static void main(String[] args) {
    // String synsetsFilename = "synsets.txt";
    // String hypernymsFilename = "hypernyms.txt";
    // String nounA = "dictator";
    // String nounB = "permission";
    String synsetsFilename = "synsets100-subgraph.txt";
    String hypernymsFilename = "hypernyms100-subgraph.txt";
    String nounA = "human_gamma_globulin";
    String nounB = "immunoglobulin_D";

    System.out.println("============== Params ================");
    System.out.println("synsetsFilename=" + synsetsFilename);
    System.out.println("hypernymsFilename=" + hypernymsFilename);
    System.out.println("nounA=" + nounA);
    System.out.println("nounB=" + nounB);
    System.out.println("============== Results ================");
    WordNet wn = new WordNet(synsetsFilename, hypernymsFilename);
    System.out.println("distance=" + wn.distance(nounA, nounB));
    System.out.println("sap=" + wn.sap(nounA, nounB));
    // WordNet wn = new WordNet("synsets15.txt", "hypernyms15Path.txt");
    // System.out.println("distance="+wn.distance("b", "a"));
    // System.out.println("sap="+wn.sap("b", "a"));

  }
}
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

  private WordNet wordnet;

  // constructor takes a WordNet object
  public Outcast(WordNet wordnet) {
    if (wordnet == null) {
      throw new IllegalArgumentException();
    }
    this.wordnet = wordnet;
  }

  // given an array of WordNet nouns, return an outcast
  public String outcast(String[] nouns) {
    if (nouns == null) {
      throw new IllegalArgumentException();
    }

    int maxDist = -1;
    String maxNoun = null;
    for (String nounA : nouns) {
      int dist = 0;
      for (String nounB : nouns) {
        dist += wordnet.distance(nounA, nounB);
      }
      if (dist > maxDist) {
        maxDist = dist;
        maxNoun = nounA;
      }
    }
    return maxNoun;
  }

  // see test client below
  public static void main(String[] args) {
    String synsetsFilename = "synsets.txt";
    String hypernymsFilename = "hypernyms.txt";
    WordNet wordnet = new WordNet(synsetsFilename, hypernymsFilename);
    Outcast outcast = new Outcast(wordnet);
    String[] nouns = new String[] { "Turing", "von_Neumann", "Mickey_Mouse" };
    StdOut.println(outcast.outcast(nouns));
  }
}

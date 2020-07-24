import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

  private Digraph g;

  // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph G) {
    this.g = G;
  }

  // length of shortest ancestral path between v and w; -1 if no such path
  public int length(int v, int w) {
    int[] sap = findSap(v, w);
    return sap[1];
  }

  // a common ancestor of v and w that participates in a shortest ancestral path;
  // -1 if no such path
  public int ancestor(int v, int w) {
    int[] sap = findSap(v, w);
    return sap[0];
  }

  private int[] findSap(int v, int w) {
    Map<Integer, Visit> visitsV = bfs(v);
    Map<Integer, Visit> visitsW = bfs(w);
    Set<Integer> ancestors = visitsV.keySet();
    ancestors.retainAll(visitsW.keySet());

    int minAncestor = -1;
    int minDist = -1;
    for (int ancestor : ancestors) {
      int dist = visitsV.get(ancestor).dist + visitsW.get(ancestor).dist;
      if (dist < minDist || minDist < 0) {
        minAncestor = ancestor;
        minDist = dist;
      }
    }
    return new int[] { minAncestor, minDist };
  }

  private class Visit {
    int vertex;
    int pathFrom;
    int dist;

    public Visit(int vertex, int pathFrom, int dist) {
      this.vertex = vertex;
      this.pathFrom = pathFrom;
      this.dist = dist;
    }
  }

  private Map<Integer, Visit> bfs(int u) {
    Map<Integer, Visit> visits = new HashMap<>();
    Queue<Visit> q = new Queue<>();

    Visit visit = new Visit(u, u, 0);
    visits.put(u, visit);
    q.enqueue(visit);
    while (!q.isEmpty()) {
      Visit v = q.dequeue();
      for (int adj : g.adj(v.vertex)) {
        if (!visits.containsKey(adj)) {
          Visit adjVisit = new Visit(adj, v.vertex, v.dist + 1);
          visits.put(adj, adjVisit);
          q.enqueue(adjVisit);
        }
      }
    }

    return visits;
  }

  // length of shortest ancestral path between any vertex in v and any vertex in
  // w; -1 if no such path
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    int[] sapForGroups = findSapForGroups(v, w);
    return sapForGroups[1];
  }

  // a common ancestor that participates in shortest ancestral path; -1 if no such
  // path
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    int[] sapForGroups = findSapForGroups(v, w);
    return sapForGroups[0];
  }

  private int[] findSapForGroups(Iterable<Integer> v, Iterable<Integer> w) {

    int minAncestor = -1;
    int minDist = -1;
    for (int vertexV : v) {
      for (int vertexW : w) {
        int[] sap = findSap(vertexV, vertexW);

        int dist = sap[1];
        if (dist < minDist || minDist < 0) {
          minAncestor = sap[0];
          minDist = dist;
        }
      }
    }
    return new int[] { minAncestor, minDist };

  }

  // do unit testing of this class
  public static void main(String[] args) {
    In in = new In("digraph1.txt");
    // In in = new In(args[0]);
    Digraph G = new Digraph(in);
    SAP sap = new SAP(G);
    while (!StdIn.isEmpty()) {
      int v = StdIn.readInt();
      int w = StdIn.readInt();
      int length = sap.length(v, w);
      int ancestor = sap.ancestor(v, w);
      StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
  }
}

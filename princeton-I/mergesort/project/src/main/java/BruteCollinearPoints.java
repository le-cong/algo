import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

  private ArrayList<LineSegment> lineSegments;

  private class Line {
    Point start;
    Point end;

    Line(Point start, Point end) {
      this.start = start;
      this.end = end;
    }

    LineSegment toLineSegment() {
      return new LineSegment(start, end);
    }

    @Override
    public boolean equals(Object obj) {
      Line that = (Line) obj;
      return this.start.compareTo(that.start) == 0 && this.end.compareTo(that.end) == 0;
    }
  }

  // finds all line segments containing 4 points
  public BruteCollinearPoints(Point[] points) {

    init(points);

    int n = points.length;
    if (n < 4) {
      return;
    }

    ArrayList<Line> lines = new ArrayList<>();

    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        double sij = points[i].slopeTo(points[j]);
        for (int k = j + 1; k < n; k++) {
          double sjk = points[j].slopeTo(points[k]);
          for (int l = k + 1; l < n; l++) {
            double skl = points[k].slopeTo(points[l]);
            if (sij == sjk && sjk == skl) {
              Point[] pGroup = new Point[] { points[i], points[j], points[k], points[l] };
              Arrays.sort(pGroup);
              Line line = new Line(pGroup[0], pGroup[3]);
              if (!lines.contains(line)) {
                lines.add(line);
                lineSegments.add(line.toLineSegment());
              }
            }
          }
        }
      }
    }
  }

  private void init(Point[] points) {
    if (null == points) {
      throw new IllegalArgumentException();
    }

    int n = points.length;
    for (int i = 0; i < n; i++) {
      if (points[i] == null) {
        throw new IllegalArgumentException();
      }
    }

    lineSegments = new ArrayList<>();

    Point[] pp = Arrays.copyOf(points, n);
    Arrays.sort(pp);
    for (int i = 1; i < n; i++) {
      if (pp[i].compareTo(pp[i - 1]) == 0) {
        throw new IllegalArgumentException();
      }
    }
  }

  // the number of line segments
  public int numberOfSegments() {
    return lineSegments.size();
  }

  // the line segments
  public LineSegment[] segments() {
    LineSegment[] ret = new LineSegment[numberOfSegments()];
    lineSegments.toArray(ret);
    return ret;
  }

  public static void main(String[] args) {

    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
      p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }

}

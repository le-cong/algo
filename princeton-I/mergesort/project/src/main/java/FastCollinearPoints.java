import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

  private ArrayList<LineSegment> lineSegments = new ArrayList<>();

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
  public FastCollinearPoints(Point[] points) {

    init(points);

    int n = points.length;
    if (n < 4) {
      return;
    }

    Point[] pp = Arrays.copyOf(points, n);
    ArrayList<Line> lines = new ArrayList<>();

    for (int i = 0; i < n; i++) {
      Point origin = points[i];
      Arrays.sort(pp, origin.slopeOrder());
      // StdOut.println("================= " + i);
      // StdOut.println(Arrays.toString(pp));

      double lineSlope = pp[0].slopeTo(origin);
      int lineStart = 0;
      int lineEnd = -1;
      for (int j = 1; j < n; j++) {
        double curSlope = pp[j].slopeTo(origin);
        if (curSlope != lineSlope) {
          lineEnd = j - 1;
          lineSlope = curSlope;
        } else if (j == n - 1) {
          lineEnd = j;
        }

        if (lineEnd >= 0) {
          int pGroupSize = lineEnd - lineStart + 2;
          // StdOut.printf("start: %d, end: %d \n", lineStart, lineEnd);
          if (pGroupSize >= 4) {
            Point[] pGroup = new Point[pGroupSize];
            pGroup[0] = origin;
            for (int k = 1; k < pGroupSize; k++) {
              pGroup[k] = pp[k + lineStart - 1];
            }
            Arrays.sort(pGroup);
            Line line = new Line(pGroup[0], pGroup[pGroupSize - 1]);
            if (!lines.contains(line)) {
              lines.add(line);
              lineSegments.add(line.toLineSegment());
            }
          }
          lineStart = j;
          lineEnd = -1;
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
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }

}

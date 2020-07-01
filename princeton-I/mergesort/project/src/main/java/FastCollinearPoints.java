import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

  private ArrayList<LineSegment> lines = new ArrayList<>();

  // finds all line segments containing 4 points
  public FastCollinearPoints(Point[] points) {
    if (null == points) {
      throw new IllegalArgumentException();
    }

    int n = points.length;
    Arrays.sort(points);
    for (int i = 0; i < n; i++) {
      if (points[i] == null) {
        throw new IllegalArgumentException();
      }
      if (i > 0 && points[i].compareTo(points[i - 1]) == 0) {
        throw new IllegalArgumentException();
      }
    }

    if (n < 4) {
      return;
    }

    Point[] pp = Arrays.copyOf(points, n);
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
          int pGroupSize = lineEnd - lineStart + 1;
          // StdOut.printf("start: %d, end: %d \n", lineStart, lineEnd);
          if (pGroupSize >= 3) {
            Point[] pGroup = new Point[pGroupSize + 1];
            pGroup[0] = origin;
            for (int k = 1; k <= pGroupSize; k++) {
              pGroup[k] = pp[k + lineStart - 1];
            }
            Arrays.sort(pGroup);
            LineSegment ls = new LineSegment(pGroup[0], pGroup[pGroup.length - 1]);
            boolean isDup = false;
            for (LineSegment existingLine : lines) {
              if (existingLine.toString().equals(ls.toString())) {
                isDup = true;
                break;
              }
            }
            if (!isDup) {
              lines.add(ls);
            }
          }
          lineStart = j;
          lineEnd = -1;
        }
      }
    }
  }

  // the number of line segments
  public int numberOfSegments() {
    return lines.size();
  }

  // the line segments
  public LineSegment[] segments() {
    LineSegment[] ret = new LineSegment[lines.size()];
    return lines.toArray(ret);
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class FastCollinearPoints {

  private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

  private class Line {
    Point start;
    Point end;

    Line(final Point start, final Point end) {
      this.start = start;
      this.end = end;
    }

    LineSegment toLineSegment() {
      return new LineSegment(start, end);
    }

    @Override
    public boolean equals(final Object obj) {
      final Line that = (Line) obj;
      return this.start == that.start && this.end == that.end;
    }
  }

  private class LineComparator implements Comparator<Line> {

    @Override
    public int compare(final Line l1, final Line l2) {
      int ret = l1.start.compareTo(l2.start);
      if (ret == 0) {
        ret = l1.end.compareTo(l2.end);
      }
      return ret;
    }

  }

  // finds all line segments containing 4 points
  public FastCollinearPoints(final Point[] points) {

    init(points);

    final int n = points.length;
    if (n < 4) {
      return;
    }

    final Point[] pp = Arrays.copyOf(points, n);
    final ArrayList<Line> lines = new ArrayList<>();

    final Stopwatch watch = new Stopwatch();

    for (int i = 0; i < n; i++) {
      final Point origin = points[i];
      Arrays.sort(pp, origin.slopeOrder());
      // StdOut.println("================= " + i);
      // StdOut.println(Arrays.toString(pp));

      double lineSlope = pp[0].slopeTo(origin);
      int lineStart = 0;
      int lineEnd = -1;
      Point pointMin = origin;
      Point pointMax = origin;
      for (int j = 1; j < n; j++) {
        final double curSlope = pp[j].slopeTo(origin);
        if (curSlope != lineSlope) {
          lineEnd = j - 1;
          lineSlope = curSlope;
        } else {
          if (j == n - 1) {
            lineEnd = j;
          }
          if (pp[j].compareTo(pointMin) < 0) {
            pointMin = pp[j];
          } else if (pp[j].compareTo(pointMax) > 0) {
            pointMax = pp[j];
          }
        }

        if (lineEnd >= 0) {
          final int pGroupSize = lineEnd - lineStart + 2;
          // StdOut.printf("start: %d, end: %d \n", lineStart, lineEnd);
          if (pGroupSize >= 4) {
            final Line line = new Line(pointMin, pointMax);
            lines.add(line);
          }
          lineStart = j;
          lineEnd = -1;
          if (origin.compareTo(pp[j]) < 0) {
            pointMin = origin;
            pointMax = pp[j];
          } else {
            pointMin = pp[j];
            pointMax = origin;
          }
        }
      }
    }

    // System.out.printf("took %f seconds to get all lines w/ dup. \n", watch.elapsedTime());
    // System.out.println("num of lines including dups: " + lines.size());
    Line[] lineArray = new Line[lines.size()];
    lines.toArray(lineArray);
    Arrays.sort(lineArray, new LineComparator());
    Line curLine = null;
    for (final Line line : lineArray) {
      if (curLine == null || !line.equals(curLine)) {
        lineSegments.add(line.toLineSegment());
        curLine = line;
      }
    }
    // System.out.printf("took %f seconds to clean and get lines with no dup. \n", watch.elapsedTime());
  }

  private void init(final Point[] points) {
    if (null == points) {
      throw new IllegalArgumentException();
    }

    final int n = points.length;
    for (int i = 0; i < n; i++) {
      if (points[i] == null) {
        throw new IllegalArgumentException();
      }
    }

    final Point[] pp = Arrays.copyOf(points, n);
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
    final LineSegment[] ret = new LineSegment[numberOfSegments()];
    lineSegments.toArray(ret);
    return ret;
  }

  public static void main(final String[] args) {

    // read the n points from a file
    final In in = new In(args[0]);
    final int n = in.readInt();
    final Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      final int x = in.readInt();
      final int y = in.readInt();
      points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (final Point p : points) {
      p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    final FastCollinearPoints collinear = new FastCollinearPoints(points);
    for (final LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }

}

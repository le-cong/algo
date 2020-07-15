import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {

  private final SET<Point2D> set;

  // construct an empty set of points
  public PointSET() {
    set = new SET<>();
  }

  // is the set empty?
  public boolean isEmpty() {
    return set.isEmpty();
  }

  // number of points in the set
  public int size() {
    return set.size();
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }

    set.add(p);
  }

  // does the set contain point p?
  public boolean contains(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }

    return set.contains(p);
  }

  // draw all points to standard draw
  public void draw() {
    for (Point2D p : set) {
      p.draw();
    }
  }

  // all points that are inside the rectangle (or on the boundary)
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) {
      throw new IllegalArgumentException();
    }

    ArrayList<Point2D> inRange = new ArrayList<>();
    for (Point2D p : set) {
      if (rect.contains(p)) {
        inRange.add(p);
      }
    }
    return inRange;
  }

  // a nearest neighbor in the set to point p; null if the set is empty
  public Point2D nearest(Point2D query) {
    if (query == null) {
      throw new IllegalArgumentException();
    }

    Point2D nearest = null;
    double nearestDistance = Double.POSITIVE_INFINITY;
    for (Point2D p : set) {
      double distance = query.distanceSquaredTo(p);
      if (distance < nearestDistance) {
        nearest = p;
        nearestDistance = distance;
      }
    }

    return nearest;
  }

  // unit testing of the methods (optional)
  public static void main(String[] args) {
    PointSET set = new PointSET();
    set.insert(new Point2D(1.0, 1.0));
    set.insert(new Point2D(1.0, 0.0));
    set.insert(new Point2D(0.0, 1.0));
    Point2D nearest = set.nearest(new Point2D(1.0, 0.0));
    System.out.println(nearest);
  }
}

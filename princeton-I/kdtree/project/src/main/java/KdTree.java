import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

  private class Node {
    Point2D p;
    Node left;
    Node right;
  }

  private class NearestTest {
    Point2D p;
    Point2D query;
    double distance;
  }

  private Node root = null;
  private int size = 0;

  // construct an empty set of points
  public KdTree() {
  }

  // is the set empty?
  public boolean isEmpty() {
    return size == 0;
  }

  // number of points in the set
  public int size() {
    return size;
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }

    root = insert(root, p, true);
  }

  private Node insert(Node curr, Point2D p, boolean useX) {
    if (curr == null) {
      curr = new Node();
      curr.p = p;
      size++;
      return curr;
    }

    if (p.equals(curr.p)) {
      return curr;
    }

    if (useX) {
      if (p.x() < curr.p.x()) {
        curr.left = insert(curr.left, p, !useX);
      } else {
        curr.right = insert(curr.right, p, !useX);
      }
    } else {
      if (p.y() < curr.p.y()) {
        curr.left = insert(curr.left, p, !useX);
      } else {
        curr.right = insert(curr.right, p, !useX);
      }
    }

    return curr;
  }

  // does the set contain point p?
  public boolean contains(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }

    if (isEmpty()) {
      return false;
    }

    return contains(root, p, true);
  }

  private boolean contains(Node curr, Point2D p, boolean useX) {
    if (curr == null) {
      return false;
    }

    if (curr.p.equals(p)) {
      return true;
    }

    if (useX) {
      if (p.x() < curr.p.x()) {
        return contains(curr.left, p, !useX);
      } else {
        return contains(curr.right, p, !useX);
      }
    } else {
      if (p.y() < curr.p.y()) {
        return contains(curr.left, p, !useX);
      } else {
        return contains(curr.right, p, !useX);
      }
    }
  }

  // draw all points to standard draw
  public void draw() {
    StdDraw.rectangle(0.5, 0.5, 0.5, 0.5);
    RectHV rect = new RectHV(0, 0, 1, 1);
    draw(root, true, rect);
  }

  private void draw(Node curr, boolean useX, RectHV container) {
    if (curr == null) {
      return;
    }

    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(0.01);
    StdDraw.point(curr.p.x(), curr.p.y());
    if (useX) {
      StdDraw.setPenColor(StdDraw.RED);
      StdDraw.setPenRadius();
      StdDraw.line(curr.p.x(), container.ymin(), curr.p.x(), container.ymax());
    } else {
      StdDraw.setPenColor(StdDraw.BLUE);
      StdDraw.setPenRadius();
      StdDraw.line(container.xmin(), curr.p.y(), container.xmax(), curr.p.y());
    }
    draw(curr.left, !useX, getContainer(curr, useX, true, container));
    draw(curr.right, !useX, getContainer(curr, useX, false, container));
  }

  // private Iterator<Node> iterator() {
  // }

  // all points that are inside the rectangle (or on the boundary)
  public Iterable<Point2D> range(RectHV query) {
    if (query == null) {
      throw new IllegalArgumentException();
    }

    ArrayList<Point2D> inRange = new ArrayList<>();
    RectHV container = new RectHV(0, 0, 1, 1);
    range(root, true, container, query, inRange);
    return inRange;
  }

  private void range(Node curr, boolean useX, RectHV container, RectHV query, ArrayList<Point2D> inRange) {
    if (curr == null) {
      return;
    }

    if (query.contains(curr.p)) {
      inRange.add(curr.p);
    }

    if (curr.left != null) {
      RectHV leftContainer = getContainer(curr, useX, true, container);
      if (query.intersects(leftContainer)) {
        range(curr.left, !useX, leftContainer, query, inRange);
      }
    }

    if (curr.right != null) {
      RectHV rightContainer = getContainer(curr, useX, false, container);
      if (query.intersects(rightContainer)) {
        range(curr.right, !useX, rightContainer, query, inRange);
      }
    }
  }

  private RectHV getContainer(Node curr, boolean useX, boolean isLeft, RectHV parentContainer) {
    RectHV container;
    if (useX) {
      if (isLeft) {
        container = new RectHV(parentContainer.xmin(), parentContainer.ymin(), curr.p.x(), parentContainer.ymax());
      } else {
        container = new RectHV(curr.p.x(), parentContainer.ymin(), parentContainer.xmax(), parentContainer.ymax());
      }
    } else {
      if (isLeft) {
        container = new RectHV(parentContainer.xmin(), parentContainer.ymin(), parentContainer.xmax(), curr.p.y());
      } else {
        container = new RectHV(parentContainer.xmin(), curr.p.y(), parentContainer.xmax(), parentContainer.ymax());
      }
    }

    return container;
  }

  // a nearest neighbor in the set to point p; null if the set is empty
  public Point2D nearest(Point2D query) {
    if (query == null) {
      throw new IllegalArgumentException();
    }

    if (isEmpty()) {
      return null;
    }

    NearestTest nearest = new NearestTest();
    nearest.query = query;
    nearest.p = null;
    nearest.distance = Double.POSITIVE_INFINITY;

    RectHV container = new RectHV(0, 0, 1, 1);

    nearest(root, true, container, nearest);
    return nearest.p;
  }

  private void nearest(Node curr, boolean useX, RectHV container, NearestTest nearest) {
    if (curr == null) {
      return;
    }

    // System.out.println("traversing: " + curr.p);

    double currDistance = curr.p.distanceSquaredTo(nearest.query);
    if (currDistance < nearest.distance) {
      nearest.p = curr.p;
      nearest.distance = currDistance;
    }

    boolean isLeftFirst = (useX && nearest.query.x() < curr.p.x()) || (!useX && nearest.query.y() < curr.p.y());
    if (isLeftFirst) {
      if (curr.left != null) {

        RectHV leftContainer = getContainer(curr, useX, true, container);
        double leftContainerDistance = leftContainer.distanceSquaredTo(nearest.query);
        // // DEBUG -- START
        // StdDraw.clear();
        // StdDraw.setPenColor(StdDraw.BLACK);
        // StdDraw.setPenRadius(0.02);
        // nearest.query.draw();
        // StdDraw.setPenColor(StdDraw.RED);
        // StdDraw.setPenRadius(0.02);
        // nearest.p.draw();
        // StdDraw.setPenColor(StdDraw.GREEN);
        // StdDraw.setPenRadius();
        // leftContainer.draw();
        // System.out.println("===============================");
        // System.out.println("leftContainerDistance=" + leftContainerDistance);
        // System.out.println("nearest.distance=" + nearest.distance);
        // // DEBUG -- END
        if (leftContainerDistance < nearest.distance) {
          nearest(curr.left, !useX, leftContainer, nearest);
        }
      }

      if (curr.right != null) {
        RectHV rightContainer = getContainer(curr, useX, false, container);
        double rightContainerDistance = rightContainer.distanceSquaredTo(nearest.query);
        // // DEBUG -- START
        // StdDraw.clear();
        // StdDraw.setPenColor(StdDraw.BLACK);
        // StdDraw.setPenRadius(0.02);
        // nearest.query.draw();
        // StdDraw.setPenColor(StdDraw.RED);
        // StdDraw.setPenRadius(0.02);
        // nearest.p.draw();
        // StdDraw.setPenColor(StdDraw.GREEN);
        // StdDraw.setPenRadius();
        // rightContainer.draw();
        // System.out.println("===============================");
        // System.out.println("rightContainerDistance=" + rightContainerDistance);
        // System.out.println("nearest.distance=" + nearest.distance);
        // // DEBUG -- END
        if (rightContainerDistance < nearest.distance) {
          nearest(curr.right, !useX, rightContainer, nearest);
        }
      }
    } else {

      if (curr.right != null) {
        RectHV rightContainer = getContainer(curr, useX, false, container);
        double rightContainerDistance = rightContainer.distanceSquaredTo(nearest.query);
        // // DEBUG -- START
        // StdDraw.clear();
        // StdDraw.setPenColor(StdDraw.BLACK);
        // StdDraw.setPenRadius(0.02);
        // nearest.query.draw();
        // StdDraw.setPenColor(StdDraw.RED);
        // StdDraw.setPenRadius(0.02);
        // nearest.p.draw();
        // StdDraw.setPenColor(StdDraw.GREEN);
        // StdDraw.setPenRadius();
        // rightContainer.draw();
        // System.out.println("===============================");
        // System.out.println("rightContainerDistance=" + rightContainerDistance);
        // System.out.println("nearest.distance=" + nearest.distance);
        // // DEBUG -- END
        if (rightContainerDistance < nearest.distance) {
          nearest(curr.right, !useX, rightContainer, nearest);
        }
      }

      if (curr.left != null) {

        RectHV leftContainer = getContainer(curr, useX, true, container);
        double leftContainerDistance = leftContainer.distanceSquaredTo(nearest.query);
        // // DEBUG -- START
        // StdDraw.clear();
        // StdDraw.setPenColor(StdDraw.BLACK);
        // StdDraw.setPenRadius(0.02);
        // nearest.query.draw();
        // StdDraw.setPenColor(StdDraw.RED);
        // StdDraw.setPenRadius(0.02);
        // nearest.p.draw();
        // StdDraw.setPenColor(StdDraw.GREEN);
        // StdDraw.setPenRadius();
        // leftContainer.draw();
        // System.out.println("===============================");
        // System.out.println("leftContainerDistance=" + leftContainerDistance);
        // System.out.println("nearest.distance=" + nearest.distance);
        // // DEBUG -- END
        if (leftContainerDistance < nearest.distance) {
          nearest(curr.left, !useX, leftContainer, nearest);
        }
      }
    }

  }

  // unit testing of the methods (optional)
  public static void main(String[] args) {
    KdTree set = new KdTree();
    set.insert(new Point2D(0.7, 0.2));
    set.insert(new Point2D(0.5, 0.4));
    set.insert(new Point2D(0.2, 0.3));
    set.insert(new Point2D(0.4, 0.7));
    set.insert(new Point2D(0.9, 0.6));
    set.draw();
    Point2D query = new Point2D(0.59, 0.63);
    query.draw();
    Point2D nearest = set.nearest(query);
    System.out.println("nearest(should be [0.4, 0.7]): " + nearest);
  }
}

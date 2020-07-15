import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

  private class Node {
    Point2D value;
    Node left;
    Node right;
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
      curr.value = p;
      size++;
      return curr;
    }

    if (useX) {
      if (p.x() < curr.value.x()) {
        curr.left = insert(curr.left, p, !useX);
      } else {
        curr.right = insert(curr.right, p, !useX);
      }
    } else {
      if (p.y() < curr.value.y()) {
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

    return false;
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
    StdDraw.point(curr.value.x(), curr.value.y());
    if (useX) {
      StdDraw.setPenColor(StdDraw.RED);
      StdDraw.setPenRadius();
      StdDraw.line(curr.value.x(), container.ymin(), curr.value.x(), container.ymax());
    } else {
      StdDraw.setPenColor(StdDraw.BLUE);
      StdDraw.setPenRadius();
      StdDraw.line(container.xmin(), curr.value.y(), container.xmax(), curr.value.y());
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

    if (query.contains(curr.value)) {
      inRange.add(curr.value);
    }

    RectHV leftContainer = getContainer(curr, useX, true, container);
    if (query.intersects(leftContainer)) {
      range(curr.left, !useX, leftContainer, query, inRange);
    }

    RectHV rightContainer = getContainer(curr, useX, false, container);
    if (query.intersects(rightContainer)) {
      range(curr.right, !useX, rightContainer, query, inRange);
    }
  }

  private RectHV getContainer(Node curr, boolean useX, boolean isLeft, RectHV parentContainer) {
    RectHV container;
    if (useX) {
      if (isLeft) {
        container = new RectHV(parentContainer.xmin(), parentContainer.ymin(), curr.value.x(), parentContainer.ymax());
      } else {
        container = new RectHV(curr.value.x(), parentContainer.ymin(), parentContainer.xmax(), parentContainer.ymax());
      }
    } else {
      if (isLeft) {
        container = new RectHV(parentContainer.xmin(), parentContainer.ymin(), parentContainer.xmax(), curr.value.y());
      } else {
        container = new RectHV(parentContainer.xmin(), curr.value.y(), parentContainer.xmax(), parentContainer.ymax());
      }
    }

    return container;
  }

  // a nearest neighbor in the set to point p; null if the set is empty
  public Point2D nearest(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }

    Point2D nearest = null;
    // double nearestDistance = Double.POSITIVE_INFINITY;
    // for (Point2D pTest : set) {
    // if (nearest != null) {
    // double distance = p.distanceSquaredTo(pTest);
    // if (distance < nearestDistance) {
    // nearest = pTest;
    // nearestDistance = distance;
    // }
    // } else {
    // nearest = pTest;
    // }
    // }

    return nearest;
  }

  // unit testing of the methods (optional)
  public static void main(String[] args) {
    System.out.println("hello");
  }
}

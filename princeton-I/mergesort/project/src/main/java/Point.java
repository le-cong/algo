
/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class Point implements Comparable<Point> {

    private final int x; // x-coordinate of this point
    private final int y; // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point to standard
     * draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point. Formally, if
     * the two points are (x0, y0) and (x1, y1), then the slope is (y1 - y0) / (x1 -
     * x0). For completeness, the slope is defined to be +0.0 if the line segment
     * connecting the two points is horizontal; Double.POSITIVE_INFINITY if the line
     * segment is vertical; and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1)
     * are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (this.compareTo(that) == 0) {
            return Double.NEGATIVE_INFINITY;
        } else if (this.x == that.x) {
            return Double.POSITIVE_INFINITY;
        } else {
            return 1.0 * (that.y - this.y) / (that.x - this.x);
        }
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate. Formally,
     * the invoking point (x0, y0) is less than the argument point (x1, y1) if and
     * only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument point (x0
     *         = x1 and y0 = y1); a negative integer if this point is less than the
     *         argument point; and a positive integer if this point is greater than
     *         the argument point
     */
    public int compareTo(Point that) {
        if (this.y == that.y) {
            return this.x - that.x;
        } else {
            return this.y - that.y;
        }
    }

    /**
     * Compares two points by the slope they make with this point. The slope is
     * defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new Comparator<Point>() {

            @Override
            public int compare(Point p1, Point p2) {
                double slop1 = slopeTo(p1);
                double slop2 = slopeTo(p2);
                if (slop1 < slop2) {
                    return -1;
                } else if (slop1 > slop2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
    }

    /**
     * Returns a string representation of this point. This method is provide for
     * debugging; your program should not rely on the format of the string
     * representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        Point p0 = new Point(10000, 0);
        p0.draw();
        Point p1 = new Point(0, 10000);
        p1.draw();
        p1.drawTo(p0);
        Point p2 = new Point(6000, 7000);
        p2.draw();
        p2.drawTo(p0);

        StdOut.printf("p0 is = %s \n ", p0);

        StdOut.printf("p0.slopeTo(p1)=%f \n ", p0.slopeTo(p1));
        StdOut.printf("p0.slopeTo(p2)=%f \n ", p0.slopeTo(p2));
        StdOut.printf("p0.compareTo(p0)=%d \n ", p0.compareTo(p0));
        StdOut.printf("p0.compareTo(p1)=%d \n ", p0.compareTo(p2));
        StdOut.printf("p2.compareTo(p1)=%d \n ", p2.compareTo(p1));
        Comparator<Point> c = p0.slopeOrder();
        StdOut.printf("compare p1 and p2 over p0=%d \n ", c.compare(p1, p2));

        // Point pa = new Point(10000,0);
        // pa.draw();
        // Point pb = new Point(0, 10000);
        // pb.draw();
        // pb.drawTo(pa);
        // Point pc = new Point(6000, 7000);
        // pc.draw();
    }
}
import java.util.TreeSet;
import java.util.LinkedList;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.In;

public class PointSET {
	private TreeSet<Point2D> points;
	// construct an empty set of points 
	public PointSET() {
		points = new TreeSet<Point2D>();
	}
	public boolean isEmpty() {
		return points.isEmpty();
	}
	// number of points in the set 
	public int size() {
		return points.size();
	}
	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		checkNull(p);
		points.add(p);
	}
	// does the set contain point p? 
	public boolean contains(Point2D p) {
		checkNull(p);
		return points.contains(p);
	}
	// draw all points to standard draw 
	public void draw() {
		for(Point2D point : points) 
			point.draw();
	}
	// all points that are inside the rectangle (or on the boundary) 
	public Iterable<Point2D> range(RectHV rect) {
		checkNull(rect);
		LinkedList<Point2D> inside = new LinkedList<Point2D>();
		for(Point2D point: points)
			if (rect.contains(point)) inside.add(point);
		return inside;
	}
	// a nearest neighbor in the set to point p; null if the set is empty 
	public Point2D nearest(Point2D p) {
		if (points.isEmpty()) return null;
		double minDistance = p.distanceSquaredTo(points.first());
		Point2D min = points.first();
		for(Point2D point : points) {
			if (p.distanceSquaredTo(point) < minDistance) {
				minDistance = p.distanceSquaredTo(point);
				min = point;
			}
		}
		return min;
	}
	private void checkNull(Object o) {
		if (o == null) throw new java.lang.IllegalArgumentException();
	}
	// unit testing of the methods (optional)
	public static void main(String[] args) {
		// initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        PointSET tree = new PointSET();
        //KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            //kdtree.insert(p);
            tree.insert(p);
        }
        int b = 10;
        double sum = 0;
        for (int j = 0; j < b; j++) {
	        int n = 100;
	        long start = System.nanoTime();
	        for (int i = 0; i < n; i++) {
	        	Point2D p = new Point2D(StdRandom.uniform(), StdRandom.uniform());
	        	tree.nearest(p);
	        }
	        sum += (double) n / (System.nanoTime() - start) * 1000000000;
        }
        System.out.format("%,.0f", sum / b);
	}
}
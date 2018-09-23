import java.util.LinkedList;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.In;

public class KdTree {
	private static final boolean VERTICAL = true; // Refers to orientation of boundary line i.e. vertical line splits on x-coordinate
	private int size;
	private Node root;
	private Point2D minPoint;
	private double minDist;
	//public int nodesVisited; // For debugging
	private static class Node {
		private boolean orientation;
		private double key;
		private Point2D value;
		private double sx, sy, ex, ey;
		Node left,right;
		public Node(boolean orientation, Point2D value, double sx, double sy, double ex, double ey) {
			this.orientation = orientation;
			if (orientation == VERTICAL) this.key = value.x();
			else this.key = value.y();
			this.value = value;
			this.sx = sx;
			this.sy = sy;
			this.ex = ex;
			this.ey = ey;
		}
	}
	// construct an empty set of points 
	public KdTree() {
		this.size = 0;
	}
	public boolean isEmpty() {
		return root == null;
	}
	// number of points in the set 
	public int size() {
		return this.size;
	}
	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		checkNull(p);
		this.root = insert(p, this.root, VERTICAL, 0, 0, 1, 1);
	}
	
	private Node insert(Point2D p, Node x, boolean orientation, double sx, double sy, double ex, double ey) {
		if (x == null) {
			this.size++;
			return new Node(orientation, p, sx, sy, ex, ey); // Insert at leaf
		}
		if (x.value.equals(p)) return x; // Ignore duplicates
		boolean goLeft = (x.orientation == VERTICAL) ? p.x() < x.key : p.y() < x.key; // Recursive case
		if (x.orientation == VERTICAL) {
			if (goLeft) x.left = insert(p, x.left, !x.orientation, x.sx, x.sy, x.key, x.ey);
			else x.right = insert(p, x.right, !x.orientation, x.key, x.sy, x.ex, x.ey);
		} else {
			if (goLeft) x.left = insert(p, x.left, !x.orientation, x.sx, x.sy, x.ex, x.key);
			else x.right = insert(p, x.right, !x.orientation, x.sx, x.key, x.ex, x.ey);
		}
		return x;
	}
	
	// does the set contain point p? 
	public boolean contains(Point2D p) {
		checkNull(p);
		return contains(p, this.root);
	}
	
	private boolean contains(Point2D p, Node x) {
		if (x == null) return false; // Not found
		if (x.value.equals(p)) return true; // Found
		boolean goLeft = (x.orientation == VERTICAL) ? p.x() < x.key : p.y() < x.key; // Recursive case
		if (goLeft) return contains(p, x.left);
		else return contains(p, x.right);
	}
	
	// draw all points to standard draw 
	public void draw() {
		draw(root);
	}
	private void draw(Node x) {
		if (x == null) return;
		StdDraw.setPenRadius(0.01);
		StdDraw.setPenColor(StdDraw.BLACK);
		x.value.draw();
		StdDraw.setPenRadius();
		if (x.orientation == VERTICAL) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(x.key, x.sx, x.key, x.ey);
		} else {
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(x.sx, x.key, x.ex, x.key);
		}
		draw(x.left);
		draw(x.right);
	}

	// all points that are inside the rectangle (or on the boundary) 
	public Iterable<Point2D> range(RectHV rect) {
		checkNull(rect);
		LinkedList<Point2D> inside = new LinkedList<Point2D>();
		range(root, rect, inside);
		return inside;
	}
	private void range(Node x, RectHV rect, LinkedList<Point2D> inside) {
		if (x == null) return; 
		if (rect.contains(x.value)) inside.add(x.value); // Add point if it's inside the rectangle
		if (x.orientation == VERTICAL) { // Explore subtrees only if it intersects the boundary line
			if (rect.xmin() < x.key) range(x.left, rect, inside);
			if (x.key <= rect.xmax()) range(x.right, rect, inside);
		} else {
			if (rect.ymin() < x.key) range(x.left, rect, inside);
			if (x.key <= rect.ymax()) range(x.right, rect, inside);
		}
	}
	
	// a nearest neighbor in the set to point p; null if the set is empty 
	// BFS rather than DFS is faster
	public Point2D nearest(Point2D p) {
		checkNull(p);
		if (isEmpty()) return null;
		this.minDist = Double.MAX_VALUE;
		//this.nodesVisited = 0;
		nearest(p, root);
		return this.minPoint;
	}
	
	private void nearest(Point2D p, Node x) {
		double distance = x.value.distanceSquaredTo(p);
		if (distance < this.minDist) {
			this.minPoint = x.value;
			this.minDist = distance;
		}
		//this.nodesVisited++;
		if ((x.orientation == VERTICAL && p.x() < x.key) || (x.orientation != VERTICAL && p.y() < x.key)) {
			if (checkBranch(x.left, p)) nearest(p, x.left);
			if (checkBranch(x.right, p)) nearest(p, x.right);
		} else {
			if (checkBranch(x.right, p)) nearest(p, x.right);
			if (checkBranch(x.left, p)) nearest(p, x.left);
		}
	}
	
	private boolean checkBranch(Node x, Point2D p) {
		if (x == null) return false;
		double dx = Math.max(Math.max(x.sx - p.x(), 0), p.x() - x.ex);
		double dy = Math.max(Math.max(x.sy - p.y(), 0), p.y() - x.ey);
		double distance = dx * dx + dy * dy;
		return distance < this.minDist;
	}
	
	private void checkNull(Object o) {
		if (o == null) throw new java.lang.IllegalArgumentException();
	}
	// unit testing of the methods (optional)
	public static void main(String[] args) {
		 // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        KdTree tree = new KdTree();
        //KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            //kdtree.insert(p);
            tree.insert(p);
        }
        
        //Point2D p = new Point2D(0.81, 0.3);
        //tree.nearest(p);
        //System.out.println(tree.nodesVisited);
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
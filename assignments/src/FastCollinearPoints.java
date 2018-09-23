import java.util.ArrayList;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Collections;
public class FastCollinearPoints {
	private LineSegment[] segments;
	public FastCollinearPoints(Point[] points) {
		if (points == null) throw new IllegalArgumentException();
		int n = points.length;
		Point[] pointsCopy = Arrays.copyOf(points, n);
		for (Point point:points)
			if (point == null) throw new IllegalArgumentException();
		Arrays.sort(pointsCopy);
		checkDuplicates(pointsCopy);
		ArrayList<LineSegment> found = new ArrayList<LineSegment>();
		LinkedList<Point> line = new LinkedList<Point>();
		for (Point start:points) {
			Arrays.sort(pointsCopy, start.slopeOrder());
			double slope = Double.NEGATIVE_INFINITY;
			for (int i = 1; i < n; i++) {
				if (Double.compare(start.slopeTo(pointsCopy[i]), slope) == 0) {
					line.add(pointsCopy[i]);
					// Corner case, where end of line coincides with end of pointsCopy
					if (line.size() >= 4 && i == (n-1)) {
						Collections.sort(line);
						if (start == line.getFirst()) 
							found.add(new LineSegment(start, line.getLast()));
						line.clear();
						line.add(start);
						line.add(pointsCopy[i]);
						slope = start.slopeTo(pointsCopy[i]);
					}
				} else {
					// Regular case, where we only know the line has ended when we reach the next point and the doesn't match
					if (line.size() >= 4) {
						Collections.sort(line);
						if (start == line.getFirst()) 
							found.add(new LineSegment(start, line.getLast()));
					}
					line.clear();
					line.add(start);
					line.add(pointsCopy[i]);
					slope = start.slopeTo(pointsCopy[i]);
				}				
			}
		}
		segments = found.toArray(new LineSegment[found.size()]);
	}
	public int numberOfSegments() {
		return segments.length;
	}
	public LineSegment[] segments() {
		return Arrays.copyOf(segments, segments.length);
	}
	// Requires points to be sorted before hand
	private void checkDuplicates(Point[] points) {
		int n = points.length;
		for (int i = 0; i < n-1; i++) 
			if (Double.compare(points[i].slopeTo(points[i+1]), Double.NEGATIVE_INFINITY) == 0) throw new IllegalArgumentException();
	}
	public static void main(String[] args) {
	    // read the n points from a file
	    In in = new In(args[0]);
	    StdOut.println(args[0]);
	    int n = in.readInt();
	    Point[] points = new Point[n];
	    for (int i = 0; i < n; i++) {
	        int x = in.readInt();
	        int y = in.readInt();
	        points[i] = new Point(x, y);
	        //StdOut.println(points[i]);
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
	    StdOut.println(collinear.segments().length);
	}
}


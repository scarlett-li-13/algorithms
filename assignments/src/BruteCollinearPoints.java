import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
public class BruteCollinearPoints {
	private LineSegment[] segments;
	public BruteCollinearPoints(Point[] points) {
		if (points == null) throw new IllegalArgumentException();
		int n = points.length;
		for (Point point:points) 
			if (point == null) throw new IllegalArgumentException();
		Point[] pointsCopy = Arrays.copyOf(points, n);
		Arrays.sort(pointsCopy);
		checkDuplicates(pointsCopy);
		ArrayList<LineSegment> found = new ArrayList<LineSegment>();
		for (int i1 = 0; i1 < n-3; i1++) {
			for (int i2 = i1+1; i2 < n-2; i2++) {
				double s1 = pointsCopy[i1].slopeTo(pointsCopy[i2]);
				for (int i3 = i2+1; i3 < n-1; i3++) {
					if (Double.compare(s1, pointsCopy[i1].slopeTo(pointsCopy[i3])) == 0) {
						for (int i4 = i3+1; i4 < n; i4++) {
							if (Double.compare(s1, pointsCopy[i1].slopeTo(pointsCopy[i4])) == 0) {
								found.add(new LineSegment(pointsCopy[i1], pointsCopy[i4]));
							}
						}
					} 	
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
	    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	    StdDraw.show();
	}
}

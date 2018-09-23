import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private boolean[][] open_sites;
	private int n_open = 0;
	private int grid_size;
	private int top = 0;
	private int bottom;
	private WeightedQuickUnionUF paths;
	private WeightedQuickUnionUF full_sites;

	// Create n-by-n grid, with all sites blocked
	public Percolation(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException();
		}
		grid_size = n;
		bottom = grid_size * grid_size + 1;
		open_sites = new boolean[grid_size][grid_size];
		paths = new WeightedQuickUnionUF(grid_size * grid_size + 2); // Add two sites for virtual top and virtual bottom
		full_sites = new WeightedQuickUnionUF(grid_size * grid_size + 1);
	}

	// Open site (row, col) if it is not open already
	public void open(int row, int col) {
		checkBounds(row, col);
		if (!isOpen(row, col)) {
			open_sites[row - 1][col - 1] = true;
			n_open++;
			// Check top / bottom / neighbor connections
			int site = index(row, col);
			if (row == 1) { // Top-most row
				paths.union(site, top);
				full_sites.union(site, top);
			}
			if (row == grid_size) { // Bottom-most row
				paths.union(site, bottom);
			}
			if (row > 1 && isOpen(row - 1, col)) { // Top neighbor
				paths.union(site, index(row - 1, col));
				full_sites.union(site, index(row - 1, col));
			}
			if (row < grid_size && isOpen(row + 1, col)) { // Bottom neighbor
				paths.union(site, index(row + 1, col));
				full_sites.union(site, index(row + 1, col));
			}
			if (col > 1 && isOpen(row, col - 1)) { // Right neighbor
				paths.union(site, index(row, col - 1));
				full_sites.union(site, index(row, col - 1));
			}
			if (col < grid_size && isOpen(row, col + 1)) { // Left neighbor
				paths.union(site, index(row, col + 1));
				full_sites.union(site, index(row, col + 1));
			}
		}
	}

	// Is site (row, col) open?
	public boolean isOpen(int row, int col) {
		checkBounds(row, col);
		return open_sites[row - 1][col - 1];
	}

	// Is site (row, col) Full?
	public boolean isFull(int row, int col) {
		checkBounds(row, col);
		return full_sites.connected(index(row, col), top);
	}

	// Number of open sites
	public int numberOfOpenSites() {
		return n_open;
	}

	// Does the system percolate?
	public boolean percolates() {
		return paths.connected(top, bottom);
	}

	private int index(int row, int col) {
		return (row - 1) * grid_size + col;
	}

	private void checkBounds(int row, int col) {
		if (row < 1 || col < 1 || row > grid_size || col > grid_size) {
			throw new IllegalArgumentException();
		}
	}
}
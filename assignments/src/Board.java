import java.util.LinkedList;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.In;
import java.lang.StringBuilder;

public class Board {
	private int [][] board = null;
	private Board twin = null;
	private final int n;
	private int dHam = -1, dMan = -1;
	private String string;
	// construct a board from an n-by-n array of blocks
	// (where blocks[i][j] = block in row i, column j)
	public Board(int[][] blocks) {
		this.n = blocks.length;
		this.board = new int [this.n][this.n];
		for (int row = 0; row < this.n; row++) 
			System.arraycopy(blocks[row], 0, this.board[row], 0, this.n);
	}
	// board dimension n									
	public int dimension() {
		return this.n;
	}
	// number of blocks out of place
	public int hamming() {
		int tile;
		if (this.dHam == -1) {
			this.dHam++;
			for (int row = 0; row < this.n; row ++) {
				for (int col = 0; col < this.n; col++) {
					tile = this.board[row][col];
					if (tile != row*this.n+col+1 && tile != 0) // Ignore empty tile
						this.dHam++;
				}
			}
		}
		return this.dHam;
	}
	// sum of Manhattan distances between blocks and goal
	public int manhattan() {
		if (this.dMan == -1) {
			this.dMan++;
			int tile;
			for (int row = 0; row < this.n; row ++) {
				for (int col = 0; col < this.n; col++) {
					tile = this.board[row][col];
					if (tile-1 != row*this.n+col && tile != 0) // Ignore empty tile
						this.dMan += tileManhattan(tile, row, col);
				}
			}
		}
		return this.dMan;
	}
	// is this board the goal board?
	public boolean isGoal() {
		for (int row = 0; row < this.n; row++) {
			for (int col = 0; col < this.n; col++) {
				if ((this.board[row][col] != row*this.n+col+1) && (this.board[row][col] != 0)) // Last tile is empty
					return false;
			}
		}
		return true;
	}
	// a board that is obtained by exchanging any pair of blocks
	public Board twin() {
		if (this.twin == null) {
			int [][] copy = new int [this.n][this.n];
			for (int row = 0; row < this.n; row ++) 
				System.arraycopy(this.board[row], 0, copy[row], 0, this.n);
			// Find non-empty tiles to switch
			int swapCol = StdRandom.uniform(this.n);
			int swapRow = StdRandom.uniform(this.n);
			if (this.board[swapRow][swapCol] != 0 && this.board[swapRow][(swapCol+1) % this.n] != 0) {
				copy[swapRow][swapCol] = this.board[swapRow][(swapCol+1) % this.n];
				copy[swapRow][(swapCol+1) % this.n] = this.board[swapRow][swapCol];
				this.twin = new Board(copy);
				//this.twin.dMan = this.dMan + twinManhattan(this.board, swapRow, swapCol, swapRow, (swapCol+1) % this.n);
				//this.twin.dHam = this.dHam + twinHamming(this.board, swapRow, swapCol, swapRow, (swapCol+1) % this.n);
			} else {
				copy[(swapRow+1) % this.n][swapCol] = this.board[(swapRow+1) % this.n][(swapCol+1) % this.n];
				copy[(swapRow+1) % this.n][(swapCol+1) % this.n] = this.board[(swapRow+1) % this.n][swapCol];
				this.twin = new Board(copy);
				this.twin.dMan = this.dMan + twinManhattan(this.board, (swapRow+1) % this.n, swapCol, (swapRow+1) % this.n, (swapCol+1) % this.n);
				this.twin.dHam = this.dHam + twinHamming(this.board, (swapRow+1) % this.n, swapCol, (swapRow+1) % this.n, (swapCol+1) % this.n);
			}
		}
		return this.twin;	
	}
	private int tileManhattan(int tile, int row, int col) {
		return Math.abs((tile-1) / this.n - row) + Math.abs((tile-1) % this.n - col);
	}
	private int tileHamming(int tile, int row, int col) {
		if (tile-1 == row*this.n+col) return 1;
		return 0;
	}
	private int twinManhattan(int[][] array, int row1, int col1, int row2, int col2) {
		int tile1 = array[row1][col1], tile2 = array[row2][col2];
		return (tileManhattan(tile1, row2, col2) + tileManhattan(tile2, row1, col1)) - (tileManhattan(tile1, row1, col1) + tileManhattan(tile2, row2, col2));
	}
	private int twinHamming(int[][] array, int row1, int col1, int row2, int col2) {
		int tile1 = array[row1][col1], tile2 = array[row2][col2];
		return (tileHamming(tile1, row2, col2) + tileHamming(tile2, row1, col1)) - (tileHamming(tile1, row1, col1) + tileHamming(tile2, row2, col2));
	}
	
	// does this board equal y?
	public boolean equals(Object y) {
		if (y == this) return true;
		if ((y != null) && (y.getClass() == this.getClass())) {
			Board that = (Board) y;
			if (that.dimension() == this.n) {
				for (int row = 0; row < this.n; row++) {
					for (int col = 0; col < this.n; col++) {
						if (that.board[row][col] != this.board[row][col])
							return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	// all neighboring boards
	public Iterable<Board> neighbors() {
		LinkedList<Board> neighbors = new LinkedList<Board>();
		int emptyRow = 0, emptyCol = 0;
		// Locate empty space
		for (int row = 0; row < this.n; row++) {
			for (int col = 0; col < this.n; col++) {
				if (this.board[row][col] == 0) {
					emptyRow = row;
					emptyCol = col;
					break;
				}
			}
		}
		// Use same board for all neighbors, simply swapping back and forth
		// Works because constructor creates it's own copy of the array
		if (emptyRow > 0) {
			swap(this.board, emptyRow, emptyCol, emptyRow-1, emptyCol);
			neighbors.add(new Board(this.board));
			swap(this.board, emptyRow, emptyCol, emptyRow-1, emptyCol);
		}
		if (emptyRow < n-1) {
			swap(this.board, emptyRow, emptyCol, emptyRow+1, emptyCol);
			neighbors.add(new Board(this.board));
			swap(this.board, emptyRow, emptyCol, emptyRow+1, emptyCol);
		}
		if (emptyCol > 0) {
			swap(this.board, emptyRow, emptyCol, emptyRow, emptyCol-1);
			neighbors.add(new Board(this.board));
			swap(this.board, emptyRow, emptyCol, emptyRow, emptyCol-1);
		}
		if (emptyCol < n-1) {
			swap(this.board, emptyRow, emptyCol, emptyRow, emptyCol+1);
			neighbors.add(new Board(this.board));
			swap(this.board, emptyRow, emptyCol, emptyRow, emptyCol+1);
		}
		return neighbors;
	}
	private void swap(int[][] array, int pRow, int pCol, int qRow, int qCol) {
		int temp = array[pRow][pCol];
		array[pRow][pCol] = array[qRow][qCol];
		array[qRow][qCol] = temp;
	}
	// string representation of this board (in the output format specified below)
	public String toString() {
		if (string == null) {
			StringBuilder output = new StringBuilder();
			output.append(this.n+"\n");
			for (int row = 0; row < this.n; row++) {
				for (int col = 0; col < this.n; col++) {
					output.append(String.format("%2d ", this.board[row][col]));
				}
				output.append("\n");
			}
			this.string = output.toString();
		}
		return this.string;
	}
	// unit tests (not graded)
	public static void main(String[] args)  {
		 // create initial board from file
	    In in = new In(args[0]);
	    int n = in.readInt();
	    int[][] blocks = new int[n][n];
	    for (int i = 0; i < n; i++)
	        for (int j = 0; j < n; j++)
	            blocks[i][j] = in.readInt();
	    Board initial = new Board(blocks);
	    StdOut.println(initial);
	    StdOut.println(initial.twin());
	    StdOut.println(initial.twin());
	    StdOut.println("Manhattan: "+initial.manhattan());
	    StdOut.println("Hamming: "+initial.hamming());
	    Iterable<Board> test = initial.neighbors();
	    for (Board board : test) 
	    	StdOut.println(board);
	}
}

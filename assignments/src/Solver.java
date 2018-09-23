import java.util.Stack;
import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;

public class Solver {
	private Node lastNode;
	private Iterable<Board> sequence = null;
	private class Node implements Comparable<Node> {
		private final Board board;
		private final Node previous;
		private int moves = 0;
		private int priority;
		public Node(Board board, Node previous) {
			this.board = board;
			this.previous = previous;
			if (previous != null)
				this.moves = previous.moves + 1;
			this.priority = this.board.manhattan() + this.moves;
		}
		public int compareTo(Node that) {
			return this.priority - that.priority;
		}
	}
	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial) {
		if (initial == null)
			throw new IllegalArgumentException();
		if (!initial.isGoal()) {
			MinPQ<Node> solver = initialize(initial);
			MinPQ<Node> solverTwin = initialize(initial.twin());
			Node current;
			do {
				current = explore(solver);
			} while (current == null && explore(solverTwin) == null);
			this.lastNode = current;
		} else this.lastNode = new Node(initial, null);
		if (this.lastNode != null && this.lastNode.board.isGoal()) {
			LinkedList<Board> solution = new LinkedList<Board>();
			Node last = this.lastNode;
			Stack<Board> reversed = new Stack<Board>();
			while (last != null) {
				reversed.push(last.board);
				last = last.previous;
			}
			while (!reversed.isEmpty()) 
				solution.add(reversed.pop());
			this.sequence = solution;
		} 
	}
	// initialize MinPQ and explore first round of neighbors
	private MinPQ<Node> initialize(Board initial) {
		MinPQ<Node> solver = new MinPQ<Node>();
		for (Board neighbor : initial.neighbors())
			solver.insert(new Node(neighbor, new Node(initial, null)));
		return solver;
	}
	// explore possible moves by removing min and adding neighbors
	private Node explore(MinPQ<Node> solver) {
		if (solver.isEmpty()) return null;
		Node current = solver.delMin();
		if (current.board.isGoal()) return current;
		for (Board neighbor : current.board.neighbors()) {
			if (!neighbor.equals(current.previous.board)) 
				solver.insert(new Node(neighbor, current));
		}
		return null;
	}
	// is the initial board solvable?
    public boolean isSolvable() {
    	return this.sequence != null;
    }
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
    	if (this.lastNode == null) return -1;
    	else return this.lastNode.moves;
    }
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
    	return this.sequence;
    }
    // solve a slider puzzle (given)
    public static void main(String[] args) {
    	long startTime = System.nanoTime();
    	// create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    	long endTime = System.nanoTime();
    	long duration = (endTime - startTime) / 1000000;  
    	StdOut.println(duration);
    }
}

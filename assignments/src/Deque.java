import java.util.Iterator;
import java.lang.IllegalArgumentException;
import java.lang.UnsupportedOperationException;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
	private Node first, last;
	private int size;

	public Deque() {
		size = 0;
	}

	private class Node {
		Item item;
		Node prev, next;
	}

	public boolean isEmpty() {
		return first == null;
	}

	public int size() {
		return size;
	}

	public void addFirst(Item item) {
		if (item == null) {
			throw new IllegalArgumentException();
		} else {
			Node oldfirst = first;
			first = new Node();
			if (size > 0)
				oldfirst.prev = first;
			else
				last = first;
			first.item = item;
			first.next = oldfirst;
			size++;
		}
	}

	public void addLast(Item item) {
		if (item == null) {
			throw new IllegalArgumentException();
		} else {
			Node oldlast = last;
			last = new Node();
			if (size > 0)
				oldlast.next = last;
			else
				first = last;
			last.item = item;
			last.prev = oldlast;
			size++;
		}
	}

	public Item removeFirst() {
		if (first == null) {
			throw new NoSuchElementException();
		} else {
			Item item = first.item;
			if (first == last)
				last = null; // After we remove this item queue will be empty
			first = first.next;
			if (first != null)
				first.prev = null; // Free old first
			size--;
			return item;
		}
	}

	public Item removeLast() {
		if (last == null) {
			throw new NoSuchElementException();
		} else {
			Item item = last.item;
			if (first == last)
				first = null; // After we remove this item queue will be empty
			last = last.prev;
			if (last != null)
				last.next = null; // Free old last
			size--;
			return item;
		}
	}

	public Iterator<Item> iterator() {
		return new DequeIterator();
	}

	private class DequeIterator implements Iterator<Item> {
		private Node current = first;

		public boolean hasNext() {
			return current != null;
		}

		public Item next() {
			if (current == null) {
				throw new NoSuchElementException();
			} else {
				Item item = current.item;
				current = current.next;
				return item;
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public static void main(String[] args) {
		Deque<Integer> test = new Deque<Integer>();
		test.addFirst(2);
		test.addLast(3);
		test.addLast(1);
		test.addFirst(5);
		System.out.println(test.size());
		for (int s : test)
			System.out.println(s);
		System.out.print(test.removeFirst());
		System.out.print(test.removeFirst());
		System.out.print(test.removeLast());
		System.out.print(test.removeLast());
		// System.out.print(test.removeFirst());
	}
}

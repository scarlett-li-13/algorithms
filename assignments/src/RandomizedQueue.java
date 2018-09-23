
import java.util.Iterator;
import java.lang.IllegalArgumentException;
import java.lang.UnsupportedOperationException;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] queue;
	private int size, capacity;
	private static final int START_CAPACITY = 16;

	public RandomizedQueue() {
		capacity = START_CAPACITY;
		size = 0;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	private void resize(int new_capacity) {
		capacity = new_capacity;
		Item[] new_queue = (Item[]) new Object[new_capacity];
		copy_queue(new_queue);
		queue = new_queue;
	}

	private void copy_queue(Item[] array_copy) {
		for (int i = 0; i < size; i++) 
			array_copy[i] = queue[i];
	}

	public void enqueue(Item item) {
		if (item == null) throw new IllegalArgumentException();
		if (queue == null) queue = (Item[]) new Object[capacity];
		else if (size == capacity) resize(2*capacity);
		queue[size] = item;
		size++;
	}

	public Item dequeue() {
		if (size == 0) throw new NoSuchElementException();
		int index = StdRandom.uniform(0, size);
		Item item = queue[index];
		size--;
		queue[index] = queue[size];
		queue[size] = null;
		if (size == capacity / 4 && size > 0) resize(capacity / 2); 
		return item;
	}

	public Item sample() {
		if (size == 0) throw new NoSuchElementException();
		int index = StdRandom.uniform(0, size);
		return queue[index];
	}

	public Iterator<Item> iterator() {
		return new RandomizedIterator();
	}

	private class RandomizedIterator implements Iterator<Item> {
		private Item[] temp;
		private int temp_size;

		public RandomizedIterator() {
			temp = (Item[]) new Object[size];
			copy_queue(temp);
			StdRandom.shuffle(temp);
			temp_size = size;
		}

		public boolean hasNext() {
			return temp_size > 0;
		}

		public Item next() {
			if (hasNext()) {
				temp_size--;
				Item item = temp[temp_size];
				temp[temp_size] = null;
				return item;
			} else {
				throw new NoSuchElementException();
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public static void main(String[] args) {
		RandomizedQueue<String> rq = new RandomizedQueue<String>();
		rq.enqueue("RGLOYUEWZQ");
        System.out.println(rq.dequeue());    
        rq.enqueue("FTJNHQJRFD");
        rq.enqueue("VLSEFQHPXP");
        System.out.println(rq.dequeue()); 
        System.out.println(rq.dequeue()); 
        rq.enqueue("FWCGWBRYTO");
        System.out.println(rq.dequeue()); 
        rq.enqueue("LTWVLICMIR");
        System.out.println(rq.dequeue()); 
        rq.enqueue("OWJREJVKZA");
		/*test.enqueue("AB");
		test.enqueue("CD");
		test.enqueue("EF");
		test.enqueue("GH");
		// test.enqueue(null);
		System.out.println(test.sample());
		System.out.println(test.dequeue());
		for (String i:test) 
			System.out.println(test.dequeue());
		// System.out.println(test.dequeue());
		// System.out.println(test.dequeue());
		// System.out.println(test.dequeue());
		// System.out.println(test.sample());
		for (String i : test) {
			System.out.print("i:");
			System.out.println(i);
			for (String j : test) {
				System.out.print("j:");
				System.out.println(j);
			}
		}*/
	}
}

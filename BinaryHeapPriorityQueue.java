/**
 * Program #3
 * Binary Heap implementation of PQ
 * CS310
 * 04/09/2018
 * @author jesuslopez cssc0736
 */
package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinaryHeapPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {

	private int currentSize;
	private int maxSize;
	private long modCounter;
	private long entryNumber;
	private Wrapper<E>[] storage;

	public BinaryHeapPriorityQueue() {
		this(DEFAULT_MAX_CAPACITY);
	}

	public BinaryHeapPriorityQueue(int size) {
		maxSize = size;
		entryNumber = 0;
		storage = (Wrapper<E>[]) new Wrapper[size];
	}

	public boolean insert(E object) {
		if (isFull())
			return false;
		storage[currentSize++] = new Wrapper<E>(object);
		trickleUp();
		modCounter++;
		return true;
	}

	private void trickleUp() {
		int newIndex = currentSize - 1;
		int parentIndex = (newIndex - 1) >> 1;
		Wrapper<E> newValue = storage[newIndex];
		while (parentIndex >= 0 && newValue.compareTo(storage[parentIndex]) < 0) {
			storage[newIndex] = storage[parentIndex];
			newIndex = parentIndex;
			parentIndex = (parentIndex - 1) >> 1;
		}
		storage[newIndex] = newValue;
	}

	private void trickleDown() {
		int current = 0;
		int child = getNextChild(current);
		while (child != -1 && storage[current].compareTo(storage[child]) > 0
				&& storage[child].compareTo(storage[currentSize - 1]) < 0) {
			storage[current] = storage[child];
			current = child;
			storage[current] = storage[currentSize - 1];
			child = getNextChild(current);
		}
	}

	private int getNextChild(int current) {
		int left = (current << 1) + 1;
		int right = left + 1;
		if (right < currentSize) {// there are two children
			if (storage[left].compareTo(storage[right]) < 0)
				return left; // the left child is smaller
			return right; // the right child is smaller
		}
		if (left < currentSize) // there is only one child
			return left;
		return -1; // no children
	}

	public E remove() {
		if (isEmpty())
			return null;
		E temp = storage[0].data;
		storage[0] = storage[currentSize - 1]; // copy last value in array to root
		trickleDown();
		currentSize--;
		modCounter++;
		return temp;
	}

	// Deletes all instances of the parameter obj from the PQ if found,
	// and returns true. Returns false if no match is found.
	public boolean delete(E obj) {
		if(!contains(obj))
			return false;
		Wrapper<E> [] tempStorage = storage;
		int size = currentSize;
		boolean removed = false;
		
		clear();
		
		for (int i =0; i < size; i++)
			if ((tempStorage[i].data).compareTo(obj) != 0) 
				insert(tempStorage[i].data);
			else {
				modCounter++; //does this go outside else
				removed = true;
			}
		return removed;
	}

	public E peek() {
		if (isEmpty())
			return null;
		return storage[0].data;

	}

	public boolean contains(E obj) {
		for (int i = 0; i < currentSize; i++)
			if ((storage[i].data).compareTo(obj) == 0)
			return true;
		return false;
	}

	public int size() {
		return currentSize;
	}

	public void clear() {
		currentSize = 0;
	}

	public boolean isEmpty() {
		return currentSize == 0;
	}

	public boolean isFull() {
		return currentSize == maxSize;
	}

	public Iterator<E> iterator() {
		return new IteratorHelper();
	}

	class IteratorHelper<E> implements Iterator<E> {
		int iterIndex;
		long modCount;
		E [] localArray;
		
		public IteratorHelper() {
			iterIndex = 0;
			localArray = (E[]) new Object[currentSize];
			modCount = modCounter;
			for(int i = 0; i < currentSize; i++)
				localArray[i] = (E) storage[i].data;
			trickleDown();
		}

		@Override
		public boolean hasNext() {
			if (modCount != modCounter)
				throw new ConcurrentModificationException();
			return iterIndex < currentSize;
		}

		@Override
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return localArray[iterIndex++];
		}

	}

	public class Wrapper<E> implements Comparable<Wrapper<E>> {
		E data;
		long seqNumber;

		public Wrapper(E d) {
			seqNumber = entryNumber++; // from parent class
			data = d;
		}

		public int compareTo(Wrapper<E> o) { //compares the heaps
			if (((Comparable<E>) data).compareTo(o.data) == 0)
				return (int) (seqNumber - o.seqNumber);
			return ((Comparable<E>) data).compareTo(o.data);
		}
	}

}

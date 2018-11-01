package data_structures;

import java.util.Iterator;

public interface PriorityQueue<E extends Comparable <E>> extends Iterable<E> {
	public static final int DEFAULT_MAX_CAPACITY = 1000;
	
	public boolean insert(E object);
	
	public E remove();
	
	public boolean delete(E obj);
	
	public E peek();
	
	public boolean contains(E obj);
	
	public int size();
	
	public void clear();
	
	public boolean isEmpty();
	
	public boolean isFull();
	
	public Iterator<E> iterator();

}
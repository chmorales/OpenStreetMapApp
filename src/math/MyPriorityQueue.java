package math;

import java.util.Arrays;
import java.util.HashMap;

/**
 * A Queue that has its elements ordered by priority. The element with the 
 * lowest priority will be the first to be polled from the queue. 
 * @author cpm02_000
 *
 * @param <T>
 */
public class MyPriorityQueue<T> {

    private class Entry<T>{
	public T data;
	public double priority;
	public Entry(T data, double priority){
	    this.data = data;
	    this.priority = priority;
	}
	@Override
	public String toString(){
	    return "["+data+", p: "+priority+"]";
	}
    }

    private HashMap<T, Integer> indices;
    private Entry<T>[] heap;
    private int heapSize;
    private int capacity;

    /**
     * Creates a new MyPriorityQueue object with the specified capacity
     * @param capacity
     */
    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int capacity){
	indices = new HashMap<T, Integer>();
	heap = new Entry[capacity];
	heapSize = 0;
	this.capacity = capacity;
    }

    private void swap(int i, int j){
	if(i < heapSize && j < heapSize){
	    if(i == j)
		return;
	    Entry<T> temp = heap[i];
	    heap[i] = heap[j];
	    heap[j] = temp;
	    indices.put(heap[i].data, i);
	    indices.put(heap[j].data, j);
	}
    }

    private int findParent(int i){
	if((i-1)/2 < 0)
	    return -1;
	else
	    return (i-1)/2;
    }

    private int findMin(int i){
	if(i*2+1>=heapSize)
	    return -1;
	if(i*2+2>=heapSize)
	    return i*2+1;
	if(heap[i*2+1].priority <= heap[i*2+2].priority)
	    return i*2+1;
	else
	    return i*2+2;
    }

    private void removeAt(int i){
	swap(i, heapSize-1);
	heap[heapSize-1] = null;
	heapSize--;
	if(i == heapSize)
	    return;
	int parent = findParent(i);
	if(parent != -1 && heap[i].priority < heap[parent].priority)
	    upwardReheapify(i);
	else
	    downwardReheapify(i);
    }

    private void upwardReheapify(int i){
	while(i!=0 && heap[i].priority < heap[(i-1)/2].priority) {
	    swap(i, (i-1)/2);
	    i = (i-1)/2;
	}
	indices.put(heap[i].data, i);
    }

    private void downwardReheapify(int i){
	int min = findMin(i);
	while(min != -1 && heap[min].priority < heap[i].priority) {
	    swap(i, min);
	    i = min;
	    min = findMin(i);
	}
    }
    
    /**
     * Returns the size of the heap
     * @return
     */
    public int size(){
	return heapSize;
    }

    /**
     * Returns true if the queue is full, false if otherwise
     * @return
     */
    public boolean isFull(){
	return heapSize == capacity;
    }

    /**
     * Returns true if the queue if empty, false if otherwise
     * @return
     */
    public boolean isEmpty(){
	return heapSize == 0;
    }
    
    /**
     * Returns true if the specified object is in the queue, false if otherwise
     * @param data
     * @return
     */
    public boolean contains(T data){
	return indices.containsKey(data);
    }

    /**
     * Inserts the object into the queue with the specified priority.
     */
    public void insert(T data, double priority) {
	heap[heapSize] = new Entry<T>(data, priority);
	int i = heapSize;
	heapSize++;
	indices.put(data, i);
	upwardReheapify(i);
    }

    /**
     * If the specified object is in the queue, removes it.
     * @param data
     */
    public void remove(T data) {
	Integer i = indices.get(data);
	if(i != null){
	    removeAt(i);
	    indices.remove(data);
	}
    }
    
    /**
     * Pops off the element of the queue with the lowest priority.
     * @return
     */
    public T poll(){
	Entry<T> polled = heap[0];
	removeAt(0);
	return polled.data;
    }

    /**
     * If the specified object is in the queue, changes its priority
     * to the new specified priority and updates the queue accordingly.
     * @param data
     * @param newPriority
     */
    public void updateKey(T data, double newPriority){
	if(!indices.containsKey(data))
	    return;
	int updateIndex = indices.get(data);
	Entry<T> oldEntry = heap[updateIndex];
	Entry<T> newEntry = new Entry<T>(oldEntry.data, newPriority);
	heap[updateIndex] = newEntry;
	if(newEntry.priority <= oldEntry.priority){
	    upwardReheapify(updateIndex);
	}else
	    downwardReheapify(updateIndex);
    }

    @Override
    public String toString(){
	return Arrays.deepToString(heap);
    }
}

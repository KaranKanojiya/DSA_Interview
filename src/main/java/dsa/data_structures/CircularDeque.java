package dsa.data_structures;

/**
 * ============================================================================
 * Implement Deque / Circular Buffer from Scratch
 * ============================================================================
 * Companies: Google, Amazon, Microsoft | LeetCode: #641
 * Difficulty: Medium
 *
 * Circular array: front and rear wrap around using modular arithmetic.
 * All operations O(1): addFirst, addLast, removeFirst, removeLast, peekFirst, peekLast.
 */
public class CircularDeque<E> {

    private final Object[] data;
    private int front, rear, size;
    private final int capacity;

    public CircularDeque(int capacity) {
        this.capacity = capacity;
        this.data = new Object[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    public void addFirst(E value) {
        if (size == capacity) throw new IllegalStateException("Deque is full");
        front = (front - 1 + capacity) % capacity; // wrap backwards
        data[front] = value;
        if (size == 0) rear = front;
        size++;
    }

    public void addLast(E value) {
        if (size == capacity) throw new IllegalStateException("Deque is full");
        rear = (rear + 1) % capacity; // wrap forward
        data[rear] = value;
        size++;
    }

    @SuppressWarnings("unchecked")
    public E removeFirst() {
        if (isEmpty()) throw new IllegalStateException("Deque is empty");
        E value = (E) data[front];
        data[front] = null;
        front = (front + 1) % capacity;
        size--;
        return value;
    }

    @SuppressWarnings("unchecked")
    public E removeLast() {
        if (isEmpty()) throw new IllegalStateException("Deque is empty");
        E value = (E) data[rear];
        data[rear] = null;
        rear = (rear - 1 + capacity) % capacity;
        size--;
        return value;
    }

    @SuppressWarnings("unchecked")
    public E peekFirst() { return isEmpty() ? null : (E) data[front]; }
    @SuppressWarnings("unchecked")
    public E peekLast() { return isEmpty() ? null : (E) data[rear]; }
    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }

    public static void main(String[] args) {
        System.out.println("=== Circular Deque Implementation ===\n");
        CircularDeque<Integer> dq = new CircularDeque<>(5);

        dq.addLast(1); dq.addLast(2); dq.addLast(3);
        dq.addFirst(0); dq.addFirst(-1);
        System.out.println("After adds: first=" + dq.peekFirst() + ", last=" + dq.peekLast() + ", size=" + dq.size());

        System.out.println("removeFirst()=" + dq.removeFirst()); // -1
        System.out.println("removeLast()=" + dq.removeLast());   // 3
        System.out.println("After removes: first=" + dq.peekFirst() + ", last=" + dq.peekLast());

        System.out.println("\nAll operations O(1) — circular array with modular arithmetic");
    }
}

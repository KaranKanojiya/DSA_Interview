package dsa.data_structures;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ============================================================================
 * Implement Queue using Two Stacks
 * ============================================================================
 * Companies: Amazon, Google, Microsoft, Meta | LeetCode: #232
 * Difficulty: Medium
 *
 * Trick: Two stacks — pushStack (for enqueue) and popStack (for dequeue).
 *   enqueue: push onto pushStack — O(1)
 *   dequeue: if popStack is empty, transfer ALL from pushStack → popStack (reverses order)
 *            then pop from popStack — O(1) amortized
 *
 * Each element is pushed and popped at most twice (once per stack) → amortized O(1).
 */
public class QueueUsingStacks<E> {

    private final Deque<E> pushStack = new ArrayDeque<>();  // enqueue here
    private final Deque<E> popStack = new ArrayDeque<>();   // dequeue from here

    public void enqueue(E value) {
        pushStack.push(value);
    }

    public E dequeue() {
        if (popStack.isEmpty()) transfer();
        if (popStack.isEmpty()) throw new IllegalStateException("Queue is empty");
        return popStack.pop();
    }

    public E peek() {
        if (popStack.isEmpty()) transfer();
        if (popStack.isEmpty()) throw new IllegalStateException("Queue is empty");
        return popStack.peek();
    }

    // Move all elements from pushStack to popStack — reverses order (LIFO → FIFO)
    private void transfer() {
        while (!pushStack.isEmpty()) {
            popStack.push(pushStack.pop());
        }
    }

    public boolean isEmpty() { return pushStack.isEmpty() && popStack.isEmpty(); }
    public int size() { return pushStack.size() + popStack.size(); }

    public static void main(String[] args) {
        System.out.println("=== Queue Using Two Stacks ===\n");
        QueueUsingStacks<Integer> queue = new QueueUsingStacks<>();

        queue.enqueue(1); queue.enqueue(2); queue.enqueue(3);
        System.out.println("Enqueued: 1, 2, 3");

        System.out.println("dequeue() = " + queue.dequeue()); // 1 (FIFO)
        System.out.println("dequeue() = " + queue.dequeue()); // 2

        queue.enqueue(4); queue.enqueue(5);
        System.out.println("Enqueued: 4, 5");

        while (!queue.isEmpty()) System.out.println("dequeue() = " + queue.dequeue()); // 3, 4, 5

        System.out.println("\nAmortized O(1) — each element transferred at most once");
    }
}

package dsa.data_structures;

/**
 * ============================================================================
 * Implement Stack + Min-Stack from Scratch
 * ============================================================================
 * Companies: Amazon, Microsoft, Apple | LeetCode: #155 Min Stack
 * Difficulty: Medium
 *
 * Stack: LIFO — push O(1), pop O(1), peek O(1)
 * Min-Stack: getMin() in O(1) — uses a second stack to track minimums
 *
 * Min-Stack trick: push current min onto minStack alongside each push.
 *   push(3): stack=[3], minStack=[3]
 *   push(5): stack=[3,5], minStack=[3,3]    ← min is still 3
 *   push(1): stack=[3,5,1], minStack=[3,3,1] ← new min is 1
 *   pop():   stack=[3,5], minStack=[3,3]      ← min back to 3
 */
public class MyStack<E extends Comparable<E>> {

    private Object[] data;
    private Object[] minData;  // tracks minimum at each level
    private int top;

    public MyStack(int capacity) {
        data = new Object[capacity];
        minData = new Object[capacity];
        top = -1;
    }

    public void push(E value) {
        if (top == data.length - 1) throw new IllegalStateException("Stack overflow");
        top++;
        data[top] = value;
        // Track current minimum
        if (top == 0) { minData[top] = value; }
        else {
            @SuppressWarnings("unchecked") E currentMin = (E) minData[top - 1];
            minData[top] = value.compareTo(currentMin) < 0 ? value : currentMin;
        }
    }

    @SuppressWarnings("unchecked")
    public E pop() {
        if (isEmpty()) throw new IllegalStateException("Stack underflow");
        E value = (E) data[top];
        data[top] = null;
        minData[top] = null;
        top--;
        return value;
    }

    @SuppressWarnings("unchecked")
    public E peek() {
        if (isEmpty()) throw new IllegalStateException("Stack is empty");
        return (E) data[top];
    }

    @SuppressWarnings("unchecked")
    public E getMin() {
        if (isEmpty()) throw new IllegalStateException("Stack is empty");
        return (E) minData[top];
    }

    public boolean isEmpty() { return top == -1; }
    public int size() { return top + 1; }

    public static void main(String[] args) {
        System.out.println("=== Stack + Min-Stack Implementation ===\n");
        MyStack<Integer> stack = new MyStack<>(10);

        stack.push(5); System.out.println("push(5) → min=" + stack.getMin());
        stack.push(3); System.out.println("push(3) → min=" + stack.getMin());
        stack.push(7); System.out.println("push(7) → min=" + stack.getMin());
        stack.push(1); System.out.println("push(1) → min=" + stack.getMin());

        System.out.println("\npop()=" + stack.pop() + " → min=" + stack.getMin());
        System.out.println("pop()=" + stack.pop() + " → min=" + stack.getMin());

        System.out.println("\nAll operations O(1) including getMin()");
    }
}

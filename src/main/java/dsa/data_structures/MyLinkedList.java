package dsa.data_structures;

/**
 * ============================================================================
 * Implement Singly + Doubly Linked List from Scratch
 * ============================================================================
 * Companies: Amazon, Microsoft, Meta
 * Difficulty: Medium | LeetCode: #707
 *
 * Singly: each node has value + next pointer
 * Doubly: each node has value + next + prev pointers
 *
 * Operations: addFirst O(1), addLast O(1) with tail, get O(n), remove O(n), reverse O(n)
 */
public class MyLinkedList<E> {

    private static class Node<E> {
        E value;
        Node<E> prev, next;
        Node(E value) { this.value = value; }
    }

    private Node<E> head, tail;
    private int size;

    public void addFirst(E value) {
        Node<E> node = new Node<>(value);
        if (head == null) { head = tail = node; }
        else { node.next = head; head.prev = node; head = node; }
        size++;
    }

    public void addLast(E value) {
        Node<E> node = new Node<>(value);
        if (tail == null) { head = tail = node; }
        else { tail.next = node; node.prev = tail; tail = node; }
        size++;
    }

    public E get(int index) {
        return getNode(index).value;
    }

    public E removeFirst() {
        if (head == null) throw new IllegalStateException("Empty list");
        E value = head.value;
        head = head.next;
        if (head != null) head.prev = null; else tail = null;
        size--;
        return value;
    }

    public E removeLast() {
        if (tail == null) throw new IllegalStateException("Empty list");
        E value = tail.value;
        tail = tail.prev;
        if (tail != null) tail.next = null; else head = null;
        size--;
        return value;
    }

    public E removeAt(int index) {
        Node<E> node = getNode(index);
        if (node.prev != null) node.prev.next = node.next; else head = node.next;
        if (node.next != null) node.next.prev = node.prev; else tail = node.prev;
        size--;
        return node.value;
    }

    public void reverse() {
        Node<E> current = head;
        while (current != null) {
            Node<E> temp = current.next;
            current.next = current.prev;
            current.prev = temp;
            current = temp;
        }
        Node<E> temp = head; head = tail; tail = temp;
    }

    private Node<E> getNode(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index);
        Node<E> current = head;
        for (int i = 0; i < index; i++) current = current.next;
        return current;
    }

    public int size() { return size; }

    public void print() {
        Node<E> curr = head;
        while (curr != null) { System.out.print(curr.value + " → "); curr = curr.next; }
        System.out.println("null (size=" + size + ")");
    }

    public static void main(String[] args) {
        System.out.println("=== Doubly Linked List Implementation ===\n");
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addLast(1); list.addLast(2); list.addLast(3); list.addFirst(0);
        list.print(); // 0 → 1 → 2 → 3

        System.out.println("get(2) = " + list.get(2));
        System.out.println("removeFirst() = " + list.removeFirst());
        System.out.println("removeLast() = " + list.removeLast());
        list.print(); // 1 → 2

        list.addLast(4); list.addLast(5);
        list.print();
        list.reverse();
        System.out.print("Reversed: "); list.print();
    }
}

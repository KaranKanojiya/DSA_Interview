package dsa.data_structures;

import java.util.Arrays;

/**
 * ============================================================================
 * Implement ArrayList (Dynamic Array) from Scratch
 * ============================================================================
 * Companies: Amazon, Google, Microsoft
 * Difficulty: Medium
 *
 * Key concept: Array that grows automatically when capacity is exceeded.
 * Growth factor: 1.5x (Java) or 2x — amortized O(1) for add.
 *
 * add(end) O(1) amortized, add(index) O(n), get O(1), remove O(n), resize O(n)
 */
public class MyArrayList<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] data;
    private int size;

    public MyArrayList() { data = new Object[DEFAULT_CAPACITY]; }

    public void add(E element) {
        ensureCapacity();
        data[size++] = element;
    }

    public void add(int index, E element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        ensureCapacity();
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return (E) data[index];
    }

    @SuppressWarnings("unchecked")
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        E removed = (E) data[index];
        System.arraycopy(data, index + 1, data, index, size - index - 1);
        data[--size] = null;
        return removed;
    }

    public void set(int index, E element) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        data[index] = element;
    }

    private void ensureCapacity() {
        if (size == data.length) {
            int newCapacity = data.length + (data.length >> 1); // 1.5x growth
            data = Arrays.copyOf(data, newCapacity);
            System.out.println("  [Resized: " + (data.length * 2 / 3) + " → " + data.length + "]");
        }
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public static void main(String[] args) {
        System.out.println("=== Dynamic Array (ArrayList) Implementation ===\n");
        MyArrayList<String> list = new MyArrayList<>();
        for (int i = 0; i < 15; i++) list.add("Item-" + i);
        System.out.println("size=" + list.size() + ", get(5)=" + list.get(5));

        list.add(0, "First");
        System.out.println("After add(0): get(0)=" + list.get(0) + ", size=" + list.size());

        System.out.println("remove(0)=" + list.remove(0));
        System.out.println("After remove: size=" + list.size());

        System.out.println("\nComplexity: add O(1) amortized, get O(1), remove O(n)");
    }
}

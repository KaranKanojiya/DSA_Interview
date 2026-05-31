package dsa.data_structures;

import java.util.Arrays;

/**
 * ============================================================================
 * #7 MOST ASKED: Implement Min-Heap / Priority Queue
 * ============================================================================
 * Companies: Google, Amazon, Meta, Microsoft
 * Difficulty: Medium
 *
 * WHAT IS A HEAP?
 *   A complete binary tree stored as an ARRAY where:
 *     Min-Heap: parent <= children (root is minimum)
 *     Max-Heap: parent >= children (root is maximum)
 *
 * ARRAY REPRESENTATION (0-indexed):
 *   Parent of i:      (i - 1) / 2
 *   Left child of i:  2 * i + 1
 *   Right child of i: 2 * i + 2
 *
 * Example Min-Heap:
 *          1
 *        /   \
 *       3     2
 *      / \   / \
 *     7   4 5   6
 *   Array: [1, 3, 2, 7, 4, 5, 6]
 *
 * OPERATIONS:
 *   insert(val):    add to end → sift UP to restore heap property, O(log n)
 *   extractMin():   remove root → move last to root → sift DOWN, O(log n)
 *   peek():         return root element, O(1)
 *   heapify(arr):   build heap from array, O(n) — NOT O(n log n)
 *
 * USE CASES: top-K problems, merge K sorted lists, median finder, Dijkstra's algorithm
 */
public class MinHeap {

    private int[] heap;
    private int size;

    public MinHeap(int capacity) {
        heap = new int[capacity];
        size = 0;
    }

    // ============================================================
    // insert(val) — add to end, sift up — O(log n)
    // ============================================================
    // 1. Add element at the end (next available slot)
    // 2. Compare with parent — if smaller, swap up
    // 3. Repeat until parent is smaller or we reach the root
    public void insert(int val) {
        if (size == heap.length) {
            heap = Arrays.copyOf(heap, heap.length * 2);  // resize
        }
        heap[size] = val;       // add at end
        siftUp(size);           // restore heap property
        size++;
    }

    // ============================================================
    // extractMin() — remove root, replace with last, sift down — O(log n)
    // ============================================================
    // 1. Save root (the minimum)
    // 2. Move last element to root position
    // 3. Sift down: compare with children, swap with smaller child
    // 4. Repeat until no child is smaller
    public int extractMin() {
        if (size == 0) throw new IllegalStateException("Heap is empty");

        int min = heap[0];           // save the minimum
        heap[0] = heap[size - 1];    // move last element to root
        size--;
        siftDown(0);                 // restore heap property

        return min;
    }

    // ============================================================
    // peek() — return root without removing — O(1)
    // ============================================================
    public int peek() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        return heap[0];
    }

    // ============================================================
    // siftUp — move element UP until parent is smaller
    // ============================================================
    // Called after insert — new element might be smaller than its parent
    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            // If parent is already smaller or equal → heap property satisfied
            if (heap[parentIndex] <= heap[index]) break;
            // Otherwise swap with parent and continue up
            swap(index, parentIndex);
            index = parentIndex;
        }
    }

    // ============================================================
    // siftDown — move element DOWN until children are larger
    // ============================================================
    // Called after extractMin — root element might be larger than children
    private void siftDown(int index) {
        while (true) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int smallest = index;

            // Find the smallest among current, left child, right child
            if (leftChild < size && heap[leftChild] < heap[smallest]) {
                smallest = leftChild;
            }
            if (rightChild < size && heap[rightChild] < heap[smallest]) {
                smallest = rightChild;
            }

            // If current is already smallest → done
            if (smallest == index) break;

            // Swap with the smaller child and continue down
            swap(index, smallest);
            index = smallest;
        }
    }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    private void printHeap() {
        System.out.print("  Heap: [");
        for (int i = 0; i < size; i++) {
            System.out.print(heap[i] + (i < size - 1 ? ", " : ""));
        }
        System.out.println("] | min=" + (size > 0 ? heap[0] : "empty"));
    }

    // ============================================================
    // DEMO
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Min-Heap / Priority Queue Implementation ===\n");

        MinHeap heap = new MinHeap(10);

        // Insert elements
        int[] values = {7, 3, 5, 1, 4, 6, 2};
        for (int v : values) {
            heap.insert(v);
            System.out.print("  insert(" + v + "): ");
            heap.printHeap();
        }

        // Peek
        System.out.println("\n  peek() = " + heap.peek());

        // Extract all in sorted order (heap sort!)
        System.out.println("\n=== Extract Min (sorted order) ===");
        while (!heap.isEmpty()) {
            int min = heap.extractMin();
            System.out.print("  extractMin() = " + min + " → ");
            heap.printHeap();
        }

        System.out.println("\n=== Complexity ===");
        System.out.println("insert:     O(log n) — sift up");
        System.out.println("extractMin: O(log n) — sift down");
        System.out.println("peek:       O(1)");
        System.out.println("heapify:    O(n) — bottom-up build");
    }
}

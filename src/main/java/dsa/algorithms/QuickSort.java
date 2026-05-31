package dsa.algorithms;

import java.util.Arrays;
import java.util.Random;

/**
 * ============================================================================
 * Implement QuickSort from Scratch
 * ============================================================================
 * Companies: Google, Microsoft, Amazon | Difficulty: Medium
 *
 * Pick pivot, partition: elements < pivot go left, > pivot go right.
 * Recursively sort left and right partitions. In-place. NOT stable.
 * O(n log n) average, O(n^2) worst (mitigated by random pivot).
 */
public class QuickSort {

    private static final Random rand = new Random();

    public static void sort(int[] arr) { quickSort(arr, 0, arr.length - 1); }

    private static void quickSort(int[] arr, int low, int high) {
        if (low >= high) return;
        int pivotIndex = partition(arr, low, high);
        quickSort(arr, low, pivotIndex - 1);
        quickSort(arr, pivotIndex + 1, high);
    }

    // Lomuto partition: random pivot, elements < pivot go left
    private static int partition(int[] arr, int low, int high) {
        // Random pivot avoids O(n^2) on sorted input
        int randIdx = low + rand.nextInt(high - low + 1);
        swap(arr, randIdx, high); // move pivot to end

        int pivot = arr[high];
        int i = low - 1; // boundary of "less than pivot" region

        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high); // place pivot in correct position
        return i + 1;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i]; arr[i] = arr[j]; arr[j] = temp;
    }

    public static void main(String[] args) {
        System.out.println("=== QuickSort ===\n");
        int[] arr = {10, 80, 30, 90, 40, 50, 70};
        System.out.println("Before: " + Arrays.toString(arr));
        sort(arr);
        System.out.println("After:  " + Arrays.toString(arr));
        System.out.println("\nO(n log n) avg, O(n^2) worst, in-place, NOT stable");
    }
}

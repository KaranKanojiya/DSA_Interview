package dsa.algorithms;

import java.util.Arrays;

/**
 * ============================================================================
 * Implement MergeSort from Scratch
 * ============================================================================
 * Companies: Microsoft, Google, Amazon | Difficulty: Medium
 *
 * Divide and conquer: split in half, sort each half, merge sorted halves.
 * STABLE sort (preserves order of equal elements). O(n log n) always. O(n) extra space.
 */
public class MergeSort {

    public static void sort(int[] arr) {
        if (arr.length <= 1) return;
        int mid = arr.length / 2;
        int[] left = Arrays.copyOfRange(arr, 0, mid);
        int[] right = Arrays.copyOfRange(arr, mid, arr.length);
        sort(left);
        sort(right);
        merge(arr, left, right);
    }

    private static void merge(int[] arr, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) arr[k++] = left[i++]; // <= makes it stable
            else arr[k++] = right[j++];
        }
        while (i < left.length) arr[k++] = left[i++];
        while (j < right.length) arr[k++] = right[j++];
    }

    public static void main(String[] args) {
        System.out.println("=== MergeSort ===\n");
        int[] arr = {38, 27, 43, 3, 9, 82, 10};
        System.out.println("Before: " + Arrays.toString(arr));
        sort(arr);
        System.out.println("After:  " + Arrays.toString(arr));
        System.out.println("\nO(n log n) always, O(n) space, STABLE");
    }
}

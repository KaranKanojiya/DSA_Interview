package dsa.design;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

/**
 * ============================================================================
 * #9 MOST ASKED: Implement Iterator (BST Iterator)
 * ============================================================================
 * Companies: Google, Meta, Amazon, Apple
 * LeetCode: #173 Binary Search Tree Iterator
 * Difficulty: Medium
 *
 * PROBLEM:
 *   Create an iterator that traverses a BST in-order (smallest to largest).
 *   next()    → return the next smallest element
 *   hasNext() → return true if more elements exist
 *   Both must be O(1) AVERAGE time and O(h) space (h = tree height).
 *
 * APPROACH:
 *   Use a STACK to simulate in-order traversal.
 *   Push all left children to stack → the top is always the next smallest.
 *
 *   pushAllLeft(node):
 *     while (node != null) {
 *       stack.push(node);
 *       node = node.left;
 *     }
 *
 *   next():
 *     1. Pop from stack (this is the next smallest)
 *     2. If popped node has a right child → pushAllLeft(right child)
 *     3. Return the popped node's value
 *
 * WHY THIS WORKS:
 *   In-order = left → root → right
 *   Stack holds the "path to the next smallest" — left side of remaining subtree
 *   When we pop (visit root), we then process the right subtree by pushing its lefts
 *
 * TIME: O(1) average per call (each node pushed and popped exactly once across all calls)
 * SPACE: O(h) where h = tree height (stack holds at most h nodes)
 */
public class BSTIterator {

    // ============================================================
    // BST Node
    // ============================================================
    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) { this.val = val; }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    // ============================================================
    // Iterator fields
    // ============================================================
    private final Deque<TreeNode> stack;

    // Constructor: push all left children of root onto stack
    public BSTIterator(TreeNode root) {
        stack = new ArrayDeque<>();
        pushAllLeft(root);
    }

    // ============================================================
    // next() — return next smallest value
    // ============================================================
    public int next() {
        if (!hasNext()) throw new NoSuchElementException("No more elements");

        // Step 1: Pop the top (this is the next smallest)
        TreeNode current = stack.pop();

        // Step 2: If it has a right subtree, push all left children of right subtree
        // This ensures the NEXT call to next() returns the correct value
        if (current.right != null) {
            pushAllLeft(current.right);
        }

        return current.val;
    }

    // ============================================================
    // hasNext() — O(1)
    // ============================================================
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    // ============================================================
    // Helper: push node and all its left children onto stack
    // ============================================================
    // This "queues up" the leftmost path — guaranteeing in-order traversal
    private void pushAllLeft(TreeNode node) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
    }

    // ============================================================
    // DEMO
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== BST Iterator Implementation ===\n");

        // Build BST:
        //         7
        //        / \
        //       3   15
        //          /  \
        //         9    20
        TreeNode root = new TreeNode(7,
                new TreeNode(3, null, null),
                new TreeNode(15,
                        new TreeNode(9, null, null),
                        new TreeNode(20, null, null)));

        BSTIterator iterator = new BSTIterator(root);

        System.out.println("In-order traversal via iterator:");
        // Expected: 3, 7, 9, 15, 20 (sorted order)
        while (iterator.hasNext()) {
            System.out.println("  next() = " + iterator.next());
        }

        System.out.println("\n=== How it works ===");
        System.out.println("Stack holds the leftmost path — top is always the next smallest");
        System.out.println("After popping, push all lefts of the right subtree");

        System.out.println("\n=== Complexity ===");
        System.out.println("next():    O(1) average (each node pushed/popped exactly once)");
        System.out.println("hasNext(): O(1)");
        System.out.println("Space:     O(h) where h = tree height");
    }
}

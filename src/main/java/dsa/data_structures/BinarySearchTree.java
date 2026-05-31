package dsa.data_structures;

/**
 * ============================================================================
 * #10 MOST ASKED: Implement Binary Search Tree with Full Operations
 * ============================================================================
 * Companies: Amazon, Microsoft, Apple, Google
 * Difficulty: Medium
 *
 * BST PROPERTY:
 *   For every node: left subtree values < node value < right subtree values
 *
 * OPERATIONS:
 *   insert(val):       O(log n) avg, O(n) worst
 *   search(val):       O(log n) avg, O(n) worst
 *   delete(val):       O(log n) avg, O(n) worst
 *   findMin/findMax:   O(log n) avg — go all the way left/right
 *   inorder traversal: O(n) — visits all nodes in sorted order
 *   isValidBST():      O(n) — verify BST property
 *
 * DELETE — the tricky operation (3 cases):
 *   Case 1: Leaf node → just remove it
 *   Case 2: One child → replace node with its child
 *   Case 3: Two children → replace with IN-ORDER SUCCESSOR (smallest in right subtree)
 *           then delete the successor from its original position
 *
 * WORST CASE: O(n) when tree degenerates to a linked list (all left or all right)
 *   Fix: self-balancing trees (AVL, Red-Black) → guarantee O(log n)
 */
public class BinarySearchTree {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    private TreeNode root;

    // ============================================================
    // insert(val) — O(log n) average
    // ============================================================
    public void insert(int val) {
        root = insertHelper(root, val);
    }

    private TreeNode insertHelper(TreeNode node, int val) {
        // Base case: found the right spot → create new node
        if (node == null) return new TreeNode(val);

        // Recurse left or right based on BST property
        if (val < node.val) {
            node.left = insertHelper(node.left, val);
        } else if (val > node.val) {
            node.right = insertHelper(node.right, val);
        }
        // If val == node.val → duplicate, do nothing (or handle as needed)

        return node;
    }

    // ============================================================
    // search(val) — O(log n) average
    // ============================================================
    public boolean search(int val) {
        return searchHelper(root, val);
    }

    private boolean searchHelper(TreeNode node, int val) {
        if (node == null) return false;           // not found
        if (val == node.val) return true;          // found
        if (val < node.val) return searchHelper(node.left, val);   // go left
        return searchHelper(node.right, val);     // go right
    }

    // ============================================================
    // delete(val) — O(log n) average
    // ============================================================
    public void delete(int val) {
        root = deleteHelper(root, val);
    }

    private TreeNode deleteHelper(TreeNode node, int val) {
        if (node == null) return null;

        // Navigate to the node to delete
        if (val < node.val) {
            node.left = deleteHelper(node.left, val);
        } else if (val > node.val) {
            node.right = deleteHelper(node.right, val);
        } else {
            // Found the node to delete — handle 3 cases:

            // Case 1 & 2: No left child or no right child
            if (node.left == null) return node.right;   // replace with right child (or null)
            if (node.right == null) return node.left;    // replace with left child

            // Case 3: Two children
            // Find in-order successor (smallest value in right subtree)
            TreeNode successor = findMinNode(node.right);
            // Replace current node's value with successor's value
            node.val = successor.val;
            // Delete the successor from right subtree
            node.right = deleteHelper(node.right, successor.val);
        }
        return node;
    }

    // ============================================================
    // findMin / findMax — O(log n) average
    // ============================================================
    public int findMin() {
        if (root == null) throw new IllegalStateException("Tree is empty");
        return findMinNode(root).val;
    }

    private TreeNode findMinNode(TreeNode node) {
        // Go all the way left
        while (node.left != null) node = node.left;
        return node;
    }

    public int findMax() {
        if (root == null) throw new IllegalStateException("Tree is empty");
        TreeNode current = root;
        while (current.right != null) current = current.right;
        return current.val;
    }

    // ============================================================
    // In-order traversal — O(n), prints sorted order
    // ============================================================
    public void inorder() {
        inorderHelper(root);
        System.out.println();
    }

    private void inorderHelper(TreeNode node) {
        if (node == null) return;
        inorderHelper(node.left);
        System.out.print(node.val + " ");
        inorderHelper(node.right);
    }

    // ============================================================
    // isValidBST — O(n)
    // ============================================================
    public boolean isValidBST() {
        return isValidHelper(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValidHelper(TreeNode node, long min, long max) {
        if (node == null) return true;
        if (node.val <= min || node.val >= max) return false;
        return isValidHelper(node.left, min, node.val)
                && isValidHelper(node.right, node.val, max);
    }

    // ============================================================
    // DEMO
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Binary Search Tree Implementation ===\n");

        BinarySearchTree bst = new BinarySearchTree();

        // Insert
        int[] values = {8, 3, 10, 1, 6, 14, 4, 7, 13};
        for (int v : values) bst.insert(v);

        System.out.print("Inorder (sorted): ");
        bst.inorder();  // 1 3 4 6 7 8 10 13 14

        // Search
        System.out.println("search(6):  " + bst.search(6));   // true
        System.out.println("search(99): " + bst.search(99));  // false

        // Min/Max
        System.out.println("findMin():  " + bst.findMin());   // 1
        System.out.println("findMax():  " + bst.findMax());   // 14

        // Validate
        System.out.println("isValidBST(): " + bst.isValidBST()); // true

        // Delete — Case 1: leaf (4)
        System.out.println("\nDelete 4 (leaf):");
        bst.delete(4);
        System.out.print("Inorder: "); bst.inorder();

        // Delete — Case 2: one child (14 has left child 13)
        System.out.println("Delete 14 (one child):");
        bst.delete(14);
        System.out.print("Inorder: "); bst.inorder();

        // Delete — Case 3: two children (3 has children 1 and 6)
        System.out.println("Delete 3 (two children — replaced by successor 6):");
        bst.delete(3);
        System.out.print("Inorder: "); bst.inorder();

        System.out.println("\n=== Complexity ===");
        System.out.println("insert/search/delete: O(log n) average, O(n) worst (degenerate)");
        System.out.println("inorder traversal:    O(n)");
        System.out.println("For guaranteed O(log n): use AVL or Red-Black Tree");
    }
}

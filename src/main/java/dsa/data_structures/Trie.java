package dsa.data_structures;

/**
 * ============================================================================
 * #4 MOST ASKED: Implement Trie (Prefix Tree)
 * ============================================================================
 * Companies: Google, Amazon, Meta, Microsoft
 * LeetCode: #208 Implement Trie (Prefix Tree)
 * Difficulty: Medium
 *
 * WHAT IS A TRIE?
 *   A tree where each node represents a CHARACTER, and paths from root to node
 *   spell out prefixes. Used for: autocomplete, spell check, IP routing, word games.
 *
 *   Example — insert "cat", "car", "card":
 *            root
 *             |
 *             c
 *             |
 *             a
 *            / \
 *           t   r*
 *           *   |
 *               d*
 *   (* = end of word)
 *
 * METHODS:
 *   insert(word)       → add word to trie, O(m) where m = word length
 *   search(word)       → exact match, O(m)
 *   startsWith(prefix) → any word starts with prefix, O(m)
 *   delete(word)       → remove word, O(m)
 *
 * WHY TRIE OVER HASHSET?
 *   Trie: prefix search O(m), autocomplete O(m + results)
 *   HashSet: exact match O(m), NO prefix search capability
 *
 * TWO IMPLEMENTATIONS:
 *   Array-based:  children[26] — fast, fixed to lowercase a-z
 *   HashMap-based: Map<Character, TrieNode> — flexible, supports any charset
 */
public class Trie {

    // ============================================================
    // Step 1: TrieNode — each node stores children and end-of-word flag
    // ============================================================
    private static class TrieNode {
        TrieNode[] children = new TrieNode[26];  // a-z only (index 0-25)
        boolean isEndOfWord = false;              // true if this node marks end of a valid word

        // Check if child exists for character c
        boolean hasChild(char c) {
            return children[c - 'a'] != null;
        }

        // Get child node for character c
        TrieNode getChild(char c) {
            return children[c - 'a'];
        }

        // Create/set child node for character c
        void setChild(char c) {
            children[c - 'a'] = new TrieNode();
        }
    }

    private final TrieNode root;

    public Trie() {
        root = new TrieNode();  // root is empty — doesn't represent any character
    }

    // ============================================================
    // Step 2: insert(word) — O(m) where m = word length
    // ============================================================
    // Walk down the trie, creating nodes as needed
    public void insert(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            // If no child for this character, create one
            if (!current.hasChild(c)) {
                current.setChild(c);
            }
            // Move to child node
            current = current.getChild(c);
        }
        // Mark the last node as end of word
        current.isEndOfWord = true;
    }

    // ============================================================
    // Step 3: search(word) — O(m), exact match
    // ============================================================
    public boolean search(String word) {
        TrieNode node = findNode(word);
        // Must find the node AND it must be marked as end of word
        // "car" exists doesn't mean "ca" is a word
        return node != null && node.isEndOfWord;
    }

    // ============================================================
    // Step 4: startsWith(prefix) — O(m), prefix match
    // ============================================================
    public boolean startsWith(String prefix) {
        // Just need to find the node — doesn't need to be end of word
        return findNode(prefix) != null;
    }

    // ============================================================
    // Step 5: delete(word) — O(m)
    // ============================================================
    public boolean delete(String word) {
        return deleteHelper(root, word, 0);
    }

    // Recursive delete — returns true if current node should be deleted
    private boolean deleteHelper(TrieNode current, String word, int index) {
        if (index == word.length()) {
            // Reached end of word
            if (!current.isEndOfWord) return false;  // word doesn't exist
            current.isEndOfWord = false;              // unmark end of word
            return hasNoChildren(current);             // delete node if no children
        }

        char c = word.charAt(index);
        if (!current.hasChild(c)) return false;  // word doesn't exist

        boolean shouldDeleteChild = deleteHelper(current.getChild(c), word, index + 1);

        if (shouldDeleteChild) {
            current.children[c - 'a'] = null;         // remove child
            return !current.isEndOfWord && hasNoChildren(current);  // clean up if empty
        }
        return false;
    }

    // ============================================================
    // Helpers
    // ============================================================
    // Navigate to the node representing the last character of the word/prefix
    private TrieNode findNode(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            if (!current.hasChild(c)) return null;
            current = current.getChild(c);
        }
        return current;
    }

    private boolean hasNoChildren(TrieNode node) {
        for (TrieNode child : node.children) {
            if (child != null) return false;
        }
        return true;
    }

    // ============================================================
    // DEMO
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Trie (Prefix Tree) Implementation ===\n");

        Trie trie = new Trie();

        // Insert words
        String[] words = {"cat", "car", "card", "care", "careful", "dog", "do", "done"};
        for (String w : words) {
            trie.insert(w);
            System.out.println("  Inserted: " + w);
        }

        // Search — exact match
        System.out.println("\n=== Search (exact match) ===");
        System.out.println("  search(cat):     " + trie.search("cat"));      // true
        System.out.println("  search(car):     " + trie.search("car"));      // true
        System.out.println("  search(ca):      " + trie.search("ca"));       // false — prefix, not word
        System.out.println("  search(careful): " + trie.search("careful"));   // true
        System.out.println("  search(cats):    " + trie.search("cats"));      // false

        // Prefix search
        System.out.println("\n=== StartsWith (prefix match) ===");
        System.out.println("  startsWith(ca):  " + trie.startsWith("ca"));   // true
        System.out.println("  startsWith(car): " + trie.startsWith("car"));  // true
        System.out.println("  startsWith(xyz): " + trie.startsWith("xyz"));  // false

        // Delete
        System.out.println("\n=== Delete ===");
        trie.delete("car");
        System.out.println("  Deleted 'car'");
        System.out.println("  search(car):  " + trie.search("car"));         // false
        System.out.println("  search(card): " + trie.search("card"));        // true — not affected
        System.out.println("  search(care): " + trie.search("care"));        // true — not affected

        System.out.println("\n=== Complexity ===");
        System.out.println("insert/search/startsWith/delete: O(m) where m = word length");
        System.out.println("Space: O(ALPHABET_SIZE * m * n) where n = number of words");
    }
}

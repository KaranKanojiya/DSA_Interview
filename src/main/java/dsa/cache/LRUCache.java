package dsa.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================================
 * #1 MOST ASKED: Implement LRU Cache from Scratch
 * ============================================================================
 * Companies: Amazon, Google, Meta, Microsoft, Apple — asked in ~30% of interviews
 * LeetCode: #146 LRU Cache
 * Difficulty: Medium
 *
 * WHAT IS LRU?
 *   Least Recently Used — evicts the item that hasn't been accessed for the longest time.
 *   When cache is full and a new item arrives, remove the LEAST recently used item.
 *
 * DATA STRUCTURE:
 *   HashMap + Doubly Linked List = O(1) for both get and put
 *
 *   HashMap: key → Node (O(1) lookup)
 *   Doubly Linked List: maintains access order
 *     HEAD ↔ most recent ↔ ... ↔ least recent ↔ TAIL
 *
 *   get(key):
 *     1. Look up in HashMap → O(1)
 *     2. Move node to HEAD (most recent) → O(1)
 *     3. Return value
 *
 *   put(key, value):
 *     1. If key exists → update value, move to HEAD
 *     2. If full → remove TAIL node (least recent) + remove from HashMap
 *     3. Create new node → add to HEAD + add to HashMap
 *
 * WHY Doubly Linked List?
 *   Need O(1) removal of any node (not just head/tail).
 *   With a doubly linked list, if you have a reference to the node,
 *   you can remove it in O(1) by updating prev.next and next.prev.
 *   Singly linked list would require O(n) to find the previous node.
 *
 * TIME COMPLEXITY: O(1) for get and put
 * SPACE COMPLEXITY: O(capacity)
 *
 * FOLLOW-UPS:
 *   - Thread-safe version? → Use ReentrantReadWriteLock or ConcurrentHashMap + synchronized list ops
 *   - LRU with TTL? → Store timestamp in each node, check on get(), background cleanup thread
 *   - Distributed LRU? → Consistent hashing across multiple cache servers
 */
public class LRUCache {

    // ============================================================
    // Step 1: Define the Doubly Linked List Node
    // Each node stores key + value + pointers to prev and next
    // Key is needed so when we evict from tail, we can remove from HashMap too
    // ============================================================
    private static class Node {
        int key;
        int value;
        Node prev;
        Node next;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    // ============================================================
    // Step 2: Cache fields
    // ============================================================
    private final int capacity;
    private final Map<Integer, Node> map;   // key → Node (O(1) lookup)
    private final Node head;                 // dummy head (most recent side)
    private final Node tail;                 // dummy tail (least recent side)

    // Why dummy nodes? They simplify edge cases — we never need to check for null
    // head and tail are NEVER removed; real nodes go between them
    // head ↔ [real nodes] ↔ tail

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();

        // Initialize dummy head and tail, linked to each other
        this.head = new Node(-1, -1);
        this.tail = new Node(-1, -1);
        head.next = tail;
        tail.prev = head;
    }

    // ============================================================
    // Step 3: get(key) — O(1)
    // ============================================================
    public int get(int key) {
        // 1. Check if key exists in HashMap
        if (!map.containsKey(key)) {
            return -1;  // cache miss
        }

        // 2. Key exists → get the node
        Node node = map.get(key);

        // 3. Move to head (mark as most recently used)
        removeNode(node);       // detach from current position
        addToHead(node);        // attach right after head (most recent)

        return node.value;
    }

    // ============================================================
    // Step 4: put(key, value) — O(1)
    // ============================================================
    public void put(int key, int value) {
        // Case 1: Key already exists → update value + move to head
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.value = value;         // update value
            removeNode(node);           // detach from current position
            addToHead(node);            // move to most recent
            return;
        }

        // Case 2: Cache is full → evict the least recently used (tail.prev)
        if (map.size() == capacity) {
            Node lru = tail.prev;       // least recently used = node just before tail
            removeNode(lru);            // remove from linked list
            map.remove(lru.key);        // remove from HashMap (that's why Node stores key!)
        }

        // Case 3: Add new node
        Node newNode = new Node(key, value);
        addToHead(newNode);             // add to most recent position
        map.put(key, newNode);          // add to HashMap
    }

    // ============================================================
    // Helper: Remove a node from its current position — O(1)
    // ============================================================
    // Before: ... ↔ A ↔ node ↔ B ↔ ...
    // After:  ... ↔ A ↔ B ↔ ...  (node is detached)
    private void removeNode(Node node) {
        node.prev.next = node.next;     // A.next = B
        node.next.prev = node.prev;     // B.prev = A
    }

    // ============================================================
    // Helper: Add a node right after head (most recent position) — O(1)
    // ============================================================
    // Before: head ↔ X ↔ ...
    // After:  head ↔ node ↔ X ↔ ...
    private void addToHead(Node node) {
        node.next = head.next;          // node.next = X
        node.prev = head;              // node.prev = head
        head.next.prev = node;         // X.prev = node
        head.next = node;             // head.next = node
    }

    // ============================================================
    // Helper: Print cache state (for debugging)
    // ============================================================
    private void printCache() {
        StringBuilder sb = new StringBuilder("Cache [most recent → least recent]: ");
        Node current = head.next;
        while (current != tail) {
            sb.append("(").append(current.key).append("=").append(current.value).append(") ");
            current = current.next;
        }
        sb.append(" | size=").append(map.size()).append("/").append(capacity);
        System.out.println(sb);
    }

    // ============================================================
    // DEMO
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== LRU Cache Implementation ===\n");

        LRUCache cache = new LRUCache(3);

        // Put 3 items
        cache.put(1, 100); cache.printCache();  // (1=100)
        cache.put(2, 200); cache.printCache();  // (2=200) (1=100)
        cache.put(3, 300); cache.printCache();  // (3=300) (2=200) (1=100)

        // Access key 1 → moves to head
        System.out.println("\nget(1) = " + cache.get(1));
        cache.printCache();  // (1=100) (3=300) (2=200) — 1 moved to front

        // Put key 4 → cache full → evicts key 2 (least recent)
        System.out.println("\nput(4, 400) — evicts key 2 (LRU)");
        cache.put(4, 400);
        cache.printCache();  // (4=400) (1=100) (3=300) — key 2 evicted

        // Key 2 is gone
        System.out.println("\nget(2) = " + cache.get(2));  // -1 (evicted)

        // Update existing key
        System.out.println("\nput(3, 999) — update value");
        cache.put(3, 999);
        cache.printCache();  // (3=999) (4=400) (1=100) — key 3 moved to front with new value

        System.out.println("\n=== Time Complexity ===");
        System.out.println("get():  O(1) — HashMap lookup + linked list move");
        System.out.println("put():  O(1) — HashMap insert + linked list insert/evict");
        System.out.println("Space:  O(capacity) — HashMap + Doubly Linked List");
    }
}

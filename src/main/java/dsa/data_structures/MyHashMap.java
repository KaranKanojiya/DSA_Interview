package dsa.data_structures;

/**
 * ============================================================================
 * #2 MOST ASKED: Implement HashMap from Scratch
 * ============================================================================
 * Companies: Amazon, Google, Microsoft, Meta
 * Difficulty: Medium
 *
 * HOW HASHMAP WORKS:
 *   1. Array of "buckets" (default size 16)
 *   2. hash(key) → bucket index = hash & (array.length - 1)
 *   3. Each bucket is a linked list (chain) of entries
 *   4. On collision: new entry appended to the chain
 *   5. When load factor > 0.75: resize to 2x and rehash all entries
 *
 * COLLISION HANDLING:
 *   Separate Chaining (this implementation): each bucket is a linked list
 *   Open Addressing: probe for next empty slot (linear, quadratic, double hashing)
 *
 * JAVA 8+ OPTIMIZATION (not implemented here, but explain in interview):
 *   When a bucket's chain exceeds 8 entries → converts to Red-Black Tree (O(log n) lookup)
 *   When it drops below 6 → converts back to linked list
 *
 * TIME COMPLEXITY:
 *   Average: O(1) for get/put/remove
 *   Worst case: O(n) — all keys in one bucket (bad hash function)
 *
 * FOLLOW-UPS:
 *   - Thread-safe? → ConcurrentHashMap (segment locking / CAS)
 *   - Null keys? → Store null key at bucket index 0 (Java HashMap supports one null key)
 *   - Custom hash function? → Must satisfy: equal objects → same hash
 */
public class MyHashMap<K, V> {

    // ============================================================
    // Step 1: Define Entry (key-value pair in the chain)
    // ============================================================
    private static class Entry<K, V> {
        final K key;
        V value;
        final int hash;       // cached hash — avoid recomputing on resize
        Entry<K, V> next;     // next entry in the chain (linked list)

        Entry(K key, V value, int hash, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    // ============================================================
    // Step 2: HashMap fields
    // ============================================================
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Entry<K, V>[] buckets;  // array of bucket heads
    private int size;                // number of key-value pairs

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.buckets = new Entry[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // ============================================================
    // Step 3: Hash function — converts key to bucket index
    // ============================================================
    // Why hash ^ (hash >>> 16)?
    //   Spreads higher bits into lower bits to reduce collisions
    //   when table size is a power of 2 (bucket = hash & (length-1) only uses lower bits)
    private int hash(K key) {
        if (key == null) return 0;
        int h = key.hashCode();
        return h ^ (h >>> 16);  // spread higher bits — same as Java's HashMap
    }

    // Convert hash to bucket index
    // Why & (length-1) instead of % length?
    //   Faster bitwise AND (works because length is always power of 2)
    //   Same as hash % length, but no division
    private int bucketIndex(int hash) {
        return hash & (buckets.length - 1);
    }

    // ============================================================
    // Step 4: put(key, value) — O(1) average
    // ============================================================
    public void put(K key, V value) {
        int hash = hash(key);
        int index = bucketIndex(hash);

        // Walk the chain at this bucket — check if key already exists
        Entry<K, V> current = buckets[index];
        while (current != null) {
            // Key found → update value
            if (current.hash == hash && (current.key == key || (key != null && key.equals(current.key)))) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        // Key not found → add new entry at HEAD of chain (O(1) insertion)
        buckets[index] = new Entry<>(key, value, hash, buckets[index]);
        size++;

        // Check if we need to resize
        if (size > buckets.length * LOAD_FACTOR) {
            resize();
        }
    }

    // ============================================================
    // Step 5: get(key) — O(1) average
    // ============================================================
    public V get(K key) {
        int hash = hash(key);
        int index = bucketIndex(hash);

        // Walk the chain at this bucket
        Entry<K, V> current = buckets[index];
        while (current != null) {
            if (current.hash == hash && (current.key == key || (key != null && key.equals(current.key)))) {
                return current.value;
            }
            current = current.next;
        }
        return null;  // key not found
    }

    // ============================================================
    // Step 6: remove(key) — O(1) average
    // ============================================================
    public V remove(K key) {
        int hash = hash(key);
        int index = bucketIndex(hash);

        Entry<K, V> current = buckets[index];
        Entry<K, V> prev = null;

        while (current != null) {
            if (current.hash == hash && (current.key == key || (key != null && key.equals(current.key)))) {
                // Found the entry — remove from chain
                if (prev == null) {
                    buckets[index] = current.next;  // head of chain
                } else {
                    prev.next = current.next;       // middle/end of chain
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;  // key not found
    }

    // ============================================================
    // Step 7: resize() — double the capacity, rehash everything
    // ============================================================
    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldBuckets = buckets;
        buckets = new Entry[oldBuckets.length * 2];  // double the size

        // Rehash all entries — bucket index changes because array length changed
        // New index = hash & (newLength - 1)
        for (Entry<K, V> head : oldBuckets) {
            Entry<K, V> current = head;
            while (current != null) {
                Entry<K, V> next = current.next;  // save next before we modify current
                int newIndex = bucketIndex(current.hash);
                current.next = buckets[newIndex];  // insert at head of new chain
                buckets[newIndex] = current;
                current = next;
            }
        }
        System.out.println("  [Resized: " + oldBuckets.length + " → " + buckets.length + " buckets]");
    }

    public boolean containsKey(K key) { return get(key) != null; }
    public int size() { return size; }

    // ============================================================
    // DEMO
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Custom HashMap Implementation ===\n");

        MyHashMap<String, Integer> map = new MyHashMap<>();

        // Put entries
        map.put("Karan", 30);
        map.put("Alice", 25);
        map.put("Bob", 35);
        System.out.println("After 3 puts: size=" + map.size());

        // Get
        System.out.println("get(Karan) = " + map.get("Karan"));   // 30
        System.out.println("get(Alice) = " + map.get("Alice"));   // 25
        System.out.println("get(Unknown) = " + map.get("Unknown")); // null

        // Update
        map.put("Karan", 31);
        System.out.println("After update: get(Karan) = " + map.get("Karan")); // 31

        // Remove
        System.out.println("remove(Bob) = " + map.remove("Bob")); // 35
        System.out.println("After remove: size=" + map.size()); // 2
        System.out.println("get(Bob) = " + map.get("Bob")); // null

        // Test resize — add enough entries to trigger
        System.out.println("\nAdding 20 entries to trigger resize...");
        for (int i = 0; i < 20; i++) {
            map.put("Key" + i, i);
        }
        System.out.println("Final size: " + map.size());
        System.out.println("get(Key15) = " + map.get("Key15")); // 15

        System.out.println("\n=== Complexity ===");
        System.out.println("put/get/remove: O(1) average, O(n) worst case");
        System.out.println("resize: O(n) — rehash all entries");
        System.out.println("Load factor 0.75 → resize at 75% full");
    }
}

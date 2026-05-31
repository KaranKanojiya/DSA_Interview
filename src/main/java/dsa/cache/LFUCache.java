package dsa.cache;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * ============================================================================
 * #8 MOST ASKED: Implement LFU Cache from Scratch
 * ============================================================================
 * Companies: Amazon, Google, Meta
 * LeetCode: #460 LFU Cache
 * Difficulty: Hard
 *
 * WHAT IS LFU?
 *   Least Frequently Used — evicts the item with the LOWEST access count.
 *   Tie-break: if two items have the same frequency, evict the LEAST RECENTLY used one (LRU).
 *
 * DATA STRUCTURES:
 *   1. keyToValue:  Map<key, value>           — O(1) value lookup
 *   2. keyToFreq:   Map<key, frequency>       — O(1) frequency lookup
 *   3. freqToKeys:  Map<freq, LinkedHashSet>  — O(1) find keys with same freq
 *                                                LinkedHashSet maintains insertion order (LRU tie-break)
 *   4. minFreq:     int                       — tracks the current minimum frequency
 *
 * get(key):
 *   1. Look up value in keyToValue → O(1)
 *   2. Increase frequency: remove from old freq set, add to new freq set
 *   3. Update minFreq if old freq set is now empty
 *
 * put(key, value):
 *   1. If exists → update value + increase frequency
 *   2. If full → evict key with minFreq (first element in LinkedHashSet = LRU among same freq)
 *   3. Add new key with freq=1, set minFreq=1
 *
 * TIME COMPLEXITY: O(1) for both get and put
 */
public class LFUCache {

    private final int capacity;
    private int minFreq;

    private final Map<Integer, Integer> keyToValue;         // key → value
    private final Map<Integer, Integer> keyToFreq;          // key → current frequency
    private final Map<Integer, LinkedHashSet<Integer>> freqToKeys;  // freq → set of keys (ordered by insertion = LRU)

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
        this.keyToValue = new HashMap<>();
        this.keyToFreq = new HashMap<>();
        this.freqToKeys = new HashMap<>();
    }

    // ============================================================
    // get(key) — O(1)
    // ============================================================
    public int get(int key) {
        if (!keyToValue.containsKey(key)) return -1;

        // Increase frequency of this key
        increaseFreq(key);

        return keyToValue.get(key);
    }

    // ============================================================
    // put(key, value) — O(1)
    // ============================================================
    public void put(int key, int value) {
        if (capacity == 0) return;

        // Case 1: Key exists → update value + increase frequency
        if (keyToValue.containsKey(key)) {
            keyToValue.put(key, value);
            increaseFreq(key);
            return;
        }

        // Case 2: Cache is full → evict the LFU key
        if (keyToValue.size() == capacity) {
            evict();
        }

        // Case 3: Add new key
        keyToValue.put(key, value);
        keyToFreq.put(key, 1);
        freqToKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        minFreq = 1;  // new key always has freq=1 → minFreq is always 1 after insert
    }

    // ============================================================
    // increaseFreq — move key from freq bucket to freq+1 bucket
    // ============================================================
    private void increaseFreq(int key) {
        int freq = keyToFreq.get(key);
        int newFreq = freq + 1;

        // Update frequency
        keyToFreq.put(key, newFreq);

        // Remove from old frequency set
        LinkedHashSet<Integer> oldSet = freqToKeys.get(freq);
        oldSet.remove(key);

        // If old freq set is empty AND it was the minFreq → increment minFreq
        if (oldSet.isEmpty()) {
            freqToKeys.remove(freq);
            if (freq == minFreq) {
                minFreq++;
            }
        }

        // Add to new frequency set
        freqToKeys.computeIfAbsent(newFreq, k -> new LinkedHashSet<>()).add(key);
    }

    // ============================================================
    // evict — remove the LFU key (tie-break: LRU among same frequency)
    // ============================================================
    private void evict() {
        // Get the set of keys with minimum frequency
        LinkedHashSet<Integer> minFreqKeys = freqToKeys.get(minFreq);

        // First element of LinkedHashSet = OLDEST (LRU tie-break)
        int evictKey = minFreqKeys.iterator().next();

        // Remove from all maps
        minFreqKeys.remove(evictKey);
        if (minFreqKeys.isEmpty()) {
            freqToKeys.remove(minFreq);
        }
        keyToValue.remove(evictKey);
        keyToFreq.remove(evictKey);

        System.out.println("  [Evicted] key=" + evictKey + " (freq=" + minFreq + ")");
    }

    // ============================================================
    // DEMO
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== LFU Cache Implementation ===\n");

        LFUCache cache = new LFUCache(3);

        cache.put(1, 10);   // freq: {1:1}
        cache.put(2, 20);   // freq: {1:1, 2:1}
        cache.put(3, 30);   // freq: {1:1, 2:1, 3:1}

        System.out.println("get(1) = " + cache.get(1));  // returns 10, freq of 1 → 2
        System.out.println("get(2) = " + cache.get(2));  // returns 20, freq of 2 → 2
        System.out.println("get(1) = " + cache.get(1));  // returns 10, freq of 1 → 3

        // Frequencies: {1:3, 2:2, 3:1} → minFreq=1 → key 3 is LFU
        System.out.println("\nput(4, 40) — cache full, evicts key 3 (freq=1, least frequent)");
        cache.put(4, 40);

        System.out.println("get(3) = " + cache.get(3));  // -1 (evicted)
        System.out.println("get(4) = " + cache.get(4));  // 40

        // Now freqs: {1:3, 2:2, 4:2} → minFreq=1 → key 4 has freq 2
        System.out.println("\nput(5, 50) — evicts least frequent");
        cache.put(5, 50);

        System.out.println("get(2) = " + cache.get(2));  // depends on which was LFU

        System.out.println("\n=== Complexity ===");
        System.out.println("get/put: O(1)");
        System.out.println("Three HashMaps: keyToValue, keyToFreq, freqToKeys");
        System.out.println("LinkedHashSet: maintains insertion order for LRU tie-break");
    }
}

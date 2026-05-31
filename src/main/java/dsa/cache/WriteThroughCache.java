package dsa.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================================
 * Implement Write-Through / Write-Back Cache
 * ============================================================================
 * Companies: Amazon, Google | Difficulty: Hard
 *
 * Write-Through: writes to cache AND backing store simultaneously.
 *   + Data always consistent. - Higher write latency.
 *
 * Write-Back (Write-Behind): writes to cache only, flushes to store later.
 *   + Lower write latency. - Risk of data loss before flush.
 *   Tracks dirty entries, flushes in batch or on eviction.
 */
public class WriteThroughCache<K, V> {

    // Simulated backing store (DB)
    private final Map<K, V> backingStore = new HashMap<>();
    private final Map<K, V> cache = new HashMap<>();
    private final Map<K, Boolean> dirty = new HashMap<>(); // for write-back
    private final int capacity;
    private final boolean writeBack;

    public WriteThroughCache(int capacity, boolean writeBack) {
        this.capacity = capacity;
        this.writeBack = writeBack;
    }

    public V get(K key) {
        // Check cache first
        if (cache.containsKey(key)) {
            System.out.println("  [Cache HIT] " + key);
            return cache.get(key);
        }
        // Cache miss — read from backing store
        V value = backingStore.get(key);
        if (value != null) {
            System.out.println("  [Cache MISS] " + key + " — loaded from store");
            cache.put(key, value);
        }
        return value;
    }

    public void put(K key, V value) {
        if (writeBack) {
            // Write-Back: write to cache only, mark dirty
            cache.put(key, value);
            dirty.put(key, true);
            System.out.println("  [Write-Back] " + key + " → cache only (dirty)");
        } else {
            // Write-Through: write to BOTH cache and store
            cache.put(key, value);
            backingStore.put(key, value);
            System.out.println("  [Write-Through] " + key + " → cache + store");
        }
    }

    // Flush dirty entries to backing store (write-back only)
    public void flush() {
        for (K key : dirty.keySet()) {
            backingStore.put(key, cache.get(key));
            System.out.println("  [Flush] " + key + " → store");
        }
        dirty.clear();
    }

    public static void main(String[] args) {
        System.out.println("=== Write-Through Cache ===");
        WriteThroughCache<String, String> wt = new WriteThroughCache<>(10, false);
        wt.put("user:1", "Karan");
        wt.put("user:2", "Alice");
        wt.get("user:1");

        System.out.println("\n=== Write-Back Cache ===");
        WriteThroughCache<String, String> wb = new WriteThroughCache<>(10, true);
        wb.put("user:1", "Karan");
        wb.put("user:2", "Alice");
        System.out.println("\nFlushing dirty entries:");
        wb.flush();
    }
}

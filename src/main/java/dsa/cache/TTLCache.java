package dsa.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================================
 * Implement TTL Cache (Cache with Expiry)
 * ============================================================================
 * Companies: Amazon, Google, Microsoft | Difficulty: Medium-Hard
 *
 * Each entry has a time-to-live (TTL). After TTL expires, entry is invalid.
 * Lazy expiration: check on get(), don't actively scan for expired entries.
 * O(1) get/put with HashMap.
 */
public class TTLCache<K, V> {

    private static class Entry<V> {
        V value;
        long expiryTime; // absolute time when entry expires
        Entry(V value, long expiryTime) { this.value = value; this.expiryTime = expiryTime; }
        boolean isExpired() { return System.currentTimeMillis() > expiryTime; }
    }

    private final Map<K, Entry<V>> map = new HashMap<>();
    private final long defaultTtlMs;

    public TTLCache(long defaultTtlMs) { this.defaultTtlMs = defaultTtlMs; }

    public void put(K key, V value) { put(key, value, defaultTtlMs); }

    public void put(K key, V value, long ttlMs) {
        map.put(key, new Entry<>(value, System.currentTimeMillis() + ttlMs));
    }

    public V get(K key) {
        Entry<V> entry = map.get(key);
        if (entry == null) return null;
        if (entry.isExpired()) { map.remove(key); return null; } // lazy expiration
        return entry.value;
    }

    public boolean containsKey(K key) { return get(key) != null; }
    public int size() { cleanup(); return map.size(); }

    // Active cleanup — remove all expired entries
    private void cleanup() {
        map.entrySet().removeIf(e -> e.getValue().isExpired());
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== TTL Cache Implementation ===\n");
        TTLCache<String, String> cache = new TTLCache<>(2000); // 2 second default TTL

        cache.put("key1", "value1");
        cache.put("key2", "value2", 500); // 500ms TTL
        System.out.println("get(key1)=" + cache.get("key1")); // value1
        System.out.println("get(key2)=" + cache.get("key2")); // value2

        Thread.sleep(600);
        System.out.println("\nAfter 600ms:");
        System.out.println("get(key1)=" + cache.get("key1")); // value1 (2s TTL)
        System.out.println("get(key2)=" + cache.get("key2")); // null (expired at 500ms)

        Thread.sleep(1500);
        System.out.println("\nAfter 2.1s:");
        System.out.println("get(key1)=" + cache.get("key1")); // null (expired)
    }
}

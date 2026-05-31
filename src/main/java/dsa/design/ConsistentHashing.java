package dsa.design;

import java.util.*;

/**
 * ============================================================================
 * Implement Consistent Hashing Ring
 * ============================================================================
 * Companies: Google, Amazon, Meta | Difficulty: Hard
 *
 * Distributes keys across nodes with minimal redistribution when nodes are added/removed.
 * Uses a sorted ring (TreeMap). Virtual nodes improve balance.
 *
 * Without consistent hashing: hash(key) % N → adding/removing a server moves ~all keys.
 * With consistent hashing: adding/removing moves only ~1/N keys.
 */
public class ConsistentHashing {

    private final TreeMap<Integer, String> ring = new TreeMap<>();
    private final int virtualNodes;

    public ConsistentHashing(int virtualNodes) { this.virtualNodes = virtualNodes; }

    public void addNode(String node) {
        for (int i = 0; i < virtualNodes; i++) {
            int hash = hash(node + "-vn" + i);
            ring.put(hash, node);
        }
        System.out.println("  Added " + node + " (" + virtualNodes + " virtual nodes)");
    }

    public void removeNode(String node) {
        for (int i = 0; i < virtualNodes; i++) {
            ring.remove(hash(node + "-vn" + i));
        }
        System.out.println("  Removed " + node);
    }

    public String getNode(String key) {
        if (ring.isEmpty()) return null;
        int hash = hash(key);
        // Find the first node clockwise from the key's hash
        Map.Entry<Integer, String> entry = ring.ceilingEntry(hash);
        if (entry == null) entry = ring.firstEntry(); // wrap around
        return entry.getValue();
    }

    private int hash(String key) { return Math.abs(key.hashCode()); }

    public static void main(String[] args) {
        System.out.println("=== Consistent Hashing ===\n");
        ConsistentHashing ch = new ConsistentHashing(3);
        ch.addNode("Server-A");
        ch.addNode("Server-B");
        ch.addNode("Server-C");

        String[] keys = {"user:1", "user:2", "user:3", "order:100", "order:200"};
        System.out.println("\nKey → Node mapping:");
        for (String key : keys) System.out.println("  " + key + " → " + ch.getNode(key));

        System.out.println("\nRemove Server-B:");
        ch.removeNode("Server-B");
        for (String key : keys) System.out.println("  " + key + " → " + ch.getNode(key));
        System.out.println("\nOnly keys from Server-B moved — minimal redistribution!");
    }
}

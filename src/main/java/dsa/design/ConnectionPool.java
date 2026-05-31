package dsa.design;

import java.util.concurrent.*;

/**
 * ============================================================================
 * Implement Connection Pool
 * ============================================================================
 * Companies: Amazon, Microsoft, Google | Difficulty: Hard
 *
 * Fixed pool of reusable connections. getConnection() blocks if none available.
 * Uses Semaphore to limit concurrent access + BlockingQueue for connection storage.
 */
public class ConnectionPool {

    static class Connection {
        private final int id;
        private boolean inUse;
        Connection(int id) { this.id = id; }
        public String toString() { return "Connection-" + id; }
    }

    private final BlockingQueue<Connection> pool;
    private final Semaphore semaphore;

    public ConnectionPool(int size) {
        pool = new LinkedBlockingQueue<>(size);
        semaphore = new Semaphore(size);
        for (int i = 1; i <= size; i++) pool.offer(new Connection(i));
    }

    public Connection getConnection() throws InterruptedException {
        semaphore.acquire(); // blocks if no permits
        Connection conn = pool.poll();
        System.out.println("  [Pool] Acquired " + conn + " (available=" + semaphore.availablePermits() + ")");
        return conn;
    }

    public void releaseConnection(Connection conn) {
        pool.offer(conn);
        semaphore.release();
        System.out.println("  [Pool] Released " + conn + " (available=" + semaphore.availablePermits() + ")");
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Connection Pool ===\n");
        ConnectionPool pool = new ConnectionPool(2);

        // 4 threads competing for 2 connections
        for (int i = 1; i <= 4; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    Connection conn = pool.getConnection();
                    Thread.sleep(300); // simulate work
                    pool.releaseConnection(conn);
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }, "Thread-" + id).start();
        }
        Thread.sleep(2000);
        System.out.println("\nSemaphore limits concurrent access, BlockingQueue stores connections");
    }
}

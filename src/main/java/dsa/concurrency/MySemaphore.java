package dsa.concurrency;

/**
 * ============================================================================
 * Implement Semaphore from Scratch
 * ============================================================================
 * Companies: Google, Amazon, Microsoft | Difficulty: Medium-Hard
 *
 * Controls access to a shared resource with a limited number of permits.
 * acquire() takes a permit (blocks if none available).
 * release() returns a permit.
 * Use case: connection pool, rate limiting, thread throttling.
 */
public class MySemaphore {

    private int permits;

    public MySemaphore(int permits) { this.permits = permits; }

    public synchronized void acquire() throws InterruptedException {
        while (permits == 0) { wait(); }
        permits--;
    }

    public synchronized void release() {
        permits++;
        notify(); // wake one waiting thread
    }

    public synchronized boolean tryAcquire() {
        if (permits > 0) { permits--; return true; }
        return false;
    }

    public synchronized int availablePermits() { return permits; }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Semaphore Implementation ===\n");
        MySemaphore sem = new MySemaphore(2); // only 2 concurrent accesses

        for (int i = 1; i <= 5; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    sem.acquire();
                    System.out.println("  Thread " + id + " acquired (permits=" + sem.availablePermits() + ")");
                    Thread.sleep(300);
                    sem.release();
                    System.out.println("  Thread " + id + " released");
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }).start();
        }

        Thread.sleep(2000);
        System.out.println("\nOnly 2 threads run concurrently — others wait for permits");
    }
}

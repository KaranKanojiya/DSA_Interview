package dsa.concurrency;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ============================================================================
 * #5 MOST ASKED: Implement BlockingQueue (Bounded Buffer)
 * ============================================================================
 * Companies: Amazon, Google, Microsoft, Apple
 * LeetCode: #1188 Design Bounded Blocking Queue
 * Difficulty: Hard
 *
 * WHAT IS A BLOCKING QUEUE?
 *   A thread-safe queue with blocking behavior:
 *     put(item)  → blocks if queue is FULL (waits until space available)
 *     take()     → blocks if queue is EMPTY (waits until item available)
 *
 *   This is the foundation of the PRODUCER-CONSUMER pattern.
 *
 * IMPLEMENTATION:
 *   ReentrantLock + TWO Conditions:
 *     notFull:  producers wait here when queue is full
 *     notEmpty: consumers wait here when queue is empty
 *
 *   Why TWO conditions (not just one)?
 *     With one condition + notifyAll(): wake ALL threads (producers AND consumers)
 *     → Wasteful — only producers need to wake when space is available
 *     With two conditions: signal() wakes only the RIGHT type of thread
 *
 * WHY NOT synchronized + wait/notify?
 *   synchronized only has ONE wait set — can't separate producers from consumers
 *   Lock + Condition gives MULTIPLE wait sets — targeted signaling
 *
 * TIME COMPLEXITY: O(1) for put and take (excluding wait time)
 */
public class MyBlockingQueue<E> {

    private final Queue<E> queue;
    private final int capacity;

    // ReentrantLock — explicit lock (more flexible than synchronized)
    private final ReentrantLock lock = new ReentrantLock();

    // Two conditions for targeted signaling:
    private final Condition notFull = lock.newCondition();   // producers wait here
    private final Condition notEmpty = lock.newCondition();  // consumers wait here

    public MyBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    // ============================================================
    // put(item) — blocks if full
    // ============================================================
    // Called by PRODUCER threads
    public void put(E item) throws InterruptedException {
        lock.lock();
        try {
            // WHILE loop (not IF) — guard against spurious wakeups
            // Spurious wakeup: thread wakes up without signal → must re-check condition
            while (queue.size() == capacity) {
                System.out.println("  [Producer] Queue full, waiting...");
                notFull.await();  // release lock + wait → re-acquire lock when signaled
            }

            queue.add(item);
            System.out.println("  [Producer] Added: " + item + " | size=" + queue.size());

            // Signal ONE waiting consumer — "there's an item for you"
            notEmpty.signal();
        } finally {
            lock.unlock();  // ALWAYS unlock in finally
        }
    }

    // ============================================================
    // take() — blocks if empty
    // ============================================================
    // Called by CONSUMER threads
    public E take() throws InterruptedException {
        lock.lock();
        try {
            // Wait while empty
            while (queue.isEmpty()) {
                System.out.println("  [Consumer] Queue empty, waiting...");
                notEmpty.await();  // release lock + wait → re-acquire when signaled
            }

            E item = queue.poll();
            System.out.println("  [Consumer] Took: " + item + " | size=" + queue.size());

            // Signal ONE waiting producer — "there's space for you"
            notFull.signal();

            return item;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try { return queue.size(); }
        finally { lock.unlock(); }
    }

    // ============================================================
    // DEMO: Producer-Consumer
    // ============================================================
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== BlockingQueue Implementation ===\n");

        MyBlockingQueue<Integer> bq = new MyBlockingQueue<>(3);

        // Producer thread — produces 6 items
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 6; i++) {
                    bq.put(i);
                    Thread.sleep(100);  // simulate work
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "Producer");

        // Consumer thread — consumes items (slower than producer)
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 6; i++) {
                    bq.take();
                    Thread.sleep(300);  // slower — queue will fill up
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "Consumer");

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("\nFinal queue size: " + bq.size());

        System.out.println("\n=== Key Points ===");
        System.out.println("1. ReentrantLock + Condition > synchronized + wait/notify");
        System.out.println("2. TWO conditions: notFull (producers), notEmpty (consumers)");
        System.out.println("3. WHILE loop, not IF — guard against spurious wakeups");
        System.out.println("4. Always unlock in finally block");
    }
}

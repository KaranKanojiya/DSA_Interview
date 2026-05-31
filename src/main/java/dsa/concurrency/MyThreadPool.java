package dsa.concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ============================================================================
 * #6 MOST ASKED: Implement Thread Pool from Scratch
 * ============================================================================
 * Companies: Amazon, Google, Microsoft
 * Difficulty: Hard
 *
 * WHAT IS A THREAD POOL?
 *   A fixed set of worker threads that pull tasks from a shared queue.
 *   Instead of creating a new thread per task (expensive), you REUSE threads.
 *
 *   submit(task) → task goes into queue → idle worker picks it up → executes
 *
 * WHY THREAD POOL?
 *   Creating/destroying threads is expensive (OS context switch, memory allocation).
 *   Thread pool amortizes this cost — threads are created once and reused.
 *
 * COMPONENTS:
 *   1. Task queue (BlockingQueue) — holds submitted tasks
 *   2. Worker threads — continuously pull tasks from queue
 *   3. shutdown() — stop accepting new tasks, finish existing ones
 *
 * HOW WORKERS WORK:
 *   while (running) {
 *     task = queue.take();   // blocks if no tasks available (worker waits)
 *     task.run();            // execute the task
 *   }
 *
 * TIME COMPLEXITY: O(1) for submit (add to queue)
 */
public class MyThreadPool {

    private final Worker[] workers;
    private final BlockingQueue<Runnable> taskQueue;
    private final AtomicBoolean isShutdown;

    // ============================================================
    // Worker — a thread that continuously pulls and executes tasks
    // ============================================================
    private static class Worker extends Thread {
        private final BlockingQueue<Runnable> queue;
        private final AtomicBoolean isShutdown;

        Worker(int id, BlockingQueue<Runnable> queue, AtomicBoolean isShutdown) {
            super("Worker-" + id);
            this.queue = queue;
            this.isShutdown = isShutdown;
            this.setDaemon(true);  // daemon threads don't prevent JVM shutdown
        }

        @Override
        public void run() {
            // Worker loop: pull tasks and execute them
            while (!isShutdown.get() || !queue.isEmpty()) {
                try {
                    // take() blocks if queue is empty — worker waits for new task
                    Runnable task = queue.poll(100, java.util.concurrent.TimeUnit.MILLISECONDS);
                    if (task != null) {
                        System.out.println("  [" + getName() + "] Executing task");
                        task.run();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println("  [" + getName() + "] Task failed: " + e.getMessage());
                    // Worker continues — don't die on task failure
                }
            }
            System.out.println("  [" + getName() + "] Stopped");
        }
    }

    // ============================================================
    // Constructor — create pool with fixed number of workers
    // ============================================================
    public MyThreadPool(int poolSize) {
        this.taskQueue = new LinkedBlockingQueue<>();
        this.isShutdown = new AtomicBoolean(false);
        this.workers = new Worker[poolSize];

        // Create and start worker threads
        for (int i = 0; i < poolSize; i++) {
            workers[i] = new Worker(i, taskQueue, isShutdown);
            workers[i].start();
        }
        System.out.println("ThreadPool created with " + poolSize + " workers\n");
    }

    // ============================================================
    // submit(task) — add task to queue
    // ============================================================
    public void submit(Runnable task) {
        if (isShutdown.get()) {
            throw new IllegalStateException("ThreadPool is shutdown, cannot submit new tasks");
        }
        taskQueue.offer(task);
    }

    // ============================================================
    // shutdown() — stop accepting new tasks, wait for existing to finish
    // ============================================================
    public void shutdown() {
        isShutdown.set(true);
        System.out.println("\nShutdown requested — waiting for tasks to complete...");
    }

    // Wait for all workers to finish
    public void awaitTermination() throws InterruptedException {
        for (Worker worker : workers) {
            worker.join(5000);  // wait up to 5 seconds per worker
        }
    }

    // ============================================================
    // DEMO
    // ============================================================
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread Pool Implementation ===\n");

        MyThreadPool pool = new MyThreadPool(3);

        // Submit 8 tasks to pool of 3 workers
        for (int i = 1; i <= 8; i++) {
            final int taskId = i;
            pool.submit(() -> {
                System.out.println("  Task " + taskId + " running on " + Thread.currentThread().getName());
                try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                System.out.println("  Task " + taskId + " completed");
            });
        }

        // Shutdown — no new tasks accepted, existing tasks finish
        Thread.sleep(500);
        pool.shutdown();
        pool.awaitTermination();

        System.out.println("\nAll tasks completed.");

        System.out.println("\n=== Key Points ===");
        System.out.println("1. Workers loop: take() from queue → run() → repeat");
        System.out.println("2. BlockingQueue: take() blocks when empty (worker waits)");
        System.out.println("3. shutdown(): stop accepting, finish existing tasks");
        System.out.println("4. Workers catch exceptions — don't die on task failure");
    }
}

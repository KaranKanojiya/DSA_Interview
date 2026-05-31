package dsa.concurrency;

/**
 * ============================================================================
 * Implement CountDownLatch from Scratch
 * ============================================================================
 * Companies: Google, Amazon | Difficulty: Medium-Hard
 *
 * One-shot synchronizer: threads wait until count reaches zero.
 * countDown() decrements. await() blocks until count is 0.
 * Cannot be reset (unlike CyclicBarrier).
 */
public class MyCountDownLatch {

    private int count;

    public MyCountDownLatch(int count) {
        if (count < 0) throw new IllegalArgumentException();
        this.count = count;
    }

    public synchronized void countDown() {
        if (count > 0) {
            count--;
            if (count == 0) notifyAll(); // wake all waiting threads
        }
    }

    public synchronized void await() throws InterruptedException {
        while (count > 0) { wait(); }
    }

    public synchronized int getCount() { return count; }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== CountDownLatch Implementation ===\n");
        MyCountDownLatch latch = new MyCountDownLatch(3);

        // 3 worker threads count down
        for (int i = 1; i <= 3; i++) {
            final int id = i;
            new Thread(() -> {
                try { Thread.sleep(id * 200); } catch (InterruptedException e) {}
                System.out.println("  Worker " + id + " done → countDown()");
                latch.countDown();
            }).start();
        }

        System.out.println("Main thread waiting...");
        latch.await();
        System.out.println("All workers done — main thread continues!");
    }
}

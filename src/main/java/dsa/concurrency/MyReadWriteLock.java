package dsa.concurrency;

/**
 * ============================================================================
 * Implement ReadWriteLock from Scratch
 * ============================================================================
 * Companies: Google, Amazon, Microsoft | Difficulty: Hard
 *
 * Multiple concurrent readers allowed. Exclusive writer access.
 * Readers block only when a writer holds the lock.
 * Writer blocks when any reader OR writer holds the lock.
 */
public class MyReadWriteLock {

    private int readers = 0;
    private boolean writerActive = false;
    private int writersWaiting = 0; // prevent writer starvation

    public synchronized void readLock() throws InterruptedException {
        while (writerActive || writersWaiting > 0) { wait(); }
        readers++;
    }

    public synchronized void readUnlock() {
        readers--;
        if (readers == 0) notifyAll();
    }

    public synchronized void writeLock() throws InterruptedException {
        writersWaiting++;
        while (readers > 0 || writerActive) { wait(); }
        writersWaiting--;
        writerActive = true;
    }

    public synchronized void writeUnlock() {
        writerActive = false;
        notifyAll();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== ReadWriteLock Implementation ===\n");
        MyReadWriteLock lock = new MyReadWriteLock();
        var sharedData = new int[]{0};

        // 3 readers + 1 writer
        Runnable reader = () -> {
            try {
                lock.readLock();
                System.out.println("  [Reader " + Thread.currentThread().getName() + "] read: " + sharedData[0]);
                Thread.sleep(100);
                lock.readUnlock();
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        };

        Runnable writer = () -> {
            try {
                lock.writeLock();
                sharedData[0]++;
                System.out.println("  [Writer] wrote: " + sharedData[0]);
                Thread.sleep(100);
                lock.writeUnlock();
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        };

        Thread r1 = new Thread(reader, "R1"), r2 = new Thread(reader, "R2"), r3 = new Thread(reader, "R3");
        Thread w1 = new Thread(writer, "W1");

        r1.start(); r2.start(); w1.start(); r3.start();
        r1.join(); r2.join(); r3.join(); w1.join();

        System.out.println("\nMultiple readers concurrent, writer exclusive");
    }
}

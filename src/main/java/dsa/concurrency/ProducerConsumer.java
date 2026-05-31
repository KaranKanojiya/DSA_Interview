package dsa.concurrency;

import java.util.LinkedList;
import java.util.Queue;

/**
 * ============================================================================
 * Implement Producer-Consumer Pattern
 * ============================================================================
 * Companies: Amazon, Microsoft, Google | Difficulty: Medium
 *
 * Shared bounded buffer with synchronized access.
 * Producer waits when buffer full. Consumer waits when buffer empty.
 * Uses wait/notify (simpler alternative to Lock/Condition shown in BlockingQueue).
 */
public class ProducerConsumer {

    static class BoundedBuffer<E> {
        private final Queue<E> queue = new LinkedList<>();
        private final int capacity;

        BoundedBuffer(int capacity) { this.capacity = capacity; }

        synchronized void produce(E item) throws InterruptedException {
            while (queue.size() == capacity) { wait(); }
            queue.add(item);
            System.out.println("  Produced: " + item + " | size=" + queue.size());
            notifyAll();
        }

        synchronized E consume() throws InterruptedException {
            while (queue.isEmpty()) { wait(); }
            E item = queue.poll();
            System.out.println("  Consumed: " + item + " | size=" + queue.size());
            notifyAll();
            return item;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Producer-Consumer Pattern ===\n");
        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(3);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 6; i++) { buffer.produce(i); Thread.sleep(50); }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 6; i++) { buffer.consume(); Thread.sleep(150); }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        producer.start(); consumer.start();
        producer.join(); consumer.join();
        System.out.println("\nProducer waits when full, consumer waits when empty");
    }
}

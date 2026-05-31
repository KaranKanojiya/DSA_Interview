package dsa.design;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * ============================================================================
 * Implement Pub-Sub (Publish-Subscribe) System
 * ============================================================================
 * Companies: Amazon, Google, Meta | Difficulty: Medium-Hard
 *
 * Topic-based message routing. Publishers and subscribers are decoupled.
 * Supports async delivery via thread pool.
 */
public class PubSubSystem {

    private final Map<String, List<Consumer<String>>> subscribers = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    public void subscribe(String topic, Consumer<String> handler) {
        subscribers.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(handler);
    }

    public void publish(String topic, String message) {
        for (Consumer<String> handler : subscribers.getOrDefault(topic, List.of())) {
            executor.submit(() -> handler.accept(message));
        }
    }

    public void shutdown() { executor.shutdown(); }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Pub-Sub System ===\n");
        PubSubSystem bus = new PubSubSystem();

        bus.subscribe("orders", msg -> System.out.println("  [OrderService] " + msg));
        bus.subscribe("orders", msg -> System.out.println("  [EmailService] " + msg));
        bus.subscribe("payments", msg -> System.out.println("  [PaymentService] " + msg));

        bus.publish("orders", "New order #100");
        bus.publish("payments", "Payment $50 received");
        bus.publish("orders", "Order #101 cancelled");

        Thread.sleep(500);
        bus.shutdown();
        System.out.println("\nAsync delivery via thread pool, topic-based routing");
    }
}

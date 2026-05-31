package dsa.design;

import java.util.*;

/**
 * ============================================================================
 * Implement Observer Pattern (Observable/Observer)
 * ============================================================================
 * Companies: Amazon, Google, Microsoft | Difficulty: Medium
 *
 * Subject maintains a list of observers and notifies them of state changes.
 * Decouples the subject from its dependents.
 */
public class ObserverPattern {

    interface Observer { void update(String event, Object data); }

    static class EventEmitter {
        private final Map<String, List<Observer>> listeners = new HashMap<>();

        void subscribe(String event, Observer observer) {
            listeners.computeIfAbsent(event, k -> new ArrayList<>()).add(observer);
        }

        void unsubscribe(String event, Observer observer) {
            List<Observer> obs = listeners.get(event);
            if (obs != null) obs.remove(observer);
        }

        void emit(String event, Object data) {
            for (Observer obs : listeners.getOrDefault(event, List.of())) {
                obs.update(event, data);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Observer Pattern ===\n");
        EventEmitter emitter = new EventEmitter();

        Observer logger = (event, data) -> System.out.println("  [Logger] " + event + ": " + data);
        Observer analytics = (event, data) -> System.out.println("  [Analytics] " + event + " tracked");

        emitter.subscribe("order.created", logger);
        emitter.subscribe("order.created", analytics);
        emitter.subscribe("user.login", logger);

        emitter.emit("order.created", "Order #123");
        emitter.emit("user.login", "karan@test.com");

        emitter.unsubscribe("order.created", analytics);
        System.out.println("\nAfter unsubscribe analytics:");
        emitter.emit("order.created", "Order #456");
    }
}

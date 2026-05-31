package dsa.design;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * ============================================================================
 * Implement Event Bus / Event Emitter
 * ============================================================================
 * Companies: Meta, Google, Amazon | Difficulty: Medium
 *
 * on(event, callback), emit(event, data), off(event, callback), once(event, callback)
 * Synchronous dispatch. Thread-safe with CopyOnWriteArrayList.
 */
public class EventBus {

    private final Map<String, List<Consumer<Object>>> listeners = new HashMap<>();

    public void on(String event, Consumer<Object> callback) {
        listeners.computeIfAbsent(event, k -> new CopyOnWriteArrayList<>()).add(callback);
    }

    public void off(String event, Consumer<Object> callback) {
        List<Consumer<Object>> cbs = listeners.get(event);
        if (cbs != null) cbs.remove(callback);
    }

    public void once(String event, Consumer<Object> callback) {
        Consumer<Object> wrapper = new Consumer<>() {
            public void accept(Object data) {
                callback.accept(data);
                off(event, this);
            }
        };
        on(event, wrapper);
    }

    public void emit(String event, Object data) {
        for (Consumer<Object> cb : listeners.getOrDefault(event, List.of())) {
            cb.accept(data);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Event Bus ===\n");
        EventBus bus = new EventBus();

        bus.on("click", data -> System.out.println("  [Listener 1] click: " + data));
        bus.on("click", data -> System.out.println("  [Listener 2] click: " + data));
        bus.once("init", data -> System.out.println("  [Once] init: " + data));

        bus.emit("click", "Button A");
        bus.emit("init", "first");
        bus.emit("init", "second — once listener already removed");

        System.out.println("\non/off/once/emit — synchronous event dispatch");
    }
}

package dsa.design;

import java.util.*;
import java.util.function.Function;

/**
 * ============================================================================
 * Implement CompletableFuture / Promise
 * ============================================================================
 * Companies: Google, Amazon | Difficulty: Hard
 *
 * A container for a value that may not be available yet.
 * Supports: complete(value), get(), thenApply(fn) for chaining.
 */
public class MyCompletableFuture<T> {

    private T result;
    private Exception error;
    private boolean completed = false;
    private final List<Function<T, ?>> callbacks = new ArrayList<>();

    public synchronized void complete(T value) {
        if (completed) throw new IllegalStateException("Already completed");
        this.result = value;
        this.completed = true;
        notifyAll();
        for (Function<T, ?> cb : callbacks) cb.apply(value);
    }

    public synchronized void completeExceptionally(Exception error) {
        this.error = error;
        this.completed = true;
        notifyAll();
    }

    public synchronized T get() throws Exception {
        while (!completed) wait();
        if (error != null) throw error;
        return result;
    }

    @SuppressWarnings("unchecked")
    public <U> MyCompletableFuture<U> thenApply(Function<T, U> fn) {
        MyCompletableFuture<U> next = new MyCompletableFuture<>();
        synchronized (this) {
            if (completed) {
                try { next.complete(fn.apply(result)); }
                catch (Exception e) { next.completeExceptionally(e); }
            } else {
                callbacks.add(val -> {
                    try { next.complete(fn.apply((T) val)); }
                    catch (Exception e) { next.completeExceptionally(e); }
                    return null;
                });
            }
        }
        return next;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== CompletableFuture / Promise ===\n");

        MyCompletableFuture<String> future = new MyCompletableFuture<>();

        // Chain transformations
        MyCompletableFuture<Integer> lengthFuture = future
                .thenApply(s -> { System.out.println("  Step 1: uppercase → " + s.toUpperCase()); return s.toUpperCase(); })
                .thenApply(s -> { System.out.println("  Step 2: length → " + s.length()); return s.length(); });

        // Complete async
        new Thread(() -> {
            try { Thread.sleep(500); } catch (InterruptedException e) {}
            System.out.println("  Completing with 'hello'...");
            future.complete("hello");
        }).start();

        System.out.println("  Waiting for result...");
        System.out.println("  Final result: " + lengthFuture.get()); // 5

        System.out.println("\ncomplete(value) → thenApply chain → get() blocks until done");
    }
}

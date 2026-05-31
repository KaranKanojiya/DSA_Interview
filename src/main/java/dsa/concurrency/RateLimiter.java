package dsa.concurrency;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ============================================================================
 * #3 MOST ASKED: Implement Rate Limiter
 * ============================================================================
 * Companies: Google, Amazon, Meta, Microsoft
 * Difficulty: Medium-Hard
 *
 * WHAT IS RATE LIMITING?
 *   Restrict the number of requests a client can make within a time window.
 *   Example: max 5 requests per second per user.
 *
 * TWO ALGORITHMS IMPLEMENTED HERE:
 *
 * 1. TOKEN BUCKET (recommended — used by AWS, Stripe):
 *    - Bucket holds tokens (max = capacity)
 *    - Tokens added at a fixed rate (e.g., 5 per second)
 *    - Each request consumes 1 token
 *    - If no tokens → request denied
 *    - Allows bursts up to bucket capacity
 *
 * 2. SLIDING WINDOW LOG:
 *    - Store timestamp of each request in a deque
 *    - On new request: remove timestamps outside the window
 *    - If count < limit → allow; else → deny
 *    - Exact counting but uses more memory
 *
 * COMPARISON:
 *   | Algorithm      | Burst  | Memory | Precision |
 *   |---------------|--------|--------|-----------|
 *   | Token Bucket  | Allows | O(1)   | Approximate |
 *   | Sliding Window| Smooth | O(n)   | Exact     |
 *   | Fixed Window  | Allows | O(1)   | Approximate |
 *
 * FOLLOW-UPS:
 *   - Distributed? → Redis + Lua script for atomic operations
 *   - Per-user? → Map<userId, RateLimiter>
 */
public class RateLimiter {

    // ============================================================
    // ALGORITHM 1: Token Bucket
    // ============================================================
    // Imagine a bucket that:
    //   - Holds at most `capacity` tokens
    //   - Tokens are added at `refillRate` per second
    //   - Each request takes 1 token
    //   - Empty bucket → request denied
    static class TokenBucket {
        private final int capacity;           // max tokens the bucket can hold
        private final double refillRate;      // tokens added per second
        private double tokens;                // current available tokens
        private long lastRefillTime;          // when we last added tokens

        TokenBucket(int capacity, double refillRate) {
            this.capacity = capacity;
            this.refillRate = refillRate;
            this.tokens = capacity;           // start full
            this.lastRefillTime = System.nanoTime();
        }

        // Returns true if request is allowed, false if rate-limited
        synchronized boolean allowRequest() {
            refill();  // add tokens based on elapsed time

            if (tokens >= 1.0) {
                tokens -= 1.0;    // consume one token
                return true;       // allowed
            }
            return false;          // denied — no tokens available
        }

        // Add tokens based on time elapsed since last refill
        // This is LAZY refill — we don't need a background thread
        private void refill() {
            long now = System.nanoTime();
            double elapsed = (now - lastRefillTime) / 1_000_000_000.0;  // convert to seconds
            double newTokens = elapsed * refillRate;                      // tokens to add

            // Add tokens but don't exceed capacity
            tokens = Math.min(capacity, tokens + newTokens);
            lastRefillTime = now;
        }

        double getTokens() { return tokens; }
    }

    // ============================================================
    // ALGORITHM 2: Sliding Window Log
    // ============================================================
    // Store exact timestamps of each request in a deque.
    // Remove timestamps older than the window on each request.
    // Count remaining → if count < limit, allow.
    static class SlidingWindowLog {
        private final int maxRequests;        // max requests allowed in the window
        private final long windowSizeMs;      // window size in milliseconds
        private final Deque<Long> timestamps; // timestamps of recent requests

        SlidingWindowLog(int maxRequests, long windowSizeMs) {
            this.maxRequests = maxRequests;
            this.windowSizeMs = windowSizeMs;
            this.timestamps = new ArrayDeque<>();
        }

        synchronized boolean allowRequest() {
            long now = System.currentTimeMillis();
            long windowStart = now - windowSizeMs;

            // Remove timestamps outside the window (older than windowStart)
            while (!timestamps.isEmpty() && timestamps.peekFirst() <= windowStart) {
                timestamps.pollFirst();
            }

            // Check if we're under the limit
            if (timestamps.size() < maxRequests) {
                timestamps.addLast(now);  // record this request
                return true;              // allowed
            }
            return false;                 // denied
        }

        int getCurrentCount() { return timestamps.size(); }
    }

    // ============================================================
    // DEMO
    // ============================================================
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Rate Limiter Implementation ===\n");

        // --- Token Bucket: 5 tokens, refill 2 per second ---
        System.out.println("=== Token Bucket (capacity=5, refill=2/sec) ===");
        TokenBucket bucket = new TokenBucket(5, 2.0);

        // Burst: use all 5 tokens quickly
        for (int i = 1; i <= 7; i++) {
            boolean allowed = bucket.allowRequest();
            System.out.printf("  Request %d: %s (tokens=%.1f)%n", i,
                    allowed ? "ALLOWED" : "DENIED", bucket.getTokens());
        }

        // Wait for refill
        System.out.println("  Waiting 1 second for refill...");
        Thread.sleep(1000);

        // After refill, should have ~2 new tokens
        for (int i = 8; i <= 10; i++) {
            boolean allowed = bucket.allowRequest();
            System.out.printf("  Request %d: %s (tokens=%.1f)%n", i,
                    allowed ? "ALLOWED" : "DENIED", bucket.getTokens());
        }

        // --- Sliding Window: 3 requests per 1 second ---
        System.out.println("\n=== Sliding Window Log (3 requests per 1 second) ===");
        SlidingWindowLog window = new SlidingWindowLog(3, 1000);

        for (int i = 1; i <= 5; i++) {
            boolean allowed = window.allowRequest();
            System.out.printf("  Request %d: %s (count=%d)%n", i,
                    allowed ? "ALLOWED" : "DENIED", window.getCurrentCount());
        }

        System.out.println("  Waiting 1.1 seconds for window to slide...");
        Thread.sleep(1100);

        for (int i = 6; i <= 8; i++) {
            boolean allowed = window.allowRequest();
            System.out.printf("  Request %d: %s (count=%d)%n", i,
                    allowed ? "ALLOWED" : "DENIED", window.getCurrentCount());
        }

        System.out.println("\n=== Comparison ===");
        System.out.println("Token Bucket:     allows bursts, O(1) memory, lazy refill");
        System.out.println("Sliding Window:   smooth rate, O(n) memory, exact counting");
    }
}

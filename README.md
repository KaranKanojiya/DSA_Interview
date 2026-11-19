# DSA Interview Prep -- Implement from Scratch

**"Build it yourself" interview questions — implement core data structures and system components without using Java built-in libraries.**

> **36 implementations** | **5 categories** | **2 cheatsheets** | **Java 21 + Maven** | **Top FAANG questions**

```
Category 1  [==========]  Data Structures    HashMap, LinkedList, ArrayList, Stack, Queue, Deque, Heap, Trie, BST, Graph   10 files
Category 2  [==========]  Cache              LRU, LFU, TTL, Write-Through/Back                                            4 files
Category 3  [==========]  Concurrency        ThreadPool, BlockingQueue, ReadWriteLock, RateLimiter, Semaphore, Latch       7 files
Category 4  [==========]  Design             Iterator, Observer, PubSub, ConnectionPool, Logger, Hashing, EventBus, Future 8 files
Category 5  [==========]  Algorithms         MergeSort, QuickSort, StringBuilder, Comparator, atoi, Streams, Builder       7 files
```

---

## Quick Start

```bash
cd DSA_Interview
mvn compile
java --enable-preview -cp target/classes dsa.cache.LRUCache
```

**From IntelliJ:** Right-click any file → **Run**

---

## All 36 Implementations

### Data Structures (10)

| # | Implementation | File | Key Concepts | Companies |
|---|---------------|------|-------------|-----------|
| 1 | HashMap | `MyHashMap.java` | Buckets, chaining, resize at 0.75 | Amazon, Google, Microsoft |
| 2 | LinkedList (Doubly) | `MyLinkedList.java` | addFirst/Last, remove, reverse | Amazon, Microsoft, Meta |
| 3 | ArrayList | `MyArrayList.java` | Auto-resize 1.5x, amortized O(1) | Amazon, Google |
| 4 | Stack + Min-Stack | `MyStack.java` | getMin() O(1) via parallel stack | Amazon, Microsoft |
| 5 | Queue using Stacks | `QueueUsingStacks.java` | Two stacks, lazy transfer | Amazon, Google, Meta |
| 6 | Deque / Circular Buffer | `CircularDeque.java` | Circular array, modular arithmetic | Google, Amazon |
| 7 | Min-Heap | `MinHeap.java` | Array tree, siftUp/siftDown | Google, Amazon, Meta |
| 8 | Trie | `Trie.java` | insert/search/startsWith/delete | Google, Meta, Amazon |
| 9 | BST | `BinarySearchTree.java` | Insert, delete (3 cases), validate | Amazon, Microsoft |
| 10 | Graph | `Graph.java` | Adjacency list, BFS, DFS, hasPath | Google, Meta, Amazon |

### Cache (4)

| # | Implementation | File | Key Concepts | Companies |
|---|---------------|------|-------------|-----------|
| 11 | **LRU Cache** | `LRUCache.java` | HashMap + Doubly Linked List, O(1) | **All FAANG** (#1 asked) |
| 12 | LFU Cache | `LFUCache.java` | 3 HashMaps + LinkedHashSet | Amazon, Google, Meta |
| 13 | TTL Cache | `TTLCache.java` | Lazy expiration, timestamps | Amazon, Google, Microsoft |
| 14 | Write-Through/Back | `WriteThroughCache.java` | Sync vs deferred writes | Amazon, Google |

### Concurrency (7)

| # | Implementation | File | Key Concepts | Companies |
|---|---------------|------|-------------|-----------|
| 15 | Thread Pool | `MyThreadPool.java` | Worker threads + task queue | Amazon, Google, Microsoft |
| 16 | BlockingQueue | `MyBlockingQueue.java` | Lock + 2 Conditions | Amazon, Google, Microsoft |
| 17 | ReadWriteLock | `MyReadWriteLock.java` | Multi-reader, exclusive writer | Google, Amazon |
| 18 | Rate Limiter | `RateLimiter.java` | Token Bucket + Sliding Window | Google, Amazon, Meta |
| 19 | Producer-Consumer | `ProducerConsumer.java` | Bounded buffer, wait/notify | Amazon, Microsoft |
| 20 | CountDownLatch | `MyCountDownLatch.java` | One-shot countdown | Google, Amazon |
| 21 | Semaphore | `MySemaphore.java` | Permit-based access control | Google, Amazon, Microsoft |

### Design Components (8)

| # | Implementation | File | Key Concepts | Companies |
|---|---------------|------|-------------|-----------|
| 22 | BST Iterator | `BSTIterator.java` | Stack-based in-order, O(h) space | Google, Meta, Apple |
| 23 | Observer Pattern | `ObserverPattern.java` | Subscribe/notify, event map | Amazon, Google |
| 24 | Pub-Sub System | `PubSubSystem.java` | Topic routing, async ExecutorService | Amazon, Google, Meta |
| 25 | Connection Pool | `ConnectionPool.java` | Semaphore + BlockingQueue | Amazon, Microsoft |
| 26 | Logger | `Logger.java` | Singleton, levels, appender strategy | Amazon, Microsoft |
| 27 | Consistent Hashing | `ConsistentHashing.java` | TreeMap ring, virtual nodes | Google, Amazon, Meta |
| 28 | Event Bus | `EventBus.java` | on/off/once/emit | Meta, Google |
| 29 | CompletableFuture | `MyCompletableFuture.java` | complete/get/thenApply chain | Google, Amazon |

### Algorithms & Java-Specific (7)

| # | Implementation | File | Key Concepts | Companies |
|---|---------------|------|-------------|-----------|
| 30 | MergeSort | `MergeSort.java` | Divide & conquer, stable, O(n log n) | Microsoft, Google |
| 31 | QuickSort | `QuickSort.java` | Lomuto partition, random pivot | Google, Microsoft |
| 32 | StringBuilder | `MyStringBuilder.java` | Dynamic char array, avoid O(n^2) concat | Amazon, Microsoft |
| 33 | Custom Comparator | `CustomComparator.java` | Comparable, Comparator, thenComparing | Amazon, Google, Meta |
| 34 | atoi | `MyAtoi.java` | Whitespace, sign, overflow handling | Amazon, Microsoft, Meta |
| 35 | Custom Stream Ops | `CustomStreamOps.java` | map/filter/reduce/flatMap pipeline | Google, Amazon |
| 36 | Immutable + Builder | `ImmutableClassBuilder.java` | final class, defensive copy, Builder | Amazon, Google, Apple |

---

## Interactive Study Canvases (36)

Each implementation has a companion **HTML study canvas** — open `canvas/index.html` in a browser or Cursor Live Preview.

Every canvas has **3 tabs**: **Concept** (diagrams, operations walkthrough), **Implementation** (annotated code), **Interview** (complexity table, follow-ups, edge cases).

```
canvas/
├── index.html                 Hub — 36 clickable tiles, 5 categories
├── assets/canvas.css          Shared dark-theme design system
├── assets/canvas.js           Tab switching, collapsibles, interactive simulators
├── cache/          (4)        LRU (interactive simulator), LFU (interactive simulator), TTL, Write-Through
├── data-structures/(10)       HashMap, LinkedList, ArrayList, Stack, Queue, Deque, MinHeap, Trie, BST, Graph
├── concurrency/    (7)        ThreadPool, BlockingQueue, RWLock, RateLimiter, ProducerConsumer, Latch, Semaphore
├── design/         (8)        BSTIterator, Observer, PubSub, ConnPool, Logger, ConsistentHashing, EventBus, CompletableFuture
└── algorithms/     (7)        MergeSort, QuickSort, StringBuilder, CustomComparator, atoi, StreamOps, ImmutableBuilder
```

**Features:** SVG architecture diagrams, syntax-highlighted Java code, interactive cache simulators (LRU/LFU), collapsible follow-up cards, responsive grid layout.

---

## Project Structure

```
src/main/java/dsa/
├── data_structures/    (10)  HashMap, LinkedList, ArrayList, Stack, Queue, Deque, Heap, Trie, BST, Graph
├── cache/              (4)   LRU, LFU, TTL, Write-Through
├── concurrency/        (7)   ThreadPool, BlockingQueue, RWLock, RateLimiter, ProducerConsumer, Latch, Semaphore
├── design/             (8)   Iterator, Observer, PubSub, ConnPool, Logger, ConsistentHash, EventBus, Future
└── algorithms/         (7)   MergeSort, QuickSort, StringBuilder, Comparator, atoi, Streams, Builder
```

---

## Top 10 (Study These First)

| Rank | Implementation | Why |
|------|---------------|-----|
| 1 | LRU Cache | #1 most asked across all FAANG |
| 2 | HashMap | Fundamentals — every interviewer expects this |
| 3 | Rate Limiter | System design + coding crossover |
| 4 | Trie | Autocomplete, spell check |
| 5 | BlockingQueue | Foundation of producer-consumer |
| 6 | Thread Pool | Concurrency fundamentals |
| 7 | Min-Heap | Top-K, merge K lists, Dijkstra |
| 8 | LFU Cache | Hard follow-up to LRU |
| 9 | BST Iterator | Iterator pattern + tree traversal |
| 10 | BST | Tree fundamentals, delete cases |

---

## Cheatsheets (Reference Docs)

In addition to the 36 implementations, this repo includes two reference documents for quick interview revision:

### [CHEATSHEET.md](CHEATSHEET.md) — Patterns & Templates

| Section | What's Covered |
|---------|---------------|
| Pattern Recognition | "I see X → I use Y" — 17 problem-to-pattern mappings |
| Array & String | Prefix sum, Kadane's, Dutch National Flag |
| Two Pointers | Opposite ends, same direction, Floyd's cycle |
| Sliding Window | Fixed window, variable window, minimum window |
| Binary Search | Standard, search on answer, first/last occurrence |
| Stack | Monotonic stack, valid parentheses |
| HashMap | Two Sum, frequency count, subarray sum = K |
| Linked List | Reverse, find middle, merge sorted |
| Trees | DFS (inorder/pre/post), height, validate BST, LCA, BFS, path sum |
| Graphs | BFS shortest path, DFS islands, topological sort, Dijkstra |
| Heap | Top K, merge K lists, running median (two heaps) |
| DP | Fibonacci, knapsack, LCS, coin change, house robber, LIS |
| Backtracking | Choose → explore → unchoose template |
| Greedy | Interval scheduling, jump game |
| Intervals | Merge, insert |
| Bit Manipulation | Count bits, single number, power of 2 |
| Complexity | Data structure + algorithm time/space table |
| Java Snippets | Sort, PQ, HashMap, String, 2D array, TreeMap |

### [ALGORITHMS.md](ALGORITHMS.md) — Must-Know Algorithms

| Section | Algorithms |
|---------|-----------|
| Sorting | MergeSort, QuickSort, HeapSort, Counting Sort, Bucket Sort |
| Searching | Binary Search (5 variants), Two Pointer, Exponential Search |
| Graph | BFS, DFS, Dijkstra, Bellman-Ford, Floyd-Warshall, Topological Sort, Union-Find, Kruskal's, Prim's |
| Tree | DFS traversals, Morris O(1) space, LCA, Serialize, AVL/RB |
| String | KMP, Rabin-Karp, Longest Palindrome, Trie-based |
| DP | 5-step framework + 10 must-know problems with recurrence table |
| Greedy | When it works + 8 classic problems |
| Math | GCD, Sieve of Eratosthenes, Fast Exponentiation, formulas |
| Bit Manipulation | 10 operations + 6 classic problems |
| Design | Consistent Hashing, Reservoir Sampling, Bloom Filter, Rate Limiting |
| Quick Reference | "What problem → what algorithm" decision table |

# DSA Interview Prep — Implement from Scratch

## What This Is
36 "build it yourself" interview questions — implement core data structures and system components without Java built-in libraries. Same format as Java_Interview: Maven, Java 21, self-contained main() methods.

## Tech Stack
- Java 21, Maven 3.9+, --enable-preview

## Build & Run
```bash
mvn compile
java --enable-preview -cp target/classes dsa.cache.LRUCache
```

## Structure (36 files)
```
dsa/data_structures/ (10) — HashMap, LinkedList, ArrayList, Stack, Queue, Deque, Heap, Trie, BST, Graph
dsa/cache/           (4)  — LRU, LFU, TTL, Write-Through/Back
dsa/concurrency/     (7)  — ThreadPool, BlockingQueue, RWLock, RateLimiter, ProducerConsumer, Latch, Semaphore
dsa/design/          (8)  — Iterator, Observer, PubSub, ConnPool, Logger, ConsistentHash, EventBus, Future
dsa/algorithms/      (7)  — MergeSort, QuickSort, StringBuilder, Comparator, atoi, Streams, Builder
```

## Conventions
- Each file = one "implement from scratch" question
- Javadoc with: what, why, companies, difficulty, complexity, follow-ups
- Step-by-step inline comments explaining every operation
- Runnable main() with demo output
- Same format as Java_Interview project

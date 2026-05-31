# DSA Interview Prep -- Implement from Scratch

**"Build it yourself" interview questions — implement core data structures and system components without using Java built-in libraries.**

> **10 implementations** | **4 categories** | **Java 21 + Maven** | **Top 10 most-asked at FAANG**

```
Category 1  [==========]  Cache              LRU, LFU                    2 files
Category 2  [==========]  Data Structures    HashMap, Trie, Heap, BST    4 files
Category 3  [==========]  Concurrency        BlockingQueue, ThreadPool, RateLimiter  3 files
Category 4  [==========]  Design             BST Iterator                1 file
```

---

## Quick Start

### Prerequisites

- **JDK 21+**
- **Maven 3.9+**

### Build & Run

```bash
cd DSA_Interview

# Compile all files
mvn compile

# Run any implementation
java --enable-preview -cp target/classes dsa.cache.LRUCache
java --enable-preview -cp target/classes dsa.data_structures.MyHashMap
java --enable-preview -cp target/classes dsa.concurrency.RateLimiter
java --enable-preview -cp target/classes dsa.data_structures.Trie
java --enable-preview -cp target/classes dsa.concurrency.MyBlockingQueue
java --enable-preview -cp target/classes dsa.concurrency.MyThreadPool
java --enable-preview -cp target/classes dsa.data_structures.MinHeap
java --enable-preview -cp target/classes dsa.cache.LFUCache
java --enable-preview -cp target/classes dsa.design.BSTIterator
java --enable-preview -cp target/classes dsa.data_structures.BinarySearchTree
```

**From IntelliJ IDEA:** Right-click any file → **Run**

---

## Top 10 Implementations (Ranked by Interview Frequency)

| # | Implementation | Category | Difficulty | Companies | Key Data Structure |
|---|---------------|----------|-----------|-----------|-------------------|
| 1 | **LRU Cache** | Cache | Medium | All FAANG | HashMap + Doubly Linked List |
| 2 | **HashMap** | Data Structures | Medium | Amazon, Google, Microsoft | Array of buckets + chaining |
| 3 | **Rate Limiter** | Concurrency | Medium-Hard | Google, Amazon, Meta | Token Bucket + Sliding Window |
| 4 | **Trie** | Data Structures | Medium | Google, Meta, Amazon | TrieNode with children[26] |
| 5 | **BlockingQueue** | Concurrency | Hard | Amazon, Google, Microsoft | Lock + 2 Conditions |
| 6 | **Thread Pool** | Concurrency | Hard | Amazon, Google, Microsoft | Workers + BlockingQueue |
| 7 | **Min-Heap** | Data Structures | Medium | Google, Amazon, Meta | Array-based complete tree |
| 8 | **LFU Cache** | Cache | Hard | Amazon, Google, Meta | 3 HashMaps + LinkedHashSet |
| 9 | **BST Iterator** | Design | Medium | Google, Meta, Apple | Stack-based in-order |
| 10 | **BST** | Data Structures | Medium | Amazon, Microsoft, Apple | Recursive insert/delete |

---

## Project Structure

```
src/main/java/dsa/
├── cache/
│   ├── LRUCache.java          # #1: HashMap + Doubly Linked List, O(1) get/put
│   └── LFUCache.java          # #8: 3 HashMaps + LinkedHashSet, O(1) get/put
│
├── data_structures/
│   ├── MyHashMap.java          # #2: Array of buckets, chaining, resize at 0.75 load
│   ├── Trie.java               # #4: Prefix tree, insert/search/startsWith/delete
│   ├── MinHeap.java            # #7: Array-based, siftUp/siftDown, O(log n) insert/extract
│   └── BinarySearchTree.java   # #10: Insert/search/delete (3 cases)/validate/traversal
│
├── concurrency/
│   ├── RateLimiter.java        # #3: Token Bucket + Sliding Window Log
│   ├── MyBlockingQueue.java    # #5: ReentrantLock + 2 Conditions (notFull, notEmpty)
│   └── MyThreadPool.java       # #6: Worker threads + task queue + shutdown
│
└── design/
    └── BSTIterator.java        # #9: Stack-based in-order traversal, O(1) avg next()
```

---

## What Each File Contains

Every file follows the same pattern (like Java_Interview):
1. **Javadoc header** — what it is, why it's asked, companies, difficulty, complexity
2. **Step-by-step comments** — how each method works internally
3. **Runnable main()** — demo with output showing the implementation working
4. **Follow-up questions** — what interviewers ask next (thread-safe, distributed, etc.)
5. **Complexity analysis** — time and space for each operation

---

## Remaining 28 Implementations (to be built)

### Core Data Structures
- LinkedList (Singly + Doubly)
- ArrayList (Dynamic Array)
- Stack (+ Min-Stack)
- Queue using Stacks
- Deque / Circular Buffer
- Graph with BFS/DFS

### Cache
- TTL Cache (with Expiry)
- Write-Through / Write-Back Cache

### Concurrency
- ReadWriteLock
- Producer-Consumer
- CountDownLatch
- Semaphore

### Design Components
- Observable / Observer Pattern
- Pub-Sub System
- Connection Pool
- Logger
- Consistent Hashing Ring
- Event Bus
- CompletableFuture / Promise

### Classic Algorithms
- MergeSort, QuickSort
- StringBuilder
- Custom Comparator
- atoi (String to Integer)

### Java-Specific
- Custom Stream Operations (map, filter, reduce)
- Immutable Class / Builder Pattern

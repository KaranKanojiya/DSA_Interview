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
src/main/java/dsa/
├── data_structures/ (10) — HashMap, LinkedList, ArrayList, Stack, Queue, Deque, Heap, Trie, BST, Graph
├── cache/           (4)  — LRU, LFU, TTL, Write-Through/Back
├── concurrency/     (7)  — ThreadPool, BlockingQueue, RWLock, RateLimiter, ProducerConsumer, Latch, Semaphore
├── design/          (8)  — Iterator, Observer, PubSub, ConnPool, Logger, ConsistentHash, EventBus, Future
├── algorithms/      (7)  — MergeSort, QuickSort, StringBuilder, Comparator, atoi, Streams, Builder
```

## Interactive Study Canvases (36/36 complete)
Each implementation has a companion HTML study canvas. Open `canvas/index.html` in browser or Cursor Live Preview.

```
canvas/
├── index.html                 Hub — 36 clickable tiles, 5 color-coded categories
├── assets/canvas.css          Dark-theme design system (semantic color tokens per category)
├── assets/canvas.js           Tab switching, collapsibles, LRU/LFU interactive simulators
├── cache/          (4)        LRU*, LFU*, TTL, Write-Through    (* = interactive simulator)
├── data-structures/(10)       HashMap, LinkedList, ArrayList, Stack, Queue, Deque, MinHeap, Trie, BST, Graph
├── concurrency/    (7)        ThreadPool, BlockingQueue, RWLock, RateLimiter, ProducerConsumer, Latch, Semaphore
├── design/         (8)        BSTIterator, Observer, PubSub, ConnPool, Logger, ConsistentHashing, EventBus, CompletableFuture
└── algorithms/     (7)        MergeSort, QuickSort, StringBuilder, CustomComparator, atoi, StreamOps, ImmutableBuilder
```

### Canvas structure (each file):
- Breadcrumb → Hero (badges, complexity) → 3 tabs (Concept / Implementation / Interview)
- Concept: SVG diagrams, grid cards, operations walkthrough
- Implementation: annotated Java code with syntax-highlighted spans (.kw .ty .cm .st .num)
- Interview: complexity table, collapsible follow-ups, callout edge cases
- SVG rules: min 120px box width, 40px height, 30px gap, viewBox for responsive, no text overflow

### Canvas color tokens:
- --ds (teal) · --cache (amber) · --conc (purple) · --design (rose) · --algo (sky)

## Conventions
- Each file = one "implement from scratch" question
- Javadoc with: what, why, companies, difficulty, complexity, follow-ups
- Step-by-step inline comments explaining every operation
- Runnable main() with demo output
- Same format as Java_Interview project

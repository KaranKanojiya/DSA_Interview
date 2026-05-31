# DSA Interview Prep — Implement from Scratch

## What This Is
"Build it yourself" interview questions — implement core data structures and system components without Java built-in libraries. Same format as Java_Interview: Maven, Java 21, self-contained main() methods.

## Tech Stack
- Java 21, Maven 3.9+, --enable-preview

## Build & Run
```bash
mvn compile
java --enable-preview -cp target/classes dsa.cache.LRUCache
```

## Structure
```
dsa/cache/           — LRU Cache, LFU Cache
dsa/data_structures/ — HashMap, Trie, MinHeap, BST
dsa/concurrency/     — BlockingQueue, ThreadPool, RateLimiter
dsa/design/          — BST Iterator
```

## Conventions
- Each file = one "implement from scratch" question
- Javadoc with: what, why, companies, difficulty, complexity, follow-ups
- Step-by-step inline comments explaining every operation
- Runnable main() with demo output
- Same format as Java_Interview project

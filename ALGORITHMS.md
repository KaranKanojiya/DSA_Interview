# Must-Know Algorithms for Interview Prep

**Every algorithm you need to know — with when to use, how it works, complexity, and Java code.**

---

## Table of Contents

1. [Sorting Algorithms](#1-sorting-algorithms)
2. [Searching Algorithms](#2-searching-algorithms)
3. [Graph Algorithms](#3-graph-algorithms)
4. [Tree Algorithms](#4-tree-algorithms)
5. [String Algorithms](#5-string-algorithms)
6. [Dynamic Programming Algorithms](#6-dynamic-programming-algorithms)
7. [Greedy Algorithms](#7-greedy-algorithms)
8. [Math & Number Theory](#8-math--number-theory)
9. [Bit Manipulation Algorithms](#9-bit-manipulation-algorithms)
10. [Design Algorithms](#10-design-algorithms)
11. [Quick Reference Table](#11-quick-reference-table)

---

## 1. Sorting Algorithms

### MergeSort — Stable, O(n log n) always
```
When: need stable sort, linked list sorting, external sort (large files)
How:  split in half → sort each → merge sorted halves
```
```java
void mergeSort(int[] arr, int l, int r) {
    if (l >= r) return;
    int mid = l + (r - l) / 2;
    mergeSort(arr, l, mid);
    mergeSort(arr, mid + 1, r);
    merge(arr, l, mid, r);
}
```

### QuickSort — In-place, O(n log n) average
```
When: general purpose, in-place needed, average case matters more than worst case
How:  pick pivot → partition (smaller left, larger right) → recurse
Trick: random pivot avoids O(n^2) on sorted input
```

### HeapSort — In-place, O(n log n), not stable
```
When: guaranteed O(n log n) + O(1) space needed
How:  build max-heap → repeatedly extract max
Rarely asked to implement, but know it exists
```

### Counting Sort — O(n + k), non-comparison
```
When: integers in small range [0, k], k is not huge
How:  count occurrences → prefix sum → place elements
```
```java
void countingSort(int[] arr, int maxVal) {
    int[] count = new int[maxVal + 1];
    for (int n : arr) count[n]++;
    int idx = 0;
    for (int i = 0; i <= maxVal; i++)
        while (count[i]-- > 0) arr[idx++] = i;
}
```

### Bucket Sort — O(n) average
```
When: uniformly distributed floating-point numbers
How:  divide range into buckets → sort each bucket → concatenate
```

### When to Use Which Sort

| Need | Use |
|------|-----|
| General purpose | QuickSort (Arrays.sort for primitives) |
| Stable sort | MergeSort (Arrays.sort for objects) |
| Almost sorted data | Insertion Sort O(n) best case |
| Small range integers | Counting Sort |
| Linked list | MergeSort (no random access needed) |
| K largest/smallest | Partial sort with Heap — O(n log k) |

---

## 2. Searching Algorithms

### Binary Search — O(log n)
```
When: sorted array, monotonic function, search space reduction
Variants: first/last occurrence, search on answer, rotated array
```

### Binary Search on Rotated Sorted Array
```java
int search(int[] nums, int target) {
    int lo = 0, hi = nums.length - 1;
    while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;
        if (nums[mid] == target) return mid;

        if (nums[lo] <= nums[mid]) { // left half is sorted
            if (target >= nums[lo] && target < nums[mid]) hi = mid - 1;
            else lo = mid + 1;
        } else { // right half is sorted
            if (target > nums[mid] && target <= nums[hi]) lo = mid + 1;
            else hi = mid - 1;
        }
    }
    return -1;
}
```

### Two Pointer Search — O(n)
```
When: sorted array pair sum, palindrome check, partition
```

### Exponential Search — O(log n)
```
When: unbounded/infinite sorted array
How:  double the range (1, 2, 4, 8, 16...) until overshoot → binary search in last range
```

---

## 3. Graph Algorithms

### BFS (Breadth-First Search) — O(V + E)
```
When: shortest path in unweighted graph, level-order, minimum steps
How:  Queue-based, visit all neighbors before going deeper
Key:  always gives shortest path in unweighted graph
```

### DFS (Depth-First Search) — O(V + E)
```
When: connected components, cycle detection, path existence, topological sort
How:  Stack/recursion-based, go as deep as possible then backtrack
```

### Dijkstra's Algorithm — O(E log V)
```
When: shortest path in WEIGHTED graph (non-negative weights)
How:  Priority Queue (min-heap), greedily pick nearest unvisited node
Cannot handle negative weights → use Bellman-Ford
```
```java
// Dijkstra template
PriorityQueue<int[]> pq = new PriorityQueue<>((a,b) -> a[1] - b[1]); // [node, dist]
int[] dist = new int[n]; Arrays.fill(dist, Integer.MAX_VALUE);
dist[src] = 0; pq.offer(new int[]{src, 0});

while (!pq.isEmpty()) {
    int[] curr = pq.poll();
    int u = curr[0], d = curr[1];
    if (d > dist[u]) continue; // stale entry
    for (int[] edge : graph.get(u)) { // [neighbor, weight]
        int v = edge[0], w = edge[1];
        if (dist[u] + w < dist[v]) {
            dist[v] = dist[u] + w;
            pq.offer(new int[]{v, dist[v]});
        }
    }
}
```

### Bellman-Ford — O(V × E)
```
When: shortest path with NEGATIVE weights, detect negative cycles
How:  relax all edges V-1 times; if Vth relaxation still improves → negative cycle
```
```java
int[] dist = new int[n]; Arrays.fill(dist, Integer.MAX_VALUE);
dist[src] = 0;
for (int i = 0; i < n - 1; i++) { // V-1 iterations
    for (int[] edge : edges) { // [from, to, weight]
        if (dist[edge[0]] != Integer.MAX_VALUE && dist[edge[0]] + edge[2] < dist[edge[1]])
            dist[edge[1]] = dist[edge[0]] + edge[2];
    }
}
// Check for negative cycle: one more iteration
for (int[] edge : edges) {
    if (dist[edge[0]] != Integer.MAX_VALUE && dist[edge[0]] + edge[2] < dist[edge[1]])
        throw new RuntimeException("Negative cycle detected!");
}
```

### Floyd-Warshall — O(V³)
```
When: all-pairs shortest path, small graph (V < 500)
How:  dp[i][j] = min(dp[i][j], dp[i][k] + dp[k][j]) for every intermediate k
```
```java
for (int k = 0; k < n; k++)
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            if (dist[i][k] != INF && dist[k][j] != INF)
                dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
```

### Topological Sort — O(V + E)
```
When: dependency ordering, task scheduling, course prerequisites
How:  Kahn's (BFS + indegree) or DFS with post-order
Only works on DAG (Directed Acyclic Graph)
```

### Union-Find (Disjoint Set Union) — O(α(n)) ≈ O(1)
```
When: connected components, cycle detection in undirected graph, Kruskal's MST
How:  find(x) with path compression, union(x,y) with rank
```
```java
int[] parent, rank;

int find(int x) {
    if (parent[x] != x) parent[x] = find(parent[x]); // path compression
    return parent[x];
}

void union(int x, int y) {
    int px = find(x), py = find(y);
    if (px == py) return;
    if (rank[px] < rank[py]) parent[px] = py;      // union by rank
    else if (rank[px] > rank[py]) parent[py] = px;
    else { parent[py] = px; rank[px]++; }
}
```

### Kruskal's MST — O(E log E)
```
When: minimum spanning tree
How:  sort edges by weight → add smallest edge if it doesn't create a cycle (Union-Find)
```

### Prim's MST — O(E log V)
```
When: minimum spanning tree (dense graph, adjacency list)
How:  start from any node → greedily add nearest edge to tree using PQ
```

### When to Use Which Graph Algorithm

| Problem | Algorithm |
|---------|-----------|
| Shortest path (unweighted) | BFS |
| Shortest path (weighted, non-negative) | Dijkstra |
| Shortest path (negative weights) | Bellman-Ford |
| All-pairs shortest path | Floyd-Warshall |
| Connected components | DFS / Union-Find |
| Cycle detection (undirected) | Union-Find |
| Cycle detection (directed) | DFS (back edge) / Topological Sort |
| Task ordering | Topological Sort |
| Minimum spanning tree | Kruskal's or Prim's |
| Bipartite check | BFS/DFS with 2-coloring |

---

## 4. Tree Algorithms

### DFS Traversals — O(n)
```
Inorder  (L-Root-R):  BST gives sorted order
Preorder (Root-L-R):  serialize tree, copy tree
Postorder (L-R-Root): delete tree, calculate height bottom-up
```

### Morris Traversal — O(n) time, O(1) space
```
When: inorder traversal without stack or recursion
How:  use threaded tree — set right child to inorder successor temporarily
```
```java
TreeNode curr = root;
while (curr != null) {
    if (curr.left == null) {
        process(curr); // visit
        curr = curr.right;
    } else {
        TreeNode pred = curr.left;
        while (pred.right != null && pred.right != curr)
            pred = pred.right;
        if (pred.right == null) { // first visit: thread it
            pred.right = curr;
            curr = curr.left;
        } else { // second visit: unthread and visit
            pred.right = null;
            process(curr);
            curr = curr.right;
        }
    }
}
```

### Lowest Common Ancestor (LCA) — O(n)
```
When: find common parent of two nodes
How:  recursive — if both sides return non-null, current is LCA
BST variant: use BST property, O(log n)
```

### Serialize / Deserialize Tree
```
When: store tree as string, transmit over network
How:  preorder with null markers, split by delimiter to rebuild
```

### AVL / Red-Black Tree Rotations
```
Know conceptually for interviews — rarely asked to implement:
  Left Rotation:  right child becomes parent
  Right Rotation: left child becomes parent
  AVL: height difference ≤ 1, rotation on insert/delete
  Red-Black: color property, O(log n) guaranteed
  Java's TreeMap uses Red-Black Tree
```

---

## 5. String Algorithms

### KMP (Knuth-Morris-Pratt) — O(n + m) pattern matching
```
When: find pattern in text efficiently (avoid O(n*m) brute force)
How:  precompute LPS (Longest Proper Prefix which is also Suffix) array
```
```java
// Build LPS array
int[] lps = new int[pattern.length()];
int len = 0, i = 1;
while (i < pattern.length()) {
    if (pattern.charAt(i) == pattern.charAt(len)) { lps[i++] = ++len; }
    else if (len != 0) { len = lps[len - 1]; }
    else { lps[i++] = 0; }
}

// Search
int j = 0; i = 0;
while (i < text.length()) {
    if (text.charAt(i) == pattern.charAt(j)) { i++; j++; }
    if (j == pattern.length()) { /* found at i - j */ j = lps[j - 1]; }
    else if (i < text.length() && text.charAt(i) != pattern.charAt(j)) {
        if (j != 0) j = lps[j - 1]; else i++;
    }
}
```

### Rabin-Karp — O(n) average pattern matching
```
When: multiple pattern search, plagiarism detection
How:  rolling hash — slide window of pattern length, compare hashes
```

### Longest Palindromic Substring — O(n²) or O(n) Manacher's
```
When: find longest palindrome in a string
How:  expand around center (2n-1 centers), or Manacher's for O(n)
```
```java
// Expand around center — O(n²)
String longestPalindrome(String s) {
    int start = 0, maxLen = 1;
    for (int i = 0; i < s.length(); i++) {
        // Odd length palindrome (single center)
        int len1 = expand(s, i, i);
        // Even length palindrome (two centers)
        int len2 = expand(s, i, i + 1);
        int len = Math.max(len1, len2);
        if (len > maxLen) { start = i - (len - 1) / 2; maxLen = len; }
    }
    return s.substring(start, start + maxLen);
}

int expand(String s, int l, int r) {
    while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) { l--; r++; }
    return r - l - 1;
}
```

### Trie-Based Algorithms
```
Autocomplete: insert words → DFS from prefix node to collect all words
Word Search in Grid: DFS + Trie (prune branches that don't match any prefix)
Longest Common Prefix: insert all → follow path where every node has exactly one child
```

---

## 6. Dynamic Programming Algorithms

### DP Framework — 5 Steps
```
1. Define state:     dp[i] represents what?
2. Base case:        dp[0] = ?
3. Recurrence:       dp[i] = f(dp[i-1], dp[i-2], ...)
4. Order:            fill left→right? bottom→top?
5. Answer:           dp[n]? max(dp[i])? dp[m][n]?
```

### Must-Know DP Problems

| Problem | State | Recurrence |
|---------|-------|------------|
| Climbing Stairs | dp[i] = ways to reach step i | dp[i] = dp[i-1] + dp[i-2] |
| Coin Change | dp[a] = min coins for amount a | dp[a] = min(dp[a], dp[a-coin] + 1) |
| House Robber | dp[i] = max money from houses 0..i | dp[i] = max(dp[i-1], dp[i-2] + nums[i]) |
| LIS | dp[i] = length of LIS ending at i | dp[i] = max(dp[j] + 1) for j < i where nums[j] < nums[i] |
| LCS | dp[i][j] = LCS of s1[0..i], s2[0..j] | if match: dp[i-1][j-1]+1, else max(dp[i-1][j], dp[i][j-1]) |
| Edit Distance | dp[i][j] = min ops to convert s1[0..i] to s2[0..j] | if match: dp[i-1][j-1], else 1+min(insert, delete, replace) |
| 0/1 Knapsack | dp[i][w] = max value with items 0..i, capacity w | max(skip, take if weight fits) |
| Word Break | dp[i] = can s[0..i] be segmented | dp[i] = any dp[j] && s[j..i] in dict |
| Unique Paths | dp[i][j] = paths to (i,j) | dp[i][j] = dp[i-1][j] + dp[i][j-1] |
| Palindrome Partitioning | dp[i] = min cuts for s[0..i] | dp[i] = min(dp[j-1] + 1) where s[j..i] is palindrome |

### Space Optimization
```
If dp[i] only depends on dp[i-1] and dp[i-2] → use two variables instead of array
If dp[i][j] only depends on dp[i-1][j] → use 1D array and iterate correctly
```

---

## 7. Greedy Algorithms

### When is Greedy Correct?
```
Greedy choice property: locally optimal choice leads to globally optimal solution
Optimal substructure: optimal solution contains optimal solutions to subproblems
If unsure: try greedy, verify with small examples, prove or use DP instead
```

### Must-Know Greedy Problems

| Problem | Greedy Strategy |
|---------|----------------|
| Activity Selection | Sort by end time, pick earliest ending |
| Fractional Knapsack | Sort by value/weight ratio, take most valuable |
| Huffman Coding | Build tree by combining two smallest frequencies |
| Jump Game | Track max reachable index |
| Gas Station | If total gas >= total cost, unique start exists |
| Task Scheduler | Most frequent task first, fill gaps with others |
| Partition Labels | Track last occurrence of each char, extend partition |
| Meeting Rooms II | Sort by start, use min-heap for end times |

---

## 8. Math & Number Theory

### GCD (Euclidean Algorithm) — O(log(min(a,b)))
```java
int gcd(int a, int b) { return b == 0 ? a : gcd(b, a % b); }
int lcm(int a, int b) { return a / gcd(a, b) * b; } // avoid overflow
```

### Sieve of Eratosthenes — O(n log log n)
```java
boolean[] isPrime = new boolean[n + 1];
Arrays.fill(isPrime, true);
isPrime[0] = isPrime[1] = false;
for (int i = 2; i * i <= n; i++) {
    if (isPrime[i]) {
        for (int j = i * i; j <= n; j += i) isPrime[j] = false;
    }
}
```

### Fast Exponentiation — O(log n)
```java
long power(long base, long exp, long mod) {
    long result = 1;
    base %= mod;
    while (exp > 0) {
        if ((exp & 1) == 1) result = result * base % mod;
        exp >>= 1;
        base = base * base % mod;
    }
    return result;
}
```

### Useful Math Formulas
```
Sum 1..n:           n * (n + 1) / 2
Sum of squares:     n * (n+1) * (2n+1) / 6
Geometric sum:      a * (r^n - 1) / (r - 1)
Permutations:       n! / (n-r)!
Combinations:       n! / (r! * (n-r)!)
Catalan number:     C(2n,n) / (n+1) — BST count, valid parentheses count
Pigeonhole:         n+1 items in n boxes → at least one box has 2+ items
```

---

## 9. Bit Manipulation Algorithms

### Essential Operations
```java
x & 1              // is odd?
x >> 1             // divide by 2
x << 1             // multiply by 2
x & (x - 1)        // clear lowest set bit
x & (-x)           // isolate lowest set bit
x | (1 << i)       // set bit i
x & ~(1 << i)      // clear bit i
x ^ (1 << i)       // toggle bit i
(x >> i) & 1       // check bit i
Integer.bitCount(x) // count set bits (Java built-in)
```

### Classic Bit Problems
```
Single Number (XOR all):          a ^ a = 0, so duplicate cancels out
Missing Number:                    XOR 0..n with array → missing remains
Power of Two:                      n > 0 && (n & (n-1)) == 0
Reverse Bits:                      shift and OR bit by bit
Hamming Distance:                  bitCount(x ^ y)
Subsets via bitmask:               for mask 0..(1<<n)-1, check each bit
```

---

## 10. Design Algorithms

### Consistent Hashing
```
When: distribute keys across servers with minimal redistribution
How:  hash servers + keys onto a ring (TreeMap), find nearest server clockwise
```

### Reservoir Sampling — O(n) time, O(k) space
```
When: random sample of k items from unknown-size stream
How:  keep first k items; for ith item (i > k), replace random item with probability k/i
```
```java
int[] reservoir = new int[k];
for (int i = 0; i < n; i++) {
    if (i < k) reservoir[i] = stream[i];
    else {
        int j = random.nextInt(i + 1);
        if (j < k) reservoir[j] = stream[i];
    }
}
```

### Bloom Filter — O(k) per operation
```
When: membership test (is X in the set?) with small memory, tolerating false positives
How:  k hash functions → k bit positions → all set = "probably yes", any unset = "definitely no"
False positive possible, false negative impossible
```

### LRU / LFU Cache (see implementations in dsa/cache/)

### Rate Limiting Algorithms
```
Token Bucket:       refill tokens at fixed rate, request consumes token
Sliding Window:     deque of timestamps, count within window
Fixed Window:       counter per time window, reset at boundary
Leaky Bucket:       constant output rate regardless of input burst
```

---

## 11. Quick Reference Table

### "What algorithm for what problem?"

| Problem Type | First Try | If That Fails |
|-------------|-----------|---------------|
| Shortest path (unweighted) | BFS | — |
| Shortest path (weighted) | Dijkstra | Bellman-Ford (negative) |
| All-pairs shortest | Floyd-Warshall | n × Dijkstra |
| Connected components | Union-Find | DFS |
| Cycle detection | Union-Find (undirected) / Topo sort (directed) | DFS |
| Minimum spanning tree | Kruskal's | Prim's |
| Sorted array search | Binary Search | Two Pointers |
| Subarray/substring | Sliding Window | Two Pointers |
| Optimization (min/max) | DP | Greedy (if valid) |
| All combinations/permutations | Backtracking | DP (counting only) |
| Pattern matching | KMP | Rabin-Karp / Trie |
| Range queries | Prefix Sum | Segment Tree |
| Top K | Heap (size K) | QuickSelect O(n) avg |
| Interval problems | Sort + Sweep | Greedy |
| Tree problems | Recursion (DFS) | BFS (level-order) |
| Stream/online data | Heap / Reservoir Sampling | — |

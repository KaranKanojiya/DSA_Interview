# DSA Cheatsheet -- Patterns, Algorithms & When to Use What

**Quick reference for coding interviews — recognize the pattern, apply the template.**

---

## Table of Contents

1. [Pattern Recognition — "I see X, I use Y"](#1-pattern-recognition)
2. [Array & String Patterns](#2-array--string-patterns)
3. [Two Pointers](#3-two-pointers)
4. [Sliding Window](#4-sliding-window)
5. [Binary Search](#5-binary-search)
6. [Stack Patterns](#6-stack-patterns)
7. [HashMap / HashSet Patterns](#7-hashmap--hashset-patterns)
8. [Linked List Patterns](#8-linked-list-patterns)
9. [Tree Patterns (Recursion is King)](#9-tree-patterns)
10. [Graph Patterns (BFS / DFS)](#10-graph-patterns)
11. [Heap / Priority Queue Patterns](#11-heap--priority-queue-patterns)
12. [Dynamic Programming Patterns](#12-dynamic-programming-patterns)
13. [Backtracking](#13-backtracking)
14. [Greedy](#14-greedy)
15. [Intervals](#15-intervals)
16. [Bit Manipulation](#16-bit-manipulation)
17. [Complexity Cheatsheet](#17-complexity-cheatsheet)
18. [Java Snippets for Interviews](#18-java-snippets)

---

## 1. Pattern Recognition

**See this → Use this:**

| Signal in Problem | Pattern | Example |
|-------------------|---------|---------|
| "Sorted array" | Binary Search / Two Pointers | Search in rotated array |
| "Subarray/substring of size K" | Sliding Window (fixed) | Max sum subarray of size K |
| "Longest/shortest subarray with condition" | Sliding Window (variable) | Longest substring without repeating |
| "Top K / Kth largest" | Heap (min-heap of size K) | Top K frequent elements |
| "All permutations/combinations/subsets" | Backtracking | Generate parentheses |
| "Can you reach / minimum steps" | BFS (shortest path) | Word ladder |
| "Connected components / cycle" | DFS / Union-Find | Number of islands |
| "Tree traversal / subtree property" | Recursion (DFS) | Validate BST |
| "Overlapping subproblems" | Dynamic Programming | Coin change |
| "Merge intervals / schedule" | Sort + Greedy | Meeting rooms |
| "Parentheses / nesting" | Stack | Valid parentheses |
| "Frequency / count / duplicate" | HashMap / HashSet | Two Sum |
| "Prefix / autocomplete" | Trie | Search suggestions |
| "Matrix traversal" | BFS / DFS + visited[][] | Rotting oranges |
| "In-place, O(1) space" | Two Pointers / Swap | Remove duplicates |
| "Next greater / smaller" | Monotonic Stack | Daily temperatures |
| "Stream of data / running median" | Two Heaps (max + min) | Find median |

---

## 2. Array & String Patterns

### Prefix Sum — range sum queries in O(1)
```java
// Build: prefix[i] = sum of arr[0..i-1]
int[] prefix = new int[n + 1];
for (int i = 0; i < n; i++) prefix[i + 1] = prefix[i] + arr[i];

// Query: sum of arr[l..r] = prefix[r+1] - prefix[l]
int rangeSum = prefix[r + 1] - prefix[l];
```
**Use for:** Range sum, subarray sum equals K, count subarrays with sum

### Kadane's Algorithm — maximum subarray sum O(n)
```java
int maxSum = arr[0], currentSum = arr[0];
for (int i = 1; i < n; i++) {
    currentSum = Math.max(arr[i], currentSum + arr[i]); // extend or restart
    maxSum = Math.max(maxSum, currentSum);
}
```

### Dutch National Flag — sort 3 values in O(n)
```java
// Sort array of 0s, 1s, 2s in-place
int lo = 0, mid = 0, hi = n - 1;
while (mid <= hi) {
    if (arr[mid] == 0) swap(arr, lo++, mid++);
    else if (arr[mid] == 1) mid++;
    else swap(arr, mid, hi--);
}
```

---

## 3. Two Pointers

### Template: Opposite ends (sorted array)
```java
int left = 0, right = n - 1;
while (left < right) {
    int sum = arr[left] + arr[right];
    if (sum == target) { /* found */ left++; right--; }
    else if (sum < target) left++;   // need larger
    else right--;                     // need smaller
}
```
**Use for:** Two Sum (sorted), 3Sum, Container with most water, Trapping rain water

### Template: Same direction (fast/slow)
```java
// Remove duplicates in-place
int slow = 0;
for (int fast = 1; fast < n; fast++) {
    if (arr[fast] != arr[slow]) {
        slow++;
        arr[slow] = arr[fast];
    }
}
// arr[0..slow] has unique elements
```
**Use for:** Remove duplicates, move zeros, linked list cycle detection (Floyd's)

### Floyd's Cycle Detection (Linked List)
```java
ListNode slow = head, fast = head;
while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
    if (slow == fast) return true; // cycle!
}
return false; // no cycle

// Find cycle start: reset slow to head, move both by 1
slow = head;
while (slow != fast) { slow = slow.next; fast = fast.next; }
return slow; // cycle start
```

---

## 4. Sliding Window

### Fixed Window (size K)
```java
// Max sum of subarray of size K
int windowSum = 0, maxSum = 0;
for (int i = 0; i < n; i++) {
    windowSum += arr[i];
    if (i >= k) windowSum -= arr[i - k]; // remove leftmost
    if (i >= k - 1) maxSum = Math.max(maxSum, windowSum);
}
```

### Variable Window (shrink when condition breaks)
```java
// Longest substring without repeating characters
int left = 0, maxLen = 0;
Set<Character> set = new HashSet<>();
for (int right = 0; right < s.length(); right++) {
    while (set.contains(s.charAt(right))) {
        set.remove(s.charAt(left));
        left++;  // shrink window
    }
    set.add(s.charAt(right));
    maxLen = Math.max(maxLen, right - left + 1);
}
```

### Variable Window (minimum window matching condition)
```java
// Minimum window substring containing all chars of T
int left = 0, minLen = Integer.MAX_VALUE, count = 0;
Map<Character, Integer> need = new HashMap<>();
for (char c : t.toCharArray()) need.merge(c, 1, Integer::sum);

for (int right = 0; right < s.length(); right++) {
    char c = s.charAt(right);
    if (need.containsKey(c)) {
        need.merge(c, -1, Integer::sum);
        if (need.get(c) >= 0) count++;
    }
    while (count == t.length()) { // window contains all chars — try to shrink
        minLen = Math.min(minLen, right - left + 1);
        char lc = s.charAt(left);
        if (need.containsKey(lc)) {
            need.merge(lc, 1, Integer::sum);
            if (need.get(lc) > 0) count--;
        }
        left++;
    }
}
```

---

## 5. Binary Search

### Standard Binary Search
```java
int lo = 0, hi = n - 1;
while (lo <= hi) {
    int mid = lo + (hi - lo) / 2; // avoid overflow
    if (arr[mid] == target) return mid;
    else if (arr[mid] < target) lo = mid + 1;
    else hi = mid - 1;
}
return -1;
```

### Binary Search on Answer (search space is the answer range)
```java
// Find minimum capacity to ship within D days
int lo = maxWeight, hi = totalWeight;
while (lo < hi) {
    int mid = lo + (hi - lo) / 2;
    if (canShip(mid, days)) hi = mid;  // try smaller capacity
    else lo = mid + 1;                  // need more capacity
}
return lo;
```
**Use for:** Koko eating bananas, Split array largest sum, Minimum days to make bouquets

### Binary Search Variants
```java
// First occurrence (leftmost)
while (lo < hi) {
    int mid = lo + (hi - lo) / 2;
    if (arr[mid] >= target) hi = mid;
    else lo = mid + 1;
}

// Last occurrence (rightmost)
while (lo < hi) {
    int mid = lo + (hi - lo + 1) / 2; // ceil
    if (arr[mid] <= target) lo = mid;
    else hi = mid - 1;
}
```

---

## 6. Stack Patterns

### Monotonic Stack — next greater/smaller element
```java
// Next Greater Element — O(n)
int[] result = new int[n];
Arrays.fill(result, -1);
Deque<Integer> stack = new ArrayDeque<>(); // stores INDICES

for (int i = 0; i < n; i++) {
    while (!stack.isEmpty() && arr[i] > arr[stack.peek()]) {
        result[stack.pop()] = arr[i]; // arr[i] is the next greater for popped index
    }
    stack.push(i);
}
```
**Use for:** Daily temperatures, Stock span, Largest rectangle in histogram, Trapping rain water

### Valid Parentheses
```java
Deque<Character> stack = new ArrayDeque<>();
for (char c : s.toCharArray()) {
    if (c == '(' || c == '{' || c == '[') stack.push(c);
    else {
        if (stack.isEmpty()) return false;
        char top = stack.pop();
        if ((c == ')' && top != '(') || (c == '}' && top != '{') || (c == ']' && top != '['))
            return false;
    }
}
return stack.isEmpty();
```

---

## 7. HashMap / HashSet Patterns

### Two Sum (classic)
```java
Map<Integer, Integer> map = new HashMap<>(); // value → index
for (int i = 0; i < n; i++) {
    int complement = target - nums[i];
    if (map.containsKey(complement)) return new int[]{map.get(complement), i};
    map.put(nums[i], i);
}
```

### Frequency Count / Group Anagrams
```java
// Group anagrams: sort each word as key
Map<String, List<String>> groups = new HashMap<>();
for (String s : strs) {
    char[] chars = s.toCharArray();
    Arrays.sort(chars);
    String key = new String(chars);
    groups.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
}
```

### Subarray Sum Equals K (prefix sum + HashMap)
```java
int count = 0, prefixSum = 0;
Map<Integer, Integer> map = new HashMap<>();
map.put(0, 1); // empty subarray

for (int num : nums) {
    prefixSum += num;
    count += map.getOrDefault(prefixSum - k, 0);
    map.merge(prefixSum, 1, Integer::sum);
}
```

---

## 8. Linked List Patterns

### Reverse Linked List
```java
ListNode prev = null, curr = head;
while (curr != null) {
    ListNode next = curr.next;
    curr.next = prev;
    prev = curr;
    curr = next;
}
return prev; // new head
```

### Find Middle (slow/fast)
```java
ListNode slow = head, fast = head;
while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
}
return slow; // middle node
```

### Merge Two Sorted Lists
```java
ListNode dummy = new ListNode(0), curr = dummy;
while (l1 != null && l2 != null) {
    if (l1.val <= l2.val) { curr.next = l1; l1 = l1.next; }
    else { curr.next = l2; l2 = l2.next; }
    curr = curr.next;
}
curr.next = (l1 != null) ? l1 : l2;
return dummy.next;
```

---

## 9. Tree Patterns

### **Rule: Trees = Recursion. Always think recursion first.**

### DFS Templates (3 traversals)
```java
// Inorder: Left → Root → Right (BST → sorted order)
void inorder(TreeNode node) {
    if (node == null) return;
    inorder(node.left);
    process(node);       // ← visit here
    inorder(node.right);
}

// Preorder: Root → Left → Right (copy tree, serialize)
// Postorder: Left → Right → Root (delete tree, bottom-up calculation)
```

### Height / Depth
```java
int height(TreeNode node) {
    if (node == null) return 0;
    return 1 + Math.max(height(node.left), height(node.right));
}
```

### Validate BST (pass min/max range down)
```java
boolean isValid(TreeNode node, long min, long max) {
    if (node == null) return true;
    if (node.val <= min || node.val >= max) return false;
    return isValid(node.left, min, node.val) && isValid(node.right, node.val, max);
}
```

### Lowest Common Ancestor (LCA)
```java
TreeNode lca(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null || root == p || root == q) return root;
    TreeNode left = lca(root.left, p, q);
    TreeNode right = lca(root.right, p, q);
    if (left != null && right != null) return root; // p and q are on different sides
    return left != null ? left : right;
}
```

### BFS (Level Order Traversal)
```java
Queue<TreeNode> queue = new LinkedList<>();
queue.offer(root);
while (!queue.isEmpty()) {
    int size = queue.size(); // nodes at this level
    for (int i = 0; i < size; i++) {
        TreeNode node = queue.poll();
        process(node);
        if (node.left != null) queue.offer(node.left);
        if (node.right != null) queue.offer(node.right);
    }
}
```

### Path Sum / Max Path Sum
```java
// Return max gain from each subtree
int maxPath = Integer.MIN_VALUE;
int maxGain(TreeNode node) {
    if (node == null) return 0;
    int left = Math.max(0, maxGain(node.left));   // ignore negative paths
    int right = Math.max(0, maxGain(node.right));
    maxPath = Math.max(maxPath, left + right + node.val); // path through this node
    return Math.max(left, right) + node.val;       // return best single path
}
```

---

## 10. Graph Patterns

### BFS — Shortest Path (unweighted)
```java
Queue<int[]> queue = new LinkedList<>();
boolean[][] visited = new boolean[rows][cols];
queue.offer(new int[]{startRow, startCol, 0}); // row, col, distance
visited[startRow][startCol] = true;

int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
while (!queue.isEmpty()) {
    int[] curr = queue.poll();
    if (curr[0] == targetRow && curr[1] == targetCol) return curr[2];
    for (int[] d : dirs) {
        int nr = curr[0] + d[0], nc = curr[1] + d[1];
        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !visited[nr][nc]) {
            visited[nr][nc] = true;
            queue.offer(new int[]{nr, nc, curr[2] + 1});
        }
    }
}
```

### DFS — Connected Components / Islands
```java
// Number of Islands
int count = 0;
for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
        if (grid[i][j] == '1') {
            dfs(grid, i, j); // mark entire island as visited
            count++;
        }
    }
}

void dfs(char[][] grid, int r, int c) {
    if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] != '1') return;
    grid[r][c] = '0'; // mark visited
    dfs(grid, r+1, c); dfs(grid, r-1, c); dfs(grid, r, c+1); dfs(grid, r, c-1);
}
```

### Topological Sort (Course Schedule)
```java
// Kahn's Algorithm (BFS-based)
int[] inDegree = new int[numCourses];
Map<Integer, List<Integer>> graph = new HashMap<>();
// build graph + count in-degrees...

Queue<Integer> queue = new LinkedList<>();
for (int i = 0; i < numCourses; i++) if (inDegree[i] == 0) queue.offer(i);

List<Integer> order = new ArrayList<>();
while (!queue.isEmpty()) {
    int course = queue.poll();
    order.add(course);
    for (int next : graph.getOrDefault(course, List.of())) {
        if (--inDegree[next] == 0) queue.offer(next);
    }
}
return order.size() == numCourses; // false = cycle exists
```

### Dijkstra's — Shortest Path (weighted)
```java
// PriorityQueue: [distance, node]
PriorityQueue<int[]> pq = new PriorityQueue<>((a,b) -> a[0] - b[0]);
int[] dist = new int[n]; Arrays.fill(dist, Integer.MAX_VALUE);
dist[src] = 0;
pq.offer(new int[]{0, src});

while (!pq.isEmpty()) {
    int[] curr = pq.poll();
    int d = curr[0], u = curr[1];
    if (d > dist[u]) continue; // already found shorter path
    for (int[] edge : graph[u]) { // [neighbor, weight]
        int v = edge[0], w = edge[1];
        if (dist[u] + w < dist[v]) {
            dist[v] = dist[u] + w;
            pq.offer(new int[]{dist[v], v});
        }
    }
}
```

---

## 11. Heap / Priority Queue Patterns

### Top K Elements
```java
// Top K frequent elements
Map<Integer, Integer> freq = new HashMap<>();
for (int n : nums) freq.merge(n, 1, Integer::sum);

// Min-heap of size K (smallest at top → remove smallest when size > K)
PriorityQueue<Integer> heap = new PriorityQueue<>((a,b) -> freq.get(a) - freq.get(b));
for (int key : freq.keySet()) {
    heap.offer(key);
    if (heap.size() > k) heap.poll(); // remove least frequent
}
```

### Merge K Sorted Lists
```java
PriorityQueue<ListNode> heap = new PriorityQueue<>((a,b) -> a.val - b.val);
for (ListNode list : lists) if (list != null) heap.offer(list);

ListNode dummy = new ListNode(0), curr = dummy;
while (!heap.isEmpty()) {
    ListNode min = heap.poll();
    curr.next = min;
    curr = curr.next;
    if (min.next != null) heap.offer(min.next);
}
return dummy.next;
```

### Running Median (Two Heaps)
```java
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder()); // left half
PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // right half

void addNum(int num) {
    maxHeap.offer(num);
    minHeap.offer(maxHeap.poll()); // balance
    if (minHeap.size() > maxHeap.size()) maxHeap.offer(minHeap.poll());
}

double findMedian() {
    return maxHeap.size() > minHeap.size() ? maxHeap.peek()
        : (maxHeap.peek() + minHeap.peek()) / 2.0;
}
```

---

## 12. Dynamic Programming Patterns

### **DP = "what decisions do I make at each step, and how do they depend on previous decisions?"**

### 1D DP — Fibonacci / Climbing Stairs
```java
// dp[i] = ways to reach step i
int[] dp = new int[n + 1];
dp[0] = 1; dp[1] = 1;
for (int i = 2; i <= n; i++) dp[i] = dp[i-1] + dp[i-2];
```

### Knapsack (0/1)
```java
// dp[i][w] = max value using items 0..i with capacity w
for (int i = 1; i <= n; i++) {
    for (int w = 0; w <= capacity; w++) {
        dp[i][w] = dp[i-1][w]; // skip item i
        if (weight[i] <= w) {
            dp[i][w] = Math.max(dp[i][w], dp[i-1][w - weight[i]] + value[i]); // take item i
        }
    }
}
```

### Longest Common Subsequence (LCS)
```java
int[][] dp = new int[m+1][n+1];
for (int i = 1; i <= m; i++) {
    for (int j = 1; j <= n; j++) {
        if (s1.charAt(i-1) == s2.charAt(j-1)) dp[i][j] = dp[i-1][j-1] + 1;
        else dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
    }
}
```

### Coin Change (min coins)
```java
int[] dp = new int[amount + 1];
Arrays.fill(dp, amount + 1);
dp[0] = 0;
for (int coin : coins) {
    for (int a = coin; a <= amount; a++) {
        dp[a] = Math.min(dp[a], dp[a - coin] + 1);
    }
}
```

### House Robber
```java
// dp[i] = max money robbing houses 0..i (can't rob adjacent)
int prev2 = 0, prev1 = 0;
for (int num : nums) {
    int curr = Math.max(prev1, prev2 + num);
    prev2 = prev1;
    prev1 = curr;
}
return prev1;
```

### Longest Increasing Subsequence (LIS)
```java
// O(n log n) with patience sorting
List<Integer> tails = new ArrayList<>();
for (int num : nums) {
    int pos = Collections.binarySearch(tails, num);
    if (pos < 0) pos = -(pos + 1);
    if (pos == tails.size()) tails.add(num);
    else tails.set(pos, num);
}
return tails.size();
```

---

## 13. Backtracking

### Template
```java
void backtrack(List<List<Integer>> result, List<Integer> current, int[] nums, int start) {
    result.add(new ArrayList<>(current)); // or: check if valid solution

    for (int i = start; i < nums.length; i++) {
        current.add(nums[i]);            // choose
        backtrack(result, current, nums, i + 1); // explore
        current.remove(current.size() - 1); // unchoose (backtrack)
    }
}
```
**Use for:** Subsets, Permutations, Combinations, N-Queens, Sudoku solver, Word search

---

## 14. Greedy

### Interval Scheduling (max non-overlapping)
```java
// Sort by end time, greedily pick earliest ending
Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
int count = 1, end = intervals[0][1];
for (int i = 1; i < n; i++) {
    if (intervals[i][0] >= end) { count++; end = intervals[i][1]; }
}
```

### Jump Game
```java
int maxReach = 0;
for (int i = 0; i < n; i++) {
    if (i > maxReach) return false;
    maxReach = Math.max(maxReach, i + nums[i]);
}
return true;
```

---

## 15. Intervals

### Merge Overlapping Intervals
```java
Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
List<int[]> merged = new ArrayList<>();
merged.add(intervals[0]);
for (int i = 1; i < n; i++) {
    int[] last = merged.get(merged.size() - 1);
    if (intervals[i][0] <= last[1]) {
        last[1] = Math.max(last[1], intervals[i][1]); // merge
    } else {
        merged.add(intervals[i]);
    }
}
```

### Insert Interval
```java
// Add all before, merge overlapping, add all after
List<int[]> result = new ArrayList<>();
int i = 0;
while (i < n && intervals[i][1] < newInterval[0]) result.add(intervals[i++]); // before
while (i < n && intervals[i][0] <= newInterval[1]) { // overlapping
    newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
    newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
    i++;
}
result.add(newInterval);
while (i < n) result.add(intervals[i++]); // after
```

---

## 16. Bit Manipulation

```java
// Common operations
n & 1           // check if odd
n >> 1          // divide by 2
n << 1          // multiply by 2
n & (n - 1)     // remove lowest set bit (count bits)
n ^ n = 0       // XOR with self = 0 (find single number)
a ^ b ^ a = b   // XOR is self-inverse

// Count set bits
int count = 0;
while (n > 0) { n &= (n - 1); count++; }

// Single Number (every element appears twice except one)
int result = 0;
for (int n : nums) result ^= n;
return result;

// Power of 2
boolean isPowerOfTwo = n > 0 && (n & (n - 1)) == 0;
```

---

## 17. Complexity Cheatsheet

| Data Structure | Access | Search | Insert | Delete | Space |
|---------------|--------|--------|--------|--------|-------|
| Array | O(1) | O(n) | O(n) | O(n) | O(n) |
| LinkedList | O(n) | O(n) | O(1)* | O(1)* | O(n) |
| HashMap | — | O(1) | O(1) | O(1) | O(n) |
| TreeMap | — | O(log n) | O(log n) | O(log n) | O(n) |
| Heap | — | O(n) | O(log n) | O(log n) | O(n) |
| BST (balanced) | — | O(log n) | O(log n) | O(log n) | O(n) |
| Trie | — | O(m) | O(m) | O(m) | O(Σ*m) |

| Algorithm | Time | Space | Stable? |
|-----------|------|-------|---------|
| MergeSort | O(n log n) | O(n) | Yes |
| QuickSort | O(n log n) avg | O(log n) | No |
| HeapSort | O(n log n) | O(1) | No |
| BFS/DFS | O(V + E) | O(V) | — |
| Dijkstra | O(E log V) | O(V) | — |
| Binary Search | O(log n) | O(1) | — |

---

## 18. Java Snippets

```java
// Sorting
Arrays.sort(arr);                                    // primitive array
Arrays.sort(arr, (a, b) -> a[0] - b[0]);            // 2D array by first element
Collections.sort(list, Comparator.reverseOrder());   // descending

// PriorityQueue
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
PriorityQueue<int[]> pq = new PriorityQueue<>((a,b) -> a[0] - b[0]); // by first element

// HashMap
map.getOrDefault(key, 0);
map.merge(key, 1, Integer::sum);              // increment count
map.computeIfAbsent(key, k -> new ArrayList<>()).add(val);

// String
char[] chars = s.toCharArray();
String reversed = new StringBuilder(s).reverse().toString();
String[] parts = s.split("\\s+");
String joined = String.join(",", list);

// Initialize 2D array
int[][] dp = new int[m][n];
Arrays.fill(dp[i], value);                    // fill one row
for (int[] row : dp) Arrays.fill(row, value); // fill all rows

// Directions (4-directional grid)
int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

// Deque as Stack AND Queue
Deque<Integer> stack = new ArrayDeque<>(); // push/pop/peek
Deque<Integer> queue = new ArrayDeque<>(); // offer/poll/peek

// TreeMap (sorted map)
TreeMap<Integer, Integer> map = new TreeMap<>();
map.ceilingKey(key);   // smallest key >= key
map.floorKey(key);     // largest key <= key
map.firstKey();        // minimum key
map.lastKey();         // maximum key
```

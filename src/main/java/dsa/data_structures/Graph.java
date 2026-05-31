package dsa.data_structures;

import java.util.*;

/**
 * ============================================================================
 * Implement Graph with BFS and DFS
 * ============================================================================
 * Companies: Google, Meta, Amazon
 * Difficulty: Medium
 *
 * Adjacency List representation: Map<vertex, List<neighbors>>
 * BFS: uses Queue — level-by-level traversal, shortest path in unweighted graph
 * DFS: uses Stack/recursion — explore as deep as possible, then backtrack
 */
public class Graph {

    private final Map<Integer, List<Integer>> adjList = new HashMap<>();
    private final boolean directed;

    public Graph(boolean directed) { this.directed = directed; }

    public void addVertex(int v) { adjList.putIfAbsent(v, new ArrayList<>()); }

    public void addEdge(int from, int to) {
        adjList.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        if (!directed) adjList.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
    }

    // BFS — O(V + E) — level-by-level, shortest path in unweighted
    public List<Integer> bfs(int start) {
        List<Integer> result = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        visited.add(start);
        queue.offer(start);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            result.add(vertex);
            for (int neighbor : adjList.getOrDefault(vertex, List.of())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
        return result;
    }

    // DFS — O(V + E) — go deep, then backtrack
    public List<Integer> dfs(int start) {
        List<Integer> result = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        dfsHelper(start, visited, result);
        return result;
    }

    private void dfsHelper(int vertex, Set<Integer> visited, List<Integer> result) {
        visited.add(vertex);
        result.add(vertex);
        for (int neighbor : adjList.getOrDefault(vertex, List.of())) {
            if (!visited.contains(neighbor)) {
                dfsHelper(neighbor, visited, result);
            }
        }
    }

    // Has path between source and destination
    public boolean hasPath(int src, int dest) {
        Set<Integer> visited = new HashSet<>();
        return hasPathDFS(src, dest, visited);
    }

    private boolean hasPathDFS(int current, int dest, Set<Integer> visited) {
        if (current == dest) return true;
        visited.add(current);
        for (int neighbor : adjList.getOrDefault(current, List.of())) {
            if (!visited.contains(neighbor) && hasPathDFS(neighbor, dest, visited)) return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("=== Graph with BFS/DFS ===\n");
        Graph g = new Graph(false); // undirected
        g.addEdge(0, 1); g.addEdge(0, 2); g.addEdge(1, 3);
        g.addEdge(2, 4); g.addEdge(3, 4); g.addEdge(4, 5);

        System.out.println("BFS from 0: " + g.bfs(0));
        System.out.println("DFS from 0: " + g.dfs(0));
        System.out.println("hasPath(0→5): " + g.hasPath(0, 5));
        System.out.println("hasPath(0→9): " + g.hasPath(0, 9));

        System.out.println("\nBFS/DFS: O(V + E)");
    }
}

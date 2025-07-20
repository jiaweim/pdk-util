package pdk.util.graph.util;

import pdk.util.graph.Edge;
import pdk.util.graph.UndirectedGraph;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Find the shortest path using Breadth first search O(V+E).
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 02 Dec 2024, 1:31 PM
 */
public class BreadthFirstPaths<V> {

    private boolean[] marked;
    private int[] edgeTo;
    private final int s;

    public BreadthFirstPaths(UndirectedGraph<V> G, int s) {
        this.s = s;
        edgeTo = new int[G.getNodeCount()];
        marked = new boolean[G.getNodeCount()];
        Deque<Integer> queue = new ArrayDeque<>();
        marked[s] = true;
        queue.addLast(s);
        while (!queue.isEmpty()) {
            int v = queue.removeFirst();
            for (Edge edge : G.getEdges(v)) {
                int u = G.getOppositeNode(edge, v);
                if (!marked[u]) {
                    edgeTo[u] = v;
                    marked[u] = true;
                    queue.addLast(u);
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Deque<Integer> stack = new ArrayDeque<>();
        for (int x = v; x != s; x = edgeTo[x]) {
            stack.push(x);
        }
        stack.push(s);
        return stack;
    }
}

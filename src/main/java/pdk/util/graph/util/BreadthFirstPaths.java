package pdk.util.graph.util;

import pdk.util.graph.Edge;
import pdk.util.graph.UndirectedGraph;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Find the shortest path using Breadth first search O(V+E).
 *
 * @param <V> type of the node
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 02 Dec 2024, 1:31 PM
 */
public class BreadthFirstPaths<V> {

    private final Map<V, Boolean> marked;
    private final Map<V, V> edgeTo;
    private final V s;

    public BreadthFirstPaths(UndirectedGraph<V> G, V s) {
        this.s = s;
        edgeTo = new HashMap<>(G.getNodeCount());
        marked = new HashMap<>(G.getNodeCount());
        for (V v : G.getNodeSet()) {
            marked.put(v, false);
        }
        marked.put(s, true);

        Deque<V> queue = new ArrayDeque<>();

        queue.addLast(s);
        while (!queue.isEmpty()) {
            V v = queue.removeFirst();
            for (Edge<V> edge : G.getEdges(v)) {
                V u = G.getOppositeNode(edge, v);
                if (!marked.get(u)) {
                    edgeTo.put(u, v);
                    marked.put(u, true);
                    queue.addLast(u);
                }
            }
        }
    }

    public boolean hasPathTo(V v) {
        return marked.get(v);
    }

    public Iterable<V> pathTo(V v) {
        if (!hasPathTo(v)) return null;
        Deque<V> stack = new ArrayDeque<>();
        for (V x = v; x != s; x = edgeTo.get(x)) {
            stack.push(x);
        }
        stack.push(s);
        return stack;
    }
}

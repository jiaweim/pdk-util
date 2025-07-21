package pdk.util.graph.util;

import pdk.util.graph.Edge;
import pdk.util.graph.UndirectedGraph;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;

/**
 * 该算法用处不大。
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 02 Dec 2024, 1:14 PM
 */
public class DepthFirstPaths<V> {

    private final HashMap<V, Boolean> marked;
    private final HashMap<V, V> edgeTo;
    private final V s;

    public DepthFirstPaths(UndirectedGraph<V> graph, V s) {
        this.s = s;
        marked = new HashMap<>(graph.getNodeCount());
        for (V v : graph.getNodeSet()) {
            marked.put(v, false);
        }
        marked.put(s, true);
        edgeTo = new HashMap<>(graph.getNodeCount());

        Deque<V> stack = new ArrayDeque<>();
        stack.push(s);
        while (!stack.isEmpty()) {
            V v1 = stack.peek();
            V v2 = null;
            for (Edge<V> edge : graph.getEdges(v1)) {
                V v = graph.getOppositeNode(edge, v1);
                if (!marked.get(v)) {
                    v2 = v;
                    break;
                }
            }
            if (v2 == null) {
                stack.pop();
            } else {
                edgeTo.put(v2, v1);
                marked.put(v2, true);
                stack.push(v2);
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

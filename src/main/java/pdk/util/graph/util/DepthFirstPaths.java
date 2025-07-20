package pdk.util.graph.util;

import pdk.util.graph.Edge;
import pdk.util.graph.UndirectedGraph;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 该算法用处不大。
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 02 Dec 2024, 1:14 PM
 */
public class DepthFirstPaths<V> {

    private final boolean[] marked;
    private final int[] edgeTo;
    private final UndirectedGraph<V> graph;
    private final int s;

    public DepthFirstPaths(UndirectedGraph<V> graph, int s) {
        this.graph = graph;
        this.s = s;
        marked = new boolean[graph.getNodeCount()];
        edgeTo = new int[graph.getNodeCount()];
        marked[s] = true;

        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(s);
        while (!stack.isEmpty()) {
            int v1 = stack.peek();
            int v2 = -1;
            for (Edge edge : graph.getEdges(v1)) {
                int v = graph.getOppositeNode(edge, v1);
                if (!marked[v]) {
                    v2 = v;
                    break;
                }
            }
            if (v2 == -1) {
                stack.pop();
            } else {
                edgeTo[v2] = v1;
                marked[v2] = true;
                stack.push(v2);
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

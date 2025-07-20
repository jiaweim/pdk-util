package pdk.util.graph.util;

import org.jspecify.annotations.Nullable;
import pdk.util.graph.Digraph;
import pdk.util.graph.Edge;

import java.util.LinkedList;
import java.util.List;

/**
 * Finding a directed path from a source node to every other node in the digraph using depth-first search (unweighted).
 * <p>
 * O(E+V) time.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 02 Dec 2024, 6:39 PM
 */
public class DiDFSPaths<V> {

    private final boolean[] marked;
    private final int[] edgeTo;
    private final int s;

    /**
     * Compute a directed path from <code>s</code> to every other node in digraph
     *
     * @param g {@link Digraph}
     * @param s source node
     */
    public DiDFSPaths(Digraph<V> g, int s) {
        int V = g.getNodeCount();
        marked = new boolean[V];
        edgeTo = new int[V];
        this.s = s;
        validateNode(s);
        dfs(g, s);
    }

    private void dfs(Digraph<V> g, int v) {
        marked[v] = true;
        for (Edge edge : g.getOutgoingEdges(v)) {
            int w = edge.getTarget();
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(g, w);
            }
        }
    }

    /**
     * Is there a directed path from the source node to node <code>v</code>
     *
     * @param v a node
     * @return true if there is a directed path from the source node <code>s</code> to node <code>v</code>,
     * false otherwise.
     */
    public boolean hasPathTo(int v) {
        validateNode(v);
        return marked[v];
    }

    /**
     * Return a directed path from the source node to a node <code>v</code>, or null if no such path.
     *
     * @param v the node
     * @return the sequence of nodes on a directed path from the source node <code>s</code> to node <code>v</code>
     */
    @Nullable
    public List<Integer> getPath(int v) {
        validateNode(v);
        if (!hasPathTo(v)) {
            return null;
        }
        LinkedList<Integer> list = new LinkedList<>();
        for (int x = v; x != s; x = edgeTo[x]) {
            list.add(x);
        }
        list.add(s);
        return list.reversed();
    }

    private void validateNode(int v) {
        if (v < 0 || v >= marked.length) {
            throw new IllegalArgumentException("node " + s + " is not between 0 and " + (marked.length - 1));
        }
    }
}

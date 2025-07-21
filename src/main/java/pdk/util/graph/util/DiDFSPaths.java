package pdk.util.graph.util;

import org.jspecify.annotations.Nullable;
import pdk.util.graph.Digraph;
import pdk.util.graph.Edge;

import java.util.HashMap;
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

    private final HashMap<V, Boolean> marked;
    private final HashMap<V, V> edgeTo;
    private final V s;

    /**
     * Compute a directed path from <code>s</code> to every other node in digraph
     *
     * @param g {@link Digraph}
     * @param s source node
     */
    public DiDFSPaths(Digraph<V> g, V s) {
        int V = g.getNodeCount();
        marked = new HashMap<>(V);
        edgeTo = new HashMap<>(V);
        for (V v : g.getNodeSet()) {
            marked.put(v, false);
        }
        this.s = s;
        dfs(g, s);
    }

    private void dfs(Digraph<V> g, V v) {
        marked.put(v, true);

        for (Edge<V> edge : g.getOutgoingEdges(v)) {
            V w = edge.getTarget();
            if (!marked.get(w)) {
                edgeTo.put(w, v);
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
    public boolean hasPathTo(V v) {
        return marked.get(v);
    }

    /**
     * Return a directed path from the source node to a node <code>v</code>, or null if no such path.
     *
     * @param v the node
     * @return the sequence of nodes on a directed path from the source node <code>s</code> to node <code>v</code>
     */
    @Nullable
    public List<V> getPath(V v) {
        if (!hasPathTo(v)) {
            return null;
        }
        LinkedList<V> list = new LinkedList<>();
        for (V x = v; x != s; x = edgeTo.get(x)) {
            list.add(x);
        }
        list.add(s);
        return list.reversed();
    }
}

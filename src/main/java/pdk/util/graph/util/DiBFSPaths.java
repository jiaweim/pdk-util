package pdk.util.graph.util;

import pdk.util.graph.Digraph;
import pdk.util.graph.Edge;

import java.util.*;

/**
 * Find the shortest path from a source node to every other node in the digraph using breadth-first-search (unweighted).
 * <p>
 * O(E+V) time.
 * ⭐
 *
 * @param <V> type of the node
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 02 Dec 2024, 7:01 PM
 */
public class DiBFSPaths<V> {

    private static final int INFINITY = Integer.MAX_VALUE;
    // is there an s->v path
    private HashMap<V, Boolean> marked;
    private HashMap<V, V> edgeTo;
    private HashMap<V, Integer> distTo;

    /**
     * Computes the shorted path from <code>s</code> to every other node in graph.
     *
     * @param g a {@link Digraph}
     * @param s source node
     */
    public DiBFSPaths(Digraph<V> g, V s) {
        int V = g.getNodeCount();
        marked = new HashMap<>(V);
        edgeTo = new HashMap<>(V);
        distTo = new HashMap<>(V);
        for (V v : g.getNodeSet()) {
            marked.put(v, false);
            distTo.put(v, INFINITY);
        }

        bfs(g, s);
    }

    /**
     * Computes the shorted path from any one of the source nodes in <code>sources</code> to every other
     * node in graph
     *
     * @param g       a {@link Digraph}
     * @param sources source nodes
     */
    public DiBFSPaths(Digraph<V> g, Collection<V> sources) {
        int V = g.getNodeCount();
        marked = new HashMap<>(V);
        edgeTo = new HashMap<>(V);
        distTo = new HashMap<>(V);
        for (V v : g.getNodeSet()) {
            distTo.put(v, INFINITY);
            marked.put(v, false);
        }
        bfs(g, sources);
    }

    private void bfs(Digraph<V> g, V s) {
        Deque<V> queue = new ArrayDeque<>();

        marked.put(s, true);
        distTo.put(s, 0);
        queue.addLast(s);
        while (!queue.isEmpty()) {
            V v = queue.removeFirst();
            for (Edge<V> edge : g.getOutgoingEdges(v)) {
                V u = edge.getTarget();
                if (!marked.get(u)) {
                    marked.put(u, true);
                    edgeTo.put(u, v);
                    distTo.put(u, distTo.get(v) + 1);
                    queue.addLast(u);
                }
            }
        }
    }

    /**
     * breadth first search for multiple sources
     */
    private void bfs(Digraph<V> g, Collection<V> sources) {
        Deque<V> queue = new ArrayDeque<>();
        for (V source : sources) {
            marked.put(source, true);
            distTo.put(source, 0);
            queue.addLast(source);
        }
        while (!queue.isEmpty()) {
            V v = queue.removeFirst();
            for (Edge<V> edge : g.getOutgoingEdges(v)) {
                V u = edge.getTarget();
                if (!marked.get(u)) {
                    marked.put(u, true);
                    edgeTo.put(u, v);
                    distTo.put(u, distTo.get(v) + 1);
                    queue.addLast(u);
                }
            }
        }
    }

    /**
     * Is there a directed path from the source <code>s</code> to node <code>v</code>
     *
     * @param v a node
     * @return true if there is a directed path, false otherwise
     */
    public boolean hasPathTo(V v) {
        return marked.get(v);
    }

    /**
     * Return the number of edges in a shorted path from the source node to node <code>v</code>
     *
     * @param v a node
     * @return number of edges in a shorted path
     */
    public int getDistance(V v) {
        return distTo.get(v);
    }

    /**
     * Return a shortest path from source node s to node <code>v</code>
     *
     * @param v a node
     * @return the sequence of nodes on a shortest path
     */
    public List<V> getPath(V v) {
        if (!hasPathTo(v))
            return null;
        LinkedList<V> path = new LinkedList<>();
        V x;
        for (x = v; distTo.get(x) != 0; x = edgeTo.get(x)) {
            path.add(x);
        }
        path.add(x);
        return path.reversed();
    }
}

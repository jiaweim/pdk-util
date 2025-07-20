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
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 02 Dec 2024, 7:01 PM
 */
public class DiBFSPaths<V> {

    private static final int INFINITY = Integer.MAX_VALUE;
    // is there an s->v path
    private boolean[] marked;
    private int[] edgeTo;
    private int[] distTo;

    /**
     * Computes the shorted path from <code>s</code> to every other node in graph.
     *
     * @param g a {@link Digraph}
     * @param s source node
     */
    public DiBFSPaths(Digraph<V> g, int s) {
        int V = g.getNodeCount();
        marked = new boolean[V];
        edgeTo = new int[V];
        distTo = new int[V];
        for (int v = 0; v < V; v++) {
            distTo[v] = INFINITY;
        }
        validateNode(s);
        bfs(g, s);
    }

    /**
     * Computes the shorted path from any one of the source nodes in <code>sources</code> to every other
     * node in graph
     *
     * @param g       a {@link Digraph}
     * @param sources source nodes
     */
    public DiBFSPaths(Digraph<V> g, Collection<Integer> sources) {
        int V = g.getNodeCount();
        marked = new boolean[V];
        edgeTo = new int[V];
        distTo = new int[V];
        for (int v = 0; v < V; v++) {
            distTo[v] = INFINITY;
        }
        for (Integer source : sources) {
            validateNode(source);
        }
        bfs(g, sources);
    }

    private void bfs(Digraph<V> g, int s) {
        Deque<Integer> queue = new ArrayDeque<>();
        marked[s] = true;
        distTo[s] = 0;
        queue.addLast(s);
        while (!queue.isEmpty()) {
            int v = queue.removeFirst();
            for (Edge edge : g.getOutgoingEdges(v)) {
                int u = edge.getTarget();
                if (!marked[u]) {
                    marked[u] = true;
                    edgeTo[u] = v;
                    distTo[u] = distTo[v] + 1;
                    queue.addLast(u);
                }
            }
        }
    }

    /**
     * breadth first search for multiple sources
     */
    private void bfs(Digraph<V> g, Collection<Integer> sources) {
        Deque<Integer> queue = new ArrayDeque<>();
        for (int source : sources) {
            marked[source] = true;
            distTo[source] = 0;
            queue.addLast(source);
        }
        while (!queue.isEmpty()) {
            int v = queue.removeFirst();
            for (Edge edge : g.getOutgoingEdges(v)) {
                int u = edge.getTarget();
                if (!marked[u]) {
                    marked[u] = true;
                    edgeTo[u] = v;
                    distTo[u] = distTo[v] + 1;
                    queue.addLast(u);
                }
            }
        }
    }

    private void validateNode(int v) {
        int V = marked.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("node " + v + " is not between 0 and " + (V - 1));
    }

    /**
     * Is there a directed path from the source <code>s</code> to node <code>v</code>
     *
     * @param v a node
     * @return true if there is a directed path, false otherwise
     */
    public boolean hasPathTo(int v) {
        validateNode(v);
        return marked[v];
    }

    /**
     * Return the number of edges in a shorted path from the source node to node <code>v</code>
     *
     * @param v a node
     * @return number of edges in a shorted path
     */
    public int getDistance(int v) {
        validateNode(v);
        return distTo[v];
    }

    /**
     * Return a shortest path from source node s to node <code>v</code>
     *
     * @param v a node
     * @return the sequence of nodes on a shortest path
     */
    public List<Integer> getPath(int v) {
        validateNode(v);

        if (!hasPathTo(v))
            return null;
        LinkedList<Integer> path = new LinkedList<>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x]) {
            path.add(x);
        }
        path.add(x);
        return path.reversed();
    }
}

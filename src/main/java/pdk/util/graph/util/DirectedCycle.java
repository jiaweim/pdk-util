package pdk.util.graph.util;

import pdk.util.graph.Digraph;
import pdk.util.graph.Edge;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * Find a directed cycle in a digraph using depth-first search.
 * <p>
 * run time O(V+E), space O(|V|)
 *
 * @author Jiawei Mao ⭐⭐
 * @version 1.0.0
 * @since 02 Dec 2024, 7:32 PM
 */
public class DirectedCycle<V> {

    private final Digraph<V> digraph;
    private final boolean[] visited;
    private final boolean[] beingVisit;
    // help to print cycle
    private final int[] edgeTo;
    private Deque<Integer> cycle;

    /**
     * Determine whether the digraph has a directed cycle
     *
     * @param digraph a {@link Digraph}
     */
    public DirectedCycle(Digraph<V> digraph) {
        this.digraph = digraph;

        int V = digraph.getNodeCount();
        visited = new boolean[V];
        edgeTo = new int[V];
        beingVisit = new boolean[V];
        for (int v = 0; v < V; v++) {
            if (!visited[v] && cycle == null) {
                dfs(v);
            }
        }
    }

    private void dfs(int v) {
        beingVisit[v] = true;
        for (Edge edge : digraph.getOutgoingEdges(v)) {
            int w = edge.getTarget();
            if (cycle != null) {
                return;
            } else if (beingVisit[w]) { // find a cycle
                cycle = new ArrayDeque<>();
                cycle.push(w);
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                assert Objects.equals(cycle.getFirst(), cycle.getLast());
            } else if (!visited[w]) {
                edgeTo[w] = v;
                dfs(w);
            }
        }
        beingVisit[v] = false;
        visited[v] = true;
    }

    /**
     * @return a directed cycle if the digraph has a directed cycle, null otherwise
     */
    public Deque<Integer> getCycle() {
        return cycle;
    }

    /**
     * @return true if the digraph has a directed cycle, false otherwise.
     */
    public boolean hasCycle() {
        return cycle != null;
    }

}

package pdk.util.graph.util;

import pdk.util.graph.Digraph;
import pdk.util.graph.Edge;

import java.util.*;

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
    private final Map<V, Boolean> visited;
    private final Map<V, Boolean> beingVisit;
    // help to print cycle
    private final Map<V, V> edgeTo;
    private Deque<V> cycle;

    /**
     * Determine whether the digraph has a directed cycle
     *
     * @param digraph a {@link Digraph}
     */
    public DirectedCycle(Digraph<V> digraph) {
        this.digraph = digraph;

        int V = digraph.getNodeCount();

        visited = new HashMap<>(V);
        beingVisit = new HashMap<>(V);
        for (V v : digraph.getNodeSet()) {
            visited.put(v, false);
            beingVisit.put(v, false);
        }
        edgeTo = new HashMap<>(V);
        for (V v : digraph.getNodeSet()) {
            if (!visited.get(v) && cycle == null) {
                dfs(v);
            }
        }
    }

    private void dfs(V v) {
        beingVisit.put(v, true);
        for (Edge<V> edge : digraph.getOutgoingEdges(v)) {
            V w = edge.getTarget();
            if (cycle != null) {
                return;
            } else if (beingVisit.get(w)) { // find a cycle
                cycle = new ArrayDeque<>();
                cycle.push(w);
                for (V x = v; x != w; x = edgeTo.get(x)) {
                    cycle.push(x);
                }
                cycle.push(w);
                assert Objects.equals(cycle.getFirst(), cycle.getLast());
            } else if (!visited.get(w)) {
                edgeTo.put(w, v);
                dfs(w);
            }
        }
        beingVisit.put(v, false);
        visited.put(v, true);
    }

    /**
     * @return a directed cycle if the digraph has a directed cycle, null otherwise
     */
    public Deque<V> getCycle() {
        return cycle;
    }

    /**
     * @return true if the digraph has a directed cycle, false otherwise.
     */
    public boolean hasCycle() {
        return cycle != null;
    }

}

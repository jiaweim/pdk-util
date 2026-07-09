package pdk.util.graph.util;

import pdk.util.graph.Digraph;
import pdk.util.graph.Edge;

import java.util.*;

/**
 * Detect directed cycle in a digraph using depth-first search.
 * <p>
 * run time O(V+E), space O(|V|)
 * <p>
 * 2026-07-09: use a state map to replace visitedMap and beingVisitedMap.
 *
 * @param <V> type of graph node
 * @author Jiawei Mao ⭐⭐
 * @version 1.1.0
 * @since 02 Dec 2024, 7:32 PM
 */
public class DirectedCycle<V> {

    /**
     * not visited
     */
    private static final byte WHITE = 0;
    /**
     * being visited, in stack
     */
    private static final byte GRAY = 1;
    /**
     * visited
     */
    private static final byte BLACK = 2;

    private final Digraph<V> digraph;
    private final Map<V, Byte> stateMap;
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

        stateMap = new HashMap<>(V);
        for (V v : digraph.getNodeSet()) {
            stateMap.put(v, WHITE);
        }
        edgeTo = new HashMap<>(V);
        for (V v : digraph.getNodeSet()) {
            if (stateMap.get(v) == WHITE && cycle == null) {
                dfs(v);
            }
        }
    }

    private void dfs(V start) {
        Deque<Frame<V>> stack = new ArrayDeque<>();
        stateMap.put(start, GRAY);
        stack.push(new Frame<>(start, digraph.getOutgoingEdges(start)));

        while (!stack.isEmpty()) {
            final Frame<V> frame = stack.peek();
            if (frame.index >= frame.edges.size()) {
                stateMap.put(frame.vertex, BLACK);
                stack.pop();
                continue;
            }

            Edge<V> edge = frame.edges.get(frame.index++);
            V w = edge.getTarget();
            byte color = stateMap.get(w);
            if (color == GRAY) {
                cycle = new ArrayDeque<>();
                cycle.push(w);
                for (V x = frame.vertex; !Objects.equals(x, w); x = edgeTo.get(x)) {
                    cycle.push(x);
                }
                cycle.push(w);
                assert Objects.equals(cycle.getFirst(), cycle.getLast());
                return;
            }

            if (color == WHITE) {
                edgeTo.put(w, frame.vertex);
                stateMap.put(w, GRAY);
                stack.push(new Frame<>(w, digraph.getOutgoingEdges(w)));
            }
        }
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

package pdk.util.graph.util;

import org.jspecify.annotations.Nullable;
import pdk.util.graph.Digraph;
import pdk.util.graph.Edge;
import pdk.util.graph.GraphPath;
import pdk.util.graph.PathFinder;

import java.util.*;

/**
 * The Bellman-Ford algorithm, the most general shortest path algorithm.
 * <p>
 * A Single-source shortest path algorithm for directed weighted graph with no negative cycles.
 * <p>
 * The edge weights can be positive, negative, or zero.
 * <p>
 * This class find either the shortest path from the source node to every other nodes or a
 * negative cycle reachable from the source node.
 * <p>
 * This implementation uses a queue-based implementation of the Bellman-Fold-Moore algorithm.
 *
 * <p>
 * time complexity: O(EV) in the worst case. In practice, it performs much better.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Dec 2024, 7:26 PM ⭐
 */
public class BellmanFordShortestPath<V> implements PathFinder<V> {

    private static final double EPSILON = 1E-14;

    private final Digraph<V> graph;
    private final V startNode;
    private final HashMap<V, Double> distTo;
    private final HashMap<V, Edge<V>> edgeTo;
    private final HashMap<V, Boolean> onQueue;
    private final ArrayDeque<V> queue;
    // number of calls to relax
    private int cost;
    private Deque<V> cycle;

    /**
     * Compute the shortest path tree from <code>s</code> to every other node in the graph.
     *
     * @param graph a weighted {@link Digraph}
     * @param s     index of the source node
     */
    public BellmanFordShortestPath(Digraph<V> graph, V s) {
        this.graph = graph;
        this.startNode = s;
        int V = graph.getNodeCount();

        distTo = new HashMap<>(V);
        edgeTo = new HashMap<>(V);
        onQueue = new HashMap<>(V);

        for (V v : graph.getNodeSet()) {
            distTo.put(v, Double.POSITIVE_INFINITY);
            onQueue.put(v, false);
        }
        distTo.put(s, 0.0);

        // Bellman-Ford
        queue = new ArrayDeque<>();
        queue.addLast(s);
        onQueue.put(s, true);
        while (!queue.isEmpty() && !hasNegativeCycle()) {
            V v = queue.pollFirst();
            onQueue.put(v, false);
            relax(v);
        }
    }

    private void relax(V v) {
        for (Edge<V> edge : graph.getOutgoingEdges(v)) {
            V w = edge.getTarget();
            if (distTo.get(w) > distTo.get(v) + edge.getWeight() + EPSILON) {
                distTo.put(w, distTo.get(v) + edge.getWeight());
                edgeTo.put(w, edge);
                if (!onQueue.get(w)) {
                    queue.addLast(w);
                    onQueue.put(w, true);
                }
            }
            if (++cost % graph.getNodeCount() == 0) {
                findNegativeCycle();
                if (hasNegativeCycle()) {
                    return; // find a negative cycle
                }
            }
        }
    }

    // finding a cycle in predecessor graph
    private void findNegativeCycle() {
        Set<Edge<V>> edges = new HashSet<>();
        Set<V> nodes = new HashSet<>();
        for (Edge<V> edge : edgeTo.values()) {
            if (edge != null) {
                nodes.add(edge.getSource());
                nodes.add(edge.getTarget());
                edges.add(edge);
            }
        }
        Digraph<V> g = new Digraph<>(nodes);
        for (Edge<V> edge : edges) {
            g.addEdge(edge.getSource(), edge.getTarget(), edge.getWeight());
        }

        DirectedCycle<V> directedCycle = new DirectedCycle<>(g);
        cycle = directedCycle.getCycle();
    }

    /**
     * Is there a negative cycle reachable from the source node
     *
     * @return true if there is a negative reachable from the source node, false otherwise.
     */
    public boolean hasNegativeCycle() {
        return cycle != null;
    }

    /**
     * @return a directed cycle if the digraph has a directed cycle, null otherwise
     */
    public Queue<V> getCycle() {
        return cycle;
    }

    @Override
    public double getWeight(V targetNode) {
        if (hasNegativeCycle()) {
            throw new UnsupportedOperationException("Negative cycle exists");
        }
        return distTo.get(targetNode);
    }

    @Override
    public boolean hasPathTo(V node) {
        return distTo.get(node) < Double.POSITIVE_INFINITY;
    }

    @Override
    public @Nullable GraphPath<V> getPath(V targetNode) {
        if (hasNegativeCycle()) {
            throw new UnsupportedOperationException("Negative cycle exists");
        }
        if (!hasPathTo(targetNode))
            return null;
        Deque<Edge<V>> path = new ArrayDeque<>();
        Deque<V> nodes = new ArrayDeque<>();
        nodes.addFirst(targetNode);
        for (Edge<V> e = edgeTo.get(targetNode); e != null; e = edgeTo.get(e.getSource())) {
            path.push(e);
            nodes.addFirst(e.getSource());
        }
        return new GraphPath<>(graph, startNode, targetNode,
                nodes, path, distTo.get(targetNode));
    }
}

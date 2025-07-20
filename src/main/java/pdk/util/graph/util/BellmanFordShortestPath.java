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
    private final int startNode;
    private final double[] distTo;
    private final Edge[] edgeTo;
    private final boolean[] onQueue;
    private final Deque<Integer> queue;
    // number of calls to relax)_
    private int cost;
    private Deque<Integer> cycle;

    /**
     * Compute the shortest path tree from <code>s</code> to every other node in the graph.
     *
     * @param graph a weighted {@link Digraph}
     * @param s     source node value
     */
    public BellmanFordShortestPath(Digraph<V> graph, V s) {
        this(graph, graph.indexOf(s));
    }

    /**
     * Compute a shortest path tree from <code>s</code> to every other node in the graph.
     *
     * @param graph a weighted {@link Digraph}
     * @param s     index of the source node
     */
    public BellmanFordShortestPath(Digraph<V> graph, int s) {
        this.graph = graph;
        this.startNode = s;
        int V = graph.getNodeCount();

        distTo = new double[V];
        edgeTo = new Edge[V];
        onQueue = new boolean[V];

        for (int v = 0; v < V; v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[s] = 0;

        // Bellman-Ford
        queue = new ArrayDeque<>();
        queue.offerLast(s);
        onQueue[s] = true;
        while (!queue.isEmpty() && !hasNegativeCycle()) {
            int v = queue.pollFirst();
            onQueue[v] = false;
            relax(v);
        }
    }

    private void relax(int v) {
        for (Edge edge : graph.getOutgoingEdges(v)) {
            int w = edge.getTarget();
            if (distTo[w] > distTo[v] + edge.getWeight() + EPSILON) {
                distTo[w] = distTo[v] + edge.getWeight();
                edgeTo[w] = edge;
                if (!onQueue[w]) {
                    queue.offerLast(w);
                    onQueue[w] = true;
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
        Set<Edge> edges = new HashSet<>();
        Set<V> nodes = new HashSet<>();
        for (Edge edge : edgeTo) {
            if (edge != null) {
                nodes.add(graph.getNode(edge.getSource()));
                nodes.add(graph.getNode(edge.getTarget()));
                edges.add(edge);
            }
        }
        Digraph<V> g = new Digraph<>(nodes);
        for (Edge edge : edges) {
            g.addEdge(graph.getNode(edge.getSource()), graph.getNode(edge.getTarget()), edge.getWeight());
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
    public Queue<Integer> getCycle() {
        return cycle;
    }

    @Override
    public int indexOf(V node) {
        return graph.indexOf(node);
    }

    @Override
    public double getWeight(int targetNode) {
        validateVertex(targetNode);
        if (hasNegativeCycle()) {
            throw new UnsupportedOperationException("Negative cycle exists");
        }
        return distTo[targetNode];
    }

    @Override
    public boolean hasPathTo(int node) {
        validateVertex(node);
        return distTo[node] < Double.POSITIVE_INFINITY;
    }

    @Override
    public @Nullable GraphPath<V> getPath(int targetNode) {
        validateVertex(targetNode);
        if (hasNegativeCycle()) {
            throw new UnsupportedOperationException("Negative cycle exists");
        }
        if (!hasPathTo(targetNode))
            return null;
        Deque<Edge> path = new ArrayDeque<>();
        Deque<Integer> nodes = new ArrayDeque<>();
        nodes.addFirst(targetNode);
        for (Edge e = edgeTo[targetNode]; e != null; e = edgeTo[e.getSource()]) {
            path.push(e);
            nodes.addFirst(e.getSource());
        }
        return new GraphPath<>(graph, graph.getNode(startNode), graph.getNode(targetNode),
                graph.getNodes(nodes), path, distTo[targetNode]);
    }

    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("node " + v + " is not between 0 and " + (V - 1));
    }
}

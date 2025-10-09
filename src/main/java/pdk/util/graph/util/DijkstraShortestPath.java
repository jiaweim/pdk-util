package pdk.util.graph.util;

import org.jheaps.AddressableHeap;
import org.jheaps.tree.PairingHeap;
import pdk.util.graph.Digraph;
import pdk.util.graph.Edge;
import pdk.util.graph.GraphPath;
import pdk.util.graph.PathFinder;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * An implementation of Dijkstra's algorithm for single-source shortest path problem in directed weighted graph, where
 * the edge weight are non-negative, allow cycle.
 * <p>
 * running time O(ElogV) in the worst case, space O(V).
 *
 * @param <V> type of graph node
 * @author Jiawei Mao⭐⭐
 * @version 1.0.0
 * @since 25 Nov 2024, 3:16 PM
 */
public class DijkstraShortestPath<V> implements PathFinder<V> {

    private final Digraph<V> graph_;
    private final V startNode_;
    private final HashMap<V, Double> distTo_;
    private final HashMap<V, Edge<V>> edgeTo_;

    /**
     * Computes a shortest-path from the source node <code>startNode</code> to every other node in the directed weighted
     * graph.
     *
     * @param graph     a {@link Digraph}
     * @param startNode source node
     */
    public DijkstraShortestPath(Digraph<V> graph, V startNode) {
        graph_ = graph;
        startNode_ = startNode;
        for (Edge<V> edge : graph.getEdgeSet()) {
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException("Edge " + edge + " has negative weight");
            }
        }

        int V = graph.getNodeCount();
        distTo_ = new HashMap<>(V);
        edgeTo_ = new HashMap<>(V);
        HashMap<V, Boolean> visited = new HashMap<>(V);
        for (V v : graph.getNodeSet()) {
            distTo_.put(v, Double.POSITIVE_INFINITY);
            visited.put(v, false);
        }
        distTo_.put(startNode, 0.0);

        PairingHeap<Double, V> heap = new PairingHeap<>();
        heap.insert(distTo_.get(startNode), startNode);
        while (!heap.isEmpty()) {
            AddressableHeap.Handle<Double, V> handle = heap.deleteMin();
            V v = handle.getValue();
            for (Edge<V> edge : graph.getOutgoingEdges(v)) {
                V w = edge.getTarget();
                if (visited.get(w))
                    continue;
                // relaxation
                if (distTo_.get(w) > distTo_.get(v) + edge.getWeight()) {
                    distTo_.put(w, distTo_.get(v) + edge.getWeight());
                    edgeTo_.put(w, edge);
                }
                heap.insert(distTo_.get(w), w);
            }
            visited.put(v, true);
        }
    }

    @Override
    public double getWeight(V targetNode) {
        return distTo_.get(targetNode);
    }

    @Override
    public boolean hasPathTo(V node) {
        return distTo_.get(node) < Double.POSITIVE_INFINITY;
    }

    @Override
    public GraphPath<V> getPath(V targetNode) {
        LinkedList<Edge<V>> edges = new LinkedList<>();
        LinkedList<V> nodeList = new LinkedList<>();
        nodeList.add(targetNode);

        V tmp = targetNode;
        while (edgeTo_.get(tmp) != null) {
            edges.add(edgeTo_.get(tmp));
            tmp = edgeTo_.get(tmp).getSource();
            nodeList.add(tmp);
        }
        return new GraphPath<>(graph_, startNode_, targetNode, nodeList.reversed(), edges.reversed(),
                getWeight(targetNode));
    }
}

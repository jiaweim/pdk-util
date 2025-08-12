package pdk.util.graph;

import java.util.Set;

/**
 * Minimum Spanning tree
 *
 * @param <V> type of the node
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 29 Nov 2024, 5:24 PM
 */
public class SpanningTree<V> {

    private final Set<Edge<V>> edges;
    private final double weight;

    public SpanningTree(Set<Edge<V>> edges, double weight) {
        this.edges = edges;
        this.weight = weight;
    }

    public Set<Edge<V>> getEdges() {
        return edges;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return edges.toString();
    }
}

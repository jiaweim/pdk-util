package pdk.util.graph;

import java.util.Set;

/**
 * Minimum Spanning tree
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 29 Nov 2024, 5:24 PM
 */
public class MinimumSpanningTree {

    private final Set<Edge> edges;
    private final double weight;

    public MinimumSpanningTree(Set<Edge> edges, double weight) {
        this.edges = edges;
        this.weight = weight;
    }

    public Set<Edge> getEdges() {
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

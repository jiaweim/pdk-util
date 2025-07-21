package pdk.util.graph.util;

import pdk.util.graph.Edge;
import pdk.util.graph.SpanningTree;
import pdk.util.graph.SpanningTreeAlgorithm;
import pdk.util.graph.UndirectedGraph;

import java.util.*;

/**
 * Implementation of the Kruskal's algorithm.
 * <p>
 * This algorithm works for undirected weighted graph.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 21 Jul 2025, 5:57 PM
 */
public class KruskalMinimumSpanningTree<V> implements SpanningTreeAlgorithm<V> {

    private final UndirectedGraph<V> graph_;

    /**
     * Create {@link KruskalMinimumSpanningTree}
     *
     * @param graph_ {@link UndirectedGraph}
     */
    public KruskalMinimumSpanningTree(UndirectedGraph<V> graph_) {
        this.graph_ = graph_;
    }

    @Override
    public SpanningTree<V> getSpanningTree() {
        List<Edge<V>> edgeList = new ArrayList<>(graph_.getEdgeSet());
        edgeList.sort(Comparator.comparingDouble(Edge::getWeight));

        Set<Edge<V>> edgeSet = new HashSet<>();
        for (Edge<V> e : edgeList) {
            V source = e.getSource();
            V target = e.getTarget();

        }


        return null;

    }

}

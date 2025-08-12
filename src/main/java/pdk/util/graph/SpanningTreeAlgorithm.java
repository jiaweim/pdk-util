package pdk.util.graph;

/**
 * Algorithm which create a {@link SpanningTree}
 *
 * @param <V> type of the node
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 21 Jul 2025, 5:54 PM
 */
public interface SpanningTreeAlgorithm<V> {

    SpanningTree<V> getSpanningTree();
}

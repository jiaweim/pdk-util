package pdk.util.graph.util;

import org.jspecify.annotations.Nullable;
import pdk.util.graph.Digraph;
import pdk.util.graph.Edge;
import pdk.util.graph.Graph;
import pdk.util.graph.GraphIterator;

import java.util.ArrayDeque;
import java.util.HashMap;

/**
 * A breadth-first iterator for a directed or undirected graph.
 * <p>
 * It can be used to find the shortest path in unweighted-graph,
 * with time complexity O(E+V).
 *
 * @author Jiawei Mao
 * @version 1.2.0 ⭐😀
 * @since 25 Nov 2024, 11:25 AM
 */
public class BreadthFirstIterator<V> implements GraphIterator<V> {

    /**
     * first-in, first-out queue, hold gray vertices
     */
    private final ArrayDeque<V> queue_ = new ArrayDeque<>();
    private final Graph<V> graph_;
    private final HashMap<V, NodeData<V>> visited_;

    /**
     * Create a {@link BreadthFirstIterator}
     *
     * @param graph     {@link Digraph} instance
     * @param startNode source node
     */
    public BreadthFirstIterator(Graph<V> graph, V startNode) {
        graph_ = graph;
        queue_.addLast(startNode);
        visited_ = new HashMap<>(graph.getNodeCount());
        visited_.put(startNode, new NodeData<>(null, 0));
    }

    /**
     * Return the {@link NodeData} for given node, if the <code>node</code> is not reachable from the source node,
     * return null
     *
     * @param node node
     * @return {@link NodeData}
     */
    protected @Nullable NodeData<V> getData(V node) {
        return visited_.get(node);
    }

    /**
     * Return the parent node of node <code>v</code> in the BFS search tree, of null if <code>v</code> is the
     * root node, or it is not reachable from the source node. This method should be invoked in a node once the iterator has visited node
     *
     * @param v node
     * @return parent node of node <code>v</code> in the BFS search tree, or null if <code>v</code> is a root node, or the
     * node is not reachable from the source node
     */
    public V getParentNode(V v) {
        NodeData<V> data = getData(v);
        if (data == null)
            return null;
        Edge<V> edge = data.parentEdge;
        if (edge == null) {
            return null;
        } else {
            return graph_.getOppositeNode(edge, v);
        }
    }

    /**
     * Return the depth of node <code>v</code> in the search tree. The depth of a node <code>v</code> is defined
     * as the number of edges traversed on the path from the root of the BFS tree to node <code>v</code>. The root
     * of the search tree has depth 0. If the node is not reachable from the source node, return {@link Integer#MAX_VALUE}
     *
     * @param v a node
     * @return depth of node <code>v</code> in the search tree
     */
    public int getDepth(V v) {
        NodeData<V> data = getData(v);
        if (data == null)
            return Integer.MAX_VALUE;
        return data.depth;
    }

    @Override
    public boolean hasNext() {
        return !queue_.isEmpty();
    }

    @Override
    public V next() {
        V currentNode = queue_.removeFirst();
        NodeData<V> currentData = visited_.get(currentNode);
        for (Edge<V> edge : graph_.getOutgoingEdges(currentNode)) {
            V target = graph_.getOppositeNode(edge, currentNode);
            NodeData<V> data = visited_.get(target); // white node
            if (data == null) {
                data = new NodeData<>(edge, currentData.depth + 1);
                visited_.put(target, data);
                queue_.addLast(target);
            }
        }

        return currentNode;
    }
}

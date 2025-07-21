package pdk.util.graph.util;

import pdk.util.graph.Edge;
import pdk.util.graph.Graph;
import pdk.util.graph.GraphIterator;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * depth-first iterator for a directed or undirected graph.
 * <p>
 * If the graph is not connected, it will only iterate nodes connected to the start node.
 *
 * @author Jiawei Mao
 * @version 1.0.0😀
 * @since 27 Nov 2024, 5:46 PM
 */
public class DepthFirstIterator<V> implements GraphIterator<V> {

    private final Graph<V> graph_;
    private final Stack<V> stack_;
    private final Set<V> visited_;
    private V currentNode;

    /**
     * Create a {@link DepthFirstIterator}
     *
     * @param graph     {@link Graph}
     * @param startNode source node
     */
    public DepthFirstIterator(Graph<V> graph, V startNode) {
        this.graph_ = graph;
        stack_ = new Stack<>();
        stack_.push(startNode);
        visited_ = new HashSet<>(graph.getNodeCount());
    }

    @Override
    public boolean hasNext() {
        while (!stack_.isEmpty()) {
            currentNode = stack_.pop();
            if (!visited_.contains(currentNode)) {
                visited_.add(currentNode);
                for (Edge<V> edge : graph_.getOutgoingEdges(currentNode)) {
                    V node = graph_.getOppositeNode(edge, currentNode);
                    if (!visited_.contains(node)) {
                        stack_.push(node);
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public V next() {
        return currentNode;
    }
}

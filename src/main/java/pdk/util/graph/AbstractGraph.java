package pdk.util.graph;

import java.util.*;

/**
 * Abstract graph implementation
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 02 Dec 2024, 2:54 PM
 */
public abstract class AbstractGraph<V> implements Graph<V> {
    /**
     * all nodes
     */
    protected final Set<V> nodeSet_;
    /**
     * all edges
     */
    protected final Set<Edge<V>> edgeSet_;

    public AbstractGraph(Collection<V> nodes) {
        nodeSet_ = new HashSet<>(nodes);
        edgeSet_ = new HashSet<>();
    }

    public AbstractGraph() {
        nodeSet_ = new HashSet<>();
        edgeSet_ = new HashSet<>();
    }

    @Override
    public boolean containsNode(V node) {
        return nodeSet_.contains(node);
    }

    @Override
    public Set<V> getNodeSet() {
        return Collections.unmodifiableSet(nodeSet_);
    }

    @Override
    public List<V> getNodeList() {
        return new ArrayList<>(nodeSet_);
    }

    @Override
    public int getNodeCount() {
        return nodeSet_.size();
    }

    @Override
    public boolean containsEdge(Edge<V> e) {
        return edgeSet_.contains(e);
    }

    @Override
    public Set<Edge<V>> getEdgeSet() {
        return Collections.unmodifiableSet(edgeSet_);
    }

    @Override
    public int getEdgeCount() {
        return edgeSet_.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getNodeCount()).append(" nodes, ")
                .append(getEdgeCount()).append(" edges\n");
        for (V v : nodeSet_) {
            sb.append(v).append(": ");
            for (Edge<V> outgoingEdge : getOutgoingEdges(v)) {
                sb.append(getOppositeNode(outgoingEdge, v)).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

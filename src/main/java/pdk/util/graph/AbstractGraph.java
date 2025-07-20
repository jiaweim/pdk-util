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

    protected final ArrayList<V> nodeList;
    protected final HashMap<V, Integer> indexMap;
    protected final Set<Edge> edgeSet;
    protected int V;

    @SafeVarargs
    public AbstractGraph(V... nodes) {
        this(Arrays.asList(nodes));
    }

    public AbstractGraph(Collection<V> nodes) {
        this.nodeList = new ArrayList<>(nodes);
        this.indexMap = new HashMap<>(nodes.size());
        for (int i = 0; i < nodeList.size(); i++) {
            if (indexMap.put(nodeList.get(i), i) != null) {
                throw new IllegalArgumentException("Nodes are not unique");
            }
        }
        edgeSet = new HashSet<>();
        V = nodeList.size();
    }

    @Override
    public boolean containsNode(V node) {
        return indexMap.containsKey(node);
    }

    @Override
    public int getNodeCount() {
        return V;
    }

    @Override
    public int indexOf(V node) {
        return indexMap.get(node);
    }

    @Override
    public V getNode(int index) {
        return nodeList.get(index);
    }

    @Override
    public Set<V> getNodeSet() {
        return indexMap.keySet();
    }

    @Override
    public int getEdgeCount() {
        return edgeSet.size();
    }

    @Override
    public Set<Edge> getEdgeSet() {
        return Collections.unmodifiableSet(edgeSet);
    }

    @Override
    public boolean containsEdge(Edge e) {
        return edgeSet.contains(e);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getNodeCount()).append(" nodes, ")
                .append(getEdgeCount()).append(" edges\n");
        for (int v = 0; v < getNodeCount(); v++) {
            sb.append(getNode(v).toString()).append(": ");
            for (Edge edge : getOutgoingEdges(v)) {
                sb.append(getNode(getOppositeNode(edge, v)))
                        .append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

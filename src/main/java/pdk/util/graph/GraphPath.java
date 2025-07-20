package pdk.util.graph;

import java.util.Collection;
import java.util.Objects;

import static pdk.util.ArgUtils.checkNotNull;

/**
 * A path in the graph
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 25 Nov 2024, 2:30 PM
 */
public final class GraphPath<V> {

    private final Digraph<V> graph;
    private final V startNode;
    private final V endNode;
    private final Collection<V> nodeList;
    private final Collection<Edge> edgeList;
    private final double weight;

    /**
     * Create a GraphPath defined by a sequence of edges and nodes. Note that both the sequence of edges and nodes must
     * describe the same path. This is not verified during the construction of the GraphPath.
     *
     * @param graph     graph
     * @param startNode start node
     * @param endNode   the last node of the path
     * @param nodeList  list of nodes of the path
     * @param edgeList  list of edges of the path
     * @param weight    total weight of the path
     */
    public GraphPath(Digraph<V> graph, V startNode, V endNode, Collection<V> nodeList, Collection<Edge> edgeList, double weight) {
        checkNotNull(nodeList);
        checkNotNull(edgeList);
        checkNotNull(graph);
        this.graph = graph;
        this.startNode = startNode;
        this.endNode = endNode;
        this.nodeList = nodeList;
        this.edgeList = edgeList;
        this.weight = weight;
    }

    /**
     * Return the graph over which this path is defined.
     *
     * @return the containing graph
     */
    public Digraph<V> graph() {
        return graph;
    }

    /**
     * @return start node in the path
     */
    public V startNode() {
        return startNode;
    }

    /**
     * @return end node in the path
     */
    public V endNode() {
        return endNode;
    }

    /**
     * @return length of the path, namely the number of edges.
     */
    public int getLength() {
        if (edgeList != null) {
            return edgeList.size();
        } else if (nodeList != null && !nodeList.isEmpty()) {
            return nodeList.size() - 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        if (nodeList != null) {
            return nodeList.toString();
        } else {
            return edgeList.toString();
        }
    }

    public Collection<V> nodeList() {return nodeList;}

    public Collection<Edge> edgeList() {return edgeList;}

    public double weight() {return weight;}

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (GraphPath) obj;
        return Objects.equals(this.graph, that.graph) &&
                Objects.equals(this.startNode, that.startNode) &&
                Objects.equals(this.endNode, that.endNode) &&
                Objects.equals(this.nodeList, that.nodeList) &&
                Objects.equals(this.edgeList, that.edgeList) &&
                Double.doubleToLongBits(this.weight) == Double.doubleToLongBits(that.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(graph, startNode, endNode, nodeList, edgeList, weight);
    }
}

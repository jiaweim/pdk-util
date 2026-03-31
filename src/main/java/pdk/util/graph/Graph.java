package pdk.util.graph;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static pdk.util.ArgUtils.checkArgument;

/**
 * Graph object. Any object implement hashCode and equals can be used as node,
 * <p>
 * Edge and WeightedEdge are used for unweighted graph and weighted graph.
 * <p>
 * Adjacency list is used to shore connection information.
 *
 * @param <V> type of the node
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Nov 2024, 3:42 PM
 */
public interface Graph<V> {

    /**
     * add a new node to this graph
     *
     * @param node node
     * @return if the graph already contains this node, return false, otherwise return true
     */
    boolean addNode(V node);

    /**
     * Return true if the graph contains the given node
     *
     * @param node node to find
     * @return true if the graph contains this node
     */
    boolean containsNode(V node);

    /**
     * Return nodes in this graph as set
     *
     * @return all nodes in the graph
     */
    Set<V> getNodeSet();

    /**
     * Return nodes in this graph as list
     *
     * @return all nodes in the graph
     */
    List<V> getNodeList();

    /**
     * number of nodes in this graph
     *
     * @return number of nodes in the graph
     */
    int getNodeCount();

    /**
     * add a new edge
     *
     * @param edge edge to add
     * @return false if the graph already contains the edge
     */
    boolean addEdge(Edge<V> edge);

    /**
     * remove given edge from the graph
     *
     * @param edge edge to remove
     * @return true if the graph contains the edge
     */
    boolean removeEdge(Edge<V> edge);

    /**
     * Test whether the graph contains the specified edge.
     *
     * @param edge {@link Edge} to test
     * @return true if this graph contains given edge
     */
    boolean containsEdge(Edge<V> edge);

    /**
     * Return an unmodified set of all edges in this graph
     *
     * @return edge set
     */
    Set<Edge<V>> getEdgeSet();

    /**
     * Return the number of edges in the graph
     *
     * @return edge count
     */
    int getEdgeCount();

    /**
     * Get the edge between two nodes
     *
     * @param source the source node
     * @param target the target node
     * @return edge between the two nodes
     */
    @Nullable
    Edge<V> getEdge(V source, V target);

    /**
     * Return the in degree of given node
     *
     * @param node a node
     * @return in degree of the node
     */
    int getInDegree(V node);

    /**
     * Return the out degree of given node
     *
     * @param node a node
     * @return out degree of the node
     */
    int getOutDegree(V node);

    /**
     * Return the degree of the node, including in-degree and out-degree for directed-graph.
     *
     * @param node a node
     * @return the degree of the node
     */
    int getDegree(V node);

    /**
     * Return edges touching the specified node. If no edges are touching the node, return an empty list.
     *
     * @param node the node for which a list of touching edges is to be returned
     * @return all edges touching the node
     */
    List<Edge<V>> getEdges(V node);

    /**
     * Return a list of edges incoming into the specified node
     *
     * @param node the node for which a list of incoming edges is to be return
     * @return all incoming edge to the node
     */
    List<Edge<V>> getIncomingEdges(V node);

    /**
     * Return a list of edges get out from the specified node
     *
     * @param node the node for which a list of edges get out from this node
     * @return all outgoing edge of this node
     */
    List<Edge<V>> getOutgoingEdges(V node);

    /**
     * Remove all specified edges in this graph
     *
     * @param edges edges to remove from this graph
     * @return true if this graph changed as a result of the call
     */
    default boolean removeAllEdges(Collection<Edge<V>> edges) {
        boolean modified = false;
        for (Edge<V> edge : edges) {
            modified |= removeEdge(edge);
        }
        return modified;
    }

    /**
     * Return nodes with zero in degree
     *
     * @return zero in degree nodes
     */
    default List<V> getZeroInDegreeNodes() {
        List<V> nodes = new ArrayList<>();
        for (V v : getNodeSet()) {
            if (getInDegree(v) == 0)
                nodes.add(v);
        }
        return nodes;
    }

    /**
     * Return nodes with zero out degree
     *
     * @return nodes with zero out degree
     */
    default List<V> getZeroOutDegreeNodes() {
        List<V> nodes = new ArrayList<>();
        for (V v : getNodeSet()) {
            if (getOutDegree(v) == 0)
                nodes.add(v);
        }
        return nodes;
    }


    /**
     * Return the opposite node across an edge
     *
     * @param e    an edge
     * @param node a node
     * @return node opposite to node across edge
     */
    default V getOppositeNode(Edge<V> e, V node) {
        V target = e.getTarget();
        V source = e.getSource();
        if (node == source) {
            return target;
        } else if (node == target) {
            return source;
        } else {
            throw new IllegalArgumentException("no such node: " + node);
        }
    }

    /**
     * add a new edge from source to target
     *
     * @param source source node of the edge
     * @param target target node of the edge
     * @param edge   edge to add
     * @return true if the graph contains both nodes, and the graph does not contain the edge
     */
    default boolean addEdge(V source, V target, Edge<V> edge) {
        checkArgument(edge.getSource() == source);
        checkArgument(edge.getTarget() == target);

        return addEdge(edge);
    }

    /**
     * add a new unweighted edge between two nodes
     *
     * @param source source node
     * @param target target node
     * @return true if add successfully
     */
    default boolean addEdge(V source, V target) {
        return addEdge(new Edge<>(source, target));
    }

    /**
     * add all edges to the graph
     *
     * @param edges edges to add
     */
    default void addAllEdges(final Collection<Edge<V>> edges) {
        for (Edge<V> edge : edges) {
            addEdge(edge);
        }
    }

    /**
     * add a new weighted edge
     *
     * @param source source node
     * @param target target node
     * @param weight edge weight
     * @return true if add successfully
     */
    default boolean addEdge(V source, V target, double weight) {
        return addEdge(new WeightedEdge<>(source, target, weight));
    }

    /**
     * Return true if this graph contains an edge going from the source node to the target node.
     * If any of the nodes does not exist in the graph,or if the edge is null, return false.
     *
     * @param source source node of the edge
     * @param target target node of the edge
     * @return true if this graph contains the edge
     */
    default boolean containsEdge(V source, V target) {
        Edge<V> edge = getEdge(source, target);
        return edge != null;
    }

    /**
     * Return the weight assigned to the edge. Unweighted edge return 1.
     *
     * @param edge edge of interest
     * @return edge weight
     */
    default double getEdgeWeight(Edge<V> edge) {
        return edge.getWeight();
    }

    /**
     * assign a weight to the edge
     *
     * @param edge   edge of interest
     * @param weight edge weight
     */
    default void setEdgeWeight(Edge<V> edge, double weight) {
        edge.setWeight(weight);
    }

    /**
     * Remove an edge between two nodes
     *
     * @param source source node of the edge
     * @param target target node of the edge
     * @return the removed edge
     */
    default Edge<V> removeEdge(V source, V target) {
        Edge<V> edge = getEdge(source, target);
        if (edge != null) {
            removeEdge(edge);
        }
        return edge;
    }
}

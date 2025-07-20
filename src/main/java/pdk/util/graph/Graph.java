package pdk.util.graph;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Graph object. Any object implement hashCode and equals can be used as node,
 * <p>
 * Edge and WeightedEdge are used for unweighted graph and weighted graph.
 * <p>
 * Adjacency list is used to shore connection information.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Nov 2024, 3:42 PM
 */
public interface Graph<V> {

    /**
     * Return the opposite node across an edge
     *
     * @param e    an edge
     * @param node a node
     * @return node opposite to node across edge
     */
    default int getOppositeNode(Edge e, int node) {
        int target = e.getTarget();
        int source = e.getSource();
        if (node == source) {
            return target;
        } else if (node == target) {
            return source;
        } else {
            throw new IllegalArgumentException("no such node: " + node);
        }
    }

    /**
     * Return the opposite node across an edge
     *
     * @param e    an edge
     * @param node a node
     * @return node opposite to node across edge
     */
    default V getOppositeNode(Edge e, V node) {
        int oppositeNode = getOppositeNode(e, indexOf(node));
        return getNode(oppositeNode);
    }

    /**
     * Return true if the graph contains the given node
     *
     * @param node node to test
     * @return true if the graph contain this node
     */
    boolean containsNode(V node);

    /**
     * @return all nodes in the graph
     */
    Set<V> getNodeSet();

    /**
     * @return number of nodes in the graph
     */
    int getNodeCount();

    /**
     * @return number of edges in the graph
     */
    int getEdgeCount();

    /**
     * Return index of given node
     *
     * @param node a node in the graph
     * @return index of the node
     */
    int indexOf(V node);

    /**
     * return the node at given index
     *
     * @param index index
     * @return node
     */
    V getNode(int index);

    /**
     * add an new edge from source to target
     *
     * @param source source node index of the edge
     * @param target target node index of the edge
     * @param edge   edge to add
     * @return true if the graph contains both nodes, and the graph does not contain the edge
     */
    boolean addEdgeByIndex(int source, int target, Edge edge);

    /**
     * add an new edge from source to target
     *
     * @param source source node of the edge
     * @param target target node of the edge
     * @param edge   edge to add
     * @return true if the graph contains both nodes, and the graph does not contain the edge
     */
    default boolean addEdge(V source, V target, Edge edge) {
        return addEdgeByIndex(indexOf(source), indexOf(target), edge);
    }

    /**
     * add a new edge from source to target
     *
     * @param source source node of the edge
     * @param target target node of the edge
     * @return true if the graph contains both nodes, and the graph does not contain the edge
     */
    default boolean addEdgeByIndex(int source, int target) {
        return addEdgeByIndex(source, target, new Edge(source, target));
    }

    /**
     * add a new unweighted edge between two nodes
     *
     * @param source source node
     * @param target target node
     * @return true if add successfully
     */
    default boolean addEdge(V source, V target) {
        int start = indexOf(source);
        int end = indexOf(target);
        return addEdge(source, target, new Edge(start, end));
    }

    /**
     * add a new edge, the source and target of the edge should added before add this edge
     *
     * @param edge edge to add
     * @return true if the graph contains both nodes, and the graph does not contain the edge
     */
    default boolean addEdge(Edge edge) {
        return addEdgeByIndex(edge.getSource(), edge.getTarget(), edge);
    }

    /**
     * add all edges to the graph
     *
     * @param edges edges to add
     */
    default void addAllEdges(final Collection<Edge> edges) {
        for (Edge edge : edges) {
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
        return addEdgeByIndex(indexOf(source), indexOf(target), weight);
    }

    /**
     * add a new weighted edge
     *
     * @param source source node
     * @param target target node
     * @param weight edge weight
     * @return true if add successfully
     */
    default boolean addEdgeByIndex(int source, int target, double weight) {
        return addEdgeByIndex(source, target, new WeightedEdge(source, target, weight));
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
        Edge edge = getEdge(source, target);
        return edge != null;
    }

    /**
     * Return true if this graph contains an edge going from the source node to the target node.
     * If any of the nodes does not exist in the graph,or if the edge is null, return false.
     *
     * @param source source node of the edge
     * @param target target node of the edge
     * @return true if this graph contains the edge
     */
    default boolean containsEdge(int source, int target) {
        Edge edge = getEdge(source, target);
        return edge != null;
    }

    /**
     * @return true if this graph contains given edge
     */
    boolean containsEdge(Edge edge);

    /**
     * Return the weight assigned to the edge. Unweighted edge return 1.
     *
     * @param edge edge of interest
     * @return edge weight
     */
    default double getEdgeWeight(Edge edge) {
        return edge.getWeight();
    }

    /**
     * assign a weight to the edge
     *
     * @param edge   edge to interest
     * @param weight edge weight
     */
    default void setEdgeWeight(Edge edge, double weight) {
        edge.setWeight(weight);
    }

    /**
     * remove given edge from the graph
     *
     * @param edge edge to remove
     * @return true if the graph contains the edge
     */
    boolean removeEdge(Edge edge);

    /**
     * Remove an edge between two nodes
     *
     * @param source source node of the edge
     * @param target target node of the edge
     * @return the removed edge
     */
    default Edge removeEdge(int source, int target) {
        Edge edge = getEdge(source, target);
        if (edge != null) {
            removeEdge(edge);
        }
        return edge;
    }

    /**
     * Remove an edge between two nodes
     *
     * @param source source node of the edge
     * @param target target node of the edge
     * @return the removed edge
     */
    default Edge removeEdge(V source, V target) {
        return removeEdge(indexOf(source), indexOf(target));
    }

    /**
     * @return an unmodified collection of all edges in the graph
     */
    Set<Edge> getEdgeSet();

    /**
     * Remove all specified edges in this graph
     *
     * @param edges edges to remove from this graph
     * @return true if this graph changed as a result of the call
     */
    default boolean removeAllEdges(Collection<Edge> edges) {
        boolean modified = false;
        for (Edge edge : edges) {
            modified |= removeEdge(edge);
        }
        return modified;
    }

    /**
     * @return the edge between two nodes
     */
    @Nullable
    default Edge getEdge(V source, V target) {
        return getEdge(indexOf(source), indexOf(target));
    }

    /**
     * Return the edge between two nodes
     *
     * @param source index of the source node
     * @param target index of the target node
     * @return edge between the two nodes
     */
    @Nullable
    Edge getEdge(int source, int target);

    /**
     * Return the in degree of given node
     *
     * @param node index of the node
     * @return in degree of the node
     */
    int getInDegree(int node);

    /**
     * Return the in degree of given node
     *
     * @param node a node
     * @return in degree of the node
     */
    default int getInDegree(V node) {
        return getInDegree(indexOf(node));
    }

    /**
     * @return index of nodes with zero in degree
     */
    default List<Integer> getZeroInDegreeNodes() {
        List<Integer> idxList = new ArrayList<>();
        for (int i = 0; i < getNodeCount(); i++) {
            if (getInDegree(i) == 0)
                idxList.add(i);
        }
        return idxList;
    }

    /**
     * @return index of nodes with zero out degree
     */
    default List<Integer> getZeroOutDegreeNodes() {
        List<Integer> idxList = new ArrayList<>();
        for (int i = 0; i < getNodeCount(); i++) {
            if (getOutDegree(i) == 0)
                idxList.add(i);
        }
        return idxList;
    }

    /**
     * Return the out degree of given node
     *
     * @param node index of the node
     * @return out degree
     */
    int getOutDegree(int node);

    /**
     * Return the out degree of given node
     *
     * @param node a node
     * @return out degree of the node
     */
    default int getOutDegree(V node) {
        return getOutDegree(indexOf(node));
    }

    /**
     * the degree of the node, including in-degree and out-degree for directed-graph.
     *
     * @param node index of the node
     * @return degree
     */
    int getDegree(int node);

    /**
     * @return the degree of the node, including in-degree and out-degree for directed-graph.
     */
    default int getDegree(V node) {
        return getDegree(indexOf(node));
    }

    /**
     * Return edges touching the specified node. If no edges are touching the node, return an empty list.
     *
     * @param node the index of the node for which a list of touching edges is to be returned
     * @return all edges touching the node
     */
    List<Edge> getEdges(int node);

    /**
     * Return edges touching the specified node. If no edges are touching the node, return an empty list.
     *
     * @param node the node for which a list of touching edges is to be returned
     * @return all edges touching the node
     */
    default List<Edge> getEdges(V node) {
        return getEdges(indexOf(node));
    }

    /**
     * Return a list of edges incoming into the specified node
     *
     * @param node the index of the node for which a list of incoming edges is to be return
     * @return all incoming edge to the node
     */
    List<Edge> getIncomingEdges(int node);

    /**
     * Return a list of edges incoming into the specified node
     *
     * @param node the node for which a list of incoming edges is to be return
     * @return all incoming edge to the node
     */
    default List<Edge> getIncomingEdges(V node) {
        return getIncomingEdges(indexOf(node));
    }

    /**
     * Return a list of edges get out from the specified node
     *
     * @param node the index of the node for which a list of edges get out from this node
     * @return all outgoing edge of this node
     */
    List<Edge> getOutgoingEdges(int node);

    /**
     * Return a list of edges get out from the specified node
     *
     * @param node the node for which a list of edges get out from this node
     * @return all outgoing edge of this node
     */
    default List<Edge> getOutgoingEdges(V node) {
        return getOutgoingEdges(indexOf(node));
    }

    /**
     * Convert a list of node index to type specific list
     *
     * @param nodes node index collection
     * @return type specific node list
     */
    default List<V> getNodes(Collection<Integer> nodes) {
        List<V> list = new ArrayList<>(nodes.size());
        for (Integer node : nodes) {
            list.add(getNode(node));
        }
        return list;
    }
}

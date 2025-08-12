package pdk.util.graph.util;

import pdk.util.graph.Edge;
import pdk.util.graph.Graph;
import pdk.util.graph.GraphPath;
import pdk.util.graph.PathFinder;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * The BFS Shortest path algorithm.
 * <p>
 * This algorithm is used to compute the shortest paths from a single source node to all other nodes in a directed or undirected graph.
 * <p>
 * The running time is $O(|V|+|E|)$。
 * ⭐
 *
 * @param <V> type of the node
 * @author Jiawei Mao
 * @version 1.0.0😀
 * @since 25 Nov 2024, 2:37 PM
 */
public class BreadFirstShortestPath<V> implements PathFinder<V> {

    private final Graph<V> graph_;
    private final V startNode_;
    private final HashMap<V, NodeData<V>> nodeDataMap_;

    /**
     * Use BFS to perform single source shortest path
     *
     * @param graph     {@link Graph}
     * @param startNode source vertex
     */
    public BreadFirstShortestPath(Graph<V> graph, V startNode) {
        graph_ = graph;
        startNode_ = startNode;

        nodeDataMap_ = new HashMap<>(graph.getNodeCount());
        nodeDataMap_.put(startNode, new NodeData<>(null, 0));

        ArrayDeque<V> deque = new ArrayDeque<>();
        deque.add(startNode);

        while (!deque.isEmpty()) {
            V node = deque.remove();
            NodeData<V> data = nodeDataMap_.get(node);

            List<Edge<V>> edges = graph.getOutgoingEdges(node);
            for (Edge<V> edge : edges) {
                V target = graph.getOppositeNode(edge, node);
                NodeData<V> nodeData = nodeDataMap_.get(target);
                if (nodeData == null) {
                    nodeData = new NodeData<>(edge, data.getDepth() + 1);
                    nodeDataMap_.put(target, nodeData);
                    deque.add(target);
                }
            }
        }
    }

    /**
     * return the path weight from the start node to given target node, for unweighted graph, it is the edge count.
     *
     * @param targetNode target node
     * @return edge count
     */
    @Override
    public double getWeight(V targetNode) {
        return nodeDataMap_.get(targetNode).depth;
    }

    @Override
    public boolean hasPathTo(V targetNode) {
        return nodeDataMap_.containsKey(targetNode);
    }

    public Edge<V> getParentEdge(V targetNode) {
        return nodeDataMap_.get(targetNode).parentEdge;
    }

    /**
     * The parent node in the bread first tree
     *
     * @param targetNode target vertex
     * @return parent vertex
     */
    public V getParentNode(V targetNode) {
        Edge<V> parentEdge = getParentEdge(targetNode);
        if (parentEdge == null) {
            return null;
        }
        return graph_.getOppositeNode(parentEdge, targetNode);
    }

    @Override
    public GraphPath<V> getPath(V targetNode) {
        LinkedList<Edge<V>> edgeList = new LinkedList<>();
        LinkedList<V> nodeList = new LinkedList<>();
        nodeList.add(targetNode);

        V tmp = targetNode;
        while (nodeDataMap_.get(tmp).parentEdge != null) {
            NodeData<V> data = nodeDataMap_.get(tmp);
            edgeList.add(data.parentEdge);
            tmp = graph_.getOppositeNode(data.parentEdge, tmp);
            nodeList.add(tmp);
        }
        return new GraphPath<>(graph_, startNode_, targetNode, nodeList.reversed(), edgeList.reversed(), getWeight(targetNode));
    }
}

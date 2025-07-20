package pdk.util.graph.util;

import pdk.util.graph.*;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;

/**
 * The BFS Shortest path algorithm.
 * <p>
 * This algorithm is used to compute the shortest paths from a single source node to all other nodes in a directed unweighted graph.
 * <p>
 * The running time is $O(|V|+|E|)$。
 * ⭐
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 25 Nov 2024, 2:37 PM
 */
public class BFSShortestPath<V> implements PathFinder<V> {

    private final Digraph<V> graph;
    private final V startNode;
    private final NodeData[] nodeDataArray;

    public BFSShortestPath(Digraph<V> graph, V startNode) {
        this.graph = graph;
        this.startNode = startNode;
        this.nodeDataArray = new NodeData[graph.getNodeCount()];

        int startIndex = graph.indexOf(startNode);
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.add(startIndex);
        nodeDataArray[startIndex] = new NodeData(null, 0);

        while (!deque.isEmpty()) {
            int node = deque.remove();
            NodeData data = nodeDataArray[node];

            List<Edge> edges = graph.getOutgoingEdges(node);
            for (Edge edge : edges) {
                int target = graph.getOppositeNode(edge, node);
                NodeData nodeData = nodeDataArray[target];
                if (nodeData == null) {
                    nodeData = new NodeData(edge, data.getDepth() + 1);
                    nodeDataArray[target] = nodeData;
                    deque.add(target);
                }
            }
        }
    }

    @Override
    public int indexOf(V node) {
        return 0;
    }

    /**
     * return the path weight from the start node to given target node, for unweighted graph, it is the edge count.
     *
     * @param targetNode target node
     * @return edge count
     */
    @Override
    public double getWeight(V targetNode) {
        return nodeDataArray[graph.indexOf(targetNode)].depth;
    }

    @Override
    public double getWeight(int targetNode) {
        return 0;
    }

    @Override
    public boolean hasPathTo(int node) {
        return false;
    }

    @Override
    public GraphPath<V> getPath(int targetNode) {
        return null;
    }

    public Edge getParentEdge(V targetNode) {
        return nodeDataArray[graph.indexOf(targetNode)].parentEdge;
    }

    public V getParentNode(V targetNode) {
        Edge parentEdge = getParentEdge(targetNode);
        if (parentEdge == null) {
            return null;
        }
        return graph.getNode(graph.getOppositeNode(parentEdge, graph.indexOf(targetNode)));
    }

    @Override
    public GraphPath<V> getPath(V targetNode) {
        LinkedList<Edge> edgeList = new LinkedList<>();
        LinkedList<V> nodeList = new LinkedList<>();
        nodeList.add(targetNode);

        V tmp = targetNode;
//        while (nodeDataMap.get(tmp).parentEdge != null) {
//            NodeData data = nodeDataMap.get(tmp);
//            edgeList.add(data.parentEdge);
//            tmp = graph.getOppositeNode(data.parentEdge, tmp);
//            nodeList.add(tmp);
//        }
        return new GraphPath<>(graph, startNode, targetNode, nodeList.reversed(), edgeList.reversed(), getWeight(targetNode));
    }
}

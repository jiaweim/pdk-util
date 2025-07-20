package pdk.util.graph.util;

import org.jheaps.AddressableHeap;
import org.jheaps.tree.PairingHeap;
import pdk.util.graph.Digraph;
import pdk.util.graph.Edge;
import pdk.util.graph.GraphPath;
import pdk.util.graph.PathFinder;

import java.util.LinkedList;

/**
 * An implementation of Dijkstra's algorithm for single-source shortest path problem in directed weighted graph, where
 * the edge weight are non-negative, allow cycle.
 * <p>
 * running time O(ElogV) in the worst case, space O(V).
 *
 * @author Jiawei Mao⭐⭐
 * @version 1.0.0
 * @since 25 Nov 2024, 3:16 PM
 */
public class DijkstraShortestPath<V> implements PathFinder<V> {

    private final Digraph<V> graph;
    private final int startNode;
    private final double[] distTo;
    private final Edge[] edgeTo;

    /**
     * Computes a shortest-path from the source node <code>startNode</code> to every other node in the directed weighted
     * graph.
     *
     * @param graph     a {@link Digraph}
     * @param startNode source node
     */
    public DijkstraShortestPath(Digraph<V> graph, int startNode) {
        this.graph = graph;
        this.startNode = startNode;
        for (Edge edge : graph.getEdgeSet()) {
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException("Edge " + edge + " has negative weight");
            }
        }

        int V = graph.getNodeCount();
        distTo = new double[V];
        edgeTo = new Edge[V];
        boolean[] visited = new boolean[V];
        validateNode(startNode);

        for (int v = 0; v < V; v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[startNode] = 0;

        PairingHeap<Double, Integer> heap = new PairingHeap<>();
        heap.insert(distTo[startNode], startNode);
        while (!heap.isEmpty()) {
            AddressableHeap.Handle<Double, Integer> handle = heap.deleteMin();
            Integer v = handle.getValue();
            for (Edge edge : graph.getOutgoingEdges(v)) {
                int w = edge.getTarget();
                if (visited[w])
                    continue;
                if (distTo[w] > distTo[v] + edge.getWeight()) {
                    distTo[w] = distTo[v] + edge.getWeight();
                    edgeTo[w] = edge;
                }
                heap.insert(distTo[w], w);
            }
            visited[v] = true;
        }
    }

    /**
     * Computes a shortest-path from the source node <code>startNode</code> to every other node in the directed weighted
     * graph.
     *
     * @param graph     a {@link Digraph}
     * @param startNode source node
     */
    public DijkstraShortestPath(Digraph<V> graph, V startNode) {
        this(graph, graph.indexOf(startNode));
    }

    @Override
    public int indexOf(V node) {
        return graph.indexOf(node);
    }

    @Override
    public double getWeight(int targetNode) {
        validateNode(targetNode);
        return distTo[targetNode];
    }

    @Override
    public boolean hasPathTo(int node) {
        return distTo[node] < Double.POSITIVE_INFINITY;
    }

    @Override
    public GraphPath<V> getPath(int targetNode) {
        LinkedList<Edge> edges = new LinkedList<>();
        LinkedList<Integer> nodeList = new LinkedList<>();
        nodeList.add(targetNode);

        int tmp = targetNode;
        while (edgeTo[tmp] != null) {
            edges.add(edgeTo[tmp]);
            tmp = edgeTo[tmp].getSource();
            nodeList.add(tmp);
        }
        return new GraphPath<>(graph, graph.getNode(startNode), graph.getNode(targetNode),
                graph.getNodes(nodeList.reversed()), edges.reversed(),
                getWeight(graph.getNode(targetNode)));
    }

    private void validateNode(int v) {
        if (v < 0 || v >= distTo.length) {
            throw new IllegalArgumentException("Node " + v + " is not between 0 and " + (distTo.length - 1));
        }
    }
}

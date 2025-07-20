package pdk.util.graph.util;

import pdk.util.graph.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

/**
 * Find single-source longest paths in DAG.The edge weights can be positive, negative, or zero.
 * <p>
 * This implementation uses a topological-sort based algorithm.
 * <p>
 * time: O(V+E), extra space O(V)
 * <p>
 * This algorithm can be used from critical path analysis.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Dec 2024, 4:49 PM ⭐⭐⭐
 */
public class DAGLongestPath<V> implements PathFinder<V> {

    private final Digraph<V> graph;
    private final int startNode;
    private final Edge[] edgeTo;
    private final double[] distTo;

    public DAGLongestPath(final Digraph<V> graph, V startNode) {
        this(graph, graph.indexOf(startNode));
    }

    /**
     * Compute longest path in DAG.
     *
     * @param graph     a {@link Digraph}
     * @param startNode start node index
     */
    public DAGLongestPath(Digraph<V> graph, int startNode) {
        this.graph = graph;
        this.startNode = startNode;

        int V = graph.getNodeCount();
        edgeTo = new Edge[V];
        distTo = new double[V];
        for (int v = 0; v < V; v++) {
            distTo[v] = Double.NEGATIVE_INFINITY;
        }
        distTo[startNode] = 0;

        // for topological sort
        Queue<Integer> queue = new ArrayDeque<>();
        int[] indegree = new int[V];
        for (int v = 0; v < V; v++) {
            int d = graph.getIncomingEdges(v).size();
            indegree[v] = d;
            if (d == 0)
                queue.offer(v);
        }
        int count = V;
        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (Edge edge : graph.getOutgoingEdges(v)) {
                int target = edge.getTarget();

                // relax edge during topological sort
                if (distTo[target] < distTo[v] + edge.getWeight()) {
                    distTo[target] = distTo[v] + edge.getWeight();
                    edgeTo[target] = edge;
                }

                if (indegree[target] > 0) {
                    indegree[target]--;
                    if (indegree[target] == 0) {
                        queue.offer(target);
                    }
                }
            }
            count--;
        }
        if (count > 0) {
            throw new NotDAGException();
        }
    }

    @Override
    public int indexOf(V node) {
        return graph.indexOf(node);
    }

    /**
     * return the path weight of a longest path from the start node to given target node
     *
     * @param targetNode target node
     * @return the weight of a longest path from the source node to the target node. {@link Double#NEGATIVE_INFINITY}
     * if no such path
     */
    @Override
    public double getWeight(int targetNode) {
        validate(targetNode);
        return distTo[targetNode];
    }

    @Override
    public boolean hasPathTo(int node) {
        validate(node);
        return distTo[node] > Double.NEGATIVE_INFINITY;
    }

    @Override
    public GraphPath<V> getPath(int targetNode) {
        if (!hasPathTo(targetNode)) {
            return null;
        }
        Deque<Edge> path = new ArrayDeque<>();
        Deque<Integer> nodes = new ArrayDeque<>();
        nodes.addFirst(targetNode);
        for (Edge e = edgeTo[targetNode]; e != null; e = edgeTo[e.getSource()]) {
            path.push(e);
            nodes.addFirst(e.getSource());
        }

        return new GraphPath<>(graph, graph.getNode(startNode), graph.getNode(targetNode),
                graph.getNodes(nodes), path, distTo[targetNode]);
    }

    private void validate(int v) {
        int V = graph.getNodeCount();
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("node " + v + " is not between 0 and " + (V - 1));
    }
}

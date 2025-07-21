package pdk.util.graph.util;

import pdk.util.graph.*;

import java.util.*;

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
    private final V startNode;
    private final HashMap<V, Edge<V>> edgeTo;
    private final HashMap<V, Double> distTo;

    /**
     * Compute the longest path in DAG.
     *
     * @param graph     a {@link Digraph}
     * @param startNode start node index
     */
    public DAGLongestPath(Digraph<V> graph, V startNode) {
        this.graph = graph;
        this.startNode = startNode;

        int V = graph.getNodeCount();
        edgeTo = new HashMap<>(V);
        distTo = new HashMap<>(V);
        for (V v : graph.getNodeSet()) {
            distTo.put(v, Double.NEGATIVE_INFINITY);
        }
        distTo.put(startNode, 0.0);

        // for topological sort
        Queue<V> queue = new ArrayDeque<>();
        Map<V, Integer> indegree = new HashMap<>();
        for (V v : graph.getNodeSet()) {
            int d = graph.getIncomingEdges(v).size();
            indegree.put(v, d);
            if (d == 0)
                queue.offer(v);
        }

        int count = V;
        while (!queue.isEmpty()) {
            V v = queue.poll();
            for (Edge<V> edge : graph.getOutgoingEdges(v)) {
                V target = edge.getTarget();

                // relax edge during topological sort
                if (distTo.get(target) < distTo.get(v) + edge.getWeight()) {
                    distTo.put(target, distTo.get(v) + edge.getWeight());
                    edgeTo.put(target, edge);
                }

                if (indegree.get(target) > 0) {
                    indegree.put(target, indegree.get(target) - 1);
                    if (indegree.get(target) == 0) {
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

    /**
     * return the path weight of a longest path from the start node to given target node
     *
     * @param targetNode target node
     * @return the weight of a longest path from the source node to the target node. {@link Double#NEGATIVE_INFINITY}
     * if no such path
     */
    @Override
    public double getWeight(V targetNode) {
        return distTo.get(targetNode);
    }

    @Override
    public boolean hasPathTo(V node) {
        return distTo.get(node) > Double.NEGATIVE_INFINITY;
    }

    @Override
    public GraphPath<V> getPath(V targetNode) {
        if (!hasPathTo(targetNode)) {
            return null;
        }
        Deque<Edge<V>> path = new ArrayDeque<>();
        Deque<V> nodes = new ArrayDeque<>();
        nodes.addFirst(targetNode);
        for (Edge<V> e = edgeTo.get(targetNode); e != null; e = edgeTo.get(e.getSource())) {
            path.push(e);
            nodes.addFirst(e.getSource());
        }

        return new GraphPath<>(graph, startNode, targetNode, nodes, path, distTo.get(targetNode));
    }

}

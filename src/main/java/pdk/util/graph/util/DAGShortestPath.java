package pdk.util.graph.util;

import org.jspecify.annotations.Nullable;
import pdk.util.graph.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Queue;

/**
 * Find single-source shortest paths in a DAG. It is faster than {@link DijkstraShortestPath},
 * and allow positive, negative and zero weight.
 * <p>
 * This implementation uses a topological-sort based algorithm.
 * <p>
 * running time O(V+E) in the worst case, O(V) extra space
 *
 * @param <V> type of the node
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Dec 2024, 3:26 PM ⭐
 */
public class DAGShortestPath<V> implements PathFinder<V> {

    private final Digraph<V> digraph;
    private final HashMap<V, Edge<V>> edgeTo;
    private final HashMap<V, Double> distTo;
    private final V startNode;

    /**
     * Compute all shortest paths tree from <code>startNode</code> to every other nodes in the DAG.
     *
     * @param digraph   a DAG
     * @param startNode source node
     */
    public DAGShortestPath(Digraph<V> digraph, V startNode) {
        this.digraph = digraph;
        this.startNode = startNode;

        int V = digraph.getNodeCount();
        edgeTo = new HashMap<>(V);
        distTo = new HashMap<>(V);
        for (V v : digraph.getNodeSet()) {
            distTo.put(v, Double.POSITIVE_INFINITY);
        }
        distTo.put(startNode, 0.0);

        Queue<V> queue = new ArrayDeque<>();
        HashMap<V, Integer> indegree = new HashMap<>(V);
        for (V v : digraph.getNodeSet()) {
            int d = digraph.getIncomingEdges(v).size();
            indegree.put(v, d);
            if (d == 0)
                queue.offer(v);
        }

        int count = V;
        while (!queue.isEmpty()) {
            V v = queue.poll();
            for (Edge<V> edge : digraph.getOutgoingEdges(v)) {
                V target = edge.getTarget();
                if (distTo.get(target) > distTo.get(v) + edge.getWeight()) {
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

    @Override
    public double getWeight(V targetNode) {
        return distTo.get(targetNode);
    }

    @Override
    @Nullable
    public GraphPath<V> getPath(V targetNode) {
        if (!hasPathTo(targetNode))
            return null;
        Deque<Edge<V>> path = new ArrayDeque<>();
        Deque<V> nodes = new ArrayDeque<>();
        nodes.addFirst(targetNode);
        for (Edge<V> e = edgeTo.get(targetNode); e != null; e = edgeTo.get(e.getSource())) {
            path.push(e);
            nodes.addFirst(e.getSource());
        }
        return new GraphPath<>(digraph, startNode, targetNode, nodes, path, distTo.get(targetNode));
    }

    @Override
    public boolean hasPathTo(V v) {
        return distTo.get(v) < Double.POSITIVE_INFINITY;
    }

}

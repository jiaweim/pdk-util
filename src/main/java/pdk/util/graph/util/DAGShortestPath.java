package pdk.util.graph.util;

import org.jspecify.annotations.Nullable;
import pdk.util.graph.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

/**
 * Find single-source shortest paths in a DAG. It is faster than {@link DijkstraShortestPath},
 * and allow positive, negative and zero weight.
 * <p>
 * This implementation uses a topological-sort based algotithm.
 * <p>
 * running time O(V+E) in the worst case, O(V) extra space
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Dec 2024, 3:26 PM ⭐
 */
public class DAGShortestPath<V> implements PathFinder<V> {

    private final Digraph<V> digraph;
    private final Edge[] edgeTo;
    private final double[] distTo;
    private final int startNode;

    /**
     * Compute all shortest paths tree from <code>startNode</code> to every other nodes in the DAG.
     *
     * @param digraph   a DAG
     * @param startNode source node
     */
    public DAGShortestPath(Digraph<V> digraph, int startNode) {
        this.digraph = digraph;
        this.startNode = startNode;

        int V = digraph.getNodeCount();
        edgeTo = new Edge[V];
        distTo = new double[V];
        for (int v = 0; v < V; v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[startNode] = 0;

        Queue<Integer> queue = new ArrayDeque<>();
        int[] indegree = new int[V];
        for (int v = 0; v < V; v++) {
            int d = digraph.getIncomingEdges(v).size();
            indegree[v] = d;
            if (d == 0)
                queue.offer(v);
        }
        int count = V;
        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (Edge edge : digraph.getOutgoingEdges(v)) {
                int target = edge.getTarget();
                if (distTo[target] > distTo[v] + edge.getWeight()) {
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
        return digraph.indexOf(node);
    }

    @Override
    public double getWeight(int targetNode) {
        validate(targetNode);

        return distTo[targetNode];
    }

    @Override
    @Nullable
    public GraphPath<V> getPath(int targetNode) {
        validate(targetNode);
        if (!hasPathTo(targetNode))
            return null;
        Deque<Edge> path = new ArrayDeque<>();
        Deque<Integer> nodes = new ArrayDeque<>();
        nodes.addFirst(targetNode);
        for (Edge e = edgeTo[targetNode]; e != null; e = edgeTo[e.getSource()]) {
            path.push(e);
            nodes.addFirst(e.getSource());
        }
        return new GraphPath<>(digraph, digraph.getNode(startNode), digraph.getNode(targetNode),
                digraph.getNodes(nodes), path, distTo[targetNode]);
    }

    @Override
    public boolean hasPathTo(int v) {
        validate(v);

        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    private void validate(int v) {
        int V = digraph.getNodeCount();
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("node " + v + " is not between 0 and " + (V - 1));
    }
}

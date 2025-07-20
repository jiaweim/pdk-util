package pdk.util.graph.util;

import pdk.util.graph.Digraph;
import pdk.util.graph.Edge;
import pdk.util.graph.GraphIterator;

import java.util.ArrayDeque;

/**
 * A breadth-first iterator for a directed graph. It can be used to find the shortest path in unweighted-directed-graph,
 * with time complexity O(E+V)。
 *
 * @author Jiawei Mao
 * @version 1.0.0 ⭐
 * @since 25 Nov 2024, 11:25 AM
 */
public class BreadthFirstIterator<V> implements GraphIterator<V> {

    private final ArrayDeque<Integer> queue = new ArrayDeque<>();
    private final Digraph<V> graph;
    private final SearchNodeData[] seen;

    public BreadthFirstIterator(Digraph<V> graph, V startNode) {
        this.graph = graph;
        queue.add(graph.indexOf(startNode));
        seen = new SearchNodeData[graph.getNodeCount()];
        seen[graph.indexOf(startNode)] = new SearchNodeData(null, 0);
    }

    protected SearchNodeData getSeenData(V node) {
        return seen[graph.indexOf(node)];
    }

    /**
     * Return the parent node of node <code>v</code> in the BFS search tree, of null if <code>v</code> is the
     * root node. This method can only be invoked in a node once the iterator has visited node
     *
     * @param v node
     * @return parent node of node <code>v</code> in the BFS search tree, or null if <code>v</code> is a root node
     */
    public V getParent(V v) {
        SearchNodeData seenData = getSeenData(v);
        Edge edge = seenData.edge;
        if (edge == null) {
            return null;
        } else {
            return graph.getNode(graph.getOppositeNode(edge, graph.indexOf(v)));
        }
    }

    /**
     * Return the depth of node <code>v</code> in the search tree. The depth of a node <code>v</code> is defined
     * as the number of edges traversed on the path from the root of the BFS tree to node <code>v</code>. The root
     * of the search tree has depth 0. This method can only be invoked on a node <code>v</code> once the iterator
     * has visited node <code>v</code>.
     *
     * @param v a node
     * @return depth of node <code>v</code> in the search tree
     */
    public int getDepth(V v) {
        return getSeenData(v).depth;
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public V next() {
        int result = queue.remove();
        SearchNodeData currentData = seen[result];

        for (Edge edge : graph.getOutgoingEdges(result)) {
            int target = edge.getTarget();
            SearchNodeData data = seen[target];
            if (data == null) {
                data = new SearchNodeData(edge, currentData.depth + 1);
                seen[target] = data;
                queue.add(target);
            }
        }

        return graph.getNode(result);
    }

    /**
     * @param edge  input edge
     * @param depth depth of this node
     */
    public record SearchNodeData(Edge edge, int depth) {

        /**
         * edge to parent
         *
         * @return the edge to the parent
         */
        @Override
        public Edge edge() {
            return edge;
        }

        /**
         * @return the depth of the node in the search tree
         */
        @Override
        public int depth() {
            return depth;
        }
    }
}

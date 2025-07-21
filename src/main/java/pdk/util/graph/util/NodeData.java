package pdk.util.graph.util;

import pdk.util.graph.Edge;

/**
 * Hold data during searching
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 25 Nov 2024, 5:36 PM
 */
public class NodeData<V> {

    protected Edge<V> parentEdge;
    protected int depth;

    /**
     * Create a {@link NodeData}
     *
     * @param parentEdge parent edge
     * @param depth      distance from the source vertex, it is 0 for the source vertex
     */
    public NodeData(Edge<V> parentEdge, int depth) {
        this.parentEdge = parentEdge;
        this.depth = depth;
    }

    /**
     * @return parent edge
     */
    public Edge<V> getParentEdge() {
        return parentEdge;
    }

    public void setParentEdge(Edge<V> parentEdge) {
        this.parentEdge = parentEdge;
    }

    /**
     * @return depth in the search tree, also represents the distance from the source vertex
     */
    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}

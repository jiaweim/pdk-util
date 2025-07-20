package pdk.util.graph.util;

import pdk.util.graph.Edge;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 25 Nov 2024, 5:36 PM
 */
public class NodeData {

    protected Edge parentEdge;
    protected int depth;
    protected double weight = Edge.DEFAULT_EDGE_WEIGHT;

    public NodeData(Edge parentEdge, int depth) {
        this.parentEdge = parentEdge;
        this.depth = depth;
    }

    public NodeData(Edge parentEdge, int depth, double weight) {
        this.parentEdge = parentEdge;
        this.depth = depth;
        this.weight = weight;
    }

    public Edge getParentEdge() {
        return parentEdge;
    }

    public int getDepth() {
        return depth;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

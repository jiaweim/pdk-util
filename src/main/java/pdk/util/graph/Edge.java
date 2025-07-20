package pdk.util.graph;

import pdk.util.Copyable;

/**
 * graph edge
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 22 Nov 2024, 23:57
 */
public class Edge implements Copyable<Edge> {

    /**
     * The default weight for an edge.
     */
    public static double DEFAULT_EDGE_WEIGHT = 1.0;

    protected final int source;
    protected final int target;

    public Edge(int source, int target) {
        this.source = source;
        this.target = target;
    }

    public int getSource() {
        return source;
    }

    public int getTarget() {
        return target;
    }

    public void setWeight(double weight) {
        throw new UnsupportedOperationException();
    }

    public double getWeight() {
        return DEFAULT_EDGE_WEIGHT;
    }

    @Override
    public String toString() {
        return source + "->" + target;
    }

    public Edge reverse() {
        return new Edge(target, source);
    }

    @Override
    public Edge copy() {
        return new Edge(source, target);
    }
}

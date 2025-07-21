package pdk.util.graph;

import pdk.util.Copyable;

import java.util.Objects;

/**
 * graph edge
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 22 Nov 2024, 23:57
 */
public class Edge<V> implements Copyable<Edge<V>> {

    /**
     * The default weight for an edge.
     */
    public static double DEFAULT_EDGE_WEIGHT = 1.0;

    protected final V source;
    protected final V target;

    /**
     * Create an edge
     *
     * @param source source node index
     * @param target target node index
     */
    public Edge(V source, V target) {
        this.source = source;
        this.target = target;
    }

    /**
     * @return the source node
     */
    public V getSource() {
        return source;
    }

    /**
     * @return index of the target node
     */
    public V getTarget() {
        return target;
    }

    /**
     * set the weight of this edge
     *
     * @param weight weight value
     */
    public void setWeight(double weight) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return edge weight
     */
    public double getWeight() {
        return DEFAULT_EDGE_WEIGHT;
    }

    @Override
    public String toString() {
        return source + "->" + target;
    }

    public Edge<V> reverse() {
        return new Edge<>(target, source);
    }

    @Override
    public Edge<V> copy() {
        return new Edge<>(source, target);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Edge<?> edge)) return false;
        return Objects.equals(source, edge.source)
                && Objects.equals(target, edge.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}

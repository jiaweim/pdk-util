package pdk.util.graph;

/**
 * Graph edge with weight
 *
 * @param <V> type of the node
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 23 Nov 2024, 17:05
 */
public class WeightedEdge<V> extends Edge<V> {

    private double weight;

    public WeightedEdge(V source, V target) {
        this(source, target, Edge.DEFAULT_EDGE_WEIGHT);
    }

    public WeightedEdge(V source, V target, double weight) {
        super(source, target);
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public Edge<V> copy() {
        return new WeightedEdge<>(source, target, weight);
    }

    @Override
    public Edge<V> reverse() {
        return new WeightedEdge<>(target, source, weight);
    }
}

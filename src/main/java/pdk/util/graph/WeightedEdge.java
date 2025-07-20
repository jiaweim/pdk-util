package pdk.util.graph;

/**
 * Graph edge with weight
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 23 Nov 2024, 17:05
 */
public class WeightedEdge extends Edge {

    private double weight;

    public WeightedEdge(int source, int target) {
        this(source, target, Edge.DEFAULT_EDGE_WEIGHT);
    }

    public WeightedEdge(int source, int target, double weight) {
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
    public Edge copy() {
        return new WeightedEdge(source, target, weight);
    }

    @Override
    public Edge reverse() {
        return new WeightedEdge(target, source, weight);
    }
}

package pdk.util.data;

/**
 * A {@link Point2D} with weight.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 01 Apr 2026, 2:52 PM
 */
public class WeightPoint2D extends Point2D {

    private double weight = 1;

    /**
     * Create a {@link WeightPoint2D} with default weight 1.0
     *
     * @param x x value
     * @param y y value
     */
    public WeightPoint2D(double x, double y) {
        super(x, y);
    }

    /**
     * Create a {@link WeightPoint2D}
     *
     * @param x      x value
     * @param y      y value
     * @param weight point weight
     */
    public WeightPoint2D(double x, double y, double weight) {
        super(x, y);
        this.weight = weight;
    }

    /**
     * Return the weight of this point
     *
     * @return weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * set weight of this point
     *
     * @param weight new weight value
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }
}

package pdk.util.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Point2D} with weight.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 01 Apr 2026, 2:52 PM
 */
public class WeightPoint2D extends Point2D {

    /**
     * Convert {@link Point2D} to {@link WeightPoint2D} with default weight 1
     *
     * @param points list of {@link Point2D}
     * @return list of {@link WeightPoint2D}
     */
    public static List<WeightPoint2D> convert(List<Point2D> points) {
        List<WeightPoint2D> result = new ArrayList<>();
        for (Point2D point : points) {
            result.add(new WeightPoint2D(point.getX(), point.getY()));
        }
        return result;
    }

    /**
     * Create {@link WeightPoint2D} list with a given dataset.
     *
     * @param x x values
     * @param y y values
     * @return list of {@link WeightPoint2D}
     */
    public static List<WeightPoint2D> convert(double[] x, double[] y) {
        List<WeightPoint2D> result = new ArrayList<>(x.length);
        for (int i = 0; i < x.length; i++) {
            result.add(new WeightPoint2D(x[i], y[i]));
        }
        return result;
    }

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

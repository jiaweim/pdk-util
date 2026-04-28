package pdk.util.data.fitting;

import org.hipparchus.fitting.WeightedObservedPoint;
import org.hipparchus.fitting.WeightedObservedPoints;
import pdk.util.data.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 Apr 2026, 5:28 PM
 */
public class DataUtils {

    /**
     * Create a dataset
     *
     * @param sample sample
     * @return {@link WeightedObservedPoints} instance
     */
    public static WeightedObservedPoints createDataset(List<Point2D> sample) {
        WeightedObservedPoints points = new WeightedObservedPoints();
        for (Point2D p : sample) {
            points.add(p.getX(), p.getY());
        }
        return points;
    }

    /**
     * Create a dataset
     *
     * @param x x values
     * @param y y values
     * @return list of {@link WeightedObservedPoint}
     */
    public static List<WeightedObservedPoint> createDataset(double[] x, double[] y) {
        List<WeightedObservedPoint> points = new ArrayList<>();
        for (int i = 0; i < x.length; i++) {
            points.add(new WeightedObservedPoint(1d, x[i], y[i]));
        }
        return points;
    }

}

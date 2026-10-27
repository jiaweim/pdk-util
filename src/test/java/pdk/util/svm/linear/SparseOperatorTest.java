package pdk.util.svm.linear;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 1:02 PM
 */
class SparseOperatorTest {

    @Test
    void testNrm2Sq() {
        assertEquals(0.0, SparseOperator.norm2Squared(features()));
        assertEquals(1.0, SparseOperator.norm2Squared(features(1.0)));
        assertEquals(1 + 4, SparseOperator.norm2Squared(features(1.0, 2.0)));
        assertEquals(5 + 9, SparseOperator.norm2Squared(features(1.0, 2.0, 3.0)));
    }

    @Test
    void testDot() {
        assertEquals(0.0, SparseOperator.dot(new double[0], features()));
        assertEquals(1.0, SparseOperator.dot(new double[]{1.0}, features(1.0)));
        assertEquals(1 + 2 * 2, SparseOperator.dot(new double[]{1.0, 2.0}, features(1.0, 2.0)));
        assertEquals(3 * 1 + 2 * 2 + 1 * 3, SparseOperator.dot(new double[]{3.0, 2.0, 1.0}, features(1.0, 2.0, 3.0)));
    }

    @Test
    public void testSparseDot() {
        Feature[] features1 = new Feature[]{
                new Feature(1, 2.0),
                new Feature(2, 3.0),
                new Feature(3, 0.5),
                new Feature(5, 2.0)
        };

        Feature[] features2 = new Feature[]{
                new Feature(1, 2.0),
                new Feature(3, 1.0),
                new Feature(4, 1.0)
        };

        assertEquals(SparseOperator.sparseDot(features1, features2), 2.0 * 2.0 + 0.5 * 1.0);
    }

    @Test
    void testAxpy() {
        assertEquals(0, axpy(1.0, features()).length);
        assertArrayEquals(new double[]{1.5 * 2.0}, axpy(1.5, features(2.0)));

        double[] y = new double[]{1.0, 2.0};
        SparseOperator.axpy(1.5, features(2.0, 3.0), y);
        assertArrayEquals(new double[]{1.0 + 1.5 * 2.0, 2.0 + 1.5 * 3.0}, y);
    }

    private static double[] axpy(double a, Feature[] features) {
        double[] result = new double[features.length];
        SparseOperator.axpy(a, features, result);
        return result;
    }

    private static Feature[] features(double... values) {
        Feature[] features = new Feature[values.length];
        for (int i = 0; i < values.length; i++) {
            features[i] = new Feature(i + 1, values[i]);
        }
        return features;
    }
}
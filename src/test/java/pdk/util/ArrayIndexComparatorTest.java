package pdk.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 12 Aug 2025, 11:22 AM
 */
class ArrayIndexComparatorTest {

    @Test
    void doubleDescending() {
        double[] values = new double[]{2.0, 3.0, 1.0, 4.0, 6.1, 7.2, 4.6};
        int[] indexArray = ArrayIndexComparator.sortDescending(values);
        assertArrayEquals(new int[]{5, 4, 6, 3, 1, 0, 2}, indexArray);
    }

    @Test
    void doubleAscending() {
        double[] intensityValues = new double[]{2.0, 1.0, 3.0, 5.0, 2.0, 6.0};
        int[] indexArray = ArrayIndexComparator.sortAscending(intensityValues);
        assertArrayEquals(new int[]{1, 0, 4, 2, 3, 5}, indexArray);
    }

    @Test
    void doubleDescending2() {
        double[] array = {1.0, 2.0, 3.0, 6.0, 4.0, 5.0};
        int[] indexArray = ArrayIndexComparator.sortDescending(array);
        assertArrayEquals(new int[]{3, 5, 4, 2, 1, 0}, indexArray);

        double[] values = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            values[i] = array[indexArray[i]];
        }
        assertArrayEquals(new double[]{6., 5., 4., 3., 2., 1.}, values, 1E-15);
    }

    @Test
    void sortSame() {
        List<Double> values = new ArrayList<>();
        values.add(1.);
        values.add(1.);
        values.add(1.);
        values.add(1.);
        Double[] array = values.toArray(new Double[0]);
        int[] indexArray = ArrayIndexComparator.sort(array, Comparator.comparingDouble(o -> o));
        assertArrayEquals(new int[]{0, 1, 2, 3}, indexArray);

        array = new Double[]{1.0, 2.0, 2.0, 3.0};
        indexArray = ArrayIndexComparator.sort(array, Comparator.comparingDouble(o -> o));
        assertArrayEquals(new int[]{0, 1, 2, 3}, indexArray);
    }

    @Test
    void doubleSame() {
        double[] array = new double[]{1.0, 1.0, 1.0, 1.0};
        int[] indexArray = ArrayIndexComparator.sortDescending(array);
        assertArrayEquals(new int[]{0, 1, 2, 3}, indexArray);

        array = new double[]{1.0, 2.0, 2.0, 3.0};
        indexArray = ArrayIndexComparator.sortDescending(array);
        assertArrayEquals(new int[]{3, 1, 2, 0}, indexArray);
    }

    @Test
    void sortDescending() {
        Double[] values = new Double[]{1.0, 2.0, 3.0, 6.0, 4.0, 5.0};
        int[] indexArray = ArrayIndexComparator.sort(values, (o1, o2) -> Double.compare(o2, o1));
        assertArrayEquals(new int[]{3, 5, 4, 2, 1, 0}, indexArray);
    }
}
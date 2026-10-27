package pdk.util.svm.linear;

import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.doubles.DoubleComparators;
import org.junit.jupiter.api.Test;
import pdk.util.ArgUtils;
import pdk.util.ArrayUtils;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static pdk.util.svm.linear.Linear.swap;


/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Oct 2025, 4:58 PM
 */
class ArraySorterTest {

    private final Random random = new Random();

    private void assertDescendingOrder(double[] array) {
        double before = array[0];
        for (double d : array) {
            // accept that case
            if (d == 0.0 && before == -0.0)
                continue;

            assertTrue(d <= before);
            before = d;
        }
    }

    private void shuffleArray(double[] array) {
        for (int i = 0; i < array.length; i++) {
            int j = random.nextInt(array.length);
            swap(array, i, j);
        }
    }

    @Test
    void testReversedMergesort() {
        for (int k = 1; k <= 16 * 8096; k *= 2) {
            // create random array
            double[] array = new double[k];
            for (int i = 0; i < array.length; i++) {
                array[i] = random.nextDouble();
            }

            DoubleArrays.quickSort(array, DoubleComparators.OPPOSITE_COMPARATOR);
            assertTrue(ArgUtils.isSorted(array, ArrayUtils.DOUBLE_OPPOSITE_COMPARATOR));
        }
    }

    @Test
    void testReversedMergesortWithMeanValues() {
        double[] array = new double[]{1.0, -0.0, -1.1, 2.0, 3.0, 0.0, 4.0, -0.0, 0.0};
        shuffleArray(array);
//        ArraySorter.reversedMergesort(array);
        DoubleArrays.quickSort(array, DoubleComparators.OPPOSITE_COMPARATOR);
        assertTrue(ArgUtils.isSorted(array, ArrayUtils.DOUBLE_OPPOSITE_COMPARATOR));
        assertDescendingOrder(array);
    }
}
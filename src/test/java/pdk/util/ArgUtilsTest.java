package pdk.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 05 Jan 2026, 4:07 PM
 */
class ArgUtilsTest {

    @Test
    void isSorted() {
        double[] values = new double[]{1, 2, 3};
        assertTrue(ArgUtils.isSorted(values, ArrayUtils.DOUBLE_NATURAL_COMPARATOR));

        assertTrue(ArgUtils.isSorted(new double[]{3, 2, 1}, ArrayUtils.DOUBLE_OPPOSITE_COMPARATOR));
        assertTrue(ArgUtils.isSorted(new double[]{3, 2, 2}, ArrayUtils.DOUBLE_OPPOSITE_COMPARATOR));
    }

    @Test
    void isSortedFalse() {
        double[] values = new double[]{1, 2, 1};
        assertFalse(ArgUtils.isSorted(values, ArrayUtils.DOUBLE_NATURAL_COMPARATOR));
    }
}
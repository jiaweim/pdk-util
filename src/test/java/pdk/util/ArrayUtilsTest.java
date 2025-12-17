package pdk.util;

import org.apache.commons.numbers.arrays.Selection;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 17 Dec 2025, 11:18 AM
 */
class ArrayUtilsTest {

    @Test
    void repeat() {
        double[] array = ArrayUtils.repeat(1.0, 2);
        assertThat(array).containsExactly(1.0, 1.0);
    }

    @Test
    void leastDouble() {
        double[] a = {7, 10, 4, 3, 20, 15};

        Selection.select(a, 0);
        assertEquals(3.0, a[0]);

        Selection.select(a, 1);
        assertEquals(4.0, a[1]);

        Selection.select(a, 2);
        assertEquals(7.0, a[2]);

        assertThat(ArrayUtils.least(a, 1)).isEqualTo(3.0);
        assertThat(ArrayUtils.least(a, 2)).isEqualTo(4.0);
        assertThat(ArrayUtils.least(a, 3)).isEqualTo(7.0);
    }

    @Test
    void leastDouble2() {
        double[] data = {4, 9, 16, 25, 36};
        assertEquals(4, ArrayUtils.least(data, 1));
        assertEquals(9, ArrayUtils.least(data, 2));
        assertEquals(16, ArrayUtils.least(data, 3));
        assertEquals(25, ArrayUtils.least(data, 4));
        assertEquals(36, ArrayUtils.least(data, 5));

        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.least(data, 0));
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.least(data, data.length + 1));
    }

    @Test
    void leastDoubleEqual() {
        double[] data = {16, 16, 25, 25, 25, 4, 4, 9, 9, 36};

        assertEquals(4, ArrayUtils.least(data, 1));
        assertEquals(4, ArrayUtils.least(data, 2));
        assertEquals(9, ArrayUtils.least(data, 3));
        assertEquals(9, ArrayUtils.least(data, 4));
        assertEquals(16, ArrayUtils.least(data, 5));
        assertEquals(16, ArrayUtils.least(data, 6));
        assertEquals(25, ArrayUtils.least(data, 7));
        assertEquals(25, ArrayUtils.least(data, 8));
        assertEquals(25, ArrayUtils.least(data, 9));
        assertEquals(36, ArrayUtils.least(data, 10));

        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.least(data, 0));
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.least(data, data.length + 1));
    }

    @Test
    void greatestDouble() {
        double[] data = {4, 9, 16, 25, 36};
        assertThat(ArrayUtils.greatest(data, 1)).isEqualTo(36);
        assertThat(ArrayUtils.greatest(data, 2)).isEqualTo(25);
        assertThat(ArrayUtils.greatest(data, 3)).isEqualTo(16);
        assertThat(ArrayUtils.greatest(data, 4)).isEqualTo(9);
        assertThat(ArrayUtils.greatest(data, 5)).isEqualTo(4);
    }

    @Test
    void greatestInt() {
        int[] data = {4, 9, 16, 25, 36};
        assertThat(ArrayUtils.greatest(data, 1)).isEqualTo(36);
        assertThat(ArrayUtils.greatest(data, 2)).isEqualTo(25);
        assertThat(ArrayUtils.greatest(data, 3)).isEqualTo(16);
        assertThat(ArrayUtils.greatest(data, 4)).isEqualTo(9);
        assertThat(ArrayUtils.greatest(data, 5)).isEqualTo(4);
    }

    @Test
    void greatestEqual() {
        double[] data = {16, 16, 25, 25, 25, 4, 4, 9, 9, 36};

        assertThat(ArrayUtils.greatest(data, 1)).isEqualTo(36);
        assertThat(ArrayUtils.greatest(data, 2)).isEqualTo(25);
        assertThat(ArrayUtils.greatest(data, 3)).isEqualTo(25);
        assertThat(ArrayUtils.greatest(data, 4)).isEqualTo(25);
        assertThat(ArrayUtils.greatest(data, 5)).isEqualTo(16);
        assertThat(ArrayUtils.greatest(data, 6)).isEqualTo(16);
        assertThat(ArrayUtils.greatest(data, 7)).isEqualTo(9);
        assertThat(ArrayUtils.greatest(data, 8)).isEqualTo(9);
        assertThat(ArrayUtils.greatest(data, 9)).isEqualTo(4);
        assertThat(ArrayUtils.greatest(data, 10)).isEqualTo(4);

        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.greatest(data, 0));
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.greatest(data, data.length + 1));
    }

    @Test
    void greatestIntEquals() {
        int[] data = {16, 16, 25, 25, 25, 4, 4, 9, 9, 36};

        assertThat(ArrayUtils.greatest(data, 1)).isEqualTo(36);
        assertThat(ArrayUtils.greatest(data, 2)).isEqualTo(25);
        assertThat(ArrayUtils.greatest(data, 3)).isEqualTo(25);
        assertThat(ArrayUtils.greatest(data, 4)).isEqualTo(25);
        assertThat(ArrayUtils.greatest(data, 5)).isEqualTo(16);
        assertThat(ArrayUtils.greatest(data, 6)).isEqualTo(16);
        assertThat(ArrayUtils.greatest(data, 7)).isEqualTo(9);
        assertThat(ArrayUtils.greatest(data, 8)).isEqualTo(9);
        assertThat(ArrayUtils.greatest(data, 9)).isEqualTo(4);
        assertThat(ArrayUtils.greatest(data, 10)).isEqualTo(4);

        assertThatThrownBy(() -> ArrayUtils.greatest(data, 0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ArrayUtils.greatest(data, data.length + 1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void leastInt() {
        int[] a = {7, 10, 4, 3, 20, 15};
        Selection.select(a, 3);

        int[] b = a.clone();
        Arrays.sort(b);

        assertEquals(b[3], a[3]);
        assertThat(ArrayUtils.least(a, 3)).isEqualTo(b[2]);
    }

    @Test
    void leastSameInt() {
        int[] a = {7, 10, 4, 3, 10, 20, 15};
        Selection.select(a, 3);
        assertEquals(10, a[3]);
        assertThat(ArrayUtils.least(a, 3)).isEqualTo(7);
        assertThat(ArrayUtils.least(a, 4)).isEqualTo(10);
        assertThat(ArrayUtils.least(a, 5)).isEqualTo(10);
    }
}
package pdk.util.svm.linear;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 12:42 PM
 */
class IntArrayPointerTest {

    @Test
    void testGetIntArrayPointer() {
        int[] foo = new int[]{1, 2, 3, 4, 6};
        IntArrayPointer pFoo = new IntArrayPointer(foo, 2);
        assertEquals(3, pFoo.get(0));
        assertEquals(4, pFoo.get(1));
        assertEquals(6, pFoo.get(2));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> pFoo.get(3));
    }

    @Test
    void testSetIntArrayPointer() {
        int[] foo = new int[]{1, 2, 3, 4, 6};
        IntArrayPointer pFoo = new IntArrayPointer(foo, 2);
        pFoo.set(2, 5);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, foo);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> pFoo.set(3, 0));
    }

    @Test
    void testGetDoubleArrayPointer() {
        double[] foo = new double[]{1, 2, 3, 4, 6};
        DoubleArrayPointer pFoo = new DoubleArrayPointer(foo, 2);

        assertEquals(3, pFoo.get(0));
        assertEquals(4, pFoo.get(1));
        assertEquals(6, pFoo.get(2));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> pFoo.get(3));
    }

    @Test
    void testSetDoubleArrayPointer() {
        double[] foo = new double[]{1, 2, 3, 4, 6};
        DoubleArrayPointer pFoo = new DoubleArrayPointer(foo, 2);
        pFoo.set(2, 5);
        assertArrayEquals(new double[]{1, 2, 3, 4, 5}, foo);

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> pFoo.set(3, 0));
    }
}
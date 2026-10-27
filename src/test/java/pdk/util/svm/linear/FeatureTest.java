package pdk.util.svm.linear;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 12:58 PM
 */
class FeatureTest {

    @Test
    void testConstructorIndexZero() {
        assertThrows(IllegalArgumentException.class,
                () -> new Feature(0, 0),
                "index must be > 0");
    }

    @Test
    void testConstructorIndexNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> new Feature(-1, 0),
                "index must be > 0");
    }

    @Test
    void testConstructorHappy() {
        Feature fn = new Feature(25, 27.39);
        assertEquals(25, fn.getIndex());
        assertEquals(27.39, fn.getValue());

        fn = new Feature(1, -0.22222);
        assertEquals(1, fn.getIndex());
        assertEquals(-0.22222, fn.getValue());
    }


    @Test
    void testEqualsAndHashCodeNaNValue() {
        Feature a = new Feature(1, Double.NaN);
        Feature b = new Feature(1, Double.NaN);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testEqualsWithPositiveAndNegativeZeroValue() {
        Feature a = new Feature(1, -0.0);
        Feature b = new Feature(1, +0.0);
        assertNotEquals(a, b);
    }

    @Test
    void testToString() {
        assertEquals("Feature(index=1, value=2.0)", new Feature(1, 2.0).toString());
    }
}
package pdk.util.data.func;

import org.junit.jupiter.api.Test;
import pdk.util.SerializationUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Jul 2025, 9:31 AM
 */
class LineFunc2DTest {

    private static final double EPS = 1E-8;

    @Test
    void testConstructor() {
        LineFunc2D f = new LineFunc2D(2.0, 1.0);
        assertEquals(1.0, f.getIntercept(), EPS);
        assertEquals(2.0, f.getSlope(), EPS);
    }

    @Test
    void testEquals() {
        LineFunc2D f1 = new LineFunc2D(2.0, 1.0);
        LineFunc2D f2 = new LineFunc2D(2.0, 1.0);
        assertEquals(f1, f2);

        f1 = new LineFunc2D(3.0, 2.0);
        assertNotEquals(f1, f2);

        f2 = new LineFunc2D(3.0, 2.0);
        assertEquals(f1, f2);
    }

    @Test
    void testSerialization() {
        LineFunc2D f1 = new LineFunc2D(2.0, 1.0);
        LineFunc2D f2 = SerializationUtils.round(f1);
        assertEquals(f1, f2);
    }

    @Test
    void testHashCode() {
        LineFunc2D f1 = new LineFunc2D(2.0, 1.0);
        LineFunc2D f2 = new LineFunc2D(2.0, 1.0);
        assertEquals(f1.hashCode(), f2.hashCode());
    }
}
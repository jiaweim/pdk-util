package pdk.util.data.func;

import org.junit.jupiter.api.Test;
import pdk.util.SerializationUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Jul 2025, 11:08 AM
 */
class PowerFunc2DTest {

    private static final double EPS = 1E-8;

    @Test
    void testConstructor() {
        PowerFunc2D f1 = new PowerFunc2D(1.0, 2.0);
        assertEquals(1.0, f1.getA(), EPS);
        assertEquals(2.0, f1.getB(), EPS);
    }

    @Test
    void testEquals() {
        PowerFunc2D f1 = new PowerFunc2D(1.0, 2.0);
        PowerFunc2D f2 = new PowerFunc2D(1.0, 2.0);
        assertEquals(f1, f2);
        f1 = new PowerFunc2D(2.0, 3.0);
        assertNotEquals(f1, f2);
        f2 = new PowerFunc2D(2.0, 3.0);
        assertEquals(f1, f2);
    }

    @Test
    void testSerialization() {
        PowerFunc2D f1 = new PowerFunc2D(1.0, 2.0);
        PowerFunc2D f2 = SerializationUtils.round(f1);
        assertEquals(f1, f2);
    }

    @Test
    void testHashCode() {
        PowerFunc2D f1 = new PowerFunc2D(1.0, 2.0);
        PowerFunc2D f2 = new PowerFunc2D(1.0, 2.0);
        assertEquals(f1.hashCode(), f2.hashCode());
    }
}
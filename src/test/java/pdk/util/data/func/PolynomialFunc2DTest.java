package pdk.util.data.func;

import org.junit.jupiter.api.Test;
import pdk.util.SerializationUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Jul 2025, 10:37 AM
 */
class PolynomialFunc2DTest {

    @Test
    void testConstructor() {
        PolynomialFunc2D f = new PolynomialFunc2D(new double[]{1.0, 2.0});
        assertArrayEquals(new double[]{1.0, 2.0}, f.getCoefficients());

        assertThrows(NullPointerException.class, () -> new PolynomialFunc2D(null));
    }

    @Test
    void testCoefficients() {
        PolynomialFunc2D f = new PolynomialFunc2D(new double[]{1.0, 2.0});
        double[] cs = f.getCoefficients();
        assertArrayEquals(new double[]{1.0, 2.0}, cs);

        cs[0] = 100.0;
        assertArrayEquals(new double[]{1.0, 2.0}, f.getCoefficients()); // return a copy
    }

    @Test
    void testGetOrder() {
        PolynomialFunc2D f = new PolynomialFunc2D(new double[]{1.0, 2.0});
        assertEquals(1, f.getOrder());
    }

    @Test
    void testEquals() {
        PolynomialFunc2D f1 = new PolynomialFunc2D(new double[]{1.0, 2.0});
        PolynomialFunc2D f2 = new PolynomialFunc2D(new double[]{1.0, 2.0});
        assertEquals(f1, f2);
        f1 = new PolynomialFunc2D(new double[]{2.0, 3.0});
        assertNotEquals(f1, f2);
        f2 = new PolynomialFunc2D(new double[]{2.0, 3.0});
        assertEquals(f1, f2);
    }

    @Test
    void testSerialization() {
        PolynomialFunc2D f1 = new PolynomialFunc2D(new double[]{1.0, 2.0});
        PolynomialFunc2D f2 = SerializationUtils.round(f1);
        assertEquals(f1, f2);
    }

    @Test
    void testHashCode() {
        PolynomialFunc2D f1 = new PolynomialFunc2D(new double[]{1.0, 2.0});
        PolynomialFunc2D f2 = new PolynomialFunc2D(new double[]{1.0, 2.0});
        assertEquals(f1.hashCode(), f2.hashCode());
    }
}
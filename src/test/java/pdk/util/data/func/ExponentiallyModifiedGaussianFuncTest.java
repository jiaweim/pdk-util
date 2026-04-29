package pdk.util.data.func;

import org.junit.jupiter.api.Test;
import pdk.util.SerializationUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Jul 2025, 1:07 PM
 */
class ExponentiallyModifiedGaussianFuncTest {

    @Test
    void testConstructor() {
        ExponentiallyModifiedGaussianFunc f = new ExponentiallyModifiedGaussianFunc(0, 1, 1);
        assertEquals(0.0, f.getMu());
        assertEquals(1.0, f.getSigma());
        assertEquals(1.0, f.getLambda());
    }

    @Test
    void testEquals() {
        ExponentiallyModifiedGaussianFunc f1 = new ExponentiallyModifiedGaussianFunc(0, 1, 1);
        ExponentiallyModifiedGaussianFunc f2 = new ExponentiallyModifiedGaussianFunc(0, 1, 1);
        assertEquals(f1, f2);

        f1 = new ExponentiallyModifiedGaussianFunc(0, 3, 0.25);
        assertNotEquals(f1, f2);
        f2 = new ExponentiallyModifiedGaussianFunc(0, 3, 0.25);
        assertEquals(f1, f2);
    }

    @Test
    void testHashCode() {
        ExponentiallyModifiedGaussianFunc f1 = new ExponentiallyModifiedGaussianFunc(0, 1, 1);
        ExponentiallyModifiedGaussianFunc f2 = new ExponentiallyModifiedGaussianFunc(0, 1, 1);
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    void testSerialization() {
        ExponentiallyModifiedGaussianFunc f1 = new ExponentiallyModifiedGaussianFunc(0, 1, 1);
        ExponentiallyModifiedGaussianFunc f2 = SerializationUtils.round(f1);
        assertEquals(f1, f2);
    }
}
package pdk.util.data.func;

import org.junit.jupiter.api.Test;
import pdk.util.SerializationUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Jul 2025, 11:43 AM
 */
class ExponentialDistributionFuncTest {

    private static final double EPS = 1E-8;

    @Test
    void testConstructor() {
        ExponentialDistributionFunc f = new ExponentialDistributionFunc(0.5);
        assertEquals(0.5, f.getLambda());

        assertThrows(IllegalArgumentException.class, () -> new ExponentialDistributionFunc(-1));
    }

    @Test
    void testEquals() {
        ExponentialDistributionFunc f1 = new ExponentialDistributionFunc(0.5);
        ExponentialDistributionFunc f2 = new ExponentialDistributionFunc(0.5);
        assertEquals(f1, f2);
        f1 = new ExponentialDistributionFunc(1.0);
        assertNotEquals(f1, f2);
        f2 = new ExponentialDistributionFunc(1.0);
        assertEquals(f1, f2);
    }

    @Test
    void testSerialization() {
        ExponentialDistributionFunc f1 = new ExponentialDistributionFunc(0.5);
        ExponentialDistributionFunc f2 = SerializationUtils.round(f1);
        assertEquals(f1, f2);
    }

    @Test
    void testHashCode() {
        ExponentialDistributionFunc f1 = new ExponentialDistributionFunc(0.5);
        ExponentialDistributionFunc f2 = new ExponentialDistributionFunc(0.5);
        assertEquals(f1.hashCode(), f2.hashCode());
    }
}
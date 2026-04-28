package pdk.util.data.func;

import org.junit.jupiter.api.Test;
import pdk.util.SerializationUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Jul 2025, 10:24 AM
 */
class NormalDistributionFuncTest {

    private static final double EPS = 1E-8;

    @Test
    void testConstructor() {
        NormalDistributionFunc f = new NormalDistributionFunc(1.0, 2.0);
        assertEquals(1.0, f.getMean(), EPS);
        assertEquals(2.0, f.getStandardDeviation(), EPS);
    }

    @Test
    void testEquals() {
        NormalDistributionFunc f1 = new NormalDistributionFunc(1.0, 2.0);
        NormalDistributionFunc f2 = new NormalDistributionFunc(1.0, 2.0);

        assertEquals(f1, f2);
        f1 = new NormalDistributionFunc(2.0, 3.0);
        assertNotEquals(f1, f2);
        f2 = new NormalDistributionFunc(2.0, 3.0);
        assertEquals(f1, f2);
    }

    @Test
    void serialization() {
        NormalDistributionFunc f1 = new NormalDistributionFunc(1.0, 2.0);
        NormalDistributionFunc f2 = SerializationUtils.round(f1);
        assertEquals(f1, f2);
    }

    @Test
    void testHashCode() {
        NormalDistributionFunc f1 = new NormalDistributionFunc(1.0, 2.0);
        NormalDistributionFunc f2 = new NormalDistributionFunc(1.0, 2.0);
        assertEquals(f1.hashCode(), f2.hashCode());
    }
}
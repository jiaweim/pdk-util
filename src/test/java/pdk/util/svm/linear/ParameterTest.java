package pdk.util.svm.linear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static pdk.util.svm.linear.SolverType.L1R_LR;
import static pdk.util.svm.linear.SolverType.L2R_L1LOSS_SVC_DUAL;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 1:17 PM
 */
class ParameterTest {

    private Parameter param;

    @BeforeEach
    public void setUp() {
        param = new Parameter(L2R_L1LOSS_SVC_DUAL, 100, 1e-3);
    }

    @Test
    void testDefaults() {
        Parameter parameters = new Parameter(L1R_LR, 1, 0.1);
        assertEquals(0.1, parameters.getP());
        assertEquals(1000, parameters.getMaxIterations());
    }

    @Test
    void testSetWeights() {
        assertNull(param.weight);
        assertEquals(0, param.getNumWeights());

        double[] weights = new double[]{0, 1, 2, 3, 4, 5};
        int[] weightLabels = new int[]{1, 1, 1, 1, 2, 3};
        param.setWeights(weights, weightLabels);

        assertEquals(6, param.getNumWeights());

        // assert parameter uses a copy
        weights[0]++;
        assertEquals(0, param.getWeights()[0]);
        weightLabels[0]++;
        assertEquals(1, param.getWeightLabels()[0]);
    }

    @Test
    void testSetWeights_IllegalArgument() {
        double[] weights = new double[]{0, 1, 2, 3, 4, 5};
        int[] weightLabels = new int[]{1};

        assertThrows(IllegalArgumentException.class,
                () -> param.setWeights(weights, weightLabels));
    }

    @Test
    void testGetWeights() {
        double[] weights = new double[]{0, 1, 2, 3, 4, 5};
        int[] weightLabels = new int[]{1, 1, 1, 1, 2, 3};
        param.setWeights(weights, weightLabels);

        assertArrayEquals(weights, param.getWeights());
        param.getWeights()[0]++; // shouldn't change the parameter as we should get a copy
        assertArrayEquals(weights, param.getWeights());

        assertArrayEquals(weightLabels, param.getWeightLabels());
        param.getWeightLabels()[0]++; // shouldn't change the parameter as we should get a copy
        assertEquals(1, param.getWeightLabels()[0]);
    }

    @Test
    void testSetC() {
        param.setCost(0.0001);
        assertEquals(0.0001, param.getCost());
        param.setCost(1);
        param.setCost(100);
        assertEquals(100, param.getCost());
        param.setCost(Double.MAX_VALUE);

        assertThrows(IllegalArgumentException.class,
                () -> param.setCost(-1),
                "must not <= 0");

        assertThrows(IllegalArgumentException.class,
                () -> param.setCost(0),
                "must not <= 0");
    }


    @Test
    void testSetEps() {
        param.setEPS(0.0001);
        assertEquals(param.getEPS(), 0.0001);
        param.setEPS(1);
        param.setEPS(100);
        assertEquals(param.getEPS(), 100);
        param.setEPS(Double.MAX_VALUE);

        assertThrows(IllegalArgumentException.class,
                () -> param.setEPS(-1),
                "must not <= 0");

        assertThrows(IllegalArgumentException.class,
                () -> param.setEPS(0),
                "must not <= 0");
    }

    @Test
    void testSetSolverType() {
        for (SolverType type : SolverType.values()) {
            param.setSolverType(type);
            assertEquals(param.getSolverType(), type);
        }

        assertThrows(IllegalArgumentException.class,
                () -> param.setSolverType(null),
                "must not null");
    }

    @Test
    void testSetInitSol() {
        assertNull(param.init_sol);

        double[] init_sol = new double[]{0, 1, 2, 3, 4, 5};
        param.setInitSol(init_sol);

        // assert parameter uses a copy
        init_sol[0]++;
        assertEquals(param.getInitSol()[0], 0);
    }

    @Test
    void testSetP() throws Exception {
        assertThrows(IllegalArgumentException.class,
                () -> param.setEpsilon(-1),
                "p must not be less than 0");
    }

    @Test
    void testSetNu() throws Exception {
        assertThrows(IllegalArgumentException.class,
                () -> param.setNu(-0.1),
                "nu must not be <=0");

        assertThrows(IllegalArgumentException.class,
                () -> param.setNu(0.0),
                "nu must not be <=0");

        assertThrows(IllegalArgumentException.class,
                () -> param.setNu(1.0),
                "nu must not be >=1");

        assertThrows(IllegalArgumentException.class,
                () -> param.setNu(1.5),
                "nu must not be >=1");
    }

    @Test
    void testGetInitSol() {
        assertNull(param.getInitSol());

        double[] init_sol = new double[]{0, 1, 2, 3, 4, 5};
        param.setInitSol(init_sol);

        assertNotNull(param.getInitSol());
        assertArrayEquals(param.getInitSol(), init_sol);
        param.getInitSol()[0]++; // shouldn't change the parameter as we should get a copy
        assertArrayEquals(param.getInitSol(), init_sol);
    }

    @Test
    void testClone_Simple() throws Exception {
        Parameter parameter = new Parameter(L1R_LR, 123.456, 0.123);
        Parameter clone = parameter.clone();
        assertEquals(clone.getSolverType(), L1R_LR);
        assertEquals(clone.getCost(), 123.456);
        assertEquals(clone.getEPS(), 0.123);
        assertNull(clone.getWeights());
        assertNull(clone.getWeightLabels());
        assertEquals(clone.getNumWeights(), 0);
    }

    @Test
    void testClone_Full() throws Exception {
        Parameter parameter = new Parameter(L1R_LR, 123.456, 0.123, 9000, 1.2);
        parameter.setWeights(new double[]{1, 2}, new int[]{3, 4});
        Random random = new Random(123);
        parameter.setRandom(random);
        Parameter clone = parameter.clone();
        assertEquals(clone.getSolverType(), L1R_LR);
        assertEquals(clone.getCost(), 123.456);
        assertEquals(clone.getEPS(), 0.123);
        assertEquals(clone.getMaxIterations(), 9000);
        assertEquals(clone.getP(), 1.2);
        assertArrayEquals(new double[]{1, 2}, clone.getWeights());
        assertArrayEquals(clone.getWeightLabels(), new int[]{3, 4});
        assertEquals(clone.getNumWeights(), 2);

        assertNotSame(clone.random, random);
        assertEquals(random.nextInt(), clone.random.nextInt());
    }
}
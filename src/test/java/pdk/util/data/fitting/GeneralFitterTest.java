package pdk.util.data.fitting;

import org.hipparchus.analysis.ParametricUnivariateFunction;
import org.hipparchus.analysis.polynomials.PolynomialFunction;
import org.hipparchus.random.RandomDataGenerator;
import org.junit.jupiter.api.Test;
import pdk.util.data.WeightPoint2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 29 Apr 2026, 1:12 PM
 */
class GeneralFitterTest {
    @Test
    void testPolynomialFit() {
        final Random randomizer = new Random(53882150042L);
        final RandomDataGenerator randomDataGenerator = new RandomDataGenerator(64925784252L);

        final double[] coeff = {12.9, -3.4, 2.1}; // 12.9 - 3.4 x + 2.1 x^2
        final PolynomialFunction f = new PolynomialFunction(coeff);

        // Collect data from a known polynomial.
        final List<WeightPoint2D> obs = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            final double x = randomDataGenerator.nextUniform(-100, 100);
            obs.add(new WeightPoint2D(x, f.value(x) + 0.1 * randomizer.nextGaussian()));
        }

        final ParametricUnivariateFunction function = new PolynomialFunction.Parametric();
        // Start fit from initial guesses that are far from the optimal values.
        final CurveFitter fitter
                = GeneralFitter.create(function)
                .initGuess(new double[]{-1e20, 3e15, -5e25});
        final double[] best = fitter.fit(obs);

        assertArrayEquals(coeff, best, 2e-2);
    }
}
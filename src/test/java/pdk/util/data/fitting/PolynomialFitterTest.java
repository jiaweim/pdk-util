package pdk.util.data.fitting;

import org.hipparchus.analysis.polynomials.PolynomialFunction;
import org.hipparchus.random.RandomDataGenerator;
import org.hipparchus.util.FastMath;
import org.junit.jupiter.api.Test;
import pdk.util.data.WeightPoint2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 29 Apr 2026, 12:42 PM
 */
class PolynomialFitterTest {
    @Test
    void testFit() {
        final RandomDataGenerator randomDataGenerator = new RandomDataGenerator(64925784252L);

        final double[] coeff = {12.9, -3.4, 2.1}; // 12.9 - 3.4 x + 2.1 x^2
        final PolynomialFunction f = new PolynomialFunction(coeff);

        // Collect data from a known polynomial.
        final List<WeightPoint2D> obs = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            final double x = randomDataGenerator.nextUniform(-100, 100);
            obs.add(new WeightPoint2D(x, f.value(x)));
        }

        // Start fit from initial guesses that are far from the optimal values.
        CurveFitter fitter = PolynomialFitter.create(0).initGuess(new double[]{-1e-20, 3e15, -5e25});
        final double[] best = fitter.fit(obs);

        assertArrayEquals(coeff, best, 1e-12);
    }

    @Test
    void testNoError() {
        final Random randomizer = new Random(64925784252L);
        for (int degree = 1; degree < 10; ++degree) {
            final PolynomialFunction p = buildRandomPolynomial(degree, randomizer);
            final PolynomialFitter fitter = PolynomialFitter.create(degree);

            final List<WeightPoint2D> obs = new ArrayList<>();
            for (int i = 0; i <= degree; ++i) {
                obs.add(new WeightPoint2D(i, p.value(i)));
            }

            final PolynomialFunction fitted = new PolynomialFunction(fitter.fit(obs));

            for (double x = -1.0; x < 1.0; x += 0.01) {
                final double error = FastMath.abs(p.value(x) - fitted.value(x)) / (1.0 + FastMath.abs(p.value(x)));
                assertEquals(0.0, error, 1.0e-6);
            }
        }
    }

    @Test
    void testSmallError() {
        final Random randomizer = new Random(53882150042L);
        double maxError = 0;
        for (int degree = 0; degree < 10; ++degree) {
            final PolynomialFunction p = buildRandomPolynomial(degree, randomizer);
            final PolynomialFitter fitter = PolynomialFitter.create(degree);

            final List<WeightPoint2D> obs = new ArrayList<>();
            for (double x = -1.0; x < 1.0; x += 0.01) {
                obs.add(new WeightPoint2D(x, p.value(x) + 0.1 * randomizer.nextGaussian()));
            }

            final PolynomialFunction fitted = new PolynomialFunction(fitter.fit(obs));
            for (double x = -1.0; x < 1.0; x += 0.01) {
                final double error = FastMath.abs(p.value(x) - fitted.value(x)) /
                        (1.0 + FastMath.abs(p.value(x)));
                maxError = FastMath.max(maxError, error);
                assertTrue(FastMath.abs(error) < 0.1);
            }
        }
        assertTrue(maxError > 0.01);
    }


    @Test
    void testLargeSample() {
        final Random randomizer = new Random(0x5551480dca5b369bL);
        double maxError = 0;
        for (int degree = 0; degree < 10; ++degree) {
            final PolynomialFunction p = buildRandomPolynomial(degree, randomizer);
            final PolynomialFitter fitter = PolynomialFitter.create(degree);

            final List<WeightPoint2D> obs = new ArrayList<>();
            for (int i = 0; i < 40000; ++i) {
                final double x = -1.0 + i / 20000.0;
                obs.add(new WeightPoint2D(x, p.value(x) + 0.1 * randomizer.nextGaussian()));
            }

            final PolynomialFunction fitted = new PolynomialFunction(fitter.fit(obs));
            for (double x = -1.0; x < 1.0; x += 0.01) {
                final double error = FastMath.abs(p.value(x) - fitted.value(x)) /
                        (1.0 + FastMath.abs(p.value(x)));
                maxError = FastMath.max(maxError, error);
                assertTrue(FastMath.abs(error) < 0.01);
            }
        }
        assertTrue(maxError > 0.001);
    }

    private PolynomialFunction buildRandomPolynomial(int degree, Random randomizer) {
        final double[] coefficients = new double[degree + 1];
        for (int i = 0; i <= degree; ++i) {
            coefficients[i] = randomizer.nextGaussian();
        }
        return new PolynomialFunction(coefficients);
    }
}
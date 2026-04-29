package pdk.util.data.fitting;

import org.hipparchus.analysis.function.HarmonicOscillator;
import org.hipparchus.exception.MathIllegalArgumentException;
import org.hipparchus.util.FastMath;
import org.hipparchus.util.MathUtils;
import org.junit.jupiter.api.Test;
import pdk.util.chart.LineChart;
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
 * @since 29 Apr 2026, 1:01 PM
 */
class HarmonicFitterTest {
    /**
     * Zero points is not enough observed points.
     */
    @Test
    void testPreconditions1() {
        assertThrows(MathIllegalArgumentException.class, () -> HarmonicFitter.create().fit(new ArrayList<>()));
    }


    @Test
    void testNoError() {
        final double a = 0.2;
        final double w = 3.4;
        final double p = 4.1;
        final HarmonicOscillator f = new HarmonicOscillator(a, w, p);

        final List<WeightPoint2D> points = new ArrayList<>();
        for (double x = 0.0; x < 1.3; x += 0.01) {
            points.add(new WeightPoint2D(x, f.value(x)));
        }

        final HarmonicFitter fitter = HarmonicFitter.create();
        final double[] fitted = fitter.fit(points);
        assertEquals(a, fitted[0], 1.0e-13);
        assertEquals(w, fitted[1], 1.0e-13);
        assertEquals(p, MathUtils.normalizeAngle(fitted[2], p), 1e-13);

        final HarmonicOscillator ff = new HarmonicOscillator(fitted[0], fitted[1], fitted[2]);
        for (double x = -1.0; x < 1.0; x += 0.01) {
            assertTrue(FastMath.abs(f.value(x) - ff.value(x)) < 1e-13);
        }
    }


    @Test
    void test1PercentError() {
        final Random randomizer = new Random(64925784252L);
        final double a = 0.2;
        final double w = 3.4;
        final double p = 4.1;
        final HarmonicOscillator f = new HarmonicOscillator(a, w, p);

        final List<WeightPoint2D> points = new ArrayList<>();
        for (double x = 0.0; x < 10.0; x += 0.1) {
            points.add(new WeightPoint2D(x, f.value(x) + 0.01 * randomizer.nextGaussian()));
        }

        final HarmonicFitter fitter = HarmonicFitter.create();
        final double[] fitted = fitter.fit(points);
        assertEquals(a, fitted[0], 7.6e-4);
        assertEquals(w, fitted[1], 2.7e-3);
        assertEquals(p, MathUtils.normalizeAngle(fitted[2], p), 1.3e-2);
    }


    @Test
    void testInitialGuess() {
        final Random randomizer = new Random(45314242L);
        final double a = 0.2;
        final double w = 3.4;
        final double p = 4.1;
        final HarmonicOscillator f = new HarmonicOscillator(a, w, p);

        final List<WeightPoint2D> points = new ArrayList<>();
        for (double x = 0.0; x < 10.0; x += 0.1) {
            points.add(new WeightPoint2D(x, f.value(x) + 0.01 * randomizer.nextGaussian()));
        }

        final CurveFitter fitter = HarmonicFitter.create()
                .initGuess(new double[]{0.15, 3.6, 4.5});
        final double[] fitted = fitter.fit(points);
        assertEquals(a, fitted[0], 1.2e-3);
        assertEquals(w, fitted[1], 3.3e-3);
        assertEquals(p, MathUtils.normalizeAngle(fitted[2], p), 1.7e-2);
    }


    @Test
    void testUnsorted() {
        Random randomizer = new Random(64925784252L);
        final double a = 0.2;
        final double w = 3.4;
        final double p = 4.1;
        final HarmonicOscillator f = new HarmonicOscillator(a, w, p);

        // Build a regularly spaced array of measurements.
        final int size = 100;
        final double[] xTab = new double[size];
        final double[] yTab = new double[size];
        for (int i = 0; i < size; i++) {
            xTab[i] = 0.1 * i;
            yTab[i] = f.value(xTab[i]) + 0.01 * randomizer.nextGaussian();
        }

        // shake it
        for (int i = 0; i < size; i++) {
            int i1 = randomizer.nextInt(size);
            int i2 = randomizer.nextInt(size);
            double xTmp = xTab[i1];
            double yTmp = yTab[i1];
            xTab[i1] = xTab[i2];
            yTab[i1] = yTab[i2];
            xTab[i2] = xTmp;
            yTab[i2] = yTmp;
        }

        // Pass it to the fitter.
        List<WeightPoint2D> points = WeightPoint2D.convert(xTab, yTab);

        final HarmonicFitter fitter = HarmonicFitter.create();
        final double[] fitted = fitter.fit(points);
        assertEquals(a, fitted[0], 7.6e-4);
        assertEquals(w, fitted[1], 3.5e-3);
        assertEquals(p, MathUtils.normalizeAngle(fitted[2], p), 1.5e-2);
    }

    static void main() {
        final Random randomizer = new Random(64925784252L);
        final double a = 0.2;
        final double w = 3.4;
        final double p = 4.1;
        final HarmonicOscillator f = new HarmonicOscillator(a, w, p);

        final List<WeightPoint2D> points = new ArrayList<>();
        for (double x = 0.0; x < 10.0; x += 0.1) {
            points.add(new WeightPoint2D(x, f.value(x) + 0.01 * randomizer.nextGaussian()));
        }

        final HarmonicFitter fitter = HarmonicFitter.create();
        final double[] fitted = fitter.fit(points);

        LineChart chart = fitter.showFit(fitted, points);
        chart.show();
    }
}
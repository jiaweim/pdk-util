package pdk.util.data.fitting;

import org.hipparchus.analysis.function.Gaussian;
import org.hipparchus.exception.LocalizedCoreFormats;
import org.hipparchus.exception.MathIllegalArgumentException;
import org.hipparchus.linear.DiagonalMatrix;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresBuilder;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresProblem;
import org.hipparchus.util.FastMath;
import pdk.util.data.WeightPoint2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Fits points to a Gaussian function.
 * <p>
 * The {@link #initGuess(double[])} must be passed
 * in the following order:
 * <ul>
 *  <li>Normalization</li>
 *  <li>Mean</li>
 *  <li>Sigma</li>
 * </ul>
 * The optimal values will be returned in the same order.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 29 Apr 2026, 9:22 AM
 */
public class GaussianFitter extends CurveFitter {

    private static final Gaussian.Parametric FUNCTION = new Gaussian.Parametric();

    /**
     * Creates a default Gaussian curve fitter.
     * <p>
     * The initial guess for the parameters will be {@link ParameterGuesser}
     * computed automatically, and the maximum number of iterations of the
     * optimization algorithm is set to {@link Integer#MAX_VALUE}.
     *
     * @return {@link GaussianFitter}
     */
    public static GaussianFitter create() {
        return new GaussianFitter();
    }

    public GaussianFitter() {
        super();
    }

    public GaussianFitter(double[] initialGuess, int maxIterations) {
        super(initialGuess, maxIterations);
    }

    /**
     * Compute the value of the function.
     *
     * @param x          Point for which the function value should be computed.
     * @param parameters Function parameters.
     * @return the value.
     */
    @Override
    public double value(double x, double... parameters) {
        double v = Double.POSITIVE_INFINITY;
        try {
            v = FUNCTION.value(x, parameters);
        } catch (MathIllegalArgumentException e) { // NOPMD
            // Do nothing.
        }
        return v;
    }

    /**
     * Compute the gradient of the function with respect to its parameters.
     *
     * @param x          Point for which the function value should be computed.
     * @param parameters Function parameters.
     * @return the value.
     */
    @Override
    public double[] gradient(double x, double... parameters) {
        double[] v = {
                Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY
        };
        try {
            v = FUNCTION.gradient(x, parameters);
        } catch (MathIllegalArgumentException e) { // NOPMD
            // Do nothing.
        }
        return v;
    }

    @Override
    protected LeastSquaresProblem getProblem(Collection<WeightPoint2D> observations) {
        // Prepare least-squares problem.
        final int len = observations.size();
        final double[] target = new double[len];
        final double[] weights = new double[len];
        final double[] x = new double[len];

        int i = 0;
        for (WeightPoint2D obs : observations) {
            x[i] = obs.getX();
            target[i] = obs.getY();
            weights[i] = obs.getWeight();
            ++i;
        }

        final double[] startPoint = initialGuess_ != null ?
                initialGuess_ :
                // Compute estimation.
                new ParameterGuesser(observations).guess();

        // Return a new least squares problem set up to fit a Gaussian curve to the
        // observed points.
        return new LeastSquaresBuilder().
                maxEvaluations(Integer.MAX_VALUE).
                maxIterations(maxIterations_).
                start(startPoint).
                target(target).
                weight(new DiagonalMatrix(weights)).
                model(getModelFunction(x), getModelFunctionJacobian(x)).
                build();
    }

    /**
     * Guesses the parameters {@code norm}, {@code mean}, and {@code sigma}
     * of a {@link org.hipparchus.analysis.function.Gaussian.Parametric}
     * based on the specified observed points.
     */
    public static class ParameterGuesser {
        /**
         * Normalization factor.
         */
        private final double norm;
        /**
         * Mean.
         */
        private final double mean;
        /**
         * Standard deviation.
         */
        private final double sigma;

        /**
         * Constructs instance with the specified observed points.
         *
         * @param observations Observed points from which to guess the
         *                     parameters of the Gaussian.
         * @throws org.hipparchus.exception.NullArgumentException if {@code observations} is
         *                                                        {@code null}.
         * @throws MathIllegalArgumentException                   if there are less than 3
         *                                                        observations.
         */
        public ParameterGuesser(Collection<WeightPoint2D> observations) {
            Objects.requireNonNull(observations);
            if (observations.size() < 3) {
                throw new MathIllegalArgumentException(LocalizedCoreFormats.NUMBER_TOO_SMALL,
                        observations.size(), 3);
            }

            final List<WeightPoint2D> sorted = new ArrayList<>(observations);
            sorted.sort(CurveFitter.DATA_COMPARATOR);
            final double[] params = basicGuess(sorted.toArray(new WeightPoint2D[0]));

            norm = params[0];
            mean = params[1];
            sigma = params[2];
        }

        /**
         * Gets an estimation of the parameters.
         *
         * @return the guessed parameters, in the following order:
         * <ul>
         *  <li>Normalization factor</li>
         *  <li>Mean</li>
         *  <li>Standard deviation</li>
         * </ul>
         */
        public double[] guess() {
            return new double[]{norm, mean, sigma};
        }

        /**
         * Guesses the parameters based on the specified observed points.
         *
         * @param points Observed points, sorted.
         * @return the guessed parameters (normalization factor, mean and
         * sigma).
         */
        private double[] basicGuess(WeightPoint2D[] points) {
            final int maxYIdx = findMaxY(points);
            final double n = points[maxYIdx].getY();
            final double m = points[maxYIdx].getX();

            double fwhmApprox;
            try {
                final double halfY = n + ((m - n) / 2);
                final double fwhmX1 = interpolateXAtY(points, maxYIdx, -1, halfY);
                final double fwhmX2 = interpolateXAtY(points, maxYIdx, 1, halfY);
                fwhmApprox = fwhmX2 - fwhmX1;
            } catch (MathIllegalArgumentException e) {
                // TODO: Exceptions should not be used for flow control.
                fwhmApprox = points[points.length - 1].getX() - points[0].getX();
            }
            final double s = fwhmApprox / (2 * FastMath.sqrt(2 * FastMath.log(2)));

            return new double[]{n, m, s};
        }

        /**
         * Finds index of point in specified points with the largest Y.
         *
         * @param points Points to search.
         * @return the index in specified points array.
         */
        private int findMaxY(WeightPoint2D[] points) {
            int maxYIdx = 0;
            for (int i = 1; i < points.length; i++) {
                if (points[i].getY() > points[maxYIdx].getY()) {
                    maxYIdx = i;
                }
            }
            return maxYIdx;
        }

        /**
         * Interpolates using the specified points to determine X at the
         * specified Y.
         *
         * @param points   Points to use for interpolation.
         * @param startIdx Index within points from which to start the search for
         *                 interpolation bounds points.
         * @param idxStep  Index step for searching interpolation bounds points.
         * @param y        Y value for which X should be determined.
         * @return the value of X for the specified Y.
         * @throws MathIllegalArgumentException if {@code idxStep} is 0.
         * @throws MathIllegalArgumentException if specified {@code y} is not within the
         *                                      range of the specified {@code points}.
         */
        private double interpolateXAtY(WeightPoint2D[] points,
                int startIdx,
                int idxStep,
                double y)
                throws MathIllegalArgumentException {
            if (idxStep == 0) {
                throw new MathIllegalArgumentException(LocalizedCoreFormats.ZERO_NOT_ALLOWED);
            }
            final WeightPoint2D[] twoPoints
                    = getInterpolationPointsForY(points, startIdx, idxStep, y);
            final WeightPoint2D p1 = twoPoints[0];
            final WeightPoint2D p2 = twoPoints[1];
            if (p1.getY() == y) {
                return p1.getX();
            }
            if (p2.getY() == y) {
                return p2.getX();
            }
            return p1.getX() + (((y - p1.getY()) * (p2.getX() - p1.getX())) /
                    (p2.getY() - p1.getY()));
        }

        /**
         * Gets the two bounding interpolation points from the specified points
         * suitable for determining X at the specified Y.
         *
         * @param points   Points to use for interpolation.
         * @param startIdx Index within points from which to start search for
         *                 interpolation bounds points.
         * @param idxStep  Index step for search for interpolation bounds points.
         * @param y        Y value for which X should be determined.
         * @return the array containing two points suitable for determining X at
         * the specified Y.
         * @throws MathIllegalArgumentException if {@code idxStep} is 0.
         * @throws MathIllegalArgumentException if specified {@code y} is not within the
         *                                      range of the specified {@code points}.
         */
        private WeightPoint2D[] getInterpolationPointsForY(WeightPoint2D[] points,
                int startIdx,
                int idxStep,
                double y)
                throws MathIllegalArgumentException {
            if (idxStep == 0) {
                throw new MathIllegalArgumentException(LocalizedCoreFormats.ZERO_NOT_ALLOWED);
            }
            for (int i = startIdx;
                 idxStep < 0 ? i + idxStep >= 0 : i + idxStep < points.length;
                 i += idxStep) {
                final WeightPoint2D p1 = points[i];
                final WeightPoint2D p2 = points[i + idxStep];
                if (isBetween(y, p1.getY(), p2.getY())) {
                    if (idxStep < 0) {
                        return new WeightPoint2D[]{p2, p1};
                    } else {
                        return new WeightPoint2D[]{p1, p2};
                    }
                }
            }

            // Boundaries are replaced by dummy values because the raised
            // exception is caught and the message never displayed.
            // TODO: Exceptions should not be used for flow control.
            throw new MathIllegalArgumentException(LocalizedCoreFormats.OUT_OF_RANGE_SIMPLE,
                    y, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }

        /**
         * Determines whether a value is between two other values.
         *
         * @param value     Value to test whether it is between {@code boundary1}
         *                  and {@code boundary2}.
         * @param boundary1 One end of the range.
         * @param boundary2 Other end of the range.
         * @return {@code true} if {@code value} is between {@code boundary1} and
         * {@code boundary2} (inclusive), {@code false} otherwise.
         */
        private boolean isBetween(double value,
                double boundary1,
                double boundary2) {
            return (value >= boundary1 && value <= boundary2) ||
                    (value >= boundary2 && value <= boundary1);
        }
    }
}

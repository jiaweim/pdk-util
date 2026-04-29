package pdk.util.data.fitting;

import org.hipparchus.analysis.polynomials.PolynomialFunction;
import org.hipparchus.exception.MathRuntimeException;
import org.hipparchus.linear.DiagonalMatrix;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresBuilder;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresProblem;
import pdk.util.data.WeightPoint2D;

import java.util.Collection;

/**
 * Fits points to a {@link
 * org.hipparchus.analysis.polynomials.PolynomialFunction.Parametric polynomial}
 * function.
 * <br>
 * The size of the {@link #initGuess(double[])} array defines the
 * degree of the polynomial to be fitted.
 * They must be sorted in increasing order of the polynomial's degree.
 * The optimal values of the coefficients will be returned in the same order.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 29 Apr 2026, 11:51 AM
 */
public class PolynomialFitter extends CurveFitter {

    private static final PolynomialFunction.Parametric FUNCTION = new PolynomialFunction.Parametric();

    public static PolynomialFitter create(int degree) {
        return new PolynomialFitter(new double[degree + 1], Integer.MAX_VALUE);
    }

    /**
     * Constructor
     *
     * @param initialGuess  Provided initial parameter values
     * @param maxIterations Maximum number of iterations of the optimization algorithm.
     */
    private PolynomialFitter(double[] initialGuess, int maxIterations) {
        super(initialGuess, maxIterations);
    }

    @Override
    public double value(double x, double... parameters) {
        return FUNCTION.value(x, parameters);
    }

    @Override
    public double[] gradient(double x, double... parameters) {
        return FUNCTION.gradient(x, parameters);
    }

    /**
     * Creates a least squares problem corresponding to the appropriate curve.
     *
     * @param points Sample points.
     * @return the least squares problem to use for fitting the curve to the
     * given {@code points}.
     */
    @Override
    protected LeastSquaresProblem getProblem(Collection<WeightPoint2D> points) {
        // Prepare least-squares problem.
        final int len = points.size();
        final double[] x = new double[len];
        final double[] target = new double[len];
        final double[] weights = new double[len];

        int i = 0;
        for (WeightPoint2D obs : points) {
            x[i] = obs.getX();
            target[i] = obs.getY();
            weights[i] = obs.getWeight();
            ++i;
        }

        if (initialGuess_ == null) {
            throw MathRuntimeException.createInternalError();
        }

        // Return a new least squares problem set up to fit a polynomial curve to the
        // observed points.
        return new LeastSquaresBuilder().
                maxEvaluations(Integer.MAX_VALUE).
                maxIterations(maxIterations_).
                start(initialGuess_).
                target(target).
                weight(new DiagonalMatrix(weights)).
                model(getModelFunction(x), getModelFunctionJacobian(x)).
                build();
    }
}

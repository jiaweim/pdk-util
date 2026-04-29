package pdk.util.data.fitting;

import org.hipparchus.analysis.ParametricUnivariateFunction;
import org.hipparchus.linear.DiagonalMatrix;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresBuilder;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresProblem;
import pdk.util.data.WeightPoint2D;

import java.util.Collection;

/**
 * Fit points to a user-defined function.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 29 Apr 2026, 11:47 AM
 */
public class GeneralFitter extends CurveFitter {

    /**
     * Create a {@link GeneralFitter}
     *
     * @param function {@link ParametricUnivariateFunction}
     * @return A {@link GeneralFitter} instance.
     */

    public static GeneralFitter create(ParametricUnivariateFunction function) {
        return new GeneralFitter(function);
    }

    private final ParametricUnivariateFunction function_;

    /**
     * Constructor
     */
    private GeneralFitter(ParametricUnivariateFunction function) {
        super();
        this.function_ = function;
    }

    @Override
    public double value(double x, double... parameters) {
        return function_.value(x, parameters);
    }

    @Override
    public double[] gradient(double x, double... parameters) {
        return function_.gradient(x, parameters);
    }

    @Override
    protected LeastSquaresProblem getProblem(Collection<WeightPoint2D> points) {
        // Prepare least-squares problem.
        final int len = points.size();
        final double[] x = new double[len];
        final double[] target = new double[len];
        final double[] weights = new double[len];

        int count = 0;
        for (WeightPoint2D obs : points) {
            x[count] = obs.getX();
            target[count] = obs.getY();
            weights[count] = obs.getWeight();
            ++count;
        }

        // Create an optimizer for fitting the curve to the observed points.
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

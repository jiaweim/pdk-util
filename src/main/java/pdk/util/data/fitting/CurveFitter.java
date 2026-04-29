package pdk.util.data.fitting;

import org.hipparchus.analysis.MultivariateMatrixFunction;
import org.hipparchus.analysis.MultivariateVectorFunction;
import org.hipparchus.analysis.ParametricUnivariateFunction;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresOptimizer;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresProblem;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LevenbergMarquardtOptimizer;
import org.jfree.data.xy.XYDataset;
import pdk.util.chart.Data;
import pdk.util.chart.LineChart;
import pdk.util.data.Point2D;
import pdk.util.data.WeightPoint2D;
import pdk.util.data.func.Func2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Base class for fitting parametric univariate real functions <code>y=f(p<sub>i</sub>;x)</code>, where
 * {@code  x} is the independent variable, and the <code>p<sub>i</sub></code> are the parameters.
 * <p>
 * A fitter will find the optimal values of the parameters by
 * <em>fitting</em> the curve so it remains very close to a set of
 * {@code N} observed points <code>(x<sub>k</sub>, y<sub>k</sub>)</code>,
 * {@code 0 <= k < N}.
 * <p>
 * An algorithm usually performs the fit by finding the parameter
 * values that minimizes the objective function
 * <pre>
 *  &sum;(y<sub>k</sub> - f(x<sub>k</sub>))<sup>2</sup>
 * </pre>
 * which is actually a least-squares problem.
 * This class contains boilerplate code for calling the
 * {@link #fit(Collection)} method for obtaining the parameters.
 * The problem setup, such as the choice of optimization algorithm
 * for fitting a specific function is delegated to subclasses.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 29 Apr 2026, 9:06 AM
 */
public abstract class CurveFitter implements ParametricUnivariateFunction {

    protected double[] initialGuess_;
    protected int maxIterations_;

    /**
     * Constructor
     */
    protected CurveFitter() {
        this(null, Integer.MAX_VALUE);
    }

    /**
     * Constructor
     *
     * @param initialGuess  Provided initial parameter values
     * @param maxIterations Maximum number of iterations of the optimization algorithm.
     */
    public CurveFitter(double[] initialGuess, int maxIterations) {
        this.initialGuess_ = initialGuess;
        this.maxIterations_ = maxIterations;
    }

    /**
     * Set the initial parameter values before fitting
     *
     * @param initialGuess parameter values
     * @return this
     */
    public CurveFitter initGuess(double[] initialGuess) {
        this.initialGuess_ = initialGuess;
        return this;
    }

    /**
     * Set the maximum number of iterations of the optimization algorithm.
     *
     * @param maxIterations number of iterations
     * @return this
     */
    public CurveFitter maxIterations(int maxIterations) {
        this.maxIterations_ = maxIterations;
        return this;
    }

    /**
     * Compute the value of the function.
     *
     * @param x          Point for which the function value should be computed.
     * @param parameters Function parameters.
     * @return the value.
     */
    @Override
    public abstract double value(double x, double... parameters);

    /**
     * Compute the gradient of the function with respect to its parameters.
     *
     * @param x          Point for which the function value should be computed.
     * @param parameters Function parameters.
     * @return the value.
     */
    @Override
    public abstract double[] gradient(double x, double... parameters);

    /**
     * Fits a curve.
     * This method computes the coefficients of the curve that best
     * fit the sample of observed points.
     *
     * @param points Observations.
     * @return the fitted parameters.
     */
    public double[] fit(Collection<WeightPoint2D> points) {
        // Perform the fit.
        return getOptimizer().optimize(getProblem(points)).getPoint().toArray();
    }

    /**
     * Fits a curve.
     * This method computes the coefficients of the curve that best
     * fit the sample of observed points.
     *
     * @param points Observations.
     * @return the fitted parameters.
     */
    public LeastSquaresOptimizer.Optimum fitDetail(Collection<WeightPoint2D> points) {
        return getOptimizer().optimize(getProblem(points));
    }

    /**
     * Creates an optimizer set up to fit the appropriate curve.
     * <p>
     * The default implementation uses a {@link LevenbergMarquardtOptimizer
     * Levenberg-Marquardt} optimizer.
     * </p>
     *
     * @return the optimizer to use for fitting the curve to the
     * given {@code points}.
     */
    public LeastSquaresOptimizer getOptimizer() {
        return new LevenbergMarquardtOptimizer();
    }

    /**
     * Creates a least squares problem corresponding to the appropriate curve.
     *
     * @param points Sample points.
     * @return the least squares problem to use for fitting the curve to the
     * given {@code points}.
     */
    protected abstract LeastSquaresProblem getProblem(Collection<WeightPoint2D> points);

    /**
     * Return the model function
     *
     * @param x input x values
     * @return model function value
     */
    protected MultivariateVectorFunction getModelFunction(double[] x) {
        return parameters -> {
            final int len = x.length;
            final double[] values = new double[len];
            for (int i = 0; i < len; i++) {
                values[i] = CurveFitter.this.value(x[i], parameters);
            }
            return values;
        };
    }

    /**
     * Return the model function jacobian
     *
     * @param x input x values
     * @return the model function Jacobian
     */
    protected MultivariateMatrixFunction getModelFunctionJacobian(double[] x) {
        return parameters -> {
            final int len = x.length;
            final double[][] jacobian = new double[len][];
            for (int i = 0; i < len; i++) {
                jacobian[i] = CurveFitter.this.gradient(x[i], parameters);
            }
            return jacobian;
        };
    }

    /**
     * Create a line chart to display the measured points and fitted points.
     *
     * @param parameters estimated parameters
     * @param dataset    measured points
     * @return {@link LineChart}
     */
    public LineChart showFit(double[] parameters, Collection<WeightPoint2D> dataset) {
        double minX = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;

        for (WeightPoint2D point : dataset) {
            double x = point.getX();
            minX = Math.min(x, minX);
            maxX = Math.max(x, maxX);
        }
        double extend = (maxX - minX) * 0.1;

        return showFit(parameters, dataset, minX - extend, maxX + extend, dataset.size() * 2);
    }

    /**
     * Create a line chart to display the measured points and fitted points.
     *
     * @param parameters estimated parameters
     * @param dataset    measured points
     * @param start      the start sampling point
     * @param end        the end sampling point
     * @param sampleSize Number of data points sampled from the fitting function
     * @return {@link LineChart}
     */
    public LineChart showFit(double[] parameters, Collection<WeightPoint2D> dataset,
            double start, double end, int sampleSize) {
        Func2D func2D = x -> CurveFitter.this.value(x, parameters);
        List<Point2D> fitSample = func2D.sample(start, end, sampleSize);

        XYDataset data = Data.xyDataset()
                .addSeries("Actual", new ArrayList<>(dataset))
                .addSeries("Fitting", fitSample)
                .build();
        return LineChart.lineChart()
                .dataset(data)
                .addLegend(true)
                .xAxisAutoRangeIncludesZero(false)
                .seriesLinesWidth(0, 4F)
                .seriesLinesWidth(1, 4F)
                .build();
    }

    /**
     * Default comparator for {@link WeightPoint2D}
     */
    public static final Comparator<WeightPoint2D> DATA_COMPARATOR = (o1, o2) -> {
        if (o1 == null && o2 == null) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        int result = Double.compare(o1.getX(), o2.getX());
        if (result != 0) {
            return result;
        }
        result = Double.compare(o1.getY(), o2.getY());
        if (result != 0) {
            return result;
        }
        result = Double.compare(o1.getWeight(), o2.getWeight());
        return result;
    };
}

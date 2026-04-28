package pdk.util.data.fitting;

import org.apache.commons.numbers.gamma.Erfc;
import org.apache.commons.statistics.descriptive.DoubleStatistics;
import org.apache.commons.statistics.descriptive.Statistic;
import org.hipparchus.analysis.ParametricUnivariateFunction;
import org.hipparchus.fitting.AbstractCurveFitter;
import org.hipparchus.fitting.WeightedObservedPoint;
import org.hipparchus.linear.DiagonalMatrix;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresBuilder;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresProblem;
import org.hipparchus.util.FastMath;

import java.util.Collection;
import java.util.Objects;

/**
 * Model for Exponentially modified Gaussian distribution function.
 *
 * @author Jiawei Mao
 * @version 1.0.0⭐
 * @since 28 Apr 2026, 1:07 PM
 */
public class EMGCurveFitter extends AbstractCurveFitter {

    private static final double SQRT2PI = FastMath.sqrt(2.0 * FastMath.PI);

    /**
     * Parametric function where the input array contains the parameters of the Exponential Modified Gaussian, ordered as
     * follows:
     * <ul>
     *  <li>Norm of the Area</li>
     *  <li>Mean</li>
     *  <li>Standard deviation</li>
     *  <li>tau: Exponential decay constant=1\lambda</li>
     * </ul>
     */
    public static final ParametricUnivariateFunction FUNCTION = new ParametricUnivariateFunction() {
        @Override
        public double value(double x, double... parameters) {
            double h = parameters[0];
            double mu = parameters[1];
            double sigma = parameters[2];
            double tau = parameters[3];

            // 预计算中间变量
            double sigma2 = sigma * sigma;
            double tau2 = tau * tau;

            double argExp = (sigma2 / (2.0 * tau2)) - ((x - mu) / tau);
            double argErfc = (sigma / tau - (x - mu) / sigma) / FastMath.sqrt(2.0);

            // 这里 h 代表总面积
            // 如果 h 代表高斯分量高度，两者相差 $\sigma \sqrt{2\pi}$ 因子，除以该因子得到面积
            return h / (2.0 * tau) * FastMath.exp(argExp) * Erfc.value(argErfc);
        }

        @Override
        public double[] gradient(double x, double... parameters) {
            double h = parameters[0];
            double mu = parameters[1];
            double sigma = parameters[2];
            double tau = parameters[3];

            double dX = x - mu;
            double sigma2 = sigma * sigma;
            double tau2 = tau * tau;

            double f = value(x, parameters);

            // 计算标准归一化高斯概率密度 G_norm(x)
            double gaussianNormalized = (1.0 / (sigma * SQRT2PI))
                    * FastMath.exp(-dX * dX / (2.0 * sigma2));

            double[] grad = new double[4];

            // 1. d(f)/dh = f / h
            grad[0] = f / h;

            // 2. d(f)/dmu = (f - h * G_norm) / tau
            grad[1] = (f - h * gaussianNormalized) / tau;

            // 3. d(f)/dsigma = (sigma / tau^2) * f - (h / tau) * (sigma / tau + (x - mu) / sigma) * G_norm
            grad[2] = (sigma / tau2) * f
                    - (h / tau) * (sigma / tau + dX / sigma) * gaussianNormalized;

            // 4. d(f)/dtau = (1 / tau) * [ f * ((x - mu) / tau - sigma^2 / tau^2 - 1) + (h * sigma^2 / tau^2) * G_norm ]
            grad[3] = (1.0 / tau) * (f * (dX / tau - sigma2 / tau2 - 1.0)
                    + (h * sigma2 / tau2) * gaussianNormalized);

            return grad;
        }
    };


    /**
     * Creates a default curve fitter. The initial guess for the paramters will be {@link #guess(Collection)} computed
     * automatically, and the maximum number of iterations of the optimization algorithm is set to {@link Integer#MAX_VALUE}
     *
     * @return a curve fitter
     */
    public static EMGCurveFitter create() {
        return new EMGCurveFitter(null, Integer.MAX_VALUE);
    }

    private final double[] initialGuess;
    private final int maxIter;

    public EMGCurveFitter(double[] initialGuess, int maxIter) {
        this.initialGuess = initialGuess;
        this.maxIter = maxIter;
    }

    @Override
    protected LeastSquaresProblem getProblem(Collection<WeightedObservedPoint> points) {
        int len = points.size();
        final double[] target = new double[len];
        final double[] weights = new double[len];
        int i = 0;
        for (WeightedObservedPoint point : points) {
            target[i] = point.getY();
            weights[i] = point.getWeight();
            i++;
        }
        final AbstractCurveFitter.TheoreticalValuesFunction model =
                new TheoreticalValuesFunction(FUNCTION, points);

        final double[] startPoint = initialGuess != null ? initialGuess : guess(points);

        return new LeastSquaresBuilder()
                .maxEvaluations(Integer.MAX_VALUE)
                .maxIterations(maxIter)
                .start(startPoint)
                .target(target)
                .weight(new DiagonalMatrix(weights))
                .model(model.getModelFunction(), model.getModelFunctionJacobian())
                .build();
    }

    /**
     * Guess the parameters a given sample
     *
     * @param points sample
     * @return parameters
     */
    public static double[] guess(Collection<WeightedObservedPoint> points) {
        Objects.requireNonNull(points);
        WeightedObservedPoint[] pointArray = points.toArray(new WeightedObservedPoint[0]);

        DoubleStatistics stats = DoubleStatistics.of(Statistic.MEAN, Statistic.VARIANCE, Statistic.SKEWNESS);
        double sumY = 0;
        for (WeightedObservedPoint p : points) {
            // 为了计算统计矩，我们将 y 视为频数/权重
            // 实际上可以用更简化的方式：
            stats.accept(p.getX());
            sumY += p.getY();
        }

        double mean = stats.getAsDouble(Statistic.MEAN);
        double variance = stats.getAsDouble(Statistic.VARIANCE);
        double skewness = stats.getAsDouble(Statistic.SKEWNESS);

        // 1. 估算 Tau (基于偏度)
        // 偏度公式需注意样本量，这里做一个鲁棒性处理
        double tau = FastMath.pow(FastMath.max(0, skewness) * FastMath.pow(variance, 1.5) / 2.0, 1.0 / 3.0);
        if (Double.isNaN(tau) || tau < 0.01)
            tau = FastMath.sqrt(variance) * 0.5;

        // 2. 估算 Mu
        double mu = mean - tau;

        // 3. 估算 Sigma
        double sigma = FastMath.sqrt(FastMath.max(0.01, variance - tau * tau));

        // 4. 估算 Area (h)
        // 假设 x 是均匀采样的，deltaX 为平均间距
        double deltaX = (pointArray[pointArray.length - 1].getX() - pointArray[0].getX()) / points.size();
        double h = sumY * deltaX;

        return new double[]{h, mu, sigma, tau};
    }
}

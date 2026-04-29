package pdk.util.data.fitting;

import org.apache.commons.numbers.gamma.Erfc;
import org.apache.commons.statistics.descriptive.DoubleStatistics;
import org.apache.commons.statistics.descriptive.Statistic;
import org.hipparchus.linear.DiagonalMatrix;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresBuilder;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresProblem;
import org.hipparchus.util.FastMath;
import pdk.util.data.WeightPoint2D;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Model for Exponentially modified Gaussian distribution function.
 * <p>
 * Parametric function where the input array contains the parameters of the Exponential Modified Gaussian, ordered as
 * <ul>
 *  <li>Norm of the Area</li>
 *  <li>Mean</li>
 *  <li>Standard deviation</li>
 *  <li>tau: Exponential decay constant=1\lambda</li>
 * </ul>
 *
 * @author Jiawei Mao
 * @version 1.0.0⭐
 * @since 28 Apr 2026, 1:07 PM
 */
public class EMGFitter extends CurveFitter {

    private static final double SQRT2 = FastMath.sqrt(2.0);
    private static final double SQRT2PI = FastMath.sqrt(2.0 * FastMath.PI);

    /**
     * Creates a default curve fitter. The initial guess for the paramters will be {@link #guess(Collection)} computed
     * automatically, and the maximum number of iterations of the optimization algorithm is set to {@link Integer#MAX_VALUE}
     *
     * @return a curve fitter
     */
    public static EMGFitter create() {
        return new EMGFitter(null, Integer.MAX_VALUE);
    }

    public EMGFitter(double[] initialGuess, int maxIter) {
        super(initialGuess, maxIter);
    }

    @Override
    public double value(double x, double... parameters) {
        double h = parameters[0];
        double mu = parameters[1];
        double sigma = parameters[2];
        double tau = parameters[3];

        double dx = x - mu;

        double argExp = (sigma * sigma / (2.0 * tau * tau)) - (dx / tau);
        double argErfc = (sigma / tau - dx / sigma) / SQRT2;

        // 这里 h 代表总面积
        // 如果 h 代表高斯分量高度，两者相差 $\sigma \sqrt{2\pi}$ 倍，除以该因子得到面积
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
        grad[2] = (sigma / tau2) * f - (h / tau) * (sigma / tau + dX / sigma) * gaussianNormalized;

        // 4. d(f)/dtau = (1 / tau) * [ f * ((x - mu) / tau - sigma^2 / tau^2 - 1) + (h * sigma^2 / tau^2) * G_norm ]
        grad[3] = (1.0 / tau) * (f * (dX / tau - sigma2 / tau2 - 1.0)
                + (h * sigma2 / tau2) * gaussianNormalized);

        return grad;
    }

    @Override
    protected LeastSquaresProblem getProblem(Collection<WeightPoint2D> points) {
        int len = points.size();
        final double[] x = new double[len];
        final double[] target = new double[len];
        final double[] weights = new double[len];
        int i = 0;
        for (WeightPoint2D point : points) {
            x[i] = point.getX();
            target[i] = point.getY();
            weights[i] = point.getWeight();
            i++;
        }

        final double[] startPoint = initialGuess_ != null ? initialGuess_ : guess(points);

        return new LeastSquaresBuilder()
                .maxEvaluations(Integer.MAX_VALUE)
                .maxIterations(maxIterations_)
                .start(startPoint)
                .target(target)
                .weight(new DiagonalMatrix(weights))
                .model(getModelFunction(x), getModelFunctionJacobian(x))
                .build();
    }

    /**
     * Estimate the parameter values by the moment estimation.
     *
     * @param points sample
     * @return parameters
     */
    public static double[] guess(Collection<WeightPoint2D> points) {
        Objects.requireNonNull(points);
        WeightPoint2D[] pointArray = points.toArray(new WeightPoint2D[0]);

        DoubleStatistics stats = DoubleStatistics.of(Statistic.MEAN, Statistic.VARIANCE, Statistic.SKEWNESS);
        double sumY = 0;
        for (WeightPoint2D p : points) {
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

    /**
     * Estimate the parameter values by heuristic method.
     * <p>
     * This method is more robust than momenet estimation.
     *
     * @param points sample
     * @return parameters
     */
    public static double[] guessByShape(Collection<WeightPoint2D> points) {
        Objects.requireNonNull(points);

        WeightPoint2D[] pointArray = points.toArray(new WeightPoint2D[0]);
        int maxIdx = 0;
        for (int i = 1; i < pointArray.length; i++) {
            if (pointArray[i].getY() > pointArray[maxIdx].getY()) {
                maxIdx = i;
            }
        }
        Arrays.sort(pointArray, CurveFitter.DATA_COMPARATOR);

        double xMax = pointArray[maxIdx].getX();
        double yMax = pointArray[maxIdx].getY();
        double halfMax = yMax / 2;

        double xLeft = pointArray[0].getX();
        double xRight = pointArray[pointArray.length - 1].getX();

        // 寻找左半高点和右半高点
        for (int i = 0; i < pointArray.length; i++) {
            if (pointArray[i].getY() >= halfMax) {
                xLeft = pointArray[i].getX();
                break;
            }
        }
        for (int i = pointArray.length - 1; i >= 0; i--) {
            if (pointArray[i].getY() >= halfMax) {
                xRight = pointArray[i].getX();
                break;
            }
        }

        // 计算左右两侧的半峰宽
        double wLeft = Math.max(0.01, xMax - xLeft);
        double wRight = Math.max(0.01, xRight - xMax);
        double fwhm = xRight - xLeft;

        // 使用受拖尾较小的左侧估算 sigma
        double sigma = wLeft / 1.177;

        // 右侧宽度超出部分主要由 tau 贡献
        double tau = Math.max(0.1 * sigma, wRight - wLeft);

        // mu 峰顶位置向左修正
        double mu = xMax - (tau * sigma * sigma) / (sigma * sigma + tau * tau);
        if (Double.isNaN(mu)) {
            mu = xMax - 0.1 * fwhm;
        }

        // 梯形积分估计面积
        double area = 0;
        for (int i = 0; i < pointArray.length - 1; i++) {
            double dx = pointArray[i + 1].getX() - pointArray[i].getX();
            double avgY = (pointArray[i + 1].getY() - pointArray[i].getY()) / 2.0;
            area += dx * avgY;
        }

        return new double[]{Math.max(1e-6, area), mu, Math.max(1e-6, sigma), Math.max(1e-6, tau)};
    }
}

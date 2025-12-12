package pdk.util.math;

import org.apache.commons.numbers.combinatorics.BinomialCoefficient;
import org.apache.commons.numbers.combinatorics.BinomialCoefficientDouble;

/**
 * Probability utilities.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 12 Dec 2025, 1:10 PM
 */
public final class ProbUtils {

    /**
     * Computes the binomial coefficient.
     *
     * <p>The largest value of {@code n} for which <em>all</em> coefficients can
     * fit into a {@code long} is 66. Larger {@code n} may result in an
     * {@link ArithmeticException} depending on the value of {@code k}.
     *
     * <p>Any {@code min(k, n - k) >= 34} cannot fit into a {@code long}
     * and will result in an {@link ArithmeticException}.
     *
     * @param n Size of the set.
     * @param k Size of the subsets to be counted.
     * @return {@code n choose k}.
     * @throws IllegalArgumentException if {@code n < 0}, {@code k < 0} or {@code k > n}.
     * @throws ArithmeticException      if the result is too large to be
     *                                  represented by a {@code long}.
     */
    public static long binomialCoefficient(int n, int k) {
        return BinomialCoefficient.value(n, k);
    }

    /**
     * Computes the binomial coefficient.
     *
     * <p>The largest value of {@code n} for which <em>all</em> coefficients can
     * fit into a {@code double} is 1029. Larger {@code n} may result in
     * infinity depending on the value of {@code k}.
     *
     * <p>Any {@code min(k, n - k) >= 515} cannot fit into a {@code double}
     * and will result in infinity.
     *
     * @param n Size of the set.
     * @param k Size of the subsets to be counted.
     * @return {@code n choose k}.
     * @throws IllegalArgumentException if {@code n < 0}, {@code k < 0} or {@code k > n}.
     */
    public static double binomialCoefficientDouble(int n, int k) {
        return BinomialCoefficientDouble.value(n, k);
    }

    static void main() {
        double Millon = 1000000.0;

        double pH1 = 35 / Millon;
        double pH2 = 11 / Millon;

        double likelihood1 = 0.98 * 0.3 * 0.28;
        double likelihood2 = 0.94 * 0.83 * 0.49;

        double r = pH1 * likelihood1 / (pH2 * likelihood2);
        System.out.println(r);

    }
}

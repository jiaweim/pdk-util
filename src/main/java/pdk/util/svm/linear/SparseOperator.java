package pdk.util.svm.linear;

import static java.util.Objects.requireNonNull;

/**
 * This class defines operations related to sparse vectors.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 May 2026, 3:46 PM
 */
class SparseOperator {

    /**
     * Computes the squared L2 norm of the given sparse feature vector: {@code sum(feature.value^2)}.
     *
     * @param x a {@link Feature} array.
     * @return sum of the squared feature values.
     */
    static double norm2Squared(Feature[] x) {
        requireNonNull(x);

        double ret = 0;
        for (Feature feature : x) {
            ret += feature.getValue() * feature.getValue();
        }
        return ret;
    }

    /**
     * Compute the dot product of a dense vector {@code s} and a sparse vector {@code x}.
     * <p>
     * It is often used to compute the linear combination of the dense model weight vector
     * and the sparse sample feature vector.
     *
     * @param s a dense vector.
     * @param x a sparse vector.
     * @return dot product.
     */
    static double dot(double[] s, Feature[] x) {
        double ret = 0;
        for (Feature feature : x) {
            ret += s[feature.getIndex() - 1] * feature.getValue();
        }
        return ret;
    }

    /**
     * Compute the dot product (inner product) of two sparse vectors.
     * <p>
     * Assume both arrays are sorted in ascending order by index, which is a common storage format for sparse vectors.
     * <p>
     * In machine learning, when feature vectors are stored in sparse representation
     * (as in the data formats of Liblinear and LibSVM), this efficient dot product algorithm
     * is widely used to calculate the similarity or kernel function value between two samples.
     *
     * @param x1 a {@link Feature} array.
     * @param x2 a {@link Feature} array.
     * @return dot product.
     */
    static double sparseDot(Feature[] x1, Feature[] x2) {
        double ret = 0;

        int pos1 = 0;
        int pos2 = 0;

        while (pos1 < x1.length && pos2 < x2.length) {
            int index1 = x1[pos1].getIndex();
            int index2 = x2[pos2].getIndex();
            if (index1 == index2) {
                ret += x1[pos1].getValue() * x2[pos2].getValue();
                pos1++;
                pos2++;
            } else {
                if (index1 > index2) {
                    pos2++;
                } else {
                    pos1++;
                }
            }
        }

        return ret;
    }

    /**
     * Performs the AXPY operation {@code y := a * x + y} in place, where
     * {@code x} is a sparse feature vector and {@code y} is a dense vector.
     *
     * <p>For each feature {@code (i, v)} in {@code x}, the entry
     * {@code y[i - 1]} is updated as {@code y[i - 1] += a * v}. Feature
     * indices are 1-based, so one is subtracted to address the 0-based dense
     * array. Components of {@code y} that do not correspond to any feature in
     * {@code x} are left unchanged, which is equivalent to adding zero for the
     * missing entries of the sparse vector.
     *
     * <p>This method mutates {@code y} in place and returns no value.
     *
     * <p><strong>Preconditions:</strong>
     * <ul>
     *   <li>{@code x} is non-null and contains no {@code null} elements;</li>
     *   <li>{@code y} is non-null;</li>
     *   <li>every feature index in {@code x} lies in {@code [1, y.length]};</li>
     *   <li>each feature index occurs at most once in {@code x}. If the same
     *       index appears multiple times, its contributions are accumulated
     *       once per occurrence.</li>
     * </ul>
     *
     * <p>No explicit validation is performed, since this method is intended
     * for performance-sensitive internal code whose callers uphold the above
     * invariants. Violating a precondition results in
     * {@link NullPointerException} or {@link ArrayIndexOutOfBoundsException}.
     *
     * <p>If {@code a * v} evaluates to {@code NaN} or an infinite value for
     * some feature, the corresponding entry of {@code y} becomes {@code NaN}
     * or infinite as well, following IEEE 754 arithmetic. This method is not
     * thread-safe with respect to concurrent mutation of {@code x}.
     *
     * @param a the scalar multiplier
     * @param x the sparse feature vector to be scaled and added; must not be
     *          {@code null} and must not contain {@code null} elements
     * @param y the dense vector to be updated in place; must not be
     *          {@code null}, and its length must be at least the largest
     *          feature index in {@code x}
     */
    static void axpy(double a, Feature[] x, double[] y) {
        for (Feature feature : x) {
            y[feature.getIndex() - 1] += a * feature.getValue();
        }
    }
}

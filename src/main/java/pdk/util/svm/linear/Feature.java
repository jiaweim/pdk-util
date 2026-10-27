package pdk.util.svm.linear;

import java.util.Objects;

/**
 * A single entry of a sparse feature vector, represented as an {@code (index, value)} pair.
 *
 * <p>Feature vectors are stored sparsely: only non-zero entries are
 * materialized, and each entry maps a <strong>1-based</strong> feature index
 * to its real-valued weight. Index {@code 0} is reserved and must not be
 * used; a valid instance always has a positive index.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 12:52 PM
 */
public final class Feature {

    /**
     * The 1-based index of this feature.
     *
     * <p>Prefer {@link #getIndex()} when writing code that may later be
     * ported to an interface-based abstraction of this class.
     */
    public final int index;
    /**
     * The value (weight) of this feature.
     *
     * <p>Prefer {@link #getValue()} and {@link #setValue(double)} when
     * writing code that may later be ported to an interface-based
     * abstraction of this class.
     */
    public double value;

    /**
     * Creates a feature with the given index and value, validating the index.
     *
     * <p>This is equivalent to {@code new Feature(index, value, true)}.
     *
     * @param index the 1-based feature index; must be strictly positive
     * @param value the feature value
     * @throws IllegalArgumentException if {@code index <= 0}
     */
    public Feature(int index, double value) {
        this(index, value, true);
    }

    /**
     * Creates a feature with the given index and value, optionally skipping
     * index validation.
     *
     * <p>When {@code validateIndex} is {@code true}, the constructor rejects
     * non-positive indices. When it is {@code false}, the check is skipped.
     * The unchecked path exists for performance-sensitive internal code that
     * is known to produce valid indices; in that case the caller is
     * responsible for guaranteeing {@code index > 0}. Passing an invalid
     * index with {@code validateIndex == false} produces an instance whose
     * behavior is undefined in downstream consumers that assume valid
     * indices, and such an instance should never escape the internal code
     * that created it.
     *
     * @param index         the 1-based feature index
     * @param value         the feature value
     * @param validateIndex whether to validate that {@code index} is
     *                      strictly positive
     * @throws IllegalArgumentException if {@code validateIndex} is
     *                                  {@code true} and {@code index <= 0}
     */
    public Feature(int index, double value, boolean validateIndex) {
        if (validateIndex && index <= 0) {
            throw new IllegalArgumentException("index must be > 0");
        }
        this.index = index;
        this.value = value;
    }

    /**
     * Returns the 1-based index of this feature.
     *
     * <p>Feature indices of sparse vectors conventionally start from 1. A
     * valid instance always has an index greater than zero.
     *
     * @return the feature index
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Returns the value of this feature.
     *
     * @return the feature value
     */
    public double getValue() {
        return this.value;
    }

    /**
     * Sets the value of this feature.
     *
     * <p>Note that {@link #equals(Object)} and {@link #hashCode()} depend on
     * the value. Do not modify the value of an instance that is currently
     * stored in a hash-based collection.
     *
     * @param value the new feature value
     */
    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Feature feature)) return false;
        return index == feature.index
                && Double.compare(value, feature.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, value);
    }

    @Override
    public String toString() {
        return "Feature(index=" + index +
                ", value=" + value +
                ')';
    }
}

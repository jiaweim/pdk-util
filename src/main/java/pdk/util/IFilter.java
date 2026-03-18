package pdk.util;

import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * Filter interface.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Jun 2024, 10:44 AM
 */
public interface IFilter<T> extends Predicate<T> {

    /**
     * Return true if given object pass the filter, false otherwise.
     *
     * @param filterable any object to be filtered.
     * @return true if given object pass the filter.
     */
    boolean test(T filterable);

    default IFilter<T> and(IFilter<? super T> other) {
        requireNonNull(other);
        return (t) -> test(t) && other.test(t);
    }

    default IFilter<T> or(IFilter<? super T> other) {
        requireNonNull(other);
        return (t) -> test(t) || other.test(t);
    }

    default IFilter<T> negate() {
        return (t) -> !test(t);
    }
}

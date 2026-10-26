package pdk.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Filter delegate to a list of {@link IFilter}
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Jun 2024, 10:51 AM
 */
public class ChainFilter<T> implements IFilter<T> {
    /**
     * Relationship between elements in the filters
     */
    public enum Mode {
        /**
         * AND means only pass all filters return true
         */
        AND,
        /**
         * OR means pass any filter return true
         */
        OR
    }

    private final ArrayList<IFilter<T>> filters_;
    private final Mode mode_;

    /**
     * Constructor
     *
     * @param filterList list of {@link IFilter}
     * @param mode       {@link Mode} to define relationship between filters
     */
    public ChainFilter(List<IFilter<T>> filterList, Mode mode) {
        requireNonNull(filterList);

        this.filters_ = new ArrayList<>(filterList);
        this.mode_ = mode;
    }

    /**
     * Construct with default {@link Mode#AND}
     *
     * @param filterList list of {@link IFilter}
     */
    public ChainFilter(List<IFilter<T>> filterList) {
        this(filterList, Mode.AND);
    }

    /**
     * Construct with initial empty filters
     *
     * @param mode {@link Mode}
     */
    public ChainFilter(Mode mode) {
        this.mode_ = mode;
        this.filters_ = new ArrayList<>();
    }

    /**
     * Construct with no {@link IFilter} and {@link Mode#AND}
     */
    public ChainFilter() {
        this(Mode.AND);
    }

    /**
     * add a new Filter to this
     *
     * @param filter a {@link IFilter}
     */
    public ChainFilter<T> add(IFilter<T> filter) {
        this.filters_.add(filter);
        return this;
    }

    public ChainFilter<T> add(ChainFilter<T> filter) {
        this.filters_.addAll(filter.filters_);
        return this;
    }

    /**
     * Add all filters to this {@link ChainFilter}
     *
     * @param filters a list of {@link IFilter}
     * @return this
     */
    public ChainFilter<T> addAll(List<IFilter<T>> filters) {
        this.filters_.addAll(filters);
        return this;
    }

    /**
     * @return number of child filters in it.
     */
    public int size() {
        return this.filters_.size();
    }

    /**
     * @return an unmodifiable filters in it.
     */
    public List<IFilter<T>> getFilterList() {
        return Collections.unmodifiableList(filters_);
    }


    /**
     * @return true if this delegate filter contains no filter.
     */
    public boolean isEmpty() {
        return filters_.isEmpty();
    }

    @Override
    public boolean test(T filterable) {
        return switch (mode_) {
            case AND -> testAnd(filterable);
            case OR -> testOr(filterable);
        };
    }

    /**
     * evaluate all filters with AND operator.
     *
     * @param filterable instance to filter
     * @return boolean with the result of the operation (Filter1(entry) AND
     * Filter2(entry) AND ...).
     */
    private boolean testAnd(T filterable) {
        for (IFilter<T> filter : filters_) {
            if (!filter.test(filterable)) {
                return false;
            }
        }
        return true;
    }

    private boolean testOr(T filterable) {
        for (IFilter<T> filter : filters_) {
            if (filter.test(filterable)) {
                return true;
            }
        }
        return false;
    }
}

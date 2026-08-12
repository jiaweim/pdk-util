package pdk.util.collect;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;


/**
 * A generic visitor interface for performing operations on each element
 * during a traversal or iteration.
 * <p>
 * Implementations define the action to be taken when visiting each element.
 * This pattern decouples the traversal logic from the processing logic,
 * allowing the same traversal to be reused for different purposes.
 * </p>
 * <p>
 * The package also provides two simple built‑in collectors:
 * <ul>
 *   <li>{@link SetVisitor} – collects visited elements into a {@link MutableSet}</li>
 *   <li>{@link ListVisitor} – collects visited elements into a {@link MutableList}</li>
 * </ul>
 * </p>
 *
 * @param <E> the type of elements that can be visited
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 Apr 2025
 */
public interface Visitor<E> {

    /**
     * A {@link Visitor} that collects each visited element into a
     * {@link MutableSet}, ignoring duplicate additions.
     *
     * @param <E> the type of elements to collect
     */
    class SetVisitor<E> implements Visitor<E> {

        private final MutableSet<E> set = Sets.mutable.empty();

        /**
         * Creates an empty {@code SetVisitor}.
         */
        public SetVisitor() {}

        /**
         * Adds the element to the underlying set.
         *
         * @param element the element to collect
         */
        @Override
        public void visit(E element) {
            set.add(element);
        }

        /**
         * Returns the mutable set containing all visited elements.
         *
         * @return the collected elements, never {@code null}
         */
        public MutableSet<E> getSet() {
            return set;
        }
    }

    /**
     * A {@link Visitor} that collects each visited element into a
     * {@link MutableList}, preserving traversal order.
     *
     * @param <E> the type of elements to collect
     */
    class ListVisitor<E> implements Visitor<E> {

        private final MutableList<E> list = Lists.mutable.empty();

        /**
         * Creates an empty {@code ListVisitor}.
         */
        public ListVisitor() {}

        /**
         * Appends the element to the underlying list.
         *
         * @param element the element to collect
         */
        @Override
        public void visit(E element) {
            list.add(element);
        }

        /**
         * Returns the mutable list containing all visited elements
         * in the order they were visited.
         *
         * @return the collected elements, never {@code null}
         */
        public MutableList<E> getList() {
            return list;
        }
    }

    /**
     * Creates a new {@link ListVisitor} that collects visited elements
     * into a {@link MutableList}.
     *
     * @param <E> the type of elements to collect
     * @return a new {@code ListVisitor}, never {@code null}
     */
    static <E> ListVisitor<E> toList() {
        return new ListVisitor<>();
    }

    /**
     * Creates a new {@link SetVisitor} that collects visited elements
     * into a {@link MutableSet}, ignoring duplicates.
     *
     * @param <E> the type of elements to collect
     * @return a new {@code SetVisitor}, never {@code null}
     */
    static <E> SetVisitor<E> toSet() {
        return new SetVisitor<>();
    }

    /**
     * Visits the given element.
     * <p>
     * Called once for each element during traversal. Implementations
     * should define the action to perform (e.g., collect, print, modify).
     * </p>
     *
     * @param element the element to visit
     */
    void visit(E element);
}

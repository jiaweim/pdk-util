package pdk.util;

/**
 * Interface for object supporting copy.
 *
 * @param <T> type to be copy
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Jul 2025, 10:02 AM
 */
public interface Copyable<T> {

    /**
     * Creates and returns a copy of this object. The precise meaning of "copy"
     * may depend on the class of the object. The general intent is that, for
     * any object {@code x}, the expression:
     * <blockquote>
     * <pre>
     * x.copy() != x
     *  </pre>
     * </blockquote>
     * will be true, and that the expression:
     * <blockquote>
     * <pre>
     * x.copy().getClass() == x.getClass()
     * </pre>
     * </blockquote>
     * will be {@code true}, but these are not absolute
     * requirements. While it is typically the case that: <blockquote>
     * <pre>
     * x.copy().equals(x)
     * </pre>
     * </blockquote> will be {@code true}, this is not an absolute requirement.
     *
     * @return a copy of this instance.
     */
    T copy();
}

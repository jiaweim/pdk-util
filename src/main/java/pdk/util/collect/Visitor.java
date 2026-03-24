package pdk.util.collect;

/**
 * Visitor interface
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 Apr 2025, 1:42 PM
 */
public interface Visitor<E> {

    /**
     * Access a specific element
     */
    void visit(E element);
}

package pdk.util.collect;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 Apr 2025, 2:16 PM
 */
public interface IntVisitor extends Visitor<Integer> {

    void visit(int value);

    @Override
    default void visit(Integer element) {
        visit(element.intValue());
    }
}

package pdk.util.collect;

/**
 * double visitor interface
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Apr 2025, 12:48 PM
 */
public interface DoubleVisitor extends Visitor<Double> {

    /**
     * visit a double value
     */
    void visit(double value);

    @Override
    default void visit(Double element) {
        visit(element.doubleValue());
    }
}

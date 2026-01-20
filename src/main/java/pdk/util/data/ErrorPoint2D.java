package pdk.util.data;

/**
 * A two-dimensional point with an additional error attribute
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 20 Jan 2026, 5:02 PM
 */
public class ErrorPoint2D extends Point2D {

    private double error;

    /**
     * Create an {@link ErrorPoint2D} with given value
     *
     * @param x x value
     * @param y y value
     */
    public ErrorPoint2D(double x, double y) {
        super(x, y);
    }

    /**
     * Create an {@link ErrorPoint2D}
     *
     * @param x     x value
     * @param y     y value
     * @param error error
     */
    public ErrorPoint2D(double x, double y, double error) {
        super(x, y);
        this.error = error;
    }

    /**
     * Return the error
     *
     * @return error
     */
    public double getError() {
        return error;
    }

    /**
     * set the error
     *
     * @param error error value
     */
    public void setError(double error) {
        this.error = error;
    }

    @Override
    public ErrorPoint2D copy() {
        return new ErrorPoint2D(x, y, error);
    }
}

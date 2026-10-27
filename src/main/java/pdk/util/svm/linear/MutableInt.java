package pdk.util.svm.linear;

/**
 * Mutable holder for an {@code int} value.
 * <p>
 * Used where an immutable {@link Integer} cannot be updated in place,
 * e.g. as a value of a {@link java.util.Map} that counts occurrences.
 * <p>
 * This class is <em>not</em> thread-safe and deliberately does not override
 * {@code equals}/{@code hashCode}, since mutable objects must not be used as
 * map keys.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 21 Oct 2026, 5:43 PM
 */
final class MutableInt {

    private int value;

    public MutableInt(int value) {
        set(value);
    }

    void set(int value) {
        this.value = value;
    }

    int get() {
        return value;
    }

    /**
     * Adds {@code delta} to the current value.
     */
    void add(int delta) {
        this.value += delta;
    }

    /**
     * Increments the current value by one.
     */
    void increment() {
        this.value++;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}

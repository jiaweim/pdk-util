package pdk.util.svm.linear;

/**
 * A simple wrapper for int array
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Oct 2025, 4:52 PM
 */
final class IntArrayPointer {

    private final int[] _array;
    private int _offset;

    void setOffset(int offset) {
        if (offset < 0 || offset >= _array.length)
            throw new IllegalArgumentException("offset must be between 0 and the length of the array");
        _offset = offset;
    }

    /**
     * Constructor
     *
     * @param array  array to wrap
     * @param offset offset
     */
    IntArrayPointer(final int[] array, final int offset) {
        _array = array;
        setOffset(offset);
    }

    /**
     * Return the element at {@code index+offset}
     *
     * @param index index
     * @return value
     */
    public int get(final int index) {
        return _array[_offset + index];
    }

    public void set(final int index, final int value) {
        _array[_offset + index] = value;
    }
}

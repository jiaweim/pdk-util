package pdk.util;

import java.util.Comparator;

/**
 * Class for argument check.
 *
 * @author Jiawei Mao
 * @version 1.1.0
 * @since 01 Jul 2024, 5:20 PM
 */
public final class ArgUtils {

    private ArgUtils() {}

    /**
     * Ensures that {@code start} and {@code end} specify a valid <i>positions</i> in an array, list
     * or string of size {@code size}, and are in order. A position index may range from zero to
     * {@code size}, inclusive.
     *
     * @param start an index identifying a starting position in an array, list or string
     * @param end   an index identifying a ending position in an array, list or string
     * @param size  the size of that array, list or string
     * @throws IndexOutOfBoundsException if either index is negative or is greater than {@code size},
     *                                   or if {@code end} is less than {@code start}
     * @throws IllegalArgumentException  if {@code size} is negative
     */
    public static void checkPositionIndexes(int start, int end, int size) {
        if (start < 0 || end < start || end > size) {
            if (start < 0 || start > size) {
                throw new IndexOutOfBoundsException(badPositionIndex(start, size, "start index"));
            }
            if (end < 0 || end > size) {
                throw new IndexOutOfBoundsException(badPositionIndex(end, size, "end index"));
            }
            // end < start
            throw new IndexOutOfBoundsException(String.format("end index (%s) must not be less than start index (%s)", end, start));
        }
    }

    /**
     * Ensures that {@code index} specifies a valid <i>position</i> in an array, list or string of
     * size {@code size}. A position index may range from [0,size].
     *
     * @param index a user-supplied index identifying a position in an array, list or string
     * @param size  the size of that array, list or string
     * @return the value of {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is negative or is greater than {@code size}
     * @throws IllegalArgumentException  if {@code size} is negative
     */
    public static int checkElementPosition(int index, int size) {
        return checkElementPosition(index, size, "index");
    }

    /**
     * Ensures that {@code index} specifies a valid <i>position</i> in an array, list or string of
     * size {@code size}. A position index may range from zero to {@code size}, inclusive.
     *
     * @param index a user-supplied index identifying a position in an array, list or string
     * @param size  the size of that array, list or string
     * @param desc  the text to use to describe this index in an error message
     * @return the value of {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is negative or is greater than {@code size}
     * @throws IllegalArgumentException  if {@code size} is negative
     */
    public static int checkElementPosition(int index, int size, String desc) {
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(badPositionIndex(index, size, desc));
        }
        return index;
    }

    private static String badPositionIndex(int index, int size, String desc) {
        if (index < 0) {
            return String.format("%s (%s) must not be negative", desc, index);
        } else if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        } else { // index > size
            return String.format("%s (%s) must not be greater than size (%s)", desc, index, size);
        }
    }

    /**
     * Ensures that {@code index} specifies a valid <i>element</i> in an array, list or string of size
     * {@code size}. An element index should be in range <code>[0,size)</code>.
     *
     * @param index an index identifying an element of an array, list or string
     * @param size  the size of that array, list or string
     * @return the value of {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is negative or is not less than {@code size}
     * @throws IllegalArgumentException  if {@code size} is negative
     */
    public static int checkElementIndex(int index, int size) {
        return checkElementIndex(index, size, "index");
    }

    /**
     * Ensures that {@code index} specifies a valid <i>element</i> in an array, list or string of size
     * {@code size}. An element index may range from zero, inclusive, to {@code size}, exclusive.
     *
     * @param index a user-supplied index identifying an element of an array, list or string
     * @param size  the size of that array, list or string
     * @param desc  the text to use to describe this index in an error message
     * @return the value of {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is negative or is not less than {@code size}
     * @throws IllegalArgumentException  if {@code size} is negative
     */
    public static int checkElementIndex(int index, int size, String desc) {
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(badElementIndex(index, size, desc));
        }
        return index;
    }

    private static String badElementIndex(int index, int size, String desc) {
        if (index < 0) {
            return String.format("%s (%s) must not be negative", desc, index);
        } else if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        } else { // index >= size
            return String.format("%s (%s) must be less than size (%s)", desc, index, size);
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression a boolean expression
     * @throws IllegalArgumentException if {@code expression} is false
     */
    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param b            a boolean expression
     * @param errorMessage error message if test failed
     */
    public static void checkArgument(boolean b, String errorMessage) {
        if (!b) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param b                    a boolean expression
     * @param errorMessageTemplate error message template
     * @param p1                   parameter value
     */
    public static void checkArgument(boolean b, String errorMessageTemplate, int p1) {
        if (!b) {
            throw new IllegalArgumentException(String.format(errorMessageTemplate, p1));
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param b                    a boolean expression
     * @param errorMessageTemplate error message template
     * @param p1                   parameter value
     */
    public static void checkArgument(boolean b, String errorMessageTemplate, long p1) {
        if (!b) {
            throw new IllegalArgumentException(String.format(errorMessageTemplate, p1));
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param b                    expression return boolean
     * @param errorMessageTemplate error message template
     * @param p1                   a parameter
     * @param p2                   a paramter
     */
    public static void checkArgument(boolean b, String errorMessageTemplate, long p1, int p2) {
        if (!b) {
            throw new IllegalArgumentException(String.format(errorMessageTemplate, p1, p2));
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param b                    boolean expression
     * @param errorMessageTemplate error message template used in {@link String#format(String, Object...)}
     * @param p1                   a long for {@link String#format(String, Object...)}
     * @param p2                   the second long for {@link String#format(String, Object...)}
     */
    public static void checkArgument(boolean b, String errorMessageTemplate, long p1, long p2) {
        if (!b) {
            throw new IllegalArgumentException(String.format(errorMessageTemplate, p1, p2));
        }
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param b                    boolean expression
     * @param errorMessageTemplate error message template used in {@link String#format(String, Object...)}
     * @param p1                   a long for {@link String#format(String, Object...)}
     * @param p2                   the second argument for {@link String#format(String, Object...)}
     */
    public static void checkArgument(boolean b, String errorMessageTemplate, long p1, Object p2) {
        if (!b) {
            throw new IllegalArgumentException(String.format(errorMessageTemplate, p1, p2));
        }
    }

    /**
     * Returns the first of two given parameters that is not {@code null}, if either is, or otherwise
     * throws a {@link NullPointerException}.
     *
     * @param first  the first argument
     * @param second the second argument
     * @param <T>    type of the argument
     * @return {@code first} if it is non-null; otherwise {@code second} if it is non-null
     * @throws NullPointerException if both {@code first} and {@code second} are null
     */
    public static <T> T firstNonNull(T first, T second) {
        if (first != null) {
            return first;
        }
        if (second != null) {
            return second;
        }
        throw new NullPointerException("Both parameters are null");
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @param <T>       type of the argument
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference    an object reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *                     string using {@link String#valueOf(Object)}
     * @param <T>          type of the argument
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    /**
     * Make use the {@code value} is nonnegative
     *
     * @param value a long value
     * @param role  role of the value
     * @return the value
     */
    public static long checkNonNegative(long value, String role) {
        if (value < 0) {
            throw new IllegalArgumentException(role + " (" + value + ") must be >= 0");
        }
        return value;
    }

    /**
     * Make use the {@code value} is nonnegative
     *
     * @param value value to check
     * @param role  name of the value
     * @return the value
     */
    public static int checkNonNegative(int value, String role) {
        if (value < 0) {
            throw new IllegalArgumentException(role + " (" + value + ") must be >= 0");
        }
        return value;
    }

    /**
     * Make use the {@code value} is nonnegative
     *
     * @param x    value to check
     * @param role name of the value
     * @return the value
     */
    public static double checkNonNegative(double x, String role) {
        if (!(x >= 0)) { // not x < 0, to work with NaN.
            throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
        }
        return x;
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     * @param expression a boolean expression
     * @throws IllegalStateException if {@code expression} is false
     */
    public static void checkState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not
     * involving any parameters to the calling method.
     *
     * @param expression   a boolean expression
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *                     string using {@link String#valueOf(Object)}
     * @throws IllegalStateException if {@code expression} is false
     */
    public static void checkState(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }

    /**
     * Test whether the array is in sorted according to the given {@link Comparator}, if the array
     * does not sorted, throw {@link AssertionError}.
     *
     * @param array      double array
     * @param comparator {@link Comparator} to use
     * @since 2026-01-05
     */
    public static boolean isSorted(double[] array, Comparator<Double> comparator) {
        if (array.length <= 1)
            return true;
        for (int i = 0; i < array.length - 1; i++) {
            int result = comparator.compare(array[i], array[i + 1]);
            if (result > 0) {
                return false;
            }
        }
        return true;
    }
}

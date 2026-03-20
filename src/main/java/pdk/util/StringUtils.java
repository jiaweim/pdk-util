package pdk.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.StringJoiner;

import static java.util.Objects.requireNonNull;

/**
 * String utilities.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 18 Mar 2026, 1:58 PM
 */
public final class StringUtils {

    private StringUtils() {}

    /**
     * The empty String {@code ""}.
     */
    public static final String EMPTY = "";

    public static final String COMMA = ",";
    /**
     * commons separator
     */
    public static final String SEMI_COLON = ";";

    /**
     * Tests if a CharSequence is empty ("") or null.
     *
     * @param cs the CharSequence to check, may be null.
     * @return {@code true} if the CharSequence is empty or null.
     * @since 2026-03-18⭐
     */
    public static boolean isEmpty(@Nullable final CharSequence cs) {
        return cs == null || cs.isEmpty();
    }

    /**
     * Checks if a CharSequence is not empty ("") and not null.
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is not empty and not null
     * @since 2026-03-18⭐
     */
    public static boolean isNotEmpty(@Nullable final CharSequence cs) {
        return !isEmpty(cs);
    }

    /**
     * Replaces all occurrences of a character in a String with another. This is a null-safe version of {@link String#replace(char, char)}.
     *
     * <p>
     * A {@code null} string input returns {@code null}. An empty ("") string input returns an empty string.
     * </p>
     *
     * @param str         String to replace characters in, may be null.
     * @param searchChar  the character to search for, may be null.
     * @param replaceChar the character to replace, may be null.
     * @return modified String, {@code null} if null string input.
     * @since 2026-03-18⭐
     */
    public static String replaceChars(@Nullable final String str, final char searchChar, final char replaceChar) {
        if (str == null) {
            return null;
        }
        return str.replace(searchChar, replaceChar);
    }

    /**
     * Reverses a String as per {@link StringBuilder#reverse()}.
     *
     * <p>
     * A {@code null} String returns {@code null}.
     * </p>
     *
     * @param str the String to reverse, may be null.
     * @return the reversed String, {@code null} if null String input.
     * @since 2026-03-18⭐
     */
    public static String reverse(@Nullable final String str) {
        if (str == null) {
            return null;
        }
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * Deletes all whitespaces from a String as defined by {@link Character#isWhitespace(char)}.
     *
     * @param str the String to delete whitespace from, may be null.
     * @return the String without whitespaces, {@code null} if null String input.
     * @since 2026-03-19⭐
     */
    public static String deleteWhitespace(final String str) {
        if (isEmpty(str)) {
            return str;
        }
        final int sz = str.length();
        final char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) {
            return str;
        }
        if (count == 0) {
            return EMPTY;
        }
        return new String(chs, 0, count);
    }

    /**
     * <p>Counts how many times the substring appears in the larger string.</p>
     *
     * <p>A {@code null} or empty ("") String input returns {@code 0}.</p>
     *
     * @param str the String to check, may be null
     * @param sub the substring to count, may be null
     * @return the number of occurrences, 0 if either String is {@code null}
     */
    public static int countMatches(final String str, final String sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }

    /**
     * <p>Counts how many times the char appears in the given string.</p>
     *
     * <p>A {@code null} or empty ("") String input returns {@code 0}.</p>
     *
     * <pre>
     * StringUtils.countMatches(null, *)       = 0
     * StringUtils.countMatches("", *)         = 0
     * StringUtils.countMatches("abba", 0)  = 0
     * StringUtils.countMatches("abba", 'a')   = 2
     * StringUtils.countMatches("abba", 'b')  = 2
     * StringUtils.countMatches("abba", 'x') = 0
     * </pre>
     *
     * @param str the CharSequence to check, may be null
     * @param ch  the char to count
     * @return the number of occurrences, 0 if the CharSequence is {@code null}
     */
    public static int countMatches(final String str, final char ch) {
        if (isEmpty(str)) {
            return 0;
        }
        int count = 0;
        // We could also call str.toCharArray() for faster look ups but that would generate more garbage.
        for (int i = 0; i < str.length(); i++) {
            if (ch == str.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Return all the index of <code>shortSeq</code> in <code>seq</code>
     *
     * @param seq      a string sequence
     * @param shortSeq a short string to be searched
     * @return all the indexes of <code>shortSeq</code> in <code>seq</code>, or an empty list if it is not contained
     * @since 2024-12-14 ⭐
     */
    public static IntArrayList indexOf(final String seq, final String shortSeq) {
        requireNonNull(seq);
        requireNonNull(shortSeq);

        // at most seq.length()-shortSeq.length()+1 match
        IntArrayList list = new IntArrayList(seq.length() - shortSeq.length() + 1);
        int index = 0;
        while ((index = seq.indexOf(shortSeq, index)) != -1) {
            list.add(index);
            index++;
        }
        return list;
    }

    /**
     * Join sequence array with given delimiter, element's toString() method is called
     * in the join method.
     *
     * @param delimiter a sequence of characters that is used to separate each
     *                  of the {@code elements} in the resulting {@code String}
     * @param elements  an {@code Collection} that will have its {@code elements}
     *                  joined together.
     * @return a new {@code String} that is composed from the {@code elements}
     * argument
     */
    public static <T> String join(String delimiter, T[] elements) {
        requireNonNull(delimiter);
        requireNonNull(elements);

        StringJoiner joiner = new StringJoiner(delimiter);
        for (T element : elements) {
            joiner.add(String.valueOf(element));
        }
        return joiner.toString();
    }

    /**
     * join sequence collection with given delimiter, element's toString() method is called
     * in the join method.
     *
     * @param delimiter a sequence of characters that is used to separate each
     *                  of the {@code elements} in the resulting {@code String}
     * @param elements  an {@code Collection} that will have its {@code elements}
     *                  joined together.
     * @return a new {@code String} that is composed from the {@code elements} argument
     */
    public static String join(String delimiter, Collection elements) {
        requireNonNull(delimiter);
        requireNonNull(elements);

        StringJoiner joiner = new StringJoiner(delimiter);
        for (Object element : elements) {
            joiner.add(element.toString());
        }
        return joiner.toString();
    }

    /**
     * join sequence array with given delimiter, element's toString() method is called
     * in the join method.
     *
     * @param delimiter a sequence of characters that is used to separate each
     *                  of the {@code elements} in the resulting {@code String}
     * @param elements  an {@code Collection} that will have its {@code elements}
     *                  joined together.
     * @return a new {@code String} that is composed from the {@code elements}
     * argument
     */
    public static String join(String delimiter, int[] elements) {
        requireNonNull(delimiter);
        requireNonNull(elements);

        StringJoiner joiner = new StringJoiner(delimiter);
        for (int element : elements) {
            joiner.add(String.valueOf(element));
        }
        return joiner.toString();
    }


    /**
     * join sequence array with given delimiter, element's toString() method is called
     * in the join method.
     *
     * @param delimiter a sequence of characters that is used to separate each
     *                  of the {@code elements} in the resulting {@code String}
     * @param elements  an {@code Collection} that will have its {@code elements}
     *                  joined together.
     * @return a new {@code String} that is composed from the {@code elements}
     * argument
     */
    public static String join(String delimiter, double[] elements) {
        requireNonNull(delimiter);
        requireNonNull(elements);

        StringJoiner joiner = new StringJoiner(delimiter);
        for (double element : elements) {
            joiner.add(String.valueOf(element));
        }
        return joiner.toString();
    }

    /**
     * join sequence array with given delimiter, element's toString() method is called
     * in the join method.
     *
     * @param delimiter a sequence of characters that is used to separate each
     *                  of the {@code elements} in the resulting {@code String}
     * @param elements  an {@code Collection} that will have its {@code elements}
     *                  joined together.
     * @return a new {@code String} that is composed from the {@code elements}
     * argument
     */
    public static String join(String delimiter, float[] elements) {
        requireNonNull(delimiter);
        requireNonNull(elements);

        StringJoiner joiner = new StringJoiner(delimiter);
        for (float element : elements) {
            joiner.add(String.valueOf(element));
        }
        return joiner.toString();
    }
}

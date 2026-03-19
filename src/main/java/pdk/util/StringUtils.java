package pdk.util;

import org.jetbrains.annotations.Nullable;

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
}

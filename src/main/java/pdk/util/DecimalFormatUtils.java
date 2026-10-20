package pdk.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static java.util.Objects.requireNonNull;
import static pdk.util.ArgUtils.checkNonNegative;

/**
 * Class to format number values.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 21 Jun 2024, 10:49 AM
 */
public class DecimalFormatUtils {

    private static final DecimalFormatSymbols US = new DecimalFormatSymbols(Locale.US);

    public static final DecimalFormat F0 = valueOf(0);
    public static final DecimalFormat F1 = valueOf(1);
    public static final DecimalFormat F2 = valueOf(2);
    public static final DecimalFormat F3 = valueOf(3);
    public static final DecimalFormat F4 = valueOf(4);
    public static final DecimalFormat F5 = valueOf(5);
    public static final DecimalFormat F6 = valueOf(6);

    /**
     * Returns a new {@link DecimalFormat} instance that formats a fraction
     * (e.g., {@code 0.1234}) as a percentage (e.g., {@code 12.34%}).
     *
     * <p>The returned formatter automatically multiplies the input by 100 and
     * appends a {@code '%'} sign. It uses {@link Locale#US} decimal format
     * symbols, so the decimal separator is {@code '.'} and the percent sign is
     * not preceded by a space. A new instance is returned on every call because
     * {@code DecimalFormat} is not thread-safe and must not be shared.
     *
     * <p>The meaning of {@code maxFractionDigits} depends on
     * {@code optionalFractionDigits}:
     * <ul>
     *   <li>If {@code optionalFractionDigits} is {@code false}, the formatter
     *       displays exactly {@code maxFractionDigits} fraction digits, padding
     *       with trailing zeros when necessary (pattern {@code 0.00%}).
     *       For example, {@code 0.5} becomes {@code "50.00%"} when
     *       {@code maxFractionDigits} is 2.</li>
     *   <li>If {@code optionalFractionDigits} is {@code true}, the formatter
     *       displays at most {@code maxFractionDigits} fraction digits and omits
     *       trailing zeros (pattern {@code 0.##%}). For example, {@code 0.5}
     *       becomes {@code "50%"} and {@code 0.1234} becomes {@code "12.34%"}
     *       when {@code maxFractionDigits} is 2.</li>
     * </ul>
     *
     * <p>When {@code maxFractionDigits} is {@code 0}, both modes produce the
     * same pattern ({@code "0%"}) and {@code optionalFractionDigits} has no
     * effect.
     *
     * <p>The formatter uses the default rounding mode of {@link DecimalFormat},
     * which is {@link RoundingMode#HALF_EVEN} (banker's rounding). Callers that
     * require a different rounding behavior, such as
     * {@link RoundingMode#HALF_UP}, should set it on the returned instance.
     *
     * @param maxFractionDigits the number of fraction digits; must be
     *                          {@code >= 0}. When
     *                          {@code optionalFractionDigits} is
     *                          {@code false}, this is the exact number of
     *                          fraction digits; otherwise it is the maximum
     *                          number of fraction digits.
     * @return a new, non-shared {@code DecimalFormat} instance configured for
     * percentage output
     * @throws IllegalArgumentException if {@code maxFractionDigits} is negative
     * @see DecimalFormat
     */
    public static DecimalFormat getPercent(int maxFractionDigits) {
        return getPercent(maxFractionDigits, false);
    }

    /**
     * Returns a new {@link DecimalFormat} instance that formats a fraction
     * (e.g., {@code 0.1234}) as a percentage (e.g., {@code 12.34%}).
     *
     * <p>The returned formatter automatically multiplies the input by 100 and
     * appends a {@code '%'} sign. It uses {@link Locale#US} decimal format
     * symbols, so the decimal separator is {@code '.'} and the percent sign is
     * not preceded by a space. A new instance is returned on every call because
     * {@code DecimalFormat} is not thread-safe and must not be shared.
     *
     * <p>The meaning of {@code maxFractionDigits} depends on
     * {@code optionalFractionDigits}:
     * <ul>
     *   <li>If {@code optionalFractionDigits} is {@code false}, the formatter
     *       displays exactly {@code maxFractionDigits} fraction digits, padding
     *       with trailing zeros when necessary (pattern {@code 0.00%}).
     *       For example, {@code 0.5} becomes {@code "50.00%"} when
     *       {@code maxFractionDigits} is 2.</li>
     *   <li>If {@code optionalFractionDigits} is {@code true}, the formatter
     *       displays at most {@code maxFractionDigits} fraction digits and omits
     *       trailing zeros (pattern {@code 0.##%}). For example, {@code 0.5}
     *       becomes {@code "50%"} and {@code 0.1234} becomes {@code "12.34%"}
     *       when {@code maxFractionDigits} is 2.</li>
     * </ul>
     *
     * <p>When {@code maxFractionDigits} is {@code 0}, both modes produce the
     * same pattern ({@code "0%"}) and {@code optionalFractionDigits} has no
     * effect.
     *
     * <p>The formatter uses the default rounding mode of {@link DecimalFormat},
     * which is {@link RoundingMode#HALF_EVEN} (banker's rounding). Callers that
     * require a different rounding behavior, such as
     * {@link RoundingMode#HALF_UP}, should set it on the returned instance.
     *
     * @param maxFractionDigits      the number of fraction digits; must be
     *                               {@code >= 0}. When
     *                               {@code optionalFractionDigits} is
     *                               {@code false}, this is the exact number of
     *                               fraction digits; otherwise it is the maximum
     *                               number of fraction digits.
     * @param optionalFractionDigits if {@code true}, trailing zeros in the
     *                               fraction part are omitted; if {@code false},
     *                               the fraction part is padded with zeros to
     *                               exactly {@code maxFractionDigits} digits
     * @return a new, non-shared {@code DecimalFormat} instance configured for
     * percentage output
     * @throws IllegalArgumentException if {@code maxFractionDigits} is negative
     * @see DecimalFormat
     */
    public static DecimalFormat getPercent(int maxFractionDigits, boolean optionalFractionDigits) {
        checkNonNegative(maxFractionDigits, "maxFractionDigits");

        StringBuilder sb = new StringBuilder("0");
        if (maxFractionDigits > 0) {
            sb.append('.');
            if (optionalFractionDigits) {
                sb.repeat("#", maxFractionDigits);
            } else {
                sb.repeat("0", maxFractionDigits);
            }
        }
        sb.append('%');
        return new DecimalFormat(sb.toString(), US);
    }

    /**
     * Create a new instance of DecimalFormat given the maximum fraction digits for
     * scientific notation.
     *
     * @param maxFractionDigits the maximum number of fractional digits.
     * @return instance of DecimalFormat.
     */
    public static DecimalFormat getScientific(int maxFractionDigits) {
        checkNonNegative(maxFractionDigits, "fraction digit");
        return valueOf(1, true, maxFractionDigits, true, true);
    }

    /**
     * Make a new instance of DecimalFormat with given number of fraction digits
     *
     * @param maxFractionDigits the maximum number of fractional digits.
     * @return instance of DecimalFormat.
     */
    public static DecimalFormat valueOf(int maxFractionDigits) {
        return valueOf(1, maxFractionDigits);
    }

    /**
     * Make a new instance of DecimalFormat given the maximum integer and
     * fraction digits.
     *
     * @param maxIntegerDigits  the maximum number of integer digits.
     * @param maxFractionDigits the maximum number of fractional digits.
     * @return instance of DecimalFormat.
     */
    public static DecimalFormat valueOf(int maxIntegerDigits, int maxFractionDigits) {
        checkNonNegative(maxFractionDigits, "max fraction digit");
        checkNonNegative(maxIntegerDigits, "max integer digit");

        return valueOf(maxIntegerDigits, false, maxFractionDigits, false, false);
    }

    /**
     * Make a new instance of DecimalFormat.
     *
     * @param maxInteger        maximum number of integer digits
     * @param optionalInteger   true if integer part is optional, if false, when the integer is insufficient, it will
     *                          be filled with 0 in front
     * @param maxFractionDigits maximum number of fractional digits
     * @param optionalFraction  true if fractional digit is optional
     * @param scientific        true if use scientific notation
     * @return {@link DecimalFormat} instance
     */
    public static DecimalFormat valueOf(int maxInteger, boolean optionalInteger,
            int maxFractionDigits, boolean optionalFraction, boolean scientific) {

        checkNonNegative(maxInteger, "max integer digit");
        checkNonNegative(maxFractionDigits, "max fraction digit");

        return valueOf(createDecimalPattern(maxInteger, maxFractionDigits, optionalInteger, optionalFraction, scientific));
    }

    /**
     * Create {@link DecimalFormat} from a given pattern
     *
     * @param pattern pattern string
     * @return {@link DecimalFormat} instance
     */
    public static DecimalFormat valueOf(String pattern) {
        requireNonNull(pattern);
        return new DecimalFormat(pattern, US);
    }

    private static String createDecimalPattern(int maxIntegerDigits, int maxFractionDigits,
            boolean optionalInteger, boolean optionalFraction, boolean sci) {
        StringBuilder sb = new StringBuilder();
        if (optionalInteger) {
            sb.repeat("#", Math.max(0, maxIntegerDigits));
        } else {
            sb.repeat("0", Math.max(0, maxIntegerDigits));
        }
        if (maxFractionDigits > 0)
            sb.append(".");

        if (optionalFraction) {
            sb.repeat("#", maxFractionDigits);
        } else {
            sb.repeat("0", maxFractionDigits);
        }

        if (sci) {
            sb.append("E0");
        }

        return sb.toString();
    }
}

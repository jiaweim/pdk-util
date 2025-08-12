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
     * Create a new instance of DecimalFormat given the maximum fraction digits for
     * scientific notation.
     *
     * @param maxFractionDigits the maximum number of fractional digits.
     * @return instance of DecimalFormat.
     */
    public static DecimalFormat scientific(int maxFractionDigits) {
        checkNonNegative("fraction digit", maxFractionDigits);

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
        checkNonNegative("max fraction digit", maxFractionDigits);
        checkNonNegative("max integer digit", maxIntegerDigits);

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

        checkNonNegative("max integer digit", maxInteger);
        checkNonNegative("max fraction digit", maxFractionDigits);

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

    private static String createPercentagePattern(int maxFractionDigits, boolean optionalFraction) {
        StringBuilder sb = new StringBuilder();
        sb.append("0");
        if (maxFractionDigits > 0) {
            sb.append(".");
        }
        if (optionalFraction) {
            sb.append("#".repeat(maxFractionDigits));
        } else {
            sb.append("0".repeat(maxFractionDigits));
        }
        return sb.toString();
    }

    private static String createDecimalPattern(int maxIntegerDigits, int maxFractionDigits,
            boolean optionalInteger, boolean optionalFraction, boolean sci) {
        StringBuilder sb = new StringBuilder();
        if (optionalInteger) {
            sb.append("#".repeat(Math.max(0, maxIntegerDigits)));
        } else {
            sb.append("0".repeat(Math.max(0, maxIntegerDigits)));
        }
        if (maxFractionDigits > 0)
            sb.append(".");

        if (optionalFraction) {
            sb.append("#".repeat(maxFractionDigits));
        } else {
            sb.append("0".repeat(maxFractionDigits));
        }

        if (sci) {
            sb.append("E0");
        }

        return sb.toString();
    }
}

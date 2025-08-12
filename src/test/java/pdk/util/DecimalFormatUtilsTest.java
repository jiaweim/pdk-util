package pdk.util;

import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 12 Aug 2025, 9:43 AM
 */
class DecimalFormatUtilsTest {

    @Test
    void testScientific() {
        DecimalFormat format = DecimalFormatUtils.scientific(2);

        assertEquals("4.15E-2", format.format(0.04151515));
        assertEquals("4.16E-2", format.format(0.04155515));
        assertEquals("9.85E6", format.format(9852522.554666));

        assertEquals("1.23E0", format.format(1.23));
        assertEquals("1.23E1", format.format(12.3456));
        assertEquals("1.23E-1", format.format(0.123));
        assertEquals("1E-1", format.format(0.1));
    }

    @Test
    void testValueOf() {
        DecimalFormat decimalFormat = DecimalFormatUtils.valueOf("##0.###");
        assertEquals("12.3", decimalFormat.format(12.3));
        assertEquals("0.123", decimalFormat.format(0.123));
        assertEquals("0.123", decimalFormat.format(0.1234));
        assertEquals("123456.123", decimalFormat.format(123456.1234));
    }

    @Test
    void testShort() {
        DecimalFormat format = DecimalFormatUtils.valueOf(1, true, 2, true, true);
        assertEquals("1.23E0", format.format(1.23));
        assertEquals("1.23E1", format.format(12.3456));
        assertEquals("1.23E-1", format.format(0.123));
        assertEquals("1E-1", format.format(0.1));

        DecimalFormat decimalFormat = DecimalFormatUtils.valueOf("##0.###");

        assertEquals("12.3", decimalFormat.format(12.3));
        assertEquals("0.123", decimalFormat.format(0.123));
        assertEquals("0.123", decimalFormat.format(0.1234));
        assertEquals("123456.123", decimalFormat.format(123456.1234));
    }

    @Test
    void testNewLocaleInstance() {
        NumberFormat df = DecimalFormat.getInstance(Locale.FRANCE);
        assertEquals("1,329", df.format(1.328984366538442783));
    }

    @Test
    void test() {
        DecimalFormat format = new DecimalFormat("$###,###.###");
        assertEquals("$12,345.67", format.format(12345.67));
    }


    @Test
    void testNewLocaleInstanceWithPattern() {
        DecimalFormatSymbols frSymbols = new DecimalFormatSymbols(Locale.FRANCE);

        DecimalFormat df = new DecimalFormat("0.0000", frSymbols);
        assertEquals("1,3290", df.format(1.328984366538442783));
    }

    @Test
    void testDecimalFormatFactory() {
        assertEquals("1.33", DecimalFormatUtils.F2.format(1.328984366538442783));
    }

    @Test
    void testDecimalFormatFactoryWithPrecision() {
        NumberFormat df = DecimalFormatUtils.valueOf(4);

        assertEquals("1.3290", df.format(1.328984366538442783));
    }

    @Test
    void testDecimalFractionalPart() {
        NumberFormat df = DecimalFormatUtils.valueOf(3);
        assertEquals("0.034", df.format(0.0342345678));
    }

    @Test
    void testDecimalIntegerPart() {
        NumberFormat df = DecimalFormatUtils.valueOf(4, 0);

        assertEquals("1329", df.format(1328.984366538442783));
    }

    @Test
    void testDecimalIntegerAndFractionalPart() {
        NumberFormat df = DecimalFormatUtils.valueOf(4, 4);

        assertEquals("1328.9844", df.format(1328.984366538442783));
    }

    @Test
    void testDecimal() {
        NumberFormat dec = DecimalFormatUtils.valueOf(2);
        System.out.println(dec.format(0.001));
        assertEquals("0.00", dec.format(0.001));
    }

    @Test
    void testInteger() {
        NumberFormat nf = DecimalFormatUtils.F0;
        assertEquals("13", nf.format(12.984366538442783));
    }

    @Test
    void testInteger2() {
        NumberFormat nf = DecimalFormatUtils.valueOf(1, 0);
        assertEquals("13", nf.format(12.984366538442783));
    }

    @Test
    void testInteger3() {
        NumberFormat nf = DecimalFormatUtils.valueOf(4, 0);
        assertEquals("0013", nf.format(12.984366538442783));
    }

    @Test
    void testInteger4() {
        NumberFormat nf = DecimalFormatUtils.F0;
        assertEquals("12984367", nf.format(12984366.538442783));
    }
}
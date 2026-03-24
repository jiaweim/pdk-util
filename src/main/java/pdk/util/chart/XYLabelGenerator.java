package pdk.util.chart;

import org.jfree.chart.labels.AbstractXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.data.xy.XYDataset;

import java.text.NumberFormat;

/**
 * Generate item label for a point.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Mar 2026, 2:35 PM
 */
public class XYLabelGenerator extends AbstractXYItemLabelGenerator implements XYItemLabelGenerator {

    /**
     * Create a  {@link XYLabelGenerator} which only format the Y value
     *
     * @param numberFormat {@link NumberFormat} instance
     * @return {@link XYLabelGenerator}
     */
    public static XYLabelGenerator formatY(NumberFormat numberFormat) {
        return new XYLabelGenerator("{2}", NumberFormat.getInstance(), numberFormat);
    }

    /**
     * Creates an item label generator using the specified number formatters.
     *
     * @param formatString the item label format string ({@code null}
     *                     not permitted).
     * @param xFormat      the format object for the x values ({@code null}
     *                     not permitted).
     * @param yFormat      the format object for the y values ({@code null}
     *                     not permitted).
     */
    public XYLabelGenerator(String formatString, NumberFormat xFormat, NumberFormat yFormat) {
        super(formatString, xFormat, yFormat);
    }

    @Override
    public String generateLabel(XYDataset dataset, int series, int item) {
        return generateLabelString(dataset, series, item);
    }
}

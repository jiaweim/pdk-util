package pdk.util.chart;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.jupiter.api.Test;
import pdk.util.DecimalFormatUtils;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Mar 2026, 2:46 PM
 */
class XYLabelGeneratorTest {

    @Test
    void testFormatString() {
        String format = MessageFormat.format("{2}", "12", "13", "14");
        assertEquals("14", format);
    }

    @Test
    void formatY() {
        XYSeriesCollection datasetCollection = new XYSeriesCollection();
        XYSeries series = new XYSeries("XYSeries");
        series.add(1.0, 2.0);

        datasetCollection.addSeries(series);
        XYLabelGenerator xyLabelGenerator = XYLabelGenerator.formatY(DecimalFormatUtils.F3);
        String label = xyLabelGenerator.generateLabel(datasetCollection, 0, 0);
        assertEquals("2.000", label);
    }
}
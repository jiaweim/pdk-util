package pdk.util.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pdk.util.io.FileUtils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 09 Apr 2026, 10:47 AM
 */
public class AnnotationDemo1 {

    private static XYSeriesCollection createDataset() throws IOException {
        XYSeriesCollection result = new XYSeriesCollection();

        URL url = AnnotationDemo1.class.getResource("wtageinf.txt");
        BufferedReader in = Files.newBufferedReader(FileUtils.toPath(url));
        in.readLine(); // ignore first line
        in.readLine(); // ignore second line
        in.readLine(); // ignore third line
        String data = in.readLine(); // headings
        XYSeries s3 = new XYSeries("P3", true, false);
        XYSeries s5 = new XYSeries("P5", true, false);
        XYSeries s10 = new XYSeries("P10", true, false);
        XYSeries s25 = new XYSeries("P25", true, false);
        XYSeries s50 = new XYSeries("P50", true, false);
        XYSeries s75 = new XYSeries("P75", true, false);
        XYSeries s90 = new XYSeries("P90", true, false);
        XYSeries s95 = new XYSeries("P95", true, false);
        XYSeries s97 = new XYSeries("P97", true, false);

        data = in.readLine();
        while (data != null) {
            int sex = Integer.parseInt(data.substring(1, 8).trim());
            float age = Float.parseFloat(data.substring(9, 17).trim());
            float p3 = Float.parseFloat(data.substring(69, 86).trim());
            float p5 = Float.parseFloat(data.substring(87, 103).trim());
            float p10 = Float.parseFloat(data.substring(104, 122).trim());
            float p25 = Float.parseFloat(data.substring(123, 140).trim());
            float p50 = Float.parseFloat(data.substring(141, 158).trim());
            float p75 = Float.parseFloat(data.substring(159, 176).trim());
            float p90 = Float.parseFloat(data.substring(177, 193).trim());
            float p95 = Float.parseFloat(data.substring(194, 212).trim());
            float p97 = Float.parseFloat(data.substring(212,
                    data.length()).trim());
            if (sex == 1) {
                s3.add(age, p3);
                s5.add(age, p5);
                s10.add(age, p10);
                s25.add(age, p25);
                s50.add(age, p50);
                s75.add(age, p75);
                s90.add(age, p90);
                s95.add(age, p95);
                s97.add(age, p97);
            }
            data = in.readLine();
        }
        in.close();

        result.addSeries(s3);
        result.addSeries(s5);
        result.addSeries(s10);
        result.addSeries(s25);
        result.addSeries(s50);
        result.addSeries(s75);
        result.addSeries(s90);
        result.addSeries(s95);
        result.addSeries(s97);
        return result;
    }

    static void main() throws IOException {
        Font font = new Font("SansSerif", Font.PLAIN, 9);

        JFreeChart chart = XYChartBuilder.start()
                .addDataset(createDataset(), XYChartType.LINE)
                .xAxisName("Age in Months")
                .yAxisName("kg")
                .addLegend(true)
                .addTooltips(true)
                .addTextAnnotation("3rd", 36.5, 11.76, font, TextAnchor.HALF_ASCENT_LEFT)
                .addTextAnnotation("5th", 36.5, 12.04, font, TextAnchor.HALF_ASCENT_LEFT)
                .addTextAnnotation("10th", 36.5, 12.493, font, TextAnchor.HALF_ASCENT_LEFT)
                .addTextAnnotation("25th", 36.5, 13.313, font, TextAnchor.HALF_ASCENT_LEFT)
                .addTextAnnotation("50th", 36.5, 14.33, font, TextAnchor.HALF_ASCENT_LEFT)
                .addTextAnnotation("75th", 36.5, 15.478, font, TextAnchor.HALF_ASCENT_LEFT)
                .addTextAnnotation("90th", 36.5, 16.642, font, TextAnchor.HALF_ASCENT_LEFT)
                .addTextAnnotation("95th", 36.5, 17.408, font, TextAnchor.HALF_ASCENT_LEFT)
                .addTextAnnotation("97th", 36.5, 17.936, font, TextAnchor.HALF_ASCENT_LEFT)
                .build();
        ChartUtils.showChart(chart, 360, 500);
    }
}

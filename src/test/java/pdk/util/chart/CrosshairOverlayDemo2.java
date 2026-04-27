package pdk.util.chart;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.UIUtils;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import pdk.util.chart.util.CrosshairOverlayBuilder;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Apr 2026, 10:35 AM
 */
public class CrosshairOverlayDemo2 {
    static void main() {

        Data.XYDatasetBuilder datasetBuilder = Data.xyDataset();
        for (int i = 0; i < 4; ++i) {
            XYSeries series = new XYSeries("S" + i);
            for (int x = 0; x < 10; ++x) {
                series.add(x, x + Math.random() * 4.0);
            }
            datasetBuilder.addSeries(series);
        }
        XYDataset dataset = datasetBuilder.build();

        LineChart chart = LineChart.lineChart()
                .title("CrosshairOverlayDemo2")
                .xAxisName("X")
                .yAxisName("Y")
                .dataset(dataset)
                .build();
        CrosshairOverlayBuilder crosshairOverlayBuilder = CrosshairOverlayBuilder.create()
                .addDomainCrosshair(Double.NaN, Color.GRAY, new BasicStroke(0.5f))
                .setDomainLabelVisible(0, true);
        for (int i = 0; i < 4; i++) {
            crosshairOverlayBuilder.addRangeCrosshair(Double.NaN, Color.GRAY, new BasicStroke(0.5F))
                    .setRangeLabelVisible(i, true);
            if (i % 2 != 0) {
                crosshairOverlayBuilder.setRangeLabelAnchor(i, RectangleAnchor.TOP_RIGHT);
            }
        }
        CrosshairOverlay crosshairOverlay = crosshairOverlayBuilder.build();

        JFreeChart jFreeChart = chart.getChart();
        ChartPanel chartPanel = new ChartPanel(jFreeChart);
        chartPanel.addOverlay(crosshairOverlay);
        ChartMouseListener mouseListener = new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent event) {

            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                Rectangle2D dataArea = chartPanel.getScreenDataArea();
                XYPlot xyPlot = jFreeChart.getXYPlot();
                ValueAxis xAxis = xyPlot.getDomainAxis();
                double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, RectangleEdge.BOTTOM);
                crosshairOverlay.getDomainCrosshairs().getFirst().setValue(x);

                for (int i = 0; i < 4; i++) {
                    double y = DatasetUtils.findYValue(xyPlot.getDataset(), i, x);
                    crosshairOverlay.getRangeCrosshairs().get(i).setValue(y);
                }
            }
        };
        chartPanel.addChartMouseListener(mouseListener);

        ApplicationFrame frame = new ApplicationFrame("");
        frame.setContentPane(chartPanel);
        frame.pack();
        UIUtils.centerFrameOnScreen(frame);
        frame.setVisible(true);
    }
}

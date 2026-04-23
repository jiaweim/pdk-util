package pdk.util.chart;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.UIUtils;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * This interface is used to represent any chart.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 13 Apr 2026, 9:12 AM
 */
public interface Chart {

    PDKChartTheme DEFAULT_THEME = new PDKChartTheme("pdk");

    /**
     * The default font for titles.
     */
    Font DEFAULT_TITLE_FONT = new Font("SansSerif", Font.BOLD, 18);


    /**
     * Returns the created JFreeChart.
     *
     * @return {@link JFreeChart} instance
     */
    JFreeChart getChart();

    /**
     * display the chart.
     */
    default void show() {
        ApplicationFrame frame = new ApplicationFrame("");
        ChartPanel chartPanel = new ChartPanel(getChart());
        frame.setContentPane(chartPanel);
        frame.pack();
        UIUtils.centerFrameOnScreen(frame);
        frame.setVisible(true);
    }

    default void addMouseListener(ChartPanel chartPanel, ChartMouseListener mouseListener) {
        ChartMouseListener listener = new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
                int x = event.getTrigger().getX();
                int y = event.getTrigger().getY();

                Point2D p = chartPanel.translateScreenToJava2D(new Point(x, y));
                Plot plot = chartPanel.getChart().getPlot();

            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {

            }
        };


        chartPanel.addChartMouseListener(mouseListener);
    }
}

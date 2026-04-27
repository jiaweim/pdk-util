package pdk.util.chart;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.UIUtils;

import javax.swing.*;
import java.awt.*;

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
        SwingUtilities.invokeLater(() -> {
            ApplicationFrame frame = new ApplicationFrame("");
            ChartPanel chartPanel = new ChartPanel(getChart());
            frame.setContentPane(chartPanel);
            frame.pack();
            UIUtils.centerFrameOnScreen(frame);
            frame.setVisible(true);
        });
    }
}

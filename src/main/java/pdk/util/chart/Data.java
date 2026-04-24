package pdk.util.chart;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jspecify.annotations.NonNull;
import pdk.util.IBuilder;
import pdk.util.data.Point2D;

import java.util.List;
import java.util.Objects;

/**
 * Utilities for creating dataset.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Apr 2026, 3:20 PM
 */
public interface Data {

    static CategoryDatasetBuilder categoryDataset() {
        return new CategoryDatasetBuilder();
    }

    static XYDatasetBuilder xyDataset() {
        return new XYDatasetBuilder();
    }

    static HistogramDatasetBuilder histogramDataset() {
        return new HistogramDatasetBuilder();
    }

    /**
     * Create a {@link XYDataset} with one series
     *
     * @param key series key
     * @param x   x values
     * @param y   y values
     * @return {@link XYDataset}
     */
    static XYDataset createXYDataset(String key, double[] x, double[] y) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries(key);
        for (int i = 0; i < x.length; i++) {
            series.add(x[i], y[i]);
        }
        dataset.addSeries(series);
        return dataset;
    }

    class HistogramDatasetBuilder implements IBuilder<HistogramDataset> {

        private final HistogramDataset dataset = new HistogramDataset();

        private HistogramDatasetBuilder() {}

        /**
         * Sets the histogram type.
         *
         * @param type the type.
         */
        public HistogramDatasetBuilder type(@NonNull HistogramType type) {
            dataset.setType(type);
            return this;
        }

        /**
         * Adds a series to the dataset, using the specified number of bins.
         *
         * @param key    the series key ({@code null} not permitted).
         * @param values the values ({@code null} not permitted).
         * @param bins   the number of bins (must be at least 1).
         */
        public HistogramDatasetBuilder addSeries(String key, double[] values, int bins) {
            dataset.addSeries(key, values, bins);
            return this;
        }


        @Override
        public HistogramDataset build() {
            return dataset;
        }
    }

    class XYDatasetBuilder implements IBuilder<XYDataset> {

        private final XYSeriesCollection dataset_ = new XYSeriesCollection();

        private XYDatasetBuilder() {}

        /**
         * Add a data series
         *
         * @param series {@link XYSeries}
         * @return this
         */
        public XYDatasetBuilder addSeries(XYSeries series) {
            Objects.requireNonNull(series);
            dataset_.addSeries(series);
            return this;
        }

        /**
         * Add a data series
         *
         * @param name series name
         * @param x    x values
         * @param y    y values
         * @return this
         */
        public XYDatasetBuilder addSeries(String name, double[] x, double[] y) {
            Objects.requireNonNull(x);
            Objects.requireNonNull(y);

            XYSeries series = new XYSeries(name);
            for (int i = 0; i < x.length; i++) {
                series.add(x[i], y[i]);
            }
            dataset_.addSeries(series);

            return this;
        }


        /**
         * Add a data series
         *
         * @param name   series name
         * @param points {@link Point2D} list
         * @return this
         */
        public XYDatasetBuilder addSeries(String name, List<Point2D> points) {
            Objects.requireNonNull(points);
            XYSeries series = new XYSeries(name);
            for (Point2D point : points) {
                series.add(point.getX(), point.getY());
            }
            dataset_.addSeries(series);
            return this;
        }

        @Override
        public XYDataset build() {
            return dataset_;
        }
    }

    class CategoryDatasetBuilder implements IBuilder<CategoryDataset> {

        private final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        private CategoryDatasetBuilder() {}

        /**
         * Add a value
         *
         * @param rowKey    row key
         * @param columnKey column key
         * @param value     the value
         * @return this
         */
        public CategoryDatasetBuilder add(Comparable rowKey, Comparable columnKey, double value) {
            dataset.addValue(value, rowKey, columnKey);
            return this;
        }

        @Override
        public CategoryDataset build() {
            return dataset;
        }
    }
}

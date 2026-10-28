package pdk.util.svm.linear;

import pdk.chart.ScatterChart;
import pdk.chart.annotations.XYLineAnnotation;
import pdk.chart.api.RectangleInsets;
import pdk.chart.data.xy.XYSeries;
import pdk.chart.data.xy.XYSeriesCollection;
import pdk.chart.plot.PlotOrientation;
import pdk.chart.renderer.xy.XYLineAndShapeRenderer;
import pdk.chart.swing.ApplicationFrame;
import pdk.chart.swing.ChartPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LinearSVMPlot extends ApplicationFrame {

    /**
     * 每个类别 40 个点
     */
    private static final int N_PER_CLASS = 40;
    /**
     * C 很大，意味着弱正则化，强拟合，对线性近似可分的数据，会让间隔很窄，
     * 这是演示支持向量的常见选择
     */
    private static final double C = 1000.0;
    /**
     * 判定接近间隔边界的容差，因为数值解不会精确满足 |f|=1，用容差圈出近似向量
     */
    private static final double MARGIN_TOLERANCE = 0.15;

    /**
     * 二维坐标和标签
     */
    private record Sample(double x, double y, double label) {}

    public LinearSVMPlot(String title, List<Sample> samples, SVMModel model) {
        super(title);

        XYSeries negative = new XYSeries("Class -1");
        XYSeries positive = new XYSeries("Class +1");
        XYSeries marginPoints = new XYSeries("Near margin");

        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (Sample s : samples) {
            if (s.label() < 0) { // -1
                negative.add(s.x(), s.y());
            } else { // +1
                positive.add(s.x(), s.y());
            }

            minX = Math.min(minX, s.x());
            maxX = Math.max(maxX, s.x());
            minY = Math.min(minY, s.y());
            maxY = Math.max(maxY, s.y());

            double f = decisionValue(model, s.x(), s.y());

            // 接近边界
            if (Math.abs(Math.abs(f) - 1.0) <= MARGIN_TOLERANCE) {
                marginPoints.add(s.x(), s.y());
            }
        }

        double xPad = (maxX - minX) * 0.15;
        double yPad = (maxY - minY) * 0.15;

        minX -= xPad;
        maxX += xPad;
        minY -= yPad;
        maxY += yPad;

        XYSeriesCollection classes = new XYSeriesCollection();
        classes.addSeries(negative);
        classes.addSeries(positive);

        ScatterChart chart = new ScatterChart(classes, "Feature 1", "Feature 2");
        chart.title("Linear SVM");
        chart.setOrientation(PlotOrientation.VERTICAL);

        chart.withPlotBackgroundPaint(Color.WHITE)
                .withDomainGridlinePaint(new Color(220, 220, 220))
                .withRangeGridlinePaint(new Color(220, 220, 220))
                .withAxisOffset(new RectangleInsets(5, 5, 5, 5));
        chart.setLinesVisible(false);

        // 两个类别的散点
        chart.setSeriesPaint(0, new Color(65, 105, 225));
        chart.setSeriesPaint(1, new Color(220, 70, 70));
        Shape point = new Ellipse2D.Double(-4, -4, 8, 8);
        chart.setSeriesShape(0, point);
        chart.setSeriesShape(1, point);

        // 叠加间隔边界附近的样本
        XYSeriesCollection marginDataset = new XYSeriesCollection();
        marginDataset.addSeries(marginPoints);

        XYLineAndShapeRenderer marginRenderer =
                new XYLineAndShapeRenderer(false, true);

        // 空心圆半径 7
        marginRenderer.setSeriesShape(
                0, new Ellipse2D.Double(-7, -7, 14, 14));
        marginRenderer.setSeriesPaint(0, Color.BLACK);
        marginRenderer.setSeriesShapesFilled(0, false);
        marginRenderer.setSeriesOutlinePaint(0, Color.BLACK);
        marginRenderer.setSeriesOutlineStroke(0, new BasicStroke(1.2f));

        chart.setDataset(1, marginDataset);
        chart.setRenderer(1, marginRenderer);

        // 通过决策函数的三个采样值恢复线性函数：
        // f(x,y) = wx*x + wy*y + b
        // 下面这种设计，会用考虑 Model weight 数组的对应关系
        double b = decisionValue(model, 0, 0);
        double wx = decisionValue(model, 1, 0) - b;
        double wy = decisionValue(model, 0, 1) - b;

        // 绘制 f(x,y) = -1、0、1 三条线
        // 0 为决策边界，±1 为间隔线
        double[] levels = {-1, 0, 1};

        for (double level : levels) {
            // 防止 wy 为 0 的情况
            if (Math.abs(wy) > 1e-12) {
                double y1 = (level - b - wx * minX) / wy;
                double y2 = (level - b - wx * maxX) / wy;

                chart.addAnnotation(new XYLineAnnotation(
                        minX, y1, maxX, y2,
                        level == 0
                                ? new BasicStroke(1.8f)
                                : new BasicStroke(1.2f,
                                BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_ROUND,
                                1.0f, new float[]{5.0f, 5.0f}, 0.0f),
                        Color.BLACK));
            } else if (Math.abs(wx) > 1e-12) {
                // 决策边界为竖直线时单独处理
                double x = (level - b) / wx;
                chart.addAnnotation(new XYLineAnnotation(
                        x, minY, x, maxY,
                        level == 0
                                ? new BasicStroke(1.8f)
                                : new BasicStroke(
                                1.2f,
                                BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_ROUND,
                                1.0f,
                                new float[]{5.0f, 5.0f},
                                0.0f),
                        Color.BLACK));
            }
        }

        setContentPane(new ChartPanel(chart));
    }

    private static double decisionValue(SVMModel model, double x, double y) {
        // LIBLINEAR 特征索引从 1 开始。
        // 第 3 个特征为常数 bias=1。
        Feature[] features = {
                new Feature(1, x),
                new Feature(2, y),
                new Feature(3, 1.0)
        };

        double[] values = new double[1];
        model.predictValues(features, values); // f(x,y)=w1x+w2y+w3
        return values[0];
    }

    private static List<Sample> makeBlobs() {
        Random random = new Random(6);
        List<Sample> samples = new ArrayList<>(40);

        // 两个高斯簇；固定随机种子以便重复运行。
        // (-2,-2) (2,2)
        double[][] centers = {
                {-2.0, -2.0},
                {2.0, 2.0}
        };

        for (int cluster = 0; cluster < 2; cluster++) {
            // 标签用 -1 和 +1
            double label = cluster == 0 ? -1.0 : 1.0;

            for (int i = 0; i < N_PER_CLASS; i++) {
                double x = centers[cluster][0] + random.nextGaussian(); // 标准差 1
                double y = centers[cluster][1] + random.nextGaussian();
                samples.add(new Sample(x, y, label));
            }
        }

        return samples;
    }

    private static SVMModel train(List<Sample> samples) {
        SVMDataset dataset = new SVMDataset();
        dataset.l = samples.size();
        dataset.n = 3;       // 两个特征 + 一个 bias
        dataset.bias = 1.0; // 包含 bias 项

        dataset.x = new Feature[dataset.l][];
        dataset.y = new double[dataset.l];

        for (int i = 0; i < samples.size(); i++) {
            Sample s = samples.get(i);

            dataset.x[i] = new Feature[]{
                    new Feature(1, s.x()),
                    new Feature(2, s.y()),
                    new Feature(3, 1.0) // 加入 bias
            };

            dataset.y[i] = s.label();
        }

        Parameter parameter = new Parameter(SolverType.L2R_L2LOSS_SVC, C, 0.001);
        return Linear.train(dataset, parameter);
    }

    static void main() {
        SwingUtilities.invokeLater(() -> {
            List<Sample> samples = makeBlobs();
            SVMModel model = train(samples);

            LinearSVMPlot frame =
                    new LinearSVMPlot("LIBLINEAR Linear SVM", samples, model);

            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

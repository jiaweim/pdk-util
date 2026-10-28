package pdk.util.svm.linear;

import pdk.chart.ScatterChart;
import pdk.chart.annotations.XYLineAnnotation;
import pdk.chart.data.xy.XYSeries;
import pdk.chart.data.xy.XYSeriesCollection;
import pdk.chart.renderer.xy.XYLineAndShapeRenderer;
import pdk.chart.swing.ApplicationFrame;
import pdk.chart.swing.ChartPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LinearSVM2 extends ApplicationFrame {

    private record Sample(double x, double y, int label) {}

    private static final double[] C_VALUES = {1.0, 1000.0};
    private static final double EPS = 1e-15;

    public LinearSVM2(List<Sample> samples, SVMModel[] models) {

        super("LIBLINEAR Linear SVM: Effect of C");

        JPanel panel = new JPanel(new GridLayout(1, 2, 8, 0));

        for (int i = 0; i < C_VALUES.length; i++) {
            panel.add(createChart(samples, models[i], C_VALUES[i]));
        }

        setContentPane(panel);
    }

    private static ChartPanel createChart(List<Sample> samples,
            SVMModel model, double c) {

        XYSeries class0 = new XYSeries("Class 0");
        XYSeries class1 = new XYSeries("Class 1");
        XYSeries support = new XYSeries("Within margin");

        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (Sample s : samples) {
            if (s.label() == 0) {
                class0.add(s.x(), s.y());
            } else {
                class1.add(s.x(), s.y());
            }

            minX = Math.min(minX, s.x());
            maxX = Math.max(maxX, s.x());
            minY = Math.min(minY, s.y());
            maxY = Math.max(maxY, s.y());

            double f = decisionValue(model, s.x(), s.y());

            // 与 sklearn 示例中的条件一致：
            // abs(decision_function) <= 1 + 1e-15
            if (Math.abs(f) <= 1.0 + EPS) {
                support.add(s.x(), s.y());
            }
        }

        double xPad = (maxX - minX) * 0.12;
        double yPad = (maxY - minY) * 0.12;

        minX -= xPad;
        maxX += xPad;
        minY -= yPad;
        maxY += yPad;

        XYSeriesCollection classData = new XYSeriesCollection();
        classData.addSeries(class0);
        classData.addSeries(class1);

        ScatterChart chart = new ScatterChart(classData,
                "Feature 1", "Feature 2",
                "C = " + (int) c);


        chart.setBackgroundPaint(Color.WHITE);
        chart.setDomainGridlinePaint(new Color(220, 220, 220));
        chart.setRangeGridlinePaint(new Color(220, 220, 220));

        XYLineAndShapeRenderer classRenderer =
                new XYLineAndShapeRenderer(false, true);

        classRenderer.setSeriesPaint(0, new Color(65, 105, 225));
        classRenderer.setSeriesPaint(1, new Color(220, 70, 70));

        Shape dot = new Ellipse2D.Double(-4, -4, 8, 8);
        classRenderer.setSeriesShape(0, dot);
        classRenderer.setSeriesShape(1, dot);
        chart.setRenderer(0, classRenderer);

        // 叠加间隔内样本：空心黑色圆圈
        XYSeriesCollection supportData = new XYSeriesCollection();
        supportData.addSeries(support);

        XYLineAndShapeRenderer supportRenderer =
                new XYLineAndShapeRenderer(false, true);

        supportRenderer.setSeriesShape(
                0, new Ellipse2D.Double(-7, -7, 14, 14));
        supportRenderer.setSeriesPaint(0, Color.BLACK);
        supportRenderer.setSeriesOutlinePaint(0, Color.BLACK);
        supportRenderer.setSeriesOutlineStroke(0, new BasicStroke(1.0f));
        supportRenderer.setSeriesFillPaint(0, Color.WHITE);
        supportRenderer.setSeriesShapesFilled(0, false);
        supportRenderer.setDrawOutlines(true);

        chart.setDataset(1, supportData);
        chart.setRenderer(1, supportRenderer);

        chart.getDomainAxis().setRange(minX, maxX);
        chart.getRangeAxis().setRange(minY, maxY);

        // 从模型中计算线性决策函数 f(x,y) = wx*x + wy*y + b
        double b = decisionValue(model, 0, 0);
        double wx = decisionValue(model, 1, 0) - b;
        double wy = decisionValue(model, 0, 1) - b;

        double[] levels = {-1, 0, 1};

        for (double level : levels) {
            BasicStroke stroke;

            if (level == 0) {
                stroke = new BasicStroke(1.8f);
            } else {
                stroke = new BasicStroke(
                        1.2f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_ROUND,
                        1.0f,
                        new float[]{5.0f, 5.0f},
                        0.0f);
            }

            if (Math.abs(wy) > 1e-12) {
                double y1 = (level - b - wx * minX) / wy;
                double y2 = (level - b - wx * maxX) / wy;

                chart.addAnnotation(new XYLineAnnotation(
                        minX, y1, maxX, y2, stroke, Color.BLACK));

            } else if (Math.abs(wx) > 1e-12) {
                double x = (level - b) / wx;

                chart.addAnnotation(new XYLineAnnotation(
                        x, minY, x, maxY, stroke, Color.BLACK));
            }
        }

        return new ChartPanel(chart);
    }

    private static double decisionValue(
            SVMModel model,
            double x,
            double y) {

        Feature[] features = {
                new Feature(1, x),
                new Feature(2, y),
                new Feature(3, 1.0) // bias feature
        };

        double[] values = new double[1];
        Linear.predictValues(model, features, values);
        return values[0];
    }

    private static SVMModel train(List<Sample> samples, double c) {
        SVMDataset dataset = new SVMDataset();
        dataset.l = samples.size();
        dataset.n = 3;       // 两个特征 + bias
        dataset.bias = 1.0;

        dataset.x = new Feature[dataset.l][];
        dataset.y = new double[dataset.l];

        for (int i = 0; i < samples.size(); i++) {
            Sample s = samples.get(i);

            dataset.x[i] = new Feature[]{
                    new Feature(1, s.x()),
                    new Feature(2, s.y()),
                    new Feature(3, 1.0)
            };

            dataset.y[i] = s.label();
        }

        Parameter parameter = new Parameter(SolverType.L2R_L1LOSS_SVC_DUAL, c, 1e-7);
        parameter.setRandom(new Random(42));

        return Linear.train(dataset, parameter);
    }

    private static List<Sample> makeBlobs() {
        Random random = new Random(0);
        List<Sample> samples = new ArrayList<>(40);

        double[][] centers = {
                {-2.0, -2.0},
                {2.0, 2.0}
        };

        // 这里 label 取 0 和 1
        for (int label = 0; label < 2; label++) {
            for (int i = 0; i < 20; i++) {
                double x = centers[label][0] + random.nextGaussian();
                double y = centers[label][1] + random.nextGaussian();

                samples.add(new Sample(x, y, label));
            }
        }

        return samples;
    }

    static void main() {
        SwingUtilities.invokeLater(() -> {
            List<Sample> samples = makeBlobs();

            SVMModel[] models = {
                    train(samples, C_VALUES[0]),
                    train(samples, C_VALUES[1])
            };

            LinearSVM2 frame = new LinearSVM2(samples, models);

            frame.setSize(1100, 520);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

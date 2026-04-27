package pdk.util.chart.util;

import org.jfree.chart.LegendItemSource;
import org.jfree.chart.block.BlockFrame;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.util.SortOrder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import pdk.util.IBuilder;
import pdk.util.chart.Chart;

import java.awt.*;

/**
 * Builder class for {@link LegendTitle}.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Apr 2026, 9:45 AM
 */
public class LegendTitleBuilder implements IBuilder<LegendTitle> {

    public static LegendTitleBuilder create(Chart source) {
        return new LegendTitleBuilder(source.getChart().getPlot());
    }

    private final LegendTitle legendTitle_;

    private LegendTitleBuilder(LegendItemSource source) {
        legendTitle_ = new LegendTitle(source);
    }

    /**
     * Sets the legend item sources.
     *
     * @param sources the sources.
     */
    public LegendTitleBuilder sources(@NonNull LegendItemSource[] sources) {
        legendTitle_.setSources(sources);
        return this;
    }

    /**
     * Sets the position for the title.
     *
     * @param position the position.
     */
    public LegendTitleBuilder position(@NonNull RectangleEdge position) {
        legendTitle_.setPosition(position);
        return this;
    }

    /**
     * Sets the frame (or border).
     *
     * @param frame the frame.
     */
    public LegendTitleBuilder frame(@NonNull BlockFrame frame) {
        legendTitle_.setFrame(frame);
        return this;
    }

    /**
     * Sets the background paint for the legend.
     *
     * @param paint the paint ({@code null} permitted).
     */
    public LegendTitleBuilder backgroundPaint(@Nullable Paint paint) {
        legendTitle_.setBackgroundPaint(paint);
        return this;
    }

    /**
     * Sets the location of the shape within each legend item.
     *
     * @param edge the edge.
     */
    public LegendTitleBuilder legendItemGraphicEdge(@NonNull RectangleEdge edge) {
        legendTitle_.setLegendItemGraphicEdge(edge);
        return this;
    }

    /**
     * Sets the anchor point used for the graphic in each legend item.
     *
     * @param anchor the anchor point.
     */
    public LegendTitleBuilder legendItemGraphicAnchor(@NonNull RectangleAnchor anchor) {
        legendTitle_.setLegendItemGraphicAnchor(anchor);
        return this;
    }

    /**
     * Sets the legend item graphic location.
     *
     * @param anchor the anchor.
     */
    public LegendTitleBuilder legendItemGraphicLocation(@NonNull RectangleAnchor anchor) {
        legendTitle_.setLegendItemGraphicLocation(anchor);
        return this;
    }

    /**
     * Sets the padding that will be applied to each item graphic in the
     * legend.
     *
     * @param padding the padding.
     */
    public LegendTitleBuilder legendItemGraphicPadding(@NonNull RectangleInsets padding) {
        legendTitle_.setLegendItemGraphicPadding(padding);
        return this;
    }

    /**
     * Sets the item font.
     *
     * @param font the font.
     */
    public LegendTitleBuilder itemFont(@NonNull Font font) {
        legendTitle_.setItemFont(font);
        return this;
    }

    /**
     * Sets the item paint.
     *
     * @param paint the paint.
     */
    public LegendTitleBuilder itemPaint(@NonNull Paint paint) {
        legendTitle_.setItemPaint(paint);
        return this;
    }

    /**
     * Sets the padding used for the item labels in the legend.
     *
     * @param padding the padding.
     */
    public LegendTitleBuilder itemLabelPadding(@NonNull RectangleInsets padding) {
        legendTitle_.setItemLabelPadding(padding);
        return this;
    }

    /**
     * Sets the order used to display legend items.
     *
     * @param order Specifies ascending or descending order.
     */
    public LegendTitleBuilder sortOrder(@NonNull SortOrder order) {
        legendTitle_.setSortOrder(order);
        return this;
    }

    @Override
    public LegendTitle build() {
        return legendTitle_;
    }
}

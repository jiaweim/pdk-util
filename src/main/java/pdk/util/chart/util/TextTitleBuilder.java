package pdk.util.chart.util;

import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.VerticalAlignment;
import org.jspecify.annotations.NonNull;
import pdk.util.IBuilder;

import java.awt.*;

/**
 * A Builder class for {@link TextTitle}
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 19 May 2026, 5:10 PM
 */
public class TextTitleBuilder implements IBuilder<TextTitle> {

    public static TextTitleBuilder create(String text) {
        return new TextTitleBuilder(text);
    }

    private TextTitle textTitle;

    public TextTitleBuilder(String text) {
        this.textTitle = new TextTitle(text);
        this.textTitle.setNotify(false);
    }

    /**
     * Sets the font used to display the title string.
     *
     * @param font the new font.
     */
    public TextTitleBuilder font(@NonNull Font font) {
        textTitle.setFont(font);
        return this;
    }

    /**
     * Sets the position for the title.
     *
     * @param position the position.
     */
    public TextTitleBuilder position(@NonNull RectangleEdge position) {
        textTitle.setPosition(position);
        return this;
    }

    /**
     * Sets the padding (use {@link RectangleInsets#ZERO_INSETS} for no
     * padding).
     *
     * @param padding the padding.
     */
    public TextTitleBuilder padding(@NonNull RectangleInsets padding) {
        textTitle.setPadding(padding);
        return this;
    }

    /**
     * Sets the vertical alignment for the title.
     *
     * @param alignment the new vertical alignment (TOP, MIDDLE or BOTTOM).
     */
    public TextTitleBuilder verticalAlignment(@NonNull VerticalAlignment alignment) {
        textTitle.setVerticalAlignment(alignment);
        return this;
    }

    @Override
    public TextTitle build() {
        return textTitle;
    }
}

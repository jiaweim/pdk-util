package pdk.util.color;

import java.awt.*;

/**
 * Sequential color scales are appropriate for most continuous data, but in some cases it can be helpful to use a
 * diverging or cyclical color scale.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 25 Jul 2025, 6:09 PM
 */
public interface JColorSequential {

    static Color[] Plotly3() {
        return new Color[]{
                new Color(5, 8, 184),
                new Color(25, 16, 216),
                new Color(60, 25, 240),
                new Color(107, 28, 251),
                new Color(152, 28, 253),
                new Color(191, 28, 253),
                new Color(221, 43, 253),
                new Color(242, 70, 254),
                new Color(252, 103, 253),
                new Color(254, 136, 252),
                new Color(254, 165, 253),
                new Color(254, 190, 254),
                new Color(254, 195, 254)
        };
    }

    static Color[] Viridis() {
        return new Color[]{
                new Color(68, 1, 84),
                new Color(72, 40, 120),
                new Color(62, 73, 137),
                new Color(49, 104, 142),
                new Color(38, 130, 142),
                new Color(31, 158, 137),
                new Color(53, 183, 121),
                new Color(110, 206, 88),
                new Color(181, 222, 43),
                new Color(253, 231, 37)
        };
    }

    static Color[] Cividis() {
        return new Color[]{
                new Color(0, 34, 78),
                new Color(18, 53, 112),
                new Color(59, 73, 108),
                new Color(87, 93, 109),
                new Color(112, 113, 115),
                new Color(138, 134, 120),
                new Color(165, 156, 116),
                new Color(195, 179, 105),
                new Color(225, 204, 85),
                new Color(254, 232, 56)
        };
    }

    static Color[] Inferno() {
        return new Color[]{
                new Color(0, 0, 4),
                new Color(27, 12, 65),
                new Color(74, 12, 107),
                new Color(120, 28, 109),
                new Color(165, 44, 96),
                new Color(207, 68, 70),
                new Color(237, 105, 37),
                new Color(251, 155, 6),
                new Color(247, 209, 61),
                new Color(252, 255, 164)
        };
    }

    static Color[] Magma() {
        return new Color[]{
                new Color(0, 0, 4),
                new Color(24, 15, 61),
                new Color(68, 15, 118),
                new Color(114, 31, 129),
                new Color(158, 47, 127),
                new Color(205, 64, 113),
                new Color(241, 96, 93),
                new Color(253, 150, 104),
                new Color(254, 202, 141),
                new Color(252, 253, 191)
        };
    }

    static Color[] Plasma() {
        return new Color[]{
                new Color(13, 8, 135),
                new Color(70, 3, 159),
                new Color(114, 1, 168),
                new Color(156, 23, 158),
                new Color(189, 55, 134),
                new Color(216, 87, 107),
                new Color(237, 121, 83),
                new Color(251, 159, 58),
                new Color(253, 202, 38),
                new Color(240, 249, 33)
        };
    }

    static Color[] Turbo() {
        return new Color[]{
                new Color(48, 18, 59),
                new Color(65, 69, 171),
                new Color(70, 117, 237),
                new Color(57, 162, 252),
                new Color(27, 207, 212),
                new Color(36, 236, 166),
                new Color(97, 252, 108),
                new Color(164, 252, 59),
                new Color(209, 232, 52),
                new Color(243, 198, 58),
                new Color(254, 155, 45),
                new Color(243, 99, 21),
                new Color(217, 56, 6),
                new Color(177, 25, 1),
                new Color(122, 4, 2)
        };
    }

    static Color[] Blackbody() {
        return new Color[]{
                new Color(0, 0, 0),
                new Color(230, 0, 0),
                new Color(230, 210, 0),
                new Color(255, 255, 255),
                new Color(160, 200, 255)
        };
    }

    static Color[] Bluered() {
        return new Color[]{
                new Color(0, 0, 255),
                new Color(255, 0, 0)
        };
    }

    static Color[] Electric() {
        return new Color[]{
                new Color(0, 0, 0),
                new Color(30, 0, 100),
                new Color(120, 0, 100),
                new Color(160, 90, 0),
                new Color(230, 200, 0),
                new Color(255, 250, 220)
        };
    }

    static Color[] Hot() {
        return new Color[]{
                new Color(0, 0, 0),
                new Color(230, 0, 0),
                new Color(255, 210, 0),
                new Color(255, 255, 255)
        };
    }

    static Color[] Jet() {
        return new Color[]{
                new Color(0, 0, 131),
                new Color(0, 60, 170),
                new Color(5, 255, 255),
                new Color(255, 255, 0),
                new Color(250, 0, 0),
                new Color(128, 0, 0)
        };
    }

    static Color[] Rainbow() {
        return new Color[]{
                new Color(150, 0, 90),
                new Color(0, 0, 200),
                new Color(0, 25, 255),
                new Color(0, 152, 255),
                new Color(44, 255, 150),
                new Color(151, 255, 0),
                new Color(255, 234, 0),
                new Color(255, 111, 0),
                new Color(255, 0, 0)
        };
    }
}

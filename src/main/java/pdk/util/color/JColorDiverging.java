package pdk.util.color;

import java.awt.*;

/**
 * Diverging color scales are appropriate for continuous data that has a natural midpoint other otherwise informative
 * special value, such as 0 altitude, or the boiling point of a liquid
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 25 Jul 2025, 5:46 PM
 */
public interface JColorDiverging {

    static Color[] BrBG() {
        return new Color[]{
                new Color(84, 48, 5),
                new Color(140, 81, 10),
                new Color(191, 129, 45),
                new Color(223, 194, 125),
                new Color(246, 232, 195),
                new Color(245, 245, 245),
                new Color(199, 234, 229),
                new Color(128, 205, 193),
                new Color(53, 151, 143),
                new Color(1, 102, 94),
                new Color(0, 60, 48),
        };
    }

    static Color[] PRGn() {
        return new Color[]{
                new Color(64, 0, 75),
                new Color(118, 42, 131),
                new Color(153, 112, 171),
                new Color(194, 165, 207),
                new Color(231, 212, 232),
                new Color(247, 247, 247),
                new Color(217, 240, 211),
                new Color(166, 219, 160),
                new Color(90, 174, 97),
                new Color(27, 120, 55),
                new Color(0, 68, 27),
        };
    }

    static Color[] PiYG() {
        return new Color[]{
                new Color(142, 1, 82),
                new Color(197, 27, 125),
                new Color(222, 119, 174),
                new Color(241, 182, 218),
                new Color(253, 224, 239),
                new Color(247, 247, 247),
                new Color(230, 245, 208),
                new Color(184, 225, 134),
                new Color(127, 188, 65),
                new Color(77, 146, 33),
                new Color(39, 100, 25),
        };
    }

    static Color[] PuOr() {
        return new Color[]{
                new Color(127, 59, 8),
                new Color(179, 88, 6),
                new Color(224, 130, 20),
                new Color(253, 184, 99),
                new Color(254, 224, 182),
                new Color(247, 247, 247),
                new Color(216, 218, 235),
                new Color(178, 171, 210),
                new Color(128, 115, 172),
                new Color(84, 39, 136),
                new Color(45, 0, 75),
        };
    }

    static Color[] RdBu() {
        return new Color[]{
                new Color(103, 0, 31),
                new Color(178, 24, 43),
                new Color(214, 96, 77),
                new Color(244, 165, 130),
                new Color(253, 219, 199),
                new Color(247, 247, 247),
                new Color(209, 229, 240),
                new Color(146, 197, 222),
                new Color(67, 147, 195),
                new Color(33, 102, 172),
                new Color(5, 48, 97),
        };
    }


    static Color[] RdGy() {
        return new Color[]{
                new Color(103, 0, 31),
                new Color(178, 24, 43),
                new Color(214, 96, 77),
                new Color(244, 165, 130),
                new Color(253, 219, 199),
                new Color(255, 255, 255),
                new Color(224, 224, 224),
                new Color(186, 186, 186),
                new Color(135, 135, 135),
                new Color(77, 77, 77),
                new Color(26, 26, 26),
        };
    }

    static Color[] RdYlBu() {
        return new Color[]{
                new Color(165, 0, 38),
                new Color(215, 48, 39),
                new Color(244, 109, 67),
                new Color(253, 174, 97),
                new Color(254, 224, 144),
                new Color(255, 255, 191),
                new Color(224, 243, 248),
                new Color(171, 217, 233),
                new Color(116, 173, 209),
                new Color(69, 117, 180),
                new Color(49, 54, 149),
        };
    }

    static Color[] RdYlGn() {
        return new Color[]{
                new Color(165, 0, 38),
                new Color(215, 48, 39),
                new Color(244, 109, 67),
                new Color(253, 174, 97),
                new Color(254, 224, 139),
                new Color(255, 255, 191),
                new Color(217, 239, 139),
                new Color(166, 217, 106),
                new Color(102, 189, 99),
                new Color(26, 152, 80),
                new Color(0, 104, 55),
        };
    }

    static Color[] Spectral() {
        return new Color[]{
                new Color(158, 1, 66),
                new Color(213, 62, 79),
                new Color(244, 109, 67),
                new Color(253, 174, 97),
                new Color(254, 224, 139),
                new Color(255, 255, 191),
                new Color(230, 245, 152),
                new Color(171, 221, 164),
                new Color(102, 194, 165),
                new Color(50, 136, 189),
                new Color(94, 79, 162),
        };
    }

    static Color[] balance() {
        return new Color[]{
                new Color(23, 28, 66),
                new Color(41, 58, 143),
                new Color(11, 102, 189),
                new Color(69, 144, 185),
                new Color(142, 181, 194),
                new Color(210, 216, 219),
                new Color(230, 210, 204),
                new Color(213, 157, 137),
                new Color(196, 101, 72),
                new Color(172, 43, 36),
                new Color(120, 14, 40),
                new Color(60, 9, 17),
        };
    }

    static Color[] delta() {
        return new Color[]{
                new Color(16, 31, 63),
                new Color(38, 62, 144),
                new Color(30, 110, 161),
                new Color(60, 154, 171),
                new Color(140, 193, 186),
                new Color(217, 229, 218),
                new Color(239, 226, 156),
                new Color(195, 182, 59),
                new Color(115, 152, 5),
                new Color(34, 120, 36),
                new Color(18, 78, 43),
                new Color(23, 35, 18),
        };
    }


    static Color[] curl() {
        return new Color[]{
                new Color(20, 29, 67),
                new Color(28, 72, 93),
                new Color(18, 115, 117),
                new Color(63, 156, 129),
                new Color(153, 189, 156),
                new Color(223, 225, 211),
                new Color(241, 218, 206),
                new Color(224, 160, 137),
                new Color(203, 101, 99),
                new Color(164, 54, 96),
                new Color(111, 23, 91),
                new Color(51, 13, 53),
        };
    }

    static Color[] oxy() {
        return new Color[]{
                new Color(63, 5, 5),
                new Color(101, 6, 13),
                new Color(138, 17, 9),
                new Color(96, 95, 95),
                new Color(119, 118, 118),
                new Color(142, 141, 141),
                new Color(166, 166, 165),
                new Color(193, 192, 191),
                new Color(222, 222, 220),
                new Color(239, 248, 90),
                new Color(230, 210, 41),
                new Color(220, 174, 25),
        };
    }


    static Color[] Armyrose() {
        return new Color[]{
                new Color(121, 130, 52),
                new Color(163, 173, 98),
                new Color(208, 211, 162),
                new Color(253, 251, 228),
                new Color(240, 198, 195),
                new Color(223, 145, 163),
                new Color(212, 103, 128),
        };
    }

    static Color[] Fall() {
        return new Color[]{
                new Color(61, 89, 65),
                new Color(119, 136, 104),
                new Color(181, 185, 145),
                new Color(246, 237, 189),
                new Color(237, 187, 138),
                new Color(222, 138, 90),
                new Color(202, 86, 44),
        };
    }

    static Color[] Geyser() {
        return new Color[]{
                new Color(0, 128, 128),
                new Color(112, 164, 148),
                new Color(180, 200, 168),
                new Color(246, 237, 189),
                new Color(237, 187, 138),
                new Color(222, 138, 90),
                new Color(202, 86, 44),
        };
    }

    static Color[] Temps() {
        return new Color[]{
                new Color(0, 147, 146),
                new Color(57, 177, 133),
                new Color(156, 203, 134),
                new Color(233, 226, 156),
                new Color(238, 180, 121),
                new Color(232, 132, 113),
                new Color(207, 89, 126),
        };
    }

    static Color[] Tealrose() {
        return new Color[]{
                new Color(0, 147, 146),
                new Color(114, 170, 161),
                new Color(177, 199, 179),
                new Color(241, 234, 200),
                new Color(229, 185, 173),
                new Color(217, 137, 148),
                new Color(208, 88, 126),
        };
    }

    static Color[] Tropic() {
        return new Color[]{
                new Color(0, 155, 158),
                new Color(66, 183, 185),
                new Color(167, 211, 212),
                new Color(241, 241, 241),
                new Color(228, 193, 217),
                new Color(214, 145, 193),
                new Color(199, 93, 171),
        };
    }

    static Color[] Earth() {
        return new Color[]{
                new Color(161, 105, 40),
                new Color(189, 146, 90),
                new Color(214, 189, 141),
                new Color(237, 234, 194),
                new Color(181, 200, 184),
                new Color(121, 167, 172),
                new Color(40, 135, 161),
        };
    }

    static Color[] Picnic() {
        return new Color[]{
                new Color(0, 0, 255),
                new Color(51, 153, 255),
                new Color(102, 204, 255),
                new Color(153, 204, 255),
                new Color(204, 204, 255),
                new Color(255, 255, 255),
                new Color(255, 204, 255),
                new Color(255, 153, 255),
                new Color(255, 102, 204),
                new Color(255, 102, 102),
                new Color(255, 0, 0),
        };
    }

    static Color[] Portland() {
        return new Color[]{
                new Color(12, 51, 131),
                new Color(10, 136, 186),
                new Color(242, 211, 56),
                new Color(242, 143, 56),
                new Color(217, 30, 30),
        };
    }
}

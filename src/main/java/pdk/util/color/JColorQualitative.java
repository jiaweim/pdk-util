package pdk.util.color;

import java.awt.*;

/**
 * Define qualitative color series, which is used to represent categorical data using discrete colors,
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 25 Jul 2025, 5:56 PM
 */
public interface JColorQualitative {

    static Color[] Plotly() {
        return new Color[]{
                new Color(99, 110, 250),
                new Color(239, 85, 59),
                new Color(0, 204, 150),
                new Color(171, 99, 250),
                new Color(255, 161, 90),
                new Color(25, 211, 243),
                new Color(255, 102, 146),
                new Color(182, 232, 128),
                new Color(255, 151, 255),
                new Color(254, 203, 82)
        };
    }

    static Color[] D3() {
        return new Color[]{
                new Color(31, 119, 180),
                new Color(255, 127, 14),
                new Color(44, 160, 44),
                new Color(214, 39, 40),
                new Color(148, 103, 189),
                new Color(140, 86, 75),
                new Color(227, 119, 194),
                new Color(127, 127, 127),
                new Color(188, 189, 34),
                new Color(23, 190, 207)
        };
    }


    static Color[] G10() {
        return new Color[]{
                new Color(51, 102, 204),
                new Color(220, 57, 18),
                new Color(255, 153, 0),
                new Color(16, 150, 24),
                new Color(153, 0, 153),
                new Color(0, 153, 198),
                new Color(221, 68, 119),
                new Color(102, 170, 0),
                new Color(184, 46, 46),
                new Color(49, 99, 149)
        };
    }

    static Color[] T10() {
        return new Color[]{
                new Color(76, 120, 168),
                new Color(245, 133, 24),
                new Color(228, 87, 86),
                new Color(114, 183, 178),
                new Color(84, 162, 75),
                new Color(238, 202, 59),
                new Color(178, 121, 162),
                new Color(255, 157, 166),
                new Color(157, 117, 93),
                new Color(186, 176, 172)
        };
    }

    static Color[] Alphabet() {
        return new Color[]{
                new Color(170, 13, 254),
                new Color(50, 131, 254),
                new Color(133, 102, 13),
                new Color(120, 42, 182),
                new Color(86, 86, 86),
                new Color(28, 131, 86),
                new Color(22, 255, 50),
                new Color(247, 225, 160),
                new Color(226, 226, 226),
                new Color(28, 190, 79),
                new Color(196, 69, 28),
                new Color(222, 160, 253),
                new Color(254, 0, 250),
                new Color(50, 90, 155),
                new Color(254, 175, 22),
                new Color(248, 161, 159),
                new Color(144, 173, 28),
                new Color(246, 34, 46),
                new Color(28, 255, 206),
                new Color(46, 217, 255),
                new Color(177, 13, 161),
                new Color(192, 117, 166),
                new Color(252, 28, 191),
                new Color(176, 0, 104),
                new Color(251, 228, 38),
                new Color(250, 0, 135)
        };
    }

    static Color[] Dark24() {
        return new Color[]{
                new Color(46, 145, 229),
                new Color(225, 95, 153),
                new Color(28, 167, 28),
                new Color(251, 13, 13),
                new Color(218, 22, 255),
                new Color(34, 42, 42),
                new Color(182, 129, 0),
                new Color(117, 13, 134),
                new Color(235, 102, 59),
                new Color(81, 28, 251),
                new Color(0, 160, 139),
                new Color(251, 0, 209),
                new Color(252, 0, 128),
                new Color(178, 130, 141),
                new Color(108, 124, 50),
                new Color(119, 138, 174),
                new Color(134, 42, 22),
                new Color(167, 119, 241),
                new Color(98, 0, 66),
                new Color(22, 22, 167),
                new Color(218, 96, 202),
                new Color(108, 69, 22),
                new Color(13, 42, 99),
                new Color(175, 0, 56)
        };
    }

    static Color[] Light24() {
        return new Color[]{
                new Color(253, 50, 22),
                new Color(0, 254, 53),
                new Color(106, 118, 252),
                new Color(254, 212, 196),
                new Color(254, 0, 206),
                new Color(13, 249, 255),
                new Color(246, 249, 38),
                new Color(255, 150, 22),
                new Color(71, 155, 85),
                new Color(238, 166, 251),
                new Color(220, 88, 125),
                new Color(214, 38, 255),
                new Color(110, 137, 156),
                new Color(0, 181, 247),
                new Color(182, 142, 0),
                new Color(201, 251, 229),
                new Color(255, 0, 146),
                new Color(34, 255, 167),
                new Color(227, 238, 158),
                new Color(134, 206, 0),
                new Color(188, 113, 150),
                new Color(126, 125, 205),
                new Color(252, 105, 85),
                new Color(228, 143, 114)
        };
    }

    static Color[] Set1() {
        return new Color[]{
                new Color(228, 26, 28),
                new Color(55, 126, 184),
                new Color(77, 175, 74),
                new Color(152, 78, 163),
                new Color(255, 127, 0),
                new Color(255, 255, 51),
                new Color(166, 86, 40),
                new Color(247, 129, 191),
                new Color(153, 153, 153)
        };
    }

    static Color[] Pastel1() {
        return new Color[]{
                new Color(251, 180, 174),
                new Color(179, 205, 227),
                new Color(204, 235, 197),
                new Color(222, 203, 228),
                new Color(254, 217, 166),
                new Color(255, 255, 204),
                new Color(229, 216, 189),
                new Color(253, 218, 236),
                new Color(242, 242, 242)
        };
    }

    static Color[] Dark2() {
        return new Color[]{
                new Color(27, 158, 119),
                new Color(217, 95, 2),
                new Color(117, 112, 179),
                new Color(231, 41, 138),
                new Color(102, 166, 30),
                new Color(230, 171, 2),
                new Color(166, 118, 29),
                new Color(102, 102, 102)
        };
    }

    static Color[] Set2() {
        return new Color[]{
                new Color(102, 194, 165),
                new Color(252, 141, 98),
                new Color(141, 160, 203),
                new Color(231, 138, 195),
                new Color(166, 216, 84),
                new Color(255, 217, 47),
                new Color(229, 196, 148),
                new Color(179, 179, 179)
        };
    }

    static Color[] Pastel2() {
        return new Color[]{
                new Color(179, 226, 205),
                new Color(253, 205, 172),
                new Color(203, 213, 232),
                new Color(244, 202, 228),
                new Color(230, 245, 201),
                new Color(255, 242, 174),
                new Color(241, 226, 204),
                new Color(204, 204, 204)
        };
    }

    static Color[] Set3() {
        return new Color[]{
                new Color(141, 211, 199),
                new Color(255, 255, 179),
                new Color(190, 186, 218),
                new Color(251, 128, 114),
                new Color(128, 177, 211),
                new Color(253, 180, 98),
                new Color(179, 222, 105),
                new Color(252, 205, 229),
                new Color(217, 217, 217),
                new Color(188, 128, 189),
                new Color(204, 235, 197),
                new Color(255, 237, 111)
        };
    }

    static Color[] Antique() {
        return new Color[]{
                new Color(133, 92, 117),
                new Color(217, 175, 107),
                new Color(175, 100, 88),
                new Color(115, 111, 76),
                new Color(82, 106, 131),
                new Color(98, 83, 119),
                new Color(104, 133, 92),
                new Color(156, 156, 94),
                new Color(160, 97, 119),
                new Color(140, 120, 93),
                new Color(124, 124, 124)
        };
    }

    static Color[] Bold() {
        return new Color[]{
                new Color(127, 60, 141),
                new Color(17, 165, 121),
                new Color(57, 105, 172),
                new Color(242, 183, 1),
                new Color(231, 63, 116),
                new Color(128, 186, 90),
                new Color(230, 131, 16),
                new Color(0, 134, 149),
                new Color(207, 28, 144),
                new Color(249, 123, 114),
                new Color(165, 170, 153)
        };
    }

    static Color[] Pastel() {
        return new Color[]{
                new Color(102, 197, 204),
                new Color(246, 207, 113),
                new Color(248, 156, 116),
                new Color(220, 176, 242),
                new Color(135, 197, 95),
                new Color(158, 185, 243),
                new Color(254, 136, 177),
                new Color(201, 219, 116),
                new Color(139, 224, 164),
                new Color(180, 151, 231),
                new Color(179, 179, 179)
        };
    }

    static Color[] Prism() {
        return new Color[]{
                new Color(95, 70, 144),
                new Color(29, 105, 150),
                new Color(56, 166, 165),
                new Color(15, 133, 84),
                new Color(115, 175, 72),
                new Color(237, 173, 8),
                new Color(225, 124, 5),
                new Color(204, 80, 62),
                new Color(148, 52, 110),
                new Color(111, 64, 112),
                new Color(102, 102, 102)
        };
    }

    static Color[] Vivid() {
        return new Color[]{
                new Color(229, 134, 6),
                new Color(93, 105, 177),
                new Color(82, 188, 163),
                new Color(153, 201, 69),
                new Color(204, 97, 176),
                new Color(36, 121, 108),
                new Color(218, 165, 27),
                new Color(47, 138, 196),
                new Color(118, 78, 159),
                new Color(237, 100, 90),
                new Color(165, 170, 153)
        };
    }

}

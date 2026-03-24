package pdk.util.color;

import java.awt.*;

/**
 * Cyclical color scales are appropriate for continuous data that has a natural cyclical structure, such as temporal
 * data (hour of day, day of week, day of year, seasons) or complex numbers or other phase or angular data.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 25 Jul 2025, 5:38 PM
 */
public interface JColorCyclical {

    static Color[] Twilight() {
        return new Color[]{
                new Color(226, 217, 226),
                new Color(158, 187, 201),
                new Color(103, 133, 190),
                new Color(94, 67, 165),
                new Color(66, 18, 87),
                new Color(71, 19, 64),
                new Color(142, 44, 80),
                new Color(186, 102, 8),
                new Color(206, 172, 148),
                new Color(226, 217, 226)
        };
    }

    static Color[] IceFire() {
        return new Color[]{
                new Color(0, 0, 0),
                new Color(0, 31, 77),
                new Color(0, 55, 134),
                new Color(14, 88, 168),
                new Color(33, 126, 184),
                new Color(48, 164, 202),
                new Color(84, 200, 223),
                new Color(155, 228, 239),
                new Color(225, 233, 209),
                new Color(243, 213, 115),
                new Color(231, 176, 0),
                new Color(218, 130, 0),
                new Color(198, 84, 0),
                new Color(172, 35, 1),
                new Color(130, 0, 0),
                new Color(76, 0, 0),
                new Color(0, 0, 0),
        };
    }

    static Color[] Edge() {
        return new Color[]{
                new Color(49, 49, 49),
                new Color(61, 1, 157),
                new Color(56, 16, 220),
                new Color(45, 71, 249),
                new Color(37, 147, 255),
                new Color(42, 222, 246),
                new Color(96, 253, 250),
                new Color(174, 253, 255),
                new Color(243, 243, 241),
                new Color(255, 253, 169),
                new Color(250, 253, 91),
                new Color(247, 218, 41),
                new Color(255, 142, 37),
                new Color(248, 67, 45),
                new Color(217, 13, 57),
                new Color(151, 2, 61),
                new Color(49, 49, 49),
        };
    }

    static Color[] Phase() {
        return new Color[]{
                new Color(167, 119, 12),
                new Color(197, 96, 51),
                new Color(217, 67, 96),
                new Color(221, 38, 163),
                new Color(196, 59, 224),
                new Color(153, 97, 244),
                new Color(95, 127, 228),
                new Color(40, 144, 183),
                new Color(15, 151, 136),
                new Color(39, 153, 79),
                new Color(119, 141, 17),
                new Color(167, 119, 12),
        };
    }

    static Color[] HSV() {
        return new Color[]{
                new Color(255, 0, 0),
                new Color(255, 167, 0),
                new Color(175, 255, 0),
                new Color(8, 255, 0),
                new Color(0, 255, 159),
                new Color(0, 183, 255),
                new Color(0, 16, 255),
                new Color(151, 0, 255),
                new Color(255, 0, 191),
                new Color(255, 0, 0),
        };
    }

    static Color[] mrybm() {
        return new Color[]{
                new Color(248, 132, 247),
                new Color(249, 104, 196),
                new Color(234, 67, 136),
                new Color(207, 36, 75),
                new Color(181, 26, 21),
                new Color(189, 67, 4),
                new Color(204, 105, 4),
                new Color(213, 143, 4),
                new Color(207, 170, 39),
                new Color(161, 159, 98),
                new Color(88, 138, 147),
                new Color(34, 105, 196),
                new Color(62, 62, 240),
                new Color(107, 78, 249),
                new Color(149, 107, 250),
                new Color(205, 125, 254),
                new Color(248, 132, 247),
        };
    }

    static Color[] mygbm() {
        return new Color[]{
                new Color(239, 85, 241),
                new Color(251, 132, 206),
                new Color(251, 175, 161),
                new Color(252, 212, 113),
                new Color(240, 237, 53),
                new Color(198, 229, 22),
                new Color(150, 211, 16),
                new Color(97, 193, 11),
                new Color(49, 172, 40),
                new Color(67, 144, 100),
                new Color(61, 113, 154),
                new Color(40, 78, 200),
                new Color(46, 33, 234),
                new Color(99, 36, 245),
                new Color(145, 57, 250),
                new Color(197, 67, 250),
                new Color(239, 85, 241),
        };
    }
}

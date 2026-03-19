package pdk.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 18 Mar 2026, 2:02 PM
 */
class StringUtilsTest {

    @Test
    void isEmpty() {
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("bob"));
        assertFalse(StringUtils.isEmpty(" bob "));
    }

    @Test
    void isNotEmpty() {
        assertFalse(StringUtils.isNotEmpty(null));
        assertFalse(StringUtils.isNotEmpty(""));
        assertTrue(StringUtils.isNotEmpty(" "));
        assertTrue(StringUtils.isNotEmpty("abd"));
        assertTrue(StringUtils.isNotEmpty("   abd   "));
    }

    @Test
    void replaceCharsChar() {
        assertNull(StringUtils.replaceChars(null, ' ', ' '));
        assertEquals("", StringUtils.replaceChars("", ' ', ' '));
        assertEquals("aycya", StringUtils.replaceChars("abcba", 'b', 'y'));
        assertEquals("abcba", StringUtils.replaceChars("abcba", 'z', 'y'));
    }

    @Test
    void reverse() {
        assertEquals(null, StringUtils.reverse(null));
        assertEquals("", StringUtils.reverse(""));
        assertEquals("tab", StringUtils.reverse("bat"));
    }

}
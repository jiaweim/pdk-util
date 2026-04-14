package pdk.util;

import com.google.common.base.Joiner;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    void insert() {
        String insert = StringUtils.insert("abcde", 2, 'X');
        assertEquals("abXcde", insert);
    }

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

    @Test
    void reversePart() {
        String a = "PEPTIDE";

        assertEquals("EDITPEP", StringUtils.reverse(a));

        assertEquals("PDITPEE", StringUtils.reverse(a, 1, a.length() - 1));
        assertEquals("PEITPDE", StringUtils.reverse(a, 2, a.length() - 2));
    }

    @Test
    void deleteWhitespace() {
        assertNull(StringUtils.deleteWhitespace(null));
        assertEquals("", StringUtils.deleteWhitespace(""));
        assertEquals("abc", StringUtils.deleteWhitespace("abc"));
        assertEquals("abc", StringUtils.deleteWhitespace("   ab  c  "));
    }

    @Test
    void countMatches() {
        assertEquals(0, StringUtils.countMatches(null, " "));
        assertEquals(0, StringUtils.countMatches("", " "));
        assertEquals(0, StringUtils.countMatches("abba", null));
        assertEquals(0, StringUtils.countMatches("abba", ""));

        assertEquals(2, StringUtils.countMatches("abba", "a"));
        assertEquals(1, StringUtils.countMatches("abba", "ab"));
        assertEquals(0, StringUtils.countMatches("abba", "xxx"));
    }

    @Test
    void countMatchesChar() {
        assertEquals(0, StringUtils.countMatches(null, ' '));
        assertEquals(0, StringUtils.countMatches("", ' '));
        assertEquals(0, StringUtils.countMatches("abba", '0'));
        assertEquals(2, StringUtils.countMatches("abba", 'a'));
        assertEquals(2, StringUtils.countMatches("abba", 'b'));
        assertEquals(0, StringUtils.countMatches("abba", 'x'));
    }

    @Test
    void indexOfPa() {
        String seq = "AAINQK(1)LIETGER";
        IntArrayList indexList = StringUtils.indexOf(seq, "(");
        assertEquals(1, indexList.size());
        assertEquals(6, indexList.getInt(0));

        String seq2 = "_AANM(ox)LQQSGSK(me)NTGAK_";
        IntArrayList idxes2 = StringUtils.indexOf(seq2, "(");
        assertIterableEquals(idxes2, IntArrayList.of(5, 16));
    }

    @Test
    void indexOf() {
        String text = "AGCTTAGATAGC";
        String p = "AG";
        IntArrayList indexList = StringUtils.indexOf(text, p);
        assertIterableEquals(IntArrayList.of(0, 5, 9), indexList);
    }

    @Test
    void indexOfOverlap() {
        String text = "AGCAGCAT";
        String p = "AGCA";
        IntArrayList indexList = StringUtils.indexOf(text, p);
        assertIterableEquals(IntArrayList.of(0, 3), indexList);
    }

    @Test
    void testIndexOfEmpty() {
        String seq = "SEQUEENCE";
        IntArrayList ints = StringUtils.indexOf(seq, "?");
        assertNotNull(ints);
        assertEquals(0, ints.size());
    }

    @Test
    void indexOfChar() {
        String seq = "VQPVQPSQTSTYPGQGM(Oxidation)PTPK";
        IntArrayList indexes = StringUtils.indexOf(seq, '(');
        assertIterableEquals(List.of(17), indexes);
    }

    @Test
    void createJoiner() {
        Joiner joiner = StringUtils.createJoiner("; ").skipNulls();
        String str = joiner.join("Harry", null, "Ron", "Hermione");
        assertEquals("Harry; Ron; Hermione", str);
    }

    @Test
    void commonPrefix() {
        String s1 = "ACCATGT";
        String s2 = "ACCAGAC";
        String lcp = StringUtils.commonPrefix(s1, s2);
        assertEquals("ACCA", lcp);
    }


    @Test
    void toSuperscript() {
        assertEquals("Fe³⁺", StringUtils.toSuperscript("Fe3+"));
        assertEquals("H₂O", StringUtils.toSubscript("H2O"));
        assertEquals("CO₂", StringUtils.toSubscript("CO2"));
    }

    @Test
    void join() {
        List<String> a = List.of("a", "b", "c");
        String str = StringUtils.join(";", a);
        assertEquals("a;b;c", str);
    }

    @Test
    void split() {
        assertTrue(StringUtils.split("", ' ').isEmpty());
        assertIterableEquals(List.of("a", "b", "c"), StringUtils.split("a.b.c", '.'));
        assertIterableEquals(List.of("a", "b", "c"), StringUtils.split("a..b.c", '.'));
        assertIterableEquals(List.of("a:b:c"), StringUtils.split("a:b:c", '.'));
        assertIterableEquals(List.of("a", "b", "c"), StringUtils.split("a b c", ' '));
    }
}
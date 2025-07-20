package pdk.util.math;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 20 Jul 2025, 9:18 PM
 */
class CounterTest {

    @Test
    void getMode() {
        Counter<String> map = new Counter<>();
        List<String> mode = map.getMode();
        assertEquals(0, mode.size());

        map.increase("3");
        mode = map.getMode();
        assertEquals(1, mode.size());
        assertEquals("3", mode.get(0));

        map.increase("2");
        mode = map.getMode();
        mode.sort(Comparator.naturalOrder());
        assertEquals(2, mode.size());
        assertEquals("2", mode.get(0));
        assertEquals("3", mode.get(1));

        map.increase("2");
        mode = map.getMode();
        assertEquals(1, mode.size());
        assertEquals("2", mode.get(0));
        assertFalse(mode.contains("1"));
        assertTrue(mode.contains("2"));
    }
}
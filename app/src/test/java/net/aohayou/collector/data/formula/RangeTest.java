package net.aohayou.collector.data.formula;

import org.junit.Test;

import static org.junit.Assert.*;

public class RangeTest {

    @Test
    public void singleton_range() throws Exception {
        Range range = new Range(5, 5);

        assertTrue(range.contains(5));
        assertFalse(range.contains(4));
        assertFalse(range.contains(6));

        assertEquals(range.size(), 1);
    }

    @Test
    public void simple_range() throws Exception {
        Range range = new Range(-2, 6);

        assertTrue(range.contains(-2));
        assertTrue(range.contains(6));
        assertTrue(range.contains(0));
        assertTrue(range.contains(3));
        assertFalse(range.contains(-3));
        assertFalse(range.contains(7));

        assertEquals(range.size(), 9);
    }

    @Test (expected = RuntimeException.class)
    public void invalid_range() throws Exception {
        new Range(5, 4);
    }
}

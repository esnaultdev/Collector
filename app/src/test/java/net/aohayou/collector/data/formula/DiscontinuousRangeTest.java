package net.aohayou.collector.data.formula;

import org.junit.Test;

import static org.junit.Assert.*;

public class DiscontinuousRangeTest {

    @Test
    public void simple_range() throws Exception {
        Range range = new Range(1, 5);
        DiscontinuousRange disRange = new DiscontinuousRange(range);

        assertTrue(disRange.contains(1));
        assertTrue(disRange.contains(3));
        assertTrue(disRange.contains(5));
        assertFalse(disRange.contains(6));
        assertFalse(disRange.contains(0));

        assertTrue(disRange.contains(range));

        assertEquals(disRange.size(), 5);
    }

    @Test
    public void add() throws Exception {
        Range range1 = new Range(0, 2);
        Range range2 = new Range(5, 7);
        DiscontinuousRange disRange = new DiscontinuousRange()
                .add(range1)
                .add(range2);

        assertTrue(disRange.contains(range1));
        assertTrue(disRange.contains(range2));

        assertFalse(disRange.contains(3));
        assertFalse(disRange.contains(4));
        assertFalse(disRange.contains(-1));
        assertFalse(disRange.contains(8));

        assertEquals(disRange.size(), 6);
    }

    @Test
    public void add_bigger() throws Exception {
        DiscontinuousRange disRange = new DiscontinuousRange()
                .add(new Range(2, 3))
                .add(new Range(0, 6));

        assertEquals(disRange.size(), 7);
    }

    @Test
    public void remove() throws Exception {
        DiscontinuousRange disRange = new DiscontinuousRange()
                .add(new Range(0, 10))
                .remove(new Range(5, 7));

        assertTrue(disRange.contains(new Range(0, 4)));
        assertTrue(disRange.contains(new Range(8, 10)));
        assertFalse(disRange.contains(5));
        assertFalse(disRange.contains(6));
        assertFalse(disRange.contains(7));
        assertFalse(disRange.contains(-1));
        assertFalse(disRange.contains(11));

        assertEquals(disRange.size(), 8);
    }

    @Test
    public void remove_all() throws Exception {
        DiscontinuousRange disRange = new DiscontinuousRange()
                .add(new Range(0, 3))
                .remove(new Range(-2, 5));

        assertFalse(disRange.contains(new Range(0, 3)));
        assertEquals(disRange.size(), 0);
    }

    @Test
    public void remove_before_add() throws Exception {
        DiscontinuousRange disRange = new DiscontinuousRange()
                .remove(new Range(-2, 5))
                .add(new Range(0, 3));

        assertTrue(disRange.contains(new Range(0, 3)));
        assertEquals(disRange.size(), 4);
    }

    @Test
    public void add_discontinuous_range() throws Exception {
        DiscontinuousRange disRange1 = new DiscontinuousRange()
                .add(new Range(0, 1))
                .add(new Range(7, 10));

        DiscontinuousRange disRange2 = new DiscontinuousRange()
                .add(new Range(0, 4))
                .add(new Range(12, 20));

        DiscontinuousRange disRange = disRange1.add(disRange2);

        assertTrue(disRange.contains(new Range(0, 4)));
        assertTrue(disRange.contains(new Range(7, 10)));
        assertTrue(disRange.contains(new Range(12, 20)));
        assertFalse(disRange.contains(5));
        assertFalse(disRange.contains(6));
        assertFalse(disRange.contains(11));

        assertEquals(disRange.size(), 18);
    }

    @Test
    public void remove_discontinuous_range() throws Exception {
        DiscontinuousRange disRange1 = new DiscontinuousRange()
                .add(new Range(0, 5))
                .add(new Range(7, 10));

        DiscontinuousRange disRange2 = new DiscontinuousRange()
                .add(new Range(-2, 0))
                .add(new Range(4, 8))
                .add(new Range(10, 20));

        DiscontinuousRange disRange = disRange1.remove(disRange2);

        assertTrue(disRange.contains(new Range(1, 3)));
        assertTrue(disRange.contains(9));
        assertFalse(disRange.contains(0));
        assertFalse(disRange.contains(4));
        assertFalse(disRange.contains(8));
        assertFalse(disRange.contains(10));

        assertEquals(disRange.size(), 4);
    }
}

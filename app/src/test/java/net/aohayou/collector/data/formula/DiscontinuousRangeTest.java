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

        Range[] expected = {range};
        assertArrayEquals(disRange.toRangeArray(), expected);
    }

    @Test
    public void add() throws Exception {
        Range range1 = new Range(0, 2);
        Range range2 = new Range(5, 7);
        DiscontinuousRange disRange = new DiscontinuousRange()
                .add(range1)
                .add(range2);

        Range[] expected = {range1, range2};
        assertArrayEquals(disRange.toRangeArray(), expected);
    }

    @Test
    public void add_bigger() throws Exception {
        DiscontinuousRange disRange = new DiscontinuousRange()
                .add(new Range(2, 3))
                .add(new Range(0, 6));

        Range[] expected = {new Range(0, 6)};
        assertArrayEquals(disRange.toRangeArray(), expected);
    }

    @Test
    public void add_continuous() throws Exception {
        DiscontinuousRange disRange = new DiscontinuousRange()
                .add(new Range(0, 3))
                .add(new Range(4, 6));

        Range[] expected = {new Range(0, 6)};
        assertArrayEquals(disRange.toRangeArray(), expected);
    }

    @Test
    public void remove() throws Exception {
        DiscontinuousRange disRange = new DiscontinuousRange()
                .add(new Range(0, 10))
                .remove(new Range(5, 7));

        Range[] expected = {new Range(0, 4), new Range(8, 10)};
        assertArrayEquals(disRange.toRangeArray(), expected);
    }

    @Test
    public void remove_same() throws Exception {
        Range range = new Range(0, 10);
        DiscontinuousRange disRange = new DiscontinuousRange()
                .add(range)
                .remove(range);

        Range[] expected = {};
        assertArrayEquals(disRange.toRangeArray(), expected);
    }

    @Test
    public void remove_all() throws Exception {
        DiscontinuousRange disRange = new DiscontinuousRange()
                .add(new Range(0, 3))
                .remove(new Range(-2, 5));

        Range[] expected = {};
        assertArrayEquals(disRange.toRangeArray(), expected);
    }

    @Test
    public void remove_before_add() throws Exception {
        Range addRange = new Range(0, 3);
        DiscontinuousRange disRange = new DiscontinuousRange()
                .remove(new Range(-2, 5))
                .add(addRange);

        Range[] expected = {addRange};
        assertArrayEquals(disRange.toRangeArray(), expected);
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

        Range[] expected = {new Range(0, 4), new Range(7, 10), new Range(12, 20)};
        assertArrayEquals(disRange.toRangeArray(), expected);
        assertEquals(disRange.size(), 18);
    }

    @Test
    public void add_discontinuous_range_complete() throws Exception {
        DiscontinuousRange disRange1 = new DiscontinuousRange()
                .add(new Range(0, 20))
                .add(new Range(30, 50));

        DiscontinuousRange disRange2 = new DiscontinuousRange()
                .add(new Range(10, 35))
                .add(new Range(45, 60));

        DiscontinuousRange disRange = disRange1.add(disRange2);

        Range[] expected = {new Range(0, 60)};
        assertArrayEquals(disRange.toRangeArray(), expected);
        assertEquals(disRange.size(), 61);
    }

    @Test
    public void remove_discontinuous_range() throws Exception {
        DiscontinuousRange disRange1 = new DiscontinuousRange()
                .add(new Range(10, 15))
                .add(new Range(30, 50))
                .add(new Range(55, 60))
                .add(new Range(70, 100));

        DiscontinuousRange disRange2 = new DiscontinuousRange()
                .add(new Range(0, 5))
                .add(new Range(20, 30))
                .add(new Range(35, 40))
                .add(new Range(45, 75))
                .add(new Range(95, 105))
                .add(new Range(110, 120));

        DiscontinuousRange disRange = disRange1.remove(disRange2);

        Range[] expected = {
                new Range(10, 15),
                new Range(31, 34),
                new Range(41, 44),
                new Range(76, 94)
        };
        assertArrayEquals(disRange.toRangeArray(), expected);
    }
}

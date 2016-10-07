package net.aohayou.collector.data.formula;

import org.junit.Test;

import static net.aohayou.collector.data.formula.FormulaConverterTestConstants.*;
import static org.junit.Assert.*;

public class FormulaConverterTest {

    @Test
    public void simple_formula() throws Exception {
        DiscontinuousRange disRange = FormulaConverter.convert(simpleFormula());
        assertArrayEquals(disRange.toRangeArray(), expectedRangesSimpleFormula());
    }
}

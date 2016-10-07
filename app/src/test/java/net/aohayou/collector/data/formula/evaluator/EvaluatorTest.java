package net.aohayou.collector.data.formula.evaluator;

import net.aohayou.collector.data.formula.DiscontinuousRange;
import net.aohayou.collector.data.formula.Range;

import org.junit.Test;

import static net.aohayou.collector.data.formula.FormulaConverterTestConstants.*;
import static org.junit.Assert.*;

public class EvaluatorTest {

    @Test
    public void simple_formula() throws Exception {
        DiscontinuousRange disRange = Evaluator.evaluate(expectedNodesSimpleFormula());
        assertArrayEquals(disRange.toRangeArray(), expectedRangesSimpleFormula());
    }

    @Test
    public void empty_formula() throws Exception {
        DiscontinuousRange disRange = Evaluator.evaluate(null);
        assertArrayEquals(disRange.toRangeArray(), new Range[]{});
    }
}

package net.aohayou.collector.data.formula.evaluator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.aohayou.collector.data.formula.DiscontinuousRange;
import net.aohayou.collector.data.formula.InvalidFormulaException;
import net.aohayou.collector.data.formula.Range;
import net.aohayou.collector.data.formula.parser.AddOperator;
import net.aohayou.collector.data.formula.parser.Node;
import net.aohayou.collector.data.formula.parser.NumberNode;
import net.aohayou.collector.data.formula.parser.RemoveOperator;
import net.aohayou.collector.data.formula.parser.UntilOperator;

public class Evaluator {

    public static @NonNull DiscontinuousRange evaluate(@Nullable Node node)
            throws InvalidFormulaException {

        if (node instanceof UntilOperator) {
            UntilOperator operator = (UntilOperator) node;
            Range range = new Range(operator.getLeft().value, operator.getRight().value);
            return new DiscontinuousRange(range);

        } else if (node instanceof NumberNode) {
            NumberNode number = (NumberNode) node;
            return new DiscontinuousRange(new Range(number.value, number.value));

        } else if (node instanceof AddOperator) {
            AddOperator operator = (AddOperator) node;
            DiscontinuousRange left = evaluate(operator.left);
            DiscontinuousRange right = evaluate(operator.right);
            return left.mergeWith(right);

        } else if (node instanceof RemoveOperator) {
            RemoveOperator operator = (RemoveOperator) node;
            DiscontinuousRange left = evaluate(operator.left);
            DiscontinuousRange right = evaluate(operator.right);
            return left.removeFrom(right);

        } else {
            throw new InvalidFormulaException("Unknown node: " + node);
        }
    }
}

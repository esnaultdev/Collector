package net.aohayou.collector.data.formula;

import net.aohayou.collector.data.formula.parser.AddOperator;
import net.aohayou.collector.data.formula.parser.Node;
import net.aohayou.collector.data.formula.parser.NumberNode;
import net.aohayou.collector.data.formula.parser.RemoveOperator;
import net.aohayou.collector.data.formula.parser.UntilOperator;
import net.aohayou.collector.data.formula.tokenizer.AddToken;
import net.aohayou.collector.data.formula.tokenizer.NumberToken;
import net.aohayou.collector.data.formula.tokenizer.RemoveToken;
import net.aohayou.collector.data.formula.tokenizer.Token;
import net.aohayou.collector.data.formula.tokenizer.UntilToken;

public abstract class FormulaConverterTestConstants {

    /** Definition of a valid and simple formula */
    public static String simpleFormula() {
        return "1+2*4-3";
    }

    /** Same formula with whitespace */
    public static String simpleFormulaWithSpaces() {
        return " 1 +2  * 4 -3 ";
    }

    /** Tokens expected for the simple formula */
    public static Token[] expectedTokensSimpleFormula() {
        Token[] expected = {
                new NumberToken(1),
                new AddToken(),
                new NumberToken(2),
                new UntilToken(),
                new NumberToken(4),
                new RemoveToken(),
                new NumberToken(3),
        };
        return expected;
    }

    /** Nodes expected for the simple formula */
    public static Node expectedNodesSimpleFormula() {
        return new RemoveOperator(
                new AddOperator(
                        new NumberNode(1),
                        new UntilOperator(
                                new NumberNode(2),
                                new NumberNode(4)
                        )
                ),
                new NumberNode(3)
        );
    }
}

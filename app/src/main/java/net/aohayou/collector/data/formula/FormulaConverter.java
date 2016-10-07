package net.aohayou.collector.data.formula;

import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;

import net.aohayou.collector.data.formula.evaluator.Evaluator;
import net.aohayou.collector.data.formula.parser.Node;
import net.aohayou.collector.data.formula.parser.Parser;
import net.aohayou.collector.data.formula.tokenizer.Token;
import net.aohayou.collector.data.formula.tokenizer.Tokenizer;

import java.util.List;

public class FormulaConverter {

    public static DiscontinuousRange convert(@NonNull String formulaString)
            throws InvalidFormulaException {
        Preconditions.checkNotNull(formulaString);
        List<Token> tokens = Tokenizer.tokenize(formulaString);
        Node node = new Parser(tokens).parse();
        return Evaluator.evaluate(node);
    }
}

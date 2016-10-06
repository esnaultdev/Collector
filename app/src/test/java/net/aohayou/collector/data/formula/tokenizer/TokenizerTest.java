package net.aohayou.collector.data.formula.tokenizer;

import net.aohayou.collector.data.formula.InvalidFormulaException;

import org.junit.Test;

import java.util.List;

import static net.aohayou.collector.data.formula.FormulaConverterTestConstants.*;
import static org.junit.Assert.*;

public class TokenizerTest {

    @Test
    public void tokenizer_single_number() throws Exception {
        List<Token> tokens = Tokenizer.tokenize("123");
        Token[] expected = {new NumberToken(123)};
        assertArrayEquals(expected, tokens.toArray());
    }

    @Test
    public void tokenizer_simple_formula() throws Exception {
        List<Token> tokens = Tokenizer.tokenize(simpleFormula());
        Token[] expected = expectedTokensSimpleFormula();
        assertArrayEquals(expected, tokens.toArray());
    }

    @Test
    public void tokenizer_whitespace_simple_formula() throws Exception {
        List<Token> tokens = Tokenizer.tokenize(simpleFormulaWithSpaces());
        Token[] expected = expectedTokensSimpleFormula();
        assertArrayEquals(expected, tokens.toArray());
    }

    @Test(expected = InvalidFormulaException.class)
    public void tokenizer_unknown_character() throws Exception {
        Tokenizer.tokenize("1+2*a4-3");
    }
}
